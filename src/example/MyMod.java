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

        // 在内容注册完成后执行
        Events.on(ClientLoadEvent.class, e -> {
            // 可以在这里做一些客户端初始化
        });
    }

    @Override
    public void loadContent() {
        Log.info("Loading content.");
        // 注册方块（HelloBlock 会在静态代码块或显式调用中注册）
        new HelloBlock().load();   // 假设 load() 方法负责注册
    }
}