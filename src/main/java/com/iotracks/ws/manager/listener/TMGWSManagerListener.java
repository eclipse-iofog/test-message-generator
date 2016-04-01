package com.iotracks.ws.manager.listener;

import com.iotracks.tmg.manager.TMGMessageManager;
import com.iotracks.utils.elements.IOMessage;
import com.iotracks.utils.ByteUtils;
import com.iotracks.ws.manager.WebSocketManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.Arrays;

/**
 * Test Message Generator WebSocket Listener
 * Implementation of {@link WebSocketManagerListener}.
 * According to specification handles next transmissions' codes:
 * In case of receiving MESSAGE from ioContainer, Test Message Generator saves IOMessage with generate ID and timestamp
 * to receivedmessages.xml and responds to ioContainer with MESSAGE_RECEIPT response containing only generated ID and timestamp.
 **/
public class TMGWSManagerListener implements WebSocketManagerListener {

    @Override
    public void handle(WebSocketManager wsManager, BinaryWebSocketFrame frame, ChannelHandlerContext ctx) {
        ByteBuf content = frame.content();
        if(content.isReadable()) {
            Byte opcode = content.readByte();
            if (opcode == WebSocketManager.OPCODE_MSG.intValue()) {
                byte[] byteArray = new byte[content.readableBytes()];
                int readerIndex = content.readerIndex();
                content.getBytes(readerIndex, byteArray);
                int totalMsgLength = ByteUtils.bytesToInteger(Arrays.copyOfRange(byteArray, 0, 4));
                IOMessage message = new IOMessage(Arrays.copyOfRange(byteArray, 4, totalMsgLength));
                message.setId(generateID());
                message.setTimestamp(System.currentTimeMillis());
                TMGMessageManager.saveMessage(message);
                wsManager.sendReceipt(ctx, message.getId(), message.getTimestamp());
            }
        }
    }

    private String generateID(){
        return "IOMSID_" + (long)Math.floor(Math.random()*1000*1000);
    }
}
