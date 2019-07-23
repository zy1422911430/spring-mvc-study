<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=7DVLIq28iZjIctfmH23pbVMlvTEIBwtG"></script>
    <title>地址解析</title>
</head>
<body>
<div>
    <input id="changeLocation" value="change1" onclick="changeLocation()"/>
    <input id="changeLocation1" value="change2" onclick="changeLocation1()"/>
</div>
<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">
    console.log("http://api.map.baidu.com/api?v=2.0&ak=7DVLIq28iZjIctfmH23pbVMlvTEIBwtG")
    // 百度地图API功能
    // 创建地址解析器实例
    /*var myGeo = new BMap.Geocoder();
    // 将地址解析结果显示在地图上,并调整地图视野
    myGeo.getPoint("长沙市华晨世纪广场", function(point){
        if (point) {
            map.centerAndZoom(point, 16);
            map.addOverlay(new BMap.Marker(point));
        }else{
            console.log("您选择地址没有解析到结果")
            alert("您选择地址没有解析到结果!");
        }
    }, "湖南省");*/
    /*map.clearOverlays();
    var new_point = new BMap.Point(112.97935279,28.21347823);
    var marker = new BMap.Marker(new_point);  // 创建标注
    map.addOverlay(marker);              // 将标注添加到地图中
    map.panTo(new_point);*/
    /*var p1 = new BMap.Point(116.301934,39.977552);
    var p2 = new BMap.Point(116.508328,39.919141);

    var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});
    driving.search(p1, p2);
    p1 = new BMap.Point(116.508328,39.919141);
    p2 = new BMap.Point(116.608328,39.909141);

    var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});
    driving.search(p1, p2);*/
    var map = new BMap.Map("allmap");
    //map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
    map.enableScrollWheelZoom();

    function changeLocation() {
        var data = [
            [112.992029,28.204077],
            [113.702337,28.922067],
            [114.205388,30.339267],
            [114.276678,30.62261]
        ];
        map.clearOverlays();
        currentLocation(data);
    }

    function changeLocation1() {
        var data = [
            [112.992029,28.204077],
            [113.702337,28.922067],
            [114.205388,30.339267],
            [114.339748,30.52291]
        ];
        map.clearOverlays();
        currentLocation(data);
    }


    function currentLocation(data) {
        // 根据轨迹点，两两连线，最终合成一条完整的轨迹
        var abc = data;
        var chartData = [];
        for (let value of data) {
            chartData.push(new BMap.Point(value[0], value[1]));
        }

        var points = [];
        for (var i = 0; i < chartData.length-1; i++) {
            var startPoint = chartData[i];
            var endPoint = chartData[i + 1];
            showPath(startPoint, endPoint,i==0,i==chartData.length-2);
        }
    }


    // 两个坐标点连线
    function showPath(startPoint, EndPoint,displayStartIcon,displayEndIcon){
        var walking = null;
        if(displayStartIcon && !displayEndIcon){ // 第一个起点只展示起点图标
            walking = new BMap.DrivingRoute(map, { renderOptions: { map: map, autoViewport: true }
                ,onMarkersSet:function(routes) {
                    removeMarker(map, routes[1].marker);
                    var myIcon = new BMap.Icon("/image/start.png", new BMap.Size(40,40));
                    var markerStart = new BMap.Marker(routes[0].marker.getPosition() ,{icon:myIcon}); // 创建点
                    removeMarker(map, routes[0].marker);
                    addMarker(map, markerStart);
                    addMsg(startPoint , markerStart,"hhhh", "xxxxx");
                }
            });
        }else if(!displayStartIcon && !displayEndIcon){//中间的起点终点不展示起点、终点图标
            walking = new BMap.DrivingRoute(map, { renderOptions: { map: map, autoViewport: true },onMarkersSet:function(routes) {map.removeOverlay(routes[0].marker);map.removeOverlay(routes[1].marker);}});
            var marker = new BMap.Marker(startPoint);
            addMarker(map, marker);
            addMsg(startPoint , marker,"hhhh", "xxxxx");
            var marker1 = new BMap.Marker(EndPoint);
            addMarker(map,marker1);
            addMsg(EndPoint , marker1 ,"dddd", "gggg");
        }else{// 最后一个终点只展示终点图标
            walking = new BMap.DrivingRoute(map, { renderOptions: { map: map, autoViewport: true },onMarkersSet:function(routes) {
                removeMarker(map, routes[0].marker);
                var myIcon = new BMap.Icon("/image/end.png", new BMap.Size(40,40));
                var markerEnd = new BMap.Marker(routes[1].marker.getPosition() ,{icon:myIcon}); // 创建点
                removeMarker(map, routes[1].marker);
                addMarker(map, markerEnd);
                addMsg(EndPoint , markerEnd,"hhhh", "xxxxx");
            }});
        }
        walking.search(startPoint, EndPoint);
    }

    function addMarker(map, marker){
        map.addOverlay(marker);
    }

    function removeMarker(map, marker){
        map.removeOverlay(marker);
    }

    function addMsg(point , marker,title, message) {
        var opts = {
            width : 250,     // 信息窗口宽度
            height: 80,     // 信息窗口高度
            title : title , // 信息窗口标题
            enableMessage:true//设置允许信息窗发送短息
        };
        addClickHandler(message, marker, opts, point);
    }

    function addClickHandler(content,marker, opts, point){
        marker.addEventListener("click",function(e){
            openInfo(content, opts, point)}
        );
    }
    function openInfo(content, opts, point){
        var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,point); //开启信息窗口
    }


    // 百度地图API功能
   /* map = new BMap.Map("allmap");
    map.centerAndZoom(new BMap.Point(116.417854,39.921988), 15);
    var data_info = [[116.417854,39.921988,"地址：北京市东城区王府井大街88号乐天银泰百货八层"],
        [116.406605,39.921585,"地址：北京市东城区东华门大街"],
        [116.412222,39.912345,"地址：北京市东城区正义路甲5号"]
    ];
    var opts = {
        width : 250,     // 信息窗口宽度
        height: 80,     // 信息窗口高度
        title : "信息窗口" , // 信息窗口标题
        enableMessage:true//设置允许信息窗发送短息
    };
    for(var i=0;i<data_info.length;i++){
        var marker = new BMap.Marker(new BMap.Point(data_info[i][0],data_info[i][1]));  // 创建标注
        var content = data_info[i][2];
        map.addOverlay(marker);               // 将标注添加到地图中
        addClickHandler(content,marker);
    }
    function addClickHandler(content,marker){
        marker.addEventListener("click",function(e){
            openInfo(content,e)}
        );
    }
    function openInfo(content,e){
        var p = e.target;
        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
        var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,point); //开启信息窗口
    }*/
</script>