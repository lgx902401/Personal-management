package com.nbclass.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.model.Permission;
import com.nbclass.model.Role;
import com.nbclass.service.PermissionService;
import com.nbclass.shiro.ShiroService;
import com.nbclass.util.CoreConst;
import com.nbclass.util.ResultUtil;
import com.nbclass.vo.base.PageResultVo;
import com.nbclass.vo.base.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superzheng
 * @version V1.0
 * @date 2018年7月11日
 */
@Api(tags = "权限接口")
@RestController
@RequestMapping("/permission")
public class PermissionController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    /**
     * 1:全部资源，2：菜单资源
     */
    private static final String[] MENU_FLAG = {"1", "2"};
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ShiroService shiroService;


    /*权限列表数据*/
    @ApiOperation(value = "权限列表数据", notes = "根据条件(permissionId,name,description,url,perms)权限列表数据 flag=1:全部资源，flag=2：菜单资源")
    @PostMapping("/list")
    //这里代表的时要走这个方法模块中就得有角色管理这个模块，没有就拒绝访问
    @RequiresPermissions("permission:list")
    public PageResultVo loadPermissions(Permission permission,@RequestParam(defaultValue = "1") String flag,
                                        @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        if (StringUtils.isBlank(flag) || MENU_FLAG[0].equals(flag)) {
            PageHelper.startPage(pageNum, pageSize);
            List<Permission>  permissionListList = permissionService.findAllPerms(permission);
            PageInfo<Permission> pages = new PageInfo<>(permissionListList);
            return ResultUtil.successTable("获取全部资源成功",permissionListList,pages.getTotal());
        } else if (MENU_FLAG[1].equals(flag)) {
            PageHelper.startPage(pageNum, pageSize);
            List<Permission>  permissionListList = permissionService.selectAllMenuName(CoreConst.STATUS_VALID);
            PageInfo<Permission> pages = new PageInfo<>(permissionListList);
            return ResultUtil.successTable("获取菜单资源成功",permissionListList,pages.getTotal());
        }
        return ResultUtil.errorTable("获取资源失败");
    }

    /*添加权限*/
    @ApiOperation(value = "添加权限", notes = "添加权限")
    @PostMapping("/add")
    @RequiresPermissions("permission:add")
    public ResponseVo addPermission(Permission permission) {
        try {
            int a = permissionService.insert(permission);
            if (a > 0) {
                shiroService.updatePermission();
                return ResultUtil.success("添加权限成功");
            } else {
                return ResultUtil.error("添加权限失败");
            }
        } catch (Exception e) {
            logger.error(String.format("PermissionController.addPermission%s", e));
            throw e;
        }
    }

    /*删除权限*/
    @ApiOperation(value = "删除权限", notes = "根据permissionId删除权限")
    @PostMapping("/delete")
    @RequiresPermissions("permission:delete")
    public ResponseVo deletePermission(String permissionId) {
        try {
            int subPermsByPermissionIdCount = permissionService.selectSubPermsByPermissionId(permissionId);
            if (subPermsByPermissionIdCount > 0) {
                return ResultUtil.error("改资源存在下级资源，无法删除！");
            }
            int a = permissionService.updateStatus(permissionId, CoreConst.STATUS_INVALID);
            if (a > 0) {
                shiroService.updatePermission();
                return ResultUtil.success("删除权限成功");
            } else {
                return ResultUtil.error("删除权限失败");
            }
        } catch (Exception e) {
            logger.error(String.format("PermissionController.deletePermission%s", e));
            throw e;
        }
    }

    /*权限详情*/
    @ApiOperation(value = "权限详情", notes = "根据permissionId查询权限详情 ")
    @RequiresPermissions("permission:edit")
    @PostMapping("/detail")
    public PageResultVo detail(String permissionId,@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Permission> permissionList = permissionService.findByPermissionId(permissionId);
        PageInfo<Permission> pages = new PageInfo<>(permissionList);
        if (null == permissionList) {
            return ResultUtil.errorTable("获取权限详情数据失败");
        } else {
            return ResultUtil.successTable("获取权限详情数据成功", permissionList,pages.getTotal());
        }
    }

    /*编辑权限*/
    @ApiOperation(value = "编辑权限", notes = "编辑权限")
    @PostMapping("/edit")
    @RequiresPermissions("permission:edit")
    public ResponseVo editPermission(Permission permission) {
        int a = permissionService.updateByPermissionId(permission);
        if (a > 0) {
            shiroService.updatePermission();
            return ResultUtil.success("编辑权限成功");
        } else {
            return ResultUtil.error("编辑权限失败");
        }
    }

}
