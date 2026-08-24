package darksteel;

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
            requirements(Category.effect, BuildVisibility.skill);
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