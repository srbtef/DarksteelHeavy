package darksteel.content;

import darksteel.ring.DysonRingMesh;
import mindustry.graphics.g3d.MultiMesh;
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
        fusionPlanet = new Planet("fusion-planet", Planets.sun, 1f, 2);
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

        // 使用默认网格加载器以避免在 HexMesher 未初始化时触发 NPE

        Color ringOuter = Color.valueOf("8a2be2"); // 紫色外圈
        Color ringInner = Color.valueOf("ff8c00"); // 橙色内圈

        // 多层网格星环（接近 Omloon 风格），已放大尺寸
        fusionPlanet.cloudMeshLoader = () -> new MultiMesh(
            // 主体层（更大半径、更厚）
                new DysonRingMesh(fusionPlanet, 1.95f, 0.08f, 512, ringOuter, ringInner),
                new DysonRingMesh(fusionPlanet, 2.25f, 0.07f, 768, ringOuter, ringInner),
                new DysonRingMesh(fusionPlanet, 2.55f, 0.05f, 1024, ringOuter, ringInner),
            // 发光层（略微外移并带发光标志）
            new DysonRingMesh(fusionPlanet, 1.95f + 0.04f, 0.04f, 512, ringOuter, ringOuter, true),
            new DysonRingMesh(fusionPlanet, 2.25f + 0.04f, 0.035f, 768, ringOuter, ringOuter, true),
            new DysonRingMesh(fusionPlanet, 2.55f + 0.04f, 0.03f, 1024, ringOuter, ringOuter, true)
        );

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