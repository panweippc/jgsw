package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jgsw.office.entity.SysPermission;
import com.jgsw.office.mapper.SysPermissionMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    public Result<List<SysPermission>> getAllPermissions() {
        List<SysPermission> permissions = sysPermissionMapper.selectList(
                new LambdaQueryWrapper<SysPermission>()
                        .eq(SysPermission::getStatus, 1)
                        .orderByAsc(SysPermission::getSortOrder)
        );
        return Result.success(permissions);
    }

    public Result<List<SysPermission>> getPermissionTree() {
        List<SysPermission> permissions = sysPermissionMapper.selectList(
                new LambdaQueryWrapper<SysPermission>()
                        .eq(SysPermission::getStatus, 1)
                        .orderByAsc(SysPermission::getSortOrder)
        );
        return Result.success(buildTree(permissions));
    }

    public Result<SysPermission> getPermissionById(Long id) {
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null) {
            return Result.error(404, "权限不存在");
        }
        return Result.success(permission);
    }

    @Transactional
    public Result<SysPermission> createPermission(SysPermission permission) {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPermission::getPermissionCode, permission.getPermissionCode());
        if (sysPermissionMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "权限编码已存在");
        }

        permission.setStatus(1);
        sysPermissionMapper.insert(permission);
        return Result.success("创建成功", permission);
    }

    @Transactional
    public Result<SysPermission> updatePermission(SysPermission permission) {
        SysPermission existing = sysPermissionMapper.selectById(permission.getId());
        if (existing == null) {
            return Result.error(404, "权限不存在");
        }

        sysPermissionMapper.updateById(permission);
        return Result.success("更新成功", permission);
    }

    @Transactional
    public Result<Void> deletePermission(Long id) {
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null) {
            return Result.error(404, "权限不存在");
        }

        sysPermissionMapper.delete(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));
        sysPermissionMapper.deleteById(id);
        return Result.success("删除成功");
    }

    private List<SysPermission> buildTree(List<SysPermission> permissions) {
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
