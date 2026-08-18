package FusionPlanet;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.TileGen;
import arc.math.Rand;
import arc.struct.FloatSeq;
import arc.util.Log;
import mindustry.game.Schematics;
import mindustry.type.Sector;
import mindustry.world.Tiles;
import mindustry.world.meta.Env;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.*;

/**
 * 单一星球生成器：纯厄雷基尔风格，无瑟普鲁混合
 */
public class FusionPlanetGenerator extends PlanetGenerator {

    /**厄雷基尔基础地形数组*/
    private static final Block[] erekirTerrain = {
            Blocks.regolith,
            Blocks.regolith,
            Blocks.regolith,
            Blocks.regolith,
            Blocks.yellowStone,
            Blocks.rhyolite,
            Blocks.rhyolite,
            Blocks.carbonStone
    };

    //高度噪声参数
    public float heightScl = 0.9f;
    public int octaves = 8;
    public float persistence = 0.7f;
    public float heightPow = 4f;
    public float heightMult = 0.8f;

    //阿基石参数
    public float arkThresh = 0.28f;
    public float arkScl = 0.83f;
    public int arkSeed = 7;
    public int arkOct = 2;
    public float redThresh = 3.1f;
    public float noArkThresh = 0.3f;

    //晶体参数
    public int crystalSeed = 8;
    public int crystalOct = 2;
    public float crystalScl = 0.9f;
    public float crystalMag = 0.3f;

    public float erekirHeightOffset = 0.1f;
    public float erekirHeightScale = 1.2f;

    public FusionPlanetGenerator() {
        baseSeed = 2;
        defaultLoadout = Loadouts.basicBastion;//厄雷基尔堡垒开局
    }

    /**原始高度噪声*/
    private float rawHeight(Vec3 pos) {
        float h = Simplex.noise3d(seed, octaves, persistence, 1f / heightScl,
                10f + pos.x, 10f + pos.y, 10f + pos.z);
        float val = (h * 1.4f) * 0.5f + 0.5f;
        return Mathf.clamp(val);
    }

    /**原始温度，控制晶体、红石、阿基石*/
    private float rawTemp(Vec3 pos) {
        float temp = pos.dst(0, 0, 1f) * 2.2f
                - Simplex.noise3d(seed, 8, 0.54f, 1.4f, 10f + pos.x, 10f + pos.y, 10f + pos.z) * 2.9f;
        return temp - 0.1f;
    }

    /**获取厄雷基尔风格方块*/
    private Block getErekirBlock(Vec3 pos) {
        float h = rawHeight(pos);
        float ice = rawTemp(pos);

        float height = (float)Math.pow(h, heightPow) * heightMult;
        float normHeight = Mathf.clamp((height - 0.05f) / 0.8f);
        float adjustedHeight = Mathf.clamp(normHeight * erekirHeightScale + erekirHeightOffset);

        float hNoise = Simplex.noise3d(seed + 888, 3, 0.5f, 0.3f, pos.x, pos.y, pos.z) * 0.12f;
        adjustedHeight = Mathf.clamp(adjustedHeight + hNoise);

        int idx = Mathf.clamp((int)(adjustedHeight * erekirTerrain.length), 0, erekirTerrain.length - 1);
        Block result = erekirTerrain[idx];

        float replaceNoise = Simplex.noise3d(seed + 789, 3, 0.5f, 0.2f, pos.x, pos.y, pos.z);
        if (replaceNoise > 0.3f && result == Blocks.regolith) {
            result = (replaceNoise > 0.5f) ? Blocks.yellowStone : Blocks.rhyolite;
        }

        //晶体石
        if (ice < 0.3f + Math.abs(Ridged.noise3d(seed + crystalSeed, pos.x + 4f, pos.y + 8f, pos.z + 1f, crystalOct, crystalScl)) * crystalMag) {
            return Blocks.crystallineStone;
        }
        //低温转碳石
        if (ice < 0.6f) {
            if (result == Blocks.rhyolite || result == Blocks.yellowStone || result == Blocks.regolith) {
                result = Blocks.carbonStone;
            }
        }
        //阿基岩母岩
        if (ice < redThresh - noArkThresh && Ridged.noise3d(seed + arkSeed, pos.x + 2f, pos.y + 8f, pos.z + 1f, arkOct, arkScl) > arkThresh) {
            result = Blocks.beryllicStone;
        }
        //高温红石
        if (ice > redThresh) {
            result = Blocks.redStone;
        } else if (ice > redThresh - 0.4f) {
            result = Blocks.regolith;
        }
        return result;
    }

