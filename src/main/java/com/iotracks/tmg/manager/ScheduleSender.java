package com.iotracks.tmg.manager;


import com.iotracks.ws.manager.WebSocketManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleSender {

    private ScheduledExecutorService mScheduler;

    private long mMessInt;
    private long mConfInt;
    private WebSocketManager mWsManager;
    private String mContId;


    public ScheduleSender(String pContId, WebSocketManager pWsManager) {
        mMessInt = TMGConfigManager.getDataMessageInterval();
        mConfInt = TMGConfigManager.getControlMessageInterval();
        mWsManager = pWsManager;
        mContId = pContId;
        mScheduler = Executors.newScheduledThreadPool(2);
    }

    public void start() {
        mScheduler.scheduleWithFixedDelay(new MessageSender(), 0, mMessInt, TimeUnit.MILLISECONDS);
        mScheduler.scheduleWithFixedDelay(new ConfigSender(), 0, mConfInt, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        mScheduler.shutdown();
    }

    private class MessageSender implements Runnable {

        @Override
        public void run() {
            System.out.println("Sending message");
            mWsManager.sendMessage(mContId, TMGMessageManager.getRandomMessage().toBytes());
        }
    }

    private class ConfigSender implements Runnable {

        @Override
        public void run() {
            System.out.println("Sending control signal");
            mWsManager.sendControl(mContId);
        }
    }


}
