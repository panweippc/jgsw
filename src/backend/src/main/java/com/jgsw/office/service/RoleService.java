package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.SysRole;
import com.jgsw.office.entity.SysRolePermission;
import com.jgsw.office.mapper.SysRoleMapper;
import com.jgsw.office.mapper.SysRolePermissionMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    public Result<IPage<SysRole>> getRoleList(int page, int size, String roleName) {
        Page<SysRole> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.like(SysRole::getRoleName, roleName);
        }
        queryWrapper.orderByAsc(SysRole::getSortOrder);
        IPage<SysRole> rolePage = sysRoleMapper.selectPage(pageParam, queryWrapper);
        return Result.success(rolePage);
    }

    public Result<SysRole> getRoleById(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            return Result.error(404, "角色不存在");
        }
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(
                new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id)
        );
        role.setPermissionIds(rolePermissions.stream().map(SysRolePermission::getPermissionId).collect(java.util.stream.Collectors.toList()));
        return Result.success(role);
    }

    @Transactional
    public Result<SysRole> createRole(SysRole role) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, role.getRoleCode());
        if (sysRoleMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "角色编码已存在");
        }

        role.setStatus(1);
        sysRoleMapper.insert(role);
        return Result.success("创建成功", role);
    }

    @Transactional
    public Result<SysRole> updateRole(SysRole role) {
        SysRole existing = sysRoleMapper.selectById(role.getId());
        if (existing == null) {
            return Result.error(404, "角色不存在");
        }

        sysRoleMapper.updateById(role);
        return Result.success("更新成功", role);
    }

    @Transactional
    public Result<Void> deleteRole(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            return Result.error(404, "角色不存在");
        }

        sysRolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id));
        sysRoleMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @Transactional
    public Result<Void> assignPermissions(Long roleId, List<Long> permissionIds) {
        sysRolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));

        for (Long permissionId : permissionIds) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            sysRolePermissionMapper.insert(rolePermission);
        }

        return Result.success("权限分配成功");
    }

    public Result<List<SysRole>> getAllRoles() {
        List<SysRole> roles = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1));
        return Result.success(roles);
    }
}
