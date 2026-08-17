package example;

import arc.graphics.Color;
import arc.math.geom.Vec3;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.PlanetGrid.Ptile;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.TileGen;

public class MLPlanets {
    public static Planet cecilia;

    public static class CeciliaGenerator extends PlanetGenerator {
        @Override
        public void genTile(Vec3 position, TileGen tile) {
            float rawNoise = Simplex.noise2d(1, position.x, position.y, 6, 0.5f, 1f / 75f);
            float height = (rawNoise + 1f) / 2f;

            if (height > 0.60f) {
                tile.floor = Blocks.stone;
                tile.block = Blocks.air;
            } else if (height > 0.45f) {
                tile.floor = Blocks.sand;
                tile.block = Blocks.air;
            } else {
                tile.floor = Blocks.ice;
                tile.block = Blocks.air;
            }
        }
    }

    public static void load() {
        cecilia = new Planet("cecilia", Planets.sun, 1f, 3) {{
            generator = new CeciliaGenerator();

            meshLoader = () -> new MultiMesh(
                new HexMesh(this, 6),
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5,
                        Color.valueOf("97B5EDFF").a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5,
                        Color.valueOf("97B5EDFF").a(0.70f), 2, 0.42f, 1.2f, 0.45f)
            );

            localizedName = "塞西莉亚";
            orbitRadius = 40f;
            rotateTime = 3000f;
            sectorSeed = 1;
            startSector = 1;

            alwaysUnlocked = true;
            accessible = true;
            drawOrbit = true;
            hasAtmosphere = true;

            iconColor = Color.valueOf("97B5EDFF");
            atmosphereColor = Color.valueOf("97B5EDFF");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;

            //直接添加Ptile，去掉new Sector
            sectors.add(new Ptile(0, 0));
        }};
    }
}
