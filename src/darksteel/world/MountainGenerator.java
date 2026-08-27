package darksteel.world;

import arc.math.Mathf;

/**
 * 简单的分形值噪声山地生成器。
 * 返回值在 0..1 范围内，较大的值表示更高的地形高度（用于山峰）。
 * 可参数化：seed、缩放、octaves、lacunarity、gain。
 */
public class MountainGenerator {
    private final int seed;
    private final float scale;
    private final int octaves;
    private final float lacunarity;
    private final float gain;
    private final float exponent;

    public MountainGenerator(int seed, float scale, int octaves, float lacunarity, float gain, float exponent) {
        this.seed = seed;
        this.scale = scale;
        this.octaves = Math.max(1, octaves);
        this.lacunarity = lacunarity;
        this.gain = gain;
        this.exponent = exponent;
    }

    public MountainGenerator(int seed) {
        this(seed, 2.5f, 5, 2f, 0.5f, 1.6f);
    }

    private static float fade(float t) {
        return t * t * (3f - 2f * t);
    }

    private float hash(int x, int y) {
        long h = x * 374761393L + y * 668265263L + seed * 0x9E3779B97F4A7C15L;
        h = (h ^ (h >>> 13)) * 1274126177L;
        h = h ^ (h >>> 16);
        // convert to [0,1)
        return (float) ((h & 0xffffffffL) / (double) 0x100000000L);
    }

    private float valueNoise(float x, float y) {
        int xi = (int) Math.floor(x);
        int yi = (int) Math.floor(y);
        float xf = x - xi;
        float yf = y - yi;

        float v00 = hash(xi, yi);
        float v10 = hash(xi + 1, yi);
        float v01 = hash(xi, yi + 1);
        float v11 = hash(xi + 1, yi + 1);

        float u = fade(xf);
        float v = fade(yf);

        float xa = Mathf.lerp(v00, v10, u);
        float xb = Mathf.lerp(v01, v11, u);
        return Mathf.lerp(xa, xb, v);
    }

    /**
     * 在归一化平面上采样高度值（u,v 可为任意实数），返回 0..1
     */
    public float sample(float u, float v) {
        float x = u * scale;
        float y = v * scale;
        float amplitude = 1f;
        float frequency = 1f;
        float sum = 0f;
        float max = 0f;
        for (int i = 0; i < octaves; i++) {
            sum += valueNoise(x * frequency, y * frequency) * amplitude;
            max += amplitude;
            amplitude *= gain;
            frequency *= lacunarity;
        }
        float n = sum / max;
        // 加强高地（使山峰更尖）
        n = Mathf.clamp(Mathf.pow(n, exponent), 0f, 1f);
        return n;
    }
}
