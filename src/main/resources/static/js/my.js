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
        } else if (mess.hasOwnProperty("allSys0")) {
            getSysChat(mess)
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
        "                                                    class=\"";
    var con = "\"><i class=\"\"></i></div>\n" +
        "                                            <div class=\"info\"><h3 class=\"nickname\"><span class=\"nickname-text2\">";
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
                var b = "<div ondblclick='relationChat(\"" + friends[i][j].username + cona + "img5" + con + friends[i][j].username + "</span>" + conb;
                content.innerHTML += b;
            } else {
                var c = "<div ondblclick='relationChat(\"" + friends[i][j].username + cona + "img6" + con + friends[i][j].username + "</span>" + conb;
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
            "            <button class=\"off02\" onclick=\"exitS('chatView" + nameB + "')\"></button>\n" +
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
    }
    chatS.hidden = false;
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
            "                                                    class=\"";
        var con = "\"><i class=\"\"></i></div>\n" +
            "                                            <div class=\"info\"><h3 class=\"nickname\"><span class=\"nickname-text2\" id='goodF'>";
        var conb = "</h3>\n" +
            "<p class=\"msg\"><span></span></p></div>\n" +
            "</div>\n";
        var b;
        if (mess.status == "1") {
            b = cona + "img5" + con + mess.username + "</span>" + conb;
        } else {
            b = cona + "img6" + con + mess.username + "</span>" + conb;
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
    var content = document.getElementById("content" + a);
    content.innerHTML += c + message.value + d;
    message.value = "";
    socket.send(JSON.stringify(mess));
}

//是否同意，
function ViewChat() {
    document.getElementById("chatQ_sys").ondblclick = function () {
        var chatS = document.getElementById("chatView_sys");
        if (chatS == null) {
            var chatPart = document.getElementById("chatPart");
            var view = "<div class=\"chatView\" id=\"chatView_sys\">\n" +
                "        <div class=\"top1\">\n" +
                "            <p class=\"timeView\"></p><img src=\"images/james.jpg\" alt=\"\" class=\"img1\"><i class=\"\"></i>\n" +
                "            <div class=\"nameA0\">系统信息</div>\n" +
                "            <button class=\"off02\" onclick=\"exitS('chatView_sys')\"></button>\n" +
                "        </div>\n" +
                "        <div class=\"chatContent\" id=\"sys_content\">\n" +
                "</div>\n" +
                "    </div>"
            chatPart.innerHTML += view;
            socket.send("{\"GetChat\": \"sys\"}");
        }
        chatS.hidden = false;
    }
}

function getSys(mess) {
    var sysMess = document.getElementById("chatQ_sys");
    var chatS = document.getElementById("chatView_sys");
    var timestampToTime1;
    if (sysMess == null) {
        var news = document.getElementById("news");
        var a = "<div class='icon-list-item active2'";
        var k = ">\n" +
            "                                    <div class=\"messTime\"><p class=\"attr\" id='time_sys'>";
        var b = "</p>\n" +
            "                                    </div>\n" +
            "                                    <div class=\"avatar\"><img\n" +
            "                                            src=\"images/james.jpg\"\n" +
            "                                            alt=\"\" class=\"img7\"><i\n" +
            "                                            class=\"\"></i></div>\n" +
            "                                    <div class=\"info\"><h3 class=\"nickname\"><span\n" +
            "                                            class=\"nickname-text3\">" + "系统信息";
        var c = "</span></h3>\n" +
            "                                        <p class=\"msg\"><span id='mess_sys'>";
        var d = "</span></p></div>\n" +
            "                                    <em class=\"close-icon\"><i class=\"fas fa-times-circle\"></i></em></div>";

        var u;
        var message;
        timestampToTime1 = timestampToTime(mess["sys0"].time);
        var view = "id=\"chatQ_sys\"";
        message = mess["sys0"].nameA + mess["sys0"].message;
        u = a + view + k + timestampToTime1 + b + c + message + d;
        news.innerHTML += u;
    } else {
        timestampToTime1 = timestampToTime(mess["sys0"].time);
        document.getElementById("time_sys").innerText = timestampToTime1;
        document.getElementById("mess_sys").innerText = mess["sys0"].nameA + mess["sys0"].message;
    }
    ViewChat(timestampToTime1, mess);
    if (chatS != null) {
        var sysContent = document.getElementById("sys_content");
        var f = "            <div class=\"messC\"><img src=\"images/james.jpg\"\n" +
            "                                    alt=\"\" class=\"img2\"><i\n" +
            "                    class=\"\"></i>\n" +
            "                <div class=\"chatMess\" id='sys_" + mess["sys0"].nameA + "'>";
        f += mess["sys0"].nameA + mess["sys0"].message + "</div></div>";
        if (mess["sys0"].message == "请求添加您为好友！" && mess["sys0"].status == 0) {
            f += "<div id='commit" + mess["sys0"].nameA + "'>\n" +
                "                <button type=\"button\" class=\"agree\" id='agree" + mess["sys0"].nameA + "'>同意</button>\n" +
                "                <button type=\"button\" class=\"disagree\" id='disagree" + mess["sys0"].nameA + "'>不同意</button>\n" +
                "            </div>\n";
            sysContent.innerHTML += f;
            document.getElementById("agree" + mess["sys0"].nameA).onclick = function () {
                var relation = {
                    "nameA": mess["sys0"].nameA,
                    "relation": "0"
                }
                socket.send(JSON.stringify(relation));
                document.getElementById("commit" + mess["sys0"].nameA).innerHTML = "<div style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px;width:100px'>已同意</div>";
            }
            document.getElementById("disagree" + mess["sys0"].nameA).onclick = function () {
                var relation = {
                    "nameA": mess["sys0"].nameA,
                    "relation": "-1"
                }
                socket.send(JSON.stringify(relation));
                document.getElementById("commit" + mess["sys0"].nameA).innerHTML = "<div  style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px'>已拒绝</div>";
            }
        }
    }

}

