# 机关事务办公室管理系统 - 技术设计文档

## 1. 项目概述

本系统是一个机关事务办公室管理系统，主要包含系统设置和物资管理两个模块，采用前后端分离架构，支持按钮级细粒度权限控制。

## 2. 技术栈

### 2.1 后端技术栈
- **语言**: Java 8+
- **框架**: Spring Boot 2.7.x
- **ORM**: MyBatis-Plus 3.5.x
- **数据库**: MySQL 5.7
- **认证**: JWT Token
- **加密**: BCryptPasswordEncoder

### 2.2 前端技术栈
- **框架**: Vue 3.4+
- **UI组件**: Element Plus 2.4+
- **构建工具**: Vite 5.2+
- **状态管理**: Pinia 2.1+
- **路由**: Vue Router 4.3+
- **HTTP客户端**: Axios 1.6+

## 3. 数据库设计

### 3.1 核心权限表

| 表名 | 说明 |
|------|------|
| sys_user | 用户表 |
| sys_role | 角色表 |
| sys_permission | 权限表（三级树形：模块/菜单/按钮） |
| sys_user_role | 用户角色关联表 |
| sys_role_permission | 角色权限关联表 |

### 3.2 系统设置模块表

| 表名 | 说明 |
|------|------|
| sys_dept | 部门表 |
| sys_post | 岗位表 |
| sys_user_post | 用户岗位关联表 |
| sys_dict_type | 数据字典类型表 |
| sys_dict_item | 数据字典明细表 |
| sys_config | 系统配置表 |
| sys_operation_log | 操作日志表 |

### 3.3 物资管理模块表

| 表名 | 说明 |
|------|------|
| material_category | 物资分类表 |
| warehouse | 库房信息表 |
| material | 物资主表 |
| material_stock | 库存表 |
| material_inbound | 入库记录表 |
| material_outbound | 出库记录表 |
| material_apply | 领用申请表 |

## 4. API接口设计

### 4.1 认证模块

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/auth/login | POST | 用户登录 |
| /api/auth/logout | POST | 用户登出 |
| /api/auth/refresh | POST | 刷新Token |
| /api/auth/info | GET | 获取当前用户信息 |

### 4.2 用户管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/user | GET | 获取用户列表 |
| /api/system/user/{id} | GET | 获取用户详情 |
| /api/system/user | POST | 新增用户 |
| /api/system/user/{id} | PUT | 更新用户 |
| /api/system/user/{id} | DELETE | 删除用户 |
| /api/system/user/{id}/reset-pwd | PUT | 重置密码 |
| /api/system/user/{id}/roles | PUT | 分配角色 |

### 4.3 角色管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/role | GET | 获取角色列表 |
| /api/system/role/{id} | GET | 获取角色详情 |
| /api/system/role | POST | 新增角色 |
| /api/system/role/{id} | PUT | 更新角色 |
| /api/system/role/{id} | DELETE | 删除角色 |
| /api/system/role/{id}/permissions | PUT | 分配权限 |

### 4.4 权限管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/permission | GET | 获取权限树形列表 |
| /api/system/permission | POST | 新增权限 |
| /api/system/permission/{id} | PUT | 更新权限 |
| /api/system/permission/{id} | DELETE | 删除权限 |

### 4.5 部门管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/dept | GET | 获取部门树形列表 |
| /api/system/dept | POST | 新增部门 |
| /api/system/dept/{id} | PUT | 更新部门 |
| /api/system/dept/{id} | DELETE | 删除部门 |

### 4.6 岗位管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/post | GET | 获取岗位列表 |
| /api/system/post | POST | 新增岗位 |
| /api/system/post/{id} | PUT | 更新岗位 |
| /api/system/post/{id} | DELETE | 删除岗位 |

