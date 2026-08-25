package darksteel;

import arc.graphics.Color;
import arc.graphics.Mesh;
import arc.math.geom.Vec3;
import mindustry.graphics.Draw3D;
import mindustry.graphics.Draw;
import mindustry.graphics.Shaders;
import mindustry.core.GameState;

public class RingMeshUtil {
    private static Mesh ringMesh;

    public static void renderRing(float x, float y, float radius, float height, int segments, String colorHex1, String colorHex2, float rotateSpeed) {
        if (ringMesh == null) {
            ringMesh = RingMesh.build(radius, height, segments, Color.valueOf(colorHex1), Color.valueOf(colorHex2));
        }

        Draw3D.begin();
        Draw.blend(Draw.additive);
        Draw3D.mat.setToTranslation(x, y, 0f);
        Draw3D.mat.rotate(Vec3.Y, GameState.instance.getTick() * rotateSpeed / 60f);
        ringMesh.render(Shaders.unlit);
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
