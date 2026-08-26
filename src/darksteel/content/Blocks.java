package magical.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.world.Block;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.gen.Building;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import mindustry.world.draw.DrawTurret;
import mindustry.entities.part.RegionPart;
import mindustry.entities.part.HaloPart;
import mindustry.entities.Effect;
import arc.struct.Seq;

public class Blocks {

    public static Block
            //基础科技
            baseCor;

    public static void load() {

        //我超，盒
        //基座核心
        baseCore = new baseCore("baseCore") {{
            requirements(Category.effect, ItemStack.with(MLItems.phantomTitaniumSteel, 200, MLItems.mysticCrystal, 200, MLItems.nanoCarbonAlloy, 100));

            unitType = UnitTypes.alpha;
            health = 500;
            itemCapacity = 2000;
            size = 2;
            alwaysUnlocked = true;

            unitCapModifier = 5;

        }};
    }
}
