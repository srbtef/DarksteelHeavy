package darksteel;

import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;
import mindustry.game.Category;
import mindustry.type.UnitType;
import mindustry.content.Items;
import mindustry.content.UnitTypes;

public class core {

    public static core coreShard;

    public static void load() {
        coreShard = new CoreBlock("core-23"){{
            requirements(Category.effect, BuildVisibility.coreZoneOnly, ItemStack.with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1100;
            itemCapacity = 4000;
            size = 3;
            buildCostMultiplier = 2f;
            unitCapModifier = 8;
        }};
    }
}
