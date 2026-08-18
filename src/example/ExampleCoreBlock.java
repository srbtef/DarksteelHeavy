package example;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.content;

public class ExampleCoreBlock extends CoreBlock {

    public ExampleCoreBlock(String name) {
        super(name);
        hasIcons = false;   // 禁用图标生成，避免加载贴图
    }

    @Override
    public boolean canBreak(Tile tile) {
        return true;
    }

    public void load() {
        content.blocks().add(this);
    }
}