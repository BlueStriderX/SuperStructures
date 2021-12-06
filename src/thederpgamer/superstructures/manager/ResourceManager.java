package thederpgamer.superstructures.manager;

import api.utils.textures.StarLoaderTexture;
import org.schema.schine.graphicsengine.core.ResourceException;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.superstructures.SuperStructures;

import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.HashMap;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 06/15/2021
 */
public class ResourceManager {

    private static final String[] textureNames = {
            "dyson-sphere-frame-texture"
    };

    private static final String[] spriteNames = {
            "empty-overlay",
            "regular-overlay",
            "construction-overlay",
            "repair-overlay",
            "upgrade-overlay",
            "super-structure-empty-module-icon",
            "super-structure-power-module-icon",
            "super-structure-resource-module-icon",
            "super-structure-foundry-module-icon",
            "super-structure-shipyard-module-icon",
            "super-structure-defense-module-icon",
            "super-structure-offense-module-icon",
            "super-structure-support-module-icon"
    };

    private static final String[] meshNames = {
            "dyson_sphere_frame",
            "dyson_sphere_empty_module_0",
            "dyson_sphere_empty_module_1",
            "dyson_sphere_empty_module_2",
            "dyson_sphere_empty_module_3",
            "dyson_sphere_empty_module_4",
            "dyson_sphere_empty_module_5",
            "dyson_sphere_empty_module_6",
            "dyson_sphere_empty_module_7",
            "dyson_sphere_empty_module_8",
            "dyson_sphere_empty_module_9",
            "dyson_sphere_empty_module_10",
            "dyson_sphere_empty_module_11"
            /*
            "dyson_sphere_power_module",
            "dyson_sphere_resource_module",
            "dyson_sphere_foundry_module",
            "dyson_sphere_shipyard_module",
            "dyson_sphere_offense_module",
            "dyson_sphere_defense_module",
            "dyson_sphere_support_module"
             */
    };

    private static final HashMap<String, StarLoaderTexture> textureMap = new HashMap<>();
    private static final HashMap<String, Sprite> spriteMap = new HashMap<>();
    private static final HashMap<String, Mesh> meshMap = new HashMap<>();

    public static void loadResources(final SuperStructures instance, final ResourceLoader loader) {

        StarLoaderTexture.runOnGraphicsThread(new Runnable() {
            @Override
            public void run() {
                //Load Textures
                for(String textureName : textureNames) {
                    try {
                        if(textureName.endsWith("icon")) {
                            textureMap.put(textureName, StarLoaderTexture.newIconTexture(instance.getJarBufferedImage("thederpgamer/superstructures/resources/textures/" + textureName + ".png")));
                        } else {
                            textureMap.put(textureName, StarLoaderTexture.newBlockTexture(instance.getJarBufferedImage("thederpgamer/superstructures/resources/textures/" + textureName + ".png")));
                        }
                    } catch(Exception exception) {
                        //LogManager.logException("Failed to load texture \"" + textureName + "\"", exception);
                    }
                }

                //Load Sprites
                for(String spriteName : spriteNames) {
                    try {
                        Sprite sprite = StarLoaderTexture.newSprite(instance.getJarBufferedImage("thederpgamer/superstructures/resources/sprites/" + spriteName + ".png"), instance, spriteName);
                        sprite.setPositionCenter(false);
                        sprite.setName(spriteName);
                        spriteMap.put(spriteName, sprite);
                    } catch(Exception exception) {
                        //LogManager.logException("Failed to load sprite \"" + spriteName + "\"", exception);
                    }
                }

                //Load meshes
                for(String meshName : meshNames) {
                    try {
                        loader.getMeshLoader().loadModMesh(instance, meshName, instance.getJarResource("thederpgamer/superstructures/resources/meshes/" + meshName + ".zip"), null);
                        Mesh mesh = loader.getMeshLoader().getModMesh(SuperStructures.getInstance(), meshName);
                        mesh.setFirstDraw(true);
                        meshMap.put(meshName, mesh);
                    } catch(ResourceException | IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static StarLoaderTexture getTexture(String name) {
        return textureMap.get(name);
    }

    public static Sprite getSprite(String name) {
        return spriteMap.get(name);
    }

    public static Mesh getMesh(String meshName) {
        return (Mesh) meshMap.get(meshName).getChilds().get(0);
    }

    private static Vector3f[] getVectorArray(String line) {
        String[] vectors = line.replace("(", "").split("\\), ");
        Vector3f[] vectorArray = new Vector3f[vectors.length];
        for(int i = 0; i < vectorArray.length; i ++) {
            String[] strippedVector = vectors[i].replace(")", "").trim().split(", ");
            vectorArray[i] = new Vector3f(Float.parseFloat(strippedVector[0].replace(",", "")), Float.parseFloat(strippedVector[1].replace(",", "")), Float.parseFloat(strippedVector[2].replace(",", "")));
        }
        return vectorArray;
    }
}