# Règles de Développement pour les Plugins Hytale

## Architecture : Entity Component System (ECS)

Le développement de plugins pour Hytale repose principalement sur l'architecture **Entity Component System (ECS)**. Il est essentiel de comprendre et d'adopter ce paradigme pour assurer la compatibilité, la performance et la maintenabilité des plugins.

### Principes de base :
*   **Entity (Entité)** : Un simple identifiant. Dans Hytale, cela peut être un joueur, un PNJ, un objet, etc.
*   **Component (Composant)** : Une brique de données pures. Ne contient aucune logique. Par exemple, un composant `Health` stocke les points de vie, un composant `Position` stocke les coordonnées.
*   **System (Système)** : Contient toute la logique. Les systèmes itèrent sur des ensembles d'entités qui possèdent des composants spécifiques pour appliquer des modifications. Par exemple, un `DamageSystem` pourrait traiter les entités ayant les composants `Health` et `DamageEvent`.

### Règle d'or :
**Séparez les données de la logique.** Vos composants doivent être de simples conteneurs de données, et vos systèmes doivent gérer tout le comportement. Évitez de créer des classes "monolithes" qui mélangent état et logique, car cela va à l'encontre du modèle ECS.
