package src.example;

import mindustry.Vars;
import mindustry.mod.Mod;

public class ExampleJavaMod extends Mod {
    @Override
    public void loadContent(){
        Test democore = new Test("democore");
        Vars.content.register(democore);
    }
}
