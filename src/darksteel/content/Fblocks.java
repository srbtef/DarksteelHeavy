package darksteel.content;

import mindustry.world.blocks.storage.CoreBlock;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.ItemStack;
import mindustry.type.Category;
import mindustry.world.meta.BuildVisibility;

public class DarkBlocks {
    
    public static CoreBlock coreShard;
    
    public static void load() {
        coreShard = new CoreBlock("core-23") {{
            // 使用构造器参数（在花括号外）
            requirements = ItemStack.with(Items.copper, 1000, Items.lead, 800);
            
            // 这些在花括号内设置
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1100;
            itemCapacity = 4000;
            size = 3;
            buildCostMultiplier = 2f;
            unitCapModifier = 8;
            buildVisibility = BuildVisibility.shown;
        }};
    }
}