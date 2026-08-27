package darksteel;

import mindustry.mod.Mod;
import mindustry.Vars;
import mindustry.mod.Mods;
import darksteel.content.fPlanets;

public class main extends Mod {
    public static Mods.LoadedMod mod;

    @Override
    public void loadContent() {
        mod = Vars.mods.getMod(this.getClass());
       fPlanets.load();
        //MLUnitTypes.load();
    }
}
