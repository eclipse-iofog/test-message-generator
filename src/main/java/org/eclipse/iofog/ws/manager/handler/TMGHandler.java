/*
 * *******************************************************************************
 *   Copyright (c) 2018 Edgeworx, Inc.
 *
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License v. 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0
 *
 *   SPDX-License-Identifier: EPL-2.0
 * *******************************************************************************
 */

package org.eclipse.iofog.ws.manager.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.eclipse.iofog.tmg.manager.ScheduleSender;
import org.eclipse.iofog.utils.elements.LocalAPIURLType;
import org.eclipse.iofog.ws.manager.WebSocketManager;
import org.eclipse.iofog.ws.manager.listener.TMGWSManagerListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Test Message Generator Handler mimics all ioFog's API calls.
 */
public class TMGHandler extends SimpleChannelInboundHandler {

    private final EventExecutorGroup executor;
    private final WebSocketManager wsManager;
    private final boolean ssl;

    private ScheduleSender schSender;

    public TMGHandler(boolean ssl, EventExecutorGroup executor) {
        super(false);
        this.ssl = ssl;
        wsManager = new WebSocketManager(new TMGWSManagerListener());
        this.executor = executor;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            wsManager.eatFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        LocalAPIURLType urlType = LocalAPIURLType.getByUrl(request.getUri());
        if (urlType != null) {
            runTask(new HttpRequestHandler(request, ctx.alloc().buffer(), urlType), ctx, request);
        } else {
            String uri = request.getUri();
            uri = uri.substring(1);
            String[] tokens = uri.split("/");
            String url = "/" + tokens[0] + "/" + tokens[1] + "/" + tokens[2];
            String id = tokens[4].trim();
            if (url.equals(LocalAPIURLType.GET_CONTROL_WEB_SOCKET_LOCAL_API.getURL())) {
                wsManager.initControlSocket(ctx, id, ssl, url, request);
            } else if (url.equals(LocalAPIURLType.GET_MSG_WEB_SOCKET_LOCAL_API.getURL())) {
                wsManager.initMessageSocket(ctx, id, ssl, url, request);
            }
            if (schSender == null) {
                schSender = new ScheduleSender(id, wsManager);
            }
            schSender.start();
        }

    }

    private void runTask(Callable<?> callable, ChannelHandlerContext ctx, FullHttpRequest req) {
        final Future<?> future = executor.submit(callable);
        future.addListener((GenericFutureListener<Future<Object>>) future1 -> {
            if (future1.isSuccess()) {
                try {
                    sendHttpResponse(ctx, req, (FullHttpResponse) future1.get());
                } catch (InterruptedException | ExecutionException e) {
                    ctx.fireExceptionCaught(e);
                    ctx.close();
                }
            } else {
                ctx.fireExceptionCaught(future1.cause());
                ctx.close();
            }
        });
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (schSender != null) {
            schSender.stop();
        }
        super.exceptionCaught(ctx, cause);
    }

}

