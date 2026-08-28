package darksteel.content;

import arc.graphics.Color;
import darksteel.ring.DysonRingMesh;
import darksteel.ring.ParticleRingMesh;
import mindustry.content.Planets;
import mindustry.Vars;
import mindustry.type.TechTree;
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
        DPlanet = new Planet("Dplanet", Planets.sun, 1f, 2);
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
        DPlanet.defaultCore = Blocks.coreShard;

        DPlanet.meshLoader = () -> new HexMesh(DPlanet, 5);
        Color ringColor1 = Color.valueOf("#4600b6");
        Color ringColor2 = Color.valueOf("#ff5100");
        // 恢复低开销的单层星环（ParticleRingMesh，性能优先）
       DPlanet.cloudMeshLoader = () -> new MultiMesh(
                new DysonRingMesh(DPlanet, 1.45f, 0.12f, 512, ringColor1, ringColor2)
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