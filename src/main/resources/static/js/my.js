window.onload = function () {
    document.getElementById("relation01").hidden = true;
    friendDis2();
}

function relation() {
    document.getElementById("mess01").hidden = true;
    document.getElementById("relation01").hidden = false;
}

function message() {
    document.getElementById("relation01").hidden = true;
    document.getElementById("mess01").hidden = false;
}

function display01(a) {
    var content = document.getElementById(a);
    if (content.style.display == "none") {
        content.style.display = "block";
    } else {
        content.style.display = "none";
    }
}

var socket;

function friendDis2() {
    socket = new WebSocket("ws://localhost:8080/friends")
    socket.onopen = function () {
    }
    socket.onmessage = function (msg) {
        var mess = JSON.parse(msg.data);
        if (mess.hasOwnProperty("0")) {
            getFriendsMess(mess);
        } else if (mess.hasOwnProperty("mess")) {
            getMess(mess)
        } else if (mess.hasOwnProperty("chat0")) {
            getChat(mess)
        } else if (mess.hasOwnProperty("sys0")) {
            getSys(mess);
        } else if (mess.hasOwnProperty("search")) {
            search02(mess);
        } else if (mess.hasOwnProperty("aF")) {
            SendAddFriends(mess);
        }
    }
    socket.onclose = function () {
    }
    socket.onerror = function () {
        alert("网络故障！");
    }
}

function getFriendsMess(friends) {
    var cona = "\")'>\n" +
        "                                        <div class=\"icon-list-item active1\">\n" +
        "                                            <div class=\"avatar\"><img\n" +
        "                                                    src=\"images/james.jpg\" alt=\"\"\n" +
        "                                                    class=\"img gray\"><i class=\"\"></i></div>\n" +
        "                                            <div class=\"info\"><h3 class=\"nickname\"><span class=\"nickname-text\">";
    var conb = "</h3>\n" +
        "<p class=\"msg\"><span></span></p></div>\n" +
        "</div>\n" +
        "</div>"
    for (var i = 0; i < 4; i++) {
        var outLine = "";
        var content = document.getElementById("content" + i);
        content.innerHTML = "";
        var count = document.getElementById("count" + i);
        var onLine = 0;
        for (var j = 0; j < friends[i].length; j++) {
            if (friends[i][j].status == 1) {
                onLine += 1;
                var b = "<div ondblclick='relationChat(\"" + friends[i][j].username + cona + friends[i][j].username + "</span>在线" + conb;
                content.innerHTML += b;
            } else {
                var c = "<div ondblclick='relationChat(\"" + friends[i][j].username + cona + friends[i][j].username + "</span>离线" + conb;
                outLine += c;
            }
        }
        count.innerText = "[" + onLine + "/" + friends[i].length + "]";

        content.innerHTML += outLine;
    }
}

