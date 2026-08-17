package example.mymod;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import example.HelloBlock;
import example.SimpleCore;

public class MyMod extends Mod {

    public MyMod() {
        Log.info("Loaded MyJavaMod constructor.");
    }

    @Override
    public void loadContent() {
        Log.info("Loading content.");
        new HelloBlock();
        new SimpleCore();
    }
}
