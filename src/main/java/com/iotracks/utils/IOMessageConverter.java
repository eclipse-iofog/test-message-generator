package com.iotracks.utils;

import com.iotracks.tmg.manager.TMGMessageManager;
import com.iotracks.utils.elements.IOMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Converter for IOMessage to XML representation and backwards.
 *
 * Created by forte on 3/29/16.
 */
public class IOMessageConverter {

    /**
     * Method converts XML representation to IOMessage.
     *
     * @return IOMessage
     */
    public static IOMessage getMessageFromNode(Node xmlMessage) {
        IOMessage message = new IOMessage();
        NodeList properties = xmlMessage.getChildNodes();
        for(int i = 0; i < properties.getLength(); i++) {
            Node property = properties.item(i);
            if(property.getNodeName().equals(IOMessage.ID_FIELD_NAME)){
                message.setId(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.TAG_FIELD_NAME)){
                message.setTag(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.GROUP_ID_FIELD_NAME)){
                message.setGroupId(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.SEQUENCE_NUMBER_FIELD_NAME)){
                message.setSequenceNumber(Integer.valueOf(property.getTextContent()));
            } else if(property.getNodeName().equals(IOMessage.SEQUENCE_TOTAL_FIELD_NAME)){
                message.setSequenceTotal(Integer.valueOf(property.getTextContent()));
            } else if(property.getNodeName().equals(IOMessage.PRIORITY_FIELD_NAME)){
                message.setPriority((byte) Integer.valueOf(property.getTextContent()).intValue());
            } else if(property.getNodeName().equals(IOMessage.TIMESTAMP_FIELD_NAME)){
                message.setTimestamp(Long.valueOf(property.getTextContent()));
            } else if(property.getNodeName().equals(IOMessage.PUBLISHER_FIELD_NAME)){
                message.setPublisher(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.AUTH_ID_FIELD_NAME)){
                message.setAuthId(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.AUTH_GROUP_FIELD_NAME)){
                message.setAuthGroup(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.CHAIN_POSITION_FIELD_NAME)){
                message.setChainPosition(Long.valueOf(property.getTextContent()));
            } else if(property.getNodeName().equals(IOMessage.HASH_FIELD_NAME)){
                message.setHash(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.PREVIOUS_HASH_FIELD_NAME)){
                message.setPreviousHash(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.NONCE_FIELD_NAME)){
                message.setNonce(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.DIFFICULTY_TARGET_FIELD_NAME)){
                message.setDifficultyTarget(Integer.valueOf(property.getTextContent()));
            } else if(property.getNodeName().equals(IOMessage.INFO_TYPE_FIELD_NAME)){
                message.setInfoType(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.INFO_FORMAT_FIELD_NAME)){
                message.setInfoFormat(property.getTextContent());
            } else if(property.getNodeName().equals(IOMessage.CONTEXT_DATA_FIELD_NAME)){
                message.setContextData(property.getTextContent().getBytes());
            } else if(property.getNodeName().equals(IOMessage.CONTENT_DATA_FIELD_NAME)){
                message.setContentData(property.getTextContent().getBytes());
            }
        }
        return message;
    }

    /**
     * Method converts IOMessage to XML representation.
     *
     * @return Node
     */
    public static Node getElementFromMessage(IOMessage message, Document document) {
        Element ioMessage = document.createElement(TMGMessageManager.IO_MESSAGE_TAG_NAME);
        // ID
        appendMessageProperty(ioMessage, document, IOMessage.ID_FIELD_NAME, message.getId());
        //TAG
        appendMessageProperty(ioMessage, document, IOMessage.TAG_FIELD_NAME, message.getTag());
        //GROUP ID
        appendMessageProperty(ioMessage, document, IOMessage.GROUP_ID_FIELD_NAME, message.getGroupId());
        //SEQUENCE NUMBER
        appendMessageProperty(ioMessage, document, IOMessage.SEQUENCE_NUMBER_FIELD_NAME, message.getSequenceNumber().toString());
        //SEQUENCE TOTAL
        appendMessageProperty(ioMessage, document, IOMessage.SEQUENCE_TOTAL_FIELD_NAME, message.getSequenceTotal().toString());
        //PRIORITY
        appendMessageProperty(ioMessage, document, IOMessage.PRIORITY_FIELD_NAME, Byte.toString(message.getPriority()));
        //TIMESTAMP
        appendMessageProperty(ioMessage, document, IOMessage.TIMESTAMP_FIELD_NAME, String.valueOf(message.getTimestamp()));
        //PUBLISHER
        appendMessageProperty(ioMessage, document, IOMessage.PUBLISHER_FIELD_NAME, message.getPublisher());
        //AUTH ID
        appendMessageProperty(ioMessage, document, IOMessage.AUTH_ID_FIELD_NAME, message.getAuthId());
        //AUTH GROUP
        appendMessageProperty(ioMessage, document, IOMessage.AUTH_GROUP_FIELD_NAME, message.getAuthGroup());
        //VERSION
        appendMessageProperty(ioMessage, document, IOMessage.VERSION_FIELD_NAME, Integer.toString(message.getVersion()));
        //CHAIN POSITION
        appendMessageProperty(ioMessage, document, IOMessage.CHAIN_POSITION_FIELD_NAME, String.valueOf(message.getChainPosition()));
        //HASH
        appendMessageProperty(ioMessage, document, IOMessage.HASH_FIELD_NAME, message.getHash());
        //PREVIOUS HASH
        appendMessageProperty(ioMessage, document, IOMessage.PREVIOUS_HASH_FIELD_NAME, message.getPreviousHash());
        //NONCE
        appendMessageProperty(ioMessage, document, IOMessage.NONCE_FIELD_NAME, message.getNonce());
        //DIFFICULTY TARGET
        appendMessageProperty(ioMessage, document, IOMessage.DIFFICULTY_TARGET_FIELD_NAME, Integer.toString(message.getDifficultyTarget()));
        //INFO TYPE
        appendMessageProperty(ioMessage, document, IOMessage.INFO_TYPE_FIELD_NAME, message.getInfoType());
        //INFO FORMAT
        appendMessageProperty(ioMessage, document, IOMessage.INFO_FORMAT_FIELD_NAME, message.getInfoFormat());
        //CONTEXT DATA
        appendMessageProperty(ioMessage, document, IOMessage.CONTEXT_DATA_FIELD_NAME, new String(message.getContextData()));
        //CONTENT DATA
        appendMessageProperty(ioMessage, document, IOMessage.CONTENT_DATA_FIELD_NAME, new String(message.getContentData()));
        return ioMessage;
    }

    private static void appendMessageProperty(Element ioMessage, Document document, String tagName, String value){
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(value));
        ioMessage.appendChild(element);
    }


}
