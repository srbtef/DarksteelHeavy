package example;

import mindustry.mod.Mod;
import example.MLPlanets;
import example.ExampleCoreBlock;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.content;

public class MyMod extends Mod{
 @Override
public void loadContent() {
    Log.info("Loading content.");

    // 创建自定义核心方块并设置属性
    ExampleCoreBlock core = new ExampleCoreBlock("example-core");
    core.localizedName = "示例核心";
    core.description = "可拆卸的自定义核心";
    core.health = 8000;
    core.size = 4;
    core.solid = true;
    core.update = true;
    core.hasPower = true;
    core.consumesPower = false;
    core.itemCapacity = 5000;
    core.liquidCapacity = 500f;
    core.coreMerge = true;
    core.category = Category.effect;
    core.buildVisibility = BuildVisibility.shown;
    core.requirements(Category.effect, ItemStack.with(Items.copper, 500, Items.lead, 300, Items.silicon, 200));

    // 注册到游戏内容
    core.load();   // 调用实例方法

    // 其他内容...
    // HelloBlock.load();  // 根据你的 HelloBlock 实现调整
    MLPlanets.load();
}
}
