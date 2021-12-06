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

    public MultiMesh(Mesh[] meshArray) {
        this.meshArray = meshArray;
    }

    public MultiMesh(Mesh frame, Mesh[] meshArray) {
        this.frame = frame;
        this.meshArray = meshArray;
    }

    @Override
    public void onInit() {
        childs.add(frame);
        for(Mesh mesh : meshArray) {
            if(mesh != null) childs.add(mesh);
        }
    }

    @Override
    public AbstractSceneNode clone() {
        return null;
    }

    public InputState getState() {
        return GameClient.getClientState();
    }
}