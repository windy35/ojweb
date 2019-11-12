//监听添加操作
layui.use(['form'], function(){
    var form = layui.form
    form.on('submit(addQuestion)', function(data){
        $.ajax({
            url: "/questioninfo",
            type: "POST",
            dataType: "json",
            data: data.field,
            success: function(result){
                if(result.code==200){
                    parent.layui.table.reload('Questiontable',{
                        where: {
                            type:0
                            ,content: ""
                        }
                    });
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                    // window.parent.location.reload();
                }else{
                    console.log(result);
                    layer.msg(result.msg, {time: 2000});
                }
            },
            error: function(result){
                console.log(data.field);
                console.log("出错了");
                console.log(result);
            }
        });
        return false;
    });
});
