package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.SysRole;
import com.jgsw.office.service.RoleService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public Result<IPage<SysRole>> getRoleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roleName) {
        return roleService.getRoleList(page, size, roleName);
    }

    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    public Result<SysRole> createRole(@RequestBody SysRole role) {
        return roleService.createRole(role);
    }

    @PutMapping("/{id}")
    public Result<SysRole> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return roleService.updateRole(role);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PutMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody Map<String, List<Long>> params) {
        List<Long> permissionIds = params.get("permissionIds");
        return roleService.assignPermissions(id, permissionIds);
    }

    @GetMapping("/all")
    public Result<List<SysRole>> getAllRoles() {
        return roleService.getAllRoles();
    }
}
