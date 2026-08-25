package darksteel;

import mindustry.game.Item;
import mindustry.graphics.Color;

public class XuanSteelItem extends Item {
    public XuanSteelItem(String name) {
        super(name);
        this.color = Color.valueOf("#708090");
        this.hardness = 3;
        this.stackSize = 50;
    }
}
