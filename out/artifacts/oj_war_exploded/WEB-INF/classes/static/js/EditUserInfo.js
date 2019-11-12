//监听修改提交
layui.use('form', function(){
    var form = layui.form;
    form.on('submit(alert)', function(data){
        $.ajax({
            url: "/userinfo/"+data.field.username,
            type: "PUT",
            dataType: "json",
            data: data.field,
            success: function(result){
                if(result.code==200){
                    console.log(result);
                    layer.msg("修改成功", {time: 2000},function(){
                        //如果你想关闭最新弹出的层，直接获取layer.index即可
                        // layer.close(layer.index); //它获取的始终是最新弹出的某个层，值是由layer内部动态递增计算的
                        window.parent.location.reload();//修改成功后刷新父界面
                        //当你在iframe页面关闭自身时
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index); //再执行关闭
                        console.log(result.msg);
                        layer.close(index);
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

