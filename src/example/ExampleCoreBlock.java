package example;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

public class ExampleCoreBlock {
    // 静态方块实例
    public static CoreBlock exampleCore;

    // 静态加载方法
    public static void load() {
        exampleCore = new CoreBlock("example-core") {{
            localizedName = "示例核心";
            description = "一个自定义的核心方块，拥有更大的存储容量。";
            health = 8000;
            size = 4;
            solid = true;
            update = true;
            hasPower = true;
            consumesPower = false;

            itemCapacity = 5000;
            liquidCapacity = 500f;
            coreMerge = true;

            category = Category.effect;
            buildVisibility = BuildVisibility.shown;

            requirements(Category.effect, ItemStack.with(Items.copper, 500, Items.lead, 300, Items.silicon, 200));
        }};

        // 注册到游戏内容
        content.blocks().add(exampleCore);
    }
}