package com.iotracks.tmg.manager;

import com.iotracks.utils.TMGFileUtils;
import com.iotracks.ws.manager.handler.HttpRequestHandler;
import org.w3c.dom.Document;

import java.util.logging.Logger;

/**
 * Test Message Generator Configuration Manager.
 *
 * Created by forte on 3/29/16.
 */
public class TMGConfigManager {

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
    public static long getDataMessageInteval(){
        Long interval = Long.valueOf(getConfigFile().getElementsByTagName(DATA_MSG_INTERVAL_TAG_NAME).item(0).getTextContent());
        if (interval == null ) {
            log.info("Couldn't retrieve Data Message Interval. Using default value = 5000 milliseconds.");
            return 5000;
        }
        return interval;
    }

    /**
     * Method retrieves value for interval at which Test Message Generator will send New Configuration Signal to ioContainer.
     *
     * @return long
     */
    public static long getControlMessageInteval(){
        Long interval = Long.valueOf(getConfigFile().getElementsByTagName(CONTROL_MSG_INTERVAL_TAG_NAME).item(0).getTextContent());
        if (interval == null ) {
            log.info("Couldn't retrieve Control Message Interval. Using default value = 5000 milliseconds.");
            return 5000;
        }
        return interval;
    }

    private static Document getConfigFile(){
        if(configFile == null) {
            configFile = TMGFileUtils.getXMLDocument(CONFIG_FILE_SOURCE);
        }
        return configFile;
    }

}
