<%--
  Created by IntelliJ IDEA.
  User: My
  Date: 2018/9/25
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <pre>
        Welcome to Spring MVC world
    </pre>
</body>
<script>
    var startDate = '2018-02-26';
    genWeek(startDate,3);
    function genWeek(startDate,each){
        for(var i=1;i<=each;i++){
            var curMonthDays = getDates(startDate);
            var endTime = new Date(startDate).getTime()+curMonthDays * 24 * 60 * 60 * 1000;
            var endDate = new Date(endTime).getFullYear() + "-" + (new Date(endTime).getMonth() + 1) + "-" + new Date(endTime).getDate();
            getFullWeek(startDate,endDate,curMonthDays);
            var startTime = new Date(startDate).getTime()+(curMonthDays+1) * 24 * 60 * 60 * 1000;
            startDate = new Date(startTime).getFullYear() + "-" + (new Date(startTime).getMonth() + 1) + "-" + new Date(startTime).getDate();
        }
    }





    function getFullWeek(startDate,endDate,curMonthDays){
        var startWeek = '';
        var endWeek = '';
        var n = new Date(startDate).getTime();
        for(var j=1;j<=curMonthDays;j++){
            var result = new Date(n);
            var curDate = result.getFullYear() + "-" + (result.getMonth() + 1) + "-" + result.getDate();
            var curWeek = result.getDay()+1;
            if(j==1){
                startWeek = startDate;
            }

            if(curWeek == 2){
                startWeek = curDate;
            }

            if(curWeek == 1 && j!=curMonthDays){
                endWeek = curDate;
                console.log(startWeek+"----"+endWeek);
                alert(startWeek+"----"+endWeek)
            }

            if(j==curMonthDays){
                endWeek = endDate;
                console.log(startWeek+"----"+endWeek);
                alert(startWeek+"----"+endWeek)
            }
            n = n+ 1 * 24 * 60 * 60 * 1000;
        }
    }

    function getDates(targetDate){
        var curDate = new Date(targetDate);
        var curMonth = curDate.getMonth();
        curDate.setMonth(curMonth + 1);
        curDate.setDate(0);
        return curDate.getDate();
    }
</script>
</html>
