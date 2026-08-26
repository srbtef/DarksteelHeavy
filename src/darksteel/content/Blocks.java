package darksteel.content;

import mindustry.world.blocks.storage.CoreBlock;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.Block;

public class Blocks {
    // 修正变量名统一
    public static Blocks baseCor;

    public static void load() {
        baseCor = new CoreBlock("core-shard"){{
            // 补全ItemStack.with，修复requirements参数
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
