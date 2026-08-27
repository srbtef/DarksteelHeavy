package darksteel.content;


import arc.graphics.Color;
import darksteel.content.SimplePlanetMesh;
import darksteel.ring.ParticleRingMesh;
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

        // 使用原版赛普罗（Serpulo）的外观设置以匹配原版样貌（云层/气候等）
        fusionPlanet.cloudMeshLoader = Planets.serpulo.cloudMeshLoader;
        fusionPlanet.bloom = Planets.serpulo.bloom;
        fusionPlanet.defaultEnv = Planets.serpulo.defaultEnv;
        fusionPlanet.atmosphereColor = Planets.serpulo.atmosphereColor;
        fusionPlanet.atmosphereRadIn = Planets.serpulo.atmosphereRadIn;
        fusionPlanet.atmosphereRadOut = Planets.serpulo.atmosphereRadOut;
        fusionPlanet.allowLaunchToNumbered = Planets.serpulo.allowLaunchToNumbered;
        fusionPlanet.startSector = 32;
        fusionPlanet.defaultCore = Blocks.coreShard;

        // 使用轻量无-op 网格以避免在内容加载阶段触发 HexMesh 相关 NPE
        fusionPlanet.meshLoader = () -> new SimplePlanetMesh();

        // 恢复低开销的单层星环（ParticleRingMesh，性能优先）
        fusionPlanet.cloudMeshLoader = () -> new ParticleRingMesh(fusionPlanet, 1.6f, 512, Color.valueOf("8a2be2"), false);

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