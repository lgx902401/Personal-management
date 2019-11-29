package com.nbclass.service.impl;

import com.nbclass.mapper.DepartmentMapper;
import com.nbclass.mapper.UserDepartmentMapper;
import com.nbclass.mapper.UserMapper;
import com.nbclass.model.Department;
import com.nbclass.model.User;
import com.nbclass.model.UserDepartment;
import com.nbclass.service.DepartmentService;
import com.nbclass.util.CoreConst;
import com.nbclass.util.ResultUtil;
import com.nbclass.util.UUIDUtil;
import com.nbclass.vo.base.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    @Override
    public List<Department> selectDepartments(Department department) {
        return departmentMapper.selectDepartments(department);
    }

    @Override
    public List<Department> selectByDepartmentId(String departmentId) {
        return departmentMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<Department> selectAll(Integer status) {
        return departmentMapper.selectAllDepartment(status);
    }

    @Override
    public int deleteByDepartmentId(String departmentId) {
        return departmentMapper.deleteByDepartmentId(departmentId);
    }

    @Override
    public int updateStatusBatch(List<String> departmentIds, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("departmentIds", departmentIds);
        params.put("status", status);
        return departmentMapper.updateStatusBatch(params);
    }

    @Override
    public int updateByDepartmentId(Department department) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("name", department.getName());
        params.put("description", department.getDescription());
        params.put("department_id", department.getDepartmentId());
        return departmentMapper.updateStatusBatch(params);
    }


    @Override
    public int insert(Department department) {
        Date date = new Date();
        department.setDepartmentId(UUIDUtil.getUniqueIdByUUId());
        department.setStatus(CoreConst.STATUS_VALID);
        department.setCreateTime(date);
        department.setUpdateTime(date);
        return departmentMapper.insert(department);
    }

    @Override
    public List<User> findByDepartmentId(String departmentId) {
        return userMapper.findByDepartmentId(departmentId);
    }

    @Override
    public List<User> findByDepartmentIds(List<String> departmentIds) {
        return userMapper.findByDepartmentIds(departmentIds);
    }

    @Override
    public List<Department> findByUserId(String userId) {
        return departmentMapper.findByUserId(userId);
    }


    @Override
    public ResponseVo addAssignDepartment(List<String> userIdStr, String departmentId) {
        try {
            UserDepartment userDepartment = new UserDepartment();
            userDepartment.setDepartmentId(departmentId);
            userDepartmentMapper.delete(userDepartment);
            for (String userId : userIdStr) {
                userDepartment.setId(null);
                userDepartment.setUserId(userId);
                userDepartmentMapper.insert(userDepartment);
            }
            return ResultUtil.success("分配部门成功");
        }catch (Exception e){
            return ResultUtil.error("分配权限失败");
        }
    }
}
