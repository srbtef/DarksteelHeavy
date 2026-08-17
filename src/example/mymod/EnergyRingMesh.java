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

public class EnergyRingMesh implements GenericMesh {
    public Planet planet;
    public Shader shader;
    public Vec3 lightDir = new Vec3();
    public Mesh mesh;

    public EnergyRingMesh(Planet planet) {
        this.planet = planet;
    }

    private Mesh buildMesh() {
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

            // 正面
            addQuad(data,
                innerR*c1, thick, innerR*s1,   0,1,0,  (float)i/segs, 0,
                outerR*c1, thick, outerR*s1,   0,1,0,  (float)i/segs, 1,
                outerR*c2, thick, outerR*s2,   0,1,0,  (float)(i+1)/segs, 1,
                innerR*c1, thick, innerR*s1,   0,1,0,  (float)i/segs, 0,
                outerR*c2, thick, outerR*s2,   0,1,0,  (float)(i+1)/segs, 1,
                innerR*c2, thick, innerR*s2,   0,1,0,  (float)(i+1)/segs, 0);

            // 背面
            addQuad(data,
                innerR*c1, 0, innerR*s1,   0,-1,0,  (float)i/segs, 0,
                innerR*c2, 0, innerR*s2,   0,-1,0,  (float)(i+1)/segs, 0,
                outerR*c1, 0, outerR*s1,   0,-1,0,  (float)i/segs, 1,
                innerR*c2, 0, innerR*s2,   0,-1,0,  (float)(i+1)/segs, 0,
                outerR*c2, 0, outerR*s2,   0,-1,0,  (float)(i+1)/segs, 1,
                outerR*c1, 0, outerR*s1,   0,-1,0,  (float)i/segs, 1);

            // 内侧面
            addQuad(data,
                innerR*c1, 0, innerR*s1,  c1,0,s1,  (float)i/segs, 0,
                innerR*c1, thick, innerR*s1,  c1,0,s1,  (float)i/segs, 1,
                innerR*c2, thick, innerR*s2,  c2,0,s2,  (float)(i+1)/segs, 1,
                innerR*c1, 0, innerR*s1,  c1,0,s1,  (float)i/segs, 0,
                innerR*c2, thick, innerR*s2,  c2,0,s2,  (float)(i+1)/segs, 1,
                innerR*c2, 0, innerR*s2,  c2,0,s2,  (float)(i+1)/segs, 0);

            // 外侧面
            addQuad(data,
                outerR*c1, 0, outerR*s1,  -c1,0,-s1,  (float)i/segs, 0,
                outerR*c2, 0, outerR*s2,  -c2,0,-s2,  (float)(i+1)/segs, 0,
                outerR*c1, thick, outerR*s1,  -c1,0,-s1,  (float)i/segs, 1,
                outerR*c2, 0, outerR*s2,  -c2,0,-s2,  (float)(i+1)/segs, 0,
                outerR*c2, thick, outerR*s2,  -c2,0,-s2,  (float)(i+1)/segs, 1,
                outerR*c1, thick, outerR*s1,  -c1,0,-s1,  (float)i/segs, 1);

            // 光晕
            addQuad(data,
                innerGlowR*c1, thick, innerGlowR*s1,  0,1,0,  (float)i/segs, 0,
                innerGlowR*c2, thick, innerGlowR*s2,  0,1,0,  (float)(i+1)/segs, 0,
                outerGlowR*c2, thick+glowOff, outerGlowR*s2,  0,1,0,  (float)(i+1)/segs, 1,
                innerGlowR*c1, thick, innerGlowR*s1,  0,1,0,  (float)i/segs, 0,
                outerGlowR*c2, thick+glowOff, outerGlowR*s2,  0,1,0,  (float)(i+1)/segs, 1,
                outerGlowR*c1, thick+glowOff, outerGlowR*s1,  0,1,0,  (float)i/segs, 1);
        }

        float[] verts = new float[data.size];
        System.arraycopy(data.items, 0, verts, 0, data.size);
        // 9 floats per vertex: pos3 + normal3 + uv2 + surface1
        return new Mesh(true, verts);
    }

    private void addQuad(FloatSeq d,
            float x1,float y1,float z1, float nx1,float ny1,float nz1, float u1,float v1,
            float x2,float y2,float z2, float nx2,float ny2,float nz2, float u2,float v2,
            float x3,float y3,float z3, float nx3,float ny3,float nz3, float u3,float v3,
            float x4,float y4,float z4, float nx4,float ny4,float nz4, float u4,float v4) {
        vtx(d, x1,y1,z1, nx1,ny1,nz1, u1,v1, 0);
        vtx(d, x2,y2,z2, nx2,ny2,nz2, u2,v2, 0);
        vtx(d, x3,y3,z3, nx3,ny3,nz3, u3,v3, 0);
        vtx(d, x1,y1,z1, nx1,ny1,nz1, u1,v1, 0);
        vtx(d, x3,y3,z3, nx3,ny3,nz3, u3,v3, 0);
        vtx(d, x4,y4,z4, nx4,ny4,nz4, u4,v4, 0);
    }

    private void vtx(FloatSeq d, float x,float y,float z,
            float nx,float ny,float nz, float u,float v, float surface) {
        d.add(x, y, z);
        d.add(nx, ny, nz);
        d.add(u, v);
        d.add(surface);
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        if (shader == null) {
            shader = new Shader(
                tree.get("shaders/energy-ring.vert"),
                tree.get("shaders/energy-ring.frag")
            );
        }

        if (mesh == null) {
            mesh = buildMesh();
        }

        lightDir.set(planet.position).sub(planet.solarSystem.position).nor();

        Gl.depthMask(false);
        Gl.blend(Gl.blendAdd);
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        shader.setUniformf("u_time", Time.globalTime / 60f);
        shader.setUniformf("u_alpha", 0.9f);
        shader.setUniformf("u_lightdir", lightDir.x, lightDir.y, lightDir.z);
        shader.setUniformf("u_campos",
            planet.position.x, planet.position.y, planet.position.z);
        shader.apply();
        mesh.render(shader, Gl.triangles);
        Gl.blend(Gl.blend);
        Gl.depthMask(true);
    }

    @Override
    public void dispose() {
        if (mesh != null) mesh.dispose();
    }
}
