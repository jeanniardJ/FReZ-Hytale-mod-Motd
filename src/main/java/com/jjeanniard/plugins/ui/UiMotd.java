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
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.util.Config;
import com.jjeanniard.plugins.config.MotdConfig;

import javax.annotation.Nonnull;

// Cette classe gère l'affichage et la logique de l'interface utilisateur (UI) du Message du Jour (Motd).
// Elle hérite de InteractiveCustomUIPage, ce qui permet de créer des menus interactifs (boutons, etc.).
// <Motd.PageData> spécifie le type de données qui sera échangé entre le client (l'écran du joueur) et le serveur.
public class UiMotd extends InteractiveCustomUIPage<UiMotd.PageData> {

    // Le chemin vers le fichier de mise en page (layout) qui définit l'apparence visuelle (HTML/XML/JSON).
    public static final String LAYOUT = "Motd/Motd.ui";

    // Référence vers le joueur. On utilise PlayerRef dans l'ECS Hytale pour éviter de stocker l'objet Player lourd directement.
    private final PlayerRef playerRef;

    // Constructeur de la page.
    // C'est ici qu'on initialise la page pour un joueur spécifique.
    public UiMotd(@Nonnull PlayerRef playerRef, Config<MotdConfig> rulesMotd) {
        // Appel au constructeur parent avec :
        // 1. Le joueur cible.
        // 2. CustomPageLifetime.CanDismiss : Permet au joueur de fermer le menu (ex: avec Echap).
        // 3. PageData.CODEC : L'outil qui permet de traduire les données (clics) du format réseau vers Java.
        super(playerRef, CustomPageLifetime.CanDismiss, PageData.CODEC);
        this.playerRef = playerRef;
    }

    // Cette méthode construit l'interface à envoyer au client.
    // C'est ici qu'on définit quel fichier afficher et comment réagir aux interactions initiales.
    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd, // Permet d'envoyer des commandes d'affichage (charger un fichier UI).
            @Nonnull UIEventBuilder evt,   // Permet de lier des éléments de l'UI (boutons) à des événements serveur.
            @Nonnull Store<EntityStore> store
    ) {
        // On demande au client de charger le fichier visuel défini dans la constante LAYOUT.
        cmd.append(LAYOUT);

        for(pages: rulesMotd.get().getPages())

        // On attache un événement au bouton identifié par "#MonBouton" dans le fichier .ui.
        // CustomUIEventBindingType.Activating : L'événement se déclenche au clic (activation).
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#MonBouton",
                // On prépare les données à envoyer au serveur lors du clic.
                // Ici, on envoie une paire clé/valeur : "Action" = "clic".
                new EventData().append("Action", "clic"),
                false
        );

        // Lier le bouton Fermer
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BoutonFermer",
                new EventData().append("Action", "fermer"),
                false
        );
    }

    // Cette méthode est appelée quand le serveur reçoit une interaction du joueur (ex: un clic configuré ci-dessus).
    // Le paramètre 'data' contient les informations envoyées par le client, décodées grâce au CODEC.
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull PageData data
    ) {
        if("fermer".equals(data.action)){
            this.close();
        }
        // On vérifie si l'action reçue est bien celle attendue ("clic").
        if ("clic".equals(data.action)) {
            //TODO Ajouter tout les actions apres un clic, ouverture d'un onglet ou action realiser dans le jeu

        }
    }

    // Classe interne (POJO) qui sert de modèle pour les données échangées entre le client et le serveur.
    // Elle doit correspondre aux données envoyées dans 'EventData' lors du build.
    public static class PageData {
        private String action;
        // Le CODEC est un outil de sérialisation fourni par Hytale.
        // Il explique comment transformer cet objet Java en données réseau et inversement.
        public static final BuilderCodec<PageData> CODEC = BuilderCodec.builder(
                        PageData.class, PageData::new
                )
                // On définit un champ "Action" de type String (Texte).
                // (e, v) -> e.action = v : Le "Setter" (comment remplir l'objet quand on reçoit la donnée).
                // e -> e.action : Le "Getter" (comment lire la donnée pour l'envoyer).
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action)
                .add()
                .build();

        public PageData() {}
    }

}
