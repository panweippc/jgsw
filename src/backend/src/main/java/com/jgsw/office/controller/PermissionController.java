package com.jgsw.office.controller;

import com.jgsw.office.entity.SysPermission;
import com.jgsw.office.service.PermissionService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    public Result<List<SysPermission>> getPermissionTree() {
        return permissionService.getPermissionTree();
    }

    @GetMapping
    public Result<List<SysPermission>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public Result<SysPermission> getPermissionById(@PathVariable Long id) {
        return permissionService.getPermissionById(id);
    }

    @PostMapping
    public Result<SysPermission> createPermission(@RequestBody SysPermission permission) {
        return permissionService.createPermission(permission);
    }

    @PutMapping("/{id}")
    public Result<SysPermission> updatePermission(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        return permissionService.updatePermission(permission);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@PathVariable Long id) {
        return permissionService.deletePermission(id);
    }
}
