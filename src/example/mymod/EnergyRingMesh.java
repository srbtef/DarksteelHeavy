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
        this.planet = planet;
    }

    private Mesh buildRing() {
        FloatSeq data = new FloatSeq(6000);

        float innerR = 2.2f, outerR = 2.8f;
        float innerGlowR = 2.0f, outerGlowR = 3.0f;
        float thick = 0.12f;
        float glowOff = 0.15f;
        float tilt = 0.5f; // 倾斜角度

        int segs = 120;

        for (int i = 0; i < segs; i++) {
            float a1 = (float)i / segs * (float)(Math.PI * 2);
            float a2 = (float)(i + 1) / segs * (float)(Math.PI * 2);

            float x1 = innerR * Mathf.cos(a1), z1 = innerR * Mathf.sin(a1);
            float x2 = innerR * Mathf.cos(a2), z2 = innerR * Mathf.sin(a2);
            float x3 = outerR * Mathf.cos(a1), z3 = outerR * Mathf.sin(a1);
            float x4 = outerR * Mathf.cos(a2), z4 = outerR * Mathf.sin(a2);

            // 正面
            quad(data, 
                x1, thick, z1, 0, 1, 0, (float)i/segs, 0,
                x2, thick, z2, 0, 1, 0, (float)(i+1)/segs, 0,
                x4, thick, z4, 0, 1, 0, (float)(i+1)/segs, 1,
                x1, thick, z1, 0, 1, 0, (float)i/segs, 0,
                x4, thick, z4, 0, 1, 0, (float)(i+1)/segs, 1,
                x3, thick, z3, 0, 1, 0, (float)i/segs, 1,
                0);
            // 背面
            quad(data,
                x1, 0, z1, 0, -1, 0, (float)i/segs, 0,
                x3, 0, z3, 0, -1, 0, (float)i/segs, 1,
                x4, 0, z4, 0, -1, 0, (float)(i+1)/segs, 1,
                x1, 0, z1, 0, -1, 0, (float)i/segs, 0,
                x4, 0, z4, 0, -1, 0, (float)(i+1)/segs, 1,
                x2, 0, z2, 0, -1, 0, (float)(i+1)/segs, 0,
                0);
            // 内侧面
            quad(data,
                x1, 0, z1, Mathf.cos(a1), 0, Mathf.sin(a1), (float)i/segs, 0,
                x1, thick, z1, Mathf.cos(a1), 0, Mathf.sin(a1), (float)i/segs, 1,
                x2, thick, z2, Mathf.cos(a2), 0, Mathf.sin(a2), (float)(i+1)/segs, 1,
                x1, 0, z1, Mathf.cos(a1), 0, Mathf.sin(a1), (float)i/segs, 0,
                x2, thick, z2, Mathf.cos(a2), 0, Mathf.sin(a2), (float)(i+1)/segs, 1,
                x2, 0, z2, Mathf.cos(a2), 0, Mathf.sin(a2), (float)(i+1)/segs, 0,
                1);
            // 外侧面
            quad(data,
                x3, 0, z3, -Mathf.cos(a1), 0, -Mathf.sin(a1), (float)i/segs, 0,
                x4, 0, z4, -Mathf.cos(a2), 0, -Mathf.sin(a2), (float)(i+1)/segs, 0,
                x3, thick, z3, -Mathf.cos(a1), 0, -Mathf.sin(a1), (float)i/segs, 1,
                x3, thick, z3, -Mathf.cos(a1), 0, -Mathf.sin(a1), (float)i/segs, 1,
                x4, 0, z4, -Mathf.cos(a2), 0, -Mathf.sin(a2), (float)(i+1)/segs, 0,
                x4, thick, z4, -Mathf.cos(a2), 0, -Mathf.sin(a2), (float)(i+1)/segs, 1,
                2);

            // 光晕内缘
            quad(data,
                innerGlowR * Mathf.cos(a1), thick, innerGlowR * Mathf.sin(a1), 0, 1, 0, (float)i/segs, 0,
                innerGlowR * Mathf.cos(a2), thick, innerGlowR * Mathf.sin(a2), 0, 1, 0, (float)(i+1)/segs, 0,
                (outerR + 0.1f) * Mathf.cos(a2), thick + glowOff, (outerR + 0.1f) * Mathf.sin(a2), 0, 1, 0, (float)(i+1)/segs, 1,
                innerGlowR * Mathf.cos(a1), thick, innerGlowR * Mathf.sin(a1), 0, 1, 0, (float)i/segs, 0,
                (outerR + 0.1f) * Mathf.cos(a2), thick + glowOff, (outerR + 0.1f) * Mathf.sin(a2), 0, 1, 0, (float)(i+1)/segs, 1,
                (outerR + 0.1f) * Mathf.cos(a1), thick + glowOff, (outerR + 0.1f) * Mathf.sin(a1), 0, 1, 0, (float)i/segs, 1,
                3);
        }

        Mesh mesh = new Mesh(true, data.items, 0, data.size);
        mesh.attributes = new VertexAttributes(
            VertexAttribute.position3,
            VertexAttribute.normal3,
            VertexAttribute.texCoords2,
            VertexAttribute.aSurface
        );
        return mesh;
    }

    /** 添加四边形（2个三角形）: 9个float per vertex */
    private void quad(FloatSeq data,
            float x1, float y1, float z1, float nx1, float ny1, float nz1, float u1, float v1,
            float x2, float y2, float z2, float nx2, float ny2, float nz2, float u2, float v2,
            float x3, float y3, float z3, float nx3, float ny3, float nz3, float u3, float v3,
            float x4, float y4, float z4, float nx4, float ny4, float nz4, float u4, float v4,
            float surface) {
        vertex(data, x1,y1,z1, nx1,ny1,nz1, u1,v1, surface);
        vertex(data, x2,y2,z2, nx2,ny2,nz2, u2,v2, surface);
        vertex(data, x3,y3,z3, nx3,ny3,nz3, u3,v3, surface);
        vertex(data, x1,y1,z1, nx1,ny1,nz1, u1,v1, surface);
        vertex(data, x3,y3,z3, nx3,ny3,nz3, u3,v3, surface);
        vertex(data, x4,y4,z4, nx4,ny4,nz4, u4,v4, surface);
    }

    private void vertex(FloatSeq data, float x,float y,float z,
            float nx,float ny,float nz, float u,float v, float surface) {
        data.add(x, y, z);
        data.add(nx, ny, nz);
        data.add(u, v);
        data.add(surface);
    }

    @Override
    public void preRender(PlanetParams params) {
        if (shader == null) {
            shader = new Shader(
                tree.get("shaders/energy-ring.vert"),
                tree.get("shaders/energy-ring.frag")
            );
        }
        lightDir.set(planet.position).sub(planet.solarSystem.position).nor();
        shader.bind();
        shader.setUniformf("u_time", Time.globalTime / 60f);
        shader.setUniformf("u_alpha", 0.85f);
        shader.setUniformf("u_lightdir", lightDir.x, lightDir.y, lightDir.z);
        shader.setUniformf("u_campos", planet.position.x, planet.position.y, planet.position.z);
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        preRender(params);
        Mesh ring = buildRing();
        Gl.depthMask(false);
        Gl.blend(Gl.blendAdd);
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        shader.apply();
        ring.render(shader, Gl.triangles);
        Gl.blend(Gl.blend);
        Gl.depthMask(true);
        ring.dispose();
    }

    @Override
    public void dispose() {}
}
