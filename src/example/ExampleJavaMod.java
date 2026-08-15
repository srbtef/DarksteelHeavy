package src.example;

import mindustry.Vars;
import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class ExampleJavaMod extends Mod {
    @Override
    public void loadContent(){
        Test democore = new Test("democore");
        Vars.content.blocks().add(democore);
    }
}

class Test extends CoreBlock {
    public Test(String name){
        super(name);
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        destructible = true;
    }

    @Override
    public boolean canBreak(Tile tile){
        return true;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        return tile != null;
    }
}
