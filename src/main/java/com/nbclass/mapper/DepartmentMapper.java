package com.nbclass.mapper;

import com.nbclass.model.Department;
import com.nbclass.util.MyMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DepartmentMapper extends MyMapper<Department> {


    /**
     * 根据department对象查询部门列表
     * @param department
     * @return
     */
    List<Department> selectDepartments(Department department);

    /**
     * 根据部门id查询部门列表
     * @param departmentId
     * @return
     */
    List<Department> selectByDepartmentId(String departmentId);

    /**
     * 根据参数批量更新状态
     * @param params
     * @return int
     */
    int updateStatusBatch(Map<String, Object> params);

    /**
     * 根据departmentId更新部门信息
     * @param params
     * @return int
     */
    int updateByDepartmentId(Map<String, Object> params);


    /**
     * 根据状态查询全部资部门
     * @param status 状态
     * @return the list
     */
    List<Department> selectAllDepartment(Integer status);

    /**
     * 根据userId查找部门
     * @param userId
     * @return
     */
    List<Department> findByUserId(String userId);

    /**
     * 根据部门id删除部门
     * @param departmentId
     * @return
     */
    int deleteByDepartmentId(String departmentId);

}
