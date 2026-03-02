package com.jjeanniard.plugins.motd.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.jjeanniard.plugins.motd.ui.Motd;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;


public class OpenUICommand extends AbstractPlayerCommand {

    public OpenUICommand() {
        super("motd", "Ouvre la page Motd");

    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            commandContext.sendMessage(Message.raw("Erreur : Vous devez être un joueur pour utiliser cette commande."));
            return;
        }

        Motd page = new Motd(playerRef);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
