
-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS "base_area";
CREATE TABLE "base_area" (
  "code" varchar(6) NOT NULL,
  "name" varchar(60) NOT NULL,
  "city_code" varchar(4) NOT NULL
)
;
COMMENT ON COLUMN "base_area"."name" IS '区域名称';
COMMENT ON COLUMN "base_area"."city_code" IS '城市编码';
COMMENT ON TABLE "base_area" IS '县区表';

-- ----------------------------
-- Table structure for base_city
-- ----------------------------
DROP TABLE IF EXISTS "base_city";
CREATE TABLE "base_city" (
  "code" varchar(4) NOT NULL,
  "name" varchar(60) NOT NULL,
  "province_code" varchar(2) NOT NULL
)
;
COMMENT ON COLUMN "base_city"."code" IS '城市编码';
COMMENT ON COLUMN "base_city"."name" IS '城市名称';
COMMENT ON COLUMN "base_city"."province_code" IS '省份编码';
COMMENT ON TABLE "base_city" IS '城市表';

-- ----------------------------
-- Table structure for base_dict
-- ----------------------------
DROP TABLE IF EXISTS "base_dict";
CREATE TABLE "base_dict" (
  "id" int8 NOT NULL,
  "name" varchar(50),
  "group_tag" varchar(50),
  "code" varchar(50),
  "remark" varchar(50),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "enable" bool,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "base_dict"."id" IS '主键';
COMMENT ON COLUMN "base_dict"."name" IS '名称';
COMMENT ON COLUMN "base_dict"."group_tag" IS '分类标签';
COMMENT ON COLUMN "base_dict"."code" IS '编码';
COMMENT ON COLUMN "base_dict"."remark" IS '备注';
COMMENT ON COLUMN "base_dict"."creator" IS '创建者ID';
COMMENT ON COLUMN "base_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "base_dict"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "base_dict"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "base_dict"."version" IS '版本号';
COMMENT ON COLUMN "base_dict"."enable" IS '是否启用';
COMMENT ON COLUMN "base_dict"."deleted" IS '删除标志';
COMMENT ON TABLE "base_dict" IS '字典表';

-- ----------------------------
-- Table structure for base_dict_item
-- ----------------------------
DROP TABLE IF EXISTS "base_dict_item";
CREATE TABLE "base_dict_item" (
  "id" int8 NOT NULL,
  "dict_id" int8 NOT NULL,
  "dict_code" varchar(50),
  "code" varchar(50),
  "name" varchar(50),
  "sort_no" int4,
  "enable" bool,
  "remark" varchar(50),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "base_dict_item"."id" IS '主键';
COMMENT ON COLUMN "base_dict_item"."dict_id" IS '字典ID';
COMMENT ON COLUMN "base_dict_item"."dict_code" IS '字典编码';
COMMENT ON COLUMN "base_dict_item"."code" IS '字典项编码';
COMMENT ON COLUMN "base_dict_item"."name" IS '名称';
COMMENT ON COLUMN "base_dict_item"."sort_no" IS '字典项排序';
COMMENT ON COLUMN "base_dict_item"."enable" IS '是否启用';
COMMENT ON COLUMN "base_dict_item"."remark" IS '备注';
COMMENT ON COLUMN "base_dict_item"."creator" IS '创建者ID';
COMMENT ON COLUMN "base_dict_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "base_dict_item"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "base_dict_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "base_dict_item"."version" IS '版本号';
COMMENT ON COLUMN "base_dict_item"."deleted" IS '删除标志';
COMMENT ON TABLE "base_dict_item" IS '字典项';

-- ----------------------------
-- Table structure for base_param
-- ----------------------------
DROP TABLE IF EXISTS "base_param";
CREATE TABLE "base_param" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(50) NOT NULL,
  "key" varchar(50) NOT NULL,
  "value" varchar(500) NOT NULL,
  "type" varchar(20),
  "enable" bool NOT NULL,
  "internal" bool NOT NULL,
  "remark" varchar(200)
)
;
COMMENT ON COLUMN "base_param"."id" IS '主键';
COMMENT ON COLUMN "base_param"."creator" IS '创建者ID';
COMMENT ON COLUMN "base_param"."create_time" IS '创建时间';
COMMENT ON COLUMN "base_param"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "base_param"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "base_param"."version" IS '版本号';
COMMENT ON COLUMN "base_param"."deleted" IS '删除标志';
COMMENT ON COLUMN "base_param"."name" IS '参数名称';
COMMENT ON COLUMN "base_param"."key" IS '参数键名';
COMMENT ON COLUMN "base_param"."value" IS '参数值';
COMMENT ON COLUMN "base_param"."type" IS '参数类型';
COMMENT ON COLUMN "base_param"."enable" IS '启用状态';
COMMENT ON COLUMN "base_param"."internal" IS '内置参数';
COMMENT ON COLUMN "base_param"."remark" IS '备注';
COMMENT ON TABLE "base_param" IS '系统参数';

-- ----------------------------
-- Table structure for base_province
-- ----------------------------
DROP TABLE IF EXISTS "base_province";
CREATE TABLE "base_province" (
  "code" varchar(2) NOT NULL,
  "name" varchar(30) NOT NULL
)
;
COMMENT ON COLUMN "base_province"."code" IS '省份编码';
COMMENT ON COLUMN "base_province"."name" IS '省份名称';
COMMENT ON TABLE "base_province" IS '省份表';

-- ----------------------------
-- Table structure for base_street
-- ----------------------------
DROP TABLE IF EXISTS "base_street";
CREATE TABLE "base_street" (
  "code" varchar(9) NOT NULL,
  "name" varchar(60) NOT NULL,
  "area_code" varchar(6) NOT NULL
)
;
COMMENT ON COLUMN "base_street"."code" IS '编码';
COMMENT ON COLUMN "base_street"."name" IS '街道名称';
COMMENT ON COLUMN "base_street"."area_code" IS '县区编码';
COMMENT ON TABLE "base_street" IS '街道表';

-- ----------------------------
-- Table structure for base_user_protocol
-- ----------------------------
DROP TABLE IF EXISTS "base_user_protocol";
CREATE TABLE "base_user_protocol" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(32),
  "show_name" varchar(64),
  "type" varchar(32),
  "default_protocol" bool,
  "content" text
)
;
COMMENT ON COLUMN "base_user_protocol"."id" IS '主键';
COMMENT ON COLUMN "base_user_protocol"."creator" IS '创建者ID';
COMMENT ON COLUMN "base_user_protocol"."create_time" IS '创建时间';
COMMENT ON COLUMN "base_user_protocol"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "base_user_protocol"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "base_user_protocol"."version" IS '版本号';
COMMENT ON COLUMN "base_user_protocol"."deleted" IS '删除标志';
COMMENT ON COLUMN "base_user_protocol"."name" IS '名称';
COMMENT ON COLUMN "base_user_protocol"."show_name" IS '显示名称';
COMMENT ON COLUMN "base_user_protocol"."type" IS '类型';
COMMENT ON COLUMN "base_user_protocol"."default_protocol" IS '默认协议';
COMMENT ON COLUMN "base_user_protocol"."content" IS '内容';
COMMENT ON TABLE "base_user_protocol" IS '用户协议';

-- ----------------------------
-- Table structure for iam_client
-- ----------------------------
DROP TABLE IF EXISTS "iam_client";
CREATE TABLE "iam_client" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "code" varchar(50) NOT NULL,
  "name" varchar(50) NOT NULL,
  "internal" bool NOT NULL,
  "remark" varchar(250)
)
;
COMMENT ON COLUMN "iam_client"."id" IS '主键';
COMMENT ON COLUMN "iam_client"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_client"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_client"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_client"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_client"."version" IS '版本号';
COMMENT ON COLUMN "iam_client"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_client"."code" IS '编码';
COMMENT ON COLUMN "iam_client"."name" IS '名称';
COMMENT ON COLUMN "iam_client"."internal" IS '是否系统内置';
COMMENT ON COLUMN "iam_client"."remark" IS '备注';
COMMENT ON TABLE "iam_client" IS '认证终端';

-- ----------------------------
-- Table structure for iam_perm_code
-- ----------------------------
DROP TABLE IF EXISTS "iam_perm_code";
CREATE TABLE "iam_perm_code" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "code" varchar(50),
  "name" varchar(50),
  "remark" varchar(300),
  "leaf" bool NOT NULL
)
;
COMMENT ON COLUMN "iam_perm_code"."id" IS '主键';
COMMENT ON COLUMN "iam_perm_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_perm_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_perm_code"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_perm_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_perm_code"."version" IS '版本号';
COMMENT ON COLUMN "iam_perm_code"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_perm_code"."pid" IS '父ID';
COMMENT ON COLUMN "iam_perm_code"."code" IS '权限码';
COMMENT ON COLUMN "iam_perm_code"."name" IS '名称';
COMMENT ON COLUMN "iam_perm_code"."remark" IS '备注';
COMMENT ON COLUMN "iam_perm_code"."leaf" IS '是否为叶子结点';
COMMENT ON TABLE "iam_perm_code" IS '权限码';

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS "iam_perm_menu";
CREATE TABLE "iam_perm_menu" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "client_code" varchar(50) NOT NULL,
  "title" varchar(100),
  "name" varchar(100),
  "icon" varchar(100),
  "hidden" bool NOT NULL,
  "hide_children_menu" bool NOT NULL,
  "component" varchar(300),
  "path" varchar(300),
  "redirect" varchar(300),
  "sort_no" float4,
  "root" bool NOT NULL,
  "keep_alive" bool,
  "target_outside" bool,
  "full_screen" bool,
  "remark" varchar(200)
)
;
COMMENT ON COLUMN "iam_perm_menu"."id" IS '主键';
COMMENT ON COLUMN "iam_perm_menu"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_perm_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_perm_menu"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_perm_menu"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_perm_menu"."version" IS '版本号';
COMMENT ON COLUMN "iam_perm_menu"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_perm_menu"."pid" IS '父id';
COMMENT ON COLUMN "iam_perm_menu"."client_code" IS '关联终端code';
COMMENT ON COLUMN "iam_perm_menu"."title" IS '菜单标题';
COMMENT ON COLUMN "iam_perm_menu"."name" IS '路由名称';
COMMENT ON COLUMN "iam_perm_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "iam_perm_menu"."hidden" IS '是否隐藏';
COMMENT ON COLUMN "iam_perm_menu"."hide_children_menu" IS '是否隐藏子菜单';
COMMENT ON COLUMN "iam_perm_menu"."component" IS '组件';
COMMENT ON COLUMN "iam_perm_menu"."path" IS '访问路径';
COMMENT ON COLUMN "iam_perm_menu"."redirect" IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN "iam_perm_menu"."sort_no" IS '菜单排序';
COMMENT ON COLUMN "iam_perm_menu"."root" IS '是否是一级菜单';
COMMENT ON COLUMN "iam_perm_menu"."keep_alive" IS '是否缓存页面';
COMMENT ON COLUMN "iam_perm_menu"."target_outside" IS '是否为外部打开';
COMMENT ON COLUMN "iam_perm_menu"."full_screen" IS '是否全屏打开';
COMMENT ON COLUMN "iam_perm_menu"."remark" IS '描述';
COMMENT ON TABLE "iam_perm_menu" IS '菜单权限';

-- ----------------------------
-- Table structure for iam_perm_path
-- ----------------------------
DROP TABLE IF EXISTS "iam_perm_path";
CREATE TABLE "iam_perm_path" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "parent_code" varchar(50),
  "client_code" varchar(50) NOT NULL,
  "code" varchar(50) NOT NULL,
  "name" varchar(50),
  "leaf" bool NOT NULL,
  "path" varchar(200),
  "method" varchar(10)
)
;
COMMENT ON COLUMN "iam_perm_path"."id" IS '主键';
COMMENT ON COLUMN "iam_perm_path"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_perm_path"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_perm_path"."parent_code" IS '上级编码';
COMMENT ON COLUMN "iam_perm_path"."client_code" IS '终端编码';
COMMENT ON COLUMN "iam_perm_path"."code" IS '标识编码(模块、分组标识)';
COMMENT ON COLUMN "iam_perm_path"."name" IS '名称(请求路径、模块、分组名称)';
COMMENT ON COLUMN "iam_perm_path"."leaf" IS '叶子节点';
COMMENT ON COLUMN "iam_perm_path"."path" IS '请求路径';
COMMENT ON COLUMN "iam_perm_path"."method" IS '请求类型, 为全大写单词';
COMMENT ON TABLE "iam_perm_path" IS '请求权限(url)';

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS "iam_role";
CREATE TABLE "iam_role" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "code" varchar(50),
  "name" varchar(100),
  "internal" bool,
  "remark" varchar(255)
)
;
COMMENT ON COLUMN "iam_role"."id" IS '主键';
COMMENT ON COLUMN "iam_role"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_role"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_role"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_role"."version" IS '版本号';
COMMENT ON COLUMN "iam_role"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_role"."pid" IS '父级Id';
COMMENT ON COLUMN "iam_role"."code" IS '编码';
COMMENT ON COLUMN "iam_role"."name" IS '名称';
COMMENT ON COLUMN "iam_role"."internal" IS '是否系统内置';
COMMENT ON COLUMN "iam_role"."remark" IS '备注';
COMMENT ON TABLE "iam_role" IS '角色';