function relationChat(nameB) {
    var chatS = document.getElementById("chatView" + nameB);
    if (chatS == null) {
        //第一次查询要获得全部聊天内容
        var chatPart = document.getElementById("chatPart");
        var a = "<div class=\"chatView\" id='chatView" + nameB + "'>\n" +
            "        <div class=\"top1\">\n" +
            "            <p class=\"timeView\" id='time" + nameB + "'>";
        var b = "</p><img src=\"images/james.jpg\"\n" +
            "                 alt=\"\" class=\"img1\"><i\n" +
            "                class=\"\"></i>\n" +
            "            <div class=\"nameA0\" >";
        var c = "</div>\n" +
            "            <button class=\"off01\" onclick=\"exitS('chatView" + nameB + "')\"></button>\n" +
            "        </div>\n" +
            "        <div class=\"chatContent\" id='content" + nameB + "'>\n";
        var d = "<div class=\"messC\"><img src=\"images/james.jpg\"\n" +
            "                                    alt=\"\" class=\"img2\"><i\n" +
            "                    class=\"\"></i>\n" +
            "                <div class=\"chatMess\">";
        var e = " </div>\n" +
            "        <div class=\"inputView\">\n" +
            "            <textarea class=\"textA\" id='text" + nameB + "'></textarea>\n" +
            "            <button type=\"button\" class=\"sendB\" onclick='sendMess(\"" + nameB + "\")'>发送</button>\n" +
            "        </div>\n" +
            "    </div>";
        var chat;
        chat = a + b + nameB + c + e;
        chatPart.innerHTML += chat;
        var mess = {
            "GetChat": nameB
        }
        socket.send(JSON.stringify(mess));
    } else {
        if (chatS.hidden == true) {
            chatS.hidden = false;
        }
    }
}
//处理第一次获取历史全部聊天
function getChat(mess) {
    var a = "<div class=\"messC\"><img src=\"images/james.jpg\"\n" +
        "                                alt=\"\" class=\"img2\"><i\n" +
        "        class=\"\"></i>\n" +
        "        <div class=\"chatMess\">";
    var b = "</div>\n" +
        "    </div>";
    var c = "<div class=\"messCB\">\n" +
        "        <div class=\"chatMess2\">";
    var d = "</div>\n" +
        "        <img src=\"images/james.jpg\"\n" +
        "             alt=\"\" class=\"img3\"><i\n" +
        "            class=\"\"></i>\n" +
        "    </div>";
    var nameA = document.getElementById("name01").innerText;
    if (nameA == mess["chat0"].nameA) {
        nameA = mess["chat0"].nameB;
    } else {
        nameA = mess["chat0"].nameA;
    }
    var chatContent = document.getElementById("content" + nameA);
    for (var item in mess) {
        if (mess[item].nameA == nameA) {
            chatContent.innerHTML += a + mess[item].message + b;
        } else {
            chatContent.innerHTML += c + mess[item].message + d;
        }
    }
    var time = document.getElementById("time" + nameA);
    time.innerText = timestampToTime(mess[item].time);
}

function exit() {
    window.location.href = "/";
}

function add() {
    var s = document.getElementById("first_add");
    if (s.hidden == true) {
        s.hidden = false;
    } else {
        s.hidden = true;
    }
}

function exitS(od) {
    var s = document.getElementById(od);
    s.hidden = true;
}

function search01() {
    document.getElementById("addF01").innerHTML = "";
    document.getElementById("tip03").innerText = "";
    document.getElementById("addB01").hidden = true;
    var sear = document.getElementById("sear01");
    var tip = document.getElementById("tip02");
    if (sear.value.length < 8 || sear.value.length > 12) {
        tip.innerText = "亲，账号长度不对欧！";
    } else {
        tip.innerText = "";
        var target = {
            "search": sear.value
        }
        socket.send(JSON.stringify(target));
    }
}

function search02(mess) {
    var addB01 = document.getElementById("addB01");
    var add = document.getElementById("addF01");
    if (mess.search == "1") {
        var cona = "<div>\n" +
            "                                        <div class=\"icon-list-item active1\" style='border: #0608ff 2px solid;border-radius: 15px;width: 330px'>\n" +
            "                                            <div class=\"ext\"><p class=\"attr\"></p></div>\n" +
            "                                            <div class=\"avatar\"><img\n" +
            "                                                    src=\"images/james.jpg\" alt=\"\"\n" +
            "                                                    class=\"img gray\"><i class=\"\"></i></div>\n" +
            "                                            <div class=\"info\"><h3 class=\"nickname\"><span class=\"nickname-text\" id='goodF'>";
        var conb = "</h3>\n" +
            "<p class=\"msg\"><span></span></p></div>\n" +
            "</div>\n";
        var b;
        if (mess.status == "1") {
            b = cona + mess.username + "</span>在线" + conb;
        } else {
            b = cona + mess.username + "</span>离线" + conb;
        }
        add.innerHTML = b;
        addB01.hidden = false;
    } else {
        add.innerHTML = "";
        addB01.hidden = true;
        var tip = document.getElementById("tip02");
        tip.innerText = "亲，该账号不存在欧！";
    }
}

