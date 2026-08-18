package mindustry.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import static mindustry.Vars.*;

/**
 * Erekir星球生成器【已移除全部蒸汽喷口】：厄雷基尔高温岩石星球
 * 负责星球每个区块sector的地形、地板、墙体、矿石、路径、装饰物、游戏规则生成
 * 继承PlanetGenerator星球生成器基类
 */
public class ErekirPlanetGenerator extends PlanetGenerator{
    // ========== 高度噪声参数，控制星球地表起伏 ==========
    public float heightScl = 0.9f;    //高度噪声缩放，越小地形变化越剧烈
    public float octaves = 8;          //噪声八度，数值越高地形细节越多
    public float persistence = 0.7f;   //噪声持续度，控制高频噪声权重
    public float heightPow = 3f;       //高度幂次，放大高低差，让高山更高洼地更低
    public float heightMult = 1.6f;    //高度整体乘数

    // ========== Arkyic(阿基石/绿晶岩)相关静态噪声参数 ==========
    //TODO 待优化：可以内联或者删除这些常量
    public static float arkThresh = 0.28f;  //阿基石噪声阈值，大于该值生成阿基岩
    public static float arkScl = 0.83f;     //阿基石噪声缩放
    public static int arkSeed = 7;          //阿基石独立随机种子
    public static int arkOct = 2;           //阿基石噪声八度

    // ========== 液体/阿基石地板参数 ==========
    public static float liqThresh = 0.64f;  //阿基石液态地板噪声阈值
    public static float liqScl = 87f;       //液态阿基石噪声缩放
    public static float redThresh = 3.1f;   //红石高温阈值，温度超过该值生成redStone红石
    public static float noArkThresh = 0.3f;  //抑制阿基石的温度偏移阈值

    // ========== 晶体石头参数 ==========
    public static int crystalSeed = 8;       //晶体独立种子
    public static int crystalOct = 2;        //晶体噪声八度
    public static float crystalScl = 0.9f;   //晶体噪声缩放
    public static float crystalMag = 0.3f;   //晶体噪声强度

    // ========== 空气/镂空墙体噪声参数 ==========
    public static float airThresh = 0.13f;   //镂空墙体阈值，超过则墙体变成空气
    public static float airScl = 14;          //镂空噪声缩放

    /**
     * 基础地形地板数组，根据rawHeight原始高度采样索引
     * regolith浮土，yellowStone黄石，rhyolite流纹岩，carbonStone碳石
     */
    Block[] terrain = {Blocks.regolith, Blocks.regolith, Blocks.regolith, Blocks.regolith, Blocks.yellowStone, Blocks.rhyolite, Blocks.rhyolite, Blocks.carbonStone};

    {
        baseSeed = 2;                          //该星球基础随机种子
        defaultLoadout = Loadouts.basicBastion;//进入该星球默认开局装备：堡垒基础套装
    }

    /**
     * 生成一个星球区块sector，这里目前留空，不在此生成AI基地
     * @param sector 需要生成的区块
     */
    @Override
    public void generateSector(Sector sector){
        //no bases right now 暂时不在这生成敌方基地
    }

    /**
     * 获取星球球体上某三维坐标的地表高度
     * @param position 星球表面三维坐标Vec3
     * @return 经过幂运算和乘数处理后的最终高度
     */
    @Override
    public float getHeight(Vec3 position){
        //原始高度做pow幂次放大，再乘以高度系数
        return Mathf.pow(rawHeight(position), heightPow) * heightMult;
    }

