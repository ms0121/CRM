package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Modifier;
import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    /**
     * 查询所有的资源列表
    */
    public List<TreeModel> queryAllModules(Integer roleId);

    /**
     * 查询所有的资源信息
     */
    public List<Module> queryModuleList();

    /**
     * 根据传入的资源等级和模块名称查询资源对象
     * @param grade
     * @param moduleName
     * @return
     */
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    /**
     * 根据传入的资源等级和模块URL查询资源对象
     * @param grade
     * @param url
     * @return
     */
    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade, @Param("url") String url);

    /**
     * 根据权限码查询资源对象
     * @param optValue
     * @return
     */
    Module queryModuleByOptValue(@Param("optValue") String optValue);
}