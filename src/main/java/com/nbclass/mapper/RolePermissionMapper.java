package com.nbclass.mapper;

import com.nbclass.model.Permission;
import com.nbclass.model.RolePermission;
import com.nbclass.util.MyMapper;

import java.util.List;

/**
 * @version V1.0
 * @date 2018年7月11日
 * @author superzheng
 */
public interface RolePermissionMapper extends MyMapper<RolePermission> {

    List<Permission> selectPermissionByRoleId(String roleId);
}