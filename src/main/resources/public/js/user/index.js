layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 所获得的 tableIns 即为当前容器的实例
     * 数据表格
     */
    var tableIns = table.render({
        id:"userId"
        // 容器table元素的id属性值
        ,elem: '#userList' // 绑定对应的数据表格
        ,height: 'full-125' // 容器的高度
        ,cellMinWidth: 95  // 单元格的最小宽度
        ,url: ctx + '/user/list' //请求的地址（后台的数据接口）
        ,limit:5  // 默认每页显示的数据条数
        ,limits:[5,10,20,30,40,50]  // 每页页数的可选择项
        ,page: true //开启分页
        ,toolbar:"#toolbarDemo"  // 开启头部工具栏选项的按钮
        ,cols: [[ //表头
            /**
             * field：要求field属性值与后端返回的数据中对应的属性字段名一致
             * title:设置列的标题
             * sort:是否允许排序(默认是false)
             * fixed：固定列，相当于固定单元格的操作
             */
            {type: 'checkbox', fixed: 'center'}    // 复选框
            ,{field: 'id', title: '编号', sort: true, fixed: 'left'}
            ,{field: 'userName', title: '用户名', align:'center'}
            ,{field: 'email', title: '客户邮箱', align:'center'}
            ,{field: 'trueName', title: '真实姓名', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '更新时间', align:'center'}
            // 开启行工具栏
            ,{title: '操作', templet: '#userListBar', fixed: 'right', align: 'center', minWidth:150}
        ]]
    });

    /**
     * 当点击搜索按钮的时候进行查询数据信息，给搜索按钮绑定单击事件
     */
    $(".search_btn").click(function () {
        //这里以搜索为例
        tableIns.reload({
            // where填写的是从前端获取对应的文本数据，并把它们传入到后端给对应的参数名的参数赋值
            where: { //设定异步数据接口的额外参数，任意设置
                userName: $("input[name='userName']").val()
                ,email: $("input[name='email']").val()
                ,phone: $("input[name='phone']").val()
            }
            ,page: {
                curr: 1 //响应之后，设置重新从第 1 页开始显示数据
            }
        });
    });


    /**
     * 监听表单数据的头部工具栏事件: toolbar()
     * 触发事件,数据表格的头部工具栏的增加删除按钮事件绑定
     * 语法格式
     table.on('toolbar(数据表格的lay-filter=""属性值), function(obj){

     })
     */
    table.on('toolbar(users)', function(obj){
        // console.log(obj);
        // obj.event：对应元素上设置的lay-even属性值
        if (obj.event == "add"){
            // 添加操作
            openAddOrUpdateUserDialog(null);
        }else if (obj.event == "del"){
            // 删除操作
            // 获取被选中的用户信息列表， table.checkStatus("elem"); elem当前表单的id号
            var checkStatus = table.checkStatus("userId");
            // console.log(checkStatus);
            // 将被选中的id列表进行判断确认删除
            deleteDialog(checkStatus.data);
        }
    });

    /**
     * 头部工具栏的删除按钮的操作：
     * @param checkData
     */
    function deleteDialog(checkData) {
        if (checkData.length == 0){
            layer.msg("请选择要删除的用户信息?", {icon: 5});
            return;
        }

        // 弹出确认信息对话框
        layer.confirm("你确认要删除吗?", {icon:3}, function (index) {
            // 关闭确认框
            layer.close(index);
            // 拼接要删除的用户id
            var id = "id=";
            for (var i = 0; i<checkData.length; i++){
                if (i < checkData.length - 1){
                    id = id + checkData[i].id + "&id=";
                }else {
                    id = id + checkData[i].id;
                }
                // 发送ajax请求，执行删除用户管理的操作
                $.ajax({
                    type:"post",
                    data:id,
                    url:ctx + "/user/delete",
                    success:function (result) {
                        showInfo(result);
                    }
                })
            }
        })
    }


    /**
     * 添加按钮
     */
    function openAddOrUpdateUserDialog(id) {
        // 添加数据的标题
        var title = "<h2 align='center'>用户管理--添加用户</h2>";
        var url = ctx + "/user/toAddorUpdateUserPage";   // 点击头部工具栏按钮，请求发送到这个地址

        if (id != null){
            title = "<h2 align='center'>用户管理--更新用户</h2>";
            url +="?id=" + id;
        }

        layui.layer.open({  // 页面内打开
            type: 2, // 类型
            title: title,   // 标题
            area: ['450px', '520px'], // 宽高
            content: url,   // 跳转的url地址
            maxmin:true,  // 弹窗的最大最小化
        });
    }


    /**
     *  数据表单的行工具栏监听事件，打开导航工具栏：tool(数据表格的lay-filter=""属性值)
     */
    table.on('tool(users)', function(data) {
        // console.log();
        if (data.event == "edit"){
            openAddOrUpdateUserDialog(data.data.id);
        }else if (data.event == "del"){
            console.log(data.data.id);
            deleteUserDiag(data.data.id);
        }
    });

    /**
     * 删除指定的用户信息
     */
    function deleteUserDiag(id) {
        layer.confirm('您确定要删除该记录吗?', {icon:3, title:'用户管理'}, function (index) {
            // 关闭弹出框
            layer.close(index);
            // 执行操作
            $.ajax({
                type:"post",
                data:{
                    id:id
                },
                url:ctx + "/user/delete",
                success: function (data) {
                    showInfo(data)
                }
            });
        });
    }

    /**
     * 发送请求之后的提示信息
     * @param result
     */
    function showInfo(result) {
        if (result.code == 200){
            layer.msg("删除成功", {icon: 6});
            // 刷新表格
            tableIns.reload();
        }else {
            // 提示失败
            layer.msg(result.msg, {icon:5});
        }
    }

});