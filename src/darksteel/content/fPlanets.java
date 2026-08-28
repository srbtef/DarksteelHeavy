package darksteel.content;


import arc.graphics.Color;
import darksteel.ring.DysonRingMesh;
import darksteel.ring.ParticleRingMesh;
import mindustry.content.Planets;
import mindustry.Vars;
import mindustry.type.ContentType;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;
import mindustry.world.meta.Env;

public class fPlanets {
    public static Planet fusionPlanet;
    public static SectorPreset undevelopedZone;
    

    public static void load() {
        fusionPlanet = new Planet("fusion-planet", Planets.sun, 1f, 2);
        fusionPlanet.generator = new FusionPlanetGenerator();
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
        fusionPlanet.startSector = 0;
        // prefer JSON-defined core block if present, otherwise use Java-defined DBlocks.coreo
        CoreBlock jsonCore = (CoreBlock) Vars.content.getByName(ContentType.block, "玄钢核心");
        fusionPlanet.defaultCore = jsonCore != null ? jsonCore : DBlocks.coreo;

        fusionPlanet.meshLoader = () -> new HexMesh(fusionPlanet, 5);
        Color ringColor1 = Color.valueOf("#4600b6");
        Color ringColor2 = Color.valueOf("#ff5100");
        // 恢复低开销的单层星环（ParticleRingMesh，性能优先）
       fusionPlanet.cloudMeshLoader = () -> new MultiMesh(
                new DysonRingMesh(fusionPlanet, 1.45f, 0.12f, 512, ringColor1, ringColor2)
        );

        fusionPlanet.ruleSetter = r -> {
            r.waveTeam = Team.crux;
            r.waves = true;
            r.env = Env.terrestrial;
            r.winWave = 10;
            r.placeRangeCheck = true;
        };

        SectorPreset gyqd = new SectorPreset("gyqd", fusionPlanet, 0);
        gyqd.localizedName = "工业起点";
        gyqd.description = "一片尚未被开发的区域";
        gyqd.difficulty = 0;
        gyqd.captureWave = 20;
        gyqd.alwaysUnlocked = true;

        SectorPreset gyyj = new SectorPreset("gyyj", fusionPlanet, 1);
        gyyj.localizedName = "工业遗迹";
        gyyj.description = "一片尚未被开发的区域";
        gyyj.difficulty = 0;
        gyyj.captureWave = 20;
        gyyj.alwaysUnlocked = false;
    }
}