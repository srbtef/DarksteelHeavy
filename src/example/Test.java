package src.example;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock;

public class Test extends CoreBlock {

    public Test(String name){
        super(name);
        //方块尺寸
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        //开启允许拆卸
        canDeconstruct = true;
        //拆掉之后掉落这个方块物品
        deconstructDrop = true;
    }

    @Override
    public void setStats(){
        super.setStats();
    }

    //建筑实体
    public class TestBuild extends CoreBuild{

        //拆卸成功回调
        @Override
        public boolean deconstruct(){
            //执行原版拆卸逻辑
            boolean ok = super.deconstruct();
            if(ok){
                //在方块位置生成掉落物
                ItemDrop.drop(block, tile.worldx(), tile.worldy());
            }
            return ok;
        }
    }
}