-- ----------------------------
-- Table structure for iam_role_code
-- ----------------------------
DROP TABLE IF EXISTS "iam_role_code";
CREATE TABLE "iam_role_code" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "code_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "iam_role_code"."id" IS '主键';
COMMENT ON COLUMN "iam_role_code"."role_id" IS '角色id';
COMMENT ON COLUMN "iam_role_code"."code_id" IS '权限码';
COMMENT ON TABLE "iam_role_code" IS '角色权限码关联关系';

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "iam_role_menu";
CREATE TABLE "iam_role_menu" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) NOT NULL,
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "iam_role_menu"."id" IS '主键';
COMMENT ON COLUMN "iam_role_menu"."role_id" IS '角色id';
COMMENT ON COLUMN "iam_role_menu"."client_code" IS '终端编码';
COMMENT ON COLUMN "iam_role_menu"."menu_id" IS '菜单id';
COMMENT ON TABLE "iam_role_menu" IS '角色菜单关联关系';

-- ----------------------------
-- Table structure for iam_role_path
-- ----------------------------
DROP TABLE IF EXISTS "iam_role_path";
CREATE TABLE "iam_role_path" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) NOT NULL,
  "path_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "iam_role_path"."id" IS '主键';
COMMENT ON COLUMN "iam_role_path"."role_id" IS '角色id';
COMMENT ON COLUMN "iam_role_path"."client_code" IS '终端编码';
COMMENT ON COLUMN "iam_role_path"."path_id" IS '请求资源id';
COMMENT ON TABLE "iam_role_path" IS '角色路径关联关系';

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS "iam_user_expand_info";
CREATE TABLE "iam_user_expand_info" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "sex" varchar(10),
  "avatar" varchar(300),
  "birthday" date,
  "last_login_time" timestamp(0),
  "register_time" timestamp(6),
  "current_login_time" timestamp(6),
  "initial_password" bool,
  "expire_password" bool,
  "last_change_password_time" timestamp(6)
)
;
COMMENT ON COLUMN "iam_user_expand_info"."id" IS '主键';
COMMENT ON COLUMN "iam_user_expand_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_user_expand_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_user_expand_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_user_expand_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_user_expand_info"."version" IS '版本号';
COMMENT ON COLUMN "iam_user_expand_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_user_expand_info"."sex" IS '性别';
COMMENT ON COLUMN "iam_user_expand_info"."avatar" IS '头像图片url';
COMMENT ON COLUMN "iam_user_expand_info"."birthday" IS '生日';
COMMENT ON COLUMN "iam_user_expand_info"."last_login_time" IS '上次登录时间';
COMMENT ON COLUMN "iam_user_expand_info"."register_time" IS '注册时间';
COMMENT ON COLUMN "iam_user_expand_info"."current_login_time" IS '本次登录时间';
COMMENT ON COLUMN "iam_user_expand_info"."initial_password" IS '是否初始密码';
COMMENT ON COLUMN "iam_user_expand_info"."expire_password" IS '密码是否过期';
COMMENT ON COLUMN "iam_user_expand_info"."last_change_password_time" IS '上次修改密码时间';
COMMENT ON TABLE "iam_user_expand_info" IS '用户扩展信息';

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS "iam_user_info";
CREATE TABLE "iam_user_info" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(50),
  "account" varchar(50) NOT NULL,
  "password" varchar(120) NOT NULL,
  "phone" varchar(50),
  "email" varchar(50),
  "administrator" bool NOT NULL,
  "status" varchar(50)
)
;
COMMENT ON COLUMN "iam_user_info"."id" IS '主键';
COMMENT ON COLUMN "iam_user_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "iam_user_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "iam_user_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "iam_user_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "iam_user_info"."version" IS '版本号';
COMMENT ON COLUMN "iam_user_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "iam_user_info"."name" IS '名称';
COMMENT ON COLUMN "iam_user_info"."account" IS '账号';
COMMENT ON COLUMN "iam_user_info"."password" IS '密码';
COMMENT ON COLUMN "iam_user_info"."phone" IS '手机号';
COMMENT ON COLUMN "iam_user_info"."email" IS '邮箱';
COMMENT ON COLUMN "iam_user_info"."administrator" IS '是否管理员';
COMMENT ON COLUMN "iam_user_info"."status" IS '账号状态';
COMMENT ON TABLE "iam_user_info" IS '用户核心信息';

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS "iam_user_role";
CREATE TABLE "iam_user_role" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "iam_user_role"."id" IS '主键';
COMMENT ON COLUMN "iam_user_role"."user_id" IS '用户';
COMMENT ON COLUMN "iam_user_role"."role_id" IS '角色';
COMMENT ON TABLE "iam_user_role" IS '用户角色关联关系';

-- ----------------------------
-- Table structure for pay_aggregate_bar_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_aggregate_bar_pay_config";
CREATE TABLE "pay_aggregate_bar_pay_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "bar_pay_type" varchar(32),
  "channel" varchar(32),
  "pay_method" varchar(32),
  "other_method" varchar(32),
  "mch_no" varchar(32),
  "agent_no" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."id" IS '主键';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."bar_pay_type" IS '付款码类型';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."agent_no" IS '所属代理商';
COMMENT ON COLUMN "pay_aggregate_bar_pay_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_aggregate_bar_pay_config" IS '网关聚合付款码支付配置';

-- ----------------------------
-- Table structure for pay_aggregate_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_aggregate_pay_config";
CREATE TABLE "pay_aggregate_pay_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "aggregate_type" varchar(32),
  "channel" varchar(32),
  "pay_method" varchar(32),
  "auto_launch" bool,
  "other_method" varchar(32),
  "mch_no" varchar(32),
  "need_open_id" bool,
  "call_type" varchar(32),
  "open_id_get_type" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_aggregate_pay_config"."id" IS '主键';
COMMENT ON COLUMN "pay_aggregate_pay_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_aggregate_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_aggregate_pay_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_aggregate_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_aggregate_pay_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_aggregate_pay_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_aggregate_pay_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_aggregate_pay_config"."aggregate_type" IS '聚合支付类型';
COMMENT ON COLUMN "pay_aggregate_pay_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_aggregate_pay_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_aggregate_pay_config"."auto_launch" IS '自动拉起支付';
COMMENT ON COLUMN "pay_aggregate_pay_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_aggregate_pay_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_aggregate_pay_config"."need_open_id" IS '需要获取OpenId';
COMMENT ON COLUMN "pay_aggregate_pay_config"."call_type" IS '调用方式';
COMMENT ON COLUMN "pay_aggregate_pay_config"."open_id_get_type" IS 'OpenId获取方式';
COMMENT ON COLUMN "pay_aggregate_pay_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_aggregate_pay_config"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_aggregate_pay_config" IS '网关聚合支付配置';

-- ----------------------------
-- Table structure for pay_api_const
-- ----------------------------
DROP TABLE IF EXISTS "pay_api_const";
CREATE TABLE "pay_api_const" (
  "id" int8 NOT NULL,
  "code" varchar(50) NOT NULL,
  "name" varchar(50) NOT NULL,
  "api" varchar(200) NOT NULL,
  "enable" bool,
  "remark" varchar(200)
)
;
COMMENT ON COLUMN "pay_api_const"."id" IS '主键';
COMMENT ON COLUMN "pay_api_const"."code" IS '编码';
COMMENT ON COLUMN "pay_api_const"."name" IS '接口名称';
COMMENT ON COLUMN "pay_api_const"."api" IS '接口地址';
COMMENT ON COLUMN "pay_api_const"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_api_const"."remark" IS '备注';
COMMENT ON TABLE "pay_api_const" IS '支付接口常量';

-- ----------------------------
-- Table structure for pay_audio_device
-- ----------------------------
DROP TABLE IF EXISTS "pay_audio_device";
CREATE TABLE "pay_audio_device" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(0),
  "last_modifier" int8,
  "last_modified_time" timestamp(0),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "isv_no" varchar(32),
  "agent_no" varchar(32),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "device_no" varchar(32),
  "batch_no" varchar(32),
  "bind_type" varchar(32),
  "cashier_code_id" int8,
  "vendor" varchar(32),
  "config_id" int8,
  "enable" bool,
  "name" varchar(64)
)
;
COMMENT ON COLUMN "pay_audio_device"."id" IS '主键';
COMMENT ON COLUMN "pay_audio_device"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_audio_device"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_audio_device"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_audio_device"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_audio_device"."version" IS '版本号';
COMMENT ON COLUMN "pay_audio_device"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_audio_device"."isv_no" IS '服务商号';
COMMENT ON COLUMN "pay_audio_device"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_audio_device"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_audio_device"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_audio_device"."device_no" IS '设备号';
COMMENT ON COLUMN "pay_audio_device"."batch_no" IS '批次号';
COMMENT ON COLUMN "pay_audio_device"."bind_type" IS '绑定类型';
COMMENT ON COLUMN "pay_audio_device"."cashier_code_id" IS '绑定码牌ID';
COMMENT ON COLUMN "pay_audio_device"."vendor" IS '设备厂商';
COMMENT ON COLUMN "pay_audio_device"."config_id" IS '配置ID';
COMMENT ON COLUMN "pay_audio_device"."enable" IS '状态';
COMMENT ON COLUMN "pay_audio_device"."name" IS '设备名称';
COMMENT ON TABLE "pay_audio_device" IS '音箱设备';

-- ----------------------------
-- Table structure for pay_audio_device_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_audio_device_config";
CREATE TABLE "pay_audio_device_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(50),
  "vendor" varchar(32),
  "enable" bool NOT NULL,
  "config" varchar(5000)
)
;
COMMENT ON COLUMN "pay_audio_device_config"."id" IS '主键';
COMMENT ON COLUMN "pay_audio_device_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_audio_device_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_audio_device_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_audio_device_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_audio_device_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_audio_device_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_audio_device_config"."name" IS '码牌名称';
COMMENT ON COLUMN "pay_audio_device_config"."vendor" IS '厂商';
COMMENT ON COLUMN "pay_audio_device_config"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_audio_device_config"."config" IS '配置Json';
COMMENT ON TABLE "pay_audio_device_config" IS '音箱厂商配置';

-- ----------------------------
-- Table structure for pay_cashier_code
-- ----------------------------
DROP TABLE IF EXISTS "pay_cashier_code";
CREATE TABLE "pay_cashier_code" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(0),
  "last_modifier" int8,
  "last_modified_time" timestamp(0),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "isv_no" varchar(32),
  "agent_no" varchar(32),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "amount_type" varchar(16),
  "amount" numeric(16,4),
  "name" varchar(64),
  "code" varchar(32),
  "config_id" int8,
  "template_id" int8,
  "enable" bool,
  "batch_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_cashier_code"."id" IS '主键';
COMMENT ON COLUMN "pay_cashier_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_cashier_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_cashier_code"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_cashier_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_cashier_code"."version" IS '版本号';
COMMENT ON COLUMN "pay_cashier_code"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_cashier_code"."isv_no" IS '服务商号';
COMMENT ON COLUMN "pay_cashier_code"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_cashier_code"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_cashier_code"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_cashier_code"."amount_type" IS '金额类型';
COMMENT ON COLUMN "pay_cashier_code"."amount" IS '金额';
COMMENT ON COLUMN "pay_cashier_code"."name" IS '名称';
COMMENT ON COLUMN "pay_cashier_code"."code" IS '编号';
COMMENT ON COLUMN "pay_cashier_code"."config_id" IS '配置Id';
COMMENT ON COLUMN "pay_cashier_code"."template_id" IS '模板ID';
COMMENT ON COLUMN "pay_cashier_code"."enable" IS '状态';
COMMENT ON COLUMN "pay_cashier_code"."batch_no" IS '批次号';
COMMENT ON TABLE "pay_cashier_code" IS '收款码牌';

