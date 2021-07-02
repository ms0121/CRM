package com.xxxx.crm.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-06-30 - 20:37
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的资源列表
     * @return
     */
    @GetMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        // 查询所有的权限记录
        List<TreeModel> treeModels = moduleService.queryAllModules(roleId);
        // 查询指定角色已经被授权过的权限记录id(查询当前角色拥有的资源id)
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        // 遍历treeModels中的哪些权限信息已经被授权
        if (permissionIds != null && permissionIds.size() > 0){
            // 循环所有的资源列表，查看哪些是已经被授权的
            for (TreeModel treeModel : treeModels) {
                if (permissionIds.contains(treeModel.getId())){
                    // 如果已经被授权的资源，将其默认选中
                    treeModel.setChecked(true);
                }
            }
        }
        // 返回查询的所有资源列表
        return treeModels;
    }

    /**
     * 进入授权页面
     * @param roleId
     * @return
     */
    @GetMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        // 将被选中的用户id设置在请求域中，从而传递到前端界面
        request.setAttribute("roleId", roleId);
        return "role/grant";
    }

    /**
     * 查询module的资源列表数据信息
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    /**
     * 跳转至module资源页面
     * @return
     */
    @GetMapping("index")
    public String index(){
        return "module/module";
    }


    /**
     * 参数校验
     *  1.参数校验
     *      模块名称， moduleName
     *          非空，同一层级下的模块名称唯一
     *      地址    url
     *          二级菜单(grade=1),非空并且不可以重复
     *      父级菜单  parentId
     *          一级菜单  目录，grade=0，  null
     *          二级|三级菜单(菜单，按钮grade=1或2)   非空，父级菜单必须存在
     *      层级  grade
     *          非空， 0,1 2，
     *      权限码  optValue
     *          非空，不可重复
     *
     *  2.设置参数的默认值
     *      是否有效  isValid    1
     *      创建时间  createDate   系统当前时间
     *      更新时间  updateDate   系统当前时间
     *
     *  3.执行添加操作，判断受影响的行数
     *
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){

    }



}
