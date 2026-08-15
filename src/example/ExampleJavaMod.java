package src.example;

import mindustry.Vars;
import mindustry.mod.Mod;

public class ExampleJavaMod extends Mod {
    @Override
    public void loadContent(){
        Vars.content.add(new Test("democore"));
    }
}
