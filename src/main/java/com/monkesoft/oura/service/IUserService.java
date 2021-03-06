package com.monkesoft.oura.service;

import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.monkesoft.oura.entity.UserInfo;
import com.monkesoft.oura.entity.UserOrgVO;
import com.monkesoft.oura.entity.UserRoleVO;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户服务接口
 */
public interface IUserService {

    public void insertUser(UserInfo userInfo);

    public void updateUser(UserInfo userInfo);
    public void updateUserStatus(String userId,int status);
    public void updateUserPassword(String userId, String password);

    public void deleteUser(String userId);

    public UserInfo getUserById(String userId);

    public Page<UserInfo> getUsers(int pageNum, int pageSize);

    public Page<UserOrgVO> getUsersOfOrg(String orgId, int pageNum, int pageSize);

    public Page<UserRoleVO> getUsersOfRole(String roleId, int pageNum, int pageSize);
}
