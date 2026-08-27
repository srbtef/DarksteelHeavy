package darksteel.content;

import darksteel.ring.DysonRingMesh;
import darksteel.ring.RingWorldPlanet;
import darksteel.ring.RingWorldMesh;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env;



public class fPlanets {
    public static Planet fusionPlanet;
    public static SectorPreset undevelopedZone;
    

    public static void load() {
        // 使用 RingWorldPlanet 以支持自定义地形网格（带真实隆起）
        fusionPlanet = new RingWorldPlanet("fusion-planet", Planets.sun, 1.65f, 2.8f, 6f, 120, 40);
        fusionPlanet.localizedName = "Fusion World";
        fusionPlanet.visible = true;
        fusionPlanet.accessible = true;
        fusionPlanet.alwaysUnlocked = true;
        fusionPlanet.bloom = false;
        fusionPlanet.defaultEnv = Env.terrestrial;
        fusionPlanet.atmosphereColor = Color.valueOf("7a8cbf");
        fusionPlanet.atmosphereRadIn = 0.02f;
        fusionPlanet.atmosphereRadOut = 0.28f;
        fusionPlanet.allowLaunchToNumbered = true;
        fusionPlanet.startSector = 32;
        fusionPlanet.defaultCore = Blocks.coreShard;

        // 使用 RingWorldMesh 生成带山脉隆起的内壁地形
        fusionPlanet.meshLoader = () -> new RingWorldMesh((RingWorldPlanet) fusionPlanet);

        Color ringOuter = Color.valueOf("8a2be2"); // 紫色外圈
        Color ringInner = Color.valueOf("ff8c00"); // 橙色内圈

        // 保持距离较小（星环靠近行星），但增大星环“大小”（厚度/视觉规模）
        fusionPlanet.cloudMeshLoader = () -> new DysonRingMesh(fusionPlanet, 1.6f, 0.3f, 1024, ringOuter, ringInner, true);

        fusionPlanet.ruleSetter = r -> {
            r.waveTeam = Team.crux;
            r.waves = true;
            r.env = Env.terrestrial;
            r.winWave = 10;
            r.placeRangeCheck = true;
        };

        undevelopedZone = new SectorPreset("undeveloped-zone", fusionPlanet, 5);
        undevelopedZone.localizedName = "未开发区";
        undevelopedZone.description = "一片尚未被开发的区域";
        undevelopedZone.difficulty = 1;
        undevelopedZone.captureWave = 20;
        undevelopedZone.alwaysUnlocked = true;
    }
}