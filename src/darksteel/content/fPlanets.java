package darksteel.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env;

public class fPlanets {
    public static Planet darksteel;
    public static SectorPreset undevelopedZone;

    public static void load() {
        if (mindustry.Vars.content == null) return;

        darksteel = new Planet("fusion-planet", Planets.sun, 1f, 2);
        darksteel.localizedName = "Fusion World";
        darksteel.visible = true;
        darksteel.accessible = true;
        darksteel.alwaysUnlocked = true;
        darksteel.bloom = false;
        darksteel.defaultEnv = Env.terrestrial;
        darksteel.atmosphereColor = Color.valueOf("7a8cbf");
        darksteel.atmosphereRadIn = 0.02f;
        darksteel.atmosphereRadOut = 0.28f;
        darksteel.allowLaunchToNumbered = true;
        darksteel.startSector = 32;
        darksteel.defaultCore = Blocks.coreShard;

        darksteel.meshLoader = () -> new HexMesh(darksteel, 1);

        darksteel.cloudMeshLoader = () -> null;

        darksteel.ruleSetter = r -> {
            r.waveTeam = Team.crux;
            r.waves = true;
            r.env = Env.terrestrial;
            r.winWave = 10;
            r.placeRangeCheck = true;
        };

        undevelopedZone = new SectorPreset("undeveloped-zone", darksteel, 5);
        undevelopedZone.localizedName = "未开发区";
        undevelopedZone.description = "一片尚未被开发的区域";
        undevelopedZone.difficulty = 1;
        undevelopedZone.captureWave = 20;
        undevelopedZone.alwaysUnlocked = true;
    }
}