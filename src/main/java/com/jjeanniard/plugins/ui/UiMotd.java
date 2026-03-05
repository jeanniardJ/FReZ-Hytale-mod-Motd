package com.jjeanniard.plugins.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jjeanniard.plugins.config.MotdConfig;
import com.jjeanniard.plugins.config.MotdPageContent;

import javax.annotation.Nonnull;

// Cette classe gère l'affichage et la logique de l'interface utilisateur (UI) du Message du Jour (Motd).
public class UiMotd extends InteractiveCustomUIPage<UiMotd.PageData> {

    public static final String LAYOUT = "Motd/Motd.ui";

    private final MotdConfig config; // Stockage de la configuration

    // Constructeur de la page.
    public UiMotd(@Nonnull PlayerRef playerRef, MotdConfig config) {
        super(playerRef, CustomPageLifetime.CanDismiss, PageData.CODEC);
        this.config = config;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        // Charger le layout de base
        cmd.append(LAYOUT);

//        for(MotdPageContent pages : config.getPages()){
//            cmd.set("#Motd.Titre", pages.getTitle());
//            for(String page: pages.getText()){
//                cmd.set("#Motd.Texte", page);
//            }
//        }
        cmd.append("#Motd", "Motd/Template.ui");
//        for (int i = 0; i < config.getPages().length; i++) {
//            String selecteur = "#Motd[" + i + "]";
//            cmd.append(selecteur, "Motd/Template.ui");
//            cmd.set(selecteur + ".Titre", config.getPages()[i].getTitle());
//            for (int j = 0; j < config.getPages()[i].getText().length; j++) {
//                cmd.set(selecteur + ".Texte[" + j + "]", config.getPages()[i].getText()[j]);
//            }
//
//            evt.addEventBinding(CustomUIEventBindingType.Activating, selecteur + ".BoutonFermer", new PageData().setAction("fermer"), false);
//        }

        // Lier le bouton Fermer
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BoutonFermer",
                new PageData().setAction("fermer"),
                false
        );
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull PageData data
    ) {
        if ("fermer".equals(data.action)) {
            this.close();
        }
    }

    // Classe interne pour les données d'événement
    public static class PageData {
        private String action;

        public static final BuilderCodec<PageData> CODEC = BuilderCodec.builder(
                        PageData.class, PageData::new
                )
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action)
                .add()
                .build();

        public PageData() {
        }

        // Méthode utilitaire pour le chaînage (Fluent API)
        public EventData setAction(String action) {
            this.action = action;
            return null;
        }
    }
}
