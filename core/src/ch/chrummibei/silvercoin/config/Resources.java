package ch.chrummibei.silvercoin.config;

import ch.chrummibei.silvercoin.universe.Universe;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Central resource handling. Contains paths as constants and converts files to Streams or Readers.
 */
public class Resources {
    private static final String basePath = "./";

    public static FileHandle getFileHandle(String resource) {
        return Gdx.files.internal(basePath + resource);
    }

    public static BitmapFont getDefaultFont() {
        return new BitmapFont(getFileHandle("fonts/fixed_font.fnt"), false);
    }

    public static FileHandle getDefaultModItemJsonFile() {
        return getFileHandle("mods/items.json");
    }
    public static FileHandle getDefaultModRecipeJsonFile() {
        if (Universe.DEBUG) {
            return getFileHandle("mods/recipes_debug.json");
        } else {
            return getFileHandle("mods/recipes.json");
        }
    }
    public static FileHandle getDefaultModFactoryJsonFile() {
        if (Universe.DEBUG) {
            return getFileHandle("mods/factories_debug.json");
        } else {
            return getFileHandle("mods/factories.json");
        }
    }
}
