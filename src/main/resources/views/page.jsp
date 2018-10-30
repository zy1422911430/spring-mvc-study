<%--
  Created by IntelliJ IDEA.
  User: My
  Date: 2018/9/25
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>手机壳合成</title>

    <style type="text/css">
        #status{
            text-align: center;
        }
        canvas{
            margin: 0 auto;
        }
    </style>

</head>
<body>


图片合成中，请稍等......

<canvas id="canvas" width="768" height="1080"></canvas>
<script type="text/javascript">
    var canvas = document.getElementById("canvas"),
        ctx = canvas.getContext("2d"),
        arr = [];
    function getImageData(src,callback){
        var im = new Image(),
            canvas = document.createElement("canvas"),
            ctx = canvas.getContext("2d");
        im.src = src;
        im.onload = function(){
            canvas.width = this.width;
            canvas.height = this.height;
            ctx.drawImage(im,0,0,canvas.width,canvas.height);
            arr.push(ctx.getImageData(0,0,canvas.width,canvas.height));
            callback();
        }
    }
    getImageData("assets/images/701f0dcbfe5859b3aef99c47b2896754.png",function(){ //装载手机壳图片像素
        getImageData("assets/images/timg.jpg",function(){ //装载手机壳装饰图片像素
            make(); //合成手机壳
            document.getElementById("status").innerHTML = '图片合成完毕！';
        });
    });
    function make(){
        var target = arr[0], //手机壳图片
            add = arr[1]; //装饰图片
        console.log(target);
        debugger
        for(var i=0;i<target.height;i++){
            for(var j=0;j<target.width;j++){
                var index = i * target.width * 4 + j * 4,
                    r = target.data[index+0],
                    g = target.data[index+1],
                    b = target.data[index+2],
                    a = target.data[index+3];
                if(a<255){
                    //debugger
                    var index1 = (i) * add.width * 4 + (j) * 4;
                    if(add.data[index1]){ //手机壳和装饰图片可能长宽不一样
                        if(a==0){ //手机壳全部透明的地方直接取装饰图片像素
                            target.data[index] = add.data[index1];
                            target.data[index+1] = add.data[index1+1];
                            target.data[index+2] = add.data[index1+2];
                            target.data[index+3] = 255;
                        }else{
                            //手机壳不完全透明的地方根据其透明度融合手机壳和装饰图片
                            var ratio = a / 255;
                            console.log(ratio)
                            target.data[index] = ratio * r + (1 - ratio) * add.data[index1];
                            target.data[index+1] = ratio * g + (1 - ratio) * add.data[index1+1];
                            target.data[index+2] =  ratio * b + (1 - ratio) * add.data[index1+2];
                            target.data[index+3] = 255;
                        }
                    }else{
                        //手机壳不透明的地方用原来手机壳的像素
                        break;
                    }
                }
                continue;
            }
        }
        ctx.putImageData(target,0,0);
    }
</script>
</body>
</html>
