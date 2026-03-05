# Règles de Développement pour les Plugins Hytale

## Architecture : Entity Component System (ECS)

Le développement de plugins pour Hytale repose principalement sur l'architecture **Entity Component System (ECS)**. Il est essentiel de comprendre et d'adopter ce paradigme pour assurer la compatibilité, la performance et la maintenabilité des plugins.

### Principes de base :
*   **Entity (Entité)** : Un simple identifiant. Dans Hytale, cela peut être un joueur, un PNJ, un objet, etc.
*   **Component (Composant)** : Une brique de données pures. Ne contient aucune logique. Par exemple, un composant `Health` stocke les points de vie, un composant `Position` stocke les coordonnées.
*   **System (Système)** : Contient toute la logique. Les systèmes itèrent sur des ensembles d'entités qui possèdent des composants spécifiques pour appliquer des modifications. Par exemple, un `DamageSystem` pourrait traiter les entités ayant les composants `Health` et `DamageEvent`.

### Règle d'or :
**Séparez les données de la logique.** Vos composants doivent être de simples conteneurs de données, et vos systèmes doivent gérer tout le comportement. Évitez de créer des classes "monolithes" qui mélangent état et logique, car cela va à l'encontre du modèle ECS.

<!-- Inclusion de l'exemple ECS -->
@./ecs_example.md

## Structure du Projet

*   **Langage** : Le code source est écrit en **Java**.
*   **Emplacement** : Les fichiers sources se trouvent dans le répertoire `src/main/java/`.

## Dépendances

*   **Bibliothèque Hytale** : Le développement des plugins doit être réalisé en utilisant la bibliothèque Hytale.

## Documentation et Commentaires

*   **Commentez pour un Développeur Junior** : Le code doit être abondamment commenté pour être compréhensible par un développeur débutant. Expliquez chaque étape complexe, non seulement *ce que* le code fait, mais surtout *pourquoi* il le fait. L'objectif est de rendre le projet accessible et maintenable par n'importe qui, quel que soit son niveau d'expérience.
