package example;

import mindustry.mod.Mod;
import example.MLPlanets;
import example.ExampleCoreBlock;

public class MyMod extends Mod{
    @Override
    public void loadContent() {
        MLPlanets.load();
        ExampleCoreBlock.load();
    }
}
