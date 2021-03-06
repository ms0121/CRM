layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    // 客户列表展示
    var  tableIns = table.render({
        elem: '#customerList',  // 绑定数据表格的id属性值
        url : ctx+'/customer/list',   // 当前表格中的数据来源于该请求的结果数据
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [5,10,15,20,25],
        limit : 5,
        toolbar: "#toolbarDemo",
        id : "customerListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'name', title: '客户名',align:"center"},
            {field: 'fr', title: '法人',  align:'center'},
            {field: 'khno', title: '客户编号', align:'center'},
            {field: 'area', title: '地区', align:'center'},
            {field: 'cusManager', title: '客户经理',  align:'center'},
            {field: 'myd', title: '满意度', align:'center'},
            {field: 'level', title: '客户级别', align:'center'},
            {field: 'xyd', title: '信用度', align:'center'},
            {field: 'address', title: '详细地址', align:'center'},
            {field: 'postCode', title: '邮编', align:'center'},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'webSite', title: '网站', align:'center'},
            {field: 'fax', title: '传真', align:'center'},
            {field: 'zczj', title: '注册资金', align:'center'},
            {field: 'yyzzzch', title: '营业执照', align:'center'},
            {field: 'khyh', title: '开户行', align:'center'},
            {field: 'khzh', title: '开户账号', align:'center'},
            {field: 'gsdjh', title: '国税', align:'center'},
            {field: 'dsdjh', title: '地税', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '更新时间', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });


    /**
     * 当点击搜索按钮的时候进行查询数据信息，给搜索按钮绑定单击事件
     */
    $(".search_btn").click(function () {
        // 将搜索的内容显示在当前数据表中
        tableIns.reload({
            // where填写的是从前端获取对应的文本数据，并把它们传入到后端给对应的参数名的参数赋值
            // （传给的是controller中的参数对象对应的变量）
            where: { //设定异步数据接口的额外参数，任意设置
                customerName: $("input[name='name']").val(),
                customerNo: $("input[name='khno']").val(),
                level: $("#level").val(),
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
    table.on('toolbar(customers)', function(obj){
        // obj.event：对应元素上设置的lay-even属性值
        if (obj.event == "add"){
            // 添加操作
            openCustomersDialog(null);
        }else if (obj.event == "recode"){
            // 删除操作
            console.log("recode");
        }else if (obj.event == "order"){
            // 获取被选中的用户信息列表()， table.checkStatus("elem"); elem当前表单的id号
            var chechStatus = table.checkStatus("customerListTable");
            // 将被选中的表单数据提交到指定的方法中进行处理
            toCustomerOrderPage(chechStatus.data);
        }
    });


    //数据表单的行工具栏监听事件，打开导航工具栏  tool(数据表格的lay-filter=""属性值)
    table.on('tool(customers)', function (data) {
        var id = data.data.id;
        if (data.event == 'edit') {
            // 打开更新营销机会数据的窗口
            openCustomersDialog(id);
        } else if (data.event == 'del'){
            deleteCustomer(id);
        }
    });


    /**
     * 删除指定的客户信息
     * @param id
     */
    function deleteCustomer(id) {
        // 弹出确认框询问用户是否确认删除
        // data：指定后端调用函数之后的返回值
        // console.log("删除按钮被点击了..............");
        layer.confirm('您确认删除该记录吗？',{icon:3, title:"客户管理"}, function (data) {
            // 如果确认删除，则发送ajax请求
            $.post(ctx+ "/customer/delete",{id:id},function (result) {
                // 判断是否成功
                if (result.code == 200) {
                    layer.msg("删除成功！",{icon:6});
                    // 刷新页面
                    window.location.reload();
                } else {
                    layer.msg(result.msg,{icon:5});
                }
            });
        });
    }

    // 打开添加/更新的客户数据的窗口
    // 如果Id为空，表示添加的弹窗
    // 如果Id不为空，表示更新的弹窗
    function openCustomersDialog(id) {
        // 添加数据的标题
        var title = "<h2 align='center'>客户管理--添加客户</h2>";
        // 发送请求到这个地址，从而打开添加的页面信息
        var url = ctx + "/customer/toCustomer";

        // 更新操作
        if (id != null && id != ''){
            // 更新数据的标题
            title = "<h2 align='center'>客户管理--更新客户</h2>";
            // 更新的请求地址，传入id在后端查询对应的数据信息并返回到前台
            url = url + "?id=" + id;
        }

        layui.layer.open({  // 页面内打开
            type: 2, // 类型
            title: title,
            area: ['650px', '500px'], // 宽高
            content: url,   // 跳转的url地址
            maxmin:true,  // 弹窗的最大最小化
        });
    }

    /**
     * 将被选中的表单数据进行处理，查看订单数据信息
     * @param data  被选中的用户数据信息列表
     */
    function toCustomerOrderPage(data) {
        if (data.length == 0){
            layer.msg("请选择要查看的用户!", {icon: 5});
            return;
        }
        if (data.length > 1){
            layer.msg("暂不支持多用户查询订单数据!", {icon: 5});
            return;
        }

        // 向当前的地址发送请求,从而进行跳转页面
        var url = ctx + "/customer/toCustomerOrderPage?id="+data[0].id;
        var title = "<h3 align='center'>客户管理 - 订单信息</h3>";

        // 打开模态框
        layui.layer.open({
            title: title,
            content: url,
            type: 2,
            area: ['680px', '450px'],
            maxmin: true
        });
    }

});