    /**
     * 获取星球球体上坐标对应的地表颜色，用于星球大地图预览渲染
     * @param position 星球三维坐标
     * @return 渲染用Color
     */
    @Override
    public Color getColor(Vec3 position){
        Block block = getBlock(position);
        //晶体石头强制替换为晶体地板颜色，让星球预览晶体效果更明显
        if(block == Blocks.crystallineStone) block = Blocks.crystalFloor;
        //TODO 这个颜色可能太绿了，暂时注释
        //if(block == Blocks.beryllicStone) block = Blocks.arkyicStone;
        //取方块地图颜色，albedo反照率做透明度修正
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    /**
     * 获取星球区块尺寸缩放系数，控制sector地图实际大小
     * @return 缩放float值
     */
    @Override
    public float getSizeScl(){
        //TODO 疑问：区块应该是600还是500方块大小？
        return 2000 * 1.07f * 6f / 5f;
    }

    /**
     * 计算原始 simplex噪声高度，未经过pow和mult处理
     * @param position 星球三维坐标
     * @return simplex3d噪声输出原始高度值
     */
    float rawHeight(Vec3 position){
        //Simplex 3D噪声，基于星球种子，输入坐标做偏移避免0值重复
        return Simplex.noise3d(seed, octaves, persistence, 1f/heightScl, 10f + position.x, 10f + position.y, 10f + position.z);
    }

    /**
     * 计算该坐标的原始温度值（厄雷基尔星球核心逻辑）
     * 越靠近星球(0,0,1)极点温度越高；叠加三维噪声扰动温度分布
     * @param position 星球三维坐标
     * @return 温度数值，用于判断红石、碳石、晶体、阿基岩
     */
    float rawTemp(Vec3 position){
        //距离极点距离*2.2 减去噪声扰动，噪声越大局部温度越低
        return position.dst(0, 0, 1)*2.2f - Simplex.noise3d(seed, 8, 0.54f, 1.4f, 10f + position.x, 10f + position.y, 10f + position.z) * 2.9f;
    }

    /**
     * 根据星球三维坐标获取对应地板方块（星球球体采样）
     * 核心逻辑：高度选基础地形，温度rawTemp决定晶体、碳石、阿基岩、红石
     * @param position 星球三维坐标
     * @return Block地板方块
     */
    Block getBlock(Vec3 position){
        //获取该点温度
        float ice = rawTemp(position);
        Tmp.v32.set(position);
        float height = rawHeight(position);
        Tmp.v31.set(position);

        height *= 1.2f;                 //高度放大
        height = Mathf.clamp(height);   //限制高度在0~1之间

        //用高度对terrain数组采样获取基础地表方块
        Block result = terrain[Mathf.clamp((int)(height * terrain.length), 0, terrain.length - 1)];

        //【晶体石头生成】温度低 + ridged山脊噪声满足条件 → crystallineStone晶体岩石
        if(ice < 0.3 + Math.abs(Ridged.noise3d(seed + crystalSeed, position.x + 4f, position.y + 8f, position.z + 1f, crystalOct, crystalScl)) * crystalMag){
            return Blocks.crystallineStone;
        }

        //温度较低时，如果基础地形是流纹岩/黄石/浮土 → 替换为碳石carbonStone
        if(ice < 0.6){
            if(result == Blocks.rhyolite || result == Blocks.yellowStone || result == Blocks.regolith){
                //TODO 想法：可以做生物发光/冰之类地块，现在先用碳石替代
                return Blocks.carbonStone;
            }
        }

        position = Tmp.v32;
        //TODO 需要调参让分布更自然；TODO 边缘畸变
        //温度合适 + ridged噪声超过阈值 → beryllicStone铍基岩（阿基岩母岩）
        if(ice < redThresh - noArkThresh && Ridged.noise3d(seed + arkSeed, position.x + 2f, position.y + 8f, position.z + 1f, arkOct, arkScl) > arkThresh){
            result = Blocks.beryllicStone;
        }

        //高温判定：温度大于redThresh → 红石redStone
        if(ice > redThresh){
            result = Blocks.redStone;
        }else if(ice > redThresh - 0.4f){
            //接近高温区间，替换成浮土regolith；TODO过渡太生硬需要调优
            result = Blocks.regolith;
        }
        return result;
    }

    /**
     * 生成单个Tile瓦片：给地板、墙体赋值，处理局部镂空
     * @param position 星球三维坐标
     * @param tile 待生成的TileGen瓦片生成对象
     */
    @Override
    public void genTile(Vec3 position, TileGen tile){
        //获取基础地板方块
        tile.floor = getBlock(position);

        //流纹岩小概率生成流纹岩陨石坑地板
        if(tile.floor == Blocks.rhyolite && rand.chance(0.01)){
            tile.floor = Blocks.rhyoliteCrater;
        }

        //默认墙体 = 当前地板对应的墙体
        tile.block = tile.floor.asFloor().wall;

        //ridged噪声判定，满足条件墙体直接变成空气，制造地形镂空悬崖
        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 2, airScl) > airThresh){
            tile.block = Blocks.air;
        }

