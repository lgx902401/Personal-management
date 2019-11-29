package com.nbclass.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.mapper.UserDepartmentMapper;
import com.nbclass.mapper.UserRoleMapper;
import com.nbclass.model.Department;
import com.nbclass.model.Role;
import com.nbclass.model.User;
import com.nbclass.service.DepartmentService;
import com.nbclass.service.RoleService;
import com.nbclass.service.UserService;
import com.nbclass.shiro.MyShiroRealm;
import com.nbclass.util.*;
import com.nbclass.vo.ChangePasswordVo;
import com.nbclass.vo.base.PageResultVo;
import com.nbclass.vo.base.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author superzheng
 * @version V1.0
 * @date 2018年7月11日
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MyShiroRealm myShiroRealm;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MyShiroRealm shiroRealm;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserDepartmentMapper userDepartmentMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    /**
     * 用户列表数据
     */
    @ApiOperation(value = "用户列表数据", notes = "可按条件查询用户列表数据(username,phone,email,sex) pageNum 默认为 1，pageSize默认为 0")
    @RequiresPermissions("user:list")
    @PostMapping("/list")
    public PageResultVo loadUsers(User user, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        // startPage(int pageNum, int pageSize)
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.selectUsers(user);
        PageInfo<User> pages = new PageInfo<>(userList);
        if (null == userList) {
            return ResultUtil.errorTable("获取用户列表数据失败");
        } else {
            return ResultUtil.successTable("获取用户列表数据成功", userList, pages.getTotal());

        }
    }

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public ResponseVo add(User userForm, String confirmPassword) {
        String username = userForm.getUsername();
        User user = userService.selectByUsername(username);
        if (null != user) {
            return ResultUtil.error("用户名已存在");
        }
        String password = userForm.getPassword();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致");
            }
        }
        userForm.setUserId(UUIDUtil.getUniqueIdByUUId());
        userForm.setStatus(CoreConst.STATUS_VALID);
        Date date = new Date();
        userForm.setCreateTime(date);
        userForm.setUpdateTime(date);
        userForm.setLastLoginTime(date);
        PasswordHelper.encryptPassword(userForm);
        int num = userService.register(userForm);
        if (num > 0) {
            return ResultUtil.success("添加用户成功");
        } else {
            return ResultUtil.error("添加用户失败");
        }
    }

    /**
     * 用户详情
     */
    @ApiOperation(value = "当前用户详情", notes = "用户详情")
    @RequiresPermissions("user:edit")
    @GetMapping("/detail")
    public ResponseVo userDetail() {
        Map<String, Object> jsonMap = new HashMap<>(2);
        List<User> userList = userService.findByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getUserId());
        Set<String> hasRoles = roleService.findRoleByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getUserId());
        List<Department> hasDepart = departmentService.findByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getUserId());
        jsonMap.put("rows", userList);
        jsonMap.put("hasRoles", hasRoles);
        jsonMap.put("hasDepart", hasDepart);
        if (null == jsonMap) {
            return ResultUtil.error("获取用户详情失败");
        } else {
            return ResultUtil.success("获取用户详情成功", jsonMap);
        }
    }

    /**
     * 根据用户id查用户详情
     */
    @ApiOperation(value = "用户详情", notes = "根据用户userId用户详情")
    @RequiresPermissions("user:edit")
    @PostMapping("/detail")
    public ResponseVo userDetailByUserId(String userId) {
        Map<String, Object> jsonMap = new HashMap<>(2);
        List<User> userList = userService.findByUserId(userId);
        Set<String> hasRoles = roleService.findRoleByUserId(userId);
        List<Department> hasDepart = departmentService.findByUserId(userId);
        jsonMap.put("rows", userList);
        jsonMap.put("hasRoles", hasRoles);
        jsonMap.put("hasDepart", hasDepart);
        if (null == userList) {
            return ResultUtil.error("获取用户详情失败");
        } else {
            return ResultUtil.success("获取用户详情成功", jsonMap);
        }
    }

    /**
     * 编辑用户
     */
    @ApiOperation(value = "编辑用户", notes = "编辑用户")
    @RequiresPermissions("user:edit")
    @PostMapping("/edit")
    public ResponseVo editUser(User userForm) {
        int a = userService.updateByUserId(userForm);
        if (a > 0) {
            return ResultUtil.success("编辑用户成功！");
        } else {
            return ResultUtil.error("编辑用户失败");
        }
    }

    /**
     * 删除用户
     */
    @ApiOperation(value = "删除用户", notes = "根据userId删除用户")
    @RequiresPermissions("user:delete")
    @PostMapping("/delete")
    public ResponseVo deleteUser(String userId) {
        List<String> userIdsList = Arrays.asList(userId);
        int a = userService.updateStatusBatch(userIdsList, CoreConst.STATUS_INVALID);
        userDepartmentMapper.deleteByUserId(userId);
        userRoleMapper.deleteByUserId(userId);
        if (a > 0) {
            return ResultUtil.success("删除用户成功");
        } else {
            return ResultUtil.error("删除用户失败");
        }
    }

    /**
     * 批量删除用户
     */
    @ApiOperation(value = "批量删除用户", notes = "根据userIdStr集合批量删除用户")
    @RequiresPermissions("user:batchDelete")
    @GetMapping("/batch/delete")
    public ResponseVo batchDeleteUser(String userIdStr) {
        String[] userIds = userIdStr.split(",");
        List<String> userIdsList = Arrays.asList(userIds);
        int a = userService.updateStatusBatch(userIdsList, CoreConst.STATUS_INVALID);
        if (a > 0) {
            return ResultUtil.success("删除用户成功");
        } else {
            return ResultUtil.error("删除用户失败");
        }
    }

    /**
     * 分配角色列表查询
     */
    @ApiOperation(value = "分配角色列表查询", notes = "根据userId分配角色列表查询")
    @RequiresPermissions("user:assignRole")
    @PostMapping("/assign/role/list")
    public ResponseVo assignRoleList(String userId) {
        List<Role> roleList = roleService.selectRoles(new Role());
        Set<String> hasRoles = roleService.findRoleByUserId(userId);
        Map<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("rows", roleList);
        jsonMap.put("hasRoles", hasRoles);
        return ResultUtil.success("分配角色列表查询成功", jsonMap);
    }

    /**
     * 保存分配角色
     */
    @ApiOperation(value = "保存分配角色", notes = "根据userId，roleIdStr保存分配角色")
    @RequiresPermissions("user:assignRole")
    @PostMapping("/assign/role")
    public ResponseVo assignRole(String userId, String roleIdStr) {
        String[] roleIds = roleIdStr.split(",");
        List<String> roleIdsList = Arrays.asList(roleIds);
        ResponseVo responseVo = userService.addAssignRole(userId, roleIdsList);
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        myShiroRealm.clearAuthorizationByUserId(userIds);
        return responseVo;
    }

    /**
     * 管理员角色修改用户密码
     */
    @ApiOperation(value = "管理员角色修改密码", notes = "根据userId修改密码")
    @RequiresPermissions("user:assignRole")
    @PostMapping(value = "/adminChangePassword")
    public ResponseVo adminChangePassword(String userId, String newPassword) {
        User formUser = userService.selectByUserId(userId);
        User newUser = CopyUtil.getCopy(formUser, User.class);
        newUser.setPassword(newPassword);
        PasswordHelper.encryptPassword(newUser);
        int a = userService.updateUserByPrimaryKey(newUser);
        if (a > 0) {
            return ResultUtil.success("修改密码成功");
        } else {
            return ResultUtil.success("修改失败成功");
        }

    }

    /**
     * 用户修改密码
     * @param changePasswordVo
     * @return
     */
    /*用户修改密码*/
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码")
    @RequiresPermissions("user:assignRole")
    @PostMapping(value = "/changePassword")
    public ResponseVo changePassword(ChangePasswordVo changePasswordVo) {
        if (!changePasswordVo.getNewPassword().equals(changePasswordVo.getConfirmNewPassword())) {
            return ResultUtil.error("两次密码输入不一致");
        }
        User loginUser = userService.selectByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getUserId());
        User newUser = CopyUtil.getCopy(loginUser, User.class);
        String sysOldPassword = loginUser.getPassword();
        newUser.setPassword(changePasswordVo.getOldPassword());
        String entryOldPassword = PasswordHelper.getPassword(newUser);
        if (sysOldPassword.equals(entryOldPassword)) {
            newUser.setPassword(changePasswordVo.getNewPassword());
            PasswordHelper.encryptPassword(newUser);
            userService.updateUserByPrimaryKey(newUser);
            //*清除登录缓存*//
            List<String> userIds = new ArrayList<>();
            userIds.add(loginUser.getUserId());
            shiroRealm.removeCachedAuthenticationInfo(userIds);
            /*SecurityUtils.getSubject().logout();*/
        } else {
            return ResultUtil.error("您输入的旧密码有误");
        }
        return ResultUtil.success("修改密码成功");
    }

}
