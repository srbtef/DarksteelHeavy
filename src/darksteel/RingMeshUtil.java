package darksteel;

import arc.graphics.Color;
import arc.graphics.Mesh;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Draw3D;
import arc.graphics.g2d.Shader;
import arc.math.Time;
import arc.math.geom.Vec3;

public class RingMeshUtil {
    private static Mesh ringMesh;

    public static void renderRing(float x, float y, float radius, float height, int segments, String colorHex1, String colorHex2, float rotateSpeed) {
        if (ringMesh == null) {
            ringMesh = RingMesh.build(radius, height, segments, Color.valueOf(colorHex1), Color.valueOf(colorHex2));
        }

        Draw3D.begin();
        Draw.blend(Draw.additive);
        Draw3D.mat.setToTranslation(x, y, 0f);
        Draw3D.mat.rotate(Vec3.Y, Time.time * rotateSpeed);
        ringMesh.render(Shader.unlit);
        Draw.blend(Draw.normal);
        Draw3D.end();
    }

    public static void disposeRing() {
        if (ringMesh != null) {
            ringMesh.dispose();
            ringMesh = null;
        }
    }
}
