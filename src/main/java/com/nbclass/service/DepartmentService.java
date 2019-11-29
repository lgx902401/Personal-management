package com.nbclass.service;

import com.nbclass.model.Department;
import com.nbclass.model.User;
import com.nbclass.vo.base.ResponseVo;

import java.util.List;
import java.util.Set;

public interface DepartmentService {
    /**
     * 根据department对象查询部门列表
     * @param department
     * @return
     */
    List<Department> selectDepartments(Department department);

    /**
     * 根据部门id查询部门列表
     *
     * @param departmentId
     * @return
     */
    List<Department> selectByDepartmentId(String departmentId);

    /**
     * 查询全部部门
     * @param status
     * @return list
     */
    List<Department> selectAll(Integer status);

    /**
     * 添加部门
     *
     * @param department
     * @return
     */
    int insert(Department department);

    /**
     * 批量更新状态（删除）
     *
     * @param departmentIds
     * @param status
     * @return int
     */
    int updateStatusBatch(List<String> departmentIds, Integer status);

    /**
     * 根据departmentId更新部门信息
     *
     * @param department
     * @return int
     */
    int updateByDepartmentId(Department department);


    /**
     * 根据用户id分配权限
     *
     * @param userIdStr
     * @param departmentId
     * @return
     */
    ResponseVo addAssignDepartment(List<String> userIdStr,String departmentId);

    /**
     * 根据部门Id删除
     *
     * @param departmentId
     * @return
     */
    int deleteByDepartmentId(String departmentId);


    /**
     * 查询部门id下的用户
     *
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);

    /**
     * 批量查询部门id下的用户
     *
     * @param departmentIds
     * @return
     */
    List<User> findByDepartmentIds(List<String> departmentIds);

    /**
     * 根据userId查找部门
     * @param userId
     * @return
     */
    List<Department> findByUserId(String userId);
}
