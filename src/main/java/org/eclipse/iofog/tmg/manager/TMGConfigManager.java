package org.eclipse.iofog.tmg.manager;

import org.eclipse.iofog.utils.TMGFileUtils;
import org.w3c.dom.Document;

import java.util.logging.Logger;

/**
 * Test Message Generator Configuration Manager.
 *
 * @author Eclipse ioFog { Iryna Laryionava, Pavel Kazlou, Sasha Yakovtseva }
 * @since 3/29/16.
 */
class TMGConfigManager {

    private static final Logger log = Logger.getLogger(TMGConfigManager.class.getName());

    private static final String CONFIG_FILE_SOURCE = "config.xml";
    private static final String DATA_MSG_INTERVAL_TAG_NAME = "datamessageinteval";
    private static final String CONTROL_MSG_INTERVAL_TAG_NAME = "controlmessageinterval";

    private static Document configFile;

    /**
     * Method retrieves value for interval at which Test Message Generator will send IOMessages to ioContainer.
     *
     * @return long
     */
    static long getDataMessageInterval(){
        try {
            return Long.valueOf(getConfigFile().getElementsByTagName(DATA_MSG_INTERVAL_TAG_NAME).item(0).getTextContent());
        } catch (Exception e) {
            log.info("Error retrieving Data Message Interval. Switching to use default value = 5000 milliseconds. Error: " + e);
            return 5000;
        }
    }

    /**
     * Method retrieves value for interval at which Test Message Generator will send New Configuration Signal to ioContainer.
     *
     * @return long
     */
    static long getControlMessageInterval(){
        try {
            return Long.valueOf(getConfigFile().getElementsByTagName(CONTROL_MSG_INTERVAL_TAG_NAME).item(0).getTextContent());
        } catch (Exception e) {
            log.info("Error retrieving Control Message Interval. Switching to use default value = 5000 milliseconds. Error: " + e);
            return 5000;
        }
    }

    private static Document getConfigFile(){
        if(configFile == null) {
            configFile = TMGFileUtils.getXMLDocument(CONFIG_FILE_SOURCE);
        }
        return configFile;
    }

}
