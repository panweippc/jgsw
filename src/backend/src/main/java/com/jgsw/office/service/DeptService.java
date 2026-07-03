package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jgsw.office.entity.SysDept;
import com.jgsw.office.mapper.SysDeptMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public Result<List<SysDept>> getDeptTree() {
        List<SysDept> depts = sysDeptMapper.selectList(
                new LambdaQueryWrapper<SysDept>()
                        .eq(SysDept::getStatus, 1)
                        .orderByAsc(SysDept::getSortOrder)
        );
        return Result.success(buildTree(depts));
    }

    public Result<SysDept> getDeptById(Long id) {
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null) {
            return Result.error(404, "部门不存在");
        }
        return Result.success(dept);
    }

    @Transactional
    public Result<SysDept> createDept(SysDept dept) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getDeptCode, dept.getDeptCode());
        if (sysDeptMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "部门编码已存在");
        }

        dept.setStatus(1);
        sysDeptMapper.insert(dept);
        return Result.success("创建成功", dept);
    }

    @Transactional
    public Result<SysDept> updateDept(SysDept dept) {
        SysDept existing = sysDeptMapper.selectById(dept.getId());
        if (existing == null) {
            return Result.error(404, "部门不存在");
        }

        sysDeptMapper.updateById(dept);
        return Result.success("更新成功", dept);
    }

    @Transactional
    public Result<Void> deleteDept(Long id) {
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null) {
            return Result.error(404, "部门不存在");
        }

        sysDeptMapper.delete(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        sysDeptMapper.deleteById(id);
        return Result.success("删除成功");
    }

    private List<SysDept> buildTree(List<SysDept> depts) {
        return depts.stream()
                .filter(d -> d.getParentId() == 0)
                .peek(d -> d.setChildren(
                        depts.stream()
                                .filter(child -> child.getParentId().equals(d.getId()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
