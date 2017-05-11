package com.iotracks.ws.manager.listener;

import com.iotracks.tmg.manager.TMGMessageManager;
import com.iotracks.utils.ByteUtils;
import com.iotracks.utils.IOMessageUtils;
import com.iotracks.utils.elements.IOMessage;
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
            if (opcode.equals(WebSocketManager.OPCODE_MSG)) {
                System.out.println("GOT MSG via SOCKET");
                byte[] byteArray = new byte[content.readableBytes()];
                int readerIndex = content.readerIndex();
                content.getBytes(readerIndex, byteArray);
                int totalMsgLength = ByteUtils.bytesToInteger(Arrays.copyOfRange(byteArray, 0, 4));
                IOMessage message = new IOMessage(Arrays.copyOfRange(byteArray, 4, totalMsgLength + 4));
                message.setId(IOMessageUtils.generateID());
                message.setTimestamp(System.currentTimeMillis());
//                System.out.println("Message: \n" + message.toString());
                TMGMessageManager.saveMessage(message);
                wsManager.sendReceipt(ctx, message.getId(), message.getTimestamp());
            }
        }
    }


}
