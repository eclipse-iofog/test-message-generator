package org.eclipse.iofog.ws.manager.listener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.eclipse.iofog.ws.manager.WebSocketManager;

/**
 * Interface of listener for WebSocket Manager.
 *
 * @author Eclipse ioFog { Iryna Laryionava, Pavel Kazlou, Sasha Yakovtseva }
 * @since 3/30/16.
 */
public interface WebSocketManagerListener {

    /**
     * Method that handles binary data calls (BinaryWebSocketFrame).
     *
     * @param wsManager - WebSocket manager
     * @param frame - binary frame to process
     * @param ctx - current channel handler context
     *
     */
    void handle(WebSocketManager wsManager, BinaryWebSocketFrame frame, ChannelHandlerContext ctx);
}
