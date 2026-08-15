package src.example;
import mindustry.mod.*;

public class ExampleJavaMod extends Mod{
    @Override
    public void loadContent(){
        new Test("democore").register();
    }
}
