package darksteel;

import mindustry.mod.Mod;
import mindustry.Vars;
import mindustry.mod.Mods;
import darksteel.content.Blocks;
//import darksteel.content.MLUnitTypes;

public classmain extends Mod {
    // 提前声明mod变量，解决找不到符号
    public static Mods.LoadedMod mod;

    @Override
    public void loadContent() {
        mod = Vars.mods.getMod(this.getClass());
        Blocks.load();
        //MLUnitTypes.load();
    }
}