-- ----------------------------
-- Table structure for pay_cashier_code_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_cashier_code_config";
CREATE TABLE "pay_cashier_code_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "name" varchar(50),
  "remark" varchar(200),
  "enable" bool NOT NULL,
  "deleted" bool NOT NULL,
  "allocation" bool,
  "auto_allocation" bool,
  "limit_pay" varchar(32)
)
;
COMMENT ON COLUMN "pay_cashier_code_config"."id" IS '主键';
COMMENT ON COLUMN "pay_cashier_code_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_cashier_code_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_cashier_code_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_cashier_code_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_cashier_code_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_cashier_code_config"."name" IS '码牌名称';
COMMENT ON COLUMN "pay_cashier_code_config"."remark" IS '备注';
COMMENT ON COLUMN "pay_cashier_code_config"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_cashier_code_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_cashier_code_config"."allocation" IS '是否开启分账';
COMMENT ON COLUMN "pay_cashier_code_config"."auto_allocation" IS '自动分账';
COMMENT ON COLUMN "pay_cashier_code_config"."limit_pay" IS '限制可用的支付方式';
COMMENT ON TABLE "pay_cashier_code_config" IS '收银码牌配置';

-- ----------------------------
-- Table structure for pay_cashier_code_scene_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_cashier_code_scene_config";
CREATE TABLE "pay_cashier_code_scene_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "config_id" int8 NOT NULL,
  "scene" varchar(32) NOT NULL,
  "channel" varchar(32),
  "pay_method" varchar(32),
  "mch_no" varchar(32),
  "deleted" bool NOT NULL,
  "need_open_id" bool,
  "call_type" varchar(32),
  "other_method" varchar(32),
  "open_id_get_type" varchar(32)
)
;
COMMENT ON COLUMN "pay_cashier_code_scene_config"."id" IS '主键';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."config_id" IS '码牌ID';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."scene" IS '收银场景';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."need_open_id" IS '需要获取OpenId';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."call_type" IS '发起调用的类型';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_cashier_code_scene_config"."open_id_get_type" IS 'OpenId获取方式';
COMMENT ON TABLE "pay_cashier_code_scene_config" IS '各类型码牌配置';

-- ----------------------------
-- Table structure for pay_cashier_group_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_cashier_group_config";
CREATE TABLE "pay_cashier_group_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "cashier_type" varchar(32),
  "name" varchar(50),
  "icon" varchar(100),
  "sort_no" numeric(16,4),
  "recommend" bool,
  "mch_no" varchar(32),
  "bg_color" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_cashier_group_config"."id" IS '主键';
COMMENT ON COLUMN "pay_cashier_group_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_cashier_group_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_cashier_group_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_cashier_group_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_cashier_group_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_cashier_group_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_cashier_group_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_cashier_group_config"."cashier_type" IS '网关收银台类型';
COMMENT ON COLUMN "pay_cashier_group_config"."name" IS '名称';
COMMENT ON COLUMN "pay_cashier_group_config"."icon" IS '图标';
COMMENT ON COLUMN "pay_cashier_group_config"."sort_no" IS '排序';
COMMENT ON COLUMN "pay_cashier_group_config"."recommend" IS '是否推荐';
COMMENT ON COLUMN "pay_cashier_group_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_cashier_group_config"."bg_color" IS '背景色';
COMMENT ON COLUMN "pay_cashier_group_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_cashier_group_config"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_cashier_group_config" IS '网关收银台类目配置';

-- ----------------------------
-- Table structure for pay_cashier_item_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_cashier_item_config";
CREATE TABLE "pay_cashier_item_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "group_id" int8,
  "channel" varchar(32),
  "pay_method" varchar(32),
  "name" varchar(50),
  "icon" varchar(100),
  "sort_no" numeric(16,4),
  "call_type" varchar(32),
  "recommend" bool,
  "bg_color" varchar(32),
  "border_color" varchar(32),
  "font_color" varchar(32),
  "other_method" varchar(32),
  "mch_no" varchar(32),
  "pay_env_types" varchar(5000),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_cashier_item_config"."id" IS '主键';
