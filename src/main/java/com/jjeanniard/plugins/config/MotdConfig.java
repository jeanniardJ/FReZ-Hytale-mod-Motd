package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MotdConfig {
    private String motd;

    public MotdConfig() {
        super();
    }

    // Définition du CODEC qui permet de transformer l'objet Config en JSON (et inversement)
    public static BuilderCodec<Config> CODEC = BuilderCodec.builder(Config.class, Config::new)
            // Ajout d'un champ à sérialiser/désérialiser
            .append(
                    // Définit la clé "Motd" dans le fichier de config et le type de donnée (String)
                    new KeyedCodec<>("Motd", Codec.STRING),
                    // Setter : Comment appliquer la valeur lue depuis le fichier (v) dans l'objet (e)
                    (e, v) -> e.motd = v,
                    // Getter : Comment récupérer la valeur depuis l'objet (e) pour l'écrire dans le fichier
                    e -> e.motd
            )
            // Valide l'ajout du champ précédent
            .add()
            // Construit l'instance finale du Codec
            .build();

    // Ajout d'un getter pour pouvoir utiliser la config ailleurs
    public String getMotd() {
        return motd;
    }
}
