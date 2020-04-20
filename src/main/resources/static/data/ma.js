(function () {
    var params = {};
    var args = '';
    //Document对象数据
    if (document) {
        params.domain = document.domain || ''; //获取域名
        params.url = document.URL || '';       //当前Url地址
        params.title = document.title || '';
        params.referrer = document.referrer || '';  //上一跳路径
    }
    //Window对象数据
    /*if (window && window.screen) {
        params.sh = window.screen.height || 0;    //获取显示屏信息
        params.sw = window.screen.width || 0;
        params.cd = window.screen.colorDepth || 0;
    }*/
    //navigator对象数据
    if (navigator) {
        params.language = navigator.language || '';    //获取所用语言种类
    }
    /*//解析_maq配置
    if (_maq) {
        for (var i in _maq) {                      //获取埋点阶段，传递过来的用户行为
            switch (_maq[i][0]) {
                case '_setAccount':
                    params.account = _maq[i][1];
                    break;
                default:
                    break;
            }
        }
    }*/
    var i;
    var els = document.getElementsByTagName('button');
    for(i=0 ; i<els.length ; i++){
        if (els[i].addEventListener) {
            els[i].addEventListener("click", processEvent, false);
        } else if (els[i].attachEvent) {
            els[i].attachEvent("onclick", processEvent, false);
        }

    }
    var j;
    var elsInput = document.getElementsByTagName('input');
    for(j=0 ; j<elsInput.length ; j++){
        if (elsInput[j].addEventListener) {
            elsInput[j].addEventListener("click", processEvent, false);
        } else if (elsInput[j].attachEvent) {
            elsInput[j].attachEvent("onclick", processEvent, false);
        }
    }
    var clickArr = [];
    //点击事件监听
    function processEvent(e){
        var target = e.target;
        var nodeName = target.nodeName;
        switch (nodeName) {
            case "BUTTON":
                var temp = {};
                temp.clickName = target.innerHTML;
                temp.href = "";
                clickArr.push(temp);
                break;
            case "INPUT":
                var inputType = target.type;
                if ("button" == inputType) {
                    var temp = {};
                    temp.clickName = target.value;
                    temp.href = "";
                    clickArr.push(temp);
                }
                break;
        }
        if (clickArr.length > 10) {
            // 攒够10次发送
            console.log(clickArr);
            params.clickEvents = clickArr;
            params.requetTime = new Date().getTime();
            sendData(params);
            //重置点击事件集合
            clickArr = [];
        }

    }
    function sendData(params) {
        //通过伪装成Image对象，请求后端脚本
        var img = new Image(1, 1);
        var jsonArgs = JSON.stringify(params);
        var src = 'http://localhost:8080/dataCollection/log.gif?args=' + encodeURIComponent(jsonArgs);
        console.log(src);
        img.src = src;
    }
    $(document).ready(function(){
        //通过伪装成Image对象，请求后端脚本
        var img = new Image(1, 1);
        params.clickEvents = [];
        params.requetTime = new Date().getTime();
        sendData(params);
    });
})();