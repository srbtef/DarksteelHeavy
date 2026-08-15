package src.example;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock;

public class Test extends CoreBlock {

    public Test(String name){
        super(name);
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        //允许拆卸（方块全局开关）
        destructible = true;
    }

    @Override
    public void setStats(){
        super.setStats();
    }

    public class TestBuild extends CoreBuild{
        //拦截拆卸指令
        @Override
        public boolean onDeconstructed(){
            super.onDeconstructed();
            //掉落自身方块物品
            FloatingItem.create(block.item, tile.worldx(), tile.worldy()).add();
            return true;
        }
    }
}