COMMENT ON COLUMN "pay_cashier_item_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_cashier_item_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_cashier_item_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_cashier_item_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_cashier_item_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_cashier_item_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_cashier_item_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_cashier_item_config"."group_id" IS '分组配置ID';
COMMENT ON COLUMN "pay_cashier_item_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_cashier_item_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_cashier_item_config"."name" IS '名称';
COMMENT ON COLUMN "pay_cashier_item_config"."icon" IS '图标';
COMMENT ON COLUMN "pay_cashier_item_config"."sort_no" IS '排序';
COMMENT ON COLUMN "pay_cashier_item_config"."call_type" IS '调用方式';
COMMENT ON COLUMN "pay_cashier_item_config"."recommend" IS '是否推荐';
COMMENT ON COLUMN "pay_cashier_item_config"."bg_color" IS '背景色';
COMMENT ON COLUMN "pay_cashier_item_config"."border_color" IS '边框色';
COMMENT ON COLUMN "pay_cashier_item_config"."font_color" IS '字体颜色';
COMMENT ON COLUMN "pay_cashier_item_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_cashier_item_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_cashier_item_config"."pay_env_types" IS '生效的支付环境列表';
COMMENT ON COLUMN "pay_cashier_item_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_cashier_item_config"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_cashier_item_config" IS '网关收银台配置项';

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_channel_config";
CREATE TABLE "pay_channel_config" (
  "id" int8 NOT NULL,
  "channel" varchar(32),
  "out_mch_no" varchar(128),
  "out_app_id" varchar(256),
  "enable" bool,
  "ext" text,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "app_id" varchar(32) NOT NULL,
  "isv_no" varchar(64),
  "sub" bool,
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_channel_config"."id" IS '主键';
COMMENT ON COLUMN "pay_channel_config"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_channel_config"."out_mch_no" IS '通道商户号';
COMMENT ON COLUMN "pay_channel_config"."out_app_id" IS '通道APPID';
COMMENT ON COLUMN "pay_channel_config"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_channel_config"."ext" IS '扩展存储';
COMMENT ON COLUMN "pay_channel_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_channel_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_channel_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_channel_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_channel_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_channel_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_channel_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_channel_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_channel_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_channel_config"."sub" IS '是否属于特约商户';
COMMENT ON COLUMN "pay_channel_config"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_channel_config" IS '支付通道配置';

-- ----------------------------
-- Table structure for pay_channel_const
-- ----------------------------
DROP TABLE IF EXISTS "pay_channel_const";
CREATE TABLE "pay_channel_const" (
  "id" int8 NOT NULL,
  "code" varchar(32) NOT NULL,
  "name" varchar(32) NOT NULL,
  "enable" bool NOT NULL DEFAULT false,
  "isv" bool NOT NULL DEFAULT false,
  "allocatable" bool NOT NULL DEFAULT false,
  "terminal" bool NOT NULL DEFAULT false,
  "apply" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "pay_channel_const"."id" IS '主键';
COMMENT ON COLUMN "pay_channel_const"."code" IS '通道编码';
COMMENT ON COLUMN "pay_channel_const"."name" IS '通道名称';
COMMENT ON COLUMN "pay_channel_const"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_channel_const"."isv" IS '是否支持服务商模式';
COMMENT ON COLUMN "pay_channel_const"."allocatable" IS '是否支持分账';
COMMENT ON COLUMN "pay_channel_const"."terminal" IS '终端报备';
COMMENT ON COLUMN "pay_channel_const"."apply" IS '进件申请';
COMMENT ON TABLE "pay_channel_const" IS '支付通道常量';

-- ----------------------------
-- Table structure for pay_channel_terminal
-- ----------------------------
DROP TABLE IF EXISTS "pay_channel_terminal";
CREATE TABLE "pay_channel_terminal" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "name" varchar(64),
  "terminal_id" int8,
  "terminal_no" varchar(64),
  "channel" varchar(32),
  "status" varchar(32),
  "out_terminal_no" varchar(64),
  "error_msg" varchar(500),
  "extra" varchar(5000),
  "type" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_channel_terminal"."id" IS '主键';
COMMENT ON COLUMN "pay_channel_terminal"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_channel_terminal"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_channel_terminal"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_channel_terminal"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_channel_terminal"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_channel_terminal"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_channel_terminal"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_channel_terminal"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_channel_terminal"."name" IS '终端名称';
COMMENT ON COLUMN "pay_channel_terminal"."terminal_id" IS '终端ID';
COMMENT ON COLUMN "pay_channel_terminal"."terminal_no" IS '终端编码';
COMMENT ON COLUMN "pay_channel_terminal"."channel" IS '通道';
COMMENT ON COLUMN "pay_channel_terminal"."status" IS '状态';
COMMENT ON COLUMN "pay_channel_terminal"."out_terminal_no" IS '通道终端号';
COMMENT ON COLUMN "pay_channel_terminal"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_channel_terminal"."extra" IS '扩展信息';
COMMENT ON COLUMN "pay_channel_terminal"."type" IS '报送类型';
COMMENT ON COLUMN "pay_channel_terminal"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_channel_terminal"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_channel_terminal" IS '通道终端设备上报记录';

-- ----------------------------
-- Table structure for pay_close_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_close_record";
CREATE TABLE "pay_close_record" (
  "id" int8 NOT NULL,
  "order_no" varchar(32) NOT NULL,
  "biz_order_no" varchar(100) NOT NULL,
  "channel" varchar(20),
  "close_type" varchar(20) NOT NULL,
  "closed" bool NOT NULL,
  "error_code" varchar(10),
  "error_msg" varchar(500),
  "client_ip" varchar(64),
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_close_record"."id" IS '主键';
COMMENT ON COLUMN "pay_close_record"."order_no" IS '支付订单号';
COMMENT ON COLUMN "pay_close_record"."biz_order_no" IS '商户支付订单号';
COMMENT ON COLUMN "pay_close_record"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_close_record"."close_type" IS '关闭类型';
COMMENT ON COLUMN "pay_close_record"."closed" IS '是否关闭成功';
COMMENT ON COLUMN "pay_close_record"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_close_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_close_record"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "pay_close_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_close_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_close_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_close_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_close_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_close_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_close_record" IS '支付关闭记录';

-- ----------------------------
-- Table structure for pay_gateway_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_gateway_config";
CREATE TABLE "pay_gateway_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "name" varchar(55),
  "aggregate_show" bool,
  "h5_auto_upgrade" bool,
  "mch_no" varchar(32),
  "bar_pay_show" bool,
  "mini_app_allocation" bool,
  "mini_app_auto_allocation" bool,
  "mini_app_limit_pay" varchar(32),
  "mini_app_terminal_no" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32),
  "read_system" bool
)
;
COMMENT ON COLUMN "pay_gateway_config"."id" IS '主键';
COMMENT ON COLUMN "pay_gateway_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_gateway_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_gateway_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_gateway_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_gateway_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_gateway_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_gateway_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_gateway_config"."name" IS '收银台名称';
COMMENT ON COLUMN "pay_gateway_config"."aggregate_show" IS 'PC收银台是否同时显示聚合收银码';
COMMENT ON COLUMN "pay_gateway_config"."h5_auto_upgrade" IS 'h5收银台自动升级聚合支付';
COMMENT ON COLUMN "pay_gateway_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_gateway_config"."bar_pay_show" IS 'PC收银台是否显示聚合条码支付';
COMMENT ON COLUMN "pay_gateway_config"."mini_app_allocation" IS '小程序开启分账';
COMMENT ON COLUMN "pay_gateway_config"."mini_app_auto_allocation" IS '小程序自动分账';
COMMENT ON COLUMN "pay_gateway_config"."mini_app_limit_pay" IS '限制小程序支付方式';
COMMENT ON COLUMN "pay_gateway_config"."mini_app_terminal_no" IS '小程序终端号';
COMMENT ON COLUMN "pay_gateway_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_gateway_config"."agent_no" IS '所属代理商';
COMMENT ON COLUMN "pay_gateway_config"."read_system" IS '读取系统配置';
COMMENT ON TABLE "pay_gateway_config" IS '网关收银配置';

-- ----------------------------
-- Table structure for pay_isv_aggregate_bar_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_aggregate_bar_pay_config";
CREATE TABLE "pay_isv_aggregate_bar_pay_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "bar_pay_type" varchar(32),
  "channel" varchar(32),
  "pay_method" varchar(32),
  "other_method" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."bar_pay_type" IS '付款码类型';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_isv_aggregate_bar_pay_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_isv_aggregate_bar_pay_config" IS '网关聚合付款码支付配置';

-- ----------------------------
-- Table structure for pay_isv_aggregate_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_aggregate_pay_config";
CREATE TABLE "pay_isv_aggregate_pay_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "aggregate_type" varchar(32),
  "channel" varchar(32),
  "pay_method" varchar(32),
  "auto_launch" bool,
  "other_method" varchar(32),
  "mch_no" varchar(32),
  "need_open_id" bool,
  "call_type" varchar(32),
  "open_id_get_type" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."aggregate_type" IS '聚合支付类型';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."auto_launch" IS '自动拉起支付';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."need_open_id" IS '需要获取OpenId';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."call_type" IS '调用方式';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."open_id_get_type" IS 'OpenId获取方式';
COMMENT ON COLUMN "pay_isv_aggregate_pay_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_isv_aggregate_pay_config" IS '网关聚合支付配置';

-- ----------------------------
-- Table structure for pay_isv_cashier_group_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_cashier_group_config";
CREATE TABLE "pay_isv_cashier_group_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "cashier_type" varchar(32),
  "name" varchar(50),
  "icon" varchar(100),
  "sort_no" numeric(16,4),
  "recommend" bool,
  "mch_no" varchar(32),
  "bg_color" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_isv_cashier_group_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."cashier_type" IS '网关收银台类型';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."name" IS '名称';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."icon" IS '图标';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."sort_no" IS '排序';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."recommend" IS '是否推荐';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."bg_color" IS '背景色';
COMMENT ON COLUMN "pay_isv_cashier_group_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_isv_cashier_group_config" IS '网关收银台类目配置';

-- ----------------------------
-- Table structure for pay_isv_cashier_item_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_cashier_item_config";
CREATE TABLE "pay_isv_cashier_item_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "group_id" int8,
  "channel" varchar(32),
  "pay_method" varchar(32),
  "name" varchar(50),
  "icon" varchar(100),
  "sort_no" numeric(16,4),
  "call_type" varchar(32),
  "recommend" bool,
  "bg_color" varchar(32),
  "border_color" varchar(32),
  "font_color" varchar(32),
  "other_method" varchar(32),
  "mch_no" varchar(32),
  "pay_env_types" varchar(5000),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_isv_cashier_item_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."group_id" IS '分组配置ID';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."channel" IS '通道';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."pay_method" IS '支付方式';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."name" IS '名称';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."icon" IS '图标';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."sort_no" IS '排序';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."call_type" IS '调用方式';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."recommend" IS '是否推荐';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."bg_color" IS '背景色';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."border_color" IS '边框色';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."font_color" IS '字体颜色';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."pay_env_types" IS '生效的支付环境列表';
COMMENT ON COLUMN "pay_isv_cashier_item_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_isv_cashier_item_config" IS '网关收银台配置项';

-- ----------------------------
-- Table structure for pay_isv_channel_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_channel_config";
CREATE TABLE "pay_isv_channel_config" (
  "id" int8 NOT NULL,
  "channel" varchar(32),
  "out_isv_no" varchar(32),
  "enable" bool,
  "ext" text,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "isv_no" varchar(32) NOT NULL
)
;
COMMENT ON COLUMN "pay_isv_channel_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_channel_config"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_isv_channel_config"."out_isv_no" IS '通道服务商号';
COMMENT ON COLUMN "pay_isv_channel_config"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_isv_channel_config"."ext" IS '扩展存储';
COMMENT ON COLUMN "pay_isv_channel_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_channel_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_channel_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_isv_channel_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_channel_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_isv_channel_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_channel_config"."isv_no" IS '服务商号';
COMMENT ON TABLE "pay_isv_channel_config" IS '通道支付配置';

-- ----------------------------
-- Table structure for pay_isv_gateway_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_isv_gateway_config";
CREATE TABLE "pay_isv_gateway_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "app_id" varchar(32),
  "name" varchar(55),
  "aggregate_show" bool,
  "h5_auto_upgrade" bool,
  "mch_no" varchar(32),
  "bar_pay_show" bool,
  "mini_app_allocation" bool,
  "mini_app_auto_allocation" bool,
  "mini_app_limit_pay" varchar(32),
  "mini_app_terminal_no" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_isv_gateway_config"."id" IS '主键';
COMMENT ON COLUMN "pay_isv_gateway_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_isv_gateway_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_isv_gateway_config"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_isv_gateway_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_isv_gateway_config"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_isv_gateway_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_isv_gateway_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_isv_gateway_config"."name" IS '收银台名称';
COMMENT ON COLUMN "pay_isv_gateway_config"."aggregate_show" IS 'PC收银台是否同时显示聚合收银码';
COMMENT ON COLUMN "pay_isv_gateway_config"."h5_auto_upgrade" IS 'h5收银台自动升级聚合支付';
COMMENT ON COLUMN "pay_isv_gateway_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_isv_gateway_config"."bar_pay_show" IS 'PC收银台是否显示聚合条码支付';
COMMENT ON COLUMN "pay_isv_gateway_config"."mini_app_allocation" IS '小程序开启分账';
COMMENT ON COLUMN "pay_isv_gateway_config"."mini_app_auto_allocation" IS '小程序自动分账';
COMMENT ON COLUMN "pay_isv_gateway_config"."mini_app_limit_pay" IS '限制小程序支付方式';
COMMENT ON COLUMN "pay_isv_gateway_config"."mini_app_terminal_no" IS '小程序终端号';
COMMENT ON COLUMN "pay_isv_gateway_config"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_isv_gateway_config" IS '服务商网关收银配置';

-- ----------------------------
-- Table structure for pay_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "pay_mch_app";
CREATE TABLE "pay_mch_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "app_name" varchar(64),
  "sign_type" varchar(32),
  "sign_secret" varchar(500),
  "req_sign" bool,
  "limit_amount" numeric(16,4),
  "status" varchar(32),
  "notify_type" varchar(32),
  "notify_url" varchar(200),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "order_timeout" int2,
  "merchant_type" varchar(32),
  "req_timeout" bool,
  "req_timeout_second" int4,
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_mch_app"."id" IS '主键';
COMMENT ON COLUMN "pay_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_mch_app"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "pay_mch_app"."sign_type" IS '签名方式';
COMMENT ON COLUMN "pay_mch_app"."sign_secret" IS '签名秘钥';
COMMENT ON COLUMN "pay_mch_app"."req_sign" IS '是否对请求进行验签';
COMMENT ON COLUMN "pay_mch_app"."limit_amount" IS '支付限额(元)';
COMMENT ON COLUMN "pay_mch_app"."status" IS '应用状态';
COMMENT ON COLUMN "pay_mch_app"."notify_type" IS '异步消息通知类型';
COMMENT ON COLUMN "pay_mch_app"."notify_url" IS '通知地址';
COMMENT ON COLUMN "pay_mch_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_mch_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_mch_app"."version" IS '版本号';
COMMENT ON COLUMN "pay_mch_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_mch_app"."order_timeout" IS '订单默认超时时间(分钟)';
COMMENT ON COLUMN "pay_mch_app"."merchant_type" IS '商户类型';
COMMENT ON COLUMN "pay_mch_app"."req_timeout" IS '是否验证请求时间是否超时';
COMMENT ON COLUMN "pay_mch_app"."req_timeout_second" IS '请求超时时间(秒)';
COMMENT ON COLUMN "pay_mch_app"."isv_no" IS '服务商号';
COMMENT ON COLUMN "pay_mch_app"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_mch_app" IS '商户应用';

-- ----------------------------
-- Table structure for pay_merchant
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant";
CREATE TABLE "pay_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) NOT NULL,
  "mch_name" varchar(100) NOT NULL,
  "company_name" varchar(100),
  "id_type" varchar(32),
  "id_no" varchar,
  "contact" varchar(100),
  "legal_person" varchar(100),
  "status" varchar(10),
  "creator" int8,
  "create_time" timestamp(0),
  "last_modifier" int8,
  "last_modified_time" timestamp(0),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "company_contact" varchar(150),
  "company_address" varchar(150),
  "company_code" varchar(50),
  "administrator" bool NOT NULL,
  "admin_user_id" int8,
  "isv_no" varchar(32),
  "merchant_type" varchar(32),
  "agent_no" varchar(32),
  "mch_short_name" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant"."mch_name" IS '商户名称';
COMMENT ON COLUMN "pay_merchant"."company_name" IS '公司名称';
COMMENT ON COLUMN "pay_merchant"."id_type" IS '法人证件类型';
COMMENT ON COLUMN "pay_merchant"."id_no" IS '法人证件号';
COMMENT ON COLUMN "pay_merchant"."contact" IS '法人联系方式';
COMMENT ON COLUMN "pay_merchant"."legal_person" IS '法人名称';
COMMENT ON COLUMN "pay_merchant"."status" IS '状态';
COMMENT ON COLUMN "pay_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_merchant"."version" IS '版本号';
COMMENT ON COLUMN "pay_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_merchant"."company_contact" IS '公司联系方式';
COMMENT ON COLUMN "pay_merchant"."company_address" IS '公司地址';
COMMENT ON COLUMN "pay_merchant"."company_code" IS '公司信用编码';
COMMENT ON COLUMN "pay_merchant"."administrator" IS '是否有关联管理员';
COMMENT ON COLUMN "pay_merchant"."admin_user_id" IS '关联管理员用户';
COMMENT ON COLUMN "pay_merchant"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant"."merchant_type" IS '商户类型';
COMMENT ON COLUMN "pay_merchant"."agent_no" IS '所属代理商';
COMMENT ON COLUMN "pay_merchant"."mch_short_name" IS '商户简称';
COMMENT ON TABLE "pay_merchant" IS '商户';

-- ----------------------------
-- Table structure for pay_merchant_callback_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_callback_record";
CREATE TABLE "pay_merchant_callback_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "task_id" int8,
  "req_count" int4,
  "success" bool,
  "send_type" varchar(30),
  "error_code" varchar(50),
  "error_msg" varchar(500),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_callback_record"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_callback_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_callback_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_callback_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_callback_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_merchant_callback_record"."task_id" IS '任务ID';
COMMENT ON COLUMN "pay_merchant_callback_record"."req_count" IS '请求次数';
COMMENT ON COLUMN "pay_merchant_callback_record"."success" IS '发送是否成功';
COMMENT ON COLUMN "pay_merchant_callback_record"."send_type" IS '发送类型, 自动发送, 手动发送';
COMMENT ON COLUMN "pay_merchant_callback_record"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_merchant_callback_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_merchant_callback_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant_callback_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_merchant_callback_record" IS '客户回调消息发送记录';

-- ----------------------------
-- Table structure for pay_merchant_callback_task
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_callback_task";
CREATE TABLE "pay_merchant_callback_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "trade_id" int8,
  "trade_no" varchar(32),
  "trade_type" varchar(20),
  "content" text,
  "success" bool,
  "next_time" timestamp(6),
  "send_count" int4,
  "delay_count" int4,
  "latest_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "url" varchar(200),
  "isv_no" varchar(64),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_callback_task"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_callback_task"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_callback_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_callback_task"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_merchant_callback_task"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_merchant_callback_task"."version" IS '版本号';
COMMENT ON COLUMN "pay_merchant_callback_task"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_merchant_callback_task"."trade_id" IS '本地交易ID';
COMMENT ON COLUMN "pay_merchant_callback_task"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "pay_merchant_callback_task"."trade_type" IS '通知类型';
COMMENT ON COLUMN "pay_merchant_callback_task"."content" IS '消息内容';
COMMENT ON COLUMN "pay_merchant_callback_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "pay_merchant_callback_task"."next_time" IS '发送次数';
COMMENT ON COLUMN "pay_merchant_callback_task"."send_count" IS '延迟次数';
COMMENT ON COLUMN "pay_merchant_callback_task"."delay_count" IS '下次发送时间';
COMMENT ON COLUMN "pay_merchant_callback_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "pay_merchant_callback_task"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_callback_task"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_merchant_callback_task"."url" IS '发送地址';
COMMENT ON COLUMN "pay_merchant_callback_task"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant_callback_task"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_merchant_callback_task" IS '客户回调通知消息任务';

-- ----------------------------
-- Table structure for pay_merchant_notify_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_notify_config";
CREATE TABLE "pay_merchant_notify_config" (
  "id" int8 NOT NULL,
  "code" varchar(50),
  "subscribe" bool,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(64),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_notify_config"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_notify_config"."code" IS '通知类型';
COMMENT ON COLUMN "pay_merchant_notify_config"."subscribe" IS '是否订阅';
COMMENT ON COLUMN "pay_merchant_notify_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_notify_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_notify_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_merchant_notify_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_merchant_notify_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_merchant_notify_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_merchant_notify_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_notify_config"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_merchant_notify_config"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant_notify_config"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_merchant_notify_config" IS '商户应用消息通知配置';

-- ----------------------------
-- Table structure for pay_merchant_notify_const
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_notify_const";
CREATE TABLE "pay_merchant_notify_const" (
  "id" int8 NOT NULL,
  "code" varchar(60),
  "name" varchar(80),
  "description" varchar(300),
  "enable" bool
)
;
COMMENT ON COLUMN "pay_merchant_notify_const"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_notify_const"."code" IS '通道编码';
COMMENT ON COLUMN "pay_merchant_notify_const"."name" IS '通道名称';
COMMENT ON COLUMN "pay_merchant_notify_const"."description" IS '描述';
COMMENT ON COLUMN "pay_merchant_notify_const"."enable" IS '是否启用';
COMMENT ON TABLE "pay_merchant_notify_const" IS '商户订阅通知类型';

-- ----------------------------
-- Table structure for pay_merchant_notify_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_notify_record";
CREATE TABLE "pay_merchant_notify_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "task_id" int8,
  "req_count" int4,
  "success" bool,
  "send_type" varchar(30),
  "error_code" varchar(50),
  "error_msg" varchar(500),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_notify_record"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_notify_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_notify_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_notify_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_notify_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_merchant_notify_record"."task_id" IS '任务ID';
COMMENT ON COLUMN "pay_merchant_notify_record"."req_count" IS '请求次数';
COMMENT ON COLUMN "pay_merchant_notify_record"."success" IS '发送是否成功';
COMMENT ON COLUMN "pay_merchant_notify_record"."send_type" IS '发送类型, 自动发送, 手动发送';
COMMENT ON COLUMN "pay_merchant_notify_record"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_merchant_notify_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_merchant_notify_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant_notify_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_merchant_notify_record" IS '客户订阅通知发送记录';

-- ----------------------------
-- Table structure for pay_merchant_notify_task
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_notify_task";
CREATE TABLE "pay_merchant_notify_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "trade_id" int8,
  "trade_no" varchar(32),
  "notify_type" varchar(20),
  "content" text,
  "success" bool,
  "next_time" timestamp(6),
  "send_count" int4,
  "delay_count" int4,
  "latest_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(64),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_notify_task"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_notify_task"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_notify_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_notify_task"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_merchant_notify_task"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_merchant_notify_task"."version" IS '版本号';
COMMENT ON COLUMN "pay_merchant_notify_task"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_merchant_notify_task"."trade_id" IS '本地交易ID';
COMMENT ON COLUMN "pay_merchant_notify_task"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "pay_merchant_notify_task"."notify_type" IS '通知类型';
COMMENT ON COLUMN "pay_merchant_notify_task"."content" IS '消息内容';
COMMENT ON COLUMN "pay_merchant_notify_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "pay_merchant_notify_task"."next_time" IS '发送次数';
COMMENT ON COLUMN "pay_merchant_notify_task"."send_count" IS '延迟次数';
COMMENT ON COLUMN "pay_merchant_notify_task"."delay_count" IS '下次发送时间';
COMMENT ON COLUMN "pay_merchant_notify_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "pay_merchant_notify_task"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_notify_task"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_merchant_notify_task"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_merchant_notify_task"."agent_no" IS '所属代理商';
COMMENT ON TABLE "pay_merchant_notify_task" IS '客户订阅通知消息任务';

-- ----------------------------
-- Table structure for pay_merchant_rate_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_rate_config";
CREATE TABLE "pay_merchant_rate_config" (
  "id" int8 NOT NULL,
  "channel" varchar(32),
  "enable" bool,
  "ext" text,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "agent_no" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_merchant_rate_config"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_rate_config"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_merchant_rate_config"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_merchant_rate_config"."ext" IS '扩展存储';
COMMENT ON COLUMN "pay_merchant_rate_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_rate_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_rate_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_merchant_rate_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_merchant_rate_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_merchant_rate_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_merchant_rate_config"."mch_no" IS '商户商号';
COMMENT ON COLUMN "pay_merchant_rate_config"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_merchant_rate_config"."isv_no" IS '服务商号';
COMMENT ON TABLE "pay_merchant_rate_config" IS '商户费率配置';

-- ----------------------------
-- Table structure for pay_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS "pay_merchant_user";
CREATE TABLE "pay_merchant_user" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "administrator" bool NOT NULL
)
;
COMMENT ON COLUMN "pay_merchant_user"."id" IS '主键';
COMMENT ON COLUMN "pay_merchant_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "pay_merchant_user"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_merchant_user"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_merchant_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_merchant_user"."administrator" IS '是否为商户管理员';
COMMENT ON TABLE "pay_merchant_user" IS '用户商户关联关系';

-- ----------------------------
-- Table structure for pay_method_const
-- ----------------------------
DROP TABLE IF EXISTS "pay_method_const";
CREATE TABLE "pay_method_const" (
  "id" int8 NOT NULL,
  "code" varchar(60),
  "name" varchar(80),
  "enable" bool,
  "remark" varchar(300)
)
;
COMMENT ON COLUMN "pay_method_const"."id" IS '主键';
COMMENT ON COLUMN "pay_method_const"."code" IS '通道编码';
COMMENT ON COLUMN "pay_method_const"."name" IS '通道名称';
COMMENT ON COLUMN "pay_method_const"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_method_const"."remark" IS '备注';
COMMENT ON TABLE "pay_method_const" IS '支付方式常量';

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS "pay_order";
CREATE TABLE "pay_order" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "biz_order_no" varchar(100),
  "order_no" varchar(100),
  "out_order_no" varchar(150),
  "title" varchar(100),
  "description" varchar(500),
  "allocation" bool,
  "auto_allocation" bool,
  "channel" varchar(20),
  "method" varchar(20),
  "amount" numeric(16,4),
  "refundable_balance" numeric(16,4),
  "status" varchar(32),
  "refund_status" varchar(32),
  "alloc_status" varchar(32),
  "return_url" varchar(200),
  "notify_url" varchar(200),
  "extra_param" varchar(2048),
  "attach" varchar(500),
  "req_time" timestamp(6),
  "client_ip" varchar(64),
  "error_code" varchar(50),
  "error_msg" varchar(500),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "expired_time" timestamp(6),
  "pay_time" timestamp(6),
  "close_time" timestamp(6),
  "isv_no" varchar(32),
  "other_method" varchar(128),
  "auth_code" varchar(128),
  "limit_pay" varchar(128),
  "terminal_no" varchar(128),
  "agent_no" varchar(32),
  "real_amount" numeric(16,4),
  "settle_status" varchar(32)
)
;
COMMENT ON COLUMN "pay_order"."id" IS '主键';
COMMENT ON COLUMN "pay_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_order"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_order"."version" IS '版本号';
COMMENT ON COLUMN "pay_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_order"."biz_order_no" IS '商户订单号';
COMMENT ON COLUMN "pay_order"."order_no" IS '订单号';
COMMENT ON COLUMN "pay_order"."out_order_no" IS '通道订单号';
COMMENT ON COLUMN "pay_order"."title" IS '标题';
COMMENT ON COLUMN "pay_order"."description" IS '描述';
COMMENT ON COLUMN "pay_order"."allocation" IS '是否支持分账';
COMMENT ON COLUMN "pay_order"."auto_allocation" IS '自动分账';
COMMENT ON COLUMN "pay_order"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_order"."method" IS '支付方式';
COMMENT ON COLUMN "pay_order"."amount" IS '金额(元)';
COMMENT ON COLUMN "pay_order"."refundable_balance" IS '可退金额(元)';
COMMENT ON COLUMN "pay_order"."status" IS '支付状态';
COMMENT ON COLUMN "pay_order"."refund_status" IS '退款状态';
COMMENT ON COLUMN "pay_order"."alloc_status" IS '分账状态';
COMMENT ON COLUMN "pay_order"."return_url" IS '同步跳转地址';
COMMENT ON COLUMN "pay_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "pay_order"."extra_param" IS '通道附加参数';
COMMENT ON COLUMN "pay_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "pay_order"."req_time" IS '请求时间';
COMMENT ON COLUMN "pay_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "pay_order"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_order"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_order"."expired_time" IS '过期时间';
COMMENT ON COLUMN "pay_order"."pay_time" IS '支付成功时间';
COMMENT ON COLUMN "pay_order"."close_time" IS '关闭时间';
COMMENT ON COLUMN "pay_order"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_order"."other_method" IS '其他支付方式';
COMMENT ON COLUMN "pay_order"."auth_code" IS '付款码';
COMMENT ON COLUMN "pay_order"."limit_pay" IS '限制支付类型';
COMMENT ON COLUMN "pay_order"."terminal_no" IS '终端设备编码';
COMMENT ON COLUMN "pay_order"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_order"."real_amount" IS '实收金额';
COMMENT ON COLUMN "pay_order"."settle_status" IS '结算状态';
COMMENT ON TABLE "pay_order" IS '支付订单';

-- ----------------------------
-- Table structure for pay_order_expand
-- ----------------------------
DROP TABLE IF EXISTS "pay_order_expand";
CREATE TABLE "pay_order_expand" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32),
  "buyer_id" varchar(64),
  "user_id" varchar(64),
  "trade_product" varchar(64),
  "trade_way" varchar(64),
  "bank_type" varchar(64),
  "trans_order_no" varchar(64),
  "promotion_type" varchar(64),
  "ext" varchar(5000)
)
;
COMMENT ON COLUMN "pay_order_expand"."id" IS '主键';
COMMENT ON COLUMN "pay_order_expand"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_order_expand"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_order_expand"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_order_expand"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_order_expand"."version" IS '版本号';
COMMENT ON COLUMN "pay_order_expand"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_order_expand"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_order_expand"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_order_expand"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_order_expand"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_order_expand"."buyer_id" IS '付款用户ID';
COMMENT ON COLUMN "pay_order_expand"."user_id" IS '用户标识';
COMMENT ON COLUMN "pay_order_expand"."trade_product" IS '支付产品';
COMMENT ON COLUMN "pay_order_expand"."trade_way" IS '交易方式';
COMMENT ON COLUMN "pay_order_expand"."bank_type" IS '银行卡类型';
COMMENT ON COLUMN "pay_order_expand"."trans_order_no" IS '透传订单号';
COMMENT ON COLUMN "pay_order_expand"."promotion_type" IS '参加活动类型';
COMMENT ON COLUMN "pay_order_expand"."ext" IS '扩展参数存储字段';
COMMENT ON TABLE "pay_order_expand" IS '支付订单扩展存储参数';

