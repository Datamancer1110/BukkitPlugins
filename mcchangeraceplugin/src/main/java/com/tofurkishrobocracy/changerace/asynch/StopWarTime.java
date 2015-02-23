package com.tofurkishrobocracy.changerace.asynch;

import com.tofurkishrobocracy.changerace.ChangeRacePlugin;

public class StopWarTime implements Runnable {

    public void run() {
        ChangeRacePlugin.isWarTime = false;
        ChangeRacePlugin.instance.getServer().dispatchCommand(ChangeRacePlugin.instance.getServer().getConsoleSender(), "War's done!");
    }

}
