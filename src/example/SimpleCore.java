package example;

import arc.struct.ObjectSet;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;

public class SimpleCore extends CoreBlock {

    public SimpleCore() {
        super("simple-core");

        localizedName = "简易核心";
        size = 2;
        health = 1000;
        solid = true;
        flags = ObjectSet.with(BlockFlag.core);
        category = Category.effect;
        requirements(Category.effect, ItemStack.with(
            Items.copper, 100,
            Items.lead, 50
        ));
        itemCapacity = 1000;
        buildType = SimpleCoreBuild::new;
    }

    public class SimpleCoreBuild extends CoreBuild {
    }
}
