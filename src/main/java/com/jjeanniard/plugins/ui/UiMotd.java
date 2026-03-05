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
import com.jjeanniard.plugins.Motd;
import com.jjeanniard.plugins.config.MotdPageContent;

import javax.annotation.Nonnull;
import java.util.List;

// Cette classe gère l'affichage et la logique de l'interface utilisateur (UI) du menu d'aide Motd.
public class UiMotd extends InteractiveCustomUIPage<UiMotd.PageData> {

    public static final String LAYOUT = "Motd/Motd.ui";
    private static final String SUMMARY_ENTRY = "Motd/SummaryEntry.ui";
    private static final String DETAIL_LINE = "Motd/DetailLine.ui";

    private final Motd plugin;
    private String selectedPageId;

    public UiMotd(@Nonnull PlayerRef playerRef, Motd plugin) {
        super(playerRef, CustomPageLifetime.CanDismiss, PageData.CODEC);
        this.plugin = plugin;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        cmd.append(LAYOUT);
        List<MotdPageContent> pages = plugin.getPages();

        if (pages.isEmpty()) {
            cmd.set("#SommaireVide.Text", "Aucune section definie pour le moment.");
        } else {
            cmd.set("#SommaireVide.Text", "");
            for (int i = 0; i < pages.size(); i++) {
                MotdPageContent page = pages.get(i);
                String entrySelector = "#SommaireList[" + i + "]";
                cmd.append("#SommaireList", SUMMARY_ENTRY);
                cmd.set(entrySelector + ".BoutonSection.Text", page.getTitle());
                cmd.set(entrySelector + ".BoutonSection.Tooltip", page.getSummary());
                evt.addEventBinding(
                        CustomUIEventBindingType.Activating,
                        entrySelector + ".BoutonSection",
                        new PageData().setAction("open").setPageId(page.getId()).toEventData(),
                        false
                );
            }
        }

        if (selectedPageId == null) {
            cmd.set("#DetailsTitre.Text", "Sommaire");
            cmd.set("#DetailsResume.Text", "Choisissez une section dans la colonne de gauche pour voir les détails.");
            cmd.set("#DetailsEmpty.Text", pages.isEmpty() ? "Il n'y a pas encore de page importée." : "Sélectionnez une section.");
        } else {
            MotdPageContent chosen = plugin.getPageById(selectedPageId);
            if (chosen == null) {
                cmd.set("#DetailsTitre.Text", "Page introuvable");
                cmd.set("#DetailsResume.Text", "");
                cmd.set("#DetailsEmpty.Text", "La page demandée a été supprimée.");
            } else {
                cmd.set("#DetailsTitre.Text", chosen.getTitle());
                cmd.set("#DetailsResume.Text", chosen.getSummary());
                String[] body = chosen.getText();
                if (body != null && body.length > 0) {
                    for (int line = 0; line < body.length; line++) {
                        String detailSelector = "#DetailsBody[" + line + "]";
                        cmd.append("#DetailsBody", DETAIL_LINE);
                        cmd.set(detailSelector + ".Ligne.Text", body[line]);
                    }
                    cmd.set("#DetailsEmpty.Text", "");
                } else {
                    cmd.set("#DetailsEmpty.Text", "Aucun contenu n'a été défini pour cette section.");
                }
            }
        }

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BoutonRetour",
                new PageData().setAction("back").toEventData(),
                false
        );

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BoutonFermer",
                new PageData().setAction("fermer").toEventData(),
                false
        );
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull PageData data
    ) {
        if (data == null) {
            return;
        }
        String action = data.getAction();
        if ("fermer".equals(action)) {
            this.close();
            return;
        }
        if ("back".equals(action)) {
            this.selectedPageId = null;
            rebuild();
            return;
        }
        if ("open".equals(action)) {
            String pageId = data.getPageId();
            if (pageId != null) {
                this.selectedPageId = pageId;
                rebuild();
            }
        }
    }

    public static class PageData {
        private String action;
        private String pageId;

        public static final BuilderCodec<PageData> CODEC = BuilderCodec.builder(PageData.class, PageData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action)
                .add()
                .append(new KeyedCodec<>("PageId", Codec.STRING), (e, v) -> e.pageId = v, e -> e.pageId)
                .add()
                .build();

        public PageData() {
        }

        public String getAction() {
            return action;
        }

        public String getPageId() {
            return pageId;
        }

        public PageData setAction(String action) {
            this.action = action;
            return this;
        }

        public PageData setPageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        public EventData toEventData() {
            EventData data = new EventData();
            if (action != null) {
                data.put("Action", action);
            }
            if (pageId != null) {
                data.put("PageId", pageId);
            }
            return data;
        }
    }
}
