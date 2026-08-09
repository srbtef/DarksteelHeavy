package example;

import example.content.block.ACoreBlock;
import mindustry.mod.Mod;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockGroup;

public class Test extends Mod{
    public static CoreBlock acore;
@Override
    public void loadContent(){
        acore = new ACoreBlock("acore");
    } 
}