-- ----------------------------
-- Table structure for pay_platform_basic_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_platform_basic_config";
CREATE TABLE "pay_platform_basic_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "single_limit_amount" numeric(16,4),
  "monthly_limit_amount" numeric(16,4),
  "daily_limit_amount" numeric(16,4)
)
;
COMMENT ON COLUMN "pay_platform_basic_config"."id" IS '主键';
COMMENT ON COLUMN "pay_platform_basic_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_platform_basic_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_platform_basic_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_platform_basic_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_platform_basic_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_platform_basic_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_platform_basic_config"."single_limit_amount" IS '全局单笔限额';
COMMENT ON COLUMN "pay_platform_basic_config"."monthly_limit_amount" IS '每月累计限额';
COMMENT ON COLUMN "pay_platform_basic_config"."daily_limit_amount" IS '每日限额';
COMMENT ON TABLE "pay_platform_basic_config" IS '管理平台基础配置';

-- ----------------------------
-- Table structure for pay_platform_cashouts_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_platform_cashouts_config";
CREATE TABLE "pay_platform_cashouts_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(0),
  "last_modifier" int8,
  "last_modified_time" timestamp(0),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "start_amount" numeric(16,4),
  "fee_formula" varchar(32),
  "fixed_fee" numeric(16,4),
  "fixed_rate" numeric(16,4),
  "fixed_fee_combined" numeric(16,4),
  "fixed_rate_combined" numeric(16,4),
  "freeze_amount" numeric(16,4)
)
;
COMMENT ON COLUMN "pay_platform_cashouts_config"."id" IS '主键';
COMMENT ON COLUMN "pay_platform_cashouts_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_platform_cashouts_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_platform_cashouts_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_platform_cashouts_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_platform_cashouts_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_platform_cashouts_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_platform_cashouts_config"."start_amount" IS '起始提现额度';
COMMENT ON COLUMN "pay_platform_cashouts_config"."fee_formula" IS '手续费计算公式';
COMMENT ON COLUMN "pay_platform_cashouts_config"."fixed_fee" IS '单笔固定';
COMMENT ON COLUMN "pay_platform_cashouts_config"."fixed_rate" IS '单笔费率';
COMMENT ON COLUMN "pay_platform_cashouts_config"."fixed_fee_combined" IS '组合 固定手续费';
COMMENT ON COLUMN "pay_platform_cashouts_config"."fixed_rate_combined" IS '组合 费率';
COMMENT ON COLUMN "pay_platform_cashouts_config"."freeze_amount" IS '冻结金额';
COMMENT ON TABLE "pay_platform_cashouts_config" IS '平台代理商默认提现配置';

