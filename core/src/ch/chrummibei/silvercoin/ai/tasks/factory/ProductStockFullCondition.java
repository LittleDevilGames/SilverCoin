package ch.chrummibei.silvercoin.ai.tasks.factory;

import ch.chrummibei.silvercoin.universe.components.FactoryComponent;
import ch.chrummibei.silvercoin.universe.entity_systems.FactorySystem;
import ch.chrummibei.silvercoin.universe.entity_systems.Mappers;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

/**
 * Created by brachiel on 24/02/2017.
 */
public class ProductStockFullCondition extends LeafTask<Entity> {
    @Override
    public Status execute() {
        FactoryComponent factory = Mappers.factory.get(this.getObject());
        return FactorySystem.getProductPosition(this.getObject()).getAmount() >= factory.goalStock ?
                Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return task;
    }
}
