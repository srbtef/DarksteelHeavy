package darksteel;

import mindustry.mod.Mod;
import darksteel.content.FBlocks;
import darksteel.content.fPlanets;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.type.Category;


import static mindustry.Vars.content;

public class main extends Mod{
 @Override
public void loadContent() {
    FBlocks.load();
    fPlanets.load();
}
}
