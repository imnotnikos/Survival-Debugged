package com.imNikos.debugstick;

import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalDebugStickPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new StickListener(this), this);
        
        GiveStickCommand giveCmd = new GiveStickCommand(this);
        getCommand("sdebug").setExecutor(giveCmd);
        getCommand("debugstick").setExecutor(giveCmd);
        
        getLogger().info("Lightweight CustomDebugStick enabled!");
    }
}