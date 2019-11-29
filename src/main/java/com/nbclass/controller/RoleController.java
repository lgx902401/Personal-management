package com.nbclass.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.model.Permission;
import com.nbclass.model.Role;
import com.nbclass.model.User;
import com.nbclass.service.PermissionService;
import com.nbclass.service.RoleService;
import com.nbclass.shiro.MyShiroRealm;
import com.nbclass.util.CoreConst;
import com.nbclass.util.ResultUtil;
import com.nbclass.vo.PermissionTreeListVo;
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

/**
 * @author superzheng
 * @version V1.0
 * @date 2018年7月11日
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MyShiroRealm myShiroRealm;

    /*角色列表数据*/
    @ApiOperation(value = "角色列表数据", notes = "可按条件(roleId,name)查询角色列表数据,pageNum 默认为 1，pageSize默认为 0")
    @PostMapping("/list")
    @RequiresPermissions("role:list")
    public PageResultVo pageRoles(Role role, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Role> roleList = roleService.selectRoles(role);
            PageInfo<Role> pages = new PageInfo<>(roleList);
            if (null == roleList) {
                return ResultUtil.errorTable("获取用户列表数据失败");
            } else {
                return ResultUtil.successTable("获取用户列表数据成功", roleList, pages.getTotal());
            }
        } catch (Exception e) {
            logger.error(String.format("RoleController.loadRoles%s", e));
            throw e;
        }

    }

    /*新增角色*/
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PostMapping("/add")
    @RequiresPermissions("role:add")
    public ResponseVo addRole(Role role) {
        try {
            int a = roleService.insert(role);
            if (a > 0) {
                return ResultUtil.success("添加角色成功");
            } else {
                return ResultUtil.error("添加角色失败");
            }
        } catch (Exception e) {
            logger.error(String.format("RoleController.addRole%s", e));
            throw e;
        }
    }

    /*删除角色*/
    @ApiOperation(value = "删除角色", notes = "根据roleId删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("role:delete")
    public ResponseVo deleteRole(String roleId) {
        if (roleService.findByRoleId(roleId).size() > 0) {
            return ResultUtil.error("删除失败,该角色下存在用户");
        }
        List<String> roleIdsList = Arrays.asList(roleId);
        int a = roleService.updateStatusBatch(roleIdsList, CoreConst.STATUS_INVALID);
        if (a > 0) {
            return ResultUtil.success("删除角色成功");
        } else {
            return ResultUtil.error("删除角色失败");
        }
    }

    /*批量删除角色*/
    @ApiOperation(value = "批量删除角色", notes = "根据roleIdStr集合批量删除角色 ")
    @PostMapping("/batch/delete")
    @RequiresPermissions("role:batchDelete")
    public ResponseVo batchDeleteRole(String roleIdStr) {
        String[] roleIds = roleIdStr.split(",");
        List<String> roleIdsList = Arrays.asList(roleIds);
        if (roleService.findByRoleIds(roleIdsList).size() > 0) {
            return ResultUtil.error("删除失败,选择的角色下存在用户");
        }
        int a = roleService.updateStatusBatch(roleIdsList, CoreConst.STATUS_INVALID);
        if (a > 0) {
            return ResultUtil.success("删除角色成功");
        } else {
            return ResultUtil.error("删除角色失败");
        }
    }

    /*编辑角色详情查询*/
    @ApiOperation(value = "编辑角色详情", notes = "根据roleid查询角色详情")
    @PostMapping("/detail")
    @RequiresPermissions("role:edit")
    public ResponseVo detail(String roleId) {
        List<Role> roleList = roleService.selectByRoleId(roleId);
        if (null == roleList) {
            return ResultUtil.error("获取用户列表数据失败");
        } else {
            return ResultUtil.success("获取用户列表数据成功", roleList);
        }
    }

    /*编辑角色*/
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @PostMapping("/edit")
    @RequiresPermissions("role:edit")
    public ResponseVo editRole(Role role) {
        int a = roleService.updateByRoleId(role);
        if (a > 0) {
            return ResultUtil.success("编辑角色成功");
        } else {
            return ResultUtil.error("编辑角色失败");
        }
    }

    /*分配权限列表查询*/
    @ApiOperation(value = "分配权限列表查询", notes = "根据roleId分配权限列表查询")
    @PostMapping("/assign/permission/list")
    @RequiresPermissions("permission:list")
    public PageResultVo assignRole(String roleId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PermissionTreeListVo> listVos = new ArrayList<>();
        List<Permission> allPermissions = permissionService.selectAll(CoreConst.STATUS_VALID);
        List<Permission> hasPermissions = roleService.findPermissionsByRoleId(roleId);
        System.out.println(hasPermissions);
        PageInfo<Permission> pages = new PageInfo<>(hasPermissions);
        for (Permission permission : allPermissions) {
            for (Permission hasPermission : hasPermissions) {
                //有权限则选中
                if (hasPermission.getPermissionId().equals(permission.getPermissionId())) {
                    PermissionTreeListVo vo = new PermissionTreeListVo();
                    vo.setId(permission.getId());
                    vo.setPermissionId(permission.getPermissionId());
                    vo.setName(permission.getName());
                    vo.setDescription(permission.getDescription());
                    vo.setUrl(permission.getUrl());
                    vo.setPerms(permission.getPerms());
                    vo.setParentId(permission.getParentId());
                    vo.setType(permission.getType());
                    vo.setOrderNum(permission.getOrderNum());
                    vo.setIcon(permission.getIcon());
                    vo.setStatus(permission.getStatus());
                    vo.setCreateTime(permission.getCreateTime());
                    vo.setUpdateTime(permission.getUpdateTime());
                    listVos.add(vo);
                }
            }
        }
        return ResultUtil.successTable("获取分配权限列表数据成功",listVos,pages.getTotal());
    }
//    public ResponseVo assignRole(String roleId) {
//        List<PermissionTreeListVo> listVos = new ArrayList<>();
//        List<Permission> allPermissions = permissionService.selectAll(CoreConst.STATUS_VALID);
//        List<Permission> hasPermissions = roleService.findPermissionsByRoleId(roleId);
//        for (Permission permission : allPermissions) {
//            PermissionTreeListVo vo = new PermissionTreeListVo();
//            vo.setId(permission.getId());
//            vo.setPermissionId(permission.getPermissionId());
//            vo.setName(permission.getName());
//            vo.setParentId(permission.getParentId());
//            for (Permission hasPermission : hasPermissions) {
//                //有权限则选中
//                if (hasPermission.getPermissionId().equals(permission.getPermissionId())) {
//                    vo.setChecked(true);
//                    break;
//                }
//            }
//            listVos.add(vo);
//        }
//        return ResultUtil.success("分配权限列表查询成功",listVos);
//    }


    /*分配权限*/
    @ApiOperation(value = "分配权限", notes = "根据roleId，permissionIdStr分配权限")
    @PostMapping("/assign/permission")
    @RequiresPermissions("role:assignPerms")
    public ResponseVo assignRole(String roleId, String permissionIdStr) {
        List<String> permissionIdsList = new ArrayList<>();
        if (StringUtils.isNotBlank(permissionIdStr)) {
            String[] permissionIds = permissionIdStr.split(",");
            permissionIdsList = Arrays.asList(permissionIds);
        }
        ResponseVo responseVo = roleService.addAssignPermission(roleId, permissionIdsList);
        /*重新加载角色下所有用户权限*/
        List<User> userList = roleService.findByRoleId(roleId);
        if (userList.size() > 0) {
            List<String> userIds = new ArrayList<>();
            for (User user : userList) {
                userIds.add(user.getUserId());
            }
            myShiroRealm.clearAuthorizationByUserId(userIds);
        }
        return responseVo;
    }

    /*分配权限*/
    @ApiOperation(value = "test分配权限", notes = "根据roleId，permissionIdStr分配权限")
    @PostMapping("/assign/testpermission")
    @RequiresPermissions("role:assignPerms")
    public ResponseVo testAssignRole(String roleId, String permissionIdStr) {
        List<String> permissionIdsList = new ArrayList<>();
        if (StringUtils.isNotBlank(permissionIdStr)) {
            String[] permissionIds = permissionIdStr.split(",");
            permissionIdsList = Arrays.asList(permissionIds);
        }
        ResponseVo responseVo = roleService.testAddAssignPermission(roleId, permissionIdsList);
        /*重新加载角色下所有用户权限*/
        List<User> userList = roleService.findByRoleId(roleId);
        if (userList.size() > 0) {
            List<String> userIds = new ArrayList<>();
            for (User user : userList) {
                userIds.add(user.getUserId());
            }
            myShiroRealm.clearAuthorizationByUserId(userIds);
        }
        return responseVo;
    }

}
