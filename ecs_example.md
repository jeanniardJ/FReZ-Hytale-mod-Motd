# Exemple ECS Hytale : Système de Soif

Voici un exemple concret d'implémentation d'une mécanique de "Soif" utilisant l'architecture ECS de Hytale.

## 1. Le Composant (Data)
Le composant ne contient que des données. Ici, le niveau de soif actuel et le maximum.

```java
package com.monplugin.components;

import com.hypixel.hytale.server.core.ecs.Component;

public class ThirstComponent extends Component {
    private int currentThirst;
    private int maxThirst;

    public ThirstComponent(int maxThirst) {
        this.maxThirst = maxThirst;
        this.currentThirst = maxThirst;
    }

    public int getCurrentThirst() { return currentThirst; }
    public void setCurrentThirst(int thirst) { this.currentThirst = thirst; }
    public int getMaxThirst() { return maxThirst; }
}
```

## 2. Le Système (Logique)
Le système contient la logique. Il réduit la soif des entités au fil du temps.

```java
package com.monplugin.systems;

import com.hypixel.hytale.server.core.ecs.Entity;
import com.hypixel.hytale.server.core.ecs.System;
import com.monplugin.components.ThirstComponent;

public class ThirstSystem extends System {

    @Override
    public void tick(float delta) {
        // On itère sur toutes les entités qui ont le composant ThirstComponent
        for (Entity entity : getWorld().getEntitiesWith(ThirstComponent.class)) {
            ThirstComponent thirst = entity.get(ThirstComponent.class);
            
            // Logique : réduire la soif (exemple simplifié)
            if (thirst.getCurrentThirst() > 0) {
                thirst.setCurrentThirst(thirst.getCurrentThirst() - 1);
            } else {
                // Appliquer des dégâts ou autre effet si la soif est à 0
                // entity.damage(1); 
            }
        }
    }
}
```

## 3. Enregistrement (Main Class)
Il faut attacher le composant aux joueurs et enregistrer le système.

```java
// Dans votre classe principale (extends JavaPlugin)

@Override
protected void setup() {
    // Enregistrer le système dans le monde du serveur
    getServer().getWorld().addSystem(new ThirstSystem());
    
    // Écouter l'événement de connexion pour ajouter le composant au joueur
    getServer().getEventBus().register(PlayerJoinEvent.class, event -> {
        event.getPlayer().add(new ThirstComponent(100));
    });
}
```
