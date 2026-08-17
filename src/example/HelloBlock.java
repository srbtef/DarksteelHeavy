package com.example.mymod;

import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class HelloBlock extends Block {

    // 自定义的 Building 类
    public class HelloBuild extends Building {
        @Override
        public void tapped() {
            // 玩家点击方块时触发
            if (this.tap != null) {
                showHelloDialog();
            }
        }
    }

    public HelloBlock() {
        super("hello-block");

        localizedName = "问候方块";
        description = "点击我，我会向你问好。";
        health = 100;
        size = 1;
        solid = true;
        update = true;
        hasPower = false;
        category = Category.units;  // 可改为其他分类
        buildType = HelloBuild::new;  // 指定自定义 Building
        requirements(Category.units, ItemStack.with(Items.copper, 10));
    }

    public void load() {
        // 注册方块到游戏中
        content.blocks().add(this);
    }

    private void showHelloDialog() {
        // 创建一个简单对话框
        Dialog dialog = new Dialog("你好");
        dialog.cont.add("你好，世界！").pad(20f);
        dialog.addCloseButton();
        dialog.show();
    }
}