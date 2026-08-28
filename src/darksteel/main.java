package darksteel;

import mindustry.mod.Mod;
import mindustry.Vars;
import mindustry.mod.Mods;
import darksteel.content.DPlanets;

public class main extends Mod {
    public static Mods.LoadedMod mod;

    @Override
    public void loadContent() {
        mod = Vars.mods.getMod(this.getClass());
    // load core blocks before planets so defaultCore references exist
    darksteel.content.DBlocks.load();
    DPlanets.load();
    }
}
