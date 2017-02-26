package ch.chrummibei.silvercoin.universe.entity_systems;

import ch.chrummibei.silvercoin.universe.components.PhysicsComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by brachiel on 26/02/2017.
 */
public class PhysicsSystem extends IteratingSystem {
    private static Family family = Family.all(PhysicsComponent.class).get();

    public PhysicsSystem() {
        super(family);
    }

    public PhysicsSystem(int priority) {
        super(family, priority);
    }

    public static void createBody(Entity entity, World box2dWorld, Vector2 position) {
        PhysicsComponent physics = Mappers.physics.get(entity);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        Body body = box2dWorld.createBody(bodyDef);

        physics.bodyDef = bodyDef;
        physics.body = body;

        CircleShape circle = new CircleShape();
        circle.setRadius(3f); // 3 meters

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.5f;
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
