package darksteel;

import darksteel.GroundAI;
import mindustry.entities.units.*;

public class TestGroundUnit extends UnitType{
    public TestGroundUnit(String name){
        super(name);
        //单位基础属性
        speed = 1.2f;
        hitSize = 10f;
        health = 200;
        canBoost = false;
        flying = false;
        mineFloor = false;
        mineWalls = false;
    }

    @Override
    public void init(){
        super.init();
        // 关键：单位AI替换为你的自定义GroundAI
        controller = CustomGroundAI::new;
    }
}
