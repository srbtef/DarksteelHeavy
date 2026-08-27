package darksteel.ring;

import arc.graphics.Color;
import arc.graphics.Gl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
    private final float pointSize;

    public ParticleRingMesh(Planet planet, float radius, int particles, Color color, boolean glow) {
        this(planet, radius, particles, color, glow, glow ? 20f : 12f);
    }

    public ParticleRingMesh(Planet planet, float radius, int particles, Color color, boolean glow, float pointSize) {
        super(planet, null, Shaders.clouds);
        this.pointSize = pointSize;
        int count = Math.max(1, particles);
        mesh = new Mesh(true, count, 0, VertexAttribute.position3, VertexAttribute.color);
        FloatBuffer buf = mesh.getVerticesBuffer();
        buf.clear();

        Rand rand = new Rand(12345);
        for (int i = 0; i < count; i++) {
            float angle = rand.random(0f, Mathf.PI2);
            float r = radius + rand.random(-0.02f, 0.02f);
            float y = rand.random(-0.01f, 0.01f);
            float x = Mathf.cos(angle) * r;
            float z = Mathf.sin(angle) * r;
            float packed = color.toFloatBits();
            buf.put(x).put(y).put(z).put(packed);
        }
        buf.flip();
    }

    @Override
    public void render(PlanetParams params, arc.math.geom.Mat3D projection, arc.math.geom.Mat3D transform) {
        if (mesh == null) return;
        // 设置点大小和平滑混合以获得更宽、更平滑的粒子效果（如果可用）
        try {
            GL20 gl = Gdx.gl20;
            if (gl != null) {
                gl.glEnable(GL20.GL_BLEND);
                gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                gl.glPointSize(pointSize);
            }
        } catch (Throwable ignored) {
        }

        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        shader.apply();
        mesh.render(shader, Gl.points);
    }

    @Override
    public void dispose() {
        if (mesh != null) mesh.dispose();
    }
}
