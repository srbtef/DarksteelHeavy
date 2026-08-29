package darksteel.content;

import arc.graphics.Color;
import darksteel.ring.DysonRingMesh;
import darksteel.ring.ParticleRingMesh;
import mindustry.content.Planets;
import mindustry.Vars;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env; 
import static mindustry.content.Blocks.*;

public class DPlanets {
    public static Planet DPlanet;
    public static SectorPreset undevelopedZone;
    

    public static void load() {
        DPlanet = new Planet("Dplanet", null, 1f, 2);
        DPlanet.generator = new DPlanetGenerator();
        DPlanet.localizedName = "斯萨卡";
        DPlanet.visible = true;
        DPlanet.accessible = true;
        DPlanet.alwaysUnlocked = true;
        DPlanet.bloom = false;
        DPlanet.defaultEnv = Env.terrestrial;
        DPlanet.atmosphereColor = Color.valueOf("7a8cbf");
        DPlanet.atmosphereRadIn = 0.02f;
        DPlanet.atmosphereRadOut = 0.28f;
        DPlanet.allowLaunchToNumbered = true;
        DPlanet.startSector = 0;
        // force use of Java-defined core block
        DPlanet.defaultCore = coreShard;

DPlanet.meshLoader = () -> new HexMesh(DPlanet, 5);
DPlanet.meshLoader = () -> new HexMesh(DPlanet, 5);
DPlanet.cloudMeshLoader = () -> new MultiMesh(
        new ParticleRingMesh(DPlanet, 2.05f, 0.12f, 512, Color.valueOf("#97B5ED"), false),
        new ParticleRingMesh(DPlanet, 2.06f, 0.12f, 512, Color.valueOf("#9bb9ee"), false),
        new ParticleRingMesh(DPlanet, 2.07f, 0.12f, 512, Color.valueOf("#a0bde8"), false),
        new ParticleRingMesh(DPlanet, 2.08f, 0.12f, 512, Color.valueOf("#a7c4ec"), false),
        new ParticleRingMesh(DPlanet, 2.09f, 0.12f, 512, Color.valueOf("#adc9ef"), false),
        new ParticleRingMesh(DPlanet, 2.13f, 0.12f, 512, Color.valueOf("#b3cde3"), false),
        new ParticleRingMesh(DPlanet, 2.14f, 0.12f, 512, Color.valueOf("#b8c8e2"), false),
        new ParticleRingMesh(DPlanet, 2.12f, 0.12f, 512, Color.valueOf("#b8c8e2"), false),
        new ParticleRingMesh(DPlanet, 2.15f, 0.12f, 512, Color.valueOf("#bed2e7"), false),
        new ParticleRingMesh(DPlanet, 2.16f, 0.12f, 512, Color.valueOf("#c4d7eb"), false),
        new ParticleRingMesh(DPlanet, 2.18f, 0.12f, 512, Color.valueOf("#cadcef"), false),
        new ParticleRingMesh(DPlanet, 2.21f, 0.12f, 512, Color.valueOf("#d0e1f2"), false),
        new ParticleRingMesh(DPlanet, 2.23f, 0.12f, 512, Color.valueOf("#d6e6f5"), false),
        new ParticleRingMesh(DPlanet, 2.26f, 0.12f, 512, Color.valueOf("#dceaf7"), false),
        new ParticleRingMesh(DPlanet, 2.29f, 0.12f, 512, Color.valueOf("#e2effa"), false),
        new ParticleRingMesh(DPlanet, 2.30f, 0.12f, 512, Color.valueOf("#e6f4f8"), false)
);



        DPlanet.ruleSetter = r -> {
            r.waveTeam = Team.crux;
            r.waves = true;
            r.env = Env.terrestrial;
            r.winWave = 10;
            r.placeRangeCheck = true;
        };

       /*  SectorPreset gyqd = new SectorPreset("gyqd", DPlanet, 0);
        gyqd.localizedName = "工业起点";
        gyqd.description = "一片尚未被开发的区域";
        gyqd.difficulty = 0;
        gyqd.captureWave = 20;
        gyqd.alwaysUnlocked = true;

        SectorPreset gyyj = new SectorPreset("gyyj", DPlanet, 1);
        gyyj.localizedName = "工业遗迹";
        gyyj.description = "一片尚未被开发的区域";
        gyyj.difficulty = 0;
        gyyj.captureWave = 20;
        gyyj.alwaysUnlocked = false;*/
    }
}