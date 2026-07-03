CREATE DATABASE IF NOT EXISTS jgsw_office DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE jgsw_office;

-- ----------------------------
-- 核心权限表结构
-- ----------------------------

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像路径',
    dept_id BIGINT COMMENT '所属部门ID',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    is_admin TINYINT DEFAULT 0 COMMENT '是否超级管理员: 0-否, 1-是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code),
    KEY idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- 权限表（三级树形结构：模块/菜单/按钮）
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型: 1-模块, 2-菜单, 3-按钮',
    menu_path VARCHAR(255) COMMENT '菜单路径',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_permission_code (permission_code),
    KEY idx_parent_id (parent_id),
    KEY idx_permission_type (permission_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (user_id, role_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (role_id, permission_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联表';

-- ----------------------------
-- 系统设置模块表结构
-- ----------------------------

-- 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(50) COMMENT '部门编码',
    leader VARCHAR(50) COMMENT '部门负责人',
    phone VARCHAR(20) COMMENT '部门电话',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_dept_code (dept_code),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

-- 岗位表
CREATE TABLE IF NOT EXISTS sys_post (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    post_name VARCHAR(50) NOT NULL COMMENT '岗位名称',
    post_code VARCHAR(50) NOT NULL COMMENT '岗位编码',
    description VARCHAR(255) COMMENT '岗位描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_code (post_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位表';

-- 用户岗位关联表
CREATE TABLE IF NOT EXISTS sys_user_post (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '岗位ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (user_id, post_id),
    KEY idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户岗位关联表';

-- 数据字典类型表
CREATE TABLE IF NOT EXISTS sys_dict_type (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
    dict_type_code VARCHAR(50) NOT NULL COMMENT '字典类型编码',
    dict_type_name VARCHAR(100) NOT NULL COMMENT '字典类型名称',
    description VARCHAR(255) COMMENT '描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_dict_type_code (dict_type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典类型表';

-- 数据字典明细表
CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典项ID',
    dict_type_id BIGINT NOT NULL COMMENT '字典类型ID',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典值',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_dict_type_id (dict_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典明细表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_name VARCHAR(100) COMMENT '配置名称',
    config_group VARCHAR(50) COMMENT '配置分组',
    description VARCHAR(255) COMMENT '描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作人ID',
    username VARCHAR(50) COMMENT '操作人姓名',
    module_name VARCHAR(100) COMMENT '模块名称',
    operation_type VARCHAR(50) COMMENT '操作类型: add-新增, edit-编辑, delete-删除, view-查看, export-导出, import-导入',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    request_url VARCHAR(255) COMMENT '请求URL',
    request_method VARCHAR(10) COMMENT '请求方法: GET, POST, PUT, DELETE',
    request_param TEXT COMMENT '请求参数',
    response_result TEXT COMMENT '响应结果',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT 'User-Agent',
    execution_time INT COMMENT '执行时间(毫秒)',
    is_success TINYINT DEFAULT 1 COMMENT '是否成功: 0-失败, 1-成功',
    error_message TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_module_name (module_name),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- ----------------------------
-- 物资管理模块表结构
-- ----------------------------

-- 物资分类表
CREATE TABLE IF NOT EXISTS material_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) COMMENT '分类编码',
    description VARCHAR(255) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_code (category_code),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='物资分类表';

-- 库房信息表
CREATE TABLE IF NOT EXISTS warehouse (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '库房ID',
    warehouse_name VARCHAR(100) NOT NULL COMMENT '库房名称',
    warehouse_code VARCHAR(50) COMMENT '库房编码',
    location VARCHAR(255) COMMENT '库房位置',
    manager VARCHAR(50) COMMENT '库房管理员',
    phone VARCHAR(20) COMMENT '联系电话',
    capacity DECIMAL(10,2) COMMENT '库房容量',
    description VARCHAR(255) COMMENT '描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_warehouse_code (warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='库房信息表';

-- 物资主表
CREATE TABLE IF NOT EXISTS material (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '物资ID',
    material_code VARCHAR(50) NOT NULL COMMENT '物资编码',
    material_name VARCHAR(100) NOT NULL COMMENT '物资名称',
    category_id BIGINT COMMENT '分类ID',
    spec VARCHAR(100) COMMENT '规格型号',
    unit VARCHAR(20) COMMENT '计量单位',
    brand VARCHAR(50) COMMENT '品牌',
    origin VARCHAR(100) COMMENT '产地',
    purchase_price DECIMAL(12,2) COMMENT '采购单价',
    retail_price DECIMAL(12,2) COMMENT '零售单价',
    min_stock INT DEFAULT 0 COMMENT '最低库存',
    max_stock INT DEFAULT 0 COMMENT '最高库存',
    description VARCHAR(500) COMMENT '物资描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_code (material_code),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='物资主表';

-- 库存表
CREATE TABLE IF NOT EXISTS material_stock (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '库存ID',
    material_id BIGINT NOT NULL COMMENT '物资ID',
    warehouse_id BIGINT NOT NULL COMMENT '库房ID',
    quantity INT DEFAULT 0 COMMENT '库存数量',
    available_quantity INT DEFAULT 0 COMMENT '可用数量',
    locked_quantity INT DEFAULT 0 COMMENT '锁定数量',
    last_inbound_time DATETIME COMMENT '最后入库时间',
    last_outbound_time DATETIME COMMENT '最后出库时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_warehouse (material_id, warehouse_id),
    KEY idx_warehouse_id (warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='库存表';

-- 入库记录表
CREATE TABLE IF NOT EXISTS material_inbound (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '入库ID',
    inbound_no VARCHAR(50) NOT NULL COMMENT '入库单号',
    material_id BIGINT NOT NULL COMMENT '物资ID',
    warehouse_id BIGINT NOT NULL COMMENT '库房ID',
    quantity INT NOT NULL COMMENT '入库数量',
    unit_price DECIMAL(12,2) COMMENT '单价',
    total_amount DECIMAL(12,2) COMMENT '总金额',
    supplier VARCHAR(100) COMMENT '供应商',
    purchase_no VARCHAR(50) COMMENT '采购单号',
    batch_no VARCHAR(50) COMMENT '批次号',
    expire_date DATETIME COMMENT '有效期',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    remark VARCHAR(500) COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-作废, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_inbound_no (inbound_no),
    KEY idx_material_id (material_id),
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='入库记录表';

-- 出库记录表
CREATE TABLE IF NOT EXISTS material_outbound (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '出库ID',
    outbound_no VARCHAR(50) NOT NULL COMMENT '出库单号',
    material_id BIGINT NOT NULL COMMENT '物资ID',
    warehouse_id BIGINT NOT NULL COMMENT '库房ID',
    quantity INT NOT NULL COMMENT '出库数量',
    unit_price DECIMAL(12,2) COMMENT '单价',
    total_amount DECIMAL(12,2) COMMENT '总金额',
    receiver_dept VARCHAR(100) COMMENT '领用部门',
    receiver_id BIGINT COMMENT '领用人ID',
    receiver_name VARCHAR(50) COMMENT '领用人姓名',
    apply_no VARCHAR(50) COMMENT '申请单号',
    purpose VARCHAR(200) COMMENT '领用用途',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    remark VARCHAR(500) COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-作废, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_outbound_no (outbound_no),
    KEY idx_material_id (material_id),
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='出库记录表';

-- 领用申请表
CREATE TABLE IF NOT EXISTS material_apply (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    apply_no VARCHAR(50) NOT NULL COMMENT '申请单号',
    material_id BIGINT NOT NULL COMMENT '物资ID',
    quantity INT NOT NULL COMMENT '申请数量',
    purpose VARCHAR(200) COMMENT '领用用途',
    apply_dept VARCHAR(100) COMMENT '申请部门',
    apply_id BIGINT COMMENT '申请人ID',
    apply_name VARCHAR(50) COMMENT '申请人姓名',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    warehouse_id BIGINT COMMENT '库房ID',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待审批, 1-已批准, 2-已拒绝, 3-已出库',
    approve_id BIGINT COMMENT '审批人ID',
    approve_name VARCHAR(50) COMMENT '审批人姓名',
    approve_time DATETIME COMMENT '审批时间',
    approve_remark VARCHAR(500) COMMENT '审批意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_apply_no (apply_no),
    KEY idx_material_id (material_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='领用申请表';

-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 插入角色
INSERT IGNORE INTO sys_role (id, role_name, role_code, description, sort_order, status) VALUES
(1, '管理员', 'admin', '系统管理员，拥有全部权限', 1, 1),
(2, '普通用户', 'user', '普通用户，权限由管理员分配', 2, 1);

-- 插入权限（三级树形结构）
-- 系统设置模块权限
INSERT IGNORE INTO sys_permission (id, parent_id, permission_name, permission_code, permission_type, menu_path, icon, sort_order, status) VALUES
(1, 0, '系统设置', 'system', 1, '', 'setting', 1, 1),
(2, 1, '用户管理', 'system:user', 2, '/system/user', 'user', 1, 1),
(3, 2, '新增用户', 'system:user:add', 3, '', '', 1, 1),
(4, 2, '编辑用户', 'system:user:edit', 3, '', '', 2, 1),
(5, 2, '删除用户', 'system:user:delete', 3, '', '', 3, 1),
(6, 2, '查看用户', 'system:user:view', 3, '', '', 4, 1),
(7, 2, '重置密码', 'system:user:resetPwd', 3, '', '', 5, 1),
(8, 2, '分配角色', 'system:user:assignRole', 3, '', '', 6, 1),
(9, 1, '角色管理', 'system:role', 2, '/system/role', 'role', 2, 1),
(10, 9, '新增角色', 'system:role:add', 3, '', '', 1, 1),
(11, 9, '编辑角色', 'system:role:edit', 3, '', '', 2, 1),
(12, 9, '删除角色', 'system:role:delete', 3, '', '', 3, 1),
(13, 9, '查看角色', 'system:role:view', 3, '', '', 4, 1),
(14, 9, '分配权限', 'system:role:assignPerm', 3, '', '', 5, 1),
(15, 1, '菜单管理', 'system:menu', 2, '/system/menu', 'menu', 3, 1),
(16, 15, '新增菜单', 'system:menu:add', 3, '', '', 1, 1),
(17, 15, '编辑菜单', 'system:menu:edit', 3, '', '', 2, 1),
(18, 15, '删除菜单', 'system:menu:delete', 3, '', '', 3, 1),
(19, 15, '查看菜单', 'system:menu:view', 3, '', '', 4, 1),
(20, 1, '部门管理', 'system:dept', 2, '/system/dept', 'dept', 4, 1),
(21, 20, '新增部门', 'system:dept:add', 3, '', '', 1, 1),
(22, 20, '编辑部门', 'system:dept:edit', 3, '', '', 2, 1),
(23, 20, '删除部门', 'system:dept:delete', 3, '', '', 3, 1),
(24, 20, '查看部门', 'system:dept:view', 3, '', '', 4, 1),
(25, 1, '岗位管理', 'system:post', 2, '/system/post', 'post', 5, 1),
(26, 25, '新增岗位', 'system:post:add', 3, '', '', 1, 1),
(27, 25, '编辑岗位', 'system:post:edit', 3, '', '', 2, 1),
(28, 25, '删除岗位', 'system:post:delete', 3, '', '', 3, 1),
(29, 25, '查看岗位', 'system:post:view', 3, '', '', 4, 1),
(30, 1, '字典管理', 'system:dict', 2, '/system/dict', 'dict', 6, 1),
(31, 30, '新增字典类型', 'system:dict:addType', 3, '', '', 1, 1),
(32, 30, '编辑字典类型', 'system:dict:editType', 3, '', '', 2, 1),
(33, 30, '删除字典类型', 'system:dict:deleteType', 3, '', '', 3, 1),
(34, 30, '新增字典项', 'system:dict:addItem', 3, '', '', 4, 1),
(35, 30, '编辑字典项', 'system:dict:editItem', 3, '', '', 5, 1),
(36, 30, '删除字典项', 'system:dict:deleteItem', 3, '', '', 6, 1),
(37, 30, '查看字典', 'system:dict:view', 3, '', '', 7, 1),
(38, 1, '系统配置', 'system:config', 2, '/system/config', 'config', 7, 1),
(39, 38, '新增配置', 'system:config:add', 3, '', '', 1, 1),
(40, 38, '编辑配置', 'system:config:edit', 3, '', '', 2, 1),
(41, 38, '删除配置', 'system:config:delete', 3, '', '', 3, 1),
(42, 38, '查看配置', 'system:config:view', 3, '', '', 4, 1),
(43, 1, '操作日志', 'system:log', 2, '/system/log', 'log', 8, 1),
(44, 43, '查看日志', 'system:log:view', 3, '', '', 1, 1),
(45, 43, '导出日志', 'system:log:export', 3, '', '', 2, 1),
(46, 43, '清理日志', 'system:log:clean', 3, '', '', 3, 1);

-- 物资管理模块权限
INSERT IGNORE INTO sys_permission (id, parent_id, permission_name, permission_code, permission_type, menu_path, icon, sort_order, status) VALUES
(47, 0, '物资管理', 'material', 1, '', 'material', 2, 1),
(48, 47, '物资信息', 'material:info', 2, '/material/info', 'materialInfo', 1, 1),
(49, 48, '新增物资', 'material:info:add', 3, '', '', 1, 1),
(50, 48, '编辑物资', 'material:info:edit', 3, '', '', 2, 1),
(51, 48, '删除物资', 'material:info:delete', 3, '', '', 3, 1),
(52, 48, '查看物资', 'material:info:view', 3, '', '', 4, 1),
(53, 48, '导入物资', 'material:info:import', 3, '', '', 5, 1),
(54, 48, '导出物资', 'material:info:export', 3, '', '', 6, 1),
(55, 47, '物资分类', 'material:category', 2, '/material/category', 'category', 2, 1),
(56, 55, '新增分类', 'material:category:add', 3, '', '', 1, 1),
(57, 55, '编辑分类', 'material:category:edit', 3, '', '', 2, 1),
(58, 55, '删除分类', 'material:category:delete', 3, '', '', 3, 1),
(59, 55, '查看分类', 'material:category:view', 3, '', '', 4, 1),
(60, 47, '入库单', 'material:inbound', 2, '/material/inbound', 'inbound', 3, 1),
(61, 60, '新增入库单', 'material:inbound:add', 3, '', '', 1, 1),
(62, 60, '编辑入库单', 'material:inbound:edit', 3, '', '', 2, 1),
(63, 60, '删除入库单', 'material:inbound:delete', 3, '', '', 3, 1),
(64, 60, '查看入库单', 'material:inbound:view', 3, '', '', 4, 1),
(65, 60, '审核入库单', 'material:inbound:audit', 3, '', '', 5, 1),
(66, 60, '导出入库单', 'material:inbound:export', 3, '', '', 6, 1),
(67, 47, '出库单', 'material:outbound', 2, '/material/outbound', 'outbound', 4, 1),
(68, 67, '新增出库单', 'material:outbound:add', 3, '', '', 1, 1),
(69, 67, '编辑出库单', 'material:outbound:edit', 3, '', '', 2, 1),
(70, 67, '删除出库单', 'material:outbound:delete', 3, '', '', 3, 1),
(71, 67, '查看出库单', 'material:outbound:view', 3, '', '', 4, 1),
(72, 67, '审核出库单', 'material:outbound:audit', 3, '', '', 5, 1),
(73, 67, '导出出库单', 'material:outbound:export', 3, '', '', 6, 1),
(74, 47, '领用申请', 'material:apply', 2, '/material/apply', 'apply', 5, 1),
(75, 74, '新增申请', 'material:apply:add', 3, '', '', 1, 1),
(76, 74, '编辑申请', 'material:apply:edit', 3, '', '', 2, 1),
(77, 74, '删除申请', 'material:apply:delete', 3, '', '', 3, 1),
(78, 74, '查看申请', 'material:apply:view', 3, '', '', 4, 1),
(79, 74, '审批申请', 'material:apply:approve', 3, '', '', 5, 1),
(80, 74, '导出申请', 'material:apply:export', 3, '', '', 6, 1),
(81, 47, '库存查询', 'material:stock', 2, '/material/stock', 'stock', 6, 1),
(82, 81, '查看库存', 'material:stock:view', 3, '', '', 1, 1),
(83, 81, '库存盘点', 'material:stock:check', 3, '', '', 2, 1),
(84, 81, '导出库存', 'material:stock:export', 3, '', '', 3, 1),
(85, 47, '库房信息', 'material:warehouse', 2, '/material/warehouse', 'warehouse', 7, 1),
(86, 85, '新增库房', 'material:warehouse:add', 3, '', '', 1, 1),
(87, 85, '编辑库房', 'material:warehouse:edit', 3, '', '', 2, 1),
(88, 85, '删除库房', 'material:warehouse:delete', 3, '', '', 3, 1),
(89, 85, '查看库房', 'material:warehouse:view', 3, '', '', 4, 1),
(90, 47, '统计表', 'material:statistics', 2, '/material/statistics', 'statistics', 8, 1),
(91, 90, '查看统计', 'material:statistics:view', 3, '', '', 1, 1),
(92, 90, '导出统计', 'material:statistics:export', 3, '', '', 2, 1);

-- 管理员角色关联全部权限
INSERT IGNORE INTO sys_role_permission (role_id, permission_id) SELECT 1, id FROM sys_permission WHERE status = 1;

-- 创建管理员用户（密码：admin123，BCrypt加密）
INSERT IGNORE INTO sys_user (id, username, password, real_name, phone, is_admin, status) VALUES
(1, 'admin', '$2a$10$vO7XqL7TtQv8K9J3J9L3Kv8K9J3J9L3Kv8K9J3J9L3Kv8K9J3J9L3K', '系统管理员', '13800138000', 1, 1);

-- 创建测试普通用户（密码：user123，BCrypt加密）
INSERT IGNORE INTO sys_user (id, username, password, real_name, phone, is_admin, status) VALUES
(2, 'user', '$2a$10$wO8Xr8TsRu9L0K4K4M4LwO8Xr8TsRu9L0K4K4M4LwO8Xr8TsRu9L0', '普通用户', '13800138001', 0, 1);

-- 普通用户关联普通用户角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES
(2, 2);

-- 初始化数据字典类型
INSERT IGNORE INTO sys_dict_type (id, dict_type_code, dict_type_name, description, sort_order, status) VALUES
(1, 'user_status', '用户状态', '用户状态枚举', 1, 1),
(2, 'role_status', '角色状态', '角色状态枚举', 2, 1),
(3, 'permission_type', '权限类型', '权限类型枚举', 3, 1),
(4, 'material_status', '物资状态', '物资状态枚举', 4, 1),
(5, 'apply_status', '申请状态', '领用申请状态枚举', 5, 1),
(6, 'stock_unit', '库存单位', '库存计量单位', 6, 1);

-- 初始化数据字典项
INSERT IGNORE INTO sys_dict_item (id, dict_type_id, dict_value, dict_label, sort_order, status) VALUES
(1, 1, '0', '禁用', 1, 1),
(2, 1, '1', '启用', 2, 1),
(3, 2, '0', '禁用', 1, 1),
(4, 2, '1', '启用', 2, 1),
(5, 3, '1', '模块', 1, 1),
(6, 3, '2', '菜单', 2, 1),
(7, 3, '3', '按钮', 3, 1),
(8, 4, '0', '禁用', 1, 1),
(9, 4, '1', '启用', 2, 1),
(10, 5, '0', '待审批', 1, 1),
(11, 5, '1', '已批准', 2, 1),
(12, 5, '2', '已拒绝', 3, 1),
(13, 5, '3', '已出库', 4, 1),
(14, 6, '个', '个', 1, 1),
(15, 6, '件', '件', 2, 1),
(16, 6, '台', '台', 3, 1),
(17, 6, '套', '套', 4, 1),
(18, 6, '箱', '箱', 5, 1),
(19, 6, '包', '包', 6, 1),
(20, 6, '米', '米', 7, 1),
(21, 6, '千克', '千克', 8, 1),
(22, 6, '升', '升', 9, 1);

-- 初始化系统配置
INSERT IGNORE INTO sys_config (id, config_key, config_value, config_name, config_group, description, sort_order, status) VALUES
(1, 'system.name', '机关事务办公室管理系统', '系统名称', 'system', '系统显示名称', 1, 1),
(2, 'system.version', '1.0.0', '系统版本', 'system', '系统当前版本号', 2, 1),
(3, 'system.copyright', '机关事务办公室', '版权信息', 'system', '系统版权归属', 3, 1),
(4, 'material.stock.alert', '10', '库存预警值', 'material', '库存低于此值时触发预警', 1, 1),
(5, 'material.apply.max', '100', '单次最大申请量', 'material', '单次领用申请的最大数量限制', 2, 1);

-- 初始化部门
INSERT IGNORE INTO sys_dept (id, parent_id, dept_name, dept_code, leader, phone, sort_order, status) VALUES
(1, 0, '机关事务办公室', 'JGSW', '张主任', '010-12345678', 1, 1),
(2, 1, '综合科', 'ZH', '李科长', '010-12345679', 2, 1),
(3, 1, '财务科', 'CW', '王科长', '010-12345680', 3, 1),
(4, 1, '物资管理科', 'WZ', '赵科长', '010-12345681', 4, 1),
(5, 1, '后勤保障科', 'HQ', '刘科长', '010-12345682', 5, 1);

-- 初始化岗位
INSERT IGNORE INTO sys_post (id, post_name, post_code, description, sort_order, status) VALUES
(1, '主任', 'ZR', '办公室主任', 1, 1),
(2, '副主任', 'FZR', '办公室副主任', 2, 1),
(3, '科长', 'KZ', '各科科长', 3, 1),
(4, '副科长', 'FKZ', '各科副科长', 4, 1),
(5, '科员', 'KY', '各科科员', 5, 1),
(6, '办事员', 'BSY', '办事员', 6, 1);

-- 初始化物资分类
INSERT IGNORE INTO material_category (id, parent_id, category_name, category_code, description, sort_order, status) VALUES
(1, 0, '办公用品', 'BG', '日常办公所需物品', 1, 1),
(2, 1, '文具类', 'BG-WJ', '笔、本、文件夹等', 1, 1),
(3, 1, '办公设备', 'BG-SB', '电脑、打印机、复印机等', 2, 1),
(4, 1, '劳保用品', 'BG-LB', '工作服、安全帽等', 3, 1),
(5, 0, '电器设备', 'DQ', '各类电器设备', 2, 1),
(6, 5, '空调设备', 'DQ-KT', '空调、风扇等', 1, 1),
(7, 5, '照明设备', 'DQ-ZM', '灯具、灯管等', 2, 1),
(8, 0, '家具用品', 'JJ', '办公家具', 3, 1),
(9, 8, '桌椅类', 'JJ-ZY', '办公桌、办公椅等', 1, 1),
(10, 8, '柜架类', 'JJ-GJ', '文件柜、书架等', 2, 1),
(11, 0, '清洁用品', 'QJ', '清洁卫生用品', 4, 1),
(12, 11, '清洁剂', 'QJ-JJ', '各类清洁剂', 1, 1),
(13, 11, '清洁工具', 'QJ-GJ', '拖把、扫帚等', 2, 1),
(14, 0, '耗材配件', 'HC', '各类耗材配件', 5, 1),
(15, 14, '打印耗材', 'HC-DY', '墨盒、硒鼓等', 1, 1),
(16, 14, '电脑配件', 'HC-DN', '鼠标、键盘等', 2, 1);

-- 初始化库房
INSERT IGNORE INTO warehouse (id, warehouse_name, warehouse_code, location, manager, phone, capacity, description, sort_order, status) VALUES
(1, '一号库房', 'WH-001', '办公楼一层东侧', '赵管理员', '13800138100', 500.00, '主要存放办公用品和文具', 1, 1),
(2, '二号库房', 'WH-002', '办公楼一层西侧', '钱管理员', '13800138101', 800.00, '主要存放办公设备和电器', 2, 1),
(3, '三号库房', 'WH-003', '附属楼一层', '孙管理员', '13800138102', 1000.00, '主要存放家具和大件物品', 3, 1),
(4, '四号库房', 'WH-004', '附属楼二层', '李管理员', '13800138103', 600.00, '主要存放清洁用品和耗材', 4, 1);

-- 初始化物资
INSERT IGNORE INTO material (id, material_code, material_name, category_id, spec, unit, brand, origin, purchase_price, retail_price, min_stock, max_stock, description, status) VALUES
(1, 'BG-WJ-001', 'A4打印纸', 2, '80g/包', '包', '得力', '浙江', 28.00, 35.00, 50, 500, '标准A4打印纸', 1),
(2, 'BG-WJ-002', '中性笔', 2, '0.5mm黑色', '盒', '晨光', '上海', 15.00, 20.00, 100, 1000, '办公用中性笔', 1),
(3, 'BG-WJ-003', '文件夹', 2, 'A4/蓝色', '个', '齐心', '广东', 8.00, 12.00, 50, 500, '标准文件夹', 1),
(4, 'BG-SB-001', '笔记本电脑', 3, 'ThinkPad X1', '台', '联想', '北京', 8500.00, 9500.00, 5, 50, '商务笔记本电脑', 1),
(5, 'BG-SB-002', '激光打印机', 3, 'HP LaserJet Pro', '台', '惠普', '美国', 1200.00, 1500.00, 3, 20, '办公激光打印机', 1),
(6, 'BG-LB-001', '工作服', 4, 'L/XL/XXL', '件', '定制', '本地', 150.00, 200.00, 20, 200, '夏季工作服', 1),
(7, 'DQ-KT-001', '壁挂空调', 6, '1.5匹/冷暖', '台', '格力', '广东', 2800.00, 3200.00, 5, 30, '壁挂式空调', 1),
(8, 'JJ-ZY-001', '办公桌', 9, '140*70cm', '张', '圣奥', '浙江', 800.00, 1000.00, 10, 100, '标准办公桌', 1),
(9, 'JJ-ZY-002', '办公椅', 9, '网布/可升降', '把', '西昊', '广东', 350.00, 450.00, 20, 200, '人体工学办公椅', 1),
(10, 'QJ-JJ-001', '消毒液', 12, '500ml/瓶', '瓶', '滴露', '英国', 18.00, 25.00, 30, 300, '84消毒液', 1);

-- 初始化库存
INSERT IGNORE INTO material_stock (id, material_id, warehouse_id, quantity, available_quantity, locked_quantity) VALUES
(1, 1, 1, 200, 200, 0),
(2, 1, 4, 100, 100, 0),
(3, 2, 1, 500, 500, 0),
(4, 3, 1, 300, 300, 0),
(5, 4, 2, 15, 15, 0),
(6, 5, 2, 8, 8, 0),
(7, 6, 1, 100, 100, 0),
(8, 7, 2, 10, 10, 0),
(9, 8, 3, 30, 30, 0),
(10, 9, 3, 50, 50, 0),
(11, 10, 4, 200, 200, 0);

-- 初始化入库记录
INSERT IGNORE INTO material_inbound (id, inbound_no, material_id, warehouse_id, quantity, unit_price, total_amount, supplier, purchase_no, operator_id, operator_name, remark, status) VALUES
(1, 'RK-20260702-001', 1, 1, 100, 28.00, 2800.00, '得力供应商', 'CG-20260701-001', 1, '系统管理员', '常规采购入库', 1),
(2, 'RK-20260702-002', 4, 2, 5, 8500.00, 42500.00, '联想供应商', 'CG-20260701-002', 1, '系统管理员', '新采购笔记本电脑', 1);

-- 初始化出库记录
INSERT IGNORE INTO material_outbound (id, outbound_no, material_id, warehouse_id, quantity, unit_price, total_amount, receiver_dept, receiver_id, receiver_name, purpose, operator_id, operator_name, remark, status) VALUES
(1, 'CK-20260702-001', 1, 1, 20, 28.00, 560.00, '综合科', 2, '普通用户', '日常办公使用', 1, '系统管理员', '综合科领用', 1);

-- 初始化领用申请
INSERT IGNORE INTO material_apply (id, apply_no, material_id, quantity, purpose, apply_dept, apply_id, apply_name, status, approve_id, approve_name, approve_time) VALUES
(1, 'SQ-20260702-001', 2, 50, '办公室日常使用', '综合科', 2, '普通用户', 3, 1, '系统管理员', NOW());