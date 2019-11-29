package com.nbclass.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.mapper.UserDepartmentMapper;
import com.nbclass.model.Department;
import com.nbclass.model.Permission;
import com.nbclass.model.User;
import com.nbclass.service.DepartmentService;
import com.nbclass.service.UserService;
import com.nbclass.util.CoreConst;
import com.nbclass.util.ResultUtil;
import com.nbclass.vo.DepartmentTreeListVo;
import com.nbclass.vo.base.PageResultVo;
import com.nbclass.vo.base.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(tags = "部门管理接口")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    /**
     * 部门列表数据
     *
     * @param department
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "部门列表数据", notes = "可按条件(departmentId,name,description)查询部门列表数据")
    @PostMapping("/list")
    @RequiresPermissions("department:list")
    public PageResultVo pageDepartments(Department department, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Department> departmentList = departmentService.selectDepartments(department);
        PageInfo<Department> pages = new PageInfo<>(departmentList);
        if (null == departmentList) {
            return ResultUtil.errorTable("获取用户列表数据失败");
        } else {
            return ResultUtil.successTable("获取用户列表数据成功", departmentList, pages.getTotal());
        }
    }

    /**
     * 编辑部门详情查询
     *
     * @param departmentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "编辑部门详情查询", notes = "根据departmentId查询部门详情")
    @PostMapping("/detail")
    @RequiresPermissions("department:edit")
    public PageResultVo detail(String departmentId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Department> departmentList = departmentService.selectByDepartmentId(departmentId);
        PageInfo<Department> pages = new PageInfo<>(departmentList);
        if (null == departmentList) {
            return ResultUtil.errorTable("获取部门列表数据失败");
        } else {
            return ResultUtil.successTable("获取部门列表数据成功", departmentList, pages.getTotal());
        }
    }

    /**
     * 编辑部门详情
     *
     * @param department
     * @return
     */
    @ApiOperation(value = "编辑部门详情", notes = "编辑部门详情")
    @PostMapping("/edit")
    @RequiresPermissions("department:edit")
    public ResponseVo editDepartment(Department department) {
        int a = departmentService.updateByDepartmentId(department);
        if (a > 0) {
            return ResultUtil.success("编辑部门详情成功");
        } else {
            return ResultUtil.error("编辑部门详情失败");
        }
    }

    /**
     * 新增部门
     *
     * @param department
     * @return
     */
    @ApiOperation(value = "新增部门", notes = "新增部门(name,description,,parentId)")
    @PostMapping("/add")
    @RequiresPermissions("department:add")
    public ResponseVo addDepartment(Department department) {
        try {
            int a = departmentService.insert(department);
            if (a > 0) {
                return ResultUtil.success("添加部门成功");
            } else {
                return ResultUtil.error("添加部门失败");
            }
        } catch (Exception e) {
            logger.error(String.format("DepartmentController.addDepartment%s", e));
            throw e;
        }
    }

    /**
     * 删除部门
     *
     * @param departmentId
     * @return
     */
    @ApiOperation(value = "删除部门", notes = "根据departmentId删除部门")
    @PostMapping("/delete")
    @RequiresPermissions("department:delete")
    public ResponseVo deleteDepartment(String departmentId) {
        if (departmentService.findByDepartmentId(departmentId).size() > 0) {
            return ResultUtil.error("部门删除失败，该部门下存在用户");
        }
        int a = departmentService.deleteByDepartmentId(departmentId);
        if (a > 0) {
            return ResultUtil.success("删除部门成功");
        } else {
            return ResultUtil.error("删除部门失败");
        }
    }

    /**
     * 分配部门
     * @param userIdStr
     * @param departmentId
     * @return
     */
    @ApiOperation(value = "分配部门", notes = "根据userId，departmentIdStr分配部门")
    @PostMapping("/assign/department")
    @RequiresPermissions("department:assignDepartment")
    public ResponseVo assignDepartment(String userIdStr, String departmentId) {
        List<String> userIdStrList = new ArrayList<>();
        if (StringUtils.isNotBlank(userIdStr)) {
            String[] departmentIds = userIdStr.split(",");
            userIdStrList = Arrays.asList(departmentIds);
        }
        ResponseVo responseVo = departmentService.addAssignDepartment(userIdStrList,departmentId);
        return responseVo;
    }

    /**
     * 根据部门id查询用户信息
     * @param departmentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据部门id查询用户信息", notes = "根据departmentId查询用户")
    @PostMapping("/assign/department/list")
    @RequiresPermissions("department:list")
    public PageResultVo assignDepart(String departmentId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.selectByDepartmentId(departmentId);
        PageInfo<User> pages = new PageInfo<>(userList);
        if (userList==null){
            return ResultUtil.errorTable("根据部门id查询用户信息失败");
        }
        return ResultUtil.successTable("根据部门id查询用户信息成功", userList, pages.getTotal());
    }
//    public PageResultVo assignDepart(String userId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
//        List<DepartmentTreeListVo> listVos = new ArrayList<>();
//        List<Department> allDepartments = departmentService.selectAll(CoreConst.STATUS_VALID);
//        List<Department> hasDepartments = departmentService.findByUserId(userId);
//        PageInfo<Department> pages = new PageInfo<>(hasDepartments);
//        for (Department department : allDepartments) {
//            for (Department hasDepartment : hasDepartments) {
//                if (hasDepartment.getDepartmentId().equals(department.getDepartmentId())) {
//                    DepartmentTreeListVo vo = new DepartmentTreeListVo();
//                    vo.setId(department.getId());
//                    vo.setDepartmentId(department.getDepartmentId());
//                    vo.setName(department.getName());
//                    vo.setDescription(department.getDescription());
//                    vo.setStatus(department.getStatus());
//                    vo.setParentId(department.getParentId());
//                    vo.setCreateTime(department.getCreateTime());
//                    vo.setUpdateTime(department.getUpdateTime());
//                    listVos.add(vo);
//                }
//            }
//        }
//        return ResultUtil.table("获取部门列表数据成功", listVos, pages.getTotal());
//    }
}