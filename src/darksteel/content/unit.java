package magical.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.weapons.Weapon;

import magical.content.MLSounds;

public class MLUnitTypes {
    public static UnitType Popular;

    public static void load() {
        Popular = new UnitType("Popular") {{
            constructor = UnitTypes.flare.constructor;
            flying = true;
            lowAltitude = true;
            rotateSpeed = 8f;
            speed = 4.2f;
            drag = 0.04f;
            accel = 0.08f;
            hitSize = 28;
            health = 220;
            armor = 2;
            itemCapacity = 60;
            engineOffset = 16;
            engineSize = 4f;
            mineSpeed = 7.5f;
            mineTier = 2;
            buildSpeed = 0.9f;

            weapons.add(new Weapon("magic-Popular0") {{
                shootY = 0f;
                rotate = false;
                mirror = false;
                reload = 30;
                x = 0;
                y = 0;
                shootSound = MLSounds.laser;
                ejectEffect = Fx.none;
                layerOffset = 0.001f;
                bullet = new LaserBulletType(25f) {{
                    healPercent = 2.5f;
                    width = 16;
                    length = width * 8;
                    colors = new Color[]{Color.valueOf("FEEBB3FF"), Color.valueOf("FEEBB3FF"), Color.valueOf("FEEBB3FF")};
                    smokeEffect = Fx.none;
                }};
            }});
        }};
    }
}