function getSysChat(mess) {
    var sysContent = document.getElementById("sys_content");
    for (var item in mess) {
        var f = "            <div class=\"messC\"><img src=\"images/james.jpg\"\n" +
            "                                    alt=\"\" class=\"img2\"><i\n" +
            "                    class=\"\"></i>\n" +
            "                <div class=\"chatMess\" id='sys_" + mess[item].nameA + "'>";
        f += mess[item].nameA + mess[item].message + "</div></div>";
        if (mess[item].message == "请求添加您为好友！" && mess[item].status == 0) {
            f += "<div id='commit" + mess[item].nameA + "'>\n" +
                "                <button type=\"button\" class=\"agree\"  onclick='agree(\"" + mess[item].nameA + "\")'>同意</button>\n" +
                "                <button type=\"button\" class=\"disagree\" onclick='disagree(\"" + mess[item].nameA + "\")'>不同意</button>\n" +
                "            </div>\n";
            sysContent.innerHTML += f;
        } else {
            sysContent.innerHTML += f;
        }
    }
}

function getMess(mess) {
    //如果有新消息，添加到消息界面。
    var nameA = mess["mess"].nameA;
    var chatQ = document.getElementById("chatQ" + nameA);
    var chatS = document.getElementById("chatView" + nameA);
    if (chatQ == null) {
        var news = document.getElementById("news");
        var a = "<div class=\"icon-list-item active2\" id='chatQ" + nameA + "' ondblclick='relationChat(\"" + nameA + "\")'>\n";
        var k = "                                    <div class=\"messTime\"><p class=\"attr\" id='net" + nameA + "'>";
        var b = "</p>\n" +
            "                                    </div>\n" +
            "                                    <div class=\"avatar\"><img\n" +
            "                                            src=\"images/james.jpg\"\n" +
            "                                            alt=\"\" class=\"img7\"><i\n" +
            "                                            class=\"\"></i></div>\n" +
            "                                    <div class=\"info\"><h3 class=\"nickname\"><span\n" +
            "                                            class=\"nickname-text3\">";
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
        document.getElementById("time" + nameA).innerText = mess["mess"].time.substring(0, 19);
    }
}

//添加好友按钮绑定
function agree(name) {
    var relation = {
        "nameA": name,
        "relation": "0"
    }
    socket.send(JSON.stringify(relation));
    document.getElementById("commit" + name).innerHTML = "<div style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px;width:100px'>已同意</div>";
}

function disagree(name) {
    var relation = {
        "nameA": name,
        "relation": "-1"
    }
    socket.send(JSON.stringify(relation));
    document.getElementById("commit" + name).innerHTML = "<div  style='color: cornflowerblue;position: relative;left: 180px;font-size: 20px'>已拒绝</div>";
}