-- ----------------------------
-- Table structure for pay_platform_url_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_platform_url_config";
CREATE TABLE "pay_platform_url_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "admin_web_url" varchar(200),
  "agent_web_url" varchar(200),
  "mch_web_url" varchar(200),
  "gateway_service_url" varchar(200),
  "gateway_h5_url" varchar(200)
)
;
COMMENT ON COLUMN "pay_platform_url_config"."id" IS '主键';
COMMENT ON COLUMN "pay_platform_url_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_platform_url_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_platform_url_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_platform_url_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_platform_url_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_platform_url_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_platform_url_config"."admin_web_url" IS '运营端网址';
COMMENT ON COLUMN "pay_platform_url_config"."agent_web_url" IS '代理商端网址';
COMMENT ON COLUMN "pay_platform_url_config"."mch_web_url" IS '商户端网址';
COMMENT ON COLUMN "pay_platform_url_config"."gateway_service_url" IS '支付网关地址';
COMMENT ON COLUMN "pay_platform_url_config"."gateway_h5_url" IS '网关h5地址';
COMMENT ON TABLE "pay_platform_url_config" IS '系统地址配置';

-- ----------------------------
-- Table structure for pay_platform_website_config
-- ----------------------------
DROP TABLE IF EXISTS "pay_platform_website_config";
CREATE TABLE "pay_platform_website_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "system_name" varchar(50),
  "company_name" varchar(100),
  "company_phone" varchar(32),
  "company_email" varchar(64),
  "whole_logo" varchar(200),
  "simple_logo" varchar(200),
  "icp_info" varchar(64),
  "icp_link" varchar(200),
  "mps_info" varchar(64),
  "mps_link" varchar(200),
  "pcac_info" varchar(64),
  "pcac_link" varchar(200),
  "icp_plus_info" varchar(64),
  "icp_plus_link" varchar(200),
  "copyright" varchar(64),
  "copyright_link" varchar(200)
)
;
COMMENT ON COLUMN "pay_platform_website_config"."id" IS '主键';
COMMENT ON COLUMN "pay_platform_website_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_platform_website_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_platform_website_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "pay_platform_website_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_platform_website_config"."version" IS '版本号';
COMMENT ON COLUMN "pay_platform_website_config"."deleted" IS '删除标志';
COMMENT ON TABLE "pay_platform_website_config" IS '站点显示内容配置';

-- ----------------------------
-- Table structure for pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS "pay_refund_order";
CREATE TABLE "pay_refund_order" (
  "id" int8 NOT NULL,
  "order_id" int8 NOT NULL,
  "order_no" varchar(100) NOT NULL,
  "biz_order_no" varchar(100) NOT NULL,
  "out_order_no" varchar(150) NOT NULL,
  "title" varchar(100) NOT NULL,
  "refund_no" varchar(150) NOT NULL,
  "biz_refund_no" varchar(100) NOT NULL,
  "out_refund_no" varchar(150),
  "channel" varchar(20) NOT NULL,
  "order_amount" numeric(16,4) NOT NULL,
  "amount" numeric(16,4) NOT NULL,
  "reason" varchar(150),
  "finish_time" timestamp(6),
  "status" varchar(20) NOT NULL,
  "notify_url" varchar(200),
  "attach" varchar(500),
  "extra_param" varchar(2048),
  "req_time" timestamp(6),
  "client_ip" varchar(64),
  "error_code" varchar(10),
  "error_msg" varchar(500),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32),
  "settle_status" varchar(32)
)
;
COMMENT ON COLUMN "pay_refund_order"."id" IS '主键';
COMMENT ON COLUMN "pay_refund_order"."order_id" IS '支付订单ID';
COMMENT ON COLUMN "pay_refund_order"."order_no" IS '支付订单号';
COMMENT ON COLUMN "pay_refund_order"."biz_order_no" IS '商户支付订单号';
COMMENT ON COLUMN "pay_refund_order"."out_order_no" IS '通道支付订单号';
COMMENT ON COLUMN "pay_refund_order"."title" IS '支付标题';
COMMENT ON COLUMN "pay_refund_order"."refund_no" IS '退款号';
COMMENT ON COLUMN "pay_refund_order"."biz_refund_no" IS '商户退款号';
COMMENT ON COLUMN "pay_refund_order"."out_refund_no" IS '通道退款交易号';
COMMENT ON COLUMN "pay_refund_order"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_refund_order"."order_amount" IS '订单金额';
COMMENT ON COLUMN "pay_refund_order"."amount" IS '退款金额';
COMMENT ON COLUMN "pay_refund_order"."reason" IS '退款原因';
COMMENT ON COLUMN "pay_refund_order"."finish_time" IS '退款完成时间';
COMMENT ON COLUMN "pay_refund_order"."status" IS '退款状态';
COMMENT ON COLUMN "pay_refund_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "pay_refund_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "pay_refund_order"."extra_param" IS '附加参数';
COMMENT ON COLUMN "pay_refund_order"."req_time" IS '请求时间，传输时间戳';
COMMENT ON COLUMN "pay_refund_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "pay_refund_order"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_refund_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_refund_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_refund_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_refund_order"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_refund_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_refund_order"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_refund_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_refund_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_refund_order"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_refund_order"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_refund_order"."agent_no" IS '代理商号';
COMMENT ON COLUMN "pay_refund_order"."settle_status" IS '结算状态';
COMMENT ON TABLE "pay_refund_order" IS '退款订单';

-- ----------------------------
-- Table structure for pay_terminal_const
-- ----------------------------
DROP TABLE IF EXISTS "pay_terminal_const";
CREATE TABLE "pay_terminal_const" (
  "id" int8 NOT NULL,
  "channel" varchar(32) NOT NULL,
  "type" varchar(32) NOT NULL,
  "name" varchar(32) NOT NULL,
  "enable" bool NOT NULL,
  "remark" varchar(200)
)
;
COMMENT ON COLUMN "pay_terminal_const"."id" IS '主键';
COMMENT ON COLUMN "pay_terminal_const"."channel" IS '通道编码';
COMMENT ON COLUMN "pay_terminal_const"."type" IS '终端报送类型';
COMMENT ON COLUMN "pay_terminal_const"."name" IS '终端报送名称';
COMMENT ON COLUMN "pay_terminal_const"."enable" IS '是否启用';
COMMENT ON COLUMN "pay_terminal_const"."remark" IS '备注';
COMMENT ON TABLE "pay_terminal_const" IS '通道终端报送类型';

-- ----------------------------
-- Table structure for pay_terminal_device
-- ----------------------------
DROP TABLE IF EXISTS "pay_terminal_device";
CREATE TABLE "pay_terminal_device" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "name" varchar(64),
  "terminal_no" varchar(64),
  "type" varchar(32),
  "serial_num" varchar(64),
  "area_code" varchar(64),
  "address" varchar(256),
  "company_name" varchar(128),
  "put_date" timestamp(6),
  "gps" bool,
  "machine_type" varchar(64),
  "longitude" varchar(32),
  "latitude" varchar(32),
  "ip" varchar(128),
  "network_license" varchar(128),
  "agent_no" varchar(32),
  "isv_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_terminal_device"."id" IS '主键';
COMMENT ON COLUMN "pay_terminal_device"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_terminal_device"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_terminal_device"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_terminal_device"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_terminal_device"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_terminal_device"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_terminal_device"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_terminal_device"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_terminal_device"."name" IS '终端名称';
COMMENT ON COLUMN "pay_terminal_device"."terminal_no" IS '终端编码';
COMMENT ON COLUMN "pay_terminal_device"."type" IS '终端类型';
COMMENT ON COLUMN "pay_terminal_device"."serial_num" IS '终端序列号';
COMMENT ON COLUMN "pay_terminal_device"."area_code" IS '省市区编码';
COMMENT ON COLUMN "pay_terminal_device"."address" IS '终端发放地址';
COMMENT ON COLUMN "pay_terminal_device"."company_name" IS '终端厂商名称';
COMMENT ON COLUMN "pay_terminal_device"."put_date" IS '发放日期';
COMMENT ON COLUMN "pay_terminal_device"."gps" IS '支持终端定位';
COMMENT ON COLUMN "pay_terminal_device"."machine_type" IS '终端机具型号';
COMMENT ON COLUMN "pay_terminal_device"."longitude" IS '经度，浮点型, 小数点后最多保留6位';
COMMENT ON COLUMN "pay_terminal_device"."latitude" IS '纬度，浮点型,小数点后最多保留6位';
COMMENT ON COLUMN "pay_terminal_device"."ip" IS '设备 IP 地址';
COMMENT ON COLUMN "pay_terminal_device"."network_license" IS '银行卡受理终端产品入网认证编号';
COMMENT ON COLUMN "pay_terminal_device"."agent_no" IS '所属代理商';
COMMENT ON COLUMN "pay_terminal_device"."isv_no" IS '所属服务商';
COMMENT ON TABLE "pay_terminal_device" IS '支付终端设备管理';

