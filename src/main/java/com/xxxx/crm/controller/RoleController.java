package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-06-14 - 16:17
 */

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的角色
     * @param userId
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        List<Map<String, Object>> maps = roleService.queryAllRoles(userId);
        System.out.println(maps);
        return maps;
    }

    @GetMapping("index")
    public String index(){
        return "role/role";
    }


    /**
     * 根据用户输入的字段进行查询相应的角色信息
     * @param roleQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(RoleQuery roleQuery){
        Map<String, Object> map = roleService.queryByParamsForTable(roleQuery);
        return map;
    }

    /**
     * 角色添加操作
     * @param role
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功!");
    }

    /**
     * 角色更新操作
     * @param role
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功!");
    }

    /**
     * 角色删除操作
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功!");
    }

    /**
     * 跳转至添加或修改角色的页面
     * @return
     */
    @GetMapping("toAddorUpdateRolePage")
    public String toAddorUpdateRolePage(Integer id, HttpServletRequest request){
        // 判断是否是更新操作
        if (id != null){
            // 查询操作
            Role role = roleService.selectByPrimaryKey(id);
            // 将查询的用户设置到请求域中
            request.setAttribute("role", role);
        }
        return "role/add_update";
    }


    /**
     * 角色授权
     * @param roleId 被授权的用户id
     * @param mIds 因为传过来的角色资源(mIds)有多个，所以传过来的是一个数组
     * @return
     */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId, Integer mIds[]){
//        System.out.println("roleId = " + roleId);
//        System.out.println("mIds = " + Arrays.asList(mIds));
        roleService.addGrant(roleId, mIds);
        return success("授权成功!");
    }

}
