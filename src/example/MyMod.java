package example.mymod;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class MyMod extends Mod {

    public MyMod() {
        Log.info("Loaded MyJavaMod constructor.");
    }

    @Override
    public void loadContent() {
        Log.info("Loading content.");
        // 注册方块（mindustry 会自动注册）
        new HelloBlock();
        new SimpleCore();
    }
}
