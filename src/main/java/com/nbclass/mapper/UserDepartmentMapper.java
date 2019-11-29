package com.nbclass.mapper;

import com.nbclass.model.UserDepartment;
import com.nbclass.model.UserRole;
import com.nbclass.util.MyMapper;

/**
 * @version V1.0
 * @date 2018年7月11日
 * @author superzheng
 */
public interface UserDepartmentMapper extends MyMapper<UserDepartment> {
    int deleteByUserId(String userId);
}