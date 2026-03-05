package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.hypixel.hytale.server.core.util.Config;
import com.jjeanniard.plugins.config.MotdConfig;
import com.jjeanniard.plugins.ui.UiMotd;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public class OpenUICommand extends AbstractPlayerCommand {
    
    private final Config<MotdConfig> config;

    public OpenUICommand(Config<MotdConfig> config) {
        super("motd", "Ouvre la page Motd");
        //this.setPermissionGroup(GameMode.Adventure);
        this.config = config;
    }

    /**
     * Pour Desactiver les permissions sur cette commande
     * @return
     */
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        // Récupérer la config depuis le plugin
        MotdConfig config = this.config.get();
        // Passer la config à l'UI
        UiMotd page = new UiMotd(playerRef, config);

        if (player == null) {
            commandContext.sendMessage(Message.raw("Erreur : Vous devez être un joueur pour utiliser cette commande."));
            return;
        }

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
