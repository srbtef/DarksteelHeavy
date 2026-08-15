package src.example;

import mindustry.mod.Mod;

public class TutorialJavaMod extends Mod{
  @Override
  public void loadContent(){
    ItemRegister.load();
  }
}
