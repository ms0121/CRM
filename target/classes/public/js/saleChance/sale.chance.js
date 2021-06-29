layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;



    //所获得的 tableIns 即为当前容器的实例
    //数据表格
    var tableIns = table.render({
        id:"saleChanceId"
        // 容器table元素的id属性值
        ,elem: '#saleChanceList' // 绑定对应的数据表格
        ,height: 'full-125' // 容器的高度
        ,cellMinWidth: 95  // 单元格的最小宽度
        ,url: ctx + '/sale_chance/list' //请求的地址（后台的数据接口）
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
            {type: 'checkbox', fixed: 'center'}
            ,{field: 'id', title: '编号', sort: true, fixed: 'left'}
            ,{field: 'chanceSource', title: '机会来源', align:'center'}
            ,{field: 'customerName', title: '客户名', align:'center'}
            ,{field: 'cgjl', title: '成功几率', align:'center'}
            ,{field: 'overview', title: '概要', align:'center'}
            ,{field: 'linkMan', title: '联系人', align:'center'}
            ,{field: 'linkPhone', title: '联系号码', align:'center'}
            ,{field: 'description', title: '描述', align:'center'}
            ,{field: 'createMan', title: '创建人', align:'center'}
            ,{field: 'uname', title: '分配人', align:'center'}
            ,{field: 'assignTime', title: '分配时间', align:'center'}
            ,{field: 'state', title: '分配状态', align:'center', templet: function (d) {
                // d代表的是当前的页面返回的5条详细的用户信息（默认设置每页显示5条数据），return返回值就是在单元格显示
                // 的内容，也可以指定样式
                // console.log(d);
                // return "已分发";
                // return "<font color='red'>已分发</font>"
                // 调用函数实现功能
                return formatState(d.state);
            }}
            ,{field: 'devResult', title: '开发状态', align:'center', templet: function (d) {
                return formatDevResult(d.devResult);
            }}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '更新时间', align:'center'}
            ,{title: '操作', templet: '#saleChanceListBar', fixed: 'right', align: 'center', minWidth:150}
        ]]
    });


    /**
     * 判断当前的状态
     * 0：未分配
     * 1：已分配
     * 其他:未知
     */
    function formatState(state) {
        if (state == 0){
            return "<div style='color: grey'>未分配</div>";
        }else if (state == 1){
            return "<div style='color: green'>已分配</div>";
        }else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 判断当前的开发状态
     * 0:未开发
     * 1：开发中
     * 2：开发成功
     * 3：开发失败
     * 其他：未知
     */
    function formatDevResult(devResult) {
        if (devResult == 0){
            return "<div style='color: grey'>未开发</div>";
        }else if (devResult == 1){
            return "<div style='color: orange'>开发中</div>";
        }else if (devResult == 2){
            return "<div style='color: green'>开发成功</div>";
        }else if (devResult == 3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: blue'>未知</div>";
        }
    }


    // 当点击搜索按钮的时候进行查询数据信息，给搜索按钮绑定单击事件
        $(".search_btn").click(function () {
        //这里以搜索为例
        tableIns.reload({
            // where填写的是从前端获取对应的文本数据，并把它们传入到后端给对应的参数名的参数赋值
            where: { //设定异步数据接口的额外参数，任意设置
                customerName: $("input[name='customerName']").val()
                ,createMan: $("input[name='createMan']").val()
                ,state: $("#state").val()
            }
            ,page: {
                curr: 1 //响应之后，设置重新从第 1 页开始显示数据
            }
        });
    });


    /**监听表单数据的头部工具栏事件
     * 语法格式
     table.on('toolbar(表单数据的lay-filter=""属性值), function(obj){

     })
     */
    //触发事件,数据表格的头部工具栏的增加删除按钮事件绑定
    table.on('toolbar(saleChances)', function(obj){
        // obj.event：对应元素上设置的lay-even属性值
        if (obj.event == "add"){
            // 添加操作
            openSaleChanceDialog(null);
        }else if (obj.event == "del"){
            // 删除操作
            deleteSaleChance(obj);
        }
    });


    /**
     * 执行营销机会数据删除操作(多条数据删除)
     * @param data
     */
    function deleteSaleChance(data) {
        //  // 获取选中行的数据
        var checkStatus = table.checkStatus("saleChanceId");
        // console.log(checkStatus.data);
        // alert(checkStatus.data.length);
        var saleChanceData = checkStatus.data;
        if (saleChanceData.length < 1){
            layer.msg("请选择要删除的数据!", {icon:5});
            return;
        }
        // 询问用户是否删除数据
        layer.confirm('您确定要删除选中的记录吗?', {icon:3, title:'营销机会管理'}, function (index) {
            // 关闭弹窗
            layer.close(index);
            // 传递的参数是数组 ids=1&ids=2.....
            var ids = "ids=";
            // 循环选中行记录的数据信息
            for (var i = 0; i <saleChanceData.length; i++) {
                if (i < saleChanceData.length - 1){
                    // 拼接批量删除数据的id号
                    ids = ids + saleChanceData[i].id + "&ids=";
                }else {
                    ids = ids + saleChanceData[i].id;
                }
                // console.log(ids);
                // 发送ajax请求，执行删除营销机会的操作
                $.ajax({
                    type:"post",
                    data:ids,
                    url:ctx + "/sale_chance/delete",
                    success:function (result) {
                        showInfo(result);
                    }
                })
            }
        })
    }



    // 打开添加/更新的营销机会数据的窗口
    // 如果saleChanceId为空，表示添加的弹窗
    // 如果saleChanceId不为空，表示更新的弹窗
    function openSaleChanceDialog(saleChanceId) {
        // 添加数据的标题
        var title = "<h2 align='center'>营销机会管理--添加营销机会</h2>";
        var url = ctx + "/sale_chance/toSaleChance";

        // 更新操作
        if (saleChanceId != null && saleChanceId != ''){
            // 更新数据的标题
            title = "<h2 align='center'>营销机会管理--更新营销机会</h2>";
            // 更新的请求地址，传入id在后端查询对应的数据信息并返回到前台
            url = url + "?id=" + saleChanceId;
        }

        layui.layer.open({  // 页面内打开
            type: 2, // 类型
            title: title,
            area: ['450px', '520px'], // 宽高
            content: url,   // 跳转的url地址
            maxmin:true,  // 弹窗的最大最小化
        });
    }


    //数据表单的行工具栏监听事件，打开导航工具栏  tool(数据表格的lay-filter=""属性值)
    table.on('tool(saleChances)', function(data) {
        // console.log(data);
        if (data.event == 'edit'){
            // alert("编辑");
            // 更新操作需要将对应的id传入
            var saleChanceId = data.data.id;
            // 打开更新营销机会数据的窗口
            openSaleChanceDialog(saleChanceId);

        }else if (data.event == 'del'){
            // alert('删除');
            // 弹出确认框按钮，询问用户是否确认删除
            // 参数1弹窗提示信息，参数2：按钮设置, function执行的函数，index当前弹窗页面
            layer.confirm('确认要删除用户: ' + data.data.customerName + ' 吗?', {icon:3, title:"营销机会管理"}, function (index) {
                // 关闭确认框
                layer.close(index);
                // 发送ajax请求实现删除的操作
                $.ajax({
                    types:"post",
                    url: ctx + "/sale_chance/delete",
                    data:{
                        ids: data.data.id,
                    },
                    success:function (result) { // result调用函数返回的resultInfo数据信息
                        showInfo(result);
                    }
                });
            });
        }
    });


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