function addFriend() {
    var gf = document.getElementById("goodF");
    socket.send("{\"addF\":\"" + gf.innerText + "\"}");
}

function SendAddFriends(mess) {
    var tip03 = document.getElementById("tip03");
    var a;
    if (mess.aF == 1) {
        a = "亲，发送成功！";
    } else if (mess.aF == 2) {
        a = "亲，请勿重复发送欧！";
    } else if (mess.aF == 3) {
        a = "亲，你们已经是好友了！";
    } else {
        a = "亲，发送失败！";
    }
    tip03.innerText = a;
}

//将时间戳转换成日期格式
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + (date.getHours()) : date.getHours()) + ':';
    var m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes()) + ':';
    var s = (date.getSeconds() < 10 ? '0' + (date.getSeconds()) : date.getSeconds());
    return Y + M + D + h + m + s;
}

function sendMess(a) {
    var message = document.getElementById("text" + a);
    var mess = {
        "mess": message.value,
        "nameA": a
    }
    var c = "<div class=\"messCB\">\n" +
        "        <div class=\"chatMess2\">";
    var d = "</div>\n" +
        "        <img src=\"images/james.jpg\"\n" +
        "             alt=\"\" class=\"img3\"><i\n" +
        "            class=\"\"></i>\n" +
        "    </div>";
    var content=document.getElementById("content"+a);
    content.innerHTML+=c+message.value+d;
    message.value="";
    socket.send(JSON.stringify(mess));
}

//是否同意， *bug
function ViewChat(item, timestampToTime1, mess) {
    var name01 = document.getElementById("name01");
    if (name01 == mess[item].nameA) {
        name01 = mess[item].nameB;
    } else {
        name01 = mess[item].nameA;
    }
    document.getElementById("chatV" + name01).ondblclick = function () {
        var chatS = document.getElementById("chatView" + name01);
        if (chatS == null) {
            var chatPart = document.getElementById("chatPart");
            var a = "<div class=\"chatView\" id='chatView" + chatView + "'>\n" +
                "        <div class=\"top1\">\n" +
                "            <p class=\"timeView\" >";
            var b = "</p><img src=\"images/james.jpg\"\n" +
                "                 alt=\"\" class=\"img1\"><i\n" +
                "                class=\"\"></i>\n" +
                "            <div class=\"nameA0\" >" + "系统信息";
            var c = "</div>\n" +
                "            <button class=\"off01\" onclick=\"exitS('chatView" + chatView + "')\"></button>\n" +
                "        </div>\n" +
                "        <div class=\"chatContent\">\n" +
                "            <div class=\"messC\"><img src=\"images/james.jpg\"\n" +
                "                                    alt=\"\" class=\"img2\"><i\n" +
                "                    class=\"\"></i>\n" +
                "                <div class=\"chatMess\">";
            var chat;
            chat = a + timestampToTime1 + b + c + mess[item].nameA + mess[item].message + "</div>\n" +
                "            </div>\n";
            if (mess[item].message == "请求添加您为好友！" && mess[item].status == 0) {
                chat += "<div id='commit" + messNumber + "'>\n" +
                    "                <button type=\"button\" class=\"agree\" id='agree" + messNumber + "'>同意</button>\n" +
                    "                <button type=\"button\" class=\"disagree\" id='disagree" + messNumber + "'>不同意</button>\n" +
                    "            </div>\n" +
                    "        </div></div>";
                chatPart.innerHTML += chat;
                document.getElementById("agree" + messNumber).onclick = function () {
                    var relation = {
                        "nameA": mess[item].nameA,
                        "relation": "0"
                    }
                    socket.send(JSON.stringify(relation));
                    document.getElementById("commit" + messNumber).innerHTML = "<div style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px;width:100px'>已同意</div>";
                }
                document.getElementById("disagree" + messNumber).onclick = function () {
                    var relation = {
                        "nameA": mess[item].nameA,
                        "relation": "-1"
                    }
                    socket.send(JSON.stringify(relation));
                    document.getElementById("commit" + messNumber).innerHTML = "<div  style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px'>已拒绝</div>";
                }
            } else {
                chatPart.innerHTML += chat;
            }
        } else {
            if (chatS.hidden == true) {
                chatS.hidden = false;
            } else {
                chatS.hidden = true;
            }
            return;
        }
    }
}
//refactor
function getSys(mess) {
    var news = document.getElementById("news");
    var a = "<div class=\"icon-list-item active\"";
    var k = ">\n" +
        "                                    <div class=\"ext\"><p class=\"attr\">";
    var b = "</p>\n" +
        "                                    </div>\n" +
        "                                    <div class=\"avatar\"><img\n" +
        "                                            src=\"images/james.jpg\"\n" +
        "                                            alt=\"\" class=\"img gray\"><i\n" +
        "                                            class=\"\"></i></div>\n" +
        "                                    <div class=\"info\"><h3 class=\"nickname\"><span\n" +
        "                                            class=\"nickname-text\">" + "系统信息";
    var c = "</span></h3>\n" +
        "                                        <p class=\"msg\"><span>";
    var d = "</span></p></div>\n" +
        "                                    <em class=\"close-icon\"><i class=\"fas fa-times-circle\"></i></em></div>";
    messNumber = 0;
    for (var item in mess) {
        var u;
        var message;
        var timestampToTime1 = timestampToTime(mess[item].time);
        var view = "id=\"chatQ" + messNumber + "\"";
        message = mess[item].nameA + mess[item].message;
        u = a + view + k + timestampToTime1 + b + c + message + d;
        news.innerHTML += u;
        ViewChat(item, timestampToTime1, mess);
        messNumber++;
    }
}

