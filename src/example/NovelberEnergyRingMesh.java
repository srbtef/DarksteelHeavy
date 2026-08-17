package example;

import arc.graphics.Mesh;
import arc.graphics.g3d.PlanetMesh;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import mindustry.type.Planet;

public class NovelberEnergyRingMesh extends PlanetMesh {
    private static final float glowOffset = 0.003f;
    private static final float innerGlowRadius = 1.35f;
    private static final float innerRadius = 1.85f;
    private static final float outerGlowRadius = 2.82f;
    private static final float outerRadius = 2.25f;
    private static final int segments = 128;
    private static final float thickness = 0.02f;
    private static final float tilt = 30.0f;

    private final Vec3 lightDir;

    public NovelberEnergyRingMesh(Planet planet) {
        super();
        lightDir = new Vec3();
        this.planet = planet;
        this.mesh = build(planet);
    }

    private static Mesh build(Planet planet) {
        FloatSeq vertices = new FloatSeq();
        float tiltRad = Mathf.degRad * tilt;

        for (int i = 0; i < segments; i++) {
            float a1 = Mathf.PI2 * i / segments;
            float a2 = Mathf.PI2 * (i + 1) / segments;
            addSegment(vertices, planet.radius, a1, a2, tiltRad, thickness, Mathf.cos(a1), Mathf.sin(a1));
        }

        Mesh mesh = new Mesh(true, vertices.size / 14, vertices.size / 21);
        mesh.setVertices(vertices);
        return mesh;
    }

    private static void addSegment(FloatSeq seq, float scale, float ang0, float ang1, float tilt, float thick, float nx, float ny) {
        float[] pIn0 = point(innerRadius * scale, ang0, tilt, nx, ny);
        float[] pOut0 = point(outerRadius * scale, ang0, tilt, nx, ny);
        float[] pIn1 = point(innerRadius * scale, ang1, tilt, nx, ny);
        float[] pOut1 = point(outerRadius * scale, ang1, tilt, nx, ny);

        float[] pIn0N = point(innerRadius * scale, -ang0, tilt, nx, ny);
        float[] pOut0N = point(outerRadius * scale, -ang0, tilt, nx, ny);
        float[] pIn1N = point(innerRadius * scale, -ang1, tilt, nx, ny);
        float[] pOut1N = point(outerRadius * scale, -ang1, tilt, nx, ny);

        float glowA0 = ang0 + glowOffset * scale;
        float glowA1 = ang1 + glowOffset * scale;

        float[] gIn0 = point(innerGlowRadius * scale, glowA0, tilt, nx, ny);
        float[] gOut0 = point(outerGlowRadius * scale, glowA0, tilt, nx, ny);
        float[] gIn1 = point(innerGlowRadius * scale, glowA1, tilt, nx, ny);
        float[] gOut1 = point(outerGlowRadius * scale, glowA1, tilt, nx, ny);

        float[] gIn0N = point(innerGlowRadius * scale, -glowA0, tilt, nx, ny);
        float[] gOut0N = point(outerGlowRadius * scale, -glowA0, tilt, nx, ny);
        float[] gIn1N = point(innerGlowRadius * scale, -glowA1, tilt, nx, ny);
        float[] gOut1N = point(outerGlowRadius * scale, -glowA1, tilt, nx, ny);

        float[] nFlat = normal(0, 1, 0, nx, ny);
        float[] nDown = normal(0, -1, 0, nx, ny);
        float[] nAng0 = normal(Mathf.cos(ang0), 0, Mathf.sin(ang0), nx, ny);
        float[] nAng1 = normal(Mathf.cos(ang1), 0, Mathf.sin(ang1), nx, ny);

        put(seq, pIn0, nFlat);
        put(seq, pOut0, nFlat);
        put(seq, pIn1, nFlat);
        put(seq, pOut0, nFlat);
        put(seq, pOut1, nFlat);
        put(seq, pIn1, nFlat);

        put(seq, pIn0N, nDown);
        put(seq, pOut0N, nDown);
        put(seq, pIn1N, nDown);
        put(seq, pOut0N, nDown);
        put(seq, pOut1N, nDown);
        put(seq, pIn1N, nDown);

        put(seq, gIn0, nAng0);
        put(seq, gOut0, nAng0);
        put(seq, gIn1, nAng1);
        put(seq, gOut0, nAng0);
        put(seq, gOut1, nAng1);
        put(seq, gIn1, nAng1);

        put(seq, gIn0N, nAng0);
        put(seq, gOut0N, nAng0);
        put(seq, gIn1N, nAng1);
        put(seq, gOut0N, nAng0);
        put(seq, gOut1N, nAng1);
        put(seq, gIn1N, nAng1);
    }

    private static float[] point(float r, float angle, float tilt, float nx, float ny) {
        float x = r * Mathf.cos(angle);
        float y = r * Mathf.sin(angle) * Mathf.cos(tilt);
        float z = r * Mathf.sin(angle) * Mathf.sin(tilt);
        return new float[]{x, y, z, nx, ny};
    }

    private static float[] normal(float x, float y, float z, float nx, float ny) {
        return new float[]{x, y, z, nx, ny};
    }

    private static void put(FloatSeq seq, float[] p, float[] n) {
        seq.add(p[0]);
        seq.add(p[1]);
        seq.add(p[2]);
        seq.add(n[0]);
        seq.add(n[1]);
        seq.add(n[2]);
        seq.add(p[3]);
        seq.add(p[4]);
    }

    @Override
    public void render() {
        super.render();
    }
}