-- ----------------------------
-- Table structure for pay_trade_callback_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_trade_callback_record";
CREATE TABLE "pay_trade_callback_record" (
  "id" int8 NOT NULL,
  "trade_no" varchar(100),
  "out_trade_no" varchar(150),
  "channel" varchar(20) NOT NULL,
  "callback_type" varchar(20) NOT NULL,
  "notify_info" text NOT NULL,
  "status" varchar(20) NOT NULL,
  "error_code" varchar(10),
  "error_msg" varchar(500),
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_trade_callback_record"."id" IS '主键';
COMMENT ON COLUMN "pay_trade_callback_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "pay_trade_callback_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "pay_trade_callback_record"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_trade_callback_record"."callback_type" IS '回调类型';
COMMENT ON COLUMN "pay_trade_callback_record"."notify_info" IS '通知消息';
COMMENT ON COLUMN "pay_trade_callback_record"."status" IS '回调处理状态';
COMMENT ON COLUMN "pay_trade_callback_record"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_trade_callback_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_trade_callback_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_trade_callback_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_trade_callback_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_trade_callback_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_trade_callback_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_trade_callback_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_trade_callback_record" IS '网关回调通知';

-- ----------------------------
-- Table structure for pay_trade_flow_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_trade_flow_record";
CREATE TABLE "pay_trade_flow_record" (
  "id" int8 NOT NULL,
  "title" varchar(100) NOT NULL,
  "amount" numeric(16,4) NOT NULL,
  "type" varchar(20) NOT NULL,
  "channel" varchar(20) NOT NULL,
  "trade_no" varchar(100) NOT NULL,
  "biz_trade_no" varchar(100) NOT NULL,
  "out_trade_no" varchar(150),
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_trade_flow_record"."id" IS '主键';
COMMENT ON COLUMN "pay_trade_flow_record"."title" IS '标题';
COMMENT ON COLUMN "pay_trade_flow_record"."amount" IS '金额';
COMMENT ON COLUMN "pay_trade_flow_record"."type" IS '业务类型';
COMMENT ON COLUMN "pay_trade_flow_record"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_trade_flow_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "pay_trade_flow_record"."biz_trade_no" IS '商户交易号';
COMMENT ON COLUMN "pay_trade_flow_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "pay_trade_flow_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_trade_flow_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_trade_flow_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_trade_flow_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_trade_flow_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_trade_flow_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_trade_flow_record" IS '资金流水记录';

-- ----------------------------
-- Table structure for pay_trade_sync_record
-- ----------------------------
DROP TABLE IF EXISTS "pay_trade_sync_record";
CREATE TABLE "pay_trade_sync_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "trade_no" varchar(100),
  "biz_trade_no" varchar(100),
  "out_trade_no" varchar(150),
  "out_trade_status" varchar(32),
  "trade_type" varchar(32),
  "channel" varchar(32),
  "sync_info" text,
  "adjust" bool NOT NULL,
  "error_code" varchar(50),
  "error_msg" varchar(500),
  "client_ip" varchar(64),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_trade_sync_record"."id" IS '主键';
COMMENT ON COLUMN "pay_trade_sync_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_trade_sync_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_trade_sync_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_trade_sync_record"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_trade_sync_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "pay_trade_sync_record"."biz_trade_no" IS '商户交易号';
COMMENT ON COLUMN "pay_trade_sync_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "pay_trade_sync_record"."out_trade_status" IS '通道返回的状态';
COMMENT ON COLUMN "pay_trade_sync_record"."trade_type" IS '同步类型';
COMMENT ON COLUMN "pay_trade_sync_record"."channel" IS '同步通道';
COMMENT ON COLUMN "pay_trade_sync_record"."sync_info" IS '网关返回的同步消息';
COMMENT ON COLUMN "pay_trade_sync_record"."adjust" IS '是否进行调整';
COMMENT ON COLUMN "pay_trade_sync_record"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_trade_sync_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_trade_sync_record"."client_ip" IS '终端ip';
COMMENT ON COLUMN "pay_trade_sync_record"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_trade_sync_record"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_trade_sync_record" IS '交易同步记录';

-- ----------------------------
-- Table structure for pay_transfer_order
-- ----------------------------
DROP TABLE IF EXISTS "pay_transfer_order";
CREATE TABLE "pay_transfer_order" (
  "id" int8 NOT NULL,
  "biz_transfer_no" varchar(100) NOT NULL,
  "transfer_no" varchar(100) NOT NULL,
  "out_transfer_no" varchar(150),
  "channel" varchar(20) NOT NULL,
  "amount" numeric(16,4) NOT NULL,
  "title" varchar(100),
  "reason" varchar(150),
  "payee_type" varchar(20),
  "payee_account" varchar(100),
  "payee_name" varchar(50),
  "status" varchar(20) NOT NULL,
  "finish_time" timestamp(6),
  "notify_url" varchar(200),
  "attach" varchar(500),
  "req_time" timestamp(6),
  "client_ip" varchar(64),
  "error_code" varchar(10),
  "error_msg" varchar(500),
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32),
  "app_id" varchar(32),
  "extra_param" varchar(2048),
  "isv_no" varchar(32),
  "agent_no" varchar(32)
)
;
COMMENT ON COLUMN "pay_transfer_order"."id" IS '主键';
COMMENT ON COLUMN "pay_transfer_order"."biz_transfer_no" IS '商户转账号';
COMMENT ON COLUMN "pay_transfer_order"."transfer_no" IS '转账号';
COMMENT ON COLUMN "pay_transfer_order"."out_transfer_no" IS '通道转账号';
COMMENT ON COLUMN "pay_transfer_order"."channel" IS '支付通道';
COMMENT ON COLUMN "pay_transfer_order"."amount" IS '转账金额';
COMMENT ON COLUMN "pay_transfer_order"."title" IS '标题';
COMMENT ON COLUMN "pay_transfer_order"."reason" IS '转账原因/备注';
COMMENT ON COLUMN "pay_transfer_order"."payee_type" IS '收款人类型';
COMMENT ON COLUMN "pay_transfer_order"."payee_account" IS '收款人账号';
COMMENT ON COLUMN "pay_transfer_order"."payee_name" IS '收款人姓名';
COMMENT ON COLUMN "pay_transfer_order"."status" IS '状态';
COMMENT ON COLUMN "pay_transfer_order"."finish_time" IS '成功时间';
COMMENT ON COLUMN "pay_transfer_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "pay_transfer_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "pay_transfer_order"."req_time" IS '请求时间，传输时间戳';
COMMENT ON COLUMN "pay_transfer_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "pay_transfer_order"."error_code" IS '错误码';
COMMENT ON COLUMN "pay_transfer_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "pay_transfer_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "pay_transfer_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "pay_transfer_order"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "pay_transfer_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "pay_transfer_order"."version" IS '乐观锁';
COMMENT ON COLUMN "pay_transfer_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "pay_transfer_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "pay_transfer_order"."app_id" IS '应用号';
COMMENT ON COLUMN "pay_transfer_order"."extra_param" IS '通道附加参数';
COMMENT ON COLUMN "pay_transfer_order"."isv_no" IS '所属服务商';
COMMENT ON COLUMN "pay_transfer_order"."agent_no" IS '代理商号';
COMMENT ON TABLE "pay_transfer_order" IS '转账订单';

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS "starter_audit_login_log";
CREATE TABLE "starter_audit_login_log" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "account" varchar(100),
  "login" bool,
  "client" varchar(20),
  "login_type" varchar(20),
  "ip" varchar(80),
  "login_location" varchar(100),
  "browser" varchar(200),
  "os" varchar(100),
  "msg" text,
  "login_time" timestamp(6)
)
;
COMMENT ON COLUMN "starter_audit_login_log"."id" IS '主键';
COMMENT ON COLUMN "starter_audit_login_log"."user_id" IS '用户账号ID';
COMMENT ON COLUMN "starter_audit_login_log"."account" IS '用户名称';
COMMENT ON COLUMN "starter_audit_login_log"."login" IS '登录成功状态';
COMMENT ON COLUMN "starter_audit_login_log"."client" IS '登录终端';
COMMENT ON COLUMN "starter_audit_login_log"."login_type" IS '登录方式';
COMMENT ON COLUMN "starter_audit_login_log"."ip" IS '登录IP地址';
COMMENT ON COLUMN "starter_audit_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "starter_audit_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "starter_audit_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "starter_audit_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "starter_audit_login_log"."login_time" IS '访问时间';
COMMENT ON TABLE "starter_audit_login_log" IS '登录日志';

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS "starter_audit_operate_log";
CREATE TABLE "starter_audit_operate_log" (
  "id" int8 NOT NULL,
  "title" varchar(100) NOT NULL,
  "operate_id" int8,
  "account" varchar(100),
  "business_type" varchar(50) NOT NULL,
  "method" varchar(100) NOT NULL,
  "request_method" varchar(20) NOT NULL,
  "operate_url" varchar(200) NOT NULL,
  "operate_ip" varchar(80),
  "operate_location" varchar(50),
  "operate_param" text,
  "operate_return" text,
  "success" bool,
  "error_msg" text,
  "operate_time" timestamp(6) NOT NULL,
  "browser" varchar(200),
  "os" varchar(100),
  "client" varchar(20)
)
;
COMMENT ON COLUMN "starter_audit_operate_log"."id" IS '主键';
COMMENT ON COLUMN "starter_audit_operate_log"."title" IS '操作模块';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_id" IS '操作人员id';
COMMENT ON COLUMN "starter_audit_operate_log"."account" IS '操作人员账号';
COMMENT ON COLUMN "starter_audit_operate_log"."business_type" IS '业务类型';
COMMENT ON COLUMN "starter_audit_operate_log"."method" IS '请求方法';
COMMENT ON COLUMN "starter_audit_operate_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_url" IS '请求url';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_ip" IS '操作ip';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_location" IS '操作地点';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_param" IS '请求参数';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_return" IS '返回参数';
COMMENT ON COLUMN "starter_audit_operate_log"."success" IS '操作状态';
COMMENT ON COLUMN "starter_audit_operate_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "starter_audit_operate_log"."operate_time" IS '操作时间';
COMMENT ON COLUMN "starter_audit_operate_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "starter_audit_operate_log"."os" IS '操作系统';
COMMENT ON COLUMN "starter_audit_operate_log"."client" IS '终端';
COMMENT ON TABLE "starter_audit_operate_log" IS '操作日志';

-- ----------------------------
-- Table structure for starter_file_platform
-- ----------------------------
DROP TABLE IF EXISTS "starter_file_platform";
CREATE TABLE "starter_file_platform" (
  "id" int8 NOT NULL,
  "type" varchar(50),
  "name" varchar(200),
  "url" varchar(200),
  "default_platform" bool,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "frontend_upload" bool
)
;
COMMENT ON COLUMN "starter_file_platform"."id" IS '文件id';
COMMENT ON COLUMN "starter_file_platform"."type" IS '平台类型';
COMMENT ON COLUMN "starter_file_platform"."name" IS '名称';
COMMENT ON COLUMN "starter_file_platform"."url" IS '访问地址';
COMMENT ON COLUMN "starter_file_platform"."default_platform" IS '默认存储平台';
COMMENT ON COLUMN "starter_file_platform"."creator" IS '创建者ID';
COMMENT ON COLUMN "starter_file_platform"."create_time" IS '创建时间';
COMMENT ON COLUMN "starter_file_platform"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "starter_file_platform"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "starter_file_platform"."version" IS '版本号';
COMMENT ON COLUMN "starter_file_platform"."frontend_upload" IS '前端直传';
COMMENT ON TABLE "starter_file_platform" IS '文件存储平台';

