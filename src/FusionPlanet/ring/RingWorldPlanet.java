package FusionPlanet.ring;

import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.VertexAttribute;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.MatMesh;
import mindustry.graphics.g3d.PlanetMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;
import mindustry.type.Sector;

public class RingWorldPlanet extends Planet {
    private static final Mat3D sectorTransform = new Mat3D();
    private static final float sqrt3 = Mathf.sqrt(3f);

    // 赤道装饰环参数，复用CylinderRingMeshBuilder
    private GenericMesh equatorRing;
    private float ringRotation;
    private static final float RING_RADIUS = 130f;
    private static final float RING_THICKNESS = 22f;
    private static final int RING_SEGMENTS = 128;
    private static final float RING_TILT = 12f;
    private static final Color RING_MAIN_COLOR = new Color(0.85f, 0.78f, 0.68f, 0.95f);
    private static final Color RING_EDGE_COLOR = new Color(0.68f, 0.75f, 0.88f, 0.9f);

    private RingWorldMesh worldMesh;

    public final int columns;
    public final int rows;
    public final float innerRadius;
    public final float outerRadius;
    public final float halfWidth;
    public final float campaignHalfWidth;
    public final float hexSize;

    public float panelScale = 0.985f;
    public float campaignDepth = 1.35f;
    public float fillInwardBias = 0.045f;
    public float sourceLatitude = 32f;
    public float maxCameraPitch = 22f;
    public float campaignCameraMinDistance = 16f;
    public float campaignCameraZoomDistance = 6f;

    private final Vec3 intersection = new Vec3();

    public RingWorldPlanet(String name, Planet parent, float innerRadius, float outerRadius, float halfWidth, int columns, int rows) {
        super(name, parent, 1f);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.halfWidth = halfWidth;
        this.campaignHalfWidth = halfWidth * 0.73f;
        this.columns = columns;
        this.rows = rows;
        this.hexSize = Mathf.PI2 * innerRadius / (columns * 1.5f);

        grid = new RingWorldGrid(this);
        sectors = new Seq<>();
        for (int i = 0; i < grid.tiles.length; i++) {
            sectors.add(new Sector(this, grid.tiles[i]));
        }
        sectorApproxRadius = sectors.first().tile.v.dst(sectors.first().tile.corners[0].v);

        orbitRadius = 0f;
        orbitTime = Float.POSITIVE_INFINITY;
        drawOrbit = false;
        if (parent != null) parent.updateTotalRadius();

        // 构建环形世界本体
        worldMesh = new RingWorldMesh(this);
        // 构建环网格，包装为MatMesh适配原生渲染管线
        Mesh ringMesh = CylinderRingMeshBuilder.build(RING_RADIUS, RING_THICKNESS, RING_SEGMENTS, RING_MAIN_COLOR, RING_EDGE_COLOR);
        Mat3D ringMat = new Mat3D();
        ringMat.rotate(Vec3.X, RING_TILT);
        equatorRing = new MatMesh(ringMesh, ringMat);
    }

    // 严格匹配Planet原生draw方法签名
    @Override
    public void draw(PlanetParams params, Mat3D projection, Mat3D transform) {
        super.draw(params, projection, transform);

        // 渲染环形世界本体
        if (worldMesh != null) {
            worldMesh.render(params, projection, transform);
        }

        // 渲染单条赤道环，叠加自转
        if (equatorRing != null) {
            Mat3D ringTransform = Tmp.m3.set(transform);
            ringTransform.rotate(Vec3.Y, ringRotation);
            equatorRing.render(params, projection, ringTransform);
        }
    }

    @Override
    public void update() {
        super.update();
        ringRotation += Time.delta * 0.15f * 0.001f;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (worldMesh != null) worldMesh.dispose();
        if (equatorRing != null) equatorRing.dispose();
    }

    @Override
    public float getRotation() {
        return 0f;
    }

    public float centerU(int id) {
        return (id % columns) * 1.5f * hexSize;
    }

