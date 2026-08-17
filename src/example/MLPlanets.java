package example;

import arc.graphics.Color;
import arc.math.geom.Vec3;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.PlanetGrid.Ptile;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.TileGen;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.gen.Sounds;
import mindustry.gen.Sector;
import mindustry.content.Fx;
import mindustry.content.TechTree;
import mindustry.type.ItemStack;

import static mindustry.Vars.*;

/**
 * 星球定义入口
 */
public class MLPlanets {
    public static Planet cecilia;

    //【独立自定义生成器类】
    public static class CeciliaGenerator extends PlanetGenerator {
        @Override
        public void genTile(Vec3 position, TileGen tile) {
            // 使用 Simplex 噪声（示例）
            float rawNoise = Simplex.noise2d(1, 4, 0.5f, 1f, position.x, position.y);
            float height = (rawNoise + 1f) / 2f;

            if (height > 0.76f) {
                tile.floor = Blocks.stone;
                tile.block = Blocks.air;
            } else if (height > 0.60f) {
                tile.floor = Blocks.stone;
                tile.block = Blocks.air;
            } else if (height > 0.45f) {
                tile.floor = Blocks.sand;
                tile.block = Blocks.air;
            } else {
                tile.floor = Blocks.ice;
                tile.block = Blocks.air;
            }
        }
    }

    public static void load() {
        cecilia = new Planet("cecilia", Planets.sun, 1f, 3) {{
            generator = new CeciliaGenerator();

            // 星球网格 + 云层（合并到 meshLoader）
            meshLoader = () -> new MultiMesh(
                new HexMesh(this, 6),
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5,
                        Color.valueOf("97B5EDFF").a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5,
                        Color.valueOf("97B5EDFF").a(0.70f), 2, 0.42f, 1.2f, 0.45f)
            );

            localizedName = "塞西莉亚";
            orbitRadius = 40f;
            rotateTime = 3000f;
            sectorSeed = 1;
            startSector = 1;

            alwaysUnlocked = true;
            accessible = true;
            drawOrbit = true;
            hasAtmosphere = true;
            // 以下属性可能不存在，注释掉或删除
            // clearSectorOnLose = true;
            // allowWaves = true;
            // allowSectorInvasion = true;
            // allowLaunchSchematics = true;
            // allowLaunchLoadout = false;
            // allowLaunchToNumbered = false;
            // launchCapacityMultiplier = 0f;
            // tidalLock = false;
            // bloom = false;
            // showRtsAIRule = false;
            // allowCampaignRules = true;
            // allowLegacyLaunchPads = false;
            // updateLighting = true;

            iconColor = Color.valueOf("97B5EDFF");
            atmosphereColor = Color.valueOf("97B5EDFF");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;

            sectors.add(new Sector(this, new Ptile(0, 0)));
        }};
    }
}