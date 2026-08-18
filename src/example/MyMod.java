package example;

import mindustry.mod.Mod;
import example.fPlanets;
import example.SimpleCore;

public class MyMod extends Mod{
    @Override
    public void loadContent() {
        fPlanets.load();
    }
}
