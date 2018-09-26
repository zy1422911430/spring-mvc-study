<%--
  Created by IntelliJ IDEA.
  User: My
  Date: 2018/9/25
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div id="msgFormPush"></div>
</body>
<script type="text/javascript" src="../assets/js/jquery-3.3.1.min.js"></script>
<script>
    if(!!window.EventSource){
        var source = new EventSource("event");
        var s = "";
        source.addEventListener("message",function (evt) {
            s+=evt.data+"<br/>";
            $("#msgFormPush").html(s);
        })
        source.addEventListener("open",function (evt) {
            console.log("连接打开");
        },false)
        source.addEventListener("error",function (evt) {
            console.log(evt)
            if(evt.readyState == EventSource.CLOSED){
                console.log("连接关闭");
            }else{
                console.log(evt.readyState);
            }
        },false)
    }else{
        console.log("您的浏览器不支持SSE")
    }
</script>
</html>
