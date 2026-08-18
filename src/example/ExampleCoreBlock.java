package example;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

public class ExampleCoreBlock extends CoreBlock {

    public ExampleCoreBlock() {
        super("example-core");

        // 基本信息
        localizedName = "示例核心";
        description = "一个自定义的核心方块，拥有更大的存储容量。";
        health = 8000;                // 生命值
        size = 4;                     // 尺寸（4x4）
        solid = true;
        update = true;
        hasPower = true;
        consumesPower = false;

        // 核心特有属性
        itemCapacity = 5000;          // 物品总容量
        liquidCapacity = 500f;        // 液体容量
        coreMerge = true;             // 允许与相邻核心合并（可选）

        // 分类和建造条件
        category = Category.effect;   // 通常核心不直接建造，但分类可以设为 effect
        buildVisibility = BuildVisibility.shown;  // 显示在建造菜单中（如果希望玩家可建造）

        // 建造需求（仅当玩家可建造时有效）
        requirements(Category.effect, ItemStack.with(Items.copper, 500, Items.lead, 300, Items.silicon, 200));
    }
}