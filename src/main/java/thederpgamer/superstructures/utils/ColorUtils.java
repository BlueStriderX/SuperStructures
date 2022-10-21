package thederpgamer.superstructures.utils;

import javax.vecmath.Vector4f;
import java.awt.*;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @version 1.0 - [09/29/2021]
 */
public class ColorUtils {

    public static Vector4f toVector4f(String code) {
        return toVector4f(code, 1.0f);
    }

    public static Vector4f toVector4f(String code, float alpha) {
        code = code.replaceAll("#", "");
        Color color = (code.startsWith("0x")) ? Color.decode(code) : Color.decode("0x" + code);
        Vector4f vector = new Vector4f(color.getColorComponents(new float[4]));
        vector.w = alpha;
        return vector;
    }
}
