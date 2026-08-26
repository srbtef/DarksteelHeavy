package darksteel;

import mindustry.mod.Mod;
import darksteel.content.Fblocks;
import darksteel.content.fPlanets;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.type.Category;


import static mindustry.Vars.content;

public class main extends Mod{
 @Override
public void loadContent() {
    // 延迟加载星球，确保 Vars.content 已初始化
    arc.Events.on(mindustry.game.EventType.ContentInitEvent.class, e -> {
        fPlanets.load();
    });
    Fblocks.load();
    Log.info("Darksteel mod loaded successfully!");
}
}
