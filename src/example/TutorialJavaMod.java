package src.example;

import mindustry.mod.Mod;
import mindustry.type.Item;

public class TutorialJavaMod extends Mod{
  @Override
  public void loadContent(){
      //创建物品
      Item testItem = new Item("test‑item");
      testItem.color.set(0x70b8ff);
      testItem.hardness = 2;
      testItem.cost = 1.2f;
      //注册物品
      Vars.content.items().add(testItem);
  }
}
