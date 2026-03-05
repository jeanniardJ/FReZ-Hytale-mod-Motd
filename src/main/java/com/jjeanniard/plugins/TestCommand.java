package com.jjeanniard.plugins;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.jjeanniard.plugins.config.MotdConfig;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class TestCommand extends AbstractPlayerCommand {
    //private final Config<MotdConfig> rulesMotd;

    public TestCommand(Config<MotdConfig> rulesMotd) {
        super("test", "test");
        this.setPermissionGroup(GameMode.Adventure);
        //this.rulesMotd = rulesMotd;
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        commandContext.sendMessage(Message.raw("Hello"));
    }
}
