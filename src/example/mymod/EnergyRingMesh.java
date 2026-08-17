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
        FloatSeq data = new FloatSeq(12000);

        float innerR = 2.2f, outerR = 2.8f;
        float thick = 0.12f;
        float glowOff = 0.15f;
        float innerGlowR = 2.0f;
        float outerGlowR = 3.0f;
        int segs = 120;

        for (int i = 0; i < segs; i++) {
            float a1 = (float)i / segs * (float)(Math.PI * 2);
            float a2 = (float)(i + 1) / segs * (float)(Math.PI * 2);

            float c1 = (float)StrictMath.cos(a1), s1 = (float)StrictMath.sin(a1);
            float c2 = (float)StrictMath.cos(a2), s2 = (float)StrictMath.sin(a2);
            float c1i = c1, s1i = s1;
            float c2i = c2, s2i = s2;
            float c1o = c1, s1o = s1;
            float c2o = c2, s2o = s2;

            float ix1 = innerR * c1i, iz1 = innerR * s1i;
            float ix2 = innerR * c2i, iz2 = innerR * s2i;
            float ox1 = outerR * c1o, oz1 = outerR * s1o;
            float ox2 = outerR * c2o, oz2 = outerR * s2o;

            // 正面
            vtx(data, ix1, thick, iz1,  0,1,0,  (float)i/segs, 0, 0);
            vtx(data, ox1, thick, oz1,  0,1,0,  (float)i/segs, 1, 0);
            vtx(data, ox2, thick, oz2,  0,1,0,  (float)(i+1)/segs, 1, 0);
            vtx(data, ix1, thick, iz1,  0,1,0,  (float)i/segs, 0, 0);
            vtx(data, ox2, thick, oz2,  0,1,0,  (float)(i+1)/segs, 1, 0);
            vtx(data, ix2, thick, iz2,  0,1,0,  (float)(i+1)/segs, 0, 0);
            // 背面
            vtx(data, ix1, 0, iz1,  0,-1,0,  (float)i/segs, 0, 0);
            vtx(data, ix2, 0, iz2,  0,-1,0,  (float)(i+1)/segs, 0, 0);
            vtx(data, ox1, 0, oz1,  0,-1,0,  (float)i/segs, 1, 0);
            vtx(data, ix2, 0, iz2,  0,-1,0,  (float)(i+1)/segs, 0, 0);
            vtx(data, ox2, 0, oz2,  0,-1,0,  (float)(i+1)/segs, 1, 0);
            vtx(data, ox1, 0, oz1,  0,-1,0,  (float)i/segs, 1, 0);
            // 内侧面
            vtx(data, ix1, 0,      iz1,  c1i,0,s1i,  (float)i/segs, 0, 1);
            vtx(data, ix2, 0,      iz2,  c2i,0,s2i,  (float)(i+1)/segs, 0, 1);
            vtx(data, ix1, thick,  iz1,  c1i,0,s1i,  (float)i/segs, 1, 1);
            vtx(data, ix2, 0,      iz2,  c2i,0,s2i,  (float)(i+1)/segs, 0, 1);
            vtx(data, ix2, thick,  iz2,  c2i,0,s2i,  (float)(i+1)/segs, 1, 1);
            vtx(data, ix1, thick,  iz1,  c1i,0,s1i,  (float)i/segs, 1, 1);
            // 外侧面
            vtx(data, ox1, 0,      oz1,  -c1o,0,-s1o,  (float)i/segs, 0, 2);
            vtx(data, ox1, thick,  oz1,  -c1o,0,-s1o,  (float)i/segs, 1, 2);
            vtx(data, ox2, thick,  oz2,  -c2o,0,-s2o,  (float)(i+1)/segs, 1, 2);
            vtx(data, ox1, 0,      oz1,  -c1o,0,-s1o,  (float)i/segs, 0, 2);
            vtx(data, ox2, thick,  oz2,  -c2o,0,-s2o,  (float)(i+1)/segs, 1, 2);
            vtx(data, ox2, 0,      oz2,  -c2o,0,-s2o,  (float)(i+1)/segs, 0, 2);
            // 光晕
            float gx1 = innerGlowR * c1i, gz1 = innerGlowR * s1i;
            float gx2 = innerGlowR * c2i, gz2 = innerGlowR * s2i;
            float gox1 = (outerR + 0.1f) * c1i, goz1 = (outerR + 0.1f) * s1i;
            float gox2 = (outerR + 0.1f) * c2i, goz2 = (outerR + 0.1f) * s2i;
            vtx(data, gx1, thick,         gz1,  0,1,0,  (float)i/segs, 0, 3);
            vtx(data, gox1, thick+glowOff, goz1,  0,1,0,  (float)i/segs, 1, 3);
            vtx(data, gox2, thick+glowOff, goz2,  0,1,0,  (float)(i+1)/segs, 1, 3);
            vtx(data, gx1, thick,         gz1,  0,1,0,  (float)i/segs, 0, 3);
            vtx(data, gox2, thick+glowOff, goz2,  0,1,0,  (float)(i+1)/segs, 1, 3);
            vtx(data, gx2, thick,         gz2,  0,1,0,  (float)(i+1)/segs, 0, 3);
        }

        int vertexCount = data.size / 9;
        Mesh mesh = new Mesh(true, data.items, 0, vertexCount);
        mesh.attributes = new VertexAttributes(
            VertexAttribute.position3,
            VertexAttribute.normal3,
            VertexAttribute.texCoords2,
            new VertexAttribute("a_surface", 1)
        );
        return mesh;
    }

    private void vtx(FloatSeq d, float x,float y,float z,
            float nx,float ny,float nz, float u,float v, float surface) {
        d.add(x,y,z); d.add(nx,ny,nz); d.add(u,v); d.add(surface);
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
        shader.setUniformf("u_campos",
            planet.position.x, planet.position.y, planet.position.z);
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
