package ch.chrummibei.silvercoin.config;

import ch.chrummibei.silvercoin.universe.item.Item;
import ch.chrummibei.silvercoin.universe.item.Recipe;
import ch.chrummibei.silvercoin.universe.item.RecipeBook;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

/**
 * Created by brachiel on 20/02/2017.
 */
public class JSONRecipeConfigReader {
    private Json json = new Json();

    public JSONRecipeConfigReader(JSONItemConfigReader itemConfig) {
        json.setSerializer(Recipe.class, new Json.Serializer<Recipe>() {
            @Override
            public void write(Json json, Recipe object, Class knownType) {
                return;
            }

            @Override
            public Recipe read(Json json, JsonValue jsonData, Class type) {
                Item product = null;
                HashMap<Item,Integer> ingredients= new HashMap<>();
                Long buildTime = null;

                for (JsonValue child : jsonData) {
                    switch (child.name()) {
                        case "product":
                            product = itemConfig.getItemByName(child.asString());
                            break;
                        case "ingredients":
                            for (JsonValue ingredient : child) {
                                ingredients.put(itemConfig.getItemByName(ingredient.name()), ingredient.asInt());
                            }
                            break;
                        case "buildTime":
                            buildTime = child.asLong();
                            break;
                        default:
                            throw new RuntimeException("Error parsing Item Configuration; unknown field " + child.name());
                    }
                }

                return new Recipe(product, ingredients, buildTime);
            }
        });

        //json.setElementType(RecipeBook.class, "recipes", Recipe.class);
    }

    public RecipeBook getRecipeBook(FileHandle defaultModRecipeJsonFile) {
        return json.fromJson(RecipeBook.class, defaultModRecipeJsonFile);
    }
}