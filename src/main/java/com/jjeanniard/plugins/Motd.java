package com.jjeanniard.plugins;

// --- IMPORTS ---
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import com.jjeanniard.plugins.config.MotdConfig;
import com.jjeanniard.plugins.config.MotdPageContent;
import com.jjeanniard.plugins.commands.OpenUICommand;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe principale du plugin Motd (Message Of The Day).
 */
public class Motd extends JavaPlugin {
    // --- PROPRIÉTÉS (VARIABLES) DU PLUGIN ---

    private final Config<MotdConfig> rulesMotd;

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    /**
     * Constructeur du plugin.
     */
    public Motd(@Nonnull JavaPluginInit init) {
        super(init);
        this.rulesMotd = this.withConfig("Motd", MotdConfig.CODEC);
    }

    /**
     * Enregistrer commandes, evenements, assets
     */
    @Override
    protected void setup() {
        // Affichage de debug dans la console
        LOGGER.atInfo().log("Plugin en cour de démarrage ...");
        // On enregistre notre commande en passant l'instance du plugin
        getCommandRegistry().registerCommand(new OpenUICommand(this.rulesMotd));
        getCommandRegistry().registerCommand(new TestCommand(this.rulesMotd));
        LOGGER.atInfo().log("Plugins setup!");
    }

    /**
     * Demarrer la logique de jeu, interagir avec d'autres plugins
     */
    @Override
    protected void start() {
        LOGGER.atInfo().log("Plugin démarré");

        // Sauvegarde la configuration par défaut si le fichier n'existe pas
        Path configPath = getDataDirectory().resolve("Motd.json");

        if (!Files.exists(configPath)) {
            rulesMotd.save();
            LOGGER.atInfo().log("Fichier de configuration par défaut créé.");
        }

        // Affichage de debug dans la console
        for (MotdPageContent pages : rulesMotd.get().getPages()){
            LOGGER.atInfo().log(pages.getTitle());
            for (String text : pages.getText()){
                LOGGER.atInfo().log(text);
            }
            LOGGER.atInfo().log("----------------");
        }

        LOGGER.atInfo().log("Fin du démarrage du plugin");
    }

    /**
     * 	Nettoyer les ressources, sauvegarder les donnees
     */
    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Plugins shutdown!");
    }

    // Getter public pour accéder à la configuration depuis d'autres classes
    public Config<MotdConfig> getConfig() {
        return rulesMotd;
    }
}
