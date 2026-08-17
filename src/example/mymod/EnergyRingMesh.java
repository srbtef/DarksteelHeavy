package example.mymod;

import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

/** 自定义能量星环渲染 */
public class EnergyRingMesh extends PlanetMesh {
    public Vec3 lightDir = new Vec3();

    public EnergyRingMesh(Planet planet) {
        this.planet = planet;
        // shader在render时才创建（延迟初始化）
    }

    @Override
    public void preRender(PlanetParams params) {
        if (shader == null) {
            shader = new Shader(
                tree.get("shaders/energy-ring.vert"),
                tree.get("shaders/energy-ring.frag")
            );
        }

        // 光照方向 = 指向太阳
        Vec3.toRadix52(planet.position);
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

        // 构建并渲染环状mesh
        Mesh ring = buildRingMesh();
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

    private Mesh buildRingMesh() {
        int ringSeg = 120;
        int widthSeg = 4;
        float innerR = 2.2f, outerR = 2.8f, height = 0.15f;

        int faceVerts = (ringSeg * widthSeg * 4) + (ringSeg * widthSeg * 2); // front + back quads
        int sideVerts = ringSeg * 12; // inner + outer sides
        int haloVerts = ringSeg * 6;
        int totalVerts = faceVerts + sideVerts + haloVerts;

        float[] vertices = new float[totalVerts * 8]; // pos3 + normal3 + uv2
        int vi = 0;

        // --- 正面 ---
        for (int i = 0; i < ringSeg; i++) {
            for (int j = 0; j < widthSeg; j++) {
                float t1 = (float)j / widthSeg;
                float t2 = (float)(j+1) / widthSeg;
                float r1 = innerR + (outerR - innerR) * t1;
                float r2 = innerR + (outerR - innerR) * t2;
                float a1 = (float)i / ringSeg * Mathf.PI2;
                float a2 = (float)(i+1) / ringSeg * Mathf.PI2;

                // quad: v0, v1, v2, v0, v2, v3
                addV(vertices, vi, r1*Mathf.cos(a1), 0.01f, r1*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, t1); vi+=8;
                addV(vertices, vi, r1*Mathf.cos(a2), 0.01f, r1*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, t1); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a2), 0.01f, r2*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, t2); vi+=8;
                addV(vertices, vi, r1*Mathf.cos(a1), 0.01f, r1*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, t1); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a2), 0.01f, r2*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, t2); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a1), 0.01f, r2*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, t2); vi+=8;
            }
        }

        // --- 背面 ---
        for (int i = 0; i < ringSeg; i++) {
            for (int j = 0; j < widthSeg; j++) {
                float t1 = (float)j / widthSeg;
                float t2 = (float)(j+1) / widthSeg;
                float r1 = innerR + (outerR - innerR) * t1;
                float r2 = innerR + (outerR - innerR) * t2;
                float a1 = (float)i / ringSeg * Mathf.PI2;
                float a2 = (float)(i+1) / ringSeg * Mathf.PI2;

                addV(vertices, vi, r1*Mathf.cos(a1), -0.01f, r1*Mathf.sin(a1), 0,-1,0, (float)i/ringSeg, t1); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a1), -0.01f, r2*Mathf.sin(a1), 0,-1,0, (float)i/ringSeg, t2); vi+=8;
                addV(vertices, vi, r1*Mathf.cos(a2), -0.01f, r1*Mathf.sin(a2), 0,-1,0, (float)(i+1)/ringSeg, t1); vi+=8;
                addV(vertices, vi, r1*Mathf.cos(a2), -0.01f, r1*Mathf.sin(a2), 0,-1,0, (float)(i+1)/ringSeg, t1); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a1), -0.01f, r2*Mathf.sin(a1), 0,-1,0, (float)i/ringSeg, t2); vi+=8;
                addV(vertices, vi, r2*Mathf.cos(a2), -0.01f, r2*Mathf.sin(a2), 0,-1,0, (float)(i+1)/ringSeg, t2); vi+=8;
            }
        }

        // --- 内侧面 ---
        for (int i = 0; i < ringSeg; i++) {
            float a1 = (float)i / ringSeg * Mathf.PI2;
            float a2 = (float)(i+1) / ringSeg * Mathf.PI2;
            addV(vertices, vi, innerR*Mathf.cos(a1), 0,       innerR*Mathf.sin(a1), Mathf.cos(a1),0,Mathf.sin(a1), (float)i/ringSeg, 0); vi+=8;
            addV(vertices, vi, innerR*Mathf.cos(a2), 0,       innerR*Mathf.sin(a2), Mathf.cos(a2),0,Mathf.sin(a2), (float)(i+1)/ringSeg, 0); vi+=8;
            addV(vertices, vi, innerR*Mathf.cos(a1), height, innerR*Mathf.sin(a1), Mathf.cos(a1),0,Mathf.sin(a1), (float)i/ringSeg, 1); vi+=8;
            addV(vertices, vi, innerR*Mathf.cos(a2), 0,       innerR*Mathf.sin(a2), Mathf.cos(a2),0,Mathf.sin(a2), (float)(i+1)/ringSeg, 0); vi+=8;
            addV(vertices, vi, innerR*Mathf.cos(a2), height, innerR*Mathf.sin(a2), Mathf.cos(a2),0,Mathf.sin(a2), (float)(i+1)/ringSeg, 1); vi+=8;
            addV(vertices, vi, innerR*Mathf.cos(a1), height, innerR*Mathf.sin(a1), Mathf.cos(a1),0,Mathf.sin(a1), (float)i/ringSeg, 1); vi+=8;

            addV(vertices, vi, outerR*Mathf.cos(a1), 0,       outerR*Mathf.sin(a1), -Mathf.cos(a1),0,-Mathf.sin(a1), (float)i/ringSeg, 0); vi+=8;
            addV(vertices, vi, outerR*Mathf.cos(a1), height, outerR*Mathf.sin(a1), -Mathf.cos(a1),0,-Mathf.sin(a1), (float)i/ringSeg, 1); vi+=8;
            addV(vertices, vi, outerR*Mathf.cos(a2), 0,       outerR*Mathf.sin(a2), -Mathf.cos(a2),0,-Mathf.sin(a2), (float)(i+1)/ringSeg, 0); vi+=8;
            addV(vertices, vi, outerR*Mathf.cos(a1), height, outerR*Mathf.sin(a1), -Mathf.cos(a1),0,-Mathf.sin(a1), (float)i/ringSeg, 1); vi+=8;
            addV(vertices, vi, outerR*Mathf.cos(a2), height, outerR*Mathf.sin(a2), -Mathf.cos(a2),0,-Mathf.sin(a2), (float)(i+1)/ringSeg, 1); vi+=8;
            addV(vertices, vi, outerR*Mathf.cos(a2), 0,       outerR*Mathf.sin(a2), -Mathf.cos(a2),0,-Mathf.sin(a2), (float)(i+1)/ringSeg, 0); vi+=8;
        }

        // --- 光晕 ---
        float haloH = 0.5f;
        for (int i = 0; i < ringSeg; i++) {
            float a1 = (float)i / ringSeg * Mathf.PI2;
            float a2 = (float)(i+1) / ringSeg * Mathf.PI2;
            addV(vertices, vi, (innerR-0.1f)*Mathf.cos(a1), height, (innerR-0.1f)*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, 0); vi+=8;
            addV(vertices, vi, (outerR+0.2f)*Mathf.cos(a2), haloH,  (outerR+0.2f)*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, 1); vi+=8;
            addV(vertices, vi, (innerR-0.1f)*Mathf.cos(a2), height, (innerR-0.1f)*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, 0); vi+=8;
            addV(vertices, vi, (innerR-0.1f)*Mathf.cos(a1), height, (innerR-0.1f)*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, 0); vi+=8;
            addV(vertices, vi, (outerR+0.2f)*Mathf.cos(a2), haloH,  (outerR+0.2f)*Mathf.sin(a2), 0,1,0, (float)(i+1)/ringSeg, 1); vi+=8;
            addV(vertices, vi, (outerR+0.2f)*Mathf.cos(a1), haloH,  (outerR+0.2f)*Mathf.sin(a1), 0,1,0, (float)i/ringSeg, 1); vi+=8;
        }

        float[] finalVerts = new float[vi];
        System.arraycopy(vertices, 0, finalVerts, 0, vi);
        return new Mesh(true, finalVerts, new VertexAttributes(
            VertexAttribute.position3,
            VertexAttribute.normal3,
            VertexAttribute.texCoords2
        ));
    }

    private void addV(float[] v, int i, float x,float y,float z, float nx,float ny,float nz, float u,float v2) {
        v[i]=x; v[i+1]=y; v[i+2]=z; v[i+3]=nx; v[i+4]=ny; v[i+5]=nz; v[i+6]=u; v[i+7]=v2;
    }
}
