package mindustry.content;

import mindustry.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

coreShard = new CoreBlock("core-23"){{
            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1100;
            itemCapacity = 4000;
            size = 3;
            buildCostMultiplier = 2f;

            unitCapModifier = 8;
        }};
