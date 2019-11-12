/**背景图片轮播*/
layui.use('carousel', function(){
    var carousel = layui.carousel;
    //建造实例
    carousel.render({
        elem: '#carousel',
        width: '100%', //设置容器宽度
        height: '100%',
        interval:1000,
        arrow: 'none',//不显示箭头
        anim: 'fade', //切换动画方式
        indicator:'none'
    });
});
//监听注册提交
layui.use('form', function(){
    var form = layui.form;
    form.on('submit(register)', function(data){
        $.ajax({
            url: "/register",
            type: "POST",
            dataType: "json",
            data: data.field,
            success: function(result){
                if(result.code==200){
                    console.log(result);
                    layer.msg("操作成功", {time: 2000},function(){
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index);
                        console.log(result.msg);
                        window.location.href=result.extend.url;
                    });
                    layer.msg(result.msg, {time: 2000});
                }else{
                    console.log(result);
                    layer.msg(result.msg, {time: 2000});
                }
            },
            error: function(result){
                console.log("出错了");
                console.log(result);
            }
        });
        return false;
    });
});