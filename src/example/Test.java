package src.example;

import mindustry.world.blocks.storage.CoreBlock;

public class Test extends CoreBlock {

    public Test(String name){
        super(name);
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        // 开启可拆卸权限
        destructible = true;
    }
}
