package darksteel;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import arc.audio.Sound;

import darksteel.content.Blocks;

public class main extends Mod {
    @Override
    public void loadContent() {
        mod = Vars.mods.getMod(this.getClass());
      //  MLLiquids.load();
      //  MLItems.load();
         Blocks.load();
     }
}