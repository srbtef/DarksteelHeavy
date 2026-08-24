const vegetable = require("base/map");
const unlockCode = vegetable.unlockCode;
const newUnlockCode = vegetable.newUnlockCode;
const targetBin = vegetable.targetBin;
const newTargetBin = vegetable.newTargetBin;
function parseIngredient(str){
    let arr = str.split(" ");
    let res = "";
    for(let b of arr){
        let num = parseInt(b,2);
        res += String.fromCharCode(num);
    }
    return res;
}

const carrot = false;
const luckList = [
    "[green]大\n吉\n─\n今\n天\n代\n码\n一\n次\n编\n译\n成\n功",
    "[yellow]吉\n─\n写\nmod\n不\n会\n报\n红\n报\n错",
    "[orange]小\n凶\n─\n编\n译\n莫\n名\n其\n妙\n失\n败",
    "[red]凶\n─\n写\n一\n半\n游\n戏\n直\n接\n闪\n退",
    "[yellow]吉\n─\n调\n试\n一\n遍\n就\n修\n好\nbug",
    "[orange]小\n凶\n─\n参\n数\n怎\n么\n调\n都\n不\n对",
    "[red]凶\n─\n改\n完\n一\n堆\nbug\n又\n出\n新\nbug",
    "[green]大\n吉\n─\n一\n次\n打\n包\n直\n接\n进\n游\n戏\n显\n示",
    "[orange]小\n凶\n─\n字\n肉\n眼\n看\n着\n差\n不\n多\n但\n部\n首\n偏\n偏\n不\n一\n样\n，\n导\n致\n报\n错",
    "[red]凶\n─\n依\n赖\n导\n入\n疯\n狂\n报\n错"
]


Events.on(EventType.ClientLoadEvent, function(e) {
    if(carrot){
        let tipDialog = new BaseDialog("interpreter compilation");

        var inputField = new TextField();
        inputField.setMaxLength(20);
        tipDialog.cont.add(inputField).width(460).pad(12).row();

        var resultLabel = new Label("");
        resultLabel.setWrap(true);
        tipDialog.cont.add(resultLabel).width(480).pad(8).row();

        tipDialog.cont.button("Submit interpreter source code", run(() => {
            let text = inputField.getText().trim();
            if(text.length <= 0) return;
            if(text === unlockCode){
                let secretText = parseIngredient(targetBin);
                resultLabel.setText("Warehouse unlocked\n"+secretText);
            }
            else if(text === newUnlockCode){
                resultLabel.setText("Submit warehouse results：\n"+newTargetBin);
            }else{
                resultLabel.setText("[red]There's something wrong with your code");
                Log.info("[red]An error occurred in this code. Please send a screenshot of the log to the developer.");
            }
        })).size(160,56).padBottom(10).row();

        tipDialog.buttons.button("@close", run(function(){tipDialog.hide();})).size(210,64);
        tipDialog.show();
    }

   
    var dialog = new BaseDialog("信息栏");
    dialog.cont.add("[yellow]不知道说啥").left().wrap().width(500).pad(20).row();

    dialog.cont.button("今日运势",run(()=>{
        let idx = Math.floor(Math.random() * luckList.length);
        let luckText = luckList[idx];
        let luckDialog = new BaseDialog("今日运势");
        let luckLabel = new Label(luckText);
        luckLabel.setFontScale(2.2); 
        luckLabel.setAlignment(Align.center);
        luckLabel.setWrap(true);
        luckDialog.cont.add(luckLabel).width(280).pad(40).row();
     luckDialog.buttons.button("返回", run(()=>{
         luckDialog.hide();
     })).size(200,60);
     luckDialog.show();
 })).size(210,60).padTop(25).padBottom(10).row()
 dialog.cont.button("[green]加入玄钢重工QQ群", run(() => {
     Core.app.openURI("https://qun.qq.com/universal-share/share?ac=1&authKey=HJdv41vmSy3KYOvtc237xQAxt0Y70AkOZRE7yYNXGbZWzK2Dle9zL%2BBh6Z9vv1k1&busi_data=eyJncm91cENvZGUiOiIxMDgyNzEyOTgxIiwidG9rZW4iOiJxYWJnbHM0R3FBcm90U25MV3FuemlmaHlwQkl2TFZhUjB4YlhSUVFJZXZjbUt1SmZkMFJPZlE1aXB0dzRwRnZrIiwidWluIjoiMzc1NTk3ODQ4NiJ9&data=Vl7cHqaa3SvJw92iD9uQ0Z6bsKz8exn91WbOTHhHLsNfW52AkFa2RQghnctPQdTxjs0NqzU5-AsNr-5FTJuF4A&svctype=4&tempid=h5_group_info");
 })).size(210,60).padTop(15).row();
    
    dialog.buttons.defaults().size(210, 64);
    dialog.buttons.button("@close", run(() => {
        dialog.hide();
    }));
    dialog.show();
});
