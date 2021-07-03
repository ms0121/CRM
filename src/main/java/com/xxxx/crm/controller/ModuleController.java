package com.xxxx.crm.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * 添加资源
     * @param module
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功!");
    }


    /**
     * 修改资源
     * @param module
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("修改资源成功!");
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除资源成功!");
    }

    /**
     * 跳转至添加资源的页面，
     * 并把要添加资源的资源的层级和父菜单码设置到请求域中，从而在添加页面中进行获取
     * @param grade
     * @param parentId
     * @param request
     * @return
     */
    @GetMapping("toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        request.setAttribute("grade", grade);
        request.setAttribute("parentId", parentId);
        return "module/add";
    }


    /**
     * 打开修改资源的页面
     * @param id
     * @param request
     * @return
     */
    @GetMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer id, HttpServletRequest request, Model model){
        Module module = moduleService.selectByPrimaryKey(id);
        request.setAttribute("module", module);
        return "module/update";
    }

}
