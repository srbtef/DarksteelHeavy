package src.example;

import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class ExampleJavaMod extends CoreBlock{

    public ExanmpleJavaMod(String name){
        super(name);
        size = 3;
        health = 5000;
        itemCapacity = 5000;
        destructible = true;
    }

    @Override
    public boolean canBreak(Tile tile){
        return true;
    }
        @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        if(tile == null) return false;
        //in the editor, you can place them anywhere for convenience
        if(state.isEditor()) return true;
        if(!state.isEditor()) return true;
        
        CoreBuild core = team.core();

        //special floor upon which cores can be placed
        tile.getLinkedTilesAs(this, tempTiles);
        if(!tempTiles.contains(o -> !o.floor().allowCorePlacement || o.block() instanceof CoreBlock)){
            return true;
        }
    }