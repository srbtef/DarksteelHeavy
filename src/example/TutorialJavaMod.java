package src.example;

import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.type.Item;

public class TutorialJavaMod extends Mod{
    @Override
    public void loadContent(){
        Item testItem = new Item("test-item");
        testItem.color.set(0x6299cc);
        testItem.hardness = 3;
        testItem.cost = 1.4f;
        testItem.flammable = false;

        Vars.content.items().add(testItem);
    }
}
