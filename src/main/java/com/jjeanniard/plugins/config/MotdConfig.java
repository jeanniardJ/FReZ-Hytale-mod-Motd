package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;


/**
 * Exemple de fichier de configuration :
 * {
 *   "Motd": [
 *     {
 *       "Title": "Bienvenue Page 1",
 *       "Text": ["Ligne 1", "Ligne 2"]
 *     },
 *     {
 *       "Title": "Règles Page 2",
 *       "Text": ["Pas d'insultes", "Amusez-vous"]
 *     },
 *     {
 *       "Title": "Discord Page 3",
 *       "Text": ["Rejoignez-nous !"]
 *     }
 *   ]
 * }
 */
public class MotdConfig {
    // On utilise un tableau de MotdPageContent pour stocker nos pages complexes (Titre + Texte)
    private MotdPageContent[] pages;

    // Définition du CODEC qui permet de transformer l'objet Config en JSON (et inversement)
    public static BuilderCodec<MotdConfig> CODEC = BuilderCodec.builder(MotdConfig.class, MotdConfig::new)
            // Ajout d'un champ à sérialiser/désérialiser
            .append(
                    // Ici, on utilise ArrayCodec. C'est un CODEC spécial pour les tableaux.
                    // Paramètre 1 : MotdPageContent.CODEC -> Le décodeur pour UN élément de la liste.
                    // Paramètre 2 : MotdPageContent[]::new -> La méthode pour créer le tableau en mémoire.
                    new KeyedCodec<>("Motd", new ArrayCodec<>(MotdPageContent.CODEC, MotdPageContent[]::new)),
                    // Setter : Comment appliquer la valeur lue depuis le fichier (v) dans l'objet (e)
                    (e, v) -> e.pages = v,
                    // Getter : Comment récupérer la valeur depuis l'objet (e) pour l'écrire dans le fichier
                    e -> e.pages
            )
            // Valide l'ajout du champ précédent
            .add()
            // Construit l'instance finale du Codec
            .build();

    public MotdConfig() {
        // On initialise avec une page par défaut pour que le fichier soit créé avec du contenu
        this.pages = new MotdPageContent[] {
            new MotdPageContent()
        };
    }

    public MotdPageContent[] getPages() {
        return pages;
    }

    public void setPages(MotdPageContent[] pages) {
        this.pages = pages != null ? pages : new MotdPageContent[0];
    }
}
