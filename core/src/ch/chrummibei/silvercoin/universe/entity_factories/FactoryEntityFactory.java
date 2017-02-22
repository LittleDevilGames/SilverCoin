package ch.chrummibei.silvercoin.universe.entity_factories;

import ch.chrummibei.silvercoin.config.UniverseConfig;
import ch.chrummibei.silvercoin.universe.components.*;
import ch.chrummibei.silvercoin.universe.credit.Price;
import ch.chrummibei.silvercoin.universe.entity_systems.FactorySystem;
import ch.chrummibei.silvercoin.universe.entity_systems.TraderSystem;
import ch.chrummibei.silvercoin.universe.item.Recipe;
import ch.chrummibei.silvercoin.universe.position.PricedItemPosition;
import ch.chrummibei.silvercoin.universe.position.YieldingItemPosition;
import com.badlogic.ashley.core.Entity;

/**
 * Created by brachiel on 21/02/2017.
 */
public class FactoryEntityFactory {
    public static int factorySequence = 0;

    public static Entity FactoryEntity(UniverseConfig universeConfig, MarketComponent market, Recipe recipe) {
        Entity entity = new Entity();

        FactoryComponent factory = new FactoryComponent(recipe,
                universeConfig.factory().getRandomInt("goalStock"),
                universeConfig.factory().getRandomDouble("spreadFactor"));
        InventoryComponent inventory = new InventoryComponent();

        entity.add(factory);
        entity.add(inventory);
        entity.add(new NamedComponent(recipe.product.getName() + " factory " + factorySequence++));
        entity.add(new TraderComponent());
        entity.add(new MarketSightComponent(market));
        entity.add(new WalletComponent(universeConfig.factory().getRandomDouble("startingCredit")));

        recipe.ingredients.forEach((ingredient, amount) -> {
            int wantedAmount = amount * factory.goalStock;
            int startingAmount = (int) Math.round(wantedAmount
                    * universeConfig.factory().getRandomDouble("inventoryPerIngredient"));
            Price purchasePrice = new Price(universeConfig.factory()
                    .getRandomDouble("purchasePricePerIngredient"));
            TraderSystem.addPricedPositionToInventory(entity,
                    new PricedItemPosition(ingredient, startingAmount, purchasePrice));
        });

        inventory.positions.put(recipe.product, new YieldingItemPosition(recipe.product));
        return entity;
    }
}