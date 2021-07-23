package thederpgamer.superstructures.manager;

import api.utils.textures.StarLoaderTexture;
import org.apache.commons.io.IOUtils;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.superstructures.SuperStructures;
import thederpgamer.superstructures.data.shapes.Shape3D;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 06/15/2021
 */
public class ResourceManager {

    private static final String[] textureNames = {

    };

    private static final String[] spriteNames = {
            "super-structure-empty-module-icon",
            "super-structure-power-module-icon",
            "super-structure-resource-module-icon",
            "super-structure-foundry-module-icon",
            "super-structure-shipyard-module-icon",
            "super-structure-defense-module-icon",
            "super-structure-offense-module-icon",
            "super-structure-support-module-icon"
    };

    private static final String[] shapeNames = {
            "dodecahedron"
    };

    private static HashMap<String, StarLoaderTexture> textureMap = new HashMap<>();
    private static HashMap<String, Sprite> spriteMap = new HashMap<>();
    private static HashMap<String, Shape3D> shapeMap = new HashMap<>();

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

                for(String shapeName : shapeNames) {
                    try {
                        shapeMap.put(shapeName, loadShape(shapeName));
                    } catch(Exception exception) {
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

    public static Shape3D getShape(String shapeName) {
        if(shapeMap.containsKey(shapeName)) {
            Shape3D shape = shapeMap.get(shapeName);
            return new Shape3D(shapeName, shape.getVertices(), shape.getEdges(), shape.getFaces());
        } else return null;
    }

    private static Shape3D loadShape(String shapeName) throws Exception {
        InputStream inputStream = SuperStructures.getInstance().getJarResource("thederpgamer/superstructures/resources/shapedata/" + shapeName + ".modeldat");
        if(inputStream != null) {
            String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(s);

            String name = "";
            String type = "";
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector3f[]> edges = new ArrayList<>();
            ArrayList<Vector3f[]> faces = new ArrayList<>();

            String next = scanner.nextLine();
            int i = 0;
            boolean readingData = false;
            boolean readingVertices = true;
            boolean readingEdges = false;
            boolean readingFaces = false;
            while(next != null) {
                if(next.startsWith("}") || next.startsWith("VERTICES") || next.startsWith("EDGES") || next.startsWith("FACES")) {
                    next = scanner.nextLine().trim();
                    i ++;
                    continue;
                }
                if(!readingData) {
                    if(i == 0) {
                        if(next.startsWith("NAME")) name = next.split("=")[1];
                        else throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                    } else if(i == 1) {
                        if(next.startsWith("TYPE")) type = next.split("=")[1];
                        else throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                    } else if(i == 2) {
                        if(next.startsWith("DATA")) {
                            readingData = true;
                        } else throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                    }
                } else {
                    if(readingVertices) {
                        try {
                            vertices.add(getVectorArray(next)[0]);
                            if(!next.endsWith(",")) {
                                readingVertices = false;
                                readingEdges = true;
                            }
                        } catch(Exception exception) {
                            exception.printStackTrace();
                            throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                        }
                    } else if(readingEdges) {
                        try {
                            edges.add(getVectorArray(next));
                            if(!next.endsWith(",")) {
                                readingEdges = false;
                                readingFaces = true;
                            }
                        } catch(Exception exception) {
                            exception.printStackTrace();
                            throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                        }
                    } else if(readingFaces) {
                        try {
                            faces.add(getVectorArray(next));
                            if(!next.endsWith(",")) break;
                        } catch(Exception exception) {
                            exception.printStackTrace();
                            throw new IOException("Invalid input at line " + i + " in input file " + shapeName + ".smdat");
                        }
                    }
                }
                next = scanner.nextLine().trim();
                i ++;
            }

            Vector3f[] vertexArray = new Vector3f[vertices.size()];
            Vector3f[][] edgeArray = new Vector3f[edges.size()][2];
            Vector3f[][] faceArray = new Vector3f[faces.size()][];

            for(i = 0; i < vertexArray.length; i ++) vertexArray[i] = vertices.get(i);
            for(i = 0; i < edgeArray.length; i ++) {
                edgeArray[i] = new Vector3f[2];
                System.arraycopy(edges.get(i), 0, edgeArray[i], 0, 2);
            }
            for(i = 0; i < faceArray.length; i ++) {
                faceArray[i] = new Vector3f[faces.get(i).length];
                System.arraycopy(faces.get(i), 0, faceArray[i], 0, faceArray[i].length);
            }

            return new Shape3D(shapeName, vertexArray, edgeArray, faceArray);
        }
        return null;
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