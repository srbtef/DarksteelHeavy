const 墙 = extend(Wall, "墙", {
    update: true,
    buildType: () => extend(Wall.WallBuild, {
        updateTile(){
            // 每 10 tick 恢复 5 生命值（不超过最大生命值）
if(this.timer.get(0, 600)){ // 10秒 = 600 tick
    this.heal(5);
}
        }
    })
});