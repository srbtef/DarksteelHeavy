package example;

import mindustry.mod.Mod;
import example.MLPlanets;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.type.Category;


import static mindustry.Vars.content;

public class MyMod extends Mod{
 @Override
public void loadContent() {
    Log.info("Loading content.");
    MLPlanets.load();
}
}
