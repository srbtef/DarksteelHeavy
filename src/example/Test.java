package src.example;

import mindustry.Vars;
import mindustry.gen.FloatingItem;
import mindustry.gen.Unit;
import mindustry.world.blocks.storage.CoreBlock;

public class Test extends CoreBlock {

    public Test(String name){
        super(name);
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        // 开启可拆卸
        destructible = true;
        // 拆卸掉落自身对应物品
        itemDrop = Vars.content.item(name);
    }

    @Override
    public void setStats(){
        super.setStats();
    }

    public class TestBuild extends CoreBuild{

        // 拆卸完成回调，必须带Unit参数匹配父类签名
        @Override
        public void onDeconstructed(Unit deconstructor){
            super.onDeconstructed(deconstructor);
            // 生成掉落物
            if(block.itemDrop != null){
                FloatingItem.create(block.itemDrop, tile.worldx(), tile.worldy()).add();
            }
        }
    }
}