    //单一星球，直接输出厄雷基尔地块
    public Block getBlock(Vec3 pos) {
        return getErekirBlock(pos);
    }

    @Override
    public float getHeight(Vec3 pos) {
        float h = rawHeight(pos);
        float baseHeight = (float)Math.pow(h, heightPow) * heightMult;
        Block block = getBlock(pos);
        if (block == Blocks.carbonStone) {
            return baseHeight * 0.33f;
        }
        return baseHeight;
    }

    @Override
    public void getColor(Vec3 pos, Color out) {
        Block block = getBlock(pos);
        out.set(block.mapColor);
        out.a = 1f;
    }

    @Override
    public void genTile(Vec3 pos, TileGen tile) {
        Block floor = getBlock(pos);
        tile.floor = floor;
        Block wall = floor.asFloor().wall;
        if (wall != null && wall != Blocks.air) {
            tile.block = wall;
            //噪声镂空制造悬崖
            if (Ridged.noise3d(seed + 1, pos.x, pos.y, pos.z, 2, 10f) > 0.1f) {
                tile.block = Blocks.air;
            }
        } else {
            tile.block = Blocks.air;
        }
    }

    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        Vec3 v = sector.rect.project(x, y).scl(5f);
        return Simplex.noise3d(seed, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
    }

