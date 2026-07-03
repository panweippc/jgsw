package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jgsw.office.entity.SysPermission;
import com.jgsw.office.entity.SysRole;
import com.jgsw.office.entity.SysUser;
import com.jgsw.office.entity.SysUserRole;
import com.jgsw.office.mapper.SysPermissionMapper;
import com.jgsw.office.mapper.SysRoleMapper;
import com.jgsw.office.mapper.SysUserMapper;
import com.jgsw.office.mapper.SysUserRoleMapper;
import com.jgsw.office.util.JwtUtil;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Result<Map<String, Object>> login(String username, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.error(401, "用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            return Result.error(401, "用户已被禁用");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        List<SysPermission> permissions = getUserPermissions(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("permissions", permissions);

        return Result.success("登录成功", result);
    }

    public Result<Map<String, Object>> getUserInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        List<SysPermission> permissions = getUserPermissions(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("permissions", permissions);

        return Result.success(result);
    }

    private List<SysPermission> getUserPermissions(Long userId) {
        LambdaQueryWrapper<SysUserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(userRoleWrapper);

        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return List.of();
        }

        List<SysPermission> permissions = sysPermissionMapper.findPermissionsByRoleIds(roleIds);
        return buildPermissionTree(permissions);
    }

    private List<SysPermission> buildPermissionTree(List<SysPermission> permissions) {
        return permissions.stream()
                .filter(p -> p.getParentId() == 0)
                .peek(p -> p.setChildren(
                        permissions.stream()
                                .filter(child -> child.getParentId().equals(p.getId()))
                                .peek(child -> child.setChildren(
                                        permissions.stream()
                                                .filter(grandChild -> grandChild.getParentId().equals(child.getId()))
                                                .collect(Collectors.toList())
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
