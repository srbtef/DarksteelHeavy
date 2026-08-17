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
        public void genTile(Vec3 position, TileGen tile) {
            // 噪声地形高度 (0~1)
            float h = (noise(position.x, position.y, 7, 0.5f, 80) + 1f) / 2f;

            // 根据高度分层
            if (h > 0.76f) {
                // 高山区：石头地面 + 高山方块
                tile.floor = Blocks.stone;
                tile.block = Blocks.stone;
            } else if (h > 0.60f) {
                // 岩石区：石头地面
                tile.floor = Blocks.stone;
                tile.block = Blocks.air;
            } else if (h > 0.45f) {
                // 沙地区
                tile.floor = Blocks.sand;
                tile.block = Blocks.air;
            } else if (h > 0.30f) {
                // 冰区
                tile.floor = Blocks.ice;
                tile.block = Blocks.air;
            } else {
                // 深冰
                tile.floor = Blocks.ice;
                tile.block = Blocks.air;
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
