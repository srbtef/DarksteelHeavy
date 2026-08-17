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

    /** 自定义高山星球生成器 */
    public static class CeciliaGenerator extends PlanetGenerator {
        @Override
        protected void genTile(Vec3 position, TileGen tile) {
            // 使用噪声生成地形高度
            float noise = noise(position.x, position.y, 8, 0.5, 110);
            float h = (float)(Simplex.noise3d(
                (long)(seed + 1), 8, 0.5, 1f / 110,
                position.x, position.y, position.z
            ) + 1) / 2f;

            if (h > 0.78f) {
                // 高山
                tile.floor = Blocks.peak1;
                tile.block = Blocks.peak1;
            } else if (h > 0.62f) {
                // 岩石
                tile.floor = Blocks.stone;
                tile.block = Blocks.stone;
            } else if (h > 0.48f) {
                // 粗糙岩石
                tile.floor = Blocks.roughRocks;
            } else if (h > 0.32f) {
                // 普通岩石
                tile.floor = Blocks.rock;
            } else {
                // 地面
                tile.floor = Blocks.floor1;
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
