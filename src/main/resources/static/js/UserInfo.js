function isYes(yes){
    if(yes ==true){
        return "男";
    }else{
        return "女";
    }
}

layui.use(['form','laydate', 'laypage', 'layer', 'table', 'carousel', 'upload', 'element', 'slider'], function(){
    var laydate = layui.laydate //日期
        ,form = layui.form
        ,laypage = layui.laypage //分页
        ,layer = layui.layer //弹层
        ,table = layui.table //表格
        ,carousel = layui.carousel //轮播
        ,upload = layui.upload //上传
        ,element = layui.element //元素操作
        ,slider = layui.slider //滑块
    table.render({
        elem: '#Usertable'
        ,id:'Usertable'
        ,height: 500
        ,url: '/userinfo/Search' //数据接口
        ,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
        ,page: true //开启分页
        ,cols: [[ //表头
            {type: 'checkbox', fixed: 'left'}
            ,{field: 'username', title: '用户名', width:200}
            ,{field: 'sex', title: '性别', width:80, sort: true, templet:'<div>{{isYes(d.sex) }}</div>'}
            ,{field: 'nickname', title: '昵称', width:80}
            ,{field: 'password', title: '密码', width: 120}
            ,{field: 'school', title: '学校', width: 120, sort: true}
            ,{field: 'email', title: '邮箱地址', width: 180, sort: true}
            ,{field: 'introduce', title: '自我介绍', width: 280}
            ,{fixed: 'right', width: 265, align:'center', toolbar: '#bar'}
        ]]
    });
    $("#BtnSearchUserInfo").on("click",function () {
        table.reload('Usertable',{
            where:{
                type:$("#type").val(),
                content:$("#content").val(),
            }
            , page: {
                curr: 1
            }
        })
    })
    //每行右边的操作按钮事件
    table.on('tool(Usertable)', function(obj){ //注：tool 是工具条事件名，Usertable 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'detail'){
            //跳转到编辑界面
            layer.alert("用户名：" + data.username + "<br>性别：" + isYes(data.sex) + "<br>昵称：" + data.nickname + "<br>密码：" + data.password + "<br>学校：" + data.school + "<br>邮箱：" + data.email + "<br>自我介绍：" + data.introduce);
        } else if(layEvent === 'del'){
            if(data.length === 0){
                layer.msg('请选择一行');
            } else if(data.length > 1){
                layer.msg('只能同时删除一个');
            } else {
                //向服务端发送删除指令
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else if(data.length > 1){
                    layer.msg('只能同时删除一个');
                } else {
                    layer.confirm('真的删除用户名为：【'+data.username+'】的行？', function(index){
                        $.ajax({
                            url: "/userinfo/"+data.username,
                            type: "DELETE",
                            dataType: "json",
                            data: data.field,
                            success: function(result){
                                if(result.code==200){
                                    console.log(result);
                                    layer.msg("删除成功", {time: 2000},function(){
                                        window.parent.location.reload();//修改成功后刷新父界面
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
                }
            }
        } else if(layEvent === 'edit'){
            if(data.length === 0){
                layer.msg('请选择一行');
            } else if(data.length > 1){
                layer.msg('只能同时编辑一个');
            } else {
                //跳转到编辑界面
                layer.open({
                    type: 2,
                    offset: 'auto',
                    area: ['700px', '600px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: '/userinfo/'+ data.username
                });
            }
        }
        else if(layEvent === 'resetPW'){
            if(data.length === 0){
                layer.msg('请选择一行');
            } else if(data.length > 1){
                layer.msg('只能同时重置一个用户的密码');
            } else {
                console.log(data);
                layer.confirm('真的重置用户名为：【'+data.username+'】的密码为123456吗？', function(index){
                    $.ajax({
                        url: "/userinfo/ResetPW/"+data.username,
                        type: "PUT",
                        dataType: "json",
                        data: data.field,
                        success: function(result){
                            if(result.code==200){
                                console.log(result);
                                layer.msg("重置密码成功", {time: 2000},function(){
                                    window.parent.location.reload();//修改成功后刷新父界面
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
            }
        }
    });

    //左上角工具栏
    table.on('toolbar(Usertable)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id)
            ,data = checkStatus.data; //获取选中的数据
        switch(obj.event){
            case 'add': //左上角工具栏添加功能
                //跳转到添加界面
                layer.open({
                    type: 2,
                    offset: 'auto',
                    area: ['700px', '600px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: '/admin/info/AddUserInfo'
                });
                break;
            case 'update'://左上角工具栏修改功能
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else if(data.length > 1){
                    layer.msg('只能同时编辑一个');
                } else {
                    //跳转到编辑界面
                    layer.open({
                        type: 2,
                        offset: 'auto',
                        area: ['700px', '600px'],
                        fixed: false, //不固定
                        maxmin: true,
                        btn: ['修改', '取消'],
                        content: '/userinfo/'+ data[0].username,
                        yes: function (index, layero) {
                            var form = layer.getChildFrame('form', index);
                            var data = form.serializeArray();
                            $.ajax({
                                url: "/userinfo/"+data[0].value,
                                type: "PUT",
                                dataType: "json",
                                data: data,
                                success: function(result){
                                    if(result.code==200){
                                        layer.msg(result.msg, {time: 2000});
                                        obj.update({
                                            title:result.extend.q.title,
                                            updatetime:result.extend.q.updatetime,
                                        })
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
                            layer.close(index);
                        },
                        no: function (index, layero) {
                            layer.close(index);
                        },
                    });
                }
                break;
            case 'delete':  //左上角删除功能
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else {
                    var arr = JSON.stringify(data);
                    console.log(arr);
                    layer.confirm('真的删除这'+data.length+'条数据吗？', function(index){
                        $.ajax({
                            url: "/userinfo/deleteAll",
                            type: "DELETE",
                            dataType: "json",
                            data: {"arr":arr},
                            success: function(result){
                                if(result.code==200){
                                    console.log(result);
                                    layer.msg("删除成功", {time: 2000},function(){
                                        window.parent.location.reload();//修改成功后刷新父界面
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
                }
                break;
        };
    });
    //选完文件后不自动上传
    upload.render({
        elem: '#ChooseFile'
        ,url: '/excel/uploadUserInfo/'
        ,auto: false
        //,multiple: true
        ,exts: 'xls|xlsx' //只允许上传Excel文件
        ,accept: 'file' //普通文件
        ,bindAction: '#uploadUserInfoFile'
        ,done: function(res){
            window.parent.location.reload();//修改成功后刷新父界面
            layer.msg(res.msg, {time: 2000});
            console.log(res)
        }
    });
});