package com.jjeanniard.plugins;

// --- IMPORTS ---
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import com.jjeanniard.plugins.commands.OpenUICommand;
import com.jjeanniard.plugins.config.MotdConfig;
import com.jjeanniard.plugins.config.MotdPageContent;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.logging.Level;

/**
 * Classe principale du plugin Motd (Message Of The Day).
 */
public class Motd extends JavaPlugin {
    private static final String[] DEFAULT_PAGE_FILES = {"getting-started.json", "commands.json"};
    private static final String DATA_PAGES_FOLDER = "MotdPages";
    private static final String RESOURCE_PAGE_FOLDER = "/Jjeanniard_Motd/pages/";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Config<MotdConfig> rulesMotd;
    private final LinkedHashMap<String, MotdPageContent> motdPages = new LinkedHashMap<>();

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public Motd(@Nonnull JavaPluginInit init) {
        super(init);
        this.rulesMotd = this.withConfig("Motd", MotdConfig.CODEC);
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Plugin en cours de démarrage …");
        OpenUICommand helpCommand = new OpenUICommand(this);
        getCommandRegistry().registerCommand(helpCommand);
        LOGGER.atInfo().log("Commandes publiées.");
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Plugin démarré");

        Path configPath = getDataDirectory().resolve("Motd.json");
        if (!Files.exists(configPath)) {
            rulesMotd.save();
            LOGGER.atInfo().log("Fichier de configuration par défaut créé.");
        }

        Path dataPagesDir = getDataDirectory().resolve(DATA_PAGES_FOLDER);
        try {
            ensurePageDirectory(dataPagesDir);
        } catch (IOException e) {
            LOGGER.at(Level.WARNING).log("Impossible de créer le dossier des pages : {}", e.getMessage());
        }

        rebuildPageCache(dataPagesDir);
        logPages();
        LOGGER.atInfo().log("Fin du démarrage du plugin");
    }

    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Plugins shutdown!");
    }

    public List<MotdPageContent> getPages() {
        synchronized (motdPages) {
            return Collections.unmodifiableList(new ArrayList<>(motdPages.values()));
        }
    }

    public MotdPageContent getPageById(String id) {
        if (id == null) {
            return null;
        }
        synchronized (motdPages) {
            return motdPages.get(id);
        }
    }

    private void rebuildPageCache(Path dataPagesDir) {
        synchronized (motdPages) {
            motdPages.clear();
            MotdPageContent[] configPages = rulesMotd.get().getPages();
            if (configPages != null) {
                for (MotdPageContent page : configPages) {
                    addPageToCache(page);
                }
            }
            List<MotdPageContent> files = loadPagesFromDirectory(dataPagesDir);
            for (MotdPageContent page : files) {
                addPageToCache(page);
            }
        }
    }

    private void addPageToCache(MotdPageContent page) {
        if (page == null) {
            return;
        }
        String id = page.getId();
        if (id == null || id.isBlank()) {
            id = slugify(page.getTitle());
            page.setId(id);
        }
        motdPages.put(id, page);
    }

    private List<MotdPageContent> loadPagesFromDirectory(Path dir) {
        List<MotdPageContent> pages = new ArrayList<>();
        if (!Files.isDirectory(dir)) {
            return pages;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    MotdPageContent page = GSON.fromJson(reader, MotdPageContent.class);
                    if (page != null) {
                        pages.add(page);
                    }
                } catch (IOException | JsonSyntaxException e) {
                    LOGGER.at(Level.WARNING).log("Impossible de charger {} : {}", file.getFileName(), e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.at(Level.WARNING).log("Impossible de lister le dossier {} : {}", dir, e.getMessage());
        }
        return pages;
    }

    private void ensurePageDirectory(Path pagesDir) throws IOException {
        if (Files.notExists(pagesDir)) {
            Files.createDirectories(pagesDir);
        }
        for (String file : DEFAULT_PAGE_FILES) {
            Path target = pagesDir.resolve(file);
            if (Files.exists(target)) {
                continue;
            }
            try (InputStream resource = getClass().getResourceAsStream(RESOURCE_PAGE_FOLDER + file)) {
                if (resource == null) {
                    LOGGER.at(Level.WARNING).log("Ressource {} introuvable.", RESOURCE_PAGE_FOLDER + file);
                    continue;
                }
                Files.copy(resource, target);
            }
        }
    }

    private void logPages() {
        List<String> titles;
        synchronized (motdPages) {
            titles = motdPages.values().stream().map(MotdPageContent::getTitle).collect(Collectors.toList());
        }
        if (titles.isEmpty()) {
            LOGGER.atInfo().log("Aucune page d'aide chargée.");
        } else {
            LOGGER.atInfo().log("Pages d'aide chargées : {}", titles);
        }
    }

    private static String slugify(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return "page-" + UUID.randomUUID();
        }
        return candidate.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
    }

    public Config<MotdConfig> getConfig() {
        return rulesMotd;
    }
}