### 4.7 字典管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/system/dict/type | GET | 获取字典类型列表 |
| /api/system/dict/type | POST | 新增字典类型 |
| /api/system/dict/type/{id} | PUT | 更新字典类型 |
| /api/system/dict/type/{id} | DELETE | 删除字典类型 |
| /api/system/dict/items/{typeId} | GET | 获取字典项列表 |
| /api/system/dict/item | POST | 新增字典项 |
| /api/system/dict/item/{id} | PUT | 更新字典项 |
| /api/system/dict/item/{id} | DELETE | 删除字典项 |

### 4.8 物资信息管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/info | GET | 获取物资列表 |
| /api/material/info/{id} | GET | 获取物资详情 |
| /api/material/info | POST | 新增物资 |
| /api/material/info/{id} | PUT | 更新物资 |
| /api/material/info/{id} | DELETE | 删除物资 |
| /api/material/info/import | POST | 导入物资 |
| /api/material/info/export | GET | 导出资物资 |

### 4.9 物资分类管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/category | GET | 获取分类树形列表 |
| /api/material/category | POST | 新增分类 |
| /api/material/category/{id} | PUT | 更新分类 |
| /api/material/category/{id} | DELETE | 删除分类 |

### 4.10 入库管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/inbound | GET | 获取入库单列表 |
| /api/material/inbound/{id} | GET | 获取入库单详情 |
| /api/material/inbound | POST | 新增入库单 |
| /api/material/inbound/{id} | PUT | 更新入库单 |
| /api/material/inbound/{id} | DELETE | 删除入库单 |
| /api/material/inbound/{id}/audit | PUT | 审核入库单 |

### 4.11 出库管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/outbound | GET | 获取出库单列表 |
| /api/material/outbound/{id} | GET | 获取出库单详情 |
| /api/material/outbound | POST | 新增出库单 |
| /api/material/outbound/{id} | PUT | 更新出库单 |
| /api/material/outbound/{id} | DELETE | 删除出库单 |
| /api/material/outbound/{id}/audit | PUT | 审核出库单 |

### 4.12 领用申请管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/apply | GET | 获取申请列表 |
| /api/material/apply/{id} | GET | 获取申请详情 |
| /api/material/apply | POST | 新增申请 |
| /api/material/apply/{id} | PUT | 更新申请 |
| /api/material/apply/{id} | DELETE | 删除申请 |
| /api/material/apply/{id}/approve | PUT | 审批申请 |

### 4.13 库存管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/stock | GET | 获取库存列表 |
| /api/material/stock/{id} | GET | 获取库存详情 |
| /api/material/stock/check | POST | 库存盘点 |

### 4.14 库房管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/warehouse | GET | 获取库房列表 |
| /api/material/warehouse/{id} | GET | 获取库房详情 |
| /api/material/warehouse | POST | 新增库房 |
| /api/material/warehouse/{id} | PUT | 更新库房 |
| /api/material/warehouse/{id} | DELETE | 删除库房 |

### 4.15 统计管理

| 接口路径 | HTTP方法 | 说明 |
|----------|----------|------|
| /api/material/statistics/inbound | GET | 入库统计 |
| /api/material/statistics/outbound | GET | 出库统计 |
| /api/material/statistics/stock | GET | 库存统计 |

## 5. 前端页面设计

### 5.1 布局结构

- **侧边栏**: 系统设置、物资管理两个模块导航
- **顶部栏**: 用户信息、退出登录
- **主内容区**: 各功能页面

### 5.2 系统设置模块页面

| 页面路径 | 说明 | 组件 |
|----------|------|------|
| /system/user | 用户管理 | UserList.vue, UserForm.vue |
| /system/role | 角色管理 | RoleList.vue, RoleForm.vue, RolePermission.vue |
| /system/menu | 菜单管理 | MenuList.vue, MenuForm.vue |
| /system/dept | 部门管理 | DeptList.vue, DeptForm.vue |
| /system/post | 岗位管理 | PostList.vue, PostForm.vue |
| /system/dict | 字典管理 | DictTypeList.vue, DictItemList.vue |

### 5.3 物资管理模块页面

