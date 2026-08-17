package example.mymod;

import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

/** 自定义能量星环渲染 */
public class EnergyRingMesh extends PlanetMesh {
    public Vec3 lightDir = new Vec3();
    public Mesh ringMesh;

    public EnergyRingMesh(Planet planet) {
        this.planet = planet;

        // 创建自定义shader
        this.shader = new Shader(
            tree.get("shaders/energy-ring.vert"),
            tree.get("shaders/energy-ring.frag")
        );

        // 构建环状mesh
        this.ringMesh = buildRingMesh();
    }

    /** 生成星环mesh */
    private Mesh buildRingMesh() {
        int ringSegments = 120; // 环的段数（越多越平滑）
        int widthSegments = 4;  // 宽度分段

        float innerRadius = 2.2f;
        float outerRadius = 2.8f;
        float height = 0.15f;
        float haloHeight = 0.5f;

        int vertCount = (ringSegments * (widthSegments + 1) * 2  // 正面 + 背面
                       + ringSegments * widthSegments * 2        // 内侧 + 外侧
                       + ringSegments * 2);                       // 光晕

        float[] vertices = new float[vertCount * 12]; // 12 floats per vertex (pos3+norm3+tex2+surface1)
        int vi = 0;

        // 生成立面（内侧面 + 外侧面）
        for (int i = 0; i < ringSegments; i++) {
            float a1 = (float) i / ringSegments * Mathf.PI * 2;
            float a2 = (float) (i + 1) / ringSegments * Mathf.PI * 2;
            float x1 = Mathf.cos(a1), z1 = Mathf.sin(a1);
            float x2 = Mathf.cos(a2), z2 = Mathf.sin(a2);

            // 内侧面
            addVertex(vertices, vi,
                innerRadius * x1, 0, innerRadius * z1,
                x1, 0, z1,
                (float) i / ringSegments, 0, 1);
            vi += 12;
            addVertex(vertices, vi,
                innerRadius * x1, height, innerRadius * z1,
                x1, 0, z1,
                (float) i / ringSegments, 1, 1);
            vi += 12;
            addVertex(vertices, vi,
                innerRadius * x2, 0, innerRadius * z2,
                x2, 0, z2,
                (float) (i + 1) / ringSegments, 0, 1);
            vi += 12;
            addVertex(vertices, vi,
                innerRadius * x1, height, innerRadius * z1,
                x1, 0, z1,
                (float) i / ringSegments, 1, 1);
            vi += 12;
            addVertex(vertices, vi,
                innerRadius * x2, height, innerRadius * z2,
                x2, 0, z2,
                (float) (i + 1) / ringSegments, 1, 1);
            vi += 12;
            addVertex(vertices, vi,
                innerRadius * x2, 0, innerRadius * z2,
                x2, 0, z2,
                (float) (i + 1) / ringSegments, 0, 1);
            vi += 12;

            // 外侧面
            addVertex(vertices, vi,
                outerRadius * x1, 0, outerRadius * z1,
                -x1, 0, -z1,
                (float) i / ringSegments, 0, 2);
            vi += 12;
            addVertex(vertices, vi,
                outerRadius * x2, 0, outerRadius * z2,
                -x2, 0, -z2,
                (float) (i + 1) / ringSegments, 0, 2);
            vi += 12;
            addVertex(vertices, vi,
                outerRadius * x1, height, outerRadius * z1,
                -x1, 0, -z1,
                (float) i / ringSegments, 1, 2);
            vi += 12;
            addVertex(vertices, vi,
                outerRadius * x1, height, outerRadius * z1,
                -x1, 0, -z1,
                (float) i / ringSegments, 1, 2);
            vi += 12;
            addVertex(vertices, vi,
                outerRadius * x2, 0, outerRadius * z2,
                -x2, 0, -z2,
                (float) (i + 1) / ringSegments, 0, 2);
            vi += 12;
            addVertex(vertices, vi,
                outerRadius * x2, height, outerRadius * z2,
                -x2, 0, -z2,
                (float) (i + 1) / ringSegments, 1, 2);
            vi += 12;
        }

        // 生成正面和背面
        for (int side = 0; side < 2; side++) {
            float ny = side == 0 ? 1 : -1;
            float vy = side == 0 ? 0.01f : -0.01f;
            int surfaceType = side == 0 ? 0 : 0; // face

            for (int i = 0; i < ringSegments; i++) {
                for (int j = 0; j < widthSegments; j++) {
                    float t1 = (float) j / widthSegments;
                    float t2 = (float) (j + 1) / widthSegments;
                    float r1 = innerRadius + (outerRadius - innerRadius) * t1;
                    float r2 = innerRadius + (outerRadius - innerRadius) * t2;

                    float a1 = (float) i / ringSegments * Mathf.PI * 2;
                    float a2 = (float) (i + 1) / ringSegments * Mathf.PI * 2;
                    float x1i = Mathf.cos(a1), z1i = Mathf.sin(a1);
                    float x2i = Mathf.cos(a2), z2i = Mathf.sin(a2);

                    float x1o = Mathf.cos(a1), z1o = Mathf.sin(a1);
                    float x2o = Mathf.cos(a2), z2o = Mathf.sin(a2);

                    // 四边形顶点
                    addVertex(vertices, vi, r1 * x1i, vy, r1 * z1i, 0, ny, 0, (float) i / ringSegments, t1, surfaceType); vi += 12;
                    addVertex(vertices, vi, r1 * x2i, vy, r1 * z2i, 0, ny, 0, (float) (i+1) / ringSegments, t1, surfaceType); vi += 12;
                    addVertex(vertices, vi, r2 * x2o, vy, r2 * z2o, 0, ny, 0, (float) (i+1) / ringSegments, t2, surfaceType); vi += 12;
                    addVertex(vertices, vi, r1 * x1i, vy, r1 * z1i, 0, ny, 0, (float) i / ringSegments, t1, surfaceType); vi += 12;
                    addVertex(vertices, vi, r2 * x2o, vy, r2 * z2o, 0, ny, 0, (float) (i+1) / ringSegments, t2, surfaceType); vi += 12;
                    addVertex(vertices, vi, r2 * x1o, vy, r2 * z1o, 0, ny, 0, (float) i / ringSegments, t2, surfaceType); vi += 12;
                }
            }
        }

        // 生成光晕（halo）
        int haloSegments = 60;
        for (int i = 0; i < haloSegments; i++) {
            float a1 = (float) i / haloSegments * Mathf.PI * 2;
            float a2 = (float) (i + 1) / haloSegments * Mathf.PI * 2;
            float r1 = innerRadius - 0.1f;
            float r2 = outerRadius + 0.2f;
            float h1 = height;
            float h2 = haloHeight;

            addVertex(vertices, vi, r1 * Mathf.cos(a1), h1, r1 * Mathf.sin(a1), 0, 1, 0, (float) i / haloSegments, 0, 3); vi += 12;
            addVertex(vertices, vi, r1 * Mathf.cos(a2), h1, r1 * Mathf.sin(a2), 0, 1, 0, (float) (i+1) / haloSegments, 0, 3); vi += 12;
            addVertex(vertices, vi, r2 * Mathf.cos(a2), h2, r2 * Mathf.sin(a2), 0, 1, 0, (float) (i+1) / haloSegments, 1, 3); vi += 12;
            addVertex(vertices, vi, r1 * Mathf.cos(a1), h1, r1 * Mathf.sin(a1), 0, 1, 0, (float) i / haloSegments, 0, 3); vi += 12;
            addVertex(vertices, vi, r2 * Mathf.cos(a2), h2, r2 * Mathf.sin(a2), 0, 1, 0, (float) (i+1) / haloSegments, 1, 3); vi += 12;
            addVertex(vertices, vi, r2 * Mathf.cos(a1), h2, r2 * Mathf.sin(a1), 0, 1, 0, (float) i / haloSegments, 1, 3); vi += 12;
        }

        Mesh mesh = new Mesh(false);
        mesh.getVertices(vertices.length / 12);
        mesh.setVertices(vertices);

        // 设置顶点属性
        mesh.attributes = new VertexAttributes(
            new VertexAttribute("a_position", 3),
            new VertexAttribute("a_normal", 3),
            new VertexAttribute("a_texCoord0", 2),
            new VertexAttribute("a_surface", 1)
        );

        return mesh;
    }

