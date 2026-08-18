package example;

import mindustry.content.Items;
import mindustry.gen.Tile;                     // 导入 Tile 类型
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.content;           // 静态导入 content

public class ExampleCoreBlock extends CoreBlock {

    public ExampleCoreBlock(String name) {
        super(name);
    }

    // 允许玩家拆除核心
    @Override
    public boolean canBreak(Tile tile) {
        return true;
    }

    // 静态加载方法，用于注册方块
    public static void load() {
        ExampleCoreBlock block = new ExampleCoreBlock("example-core") {{
            localizedName = "示例核心";
            description = "可拆卸的自定义核心";
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

        content.blocks().add(block);
    }
}