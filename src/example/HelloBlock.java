package example;

import arc.scene.ui.Dialog;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

public class HelloBlock extends Block {

    public HelloBlock() {
        super("hello-block");
        localizedName = "问候方块";
        description = "点击我，我会向你问好。";
        health = 100;
        size = 1;
        solid = true;
        update = true;
        hasPower = false;
        category = Category.units;
        requirements(Category.units, ItemStack.with(Items.copper, 10));
        buildType = HelloBuild::new;
    }

    public class HelloBuild extends Building {
        @Override
        public void tapped() {
            Dialog dialog = new Dialog("你好");
            dialog.cont.add("你好，世界！").pad(20f);
            dialog.addCloseButton();
            dialog.show();
        }
    }
}
