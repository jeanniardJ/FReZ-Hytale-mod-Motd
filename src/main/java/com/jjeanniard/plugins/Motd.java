package com.jjeanniard.plugins;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import com.jjeanniard.plugins.commands.OpenUICommand;

import javax.annotation.Nonnull;


public class MotdPlugin extends JavaPlugin {

    public MotdPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // TODO: Implement setup
    }

    @Override
    protected void start() {
        // TODO: Implement start
        getCommandRegistry().registerCommand(new OpenUICommand());

    }

    @Override
    protected void shutdown() {
        // TODO: Implement shutdown
    }
}
