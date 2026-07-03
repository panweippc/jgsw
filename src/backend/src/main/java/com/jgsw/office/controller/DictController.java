package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.SysDictItem;
import com.jgsw.office.entity.SysDictType;
import com.jgsw.office.service.DictService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/type")
    public Result<IPage<SysDictType>> getDictTypeList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String dictTypeName) {
        return dictService.getDictTypeList(page, size, dictTypeName);
    }

    @GetMapping("/type/{id}")
    public Result<SysDictType> getDictTypeById(@PathVariable Long id) {
        return dictService.getDictTypeById(id);
    }

    @PostMapping("/type")
    public Result<SysDictType> createDictType(@RequestBody SysDictType type) {
        return dictService.createDictType(type);
    }

    @PutMapping("/type/{id}")
    public Result<SysDictType> updateDictType(@PathVariable Long id, @RequestBody SysDictType type) {
        type.setId(id);
        return dictService.updateDictType(type);
    }

    @DeleteMapping("/type/{id}")
    public Result<Void> deleteDictType(@PathVariable Long id) {
        return dictService.deleteDictType(id);
    }

    @GetMapping("/items/{typeId}")
    public Result<List<SysDictItem>> getDictItems(@PathVariable Long typeId) {
        return dictService.getDictItems(typeId);
    }

    @PostMapping("/item")
    public Result<SysDictItem> createDictItem(@RequestBody SysDictItem item) {
        return dictService.createDictItem(item);
    }

    @PutMapping("/item/{id}")
    public Result<SysDictItem> updateDictItem(@PathVariable Long id, @RequestBody SysDictItem item) {
        item.setId(id);
        return dictService.updateDictItem(item);
    }

    @DeleteMapping("/item/{id}")
    public Result<Void> deleteDictItem(@PathVariable Long id) {
        return dictService.deleteDictItem(id);
    }
}
