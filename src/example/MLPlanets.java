package example.mymod;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.generators.*;
import mindustry.maps.planet.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import static mindustry.Vars.*;

public class MLPlanets {
    public static Planet cecilia;

    /** 自定义高山地形生成器 */
    public static class CeciliaGenerator extends PlanetGenerator {
        public float[] generateHeight(Watermap world) {
            int width = world.width, height = world.height;
            float[] h = new float[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float dx = (float)x / width - 0.5f;
                    float dy = (float)y / height - 0.5f;
                    float base = (float)Math.sqrt(dx * dx + dy * dy);
                    // 中央为高山，往外逐渐降低
                    float mountain = 1.2f - base * 3.5f;
                    // 用噪声增加细节
                    float noise = (float)OctaveGenerator(3, 0.45f).getFloat(x * 0.03f, y * 0.03f);
                    h[y * width + x] = Math.max(0f, mountain + noise * 0.4f);
                }
            }
            return h;
        }

        @Override
        public void generateTile(Sector sector, TileGen tile, float[] height, int x, int y) {
            float h = height[y * sector.tileWidth + x];
            tile.height = h;

            if (h > 0.82f) {
                tile.block = Blocks.mountain; // 高山
            } else if (h > 0.65f) {
                tile.block = Blocks.stone;    // 岩石
            } else if (h > 0.45f) {
                tile.block = Blocks.roughRocks; // 粗糙岩石
            } else if (h > 0.25f) {
                tile.block = Blocks.rock;     // 普通石头
            } else {
                tile.block = Blocks.floor1;   // 地面
            }
        }
    }

    public static void load() {
         cecilia = new Planet("cecilia", Planets.sun, 1f, 3) {{
            generator = new CeciliaGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("97B5EDFF").a(0.75f), 2, 0.42f, 1f, 0.43f),
                    new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("97B5EDFF").a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );

            launchCapacityMultiplier = 0f;
            sectorSeed = 1;
            tidalLock = false;
            alwaysUnlocked = true;
            clearSectorOnLose = true;
            showRtsAIRule = false;
            allowCampaignRules = true;
            allowLegacyLaunchPads = false;
            allowWaves = true;
            allowLaunchLoadout = false;
            visible = true;
            drawOrbit = true;
            accessible = true;
            hasAtmosphere = true;
            updateLighting = true;
            allowLaunchToNumbered = false;
            allowSectorInvasion = true;
            bloom = false;
            allowLaunchSchematics = true;
            iconColor = Color.valueOf("97B5EDFF");
            atmosphereColor = Color.valueOf("97B5EDFF");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            orbitRadius = 40;
            rotateTime = 3000;
            startSector = 1;
            landCloudColor = Pal.spore.cpy().a(0.5f);
        }};
    }
}
