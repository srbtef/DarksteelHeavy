package FusionPlanet;

import arc.graphics.Color;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import FusionPlanet.FusionPlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.meta.Env;
import mindustry.content.Blocks;
import mindustry.game.Team;

public class fPlanets {
    public static Planet fusionPlanet;

    public static void load() {
        fusionPlanet = new Planet("fusion-planet", Planets.sun, 1f, 2);
        fusionPlanet.generator = new FusionPlanetGenerator();
        fusionPlanet.localizedName = "Fusion World";
        fusionPlanet.visible = true;
        fusionPlanet.accessible = true;
        fusionPlanet.alwaysUnlocked = true;
        fusionPlanet.bloom = false;

        // ✔厄雷基尔高温默认环境
        fusionPlanet.defaultEnv = Env.erekir;
        fusionPlanet.atmosphereColor = Color.valueOf("#c46038"); // 橙红色大气，适配高温岩石星球
        fusionPlanet.atmosphereRadIn = 0.02f;
        fusionPlanet.atmosphereRadOut = 0.28f;

        fusionPlanet.allowLaunchToNumbered = true;
        fusionPlanet.startSector = 32;
        // ✔厄雷基尔堡垒核心开局
        fusionPlanet.defaultCore = Blocks.coreBastion;

        // 厄雷基尔风格红橙色云层
        Color cloud1 = Color.valueOf("#e68c5c");
        cloud1.a = 0.4f;
        Color cloud2 = Color.valueOf("#b86242");
        cloud2.a = 0.3f;

        fusionPlanet.cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(fusionPlanet, 6, 0.15f, 0.12f, 5, cloud1, 2, 0.4f, 0.9f, 0.38f),
                new HexSkyMesh(fusionPlanet, 4, 0.3f, 0.10f, 5, cloud2, 1, 0.3f, 1.0f, 0.4f)
        );

        fusionPlanet.meshLoader = () -> new HexMesh(fusionPlanet, 5);

        fusionPlanet.ruleSetter = r -> {
            r.waveTeam = Team.malis;      // ✔厄雷基尔malis紫色敌人
            r.waves = true;
            r.env = Env.erekir;           // ✔高温环境
            r.winWave = 10;
            r.placeRangeCheck = true;
        };
    }
}