    private void addVertex(float[] verts, int idx,
            float x, float y, float z,
            float nx, float ny, float nz,
            float u, float v, float surface) {
        verts[idx] = x;      verts[idx+1] = y;  verts[idx+2] = z;
        verts[idx+3] = nx;   verts[idx+4] = ny; verts[idx+5] = nz;
        verts[idx+6] = u;    verts[idx+7] = v;
        verts[idx+8] = surface;
    }

    @Override
    public void preRender(PlanetParams params) {
        // 计算光照方向
        Vec3 light = new Vec3(1, 1, 1).nor();
        Vec3.toRadix52(light);
        lightDir.set(light);

        shader.bind();
        shader.setUniformf("u_time", Time.globalTime / 60f);
        shader.setUniformf("u_alpha", 0.85f);
        shader.setUniformf("u_lightdir", light.x, light.y, light.z);
        shader.setUniformf("u_campos",
            planet.position.x,
            planet.position.y,
            planet.position.z);
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        // 先渲染星球mesh
        super.render(params, projection, transform);

        // 再渲染星环
        if (ringMesh != null && !ringMesh.isDisposed()) {
            preRender(params);
            shader.bind();
            shader.setUniformMatrix4("u_proj", projection.val);
            shader.setUniformMatrix4("u_trans", transform.val);
            shader.apply();
            Gl.depthMask(false);
            Gl.blend(Gl.blendAdd);
            ringMesh.render(shader, Gl.triangles);
            Gl.blend(Gl.blend);
            Gl.depthMask(true);
        }
    }

    @Override
    public void dispose() {
        if (ringMesh != null) ringMesh.dispose();
    }
}