    @Override
    public void postGenerate(Tiles tiles) {
        if (tiles == null) return;
        int w = tiles.width, h = tiles.height;
        int cx = w / 2, cy = h / 2;
        float difficulty = sector != null ? sector.threat : 0.5f;

        //中心点清空墙体，放核心
        for (int dx = -15; dx <= 15; dx++) {
            for (int dy = -15; dy <= 15; dy++) {
                if (dx * dx + dy * dy > 15 * 15) continue;
                int tx = cx + dx, ty = cy + dy;
                if (tx < 0 || tx >= w || ty < 0 || ty >= h) continue;
                Tile tile = tiles.getn(tx, ty);
                if (tile != null) tile.setBlock(Blocks.air);
            }
        }

        //厄雷基尔矿石池：铍、钨、晶体钍
        Seq<Block> ores = Seq.with(Blocks.wallOreBeryllium, Blocks.oreTungsten);
        if(rand.chance(0.4f)) ores.add(Blocks.oreCrystalThorium);

        FloatSeq frequencies = new FloatSeq();
        for (int i = 0; i < ores.size; i++) {
            frequencies.add(rand.random(-0.1f, 0.01f) - i * 0.01f);
        }

        //铺矿石overlay
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Tile tile = tiles.getn(x, y);
                if (tile == null) continue;
                Floor floor = tile.floor();
                if (floor == null || !floor.hasSurface()) continue;
                if (Math.abs(x - cx) <= 5 && Math.abs(y - cy) <= 5) continue;

                int offsetX = x - 4, offsetY = y + 23;
                for (int i = ores.size - 1; i >= 0; i--) {
                    Block entry = ores.get(i);
                    float freq = frequencies.get(i);
                    float cond1 = Math.abs(0.5f - noise(offsetX, offsetY + i * 999, 2, 0.7f, 40 + i * 2f, 1f));
                    float cond2 = Math.abs(0.5f - noise(offsetX, offsetY - i * 999, 1, 1f, 30 + i * 4f, 1f));
                    if (cond1 > 0.22f + i * 0.01f && cond2 > 0.37f + freq) {
                        tile.setOverlay(entry);
                        break;
                    }
                }
            }
        }

        //黑暗区域恢复墙体，防止悬空
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Tile tile = tiles.getn(x, y);
                if (tile == null) continue;
                if (tile.block() != Blocks.air) continue;
                float maxDark = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;
                        int nx = x + dx, ny = y + dy;
                        if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                        float dark = world.getDarkness(nx, ny);
                        if (dark > maxDark) maxDark = dark;
                    }
                }
                if (maxDark > 0) {
                    Floor floor = tile.floor();
                    if (floor != null) {
                        Block wall = floor.wall;
                        if (wall != null && wall != Blocks.air) tile.setBlock(wall);
                    }
                }
            }
        }

        //敌方出生点逻辑
        int spawnX = cx, spawnY = cy;
        Seq<Vec2> enemySpawns = new Seq<>();
        int offset = rand.nextInt(360);
        float length = w / 2.55f - rand.random(13f, 23f);

        for (int i = 0; i < 360; i += 5) {
            int angle = offset + i;
            int ex = (int)(w / 2f + Angles.trnsx(angle, length));
            int ey = (int)(h / 2f + Angles.trnsy(angle, length));
            if (ex < 0 || ex >= w || ey < 0 || ey >= h) continue;
            spawnX = ex;
            spawnY = ey;
            int enemyCount = Math.max(1, (int)(difficulty * 4));
            for (int j = 0; j < enemyCount; j++) {
                float enemyOffset = rand.range(60f);
                int ex2 = (int)(spawnX + Angles.trnsx(180f + enemyOffset, w / 2.5f));
                int ey2 = (int)(spawnY + Angles.trnsy(180f + enemyOffset, w / 2.5f));
                if (ex2 >= 0 && ex2 < w && ey2 >= 0 && ey2 < h) {
                    enemySpawns.add(new Vec2(ex2, ey2));
                }
            }
            break;
        }

        Seq<Tile> enemyTiles = new Seq<>();
        for (Vec2 e : enemySpawns) {
            Tile tile = tiles.getn((int)e.x, (int)e.y);
            if (tile != null) {
                tile.setOverlay(Blocks.spawn);
                enemyTiles.add(tile);
            }
        }

        //厄雷基尔：Malis紫色敌人队伍
        if (enemySpawns.size > 0) {
            state.rules.attackMode = true;
            state.rules.winWave = 15;
            state.rules.waveSpacing = 60f * 60f * 2f;
            state.rules.waves = true;
            state.rules.enemyCoreBuildRadius = 600f;
            state.rules.waveTeam = Team.malis;
            try {
                state.rules.spawns = Waves.generate(difficulty, new Rand(sector != null ? sector.id : 0), true, false, false);
            } catch (Exception e) {
                Log.err("波次生成失败: @", e);
            }
        } else {
            state.rules.winWave = 10;
            state.rules.waves = true;
        }

        //搜索可放核心的空位
        int coreX = spawnX, coreY = spawnY;
        boolean found = false;
        int searchRadius = 30;
        for (int r = 0; r <= searchRadius && !found; r++) {
            for (int dx = -r; dx <= r && !found; dx++) {
                for (int dy = -r; dy <= r && !found; dy++) {
                    int tx = spawnX + dx, ty = spawnY + dy;
                    if (tx < 0 || tx >= w || ty < 0 || ty >= h) continue;
                    Tile tile = tiles.getn(tx, ty);
                    if (tile == null) continue;
                    if (tile.block() != Blocks.air) continue;
                    Floor floor = tile.floor();
                    if (floor == null || floor.isLiquid) continue;
                    boolean hasWallNearby = false;
                    int checkRadius = 3;
                    for (int dx2 = -checkRadius; dx2 <= checkRadius && !hasWallNearby; dx2++) {
                        for (int dy2 = -checkRadius; dy2 <= checkRadius && !hasWallNearby; dy2++) {
                            int nx = tx + dx2, ny = ty + dy2;
                            if (nx < 0 || nx >= w || ny < 0 || ny >= h) { hasWallNearby = true; break; }
                            Tile neighbor = tiles.getn(nx, ny);
                            if (neighbor != null && neighbor.block() != Blocks.air) { hasWallNearby = true; break; }
                        }
                    }
                    if (!hasWallNearby) {
                        coreX = tx;
                        coreY = ty;
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            coreX = spawnX;
            coreY = spawnY;
            int clearRadius = 6;
            for (int dx = -clearRadius; dx <= clearRadius; dx++) {
                for (int dy = -clearRadius; dy <= clearRadius; dy++) {
                    int tx = coreX + dx, ty = coreY + dy;
                    if (tx >= 0 && tx < w && ty >= 0 && ty < h) {
                        Tile tile = tiles.getn(tx, ty);
                        if (tile != null) tile.setBlock(Blocks.air);
                    }
                }
            }
        }

        Schematics.placeLaunchLoadout(coreX, coreY);
        //厄雷基尔高温环境
        state.rules.env = Env.erekir;
        state.rules.placeRangeCheck = true;
    }

    @Override
    public float getSizeScl() {
        return 2000f;
    }
}
