package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

}