-- ----------------------------
-- Table structure for starter_file_upload_info
-- ----------------------------
DROP TABLE IF EXISTS "starter_file_upload_info";
CREATE TABLE "starter_file_upload_info" (
  "id" int8 NOT NULL,
  "url" varchar(512) NOT NULL,
  "size" int8,
  "filename" varchar(256),
  "original_filename" varchar(256),
  "base_path" varchar(256),
  "path" varchar(256),
  "ext" varchar(32),
  "content_type" varchar(128),
  "platform" varchar(32),
  "th_url" varchar(512),
  "th_filename" varchar(256),
  "th_size" int8,
  "th_content_type" varchar(128),
  "object_id" varchar(32),
  "object_type" varchar(32),
  "metadata" text,
  "user_metadata" text,
  "th_metadata" text,
  "th_user_metadata" text,
  "attr" text,
  "file_acl" varchar(32),
  "th_file_acl" varchar(32),
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "starter_file_upload_info"."id" IS '文件id';
COMMENT ON COLUMN "starter_file_upload_info"."url" IS '文件访问地址';
COMMENT ON COLUMN "starter_file_upload_info"."size" IS '文件大小，单位字节';
COMMENT ON COLUMN "starter_file_upload_info"."filename" IS '文件名称';
COMMENT ON COLUMN "starter_file_upload_info"."original_filename" IS '原始文件名';
COMMENT ON COLUMN "starter_file_upload_info"."base_path" IS '基础存储路径';
COMMENT ON COLUMN "starter_file_upload_info"."path" IS '存储路径';
COMMENT ON COLUMN "starter_file_upload_info"."ext" IS '文件扩展名';
COMMENT ON COLUMN "starter_file_upload_info"."content_type" IS 'MIME类型';
COMMENT ON COLUMN "starter_file_upload_info"."platform" IS '存储平台';
COMMENT ON COLUMN "starter_file_upload_info"."th_url" IS '缩略图访问路径';
COMMENT ON COLUMN "starter_file_upload_info"."th_filename" IS '缩略图名称';
COMMENT ON COLUMN "starter_file_upload_info"."th_size" IS '缩略图大小，单位字节';
COMMENT ON COLUMN "starter_file_upload_info"."th_content_type" IS '缩略图MIME类型';
COMMENT ON COLUMN "starter_file_upload_info"."object_id" IS '文件所属对象id';
COMMENT ON COLUMN "starter_file_upload_info"."object_type" IS '文件所属对象类型，例如用户头像，评价图片';
COMMENT ON COLUMN "starter_file_upload_info"."metadata" IS '文件元数据';
COMMENT ON COLUMN "starter_file_upload_info"."user_metadata" IS '文件用户元数据';
COMMENT ON COLUMN "starter_file_upload_info"."th_metadata" IS '缩略图元数据';
COMMENT ON COLUMN "starter_file_upload_info"."th_user_metadata" IS '缩略图用户元数据';
COMMENT ON COLUMN "starter_file_upload_info"."attr" IS '附加属性';
COMMENT ON COLUMN "starter_file_upload_info"."file_acl" IS '文件ACL';
COMMENT ON COLUMN "starter_file_upload_info"."th_file_acl" IS '缩略图文件ACL';
COMMENT ON COLUMN "starter_file_upload_info"."create_time" IS '创建时间';
COMMENT ON TABLE "starter_file_upload_info" IS '文件记录表';

-- ----------------------------
-- Primary Key structure for table base_area
-- ----------------------------
ALTER TABLE "base_area" ADD CONSTRAINT "base_area_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_city
-- ----------------------------
ALTER TABLE "base_city" ADD CONSTRAINT "base_city_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_dict
-- ----------------------------
ALTER TABLE "base_dict" ADD CONSTRAINT "base_dict_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_dict_item
-- ----------------------------
ALTER TABLE "base_dict_item" ADD CONSTRAINT "base_dict_item_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_param
-- ----------------------------
ALTER TABLE "base_param" ADD CONSTRAINT "base_param_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_province
-- ----------------------------
ALTER TABLE "base_province" ADD CONSTRAINT "base_province_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Indexes structure for table base_street
-- ----------------------------
CREATE INDEX "inx_area_code" ON "base_street" USING btree (
  "area_code" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "inx_area_code" IS '县区';

-- ----------------------------
-- Primary Key structure for table base_street
-- ----------------------------
ALTER TABLE "base_street" ADD CONSTRAINT "base_street_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_user_protocol
-- ----------------------------
ALTER TABLE "base_user_protocol" ADD CONSTRAINT "base_user_protocol_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_client
-- ----------------------------
ALTER TABLE "iam_client" ADD CONSTRAINT "iam_client_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_code
-- ----------------------------
ALTER TABLE "iam_perm_code" ADD CONSTRAINT "iam_perm_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_path
-- ----------------------------
ALTER TABLE "iam_perm_path" ADD CONSTRAINT "iam_perm_code_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role
-- ----------------------------
ALTER TABLE "iam_role" ADD CONSTRAINT "iam_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_code
-- ----------------------------
ALTER TABLE "iam_role_code" ADD CONSTRAINT "iam_role_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_menu
-- ----------------------------
ALTER TABLE "iam_role_menu" ADD CONSTRAINT "iam_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_path
-- ----------------------------
ALTER TABLE "iam_role_path" ADD CONSTRAINT "iam_role_path_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_expand_info
-- ----------------------------
ALTER TABLE "iam_user_expand_info" ADD CONSTRAINT "iam_user_expand_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_info
-- ----------------------------
ALTER TABLE "iam_user_info" ADD CONSTRAINT "iam_user_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_role
-- ----------------------------
ALTER TABLE "iam_user_role" ADD CONSTRAINT "iam_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_aggregate_bar_pay_config
-- ----------------------------
ALTER TABLE "pay_aggregate_bar_pay_config" ADD CONSTRAINT "pay_aggregate_pay_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_aggregate_pay_config
-- ----------------------------
ALTER TABLE "pay_aggregate_pay_config" ADD CONSTRAINT "pay_checkout_aggregate_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_api_const
-- ----------------------------
ALTER TABLE "pay_api_const" ADD CONSTRAINT "pay_channel_const_copy1_pkey1" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_audio_device
-- ----------------------------
ALTER TABLE "pay_audio_device" ADD CONSTRAINT "pay_audio_device_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_audio_device_config
-- ----------------------------
ALTER TABLE "pay_audio_device_config" ADD CONSTRAINT "pay_audio_device_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_cashier_code
-- ----------------------------
ALTER TABLE "pay_cashier_code" ADD CONSTRAINT "pay_cashier_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_cashier_code_config
-- ----------------------------
ALTER TABLE "pay_cashier_code_config" ADD CONSTRAINT "pay_cashier_code_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_cashier_code_scene_config
-- ----------------------------
ALTER TABLE "pay_cashier_code_scene_config" ADD CONSTRAINT "pay_cashier_code_type_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_cashier_group_config
-- ----------------------------
ALTER TABLE "pay_cashier_group_config" ADD CONSTRAINT "pay_checkout_group_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_cashier_item_config
-- ----------------------------
ALTER TABLE "pay_cashier_item_config" ADD CONSTRAINT "pay_checkout_item_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_config
-- ----------------------------
ALTER TABLE "pay_channel_config" ADD CONSTRAINT "pay_channel_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_const
-- ----------------------------
ALTER TABLE "pay_channel_const" ADD CONSTRAINT "pay_channel_const_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_terminal
-- ----------------------------
ALTER TABLE "pay_channel_terminal" ADD CONSTRAINT "pay_channel_terminal_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_close_record
-- ----------------------------
CREATE INDEX "biz_order_no" ON "pay_close_record" USING btree (
  "biz_order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "biz_order_no" IS '商户支付订单号索引';
CREATE INDEX "order_no" ON "pay_close_record" USING btree (
  "order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "order_no" IS '支付订单号索引';

-- ----------------------------
-- Primary Key structure for table pay_close_record
-- ----------------------------
ALTER TABLE "pay_close_record" ADD CONSTRAINT "pay_close_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_gateway_config
-- ----------------------------
ALTER TABLE "pay_gateway_config" ADD CONSTRAINT "pay_checkout_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_aggregate_bar_pay_config
-- ----------------------------
ALTER TABLE "pay_isv_aggregate_bar_pay_config" ADD CONSTRAINT "pay_aggregate_bar_pay_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_aggregate_pay_config
-- ----------------------------
ALTER TABLE "pay_isv_aggregate_pay_config" ADD CONSTRAINT "pay_aggregate_pay_config_copy1_pkey1" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_cashier_group_config
-- ----------------------------
ALTER TABLE "pay_isv_cashier_group_config" ADD CONSTRAINT "pay_cashier_group_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_cashier_item_config
-- ----------------------------
ALTER TABLE "pay_isv_cashier_item_config" ADD CONSTRAINT "pay_cashier_item_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_channel_config
-- ----------------------------
ALTER TABLE "pay_isv_channel_config" ADD CONSTRAINT "pay_channel_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_isv_gateway_config
-- ----------------------------
ALTER TABLE "pay_isv_gateway_config" ADD CONSTRAINT "pay_gateway_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_mch_app
-- ----------------------------
ALTER TABLE "pay_mch_app" ADD CONSTRAINT "pay_mch_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant
-- ----------------------------
ALTER TABLE "pay_merchant" ADD CONSTRAINT "pay_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_record
-- ----------------------------
ALTER TABLE "pay_merchant_callback_record" ADD CONSTRAINT "pay_merchant_callback_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_task
-- ----------------------------
ALTER TABLE "pay_merchant_callback_task" ADD CONSTRAINT "pay_merchant_callback_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_config
-- ----------------------------
ALTER TABLE "pay_merchant_notify_config" ADD CONSTRAINT "pay_merchant_notify_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_const
-- ----------------------------
ALTER TABLE "pay_merchant_notify_const" ADD CONSTRAINT "pay_merchant_notify_const_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_record
-- ----------------------------
ALTER TABLE "pay_merchant_notify_record" ADD CONSTRAINT "pay_merchant_notify_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_task
-- ----------------------------
ALTER TABLE "pay_merchant_notify_task" ADD CONSTRAINT "pay_merchant_notify_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_rate_config
-- ----------------------------
ALTER TABLE "pay_merchant_rate_config" ADD CONSTRAINT "pay_agent_rate_config_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_user
-- ----------------------------
ALTER TABLE "pay_merchant_user" ADD CONSTRAINT "pay_user_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_method_const
-- ----------------------------
ALTER TABLE "pay_method_const" ADD CONSTRAINT "pay_channel_const_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_order
-- ----------------------------
CREATE INDEX "order_biz_order_order_no_idx" ON "pay_order" USING btree (
  "biz_order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "order_pay_order_order_no_idx" ON "pay_order" USING btree (
  "order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "order_pay_order_out_order_no_idx" ON "pay_order" USING btree (
  "out_order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table pay_order
-- ----------------------------
ALTER TABLE "pay_order" ADD CONSTRAINT "pay_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_order_expand
-- ----------------------------
ALTER TABLE "pay_order_expand" ADD CONSTRAINT "pay_order_expand_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_platform_basic_config
-- ----------------------------
ALTER TABLE "pay_platform_basic_config" ADD CONSTRAINT "pay_platform_basic_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_platform_cashouts_config
-- ----------------------------
ALTER TABLE "pay_platform_cashouts_config" ADD CONSTRAINT "pay_platform_cashouts_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_platform_url_config
-- ----------------------------
ALTER TABLE "pay_platform_url_config" ADD CONSTRAINT "pay_platform_url_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_platform_website_config
-- ----------------------------
ALTER TABLE "pay_platform_website_config" ADD CONSTRAINT "pay_platform_website_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_refund_order
-- ----------------------------
CREATE INDEX "refund_biz_order_no" ON "pay_refund_order" USING btree (
  "biz_order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_biz_order_no" IS '商户支付订单号索引';
CREATE INDEX "refund_biz_refund_no" ON "pay_refund_order" USING btree (
  "biz_refund_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_biz_refund_no" IS '商户退款号索引';
CREATE INDEX "refund_order_id" ON "pay_refund_order" USING btree (
  "order_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_order_id" IS '支付订单ID索引';
CREATE INDEX "refund_order_no" ON "pay_refund_order" USING btree (
  "order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_order_no" IS '支付订单号索引';
CREATE INDEX "refund_out_order_no" ON "pay_refund_order" USING btree (
  "out_order_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_out_order_no" IS '通道支付订单号索引';
CREATE INDEX "refund_out_refund_no" ON "pay_refund_order" USING btree (
  "out_refund_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_out_refund_no" IS '通道退款交易号索引';
CREATE INDEX "refund_refund_no" ON "pay_refund_order" USING btree (
  "refund_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "refund_refund_no" IS '退款号索引';

-- ----------------------------
-- Primary Key structure for table pay_refund_order
-- ----------------------------
ALTER TABLE "pay_refund_order" ADD CONSTRAINT "pay_refund_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_terminal_const
-- ----------------------------
ALTER TABLE "pay_terminal_const" ADD CONSTRAINT "pay_terminal_type_const_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_terminal_device
-- ----------------------------
ALTER TABLE "pay_terminal_device" ADD CONSTRAINT "pay_xxxx_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_trade_callback_record
-- ----------------------------
CREATE INDEX "out_trade_no" ON "pay_trade_callback_record" USING btree (
  "out_trade_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "out_trade_no" IS '通道交易号索引';
CREATE INDEX "trade_no" ON "pay_trade_callback_record" USING btree (
  "trade_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "trade_no" IS '本地交易号索引';

-- ----------------------------
-- Primary Key structure for table pay_trade_callback_record
-- ----------------------------
ALTER TABLE "pay_trade_callback_record" ADD CONSTRAINT "pay_callback_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_trade_sync_record
-- ----------------------------
ALTER TABLE "pay_trade_sync_record" ADD CONSTRAINT "pay_trade_sync_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_order
-- ----------------------------
CREATE INDEX "transfer_biz_transfer_no" ON "pay_transfer_order" USING btree (
  "biz_transfer_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "transfer_biz_transfer_no" IS '商户转账号索引';
CREATE INDEX "transfer_out_transfer_no" ON "pay_transfer_order" USING btree (
  "out_transfer_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "transfer_out_transfer_no" IS '通道转账号索引';
CREATE INDEX "transfer_transfer_no" ON "pay_transfer_order" USING btree (
  "transfer_no" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "transfer_transfer_no" IS '转账号索引';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order
-- ----------------------------
ALTER TABLE "pay_transfer_order" ADD CONSTRAINT "pay_transfer_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_audit_login_log
-- ----------------------------
ALTER TABLE "starter_audit_login_log" ADD CONSTRAINT "starter_audit_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_audit_operate_log
-- ----------------------------
ALTER TABLE "starter_audit_operate_log" ADD CONSTRAINT "starter_audit_operate_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_file_platform
-- ----------------------------
ALTER TABLE "starter_file_platform" ADD CONSTRAINT "starter_file_platform_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_file_upload_info
-- ----------------------------
ALTER TABLE "starter_file_upload_info" ADD CONSTRAINT "starter_file_upload_info_pkey" PRIMARY KEY ("id");
