package example.mymod;

import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.graphics.*;
import static mindustry.Vars.*;

public class EnergyRingMesh extends PlanetMesh {
    public Vec3 lightDir = new Vec3();

    public EnergyRingMesh(Planet planet) {
        super(planet, null, null);
    }

    @Override
    public void preRender(PlanetParams params) {
        lightDir.set(planet.position).nor();
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        preRender(params);

        if (shader == null) {
            shader = new Shader(
                tree.get("shaders/energy-ring.vert"),
                tree.get("shaders/energy-ring.frag")
            );
        }

        if (mesh == null) {
            mesh = buildRing();
        }

        Gl.depthMask(false);
        Blending.additive.apply();
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        shader.setUniformf("u_time", Time.globalTime / 60f);
        shader.setUniformf("u_alpha", 0.9f);
        shader.setUniformf("u_lightdir", lightDir.x, lightDir.y, lightDir.z);
        shader.setUniformf("u_campos",
            planet.position.x, planet.position.y, planet.position.z);
        shader.apply();
        mesh.render(shader, 4);
        Blending.normal.apply();
        Gl.depthMask(true);
    }

    private Mesh buildRing() {
        FloatSeq data = new FloatSeq(15000);
        int segs = 120;
        float innerR = 2.2f, outerR = 2.8f;
        float thick = 0.1f;
        float glowOff = 0.2f;
        float innerGlowR = 1.9f, outerGlowR = 3.1f;
        float twoPi = (float)(Math.PI * 2);

        for (int i = 0; i < segs; i++) {
            float a1 = (float)i / segs * twoPi;
            float a2 = (float)(i + 1) / segs * twoPi;
            float c1 = (float)StrictMath.cos(a1), s1 = (float)StrictMath.sin(a1);
            float c2 = (float)StrictMath.cos(a2), s2 = (float)StrictMath.sin(a2);

            float u1 = (float)i / segs, u2 = (float)(i + 1) / segs;

            // 正面三角形1
            data.add(innerR*c1, thick, innerR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 0f);
            data.add(outerR*c1, thick, outerR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 1f);
            data.add(outerR*c2, thick, outerR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 1f);
            // 正面三角形2
            data.add(innerR*c1, thick, innerR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 0f);
            data.add(outerR*c2, thick, outerR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 1f);
            data.add(innerR*c2, thick, innerR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 0f);

            // 背面三角形1
            data.add(innerR*c1, 0f, innerR*s1);
            data.add(0f, -1f, 0f); data.add(u1, 0f);
            data.add(innerR*c2, 0f, innerR*s2);
            data.add(0f, -1f, 0f); data.add(u2, 0f);
            data.add(outerR*c1, 0f, outerR*s1);
            data.add(0f, -1f, 0f); data.add(u1, 1f);
            // 背面三角形2
            data.add(innerR*c2, 0f, innerR*s2);
            data.add(0f, -1f, 0f); data.add(u2, 0f);
            data.add(outerR*c2, 0f, outerR*s2);
            data.add(0f, -1f, 0f); data.add(u2, 1f);
            data.add(outerR*c1, 0f, outerR*s1);
            data.add(0f, -1f, 0f); data.add(u1, 1f);

            // 内侧面三角形1
            data.add(innerR*c1, 0f, innerR*s1);
            data.add(c1, 0f, s1); data.add(u1, 0f);
            data.add(innerR*c1, thick, innerR*s1);
            data.add(c1, 0f, s1); data.add(u1, 1f);
            data.add(innerR*c2, thick, innerR*s2);
            data.add(c2, 0f, s2); data.add(u2, 1f);
            // 内侧面三角形2
            data.add(innerR*c1, 0f, innerR*s1);
            data.add(c1, 0f, s1); data.add(u1, 0f);
            data.add(innerR*c2, thick, innerR*s2);
            data.add(c2, 0f, s2); data.add(u2, 1f);
            data.add(innerR*c2, 0f, innerR*s2);
            data.add(c2, 0f, s2); data.add(u2, 0f);

            // 外侧面三角形1
            data.add(outerR*c1, 0f, outerR*s1);
            data.add(-c1, 0f, -s1); data.add(u1, 0f);
            data.add(outerR*c2, 0f, outerR*s2);
            data.add(-c2, 0f, -s2); data.add(u2, 0f);
            data.add(outerR*c1, thick, outerR*s1);
            data.add(-c1, 0f, -s1); data.add(u1, 1f);
            // 外侧面三角形2
            data.add(outerR*c2, 0f, outerR*s2);
            data.add(-c2, 0f, -s2); data.add(u2, 0f);
            data.add(outerR*c2, thick, outerR*s2);
            data.add(-c2, 0f, -s2); data.add(u2, 1f);
            data.add(outerR*c1, thick, outerR*s1);
            data.add(-c1, 0f, -s1); data.add(u1, 1f);

            // 光晕三角形1
            data.add(innerGlowR*c1, thick, innerGlowR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 0f);
            data.add(innerGlowR*c2, thick, innerGlowR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 0f);
            data.add(outerGlowR*c2, thick+glowOff, outerGlowR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 1f);
            // 光晕三角形2
            data.add(innerGlowR*c1, thick, innerGlowR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 0f);
            data.add(outerGlowR*c2, thick+glowOff, outerGlowR*s2);
            data.add(0f, 1f, 0f); data.add(u2, 1f);
            data.add(outerGlowR*c1, thick+glowOff, outerGlowR*s1);
            data.add(0f, 1f, 0f); data.add(u1, 1f);
        }

        int verts = data.size / 8;
        VertexAttribute[] attrs = new VertexAttribute[]{
            VertexAttribute.position3,
            VertexAttribute.normal,
            VertexAttribute.texCoords
        };
        Mesh m = new Mesh(true, verts, 8, attrs);
        m.setVertices(data.items, 0, data.size);
        return m;
    }

    @Override
    public void dispose() {}
}
