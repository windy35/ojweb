function isYes(yes){
    if(yes ==true){
        return "可用";
    }else{
        return "不可用";
    }
}
// 数据表单时间戳转换为日期显示
function DateFormat(sjc){
    var date = new Date(sjc);
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    m = m<10?'0'+m:m;
    var d = date.getDate();
    d = d<10?("0"+d):d;
    var h = date.getHours();
    h = h<10?("0"+h):h;
    var min = date.getMinutes();
    min = min<10?("0"+min):min;
    var s = date.getSeconds();
    s = s<10?("0"+s):s;
    var str = y+"-"+m+"-"+d+" "+h+":"+min+":"+s;
    return str;
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
        elem: '#Questiontable'
        ,height: 500
        ,url: '/questioninfo/SearchQuestion?page=1&limit=30&timestamp='+ Date.parse(new Date())//数据接口
        ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        ,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
        ,limits:[30,60,70,80,90,100]
        // 分页 curr起始页，groups连续显示的页码，默认每页显示的条数
        , page: {
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']
            , curr: 1
            , groups: 6
            , limit: 50
        }
        ,cols: [[ //表头
            {type: 'checkbox', fixed: 'left'}
            ,{field: 'questionid', title: '编号', width:80, align: 'center'}
            ,{field: 'title', title: '标题',  align: 'center', sort: true}
            ,{field: 'accepttime', title: '通过次数',  width:100,align: 'center'}
            ,{field: 'updatetime', title: '修改时间', align: 'center',sort:true,
             templet:function (row){
                return DateFormat(row.updatetime);}}
            ,{field: 'status', title: '状态(点击切换)',  width:150, align: 'center',sort: true,event:'ChangeStatus',templet:function (row){
                        return isYes(row.status);}}
            ,{fixed: 'right',  align: 'center', align:'center', title:'操作选项',toolbar: '#bar'}
        ]]
    });
    $("#BtnSearchQuestionInfo").on("click",function () {
        table.reload('Questiontable',{
            where:{
                type:$("#type").val(),
                content:$("#content").val(),
            }
        })
    })
    //每行右边的操作按钮事件
    table.on('tool(Questiontable)', function(obj){ //注：tool 是工具条事件名，Questiontable 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'ChangeStatus'){
            $.ajax({
                url: "/questioninfo/ChangeStatus",
                type: "PUT",
                dataType: "json",
                data: data,
                success: function(result){
                    if(result.code==200){
                        obj.update({
                            status: result.extend.status
                        });
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
        }
        else if(layEvent === 'del'){
            if(data.length === 0){
                layer.msg('请选择一行');
            } else if(data.length > 1){
                layer.msg('只能同时删除一行');
            } else {
                //向服务端发送删除指令
                    layer.confirm('真的删除编号为：【'+data.questionid+'】的行？', function(index){
                        var arr = JSON.stringify(data);
                        $.ajax({
                            url: "/questioninfo/deleteAll",
                            type: "DELETE",
                            dataType: "json",
                            data: {"arr":"["+arr+"]"},
                            success: function(result){
                                if(result.code==200){
                                    console.log(result);
                                    layer.msg("删除成功", {time: 2000},function(){
                                        console.log(result.msg);
                                        layui.table.reload('Questiontable',{
                                            where:{
                                                type:$("#type").val(),
                                                content:$("#content").val(),
                                            }
                                        })
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
                    shadeClose: true, //点击遮罩关闭层
                    content: '/questioninfo/'+ data.questionid,
                    btn: ['修改', '取消'],
                    yes: function (index, layero) {
                        var form = layer.getChildFrame('form', index);
                        var data = form.serializeArray();
                        $.ajax({
                            url: "/questioninfo/"+data[0].value,
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
                    // end: function (index) {
                    //     // table.reload('Questiontable')
                    //
                    // }
                });
            }
        }
    });

    //左上角工具栏
    table.on('toolbar(Questiontable)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id)
            ,data = checkStatus.data; //获取选中的数据
        switch(obj.event){
            case 'add': //左上角工具栏添加功能
                //跳转到添加界面
                layer.open({
                    type: 2,
                    offset: 'auto',
                    area: ['1000px', '600px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: '/admin/info/AddQuestionInfo',
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
                        shadeClose: true, //点击遮罩关闭层
                        content: '/questioninfo/'+ data.questionid,
                        btn: ['修改', '取消'],
                        yes: function (index, layero) {
                            var form = layer.getChildFrame('form', index);
                            var data = form.serializeArray();
                            $.ajax({
                                url: "/questioninfo/"+data[0].value,
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
                        // end: function (index) {
                        //     // table.reload('Questiontable')
                        //
                        // }
                    });
                }
                break;
            case 'delete':  //左上角删除功能
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else {
                    layer.confirm('真的删除这'+data.length+'条数据吗？', function(index){
                        var arr = JSON.stringify(data);
                        console.log(arr);
                        $.ajax({
                            url: "/questioninfo/deleteAll",
                            type: "DELETE",
                            dataType: "json",
                            data: {"arr":arr},
                            success: function(result){
                                if(result.code==200){
                                    console.log(result);
                                    layer.msg("删除成功", {time: 2000},function(){
                                        layui.table.reload('Questiontable',{
                                            where:{
                                                type:$("#type").val(),
                                                content:$("#content").val(),
                                            }
                                        })
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