function getMess(mess) {
    //如果有新消息，添加到消息界面。
    var nameA = mess["mess"].nameA;
    var chatQ = document.getElementById("chatQ" + nameA);
    var chatS = document.getElementById("chatView" + nameA);
    if (chatQ == null) {
        var news = document.getElementById("news");
        var a = "<div class=\"icon-list-item active\" id='chatQ" + nameA + "' ondblclick='relationChat(\"" + nameA + "\")'>\n";
        var k = "                                    <div class=\"ext\"><p class=\"attr\" id='net" + nameA + "'>";
        var b = "</p>\n" +
            "                                    </div>\n" +
            "                                    <div class=\"avatar\"><img\n" +
            "                                            src=\"images/james.jpg\"\n" +
            "                                            alt=\"\" class=\"img gray\"><i\n" +
            "                                            class=\"\"></i></div>\n" +
            "                                    <div class=\"info\"><h3 class=\"nickname\"><span\n" +
            "                                            class=\"nickname-text\">";
        var c = "</span></h3>\n" +
            "                                        <p class=\"msg\"><span id='new" + nameA + "'>";
        var d = "</span></p></div>\n" +
            "                                    <em class=\"close-icon\"><i class=\"fas fa-times-circle\"></i></em></div>";
        var u;
        var timestampToTime1 = timestampToTime(mess["mess"].time);
        u = a + k + timestampToTime1 + b + nameA + c + mess["mess"].message + d;
        news.innerHTML += u;
    } else {
        //新消息刷新时间和信息
        document.getElementById("new" + nameA).innerText = mess["mess"].message;
        document.getElementById("net" + nameA).innerText = timestampToTime(mess["mess"].time);
    }
    if (chatS != null) {
        var a = "<div class=\"messC\"><img src=\"images/james.jpg\"\n" +
            "                                alt=\"\" class=\"img2\"><i\n" +
            "        class=\"\"></i>\n" +
            "        <div class=\"chatMess\">";
        var b = "</div>\n" +
            "    </div>";
        var chatContent = document.getElementById("content" + nameA);
        chatContent.innerHTML += a + mess["mess"].message + b;
        document.getElementById("time"+nameA).innerText=mess["mess"].time.substring(0,19);
    }
}