    public float centerV(int id) {
        int column = id % columns;
        int row = id / columns;
        float stagger = (column & 1) == 0 ? -0.25f : 0.25f;
        return (row - (rows - 1f) / 2f + stagger) * sqrt3 * hexSize;
    }

    public Vec3 getSurfacePoint(float u, float v, float radius, Vec3 out) {
        float angle = u / innerRadius;
        out.set(Mathf.cos(angle) * radius, v, Mathf.sin(angle) * radius);
        return out;
    }

    public Vec3 getSourcePoint(float u, float v, Vec3 out) {
        float angle = u / innerRadius;
        out.set(Mathf.cos(angle), 0f, Mathf.sin(angle));
        return out;
    }

    public Vec3 mapSurface(float u, float v, float radial, Vec3 out) {
        return getSurfacePoint(u, v, radial, out);
    }

    // 修复RingWorldGrid调用缺失的方法
    public Vec3 getSourcePoint(int id, Vec3 out) {
        float u = centerU(id);
        float v = centerV(id);
        return getSourcePoint(u, v, out);
    }

    public Vec3 getSourceCorner(int id, int cornerIndex, Vec3 out) {
        float u = centerU(id);
        float v = centerV(id);
        float angle = u / innerRadius;
        Vec3 base = new Vec3(Mathf.cos(angle), 0f, Mathf.sin(angle));
        // 简单六边形角点偏移，适配网格逻辑
        float angOff = cornerIndex * Mathf.PI/3f;
        out.set(base).rotate(Vec3.Y, angOff).scl(hexSize * 0.9f);
        out.y = v;
        return out;
    }

    // 内部地形Chunk工具类，修复vert方法缺失
    private static class TerrainChunk {
        static final int maxCells = 500;
        int cells;
        FloatSeq vertices = new FloatSeq();

        void addSurface(Vec3[] top, Vec3 normal, Color color) {
            triangle(vertices, top[0], top[1], top[2], normal, color);
            triangle(vertices, top[0], top[2], top[3], normal, color);
            triangle(vertices, top[0], top[3], top[4], normal, color);
            triangle(vertices, top[0], top[4], top[5], normal, color);
            cells++;
        }

        private static void triangle(FloatSeq verts, Vec3 a, Vec3 b, Vec3 c, Vec3 normal, Color color) {
            vert(verts, a, normal, color);
            vert(verts, b, normal, color);
            vert(verts, c, normal, color);
        }

        private static void vert(FloatSeq verts, Vec3 p, Vec3 normal, Color color) {
            verts.add(p.x, p.y, p.z);
            verts.add(normal.x, normal.y, normal.z);
            verts.add(color.toFloatBits());
        }

        Mesh build() {
            Mesh mesh = new Mesh(true, vertices.size / 7, 0,
                    VertexAttribute.position3,
                    VertexAttribute.normal,
                    VertexAttribute.color);
            mesh.setVertices(vertices.toArray());
            return mesh;
        }
    }

    private static void triangle(FloatSeq vertices, Vec3 a, Vec3 b, Vec3 c, Vec3 normal, Color color) {
        vert(vertices, a, normal, color);
        vert(vertices, b, normal, color);
        vert(vertices, c, normal, color);
    }

    private static void quad(FloatSeq vertices, Vec3 a, Vec3 b, Vec3 c, Vec3 d, Vec3 normal, Color color) {
        triangle(vertices, a, b, c, normal, color);
        triangle(vertices, a, c, d, normal, color);
    }

    private static Vec3 point(float angle, float radius, float y) {
        return new Vec3(Mathf.cos(angle) * radius, y, Mathf.sin(angle) * radius);
    }

    private static void vert(FloatSeq vertices, Vec3 p, Vec3 normal, Color color) {
        vertices.add(p.x, p.y, p.z);
        vertices.add(normal.x, normal.y, normal.z);
        vertices.add(color.toFloatBits());
    }

    private static Mesh mesh(FloatSeq vertices) {
        Mesh mesh = new Mesh(true, vertices.size, 0,
                VertexAttribute.position3,
                VertexAttribute.normal,
                VertexAttribute.color);
        mesh.setVertices(vertices.toArray());
        return mesh;
    }
}
