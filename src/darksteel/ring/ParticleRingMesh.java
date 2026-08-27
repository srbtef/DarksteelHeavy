package darksteel.ring;

import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.VertexAttribute;
import arc.math.Mathf;
import arc.math.Rand;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.PlanetMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;

import java.nio.FloatBuffer;

/** 简单的点粒子环实现，用 Mesh 的点渲染模拟粒子环 */
public class ParticleRingMesh extends PlanetMesh {
    private final Mesh mesh;

    public ParticleRingMesh(Planet planet, float radius, int particles, Color color, boolean glow) {
        this(planet, radius, particles, color, glow, glow ? 20f : 12f);
    }

    public ParticleRingMesh(Planet planet, float radius, int particles, Color color, boolean glow, float pointSize) {
        super(planet, null, Shaders.clouds);
        float localPointSize = pointSize;
        int count = Math.max(1, particles);

        // 每个粒子由两个三角形（6 顶点）组成，以提供更可控的大小和平滑度
        int verts = count * 6;
        mesh = new Mesh(true, verts, 0, VertexAttribute.position3, VertexAttribute.color);
        FloatBuffer buf = mesh.getVerticesBuffer();
        buf.clear();

        Rand rand = new Rand(12345);
        for (int i = 0; i < count; i++) {
            float angle = rand.random(0f, Mathf.PI2);
            float r = radius + rand.random(-0.02f, 0.02f);
            float y = rand.random(-0.01f, 0.01f);
            float cx = Mathf.cos(angle) * r;
            float cz = Mathf.sin(angle) * r;

            // 粒子世界尺寸：基于 pointSize 缩放到合适的世界单位
            float half = 0.0015f * localPointSize;

            // 切线与上向量
            float tx = -Mathf.sin(angle);
            float tz = Mathf.cos(angle);
            float ux = 0f, uy = 1f, uz = 0f;

            // 四个角
            float ax = cx + tx * half + ux * half;
            float ay = y + uy * half;
            float az = cz + tz * half + uz * half;

            float bx = cx - tx * half + ux * half;
            float by = y + uy * half;
            float bz = cz - tz * half + uz * half;

            float cx2 = cx - tx * half - ux * half;
            float cy2 = y - uy * half;
            float cz2 = cz - tz * half - uz * half;

            float dx = cx + tx * half - ux * half;
            float dy = y - uy * half;
            float dz = cz + tz * half - uz * half;

            float packed = color.toFloatBits();

            // 三角形 1: A B C
            buf.put(ax).put(ay).put(az).put(packed);
            buf.put(bx).put(by).put(bz).put(packed);
            buf.put(cx2).put(cy2).put(cz2).put(packed);

            // 三角形 2: A C D
            buf.put(ax).put(ay).put(az).put(packed);
            buf.put(cx2).put(cy2).put(cz2).put(packed);
            buf.put(dx).put(dy).put(dz).put(packed);
        }
        buf.flip();
    }

    @Override
    public void render(PlanetParams params, arc.math.geom.Mat3D projection, arc.math.geom.Mat3D transform) {
        if (mesh == null) return;
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        shader.apply();
        mesh.render(shader, Gl.triangles);
    }

    @Override
    public void dispose() {
        if (mesh != null) mesh.dispose();
    }
}
