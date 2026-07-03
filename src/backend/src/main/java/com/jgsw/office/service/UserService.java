package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.SysUser;
import com.jgsw.office.entity.SysUserRole;
import com.jgsw.office.mapper.SysUserMapper;
import com.jgsw.office.mapper.SysUserRoleMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Result<IPage<SysUser>> getUserList(int page, int size, String username) {
        Page<SysUser> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like(SysUser::getUsername, username);
        }
        queryWrapper.orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> userPage = sysUserMapper.selectPage(pageParam, queryWrapper);
        return Result.success(userPage);
    }

    public Result<SysUser> getUserById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(user);
    }

    @Transactional
    public Result<SysUser> createUser(SysUser user) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, user.getUsername());
        if (sysUserMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "用户名已存在");
        }

        user.setPassword(passwordEncoder.encode("123456"));
        user.setStatus(1);
        sysUserMapper.insert(user);
        return Result.success("创建成功", user);
    }

    @Transactional
    public Result<SysUser> updateUser(SysUser user) {
        SysUser existing = sysUserMapper.selectById(user.getId());
        if (existing == null) {
            return Result.error(404, "用户不存在");
        }

        user.setPassword(null);
        sysUserMapper.updateById(user);
        return Result.success("更新成功", user);
    }

    @Transactional
    public Result<Void> deleteUser(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        sysUserMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @Transactional
    public Result<Void> resetPassword(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        user.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.updateById(user);
        return Result.success("密码已重置为123456");
    }

    @Transactional
    public Result<Void> assignRoles(Long userId, List<Long> roleIds) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));

        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }

        return Result.success("角色分配成功");
    }
}
