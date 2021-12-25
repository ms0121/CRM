layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;

    // 渲染表格
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx+'/module/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose:true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if(d.grade==1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });


    /**
     * 监听头部工具栏
     */
    table.on('toolbar(munu-table)', function (data) {
        // 判断lay-event属性
        if (data.event == "expand") {
            // 数据表格全部展开，将当前的表格传入到函数中
            treeTable.expandAll("#munu-table");

        } else if (data.event == "fold") {
            // 全部折叠
            treeTable.foldAll("#munu-table");

        } else if (data.event == "add") {
            // 添加目录 层级=0 父菜单=-1
            openAddModuleDialog(0, -1)
        }
    });

    /**
     * 监听行工具栏
     * 点击行工具栏之后，会触发点击事件函数，返回数据信息（用obj接收返回的数据信息）
     */
    table.on('tool(munu-table)',function (obj) {
        console.log(obj);
        // 判断lay-event属性
        if (obj.event == "add") {
            // 添加子项
            // 判断当前的层级（如果是三级菜单，就不能添加）
            if (obj.data.grade == 2) {
                layer.msg("暂不支持添加四级菜单！",{icon:5});
                return;
            }
            // 一级|二级菜单   grade=当前层级+1，parentId=当前资源的ID
            openAddModuleDialog(obj.data.grade+1, obj.data.id);

        } else if (obj.event == "edit") {
            // 修改资源
            openUpdateModuleDialog(obj.data.id);

        } else if (obj.event == "del") {
            // 删除资源
            deleteModule(obj.data.id);
        }
    });


    /**
     * 打开添加资源的对话框，并将层级和父菜单的id传入到添加页面中
     * @param grade 层级
     * @param parentId 父菜单ID
     */
    function openAddModuleDialog(grade, parentId) {
        var title = "<h3 align='center'>资源管理 - 添加资源</h3>";
        var url = ctx + "/module/toAddModulePage?grade=" + grade + "&parentId=" + parentId;
        // 控制打开的页面的大小，以及设置打开的页面的标题信息
        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        });
    }


    /**
     * 打开修改资源的对话框
     * @param id
     */
    function openUpdateModuleDialog(id) {
        var title = "<h3>资源管理 - 修改资源</h3>";
        var url = ctx + "/module/toUpdateModulePage?id=" + id;

        layui.layer.open({
            type:2,
            title:title,
            content:url,
            area:["500px","450px"],
            maxmin:true
        });
    }


    /**
     * 删除资源
     * @param id
     */
    function deleteModule(id) {
        // 弹出确认框询问用户是否确认删除
        // data：指定后端调用函数之后的返回值
        console.log("删除按钮被点击了..............");
        layer.confirm('您确认删除该记录吗？',{icon:3, title:"资源管理"}, function (data) {
            // 如果确认删除，则发送ajax请求
            $.post(ctx+ "/module/delete",{id:id},function (result) {
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
});