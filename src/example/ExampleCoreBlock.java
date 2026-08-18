package example;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Tile;                  // ✅ 正确的 Tile 导入
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.content;

public class ExampleCoreBlock extends CoreBlock {

    public ExampleCoreBlock(String name) {
        super(name);
    }

    // 允许拆除核心
    @Override
    public boolean canBreak(Tile tile) {
        return true;
    }

    // 实例方法用于注册（不能是 static）
    public void load() {
        content.blocks().add(this);
    }
}