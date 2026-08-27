package darksteel.content;


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
        // 使用普通 Planet，保持外观简单以提高性能
        fusionPlanet = new Planet("fusion-planet", Planets.sun, 1f, 2);
        fusionPlanet.localizedName = "Fusion World";
        fusionPlanet.visible = true;
        fusionPlanet.accessible = true;
        fusionPlanet.alwaysUnlocked = true;

        // 使用原版赛普罗（Serpulo）的外观设置以匹配原版样貌
        fusionPlanet.meshLoader = Planets.serpulo.meshLoader;
        fusionPlanet.cloudMeshLoader = Planets.serpulo.cloudMeshLoader;
        fusionPlanet.bloom = Planets.serpulo.bloom;
        fusionPlanet.defaultEnv = Planets.serpulo.defaultEnv;
        fusionPlanet.atmosphereColor = Planets.serpulo.atmosphereColor;
        fusionPlanet.atmosphereRadIn = Planets.serpulo.atmosphereRadIn;
        fusionPlanet.atmosphereRadOut = Planets.serpulo.atmosphereRadOut;
        fusionPlanet.allowLaunchToNumbered = Planets.serpulo.allowLaunchToNumbered;
        fusionPlanet.startSector = 32;
        fusionPlanet.defaultCore = Blocks.coreShard;

        // 禁用在内容加载时创建复杂网格以避免引擎初始化顺序导致的 NPE
        fusionPlanet.meshLoader = null;
        fusionPlanet.cloudMeshLoader = null;

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