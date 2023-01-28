package thederpgamer.superstructures.graphics.mesh;

import api.common.GameClient;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.input.InputState;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/28/2021]
 */
public abstract class MultiMesh extends AbstractSceneNode {

    public Mesh frame;
    public final Mesh[] meshArray;

    protected MultiMesh(Mesh[] meshArray) {
        this.meshArray = meshArray;
    }

    protected MultiMesh(Mesh frame, Mesh[] meshArray) {
        this.frame = frame;
        this.meshArray = meshArray;
    }

    @Override
    public void onInit() {
        childs.add(frame);
        for(int i = 0; i < meshArray.length; i ++) {
            Mesh mesh = meshArray[i];
            if(mesh != null) {
                childs.add(mesh);
                alignMesh(mesh, i);
            }
        }
    }

    @Override
    public AbstractSceneNode clone() {
        return null;
    }

    public InputState getState() {
        return GameClient.getClientState();
    }

    public abstract void alignMesh(Mesh mesh, int index);
}