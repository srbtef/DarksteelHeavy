package darksteel.content;

import darksteel.ring.ParticleRingMesh;
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

        // 外圈紫色（带发光），内圈橙色，外层加一层细微发光作为粒子效果
        fusionPlanet.cloudMeshLoader = () -> {
            // 全部使用粒子层：外圈紫色，内圈橙色
            ParticleRingMesh outer = new ParticleRingMesh(fusionPlanet, 1.85f, 512, ringOuter, true, 28f);
            ParticleRingMesh inner = new ParticleRingMesh(fusionPlanet, 1.65f, 384, ringInner, false, 18f);
            return new MultiMesh(outer, inner);
        };

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