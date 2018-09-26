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
</body>
<script type="text/javascript" src="../assets/js/jquery-3.3.1.min.js"></script>
<script>
    deferred();

    function deferred(){
        console.log("执行了deferred");
        $.get('/async/defer',function (data) {
            console.log("执行了defer请求");
            console.log(data);
            deferred();
        })
    }
</script>
</html>
