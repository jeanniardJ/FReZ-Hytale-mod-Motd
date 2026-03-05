package com.jjeanniard.plugins.config;

import com.google.gson.annotations.SerializedName;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;


/**
 * Cette classe représente le contenu d'une page de règles (Titre + Lignes de texte).
 * C'est un modèle de données (POJO) utilisé par la configuration.
 *
 * Structure JSON correspondante :
 * {
 *   "Title": "...",
 *   "Text": ["...", "..."]
 * }
 */
public class MotdPageContent {
    @SerializedName("Id")
    private String id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Summary")
    private String summary;

    @SerializedName("Text")
    private String[] text;

    // --- DÉFINITION DU CODEC ---
    // Le BuilderCodec permet de dire à Hytale comment transformer cette classe en JSON et inversement.
    public static final BuilderCodec<MotdPageContent> CODEC = BuilderCodec.builder(MotdPageContent.class, MotdPageContent::new)
            .append(
                    new KeyedCodec<>("Id", Codec.STRING),
                    (e, v) -> e.id = v,
                    e -> e.id
            )
            .add()
            .append(
                    new KeyedCodec<>("Title", Codec.STRING),
                    (e, v) -> e.title = v,
                    e -> e.title
            )
            .add()
            .append(
                    new KeyedCodec<>("Summary", Codec.STRING),
                    (e, v) -> e.summary = v,
                    e -> e.summary
            )
            .add()
            .append(
                    new KeyedCodec<>("Text", Codec.STRING_ARRAY),
                    (e, v) -> e.text = v,
                    e -> e.text
            )
            .add()
            .build();

    // Constructeur par défaut.
    // Il est utilisé par le Codec lors de la création de l'objet, mais aussi pour définir les valeurs par défaut
    // si le fichier de configuration n'existe pas encore.
    public MotdPageContent() {
        this.id = "rules";
        this.title = "Règles de développement";
        this.summary = "Bonnes pratiques générales et attentes du serveur.";
        this.text = new String[]{
                "1. Toujours commenter les sections complexes.",
                "2. Chaque zone doit rester compréhensible par un développeur junior."
        };
    }

    // --- GETTERS ---
    // Permettent de lire les valeurs depuis d'autres parties du plugin.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary != null ? summary : "";
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getText() {
        return text;
    }
}
