package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente le contenu d'une page spécifique.
 * Elle correspond à :
 * {
 *   "Title": "...",
 *   "Text": ["...", "..."]
 * }
 */
public class MotdPageContent {
    private String title;
    private List<String> text;

    public static final BuilderCodec<MotdPageContent> CODEC = BuilderCodec.builder(MotdPageContent.class, MotdPageContent::new)
            .append(
                    // Clé "Title" (J'ai corrigé "Tilte" en "Title" pour l'anglais correct, mais vous pouvez remettre "Tilte" si nécessaire)
                    new KeyedCodec<>("Title", Codec.STRING),
                    (e, v) -> e.title = v,
                    e -> e.title
            )
            .append(
                    // Clé "Text". Codec.list(Codec.STRING) permet de gérer une liste de textes JSON ["a", "b"]
                    new KeyedCodec<>("Text", Codec.list(Codec.STRING)),
                    (e, v) -> e.text = v,
                    e -> e.text
            )
            .add()
            .build();

    public MotdPageContent() {
        this.title = "Titre par défaut";
        this.text = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getText() {
        return text;
    }
}