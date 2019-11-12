//监听添加用户操作
layui.use('form', function(){
    var form = layui.form;
    form.on('submit(addUser)', function(data){
        $.ajax({
            url: "/userinfo",
            type: "POST",
            dataType: "json",
            data: data.field,
            success: function(result){
                if(result.code==200){
                    console.log(result);
                    layer.alert("添加成功", {icon: 6}, function () {
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index);
                        //修改成功后刷新父界面
                        window.parent.location.reload();
                        console.log(result.msg);
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
