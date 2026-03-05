package com.jjeanniard.plugins.config;

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
    // Le titre de la page (ex: "Règles du serveur")
    private String title;
    // Les lignes de texte de la page (tableau de chaînes de caractères)
    private String[] text;

    // --- DÉFINITION DU CODEC ---
    // Le BuilderCodec permet de dire à Hytale comment transformer cette classe en JSON et inversement.
    public static final BuilderCodec<MotdPageContent> CODEC = BuilderCodec.builder(MotdPageContent.class, MotdPageContent::new)
            .append(
                    // 1. Gestion du Titre
                    // On associe la clé JSON "Title" à un Codec de type String (Texte).
                    new KeyedCodec<>("Title", Codec.STRING),
                    // Setter : Quand on lit le JSON, on met la valeur (v) dans le champ 'title' de l'objet (e)
                    (e, v) -> e.title = v,
                    // Getter : Quand on écrit le JSON, on récupère la valeur du champ 'title' de l'objet (e)
                    e -> e.title
            )
            .add()
            .append(
                    // 2. Gestion du Texte
                    // On associe la clé JSON "Text" à un Codec de type Tableau de String (STRING_ARRAY).
                    new KeyedCodec<>("Text", Codec.STRING_ARRAY),
                    // Setter : On remplit le tableau 'text' avec la valeur lue (v)
                    (e, v) -> e.text = v,
                    // Getter : On lit le tableau 'text' pour le sauvegarder
                    e -> e.text
            )
            .add()
            .build();

    // Constructeur par défaut.
    // Il est utilisé par le Codec lors de la création de l'objet, mais aussi pour définir les valeurs par défaut
    // si le fichier de configuration n'existe pas encore.
    public MotdPageContent() {
        this.title = "Règles de Développement";
        this.text = new String[]{
                "1. Toujours commenter le code.",
                "2. Il faut commenter le code pour un dev junior dans tout le projet."
        };
    }

    // --- GETTERS ---
    // Permettent de lire les valeurs depuis d'autres parties du plugin.

    public String getTitle() {
        return title;
    }

    public String[] getText() {
        return text;
    }
}