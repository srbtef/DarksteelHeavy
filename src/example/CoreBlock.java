package example;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.CoreBlock;
import mindustry.world.meta.BlockFlag;

public class CoreBlock extends CoreBlock {

    public CoreBlock() {
        super("simple-core");

        // 地图显示名称
        localizedName = "简易核心";

        // 核心大小（1/2/3）
        size = 2;

        // 血量
        health = 1000;

        // 是否 solid（可阻挡）
        solid = true;

        // 核心认证（玩家出生点）
        flags.add(BlockFlag.core);

        // 所属分类
        category = Category.effect;

        // 建造需求
        requirements(Category.effect, ItemStack.with(
            Items.copper, 100,
            Items.lead, 50
        ));

        // itemCapacity：核心能存多少资源
        itemCapacity = 1000;

        buildType = CoreBuild::new;
    }

    public class CoreBuild extends CoreBuild {
        // 这里可以写核心被点击/激活时的逻辑
        // 例如：显示一个提示框
    }
}