        //另一层噪声，局部强制覆盖为碳石地板 TODO：控制碳石只出现在特定生物群系
        if(Ridged.noise3d(seed + 2, position.x, position.y + 4f, position.z, 3, 6f) > 0.6){
            tile.floor = Blocks.carbonStone;
        }
    }

    /**
     * 核心generate函数：完整生成一张sector地图的所有细节
     * 在genTile球体采样之后，做2D后处理：噪声重写地板、路径、【已移除蒸汽喷口】矿石、装饰物、游戏规则
     */
    @Override
    protected void generate(){
        //获取本区块中心点温度
        float temp = rawTemp(sector.tile.v);

        //如果区块整体温度很高
        if(temp > 0.7){
            pass((x, y) -> { //pass遍历地图所有瓦片执行回调
                if(floor != Blocks.redIce){
                    float noise = noise(x + 782, y, 7, 0.8f, 280f, 1f);
                    if(noise > 0.62f){
                        //噪声极高生成熔渣slag，否则生成黄石yellowStone
                        if(noise > 0.635f){
                            floor = Blocks.slag;
                        }else{
                            floor = Blocks.yellowStone;
                        }
                        ore = Blocks.air; //清除矿石
                    }
                    //TODO 需要调参；beryllicStone铍基岩局部噪声改成黄石
                    if(noise > 0.55f && floor == Blocks.beryllicStone){
                        floor = Blocks.yellowStone;
                    }
                }
            });
        }

        cells(4); //细胞噪声，做地块斑块分化

        //浮土regolith局部生成浮土墙，让地形墙体更密集
        pass((x, y) -> {
            if(floor == Blocks.regolith && noise(x, y, 3, 0.4f, 13f, 1f) > 0.59f){
                block = Blocks.regolithWall;
            }
        });

        //TODO 黄色浮土生物群系调优；TODO冰生物群系

        //===== A星寻路生成一条贯穿地图的通路，给敌人通行 =====
        float length = width/2.6f;
        //随机角度生成起点、终点（对角）
        Vec2 trns = Tmp.v1.trns(rand.random(360f), length);
        int
        spawnX = (int)(trns.x + width/2f), spawnY = (int)(trns.y + height/2f),
        endX = (int)(-trns.x + width/2f), endY = (int)(-trns.y + height/2f);
        float maxd = Mathf.dst(width/2f, height/2f);

        erase(spawnX, spawnY, 15); //出生点清空半径15，放核心
        //A星寻路：代价函数，固体块代价极高，优先远离地图中心；manhattan曼哈顿启发函数
        brush(pathfind(spawnX, spawnY, endX, endY, tile -> (tile.solid() ? 300f : 0f) + maxd - tile.dst(width/2f, height/2f)/10f, Astar.manhattan), 9);
        erase(endX, endY, 15);     //敌方出生点清空半径15

        //===== 阿基石 arkycite 后处理 =====
        pass((x, y) -> {
            if(floor != Blocks.beryllicStone) return;

            //铍基岩局部噪声转成阿基石地板 arkyicStone
            if(Math.abs(noise(x, y + 500f, 5, 0.6f, 40f, 1f) - 0.5f) < 0.09f){
                floor = Blocks.arkyicStone;
            }
            if(nearWall(x, y)) return; //靠近墙体跳过

            //噪声达标生成液态阿基石地板 arkyciteFloor
            float noise = noise(x + 300, y - x*1.6f + 100, 4, 0.8f, liqScl, 1f);
            if(noise > liqThresh){
                floor = Blocks.arkyciteFloor;
            }
        });

        median(2, 0.6, Blocks.arkyciteFloor);  //中值滤波，平滑液态阿基石，消除噪点碎块
        blend(Blocks.arkyciteFloor, Blocks.arkyicStone, 4);
        //TODO may overwrite floor blocks under walls and look bad
        blend(Blocks.slag, Blocks.yellowStonePlates, 4);
        distort(10f, 12f);
        distort(5f, 7f);
        //does arkycite need smoothing?
        median(2, 0.6, Blocks.arkyciteFloor);
        //smooth out slag to prevent random 1‑tile patches
        median(3, 0.6, Blocks.slag);

        pass((x, y) -> {
            //rough rhyolite
            if(noise(x, y + 600 + x, 5, 0.86f, 60f, 1f) < 0.41f && floor == Blocks.rhyolite){
                floor = Blocks.roughRhyolite;
            }
            if(floor == Blocks.slag && Mathf.within(x, y, spawnX, spawnY, 30f + noise(x, y, 2, 0.8f, 9f, 15f))){
                floor = Blocks.yellowStonePlates;
            }
            if((floor == Blocks.arkyciteFloor || floor == Blocks.arkyicStone) && block.isStatic()){
                block = Blocks.arkyicWall;
            }
            float max = 0;
            for(Point2 p : Geometry.d8){
                //TODO I think this is the cause of lag
                max = Math.max(max, world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                block = floor.asFloor().wall;
                if(block == Blocks.air) block = Blocks.yellowStoneWall;
            }
            if(floor == Blocks.yellowStonePlates && noise(x + 78 + y, y, 3, 0.8f, 6f, 1f) > 0.44f){
                floor = Blocks.yellowStone;
            }
            if(floor == Blocks.redStone && noise(x + 78 - y, y, 4, 0.73f, 19f, 1f) > 0.63f){
                floor = Blocks.denseRedStone;
            }
        });

        inverseFloodFill(tiles.getn(spawnX, spawnY));
        //TODO veins, blend after inverse flood fill?
        blend(Blocks.redStoneWall, Blocks.denseRedStone, 4);
        //make sure enemies have room
        erase(endX, endY, 6);
        //TODO enemies get stuck on 1x1 passages.
        tiles.getn(endX, endY).setOverlay(Blocks.spawn);

        //矿石生成逻辑
        pass((x, y) -> {
            if(block != Blocks.air){
                if(nearAir(x, y)){
                    if(block == Blocks.carbonWall && noise(x + 78, y, 4, 0.7f, 33f, 1f) > 0.52f){
                        block = Blocks.graphiticWall;
                    }else if(block != Blocks.carbonWall && noise(x + 782, y, 4, 0.8f, 38f, 1f) > 0.665f){
                        ore = Blocks.wallOreBeryllium;
                    }
                }
            }else if(!nearWall(x, y)){
                if(noise(x + 150, y + x*2 + 100, 4, 0.8f, 55f, 1f) > 0.76f){
                    ore = Blocks.oreTungsten;
                }
                //TODO design ore generation so it doesn't overlap
                if(noise(x + 999, y + 600 - x, 4, 0.63f, 45f, 1f) < 0.27f && floor == Blocks.crystallineStone){
                    ore = Blocks.oreCrystalThorium;
                }
            }
            if(noise(x + 999, y + 600 - x, 5, 0.8f, 45f, 1f) < 0.44f && floor == Blocks.crystallineStone){
                floor = Blocks.crystalFloor;
            }
            if(block == Blocks.air && (floor == Blocks.crystallineStone || floor == Blocks.crystalFloor) && rand.chance(0.09) && nearWall(x, y)
                && !near(x, y, 4, Blocks.crystalCluster) && !near(x, y, 4, Blocks.vibrantCrystalCluster)){
                block = floor == Blocks.crystalFloor ? Blocks.vibrantCrystalCluster : Blocks.crystalCluster;
                ore = Blocks.air;
            }
            if(block == Blocks.arkyicWall && rand.chance(0.23) && nearAir(x, y) && !near(x, y, 3, Blocks.crystalOrbs)){
                block = Blocks.crystalOrbs;
                ore = Blocks.air;
            }
            //TODO test, different placement
            //TODO this biome should have more blocks in general
            if(block == Blocks.regolithWall && rand.chance(0.3) && nearAir(x, y) && !near(x, y, 3, Blocks.crystalBlocks)){
                block = Blocks.crystalBlocks;
                ore = Blocks.air;
            }
        });

        //remove props near ores, they're too annoying
        pass((x, y) -> {
            if(ore.asFloor().wallOre || block.itemDrop != null || (block == Blocks.air && ore != Blocks.air)){
                removeWall(x, y, 3, b -> b instanceof TallBlock);
            }
        });

        trimDark();

        //==================== 【已完全删除蒸汽喷口vents全部代码块】 ====================

        //清理无效overlay，某些overlay需要地面，没有地面就清除
        for(Tile tile : tiles){
            if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                tile.setOverlay(Blocks.air);
            }
        }

        decoration(0.017f);
        //it is very hot
        state.rules.env = sector.planet.defaultEnv;
        state.rules.placeRangeCheck = true;
        //TODO remove slag and arkycite around core.
        Schematics.placeLaunchLoadout(spawnX, spawnY);
        //all sectors are wave sectors
        state.rules.waves = false;
        state.rules.showSpawns = true;
    }
}
