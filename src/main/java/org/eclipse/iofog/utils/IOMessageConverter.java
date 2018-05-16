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

package org.eclipse.iofog.utils;

import org.eclipse.iofog.tmg.manager.TMGMessageManager;
import org.eclipse.iofog.utils.elements.IOMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Converter for IOMessage to XML representation and backwards.
 *
 * @author Eclipse ioFog { Iryna Laryionava, Pavel Kazlou, Sasha Yakovtseva }
 * @since 3/29/16.
 */
public class IOMessageConverter {

    /**
     * Method converts XML representation to IOMessage.
     *
     * @return IOMessage
     */
    public static IOMessage getMessageFromNode(Node xmlMessage) throws NumberFormatException {
        IOMessage message = new IOMessage();
        NodeList properties = xmlMessage.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++) {
            Node property = properties.item(i);
            String nodeValue = property.getTextContent();
            switch (property.getNodeName()) {
                case IOMessage.ID_FIELD_NAME:
                    message.setId(nodeValue);
                    break;
                case IOMessage.TAG_FIELD_NAME:
                    message.setTag(nodeValue);
                    break;
                case IOMessage.GROUP_ID_FIELD_NAME:
                    message.setGroupId(nodeValue);
                    break;
                case IOMessage.SEQUENCE_NUMBER_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setSequenceNumber(Integer.parseInt(nodeValue));
                    }
                    break;
                case IOMessage.SEQUENCE_TOTAL_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setSequenceTotal(Integer.parseInt(nodeValue));
                    }
                    break;
                case IOMessage.PRIORITY_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setPriority((byte) Integer.parseInt(nodeValue));
                    }
                    break;
                case IOMessage.TIMESTAMP_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setTimestamp(Long.parseLong(nodeValue));
                    }
                    break;
                case IOMessage.PUBLISHER_FIELD_NAME:
                    message.setPublisher(nodeValue);
                    break;
                case IOMessage.AUTH_ID_FIELD_NAME:
                    message.setAuthId(nodeValue);
                    break;
                case IOMessage.AUTH_GROUP_FIELD_NAME:
                    message.setAuthGroup(nodeValue);
                    break;
                case IOMessage.CHAIN_POSITION_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setChainPosition(Long.parseLong(nodeValue));
                    }
                    break;
                case IOMessage.HASH_FIELD_NAME:
                    message.setHash(nodeValue);
                    break;
                case IOMessage.PREVIOUS_HASH_FIELD_NAME:
                    message.setPreviousHash(nodeValue);
                    break;
                case IOMessage.NONCE_FIELD_NAME:
                    message.setNonce(nodeValue);
                    break;
                case IOMessage.DIFFICULTY_TARGET_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setDifficultyTarget(Integer.parseInt(nodeValue));
                    }
                    break;
                case IOMessage.INFO_TYPE_FIELD_NAME:
                    message.setInfoType(nodeValue);
                    break;
                case IOMessage.INFO_FORMAT_FIELD_NAME:
                    message.setInfoFormat(nodeValue);
                    break;
                case IOMessage.CONTEXT_DATA_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setContextData(nodeValue.getBytes());
                    }
                    break;
                case IOMessage.CONTENT_DATA_FIELD_NAME:
                    if (nodeValue != null) {
                        message.setContentData(nodeValue.getBytes());
                    }
                    break;

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
        appendMessageProperty(ioMessage, document,
                              IOMessage.ID_FIELD_NAME, message.getId());
        appendMessageProperty(ioMessage, document,
                              IOMessage.TAG_FIELD_NAME, message.getTag());
        appendMessageProperty(ioMessage, document,
                              IOMessage.GROUP_ID_FIELD_NAME, message.getGroupId());
        appendMessageProperty(ioMessage, document,
                              IOMessage.SEQUENCE_NUMBER_FIELD_NAME, String.valueOf(message.getSequenceNumber()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.SEQUENCE_TOTAL_FIELD_NAME, String.valueOf(message.getSequenceTotal()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.PRIORITY_FIELD_NAME, String.valueOf(message.getPriority()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.TIMESTAMP_FIELD_NAME, String.valueOf(message.getTimestamp()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.PUBLISHER_FIELD_NAME, message.getPublisher());
        appendMessageProperty(ioMessage, document,
                              IOMessage.AUTH_ID_FIELD_NAME, message.getAuthId());
        appendMessageProperty(ioMessage, document,
                              IOMessage.AUTH_GROUP_FIELD_NAME, message.getAuthGroup());
        appendMessageProperty(ioMessage, document,
                              IOMessage.VERSION_FIELD_NAME, String.valueOf(message.getVersion()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.CHAIN_POSITION_FIELD_NAME, String.valueOf(message.getChainPosition()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.HASH_FIELD_NAME, message.getHash());
        appendMessageProperty(ioMessage, document,
                              IOMessage.PREVIOUS_HASH_FIELD_NAME, message.getPreviousHash());
        appendMessageProperty(ioMessage, document,
                              IOMessage.NONCE_FIELD_NAME, message.getNonce());
        appendMessageProperty(ioMessage, document,
                              IOMessage.DIFFICULTY_TARGET_FIELD_NAME, String.valueOf(message.getDifficultyTarget()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.INFO_TYPE_FIELD_NAME, message.getInfoType());
        appendMessageProperty(ioMessage, document,
                              IOMessage.INFO_FORMAT_FIELD_NAME, message.getInfoFormat());
        appendMessageProperty(ioMessage, document,
                              IOMessage.CONTEXT_DATA_FIELD_NAME, new String((message.getContextData() == null) ?
                                                                            ByteUtils.stringToBytes("") :
                                                                            message.getContextData()));
        appendMessageProperty(ioMessage, document,
                              IOMessage.CONTENT_DATA_FIELD_NAME, new String((message.getContentData() == null) ?
                                                                            ByteUtils.stringToBytes("") :
                                                                            message.getContentData()));
        return ioMessage;
    }

    private static void appendMessageProperty(Element ioMessage, Document document, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(value));
        ioMessage.appendChild(element);
    }


}
