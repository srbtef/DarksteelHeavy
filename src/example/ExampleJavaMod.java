package src.example;

import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class ExampleJavaMod extends Mod {
    @Override
    public void loadContent(){
        new Test("democore").register();
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
        // 移除所有地形、核心重叠限制，任意位置均可放置
        return tile != null;
    }
}
