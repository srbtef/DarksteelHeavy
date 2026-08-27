package darksteel.content;

import arc.math.geom.Mat3D;
import mindustry.graphics.g3d.PlanetMesh;
import mindustry.graphics.g3d.PlanetParams;

public class SimplePlanetMesh extends PlanetMesh {
    public SimplePlanetMesh(){
        super(null, null, null);
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        // no-op: keep appearance simple and avoid heavy initialization during content load
    }

    @Override
    public void preRender(PlanetParams params) {
        // no-op
    }
}
