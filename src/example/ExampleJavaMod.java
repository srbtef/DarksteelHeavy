package src.example;
import mindustry.mod.*;
import mindustry.ctype.Content;

public class ExampleJavaMod extends Mod{
    @Override
    public void loadContent(){
        Test core = new Test("democore");
        content.register(core);
    }
}
