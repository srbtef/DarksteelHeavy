package darksteel;

import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class CustomGroundAI extends AIController{
    float stuckTime = 0f;
    float stuckX = -999f, stuckY = -999f;
    static final float stuckRange = tilesize * 1.5f;
    private static final int CHECK_RANGE = 3;
    private static final int[][] dirs = {{-1,1}, {0,1}, {1,1}, {-1,0}, {1,0}, {-1,-1}, {0,-1}, {1,-1}};

    @Override
    public void updateMovement(){
        float stuckThreshold = Math.max(1f, stuckRange * 2f / unit.type.speed);
        Building core = unit.closestEnemyCore();
        boolean moved = false;

        if(core != null && unit.within(core, unit.range() / 1.3f + core.block.size * tilesize / 2f)){
            target = core;
            for(var mount : unit.mounts){
                if(mount.weapon.controllable && mount.weapon.bullet.collidesGround){
                    mount.target = core;
                }
            }
        }

        boolean shouldMove = false;
        if(core == null || !unit.within(core, unit.type.range * 0.5f)){
            shouldMove = true;
            if(state.rules.waves && unit.team == state.rules.defaultTeam){
                Tile spawner = getClosestSpawner();
                if(spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f)) shouldMove = false;
                if(spawner == null && core == null) shouldMove = false;
            }
            if(core == null && (!state.rules.waves || getClosestSpawner() == null)) shouldMove = false;
        }

        moved = shouldMove;

        if(moved){
            int tileX = Mathf.floor(unit.x / tilesize);
            int tileY = Mathf.floor(unit.y / tilesize);
            boolean hasTopLeftObstacle = false;

            for(int dx = -CHECK_RANGE; dx <= CHECK_RANGE; dx++){
                for(int dy = -CHECK_RANGE; dy <= CHECK_RANGE; dy++){
                    Tile t = world.tile(tileX + dx, tileY + dy);
                    if(t != null && !unit.canPass(t)){
                        if(dx == -1 && dy == 1){
                            hasTopLeftObstacle = true;
                            break;
                        }
                    }
                }
                if(hasTopLeftObstacle) break;
            }

            float aimX = core.x;
            float aimY = core.y;
            if(hasTopLeftObstacle){
                aimX += tilesize * 1.5f;
                aimY += tilesize * 1.5f;
            }

            float bestX = aimX;
            float bestY = aimY;
            float minCost = Float.MAX_VALUE;
            for(int[] dir : dirs){
                int dx = dir[0];
                int dy = dir[1];
                float checkX = unit.x + dx * tilesize;
                float checkY = unit.y + dy * tilesize;
                Tile checkTile = world.tile(Mathf.floor(checkX / tilesize), Mathf.floor(checkY / tilesize));
                if(checkTile == null || unit.canPass(checkTile)){
                    float dist = Mathf.dst(checkX, checkY, aimX, aimY);
                    float cost = dist;
                    if(dx == -1 && dy == 1) cost *= 10f;
                    if(cost < minCost){
                        minCost = cost;
                        bestX = checkX;
                        bestY = checkY;
                    }
                }
            }

            moveTo(Tmp.v1.set(bestX, bestY), 1f, stuckTime >= stuckThreshold ? 0f : 30f);
        }

        if(unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()){
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.descentSpeed);
        }

        faceTarget();

        if(moved){
            if(unit.within(stuckX, stuckY, stuckRange)){
                stuckTime += Time.delta;
                if(stuckTime - Time.delta < stuckThreshold && stuckTime >= stuckThreshold){
                    float radius = unit.hitSize * Vars.unitCollisionRadiusScale * 2f;
                    Units.nearby(unit.team, unit.x, unit.y, radius, other -> {
                        if(other != unit && other.controller() instanceof CustomGroundAI ai && other.within(unit.x, unit.y, radius + other.hitSize * unitCollisionRadiusScale)){
                            ai.stuckX = other.x;
                            ai.stuckY = other.y;
                            ai.stuckTime = Math.max(1f, stuckRange * 2f / other.type.speed) + 1f;
                        }
                    });
                }
            }else{
                stuckX = unit.x;
                stuckY = unit.y;
                stuckTime = 0f;
            }
        }else{
            stuckTime = 0f;
        }
    }
}
