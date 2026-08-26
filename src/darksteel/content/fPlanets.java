package darksteel.content;

import darksteel.ring.DysonRingMesh;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.*;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env;

import static darksteel.ring.RingWorldPlanet.*;
import static darksteel.ring.RingWorldMesh.*;
import static darksteel.ring.RingWorldGrid.*;
import static darksteel.ring.DysonRingMesh.*;

public class fPlanets {
    public static Planet darksteel;
    public static SectorPreset undevelopedZone;
    private static final float ringGlowOffset = 0.008f;

    public static void load() {
        darksteel = new Planet("fusion-planet", Planets.sun, 1f, 2);
        darksteel.generator = new darksteelGenerator();
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

        darksteel.meshLoader = () -> new HexMesh(darksteel, 5);

        Color ringColor1 = Color.valueOf("88aacc");
        Color ringColor2 = Color.valueOf("667799");
        Color glowColor = Color.valueOf("aaccee");

        darksteel.cloudMeshLoader = () -> new MultiMesh(
                new DysonRingMesh(darksteel, 1.45f, 0.12f, 512, ringColor1, ringColor2),
                new DysonRingMesh(darksteel, 1.65f, 0.10f, 768, ringColor1, ringColor2),
                new DysonRingMesh(darksteel, 1.85f, 0.08f, 1024, ringColor1, ringColor2),
                new DysonRingMesh(darksteel, 1.45f + ringGlowOffset, 0.06f, 512, glowColor, glowColor, true),
                new DysonRingMesh(darksteel, 1.65f + ringGlowOffset, 0.05f, 768, glowColor, glowColor, true),
                new DysonRingMesh(darksteel, 1.85f + ringGlowOffset, 0.04f, 1024, glowColor, glowColor, true)
        );

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