| 页面路径 | 说明 | 组件 |
|----------|------|------|
| /material/info | 物资信息 | MaterialList.vue, MaterialForm.vue |
| /material/category | 物资分类 | CategoryList.vue, CategoryForm.vue |
| /material/inbound | 入库单 | InboundList.vue, InboundForm.vue |
| /material/outbound | 出库单 | OutboundList.vue, OutboundForm.vue |
| /material/apply | 领用申请 | ApplyList.vue, ApplyForm.vue |
| /material/stock | 库存查询 | StockList.vue |
| /material/warehouse | 库房信息 | WarehouseList.vue, WarehouseForm.vue |
| /material/statistics | 统计表 | Statistics.vue |

## 6. 配色方案（蓝色主题）

### 6.1 主色调

| 颜色名称 | 颜色值 | 用途 |
|----------|--------|------|
| 主色 | #1890ff | 主按钮、选中状态、导航栏 |
| 主色浅 | #40a9ff | 悬停状态 |
| 主色深 | #096dd9 | 点击状态 |

### 6.2 辅助色

| 颜色名称 | 颜色值 | 用途 |
|----------|--------|------|
| 成功色 | #52c41a | 成功提示、操作成功 |
| 警告色 | #faad14 | 警告提示、警告状态 |
| 错误色 | #f5222d | 错误提示、删除操作 |
| 信息色 | #1890ff | 信息提示 |

### 6.3 中性色

| 颜色名称 | 颜色值 | 用途 |
|----------|--------|------|
| 文字主色 | #000000 | 主要文字 |
| 文字次色 | #595959 | 次要文字 |
| 文字浅色 | #8c8c8c | 辅助文字 |
| 边框色 | #d9d9d9 | 边框、分割线 |
| 背景色 | #f0f2f5 | 页面背景 |
| 卡片色 | #ffffff | 卡片背景 |

### 6.4 导航栏配色

| 元素 | 颜色值 |
|------|--------|
| 侧边栏背景 | #001529 |
| 侧边栏文字 | rgba(255,255,255,0.85) |
| 侧边栏选中 | #1890ff |
| 顶部栏背景 | #ffffff |

## 7. 权限控制设计

### 7.1 权限模型

采用三级树形权限结构：
- **模块级**（type=1）：如"系统设置"、"物资管理"
- **菜单级**（type=2）：如"用户管理"、"角色管理"
- **按钮级**（type=3）：如"新增用户"、"编辑用户"

### 7.2 权限编码规则

```
{模块}:{菜单}:{操作}
例如: system:user:add
```

### 7.3 权限校验流程

1. 用户登录获取JWT Token
2. 请求时携带Token
3. 后端解析Token获取用户信息
4. 获取用户角色关联的权限列表
5. 校验请求路径对应的权限

## 8. 项目目录结构

### 8.1 后端目录结构

```
backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/jgsw/office/
│       │       ├── controller/     # REST API控制器
│       │       ├── service/        # 业务逻辑层
│       │       ├── mapper/         # 数据访问层
│       │       ├── entity/         # 数据库实体
│       │       ├── dto/            # 数据传输对象
│       │       ├── config/         # 配置类
│       │       ├── security/       # 安全认证
│       │       ├── util/           # 工具类
│       │       └── OfficeApplication.java
│       └── resources/
│           ├── application.yml     # 应用配置
│           ├── mapper/             # MyBatis映射文件
│           └── schema.sql          # 数据库初始化脚本
└── pom.xml                         # Maven配置
```

### 8.2 前端目录结构

```
frontend/
├── src/
│   ├── components/                 # 通用组件
│   ├── views/                      # 页面组件
│   │   ├── system/                 # 系统设置模块
│   │   └── material/               # 物资管理模块
│   ├── router/                     # 路由配置
│   ├── store/                      # 状态管理
│   ├── services/                   # API服务
│   ├── utils/                      # 工具函数
│   ├── styles/                     # 样式文件
│   ├── App.vue                     # 根组件
│   └── main.js                     # 入口文件
├── public/                         # 静态资源
├── package.json                    # 依赖配置
├── vite.config.js                  # Vite配置
└── index.html                      # HTML模板
```
