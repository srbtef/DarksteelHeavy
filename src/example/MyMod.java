package example;

import mindustry.mod.Mod;
import example.MLPlanets;

public class MyMod extends Mod{
    @Override
    public void loadContent() {
        MLPlanets.load();
    }
}
