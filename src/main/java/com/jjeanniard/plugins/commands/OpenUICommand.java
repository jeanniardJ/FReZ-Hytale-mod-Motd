package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jjeanniard.plugins.Motd;
import com.jjeanniard.plugins.ui.UiMotd;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public class OpenUICommand extends AbstractPlayerCommand {

    private final Motd plugin;

    public OpenUICommand(Motd plugin) {
        super("motd", "Ouvre le menu d'aide");
        this.plugin = plugin;
        this.setPermissionGroup(GameMode.Adventure);
        this.addAliases("info", "help");
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            commandContext.sendMessage(Message.raw("Erreur : vous devez être un joueur pour utiliser cette commande."));
            return;
        }

        UiMotd page = new UiMotd(playerRef, plugin);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
