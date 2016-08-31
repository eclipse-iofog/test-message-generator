package test;

import com.iotracks.tmg.manager.TMGMessageManager;
import com.iotracks.utils.elements.IOMessage;

public class Test {

    public static void main(String[] args) throws Exception {

        /*IOMessage m = new IOMessage();
        m.setId("sdkjhwrtiy8wrtgSDFOiuhsrgowh4tou");
        m.setTag("Bosch Camera 8798797");
        m.setGroupId("group1");
        m.setSequenceNumber(10);
        m.setSequenceTotal(100);
        m.setPriority((byte)5);
        m.setTimestamp(System.currentTimeMillis());
        m.setPublisher("UNKNOWN_IO_TRACKS_CONTEINER_UIID");
        m.setAuthId("auth");
        m.setAuthGroup("authgrp");
        m.setChainPosition(10);
        m.setHash("hashingggg");
        m.setPreviousHash("prevhashingggg");
        m.setNonce("nounceee");
        m.setDifficultyTarget(30);
        m.setInfoType("image/jpeg");
        m.setInfoFormat("base64");
        m.setContextData("gghh".getBytes());
        m.setContentData("sdkjhwrtiy8wrtgSDFOiuhsrgowh4touwsdhsDFDSKJhsdkljasjklweklfjwhefiauhw98p328testcounter".getBytes());

        byte [] bytes = m.getBytes();

        IOMessage decodedMessage = new IOMessage(bytes);*/

        byte[] bytes = TMGMessageManager.getRandomMessage().getBytes();

        System.out.println("done");

    }

}
