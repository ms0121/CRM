package com.xxxx.crm.service;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-06-30 - 20:36
 */

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleMapper.queryAllModules(roleId);
    }

    /**
     * 当前查询的数据信息要显示在前端layUi的数据表格中，LayUi数据表格要求的数据形式是Map(string,object)，
     * 所以返回的数据格式要符合前端的数据形式
     * @return
     */
    public Map<String, Object> queryModuleList(){
        Map<String, Object> map = new HashMap<>();
        List<Module> moduleList = moduleMapper.queryModuleList();
        map.put("code", 0);
        map.put("msg","");
        map.put("count", moduleList.size());
        map.put("data",moduleList);
        return map;
    }

    /**
     * 资源添加操作
     * 参数校验
     *  1.参数校验
     *      模块名称， moduleName
     *          非空，同一层级下的模块名称唯一
     *      地址    url
     *          二级菜单(grade=1),非空并且同一层级下不可以重复
     *      父级菜单  parentId
     *          一级菜单  目录，grade=0，  -1
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
        /*1.参数校验*/
        // 层级grade 非空，0 | 1 | 2
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade == 0 || grade==1 || grade == 2), "菜单层级不合法!");
        // 模块名称， moduleName
        // 非空，同一层级下的模块名称唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空!");
        // 同一层级下的模块名称唯一
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()), "该层级下模块名称已存在!");

        // 地址     url  二级菜单(grade=1),非空并且同一层级下不可以重复
        if (grade == 1){
            // url  二级菜单(grade=1), 非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "URL不能为空!");
            // url  二级菜单(grade=1), 同一层级下url不可以重复
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl()), "该层级下模块URL已存在!");
        }

        // 父级菜单  parentId    一级菜单  目录，grade=0， -1
        if (grade == 0){
            module.setParentId(-1);
        }

        // 父级菜单  parentId    二级|三级菜单(菜单，按钮grade=1或2)   非空，父级菜单必须存在
        if (grade != 0){
            // 非空
            AssertUtil.isTrue(module.getParentId() == null, "父级菜单不能为空!");
            // 父级菜单必须存在
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()),"父级菜单必须存在！");
        }

        // 权限码  optValue
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "权限码不能为空!");
        // 非空，不可重复
        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptValue(module.getOptValue()), "权限码已存在!");

        // 2.设置参数的默认值
        // 是否有效  isValid    1
        module.setIsValid((byte) 1);
        // 创建时间  createDate   系统当前时间
        module.setCreateDate(new Date());
        // 更新时间  updateDate   系统当前时间
        module.setUpdateDate(new Date());

        // 3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(moduleMapper.insertSelective(module) < 1, "资源添加失败！");
    }


    /**
     * 资源修改操作
     *  1.参数校验
     *      id      非空，数据存在
     *      grade   层级，非空，0-1-2
     *      模块名称   moduleName
     *          非空，同一层级下的模块名称唯一(不包含当前修改记录本身)
     *      地址  url
     *          二级菜单，grade = 1,非空，并且同一层级下不可重复(不包含修改记录本身)
     *      权限码  optValue  非空，不可重复(不包含修改记录本身)
     *
     *  2.设置默认值
     *      设置修改时间为当前的系统时间
     *  3，执行修改操作
     * @param module
     */
    public void updateModule(Module module){
        // 1.参数校验
        // id 不为空
        AssertUtil.isTrue(null == module.getId(), "资源不存在!");
        // id 数据存在
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(temp == null, "资源不存在");

    }


}
