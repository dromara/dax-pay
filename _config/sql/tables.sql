
-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS base_area;
CREATE TABLE base_area (
                                      code varchar(6) COLLATE pg_catalog.default NOT NULL,
                                      name varchar(60) COLLATE pg_catalog.default NOT NULL,
                                      city_code varchar(4) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN base_area.name IS '区域名称';
COMMENT ON COLUMN base_area.city_code IS '城市编码';
COMMENT ON TABLE base_area IS '县区表';

-- ----------------------------
-- Table structure for base_city
-- ----------------------------
DROP TABLE IF EXISTS base_city;
CREATE TABLE base_city (
                                      code varchar(4) COLLATE pg_catalog.default NOT NULL,
                                      name varchar(60) COLLATE pg_catalog.default NOT NULL,
                                      province_code varchar(2) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN base_city.code IS '城市编码';
COMMENT ON COLUMN base_city.name IS '城市名称';
COMMENT ON COLUMN base_city.province_code IS '省份编码';
COMMENT ON TABLE base_city IS '城市表';

-- ----------------------------
-- Table structure for base_dict
-- ----------------------------
DROP TABLE IF EXISTS base_dict;
CREATE TABLE base_dict (
                                      id int8 NOT NULL,
                                      name varchar(50) COLLATE pg_catalog.default,
                                      group_tag varchar(50) COLLATE pg_catalog.default,
                                      code varchar(50) COLLATE pg_catalog.default,
                                      remark varchar(50) COLLATE pg_catalog.default,
                                      creator int8,
                                      create_time timestamp(6),
                                      last_modifier int8,
                                      last_modified_time timestamp(6),
                                      version int4 NOT NULL,
                                      enable bool,
                                      deleted bool NOT NULL
)
;
COMMENT ON COLUMN base_dict.id IS '主键';
COMMENT ON COLUMN base_dict.name IS '名称';
COMMENT ON COLUMN base_dict.group_tag IS '分类标签';
COMMENT ON COLUMN base_dict.code IS '编码';
COMMENT ON COLUMN base_dict.remark IS '备注';
COMMENT ON COLUMN base_dict.creator IS '创建者ID';
COMMENT ON COLUMN base_dict.create_time IS '创建时间';
COMMENT ON COLUMN base_dict.last_modifier IS '最后修改ID';
COMMENT ON COLUMN base_dict.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN base_dict.version IS '版本号';
COMMENT ON COLUMN base_dict.enable IS '是否启用';
COMMENT ON COLUMN base_dict.deleted IS '删除标志';
COMMENT ON TABLE base_dict IS '字典表';

-- ----------------------------
-- Table structure for base_dict_item
-- ----------------------------
DROP TABLE IF EXISTS base_dict_item;
CREATE TABLE base_dict_item (
                                           id int8 NOT NULL,
                                           dict_id int8 NOT NULL,
                                           dict_code varchar(50) COLLATE pg_catalog.default,
                                           code varchar(50) COLLATE pg_catalog.default,
                                           name varchar(50) COLLATE pg_catalog.default,
                                           sort_no int4,
                                           enable bool,
                                           remark varchar(50) COLLATE pg_catalog.default,
                                           creator int8,
                                           create_time timestamp(6),
                                           last_modifier int8,
                                           last_modified_time timestamp(6),
                                           version int4 NOT NULL,
                                           deleted bool NOT NULL
)
;
COMMENT ON COLUMN base_dict_item.id IS '主键';
COMMENT ON COLUMN base_dict_item.dict_id IS '字典ID';
COMMENT ON COLUMN base_dict_item.dict_code IS '字典编码';
COMMENT ON COLUMN base_dict_item.code IS '字典项编码';
COMMENT ON COLUMN base_dict_item.name IS '名称';
COMMENT ON COLUMN base_dict_item.sort_no IS '字典项排序';
COMMENT ON COLUMN base_dict_item.enable IS '是否启用';
COMMENT ON COLUMN base_dict_item.remark IS '备注';
COMMENT ON COLUMN base_dict_item.creator IS '创建者ID';
COMMENT ON COLUMN base_dict_item.create_time IS '创建时间';
COMMENT ON COLUMN base_dict_item.last_modifier IS '最后修改ID';
COMMENT ON COLUMN base_dict_item.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN base_dict_item.version IS '版本号';
COMMENT ON COLUMN base_dict_item.deleted IS '删除标志';
COMMENT ON TABLE base_dict_item IS '字典项';

-- ----------------------------
-- Table structure for base_param
-- ----------------------------
DROP TABLE IF EXISTS base_param;
CREATE TABLE base_param (
                                       id int8 NOT NULL,
                                       creator int8,
                                       create_time timestamp(6),
                                       last_modifier int8,
                                       last_modified_time timestamp(6),
                                       version int4 NOT NULL,
                                       deleted bool NOT NULL,
                                       name varchar(50) COLLATE pg_catalog.default NOT NULL,
                                       key varchar(50) COLLATE pg_catalog.default NOT NULL,
                                       value varchar(500) COLLATE pg_catalog.default NOT NULL,
                                       type varchar(20) COLLATE pg_catalog.default,
                                       enable bool NOT NULL,
                                       internal bool NOT NULL,
                                       remark varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN base_param.id IS '主键';
COMMENT ON COLUMN base_param.creator IS '创建者ID';
COMMENT ON COLUMN base_param.create_time IS '创建时间';
COMMENT ON COLUMN base_param.last_modifier IS '最后修改ID';
COMMENT ON COLUMN base_param.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN base_param.version IS '版本号';
COMMENT ON COLUMN base_param.deleted IS '删除标志';
COMMENT ON COLUMN base_param.name IS '参数名称';
COMMENT ON COLUMN base_param.key IS '参数键名';
COMMENT ON COLUMN base_param.value IS '参数值';
COMMENT ON COLUMN base_param.type IS '参数类型';
COMMENT ON COLUMN base_param.enable IS '启用状态';
COMMENT ON COLUMN base_param.internal IS '内置参数';
COMMENT ON COLUMN base_param.remark IS '备注';
COMMENT ON TABLE base_param IS '系统参数';

-- ----------------------------
-- Table structure for base_province
-- ----------------------------
DROP TABLE IF EXISTS base_province;
CREATE TABLE base_province (
                                          code varchar(2) COLLATE pg_catalog.default NOT NULL,
                                          name varchar(30) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN base_province.code IS '省份编码';
COMMENT ON COLUMN base_province.name IS '省份名称';
COMMENT ON TABLE base_province IS '省份表';

-- ----------------------------
-- Table structure for base_street
-- ----------------------------
DROP TABLE IF EXISTS base_street;
CREATE TABLE base_street (
                                        code varchar(9) COLLATE pg_catalog.default NOT NULL,
                                        name varchar(60) COLLATE pg_catalog.default NOT NULL,
                                        area_code varchar(6) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN base_street.code IS '编码';
COMMENT ON COLUMN base_street.name IS '街道名称';
COMMENT ON COLUMN base_street.area_code IS '县区编码';
COMMENT ON TABLE base_street IS '街道表';

-- ----------------------------
-- Table structure for base_user_protocol
-- ----------------------------
DROP TABLE IF EXISTS base_user_protocol;
CREATE TABLE base_user_protocol (
                                               id int8 NOT NULL,
                                               creator int8,
                                               create_time timestamp(6),
                                               last_modifier int8,
                                               last_modified_time timestamp(6),
                                               version int4 NOT NULL,
                                               deleted bool NOT NULL,
                                               name varchar(32) COLLATE pg_catalog.default,
                                               show_name varchar(64) COLLATE pg_catalog.default,
                                               type varchar(32) COLLATE pg_catalog.default,
                                               default_protocol bool,
                                               content text COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN base_user_protocol.id IS '主键';
COMMENT ON COLUMN base_user_protocol.creator IS '创建者ID';
COMMENT ON COLUMN base_user_protocol.create_time IS '创建时间';
COMMENT ON COLUMN base_user_protocol.last_modifier IS '最后修改ID';
COMMENT ON COLUMN base_user_protocol.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN base_user_protocol.version IS '版本号';
COMMENT ON COLUMN base_user_protocol.deleted IS '删除标志';
COMMENT ON COLUMN base_user_protocol.name IS '名称';
COMMENT ON COLUMN base_user_protocol.show_name IS '显示名称';
COMMENT ON COLUMN base_user_protocol.type IS '类型';
COMMENT ON COLUMN base_user_protocol.default_protocol IS '默认协议';
COMMENT ON COLUMN base_user_protocol.content IS '内容';
COMMENT ON TABLE base_user_protocol IS '用户协议';

-- ----------------------------
-- Table structure for iam_client
-- ----------------------------
DROP TABLE IF EXISTS iam_client;
CREATE TABLE iam_client (
                                       id int8 NOT NULL,
                                       creator int8,
                                       create_time timestamp(6),
                                       last_modifier int8,
                                       last_modified_time timestamp(6),
                                       version int4 NOT NULL,
                                       deleted bool NOT NULL,
                                       code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                       name varchar(50) COLLATE pg_catalog.default NOT NULL,
                                       internal bool NOT NULL,
                                       remark varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN iam_client.id IS '主键';
COMMENT ON COLUMN iam_client.creator IS '创建者ID';
COMMENT ON COLUMN iam_client.create_time IS '创建时间';
COMMENT ON COLUMN iam_client.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_client.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_client.version IS '版本号';
COMMENT ON COLUMN iam_client.deleted IS '删除标志';
COMMENT ON COLUMN iam_client.code IS '编码';
COMMENT ON COLUMN iam_client.name IS '名称';
COMMENT ON COLUMN iam_client.internal IS '是否系统内置';
COMMENT ON COLUMN iam_client.remark IS '备注';
COMMENT ON TABLE iam_client IS '认证终端';

-- ----------------------------
-- Table structure for iam_perm_code
-- ----------------------------
DROP TABLE IF EXISTS iam_perm_code;
CREATE TABLE iam_perm_code (
                                          id int8 NOT NULL,
                                          creator int8,
                                          create_time timestamp(6),
                                          last_modifier int8,
                                          last_modified_time timestamp(6),
                                          version int4 NOT NULL,
                                          deleted bool NOT NULL,
                                          pid int8,
                                          code varchar(50) COLLATE pg_catalog.default,
                                          name varchar(50) COLLATE pg_catalog.default,
                                          remark varchar(300) COLLATE pg_catalog.default,
                                          leaf bool NOT NULL
)
;
COMMENT ON COLUMN iam_perm_code.id IS '主键';
COMMENT ON COLUMN iam_perm_code.creator IS '创建者ID';
COMMENT ON COLUMN iam_perm_code.create_time IS '创建时间';
COMMENT ON COLUMN iam_perm_code.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_perm_code.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_perm_code.version IS '版本号';
COMMENT ON COLUMN iam_perm_code.deleted IS '删除标志';
COMMENT ON COLUMN iam_perm_code.pid IS '父ID';
COMMENT ON COLUMN iam_perm_code.code IS '权限码';
COMMENT ON COLUMN iam_perm_code.name IS '名称';
COMMENT ON COLUMN iam_perm_code.remark IS '备注';
COMMENT ON COLUMN iam_perm_code.leaf IS '是否为叶子结点';
COMMENT ON TABLE iam_perm_code IS '权限码';

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS iam_perm_menu;
CREATE TABLE iam_perm_menu (
                                          id int8 NOT NULL,
                                          creator int8,
                                          create_time timestamp(6),
                                          last_modifier int8,
                                          last_modified_time timestamp(6),
                                          version int4 NOT NULL,
                                          deleted bool NOT NULL,
                                          pid int8,
                                          client_code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          title varchar(100) COLLATE pg_catalog.default,
                                          name varchar(100) COLLATE pg_catalog.default,
                                          icon varchar(100) COLLATE pg_catalog.default,
                                          hidden bool NOT NULL,
                                          hide_children_menu bool NOT NULL,
                                          component varchar(300) COLLATE pg_catalog.default,
                                          path varchar(300) COLLATE pg_catalog.default,
                                          redirect varchar(300) COLLATE pg_catalog.default,
                                          sort_no float4,
                                          root bool NOT NULL,
                                          keep_alive bool,
                                          target_outside bool,
                                          full_screen bool,
                                          remark varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN iam_perm_menu.id IS '主键';
COMMENT ON COLUMN iam_perm_menu.creator IS '创建者ID';
COMMENT ON COLUMN iam_perm_menu.create_time IS '创建时间';
COMMENT ON COLUMN iam_perm_menu.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_perm_menu.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_perm_menu.version IS '版本号';
COMMENT ON COLUMN iam_perm_menu.deleted IS '删除标志';
COMMENT ON COLUMN iam_perm_menu.pid IS '父id';
COMMENT ON COLUMN iam_perm_menu.client_code IS '关联终端code';
COMMENT ON COLUMN iam_perm_menu.title IS '菜单标题';
COMMENT ON COLUMN iam_perm_menu.name IS '路由名称';
COMMENT ON COLUMN iam_perm_menu.icon IS '菜单图标';
COMMENT ON COLUMN iam_perm_menu.hidden IS '是否隐藏';
COMMENT ON COLUMN iam_perm_menu.hide_children_menu IS '是否隐藏子菜单';
COMMENT ON COLUMN iam_perm_menu.component IS '组件';
COMMENT ON COLUMN iam_perm_menu.path IS '访问路径';
COMMENT ON COLUMN iam_perm_menu.redirect IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN iam_perm_menu.sort_no IS '菜单排序';
COMMENT ON COLUMN iam_perm_menu.root IS '是否是一级菜单';
COMMENT ON COLUMN iam_perm_menu.keep_alive IS '是否缓存页面';
COMMENT ON COLUMN iam_perm_menu.target_outside IS '是否为外部打开';
COMMENT ON COLUMN iam_perm_menu.full_screen IS '是否全屏打开';
COMMENT ON COLUMN iam_perm_menu.remark IS '描述';
COMMENT ON TABLE iam_perm_menu IS '菜单权限';

-- ----------------------------
-- Table structure for iam_perm_path
-- ----------------------------
DROP TABLE IF EXISTS iam_perm_path;
CREATE TABLE iam_perm_path (
                                          id int8 NOT NULL,
                                          parent_code varchar(50) COLLATE pg_catalog.default,
                                          client_code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          name varchar(50) COLLATE pg_catalog.default,
                                          leaf bool NOT NULL,
                                          path varchar(200) COLLATE pg_catalog.default,
                                          method varchar(10) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN iam_perm_path.id IS '主键';
COMMENT ON COLUMN iam_perm_path.parent_code IS '上级编码';
COMMENT ON COLUMN iam_perm_path.client_code IS '终端编码';
COMMENT ON COLUMN iam_perm_path.code IS '标识编码(模块、分组标识)';
COMMENT ON COLUMN iam_perm_path.name IS '名称(请求路径、模块、分组名称)';
COMMENT ON COLUMN iam_perm_path.leaf IS '叶子节点';
COMMENT ON COLUMN iam_perm_path.path IS '请求路径';
COMMENT ON COLUMN iam_perm_path.method IS '请求类型, 为全大写单词';
COMMENT ON TABLE iam_perm_path IS '请求权限(url)';

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS iam_role;
CREATE TABLE iam_role (
                                     id int8 NOT NULL,
                                     creator int8,
                                     create_time timestamp(6),
                                     last_modifier int8,
                                     last_modified_time timestamp(6),
                                     version int4 NOT NULL,
                                     deleted bool NOT NULL,
                                     pid int8,
                                     code varchar(50) COLLATE pg_catalog.default,
                                     name varchar(100) COLLATE pg_catalog.default,
                                     internal bool,
                                     remark varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN iam_role.id IS '主键';
COMMENT ON COLUMN iam_role.creator IS '创建者ID';
COMMENT ON COLUMN iam_role.create_time IS '创建时间';
COMMENT ON COLUMN iam_role.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_role.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_role.version IS '版本号';
COMMENT ON COLUMN iam_role.deleted IS '删除标志';
COMMENT ON COLUMN iam_role.pid IS '父级Id';
COMMENT ON COLUMN iam_role.code IS '编码';
COMMENT ON COLUMN iam_role.name IS '名称';
COMMENT ON COLUMN iam_role.internal IS '是否系统内置';
COMMENT ON COLUMN iam_role.remark IS '备注';
COMMENT ON TABLE iam_role IS '角色';

-- ----------------------------
-- Table structure for iam_role_code
-- ----------------------------
DROP TABLE IF EXISTS iam_role_code;
CREATE TABLE iam_role_code (
                                          id int8 NOT NULL,
                                          role_id int8 NOT NULL,
                                          code_id int8 NOT NULL
)
;
COMMENT ON COLUMN iam_role_code.id IS '主键';
COMMENT ON COLUMN iam_role_code.role_id IS '角色id';
COMMENT ON COLUMN iam_role_code.code_id IS '权限码';
COMMENT ON TABLE iam_role_code IS '角色权限码关联关系';

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS iam_role_menu;
CREATE TABLE iam_role_menu (
                                          id int8 NOT NULL,
                                          role_id int8 NOT NULL,
                                          client_code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          menu_id int8 NOT NULL
)
;
COMMENT ON COLUMN iam_role_menu.id IS '主键';
COMMENT ON COLUMN iam_role_menu.role_id IS '角色id';
COMMENT ON COLUMN iam_role_menu.client_code IS '终端编码';
COMMENT ON COLUMN iam_role_menu.menu_id IS '菜单id';
COMMENT ON TABLE iam_role_menu IS '角色菜单关联关系';

-- ----------------------------
-- Table structure for iam_role_path
-- ----------------------------
DROP TABLE IF EXISTS iam_role_path;
CREATE TABLE iam_role_path (
                                          id int8 NOT NULL,
                                          role_id int8 NOT NULL,
                                          client_code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          path_id int8 NOT NULL
)
;
COMMENT ON COLUMN iam_role_path.id IS '主键';
COMMENT ON COLUMN iam_role_path.role_id IS '角色id';
COMMENT ON COLUMN iam_role_path.client_code IS '终端编码';
COMMENT ON COLUMN iam_role_path.path_id IS '请求资源id';
COMMENT ON TABLE iam_role_path IS '角色路径关联关系';

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS iam_user_expand_info;
CREATE TABLE iam_user_expand_info (
                                                 id int8 NOT NULL,
                                                 creator int8,
                                                 create_time timestamp(6),
                                                 last_modifier int8,
                                                 last_modified_time timestamp(6),
                                                 version int4 NOT NULL,
                                                 deleted bool NOT NULL,
                                                 sex varchar(10) COLLATE pg_catalog.default,
                                                 avatar varchar(300) COLLATE pg_catalog.default,
                                                 birthday date,
                                                 last_login_time timestamp(0),
                                                 register_time timestamp(6),
                                                 current_login_time timestamp(6),
                                                 initial_password bool,
                                                 expire_password bool,
                                                 last_change_password_time timestamp(6)
)
;
COMMENT ON COLUMN iam_user_expand_info.id IS '主键';
COMMENT ON COLUMN iam_user_expand_info.creator IS '创建者ID';
COMMENT ON COLUMN iam_user_expand_info.create_time IS '创建时间';
COMMENT ON COLUMN iam_user_expand_info.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_user_expand_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_user_expand_info.version IS '版本号';
COMMENT ON COLUMN iam_user_expand_info.deleted IS '删除标志';
COMMENT ON COLUMN iam_user_expand_info.sex IS '性别';
COMMENT ON COLUMN iam_user_expand_info.avatar IS '头像图片url';
COMMENT ON COLUMN iam_user_expand_info.birthday IS '生日';
COMMENT ON COLUMN iam_user_expand_info.last_login_time IS '上次登录时间';
COMMENT ON COLUMN iam_user_expand_info.register_time IS '注册时间';
COMMENT ON COLUMN iam_user_expand_info.current_login_time IS '本次登录时间';
COMMENT ON COLUMN iam_user_expand_info.initial_password IS '是否初始密码';
COMMENT ON COLUMN iam_user_expand_info.expire_password IS '密码是否过期';
COMMENT ON COLUMN iam_user_expand_info.last_change_password_time IS '上次修改密码时间';
COMMENT ON TABLE iam_user_expand_info IS '用户扩展信息';

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS iam_user_info;
CREATE TABLE iam_user_info (
                                          id int8 NOT NULL,
                                          creator int8,
                                          create_time timestamp(6),
                                          last_modifier int8,
                                          last_modified_time timestamp(6),
                                          version int4 NOT NULL,
                                          deleted bool NOT NULL,
                                          name varchar(50) COLLATE pg_catalog.default,
                                          account varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          password varchar(120) COLLATE pg_catalog.default NOT NULL,
                                          phone varchar(50) COLLATE pg_catalog.default,
                                          email varchar(50) COLLATE pg_catalog.default,
                                          administrator bool NOT NULL,
                                          status varchar(50) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN iam_user_info.id IS '主键';
COMMENT ON COLUMN iam_user_info.creator IS '创建者ID';
COMMENT ON COLUMN iam_user_info.create_time IS '创建时间';
COMMENT ON COLUMN iam_user_info.last_modifier IS '最后修改ID';
COMMENT ON COLUMN iam_user_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_user_info.version IS '版本号';
COMMENT ON COLUMN iam_user_info.deleted IS '删除标志';
COMMENT ON COLUMN iam_user_info.name IS '名称';
COMMENT ON COLUMN iam_user_info.account IS '账号';
COMMENT ON COLUMN iam_user_info.password IS '密码';
COMMENT ON COLUMN iam_user_info.phone IS '手机号';
COMMENT ON COLUMN iam_user_info.email IS '邮箱';
COMMENT ON COLUMN iam_user_info.administrator IS '是否管理员';
COMMENT ON COLUMN iam_user_info.status IS '账号状态';
COMMENT ON TABLE iam_user_info IS '用户核心信息';

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS iam_user_role;
CREATE TABLE iam_user_role (
                                          id int8 NOT NULL,
                                          user_id int8 NOT NULL,
                                          role_id int8 NOT NULL
)
;
COMMENT ON COLUMN iam_user_role.id IS '主键';
COMMENT ON COLUMN iam_user_role.user_id IS '用户';
COMMENT ON COLUMN iam_user_role.role_id IS '角色';
COMMENT ON TABLE iam_user_role IS '用户角色关联关系';

-- ----------------------------
-- Table structure for pay_adapay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_adapay_config;
CREATE TABLE pay_adapay_config (
                                              id int8 NOT NULL,
                                              creator int8,
                                              create_time timestamp(6),
                                              last_modifier int8,
                                              last_modified_time timestamp(6),
                                              version int4 NOT NULL,
                                              deleted bool NOT NULL,
                                              ada_pay_mch_no varchar(64) COLLATE pg_catalog.default,
                                              ada_pay_app_id varchar(64) COLLATE pg_catalog.default,
                                              api_key text COLLATE pg_catalog.default,
                                              mch_private_key text COLLATE pg_catalog.default,
                                              ada_pay_public_key text COLLATE pg_catalog.default,
                                              enable bool,
                                              sandbox bool,
                                              alloc_fee varchar(10) COLLATE pg_catalog.default,
                                              mch_no varchar(32) COLLATE pg_catalog.default,
                                              app_id varchar(32) COLLATE pg_catalog.default,
                                              wx_app_id varchar(32) COLLATE pg_catalog.default,
                                              wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                              wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                              isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_adapay_config.id IS '主键';
COMMENT ON COLUMN pay_adapay_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_adapay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_adapay_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_adapay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_adapay_config.version IS '版本号';
COMMENT ON COLUMN pay_adapay_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_adapay_config.ada_pay_mch_no IS '汇付商户号';
COMMENT ON COLUMN pay_adapay_config.ada_pay_app_id IS '汇付应用号';
COMMENT ON COLUMN pay_adapay_config.api_key IS '交易密钥';
COMMENT ON COLUMN pay_adapay_config.mch_private_key IS '商户RSA私钥';
COMMENT ON COLUMN pay_adapay_config.ada_pay_public_key IS 'Adapay RSA公钥';
COMMENT ON COLUMN pay_adapay_config.enable IS '是否启用';
COMMENT ON COLUMN pay_adapay_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN pay_adapay_config.alloc_fee IS '分账费用承担方 mch/user';
COMMENT ON COLUMN pay_adapay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_adapay_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_adapay_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_adapay_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_adapay_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_adapay_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_adapay_config IS '汇付支付配置';

-- ----------------------------
-- Table structure for pay_aggregate_bar_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_aggregate_bar_pay_config;
CREATE TABLE pay_aggregate_bar_pay_config (
                                                         id int8 NOT NULL,
                                                         creator varchar(64) COLLATE pg_catalog.default,
                                                         create_time timestamp(6),
                                                         last_modifier varchar(64) COLLATE pg_catalog.default,
                                                         last_modified_time timestamp(6),
                                                         version int4 NOT NULL DEFAULT 0,
                                                         deleted bool NOT NULL DEFAULT false,
                                                         isv_no varchar(32) COLLATE pg_catalog.default,
                                                         mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                         app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                         wx_channel varchar(32) COLLATE pg_catalog.default,
                                                         wx_method varchar(32) COLLATE pg_catalog.default,
                                                         alipay_channel varchar(32) COLLATE pg_catalog.default,
                                                         alipay_method varchar(32) COLLATE pg_catalog.default,
                                                         union_channel varchar(32) COLLATE pg_catalog.default,
                                                         union_method varchar(32) COLLATE pg_catalog.default,
                                                         terminal_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_aggregate_bar_pay_config.id IS '主键ID';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.creator IS '创建者';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.union_method IS '银联场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_bar_pay_config.terminal_no IS '付款终端号';
COMMENT ON TABLE pay_aggregate_bar_pay_config IS '网关聚合付款码支付配置';

-- ----------------------------
-- Table structure for pay_aggregate_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_aggregate_pay_config;
CREATE TABLE pay_aggregate_pay_config (
                                                     id int8 NOT NULL,
                                                     creator int8,
                                                     create_time timestamp(6),
                                                     last_modifier int8,
                                                     last_modified_time timestamp(6),
                                                     version int4 NOT NULL,
                                                     deleted bool NOT NULL,
                                                     app_id varchar(32) COLLATE pg_catalog.default,
                                                     aggregate_type varchar(32) COLLATE pg_catalog.default,
                                                     channel varchar(32) COLLATE pg_catalog.default,
                                                     pay_method varchar(32) COLLATE pg_catalog.default,
                                                     auto_launch bool,
                                                     other_method varchar(32) COLLATE pg_catalog.default,
                                                     mch_no varchar(32) COLLATE pg_catalog.default,
                                                     need_open_id bool,
                                                     call_type varchar(32) COLLATE pg_catalog.default,
                                                     open_id_get_type varchar(32) COLLATE pg_catalog.default,
                                                     isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_aggregate_pay_config.id IS '主键';
COMMENT ON COLUMN pay_aggregate_pay_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_aggregate_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_aggregate_pay_config.last_modifier IS '最后修者ID';
COMMENT ON COLUMN pay_aggregate_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_aggregate_pay_config.version IS '乐观锁';
COMMENT ON COLUMN pay_aggregate_pay_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_aggregate_pay_config.app_id IS '应用号';
COMMENT ON COLUMN pay_aggregate_pay_config.aggregate_type IS '聚合支付类型';
COMMENT ON COLUMN pay_aggregate_pay_config.channel IS '通道';
COMMENT ON COLUMN pay_aggregate_pay_config.pay_method IS '支付方式';
COMMENT ON COLUMN pay_aggregate_pay_config.auto_launch IS '自动拉起支付';
COMMENT ON COLUMN pay_aggregate_pay_config.other_method IS '其他支付方式';
COMMENT ON COLUMN pay_aggregate_pay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_aggregate_pay_config.need_open_id IS '需要获取OpenId';
COMMENT ON COLUMN pay_aggregate_pay_config.call_type IS '调用方式';
COMMENT ON COLUMN pay_aggregate_pay_config.open_id_get_type IS 'OpenId获取方式';
COMMENT ON COLUMN pay_aggregate_pay_config.isv_no IS '所属服务商';
COMMENT ON TABLE pay_aggregate_pay_config IS '网关聚合支付配置';

-- ----------------------------
-- Table structure for pay_aggregate_qr_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_aggregate_qr_pay_config;
CREATE TABLE pay_aggregate_qr_pay_config (
                                                        id int8 NOT NULL,
                                                        creator varchar(64) COLLATE pg_catalog.default,
                                                        create_time timestamp(6),
                                                        last_modifier varchar(64) COLLATE pg_catalog.default,
                                                        last_modified_time timestamp(6),
                                                        version int4 NOT NULL DEFAULT 0,
                                                        deleted bool NOT NULL DEFAULT false,
                                                        isv_no varchar(32) COLLATE pg_catalog.default,
                                                        mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        wx_channel varchar(32) COLLATE pg_catalog.default,
                                                        wx_method varchar(32) COLLATE pg_catalog.default,
                                                        alipay_channel varchar(32) COLLATE pg_catalog.default,
                                                        alipay_method varchar(32) COLLATE pg_catalog.default,
                                                        union_channel varchar(32) COLLATE pg_catalog.default,
                                                        union_method varchar(32) COLLATE pg_catalog.default,
                                                        auto_launch bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN pay_aggregate_qr_pay_config.id IS '主键ID';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.creator IS '创建者';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.union_method IS '银联场景对应支付方式';
COMMENT ON COLUMN pay_aggregate_qr_pay_config.auto_launch IS '自动拉起支付';
COMMENT ON TABLE pay_aggregate_qr_pay_config IS '网关聚合扫码支付配置';

-- ----------------------------
-- Table structure for pay_alipay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_alipay_config;
CREATE TABLE pay_alipay_config (
                                              id int8 NOT NULL,
                                              creator int8,
                                              create_time timestamp(6),
                                              last_modifier int8,
                                              last_modified_time timestamp(6),
                                              version int4 NOT NULL,
                                              deleted bool NOT NULL,
                                              isv bool,
                                              ali_app_id varchar(64) COLLATE pg_catalog.default,
                                              app_auth_token varchar(128) COLLATE pg_catalog.default,
                                              enable bool,
                                              auth_type varchar(20) COLLATE pg_catalog.default,
                                              sign_type varchar(20) COLLATE pg_catalog.default,
                                              alipay_user_id varchar(32) COLLATE pg_catalog.default,
                                              alipay_public_key text COLLATE pg_catalog.default,
                                              private_key text COLLATE pg_catalog.default,
                                              app_cert text COLLATE pg_catalog.default,
                                              alipay_cert text COLLATE pg_catalog.default,
                                              alipay_root_cert text COLLATE pg_catalog.default,
                                              sandbox bool,
                                              mch_no varchar(32) COLLATE pg_catalog.default,
                                              app_id varchar(32) COLLATE pg_catalog.default,
                                              isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_alipay_config.id IS '主键';
COMMENT ON COLUMN pay_alipay_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_alipay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_alipay_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_alipay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_alipay_config.version IS '版本号';
COMMENT ON COLUMN pay_alipay_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_alipay_config.isv IS '是否为ISV商户(特约商户)';
COMMENT ON COLUMN pay_alipay_config.ali_app_id IS '支付宝商户appId';
COMMENT ON COLUMN pay_alipay_config.app_auth_token IS '支付宝特约商户Token';
COMMENT ON COLUMN pay_alipay_config.enable IS '是否启用';
COMMENT ON COLUMN pay_alipay_config.auth_type IS '认证类型 证书/公钥';
COMMENT ON COLUMN pay_alipay_config.sign_type IS '签名类型 RSA2';
COMMENT ON COLUMN pay_alipay_config.alipay_user_id IS '是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取';
COMMENT ON COLUMN pay_alipay_config.alipay_public_key IS '支付宝公钥';
COMMENT ON COLUMN pay_alipay_config.private_key IS '应用私钥';
COMMENT ON COLUMN pay_alipay_config.app_cert IS '应用公钥证书';
COMMENT ON COLUMN pay_alipay_config.alipay_cert IS '支付宝公钥证书';
COMMENT ON COLUMN pay_alipay_config.alipay_root_cert IS '支付宝CA根证书';
COMMENT ON COLUMN pay_alipay_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN pay_alipay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_alipay_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_alipay_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_alipay_config IS '支付宝配置';

-- ----------------------------
-- Table structure for pay_alipay_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_alipay_isv_config;
CREATE TABLE pay_alipay_isv_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  ali_app_id varchar(64) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  auth_type varchar(20) COLLATE pg_catalog.default,
                                                  sign_type varchar(20) COLLATE pg_catalog.default,
                                                  alipay_user_id varchar(32) COLLATE pg_catalog.default,
                                                  alipay_public_key varchar(512) COLLATE pg_catalog.default,
                                                  private_key text COLLATE pg_catalog.default,
                                                  app_cert text COLLATE pg_catalog.default,
                                                  alipay_cert text COLLATE pg_catalog.default,
                                                  alipay_root_cert text COLLATE pg_catalog.default,
                                                  sandbox bool,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_alipay_isv_config.id IS '主键';
COMMENT ON COLUMN pay_alipay_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_alipay_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_alipay_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_alipay_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_alipay_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_alipay_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_alipay_isv_config.ali_app_id IS '支付宝商户appId';
COMMENT ON COLUMN pay_alipay_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_alipay_isv_config.auth_type IS '认证类型 证书/公钥';
COMMENT ON COLUMN pay_alipay_isv_config.sign_type IS '签名类型 RSA2';
COMMENT ON COLUMN pay_alipay_isv_config.alipay_user_id IS '是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取';
COMMENT ON COLUMN pay_alipay_isv_config.alipay_public_key IS '支付宝公钥';
COMMENT ON COLUMN pay_alipay_isv_config.private_key IS '应用私钥';
COMMENT ON COLUMN pay_alipay_isv_config.app_cert IS '应用公钥证书';
COMMENT ON COLUMN pay_alipay_isv_config.alipay_cert IS '支付宝公钥证书';
COMMENT ON COLUMN pay_alipay_isv_config.alipay_root_cert IS '支付宝CA根证书';
COMMENT ON COLUMN pay_alipay_isv_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN pay_alipay_isv_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_alipay_isv_config IS '支付宝服务商配置';

-- ----------------------------
-- Table structure for pay_alipay_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_alipay_sub_config;
CREATE TABLE pay_alipay_sub_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  app_auth_token varchar(128) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  alipay_user_id varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_alipay_sub_config.id IS '主键';
COMMENT ON COLUMN pay_alipay_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_alipay_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_alipay_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_alipay_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_alipay_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_alipay_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_alipay_sub_config.app_auth_token IS '支付宝特约商户Token';
COMMENT ON COLUMN pay_alipay_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_alipay_sub_config.alipay_user_id IS '支付宝商家唯一识别码 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取';
COMMENT ON COLUMN pay_alipay_sub_config.app_id IS '应用号';
COMMENT ON COLUMN pay_alipay_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_alipay_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_alipay_sub_config IS '支付宝特约商户配置';

-- ----------------------------
-- Table structure for pay_api_const
-- ----------------------------
DROP TABLE IF EXISTS pay_api_const;
CREATE TABLE pay_api_const (
                                          id int8 NOT NULL,
                                          code varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          name varchar(50) COLLATE pg_catalog.default NOT NULL,
                                          api varchar(200) COLLATE pg_catalog.default NOT NULL,
                                          enable bool,
                                          remark varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_api_const.id IS '主键';
COMMENT ON COLUMN pay_api_const.code IS '编码';
COMMENT ON COLUMN pay_api_const.name IS '接口名称';
COMMENT ON COLUMN pay_api_const.api IS '接口地址';
COMMENT ON COLUMN pay_api_const.enable IS '是否启用';
COMMENT ON COLUMN pay_api_const.remark IS '备注';
COMMENT ON TABLE pay_api_const IS '支付接口常量';

-- ----------------------------
-- Table structure for pay_cashier_code
-- ----------------------------
DROP TABLE IF EXISTS pay_cashier_code;
CREATE TABLE pay_cashier_code (
                                             id int8 NOT NULL,
                                             creator int8,
                                             create_time timestamp(6),
                                             last_modifier int8,
                                             last_modified_time timestamp(6),
                                             version int4 DEFAULT 0,
                                             deleted bool DEFAULT false,
                                             isv_no varchar(32) COLLATE pg_catalog.default,
                                             mch_no varchar(32) COLLATE pg_catalog.default,
                                             app_id varchar(32) COLLATE pg_catalog.default,
                                             amount_type varchar(20) COLLATE pg_catalog.default,
                                             amount numeric(15,2),
                                             name varchar(100) COLLATE pg_catalog.default,
                                             code varchar(50) COLLATE pg_catalog.default,
                                             template_id int8,
                                             allocation bool DEFAULT false,
                                             auto_allocation bool DEFAULT false,
                                             limit_pay varchar(50) COLLATE pg_catalog.default,
                                             read_system bool DEFAULT false,
                                             wx_channel varchar(20) COLLATE pg_catalog.default,
                                             wx_method varchar(20) COLLATE pg_catalog.default,
                                             alipay_channel varchar(20) COLLATE pg_catalog.default,
                                             alipay_method varchar(20) COLLATE pg_catalog.default,
                                             union_channel varchar(20) COLLATE pg_catalog.default,
                                             union_method varchar(20) COLLATE pg_catalog.default,
                                             enable bool DEFAULT true,
                                             batch_no varchar(50) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_cashier_code.id IS '主键ID';
COMMENT ON COLUMN pay_cashier_code.creator IS '创建者ID';
COMMENT ON COLUMN pay_cashier_code.create_time IS '创建时间';
COMMENT ON COLUMN pay_cashier_code.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_cashier_code.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_cashier_code.version IS '版本号';
COMMENT ON COLUMN pay_cashier_code.deleted IS '逻辑删除标识';
COMMENT ON COLUMN pay_cashier_code.isv_no IS '服务商号';
COMMENT ON COLUMN pay_cashier_code.mch_no IS '商户号';
COMMENT ON COLUMN pay_cashier_code.app_id IS '应用号';
COMMENT ON COLUMN pay_cashier_code.amount_type IS '金额类型 固定金额/任意金额';
COMMENT ON COLUMN pay_cashier_code.amount IS '金额';
COMMENT ON COLUMN pay_cashier_code.name IS '码牌名称';
COMMENT ON COLUMN pay_cashier_code.code IS '编号';
COMMENT ON COLUMN pay_cashier_code.template_id IS '模板ID';
COMMENT ON COLUMN pay_cashier_code.allocation IS '是否开启分账';
COMMENT ON COLUMN pay_cashier_code.auto_allocation IS '自动分账';
COMMENT ON COLUMN pay_cashier_code.limit_pay IS '限制用户支付方式';
COMMENT ON COLUMN pay_cashier_code.read_system IS '读取预设配置';
COMMENT ON COLUMN pay_cashier_code.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_cashier_code.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_cashier_code.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_cashier_code.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_cashier_code.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_cashier_code.union_method IS '银联场景对应支付方式';
COMMENT ON COLUMN pay_cashier_code.enable IS '状态';
COMMENT ON COLUMN pay_cashier_code.batch_no IS '批次号';
COMMENT ON TABLE pay_cashier_code IS '收款码牌';

-- ----------------------------
-- Table structure for pay_cashier_code_config
-- ----------------------------
DROP TABLE IF EXISTS pay_cashier_code_config;
CREATE TABLE pay_cashier_code_config (
                                                    id int8 NOT NULL,
                                                    creator varchar(64) COLLATE pg_catalog.default,
                                                    create_time timestamp(6),
                                                    last_modifier varchar(64) COLLATE pg_catalog.default,
                                                    last_modified_time timestamp(6),
                                                    version int4 NOT NULL DEFAULT 0,
                                                    deleted bool NOT NULL DEFAULT false,
                                                    isv_no varchar(32) COLLATE pg_catalog.default,
                                                    mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                    app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                    allocation bool NOT NULL DEFAULT false,
                                                    auto_allocation bool NOT NULL DEFAULT false,
                                                    limit_pay varchar(512) COLLATE pg_catalog.default,
                                                    wx_channel varchar(32) COLLATE pg_catalog.default,
                                                    wx_method varchar(32) COLLATE pg_catalog.default,
                                                    alipay_channel varchar(32) COLLATE pg_catalog.default,
                                                    alipay_method varchar(32) COLLATE pg_catalog.default,
                                                    union_channel varchar(32) COLLATE pg_catalog.default,
                                                    union_method varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_cashier_code_config.id IS '主键ID';
COMMENT ON COLUMN pay_cashier_code_config.creator IS '创建者';
COMMENT ON COLUMN pay_cashier_code_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_cashier_code_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_cashier_code_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_cashier_code_config.version IS '版本号';
COMMENT ON COLUMN pay_cashier_code_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_cashier_code_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_cashier_code_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_cashier_code_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_cashier_code_config.allocation IS '是否开启分账';
COMMENT ON COLUMN pay_cashier_code_config.auto_allocation IS '自动分账';
COMMENT ON COLUMN pay_cashier_code_config.limit_pay IS '限制用户支付方式';
COMMENT ON COLUMN pay_cashier_code_config.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_cashier_code_config.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_cashier_code_config.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_cashier_code_config.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_cashier_code_config.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_cashier_code_config.union_method IS '银联场景对应支付方式';
COMMENT ON TABLE pay_cashier_code_config IS '收银码牌配置';

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS pay_channel_config;
CREATE TABLE pay_channel_config (
                                               id int8 NOT NULL,
                                               channel varchar(32) COLLATE pg_catalog.default,
                                               out_mch_no varchar(128) COLLATE pg_catalog.default,
                                               out_app_id varchar(256) COLLATE pg_catalog.default,
                                               enable bool,
                                               ext text COLLATE pg_catalog.default,
                                               creator int8,
                                               create_time timestamp(6),
                                               last_modifier int8,
                                               last_modified_time timestamp(6),
                                               version int4 NOT NULL,
                                               deleted bool NOT NULL,
                                               mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                               app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                               isv_no varchar(64) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_channel_config.id IS '主键';
COMMENT ON COLUMN pay_channel_config.channel IS '支付通道';
COMMENT ON COLUMN pay_channel_config.out_mch_no IS '通道商户号';
COMMENT ON COLUMN pay_channel_config.out_app_id IS '通道APPID';
COMMENT ON COLUMN pay_channel_config.enable IS '是否启用';
COMMENT ON COLUMN pay_channel_config.ext IS '扩展存储';
COMMENT ON COLUMN pay_channel_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_channel_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_channel_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_channel_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_channel_config.version IS '版本号';
COMMENT ON COLUMN pay_channel_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_channel_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_channel_config.app_id IS '应用号';
COMMENT ON COLUMN pay_channel_config.isv_no IS '所属服务商';
COMMENT ON TABLE pay_channel_config IS '支付通道配置';

-- ----------------------------
-- Table structure for pay_channel_const
-- ----------------------------
DROP TABLE IF EXISTS pay_channel_const;
CREATE TABLE pay_channel_const (
                                              id int8 NOT NULL,
                                              code varchar(32) COLLATE pg_catalog.default NOT NULL,
                                              name varchar(32) COLLATE pg_catalog.default NOT NULL,
                                              enable bool NOT NULL DEFAULT false,
                                              isv bool NOT NULL DEFAULT false,
                                              allocatable bool NOT NULL DEFAULT false,
                                              terminal bool NOT NULL DEFAULT false,
                                              apply bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN pay_channel_const.id IS '主键';
COMMENT ON COLUMN pay_channel_const.code IS '通道编码';
COMMENT ON COLUMN pay_channel_const.name IS '通道名称';
COMMENT ON COLUMN pay_channel_const.enable IS '是否启用';
COMMENT ON COLUMN pay_channel_const.isv IS '是否支持服务商模式';
COMMENT ON COLUMN pay_channel_const.allocatable IS '是否支持分账';
COMMENT ON COLUMN pay_channel_const.terminal IS '终端报备';
COMMENT ON COLUMN pay_channel_const.apply IS '进件申请';
COMMENT ON TABLE pay_channel_const IS '支付通道常量';

-- ----------------------------
-- Table structure for pay_checkout_counter_config
-- ----------------------------
DROP TABLE IF EXISTS pay_checkout_counter_config;
CREATE TABLE pay_checkout_counter_config (
                                                        id int8 NOT NULL,
                                                        creator varchar(64) COLLATE pg_catalog.default,
                                                        create_time timestamp(6),
                                                        last_modifier varchar(64) COLLATE pg_catalog.default,
                                                        last_modified_time timestamp(6),
                                                        version int4 NOT NULL DEFAULT 0,
                                                        deleted bool NOT NULL DEFAULT false,
                                                        isv_no varchar(32) COLLATE pg_catalog.default,
                                                        mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        name varchar(100) COLLATE pg_catalog.default,
                                                        type varchar(32) COLLATE pg_catalog.default,
                                                        recommend bool NOT NULL DEFAULT false,
                                                        bg_color varchar(20) COLLATE pg_catalog.default,
                                                        border_color varchar(20) COLLATE pg_catalog.default,
                                                        font_color varchar(20) COLLATE pg_catalog.default,
                                                        icon varchar(200) COLLATE pg_catalog.default,
                                                        sort_no numeric(10,2),
                                                        channel varchar(32) COLLATE pg_catalog.default,
                                                        pay_method varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_checkout_counter_config.id IS '主键ID';
COMMENT ON COLUMN pay_checkout_counter_config.creator IS '创建者';
COMMENT ON COLUMN pay_checkout_counter_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_checkout_counter_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_checkout_counter_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_checkout_counter_config.version IS '版本号';
COMMENT ON COLUMN pay_checkout_counter_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_checkout_counter_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_checkout_counter_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_checkout_counter_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_checkout_counter_config.name IS '名称';
COMMENT ON COLUMN pay_checkout_counter_config.type IS '类型';
COMMENT ON COLUMN pay_checkout_counter_config.recommend IS '是否推荐';
COMMENT ON COLUMN pay_checkout_counter_config.bg_color IS '背景色';
COMMENT ON COLUMN pay_checkout_counter_config.border_color IS '边框色';
COMMENT ON COLUMN pay_checkout_counter_config.font_color IS '字体颜色';
COMMENT ON COLUMN pay_checkout_counter_config.icon IS '图标';
COMMENT ON COLUMN pay_checkout_counter_config.sort_no IS '排序';
COMMENT ON COLUMN pay_checkout_counter_config.channel IS '支付通道';
COMMENT ON COLUMN pay_checkout_counter_config.pay_method IS '支付方式';
COMMENT ON TABLE pay_checkout_counter_config IS '网关收银台配置项';

-- ----------------------------
-- Table structure for pay_close_record
-- ----------------------------
DROP TABLE IF EXISTS pay_close_record;
CREATE TABLE pay_close_record (
                                             id int8 NOT NULL,
                                             order_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                             biz_order_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                             channel varchar(20) COLLATE pg_catalog.default,
                                             close_type varchar(20) COLLATE pg_catalog.default NOT NULL,
                                             closed bool NOT NULL,
                                             error_code varchar(10) COLLATE pg_catalog.default,
                                             error_msg varchar(500) COLLATE pg_catalog.default,
                                             client_ip varchar(64) COLLATE pg_catalog.default,
                                             creator int8,
                                             create_time timestamp(6),
                                             mch_no varchar(32) COLLATE pg_catalog.default,
                                             app_id varchar(32) COLLATE pg_catalog.default,
                                             isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_close_record.id IS '主键';
COMMENT ON COLUMN pay_close_record.order_no IS '支付订单号';
COMMENT ON COLUMN pay_close_record.biz_order_no IS '商户支付订单号';
COMMENT ON COLUMN pay_close_record.channel IS '支付通道';
COMMENT ON COLUMN pay_close_record.close_type IS '关闭类型';
COMMENT ON COLUMN pay_close_record.closed IS '是否关闭成功';
COMMENT ON COLUMN pay_close_record.error_code IS '错误码';
COMMENT ON COLUMN pay_close_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_close_record.client_ip IS '支付终端ip';
COMMENT ON COLUMN pay_close_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_close_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_close_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_close_record.app_id IS '应用号';
COMMENT ON COLUMN pay_close_record.isv_no IS '所属服务商';
COMMENT ON TABLE pay_close_record IS '支付关闭记录';

-- ----------------------------
-- Table structure for pay_dougong_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_dougong_isv_config;
CREATE TABLE pay_dougong_isv_config (
                                                   id int8 NOT NULL,
                                                   creator int8,
                                                   create_time timestamp(6),
                                                   last_modifier int8,
                                                   last_modified_time timestamp(6),
                                                   version int4 NOT NULL DEFAULT 0,
                                                   deleted bool NOT NULL DEFAULT false,
                                                   enable bool,
                                                   sandbox bool,
                                                   sys_id varchar(64) COLLATE pg_catalog.default,
                                                   product_id varchar(32) COLLATE pg_catalog.default,
                                                   dg_public_key varchar(5000) COLLATE pg_catalog.default,
                                                   private_key varchar(2048) COLLATE pg_catalog.default,
                                                   wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                   wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                   wx_auth_url varchar(150) COLLATE pg_catalog.default,
                                                   isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_dougong_isv_config.id IS '主键';
COMMENT ON COLUMN pay_dougong_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_dougong_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_dougong_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_dougong_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_dougong_isv_config.version IS '版本号, 使用乐观锁';
COMMENT ON COLUMN pay_dougong_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_dougong_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_dougong_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_dougong_isv_config.sys_id IS '服务商系统ID';
COMMENT ON COLUMN pay_dougong_isv_config.product_id IS '产品号';
COMMENT ON COLUMN pay_dougong_isv_config.dg_public_key IS '斗拱公钥';
COMMENT ON COLUMN pay_dougong_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_dougong_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_dougong_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_dougong_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_dougong_isv_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_dougong_isv_config IS '斗拱服务商配置';

-- ----------------------------
-- Table structure for pay_dougong_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_dougong_sub_config;
CREATE TABLE pay_dougong_sub_config (
                                                   id int8 NOT NULL,
                                                   creator int8,
                                                   create_time timestamp(6),
                                                   last_modifier int8,
                                                   last_modified_time timestamp(6),
                                                   version int4 NOT NULL DEFAULT 0,
                                                   deleted bool NOT NULL DEFAULT false,
                                                   isv_no varchar(32) COLLATE pg_catalog.default,
                                                   mch_no varchar(32) COLLATE pg_catalog.default,
                                                   app_id varchar(32) COLLATE pg_catalog.default,
                                                   enable bool DEFAULT false,
                                                   merchant_no varchar(32) COLLATE pg_catalog.default,
                                                   read_system bool DEFAULT true,
                                                   wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                   wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                   wx_auth_url varchar(150) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_dougong_sub_config.id IS '主键';
COMMENT ON COLUMN pay_dougong_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_dougong_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_dougong_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_dougong_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_dougong_sub_config.version IS '版本号, 使用乐观锁';
COMMENT ON COLUMN pay_dougong_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_dougong_sub_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_dougong_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_dougong_sub_config.app_id IS '应用号';
COMMENT ON COLUMN pay_dougong_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_dougong_sub_config.merchant_no IS '支付通道商户号';
COMMENT ON COLUMN pay_dougong_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_dougong_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_dougong_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_dougong_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON TABLE pay_dougong_sub_config IS '斗拱特约商户配置';

-- ----------------------------
-- Table structure for pay_fuyou_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_fuyou_isv_config;
CREATE TABLE pay_fuyou_isv_config (
                                                 id int8 NOT NULL,
                                                 enable bool,
                                                 sandbox bool,
                                                 fy_app_id varchar(64) COLLATE pg_catalog.default,
                                                 order_prefix varchar(64) COLLATE pg_catalog.default,
                                                 private_key text COLLATE pg_catalog.default,
                                                 public_key text COLLATE pg_catalog.default,
                                                 wx_channel_auth bool,
                                                 wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                 wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                 wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                 isv_no varchar(32) COLLATE pg_catalog.default,
                                                 version int8,
                                                 creator int8,
                                                 create_time timestamp(6),
                                                 last_modifier int8,
                                                 last_modified_time timestamp(6),
                                                 deleted bool DEFAULT false,
                                                 onb_key varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_fuyou_isv_config.id IS '主键';
COMMENT ON COLUMN pay_fuyou_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_fuyou_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_fuyou_isv_config.fy_app_id IS '富友应用编号';
COMMENT ON COLUMN pay_fuyou_isv_config.order_prefix IS '富友订单前缀';
COMMENT ON COLUMN pay_fuyou_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_fuyou_isv_config.public_key IS '公钥';
COMMENT ON COLUMN pay_fuyou_isv_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_fuyou_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_fuyou_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_fuyou_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_fuyou_isv_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_fuyou_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_fuyou_isv_config.creator IS '创建人';
COMMENT ON COLUMN pay_fuyou_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_fuyou_isv_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_fuyou_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_fuyou_isv_config.deleted IS '删除标识';
COMMENT ON COLUMN pay_fuyou_isv_config.onb_key IS '进件密钥';
COMMENT ON TABLE pay_fuyou_isv_config IS '富友服务商配置';

-- ----------------------------
-- Table structure for pay_fuyou_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_fuyou_sub_config;
CREATE TABLE pay_fuyou_sub_config (
                                                 id int8 NOT NULL,
                                                 enable bool,
                                                 merchant_no varchar(32) COLLATE pg_catalog.default,
                                                 term_no varchar(32) COLLATE pg_catalog.default,
                                                 mch_no varchar(32) COLLATE pg_catalog.default,
                                                 app_id varchar(32) COLLATE pg_catalog.default,
                                                 read_system bool,
                                                 wx_channel_auth bool,
                                                 wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                 wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                 wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                 version int8,
                                                 creator int8,
                                                 create_time timestamp(6),
                                                 last_modifier int8,
                                                 last_modified_time timestamp(6),
                                                 deleted bool DEFAULT false,
                                                 isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_fuyou_sub_config.id IS '主键';
COMMENT ON COLUMN pay_fuyou_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_fuyou_sub_config.merchant_no IS '富友商户编号';
COMMENT ON COLUMN pay_fuyou_sub_config.term_no IS '终端号';
COMMENT ON COLUMN pay_fuyou_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_fuyou_sub_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_fuyou_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_fuyou_sub_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_fuyou_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_fuyou_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_fuyou_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_fuyou_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_fuyou_sub_config.creator IS '创建人';
COMMENT ON COLUMN pay_fuyou_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_fuyou_sub_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_fuyou_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_fuyou_sub_config.deleted IS '删除标识';
COMMENT ON COLUMN pay_fuyou_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_fuyou_sub_config IS '富友子商户配置';

-- ----------------------------
-- Table structure for pay_gateway_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_gateway_pay_config;
CREATE TABLE pay_gateway_pay_config (
                                                   id int8 NOT NULL,
                                                   creator varchar(64) COLLATE pg_catalog.default,
                                                   create_time timestamp(6),
                                                   last_modifier varchar(64) COLLATE pg_catalog.default,
                                                   last_modified_time timestamp(6),
                                                   version int4 NOT NULL DEFAULT 0,
                                                   deleted bool NOT NULL DEFAULT false,
                                                   isv_no varchar(32) COLLATE pg_catalog.default,
                                                   mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                   app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                   aggregate_qr_show bool NOT NULL DEFAULT false,
                                                   h5_auto_upgrade bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN pay_gateway_pay_config.id IS '主键ID';
COMMENT ON COLUMN pay_gateway_pay_config.creator IS '创建者';
COMMENT ON COLUMN pay_gateway_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_pay_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_gateway_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_gateway_pay_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_gateway_pay_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_gateway_pay_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_gateway_pay_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_gateway_pay_config.aggregate_qr_show IS 'PC收银台是否同时显示聚合收银码';
COMMENT ON COLUMN pay_gateway_pay_config.h5_auto_upgrade IS 'h5收银台自动升级聚合支付';
COMMENT ON TABLE pay_gateway_pay_config IS '网关支付配置';

-- ----------------------------
-- Table structure for pay_gateway_pay_read_config
-- ----------------------------
DROP TABLE IF EXISTS pay_gateway_pay_read_config;
CREATE TABLE pay_gateway_pay_read_config (
                                                        id int8 NOT NULL,
                                                        creator varchar(64) COLLATE pg_catalog.default,
                                                        create_time timestamp(6),
                                                        last_modifier varchar(64) COLLATE pg_catalog.default,
                                                        last_modified_time timestamp(6),
                                                        version int4 NOT NULL DEFAULT 0,
                                                        deleted bool NOT NULL DEFAULT false,
                                                        isv_no varchar(32) COLLATE pg_catalog.default,
                                                        mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                        gateway_read_system bool NOT NULL DEFAULT false,
                                                        h5_read_system bool NOT NULL DEFAULT false,
                                                        pc_read_system bool NOT NULL DEFAULT false,
                                                        aggregate_qr_read_system bool NOT NULL DEFAULT false,
                                                        aggregate_bar_read_system bool NOT NULL DEFAULT false,
                                                        mini_quickly_read_system bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN pay_gateway_pay_read_config.id IS '主键ID';
COMMENT ON COLUMN pay_gateway_pay_read_config.creator IS '创建者';
COMMENT ON COLUMN pay_gateway_pay_read_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_pay_read_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_gateway_pay_read_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_pay_read_config.version IS '版本号';
COMMENT ON COLUMN pay_gateway_pay_read_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_gateway_pay_read_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_gateway_pay_read_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_gateway_pay_read_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_gateway_pay_read_config.gateway_read_system IS '网关支付是否读取系统';
COMMENT ON COLUMN pay_gateway_pay_read_config.h5_read_system IS 'H5收银台读取系统';
COMMENT ON COLUMN pay_gateway_pay_read_config.pc_read_system IS 'Pc收银台读取系统';
COMMENT ON COLUMN pay_gateway_pay_read_config.aggregate_qr_read_system IS '聚合扫码支付读取系统';
COMMENT ON COLUMN pay_gateway_pay_read_config.aggregate_bar_read_system IS '聚合付款码支付读取系统';
COMMENT ON COLUMN pay_gateway_pay_read_config.mini_quickly_read_system IS '小程序快捷支付读取系统';
COMMENT ON TABLE pay_gateway_pay_read_config IS '网关支付读取配置';

-- ----------------------------
-- Table structure for pay_hkrt_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_hkrt_isv_config;
CREATE TABLE pay_hkrt_isv_config (
                                                id int8 NOT NULL,
                                                creator int8,
                                                create_time timestamp(6),
                                                last_modifier int8,
                                                last_modified_time timestamp(6),
                                                version int4 NOT NULL,
                                                deleted bool NOT NULL,
                                                access_id varchar(64) COLLATE pg_catalog.default,
                                                wx_channel_no varchar(32) COLLATE pg_catalog.default,
                                                ali_channel_no varchar(32) COLLATE pg_catalog.default,
                                                access_key text COLLATE pg_catalog.default,
                                                transfer_key text COLLATE pg_catalog.default,
                                                enable bool,
                                                sandbox bool,
                                                trade_url varchar(200) COLLATE pg_catalog.default,
                                                other_url varchar(200) COLLATE pg_catalog.default,
                                                isv_no varchar(32) COLLATE pg_catalog.default,
                                                wx_channel_auth bool,
                                                wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                hkrt_agent_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_hkrt_isv_config.id IS '主键';
COMMENT ON COLUMN pay_hkrt_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_hkrt_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_hkrt_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_hkrt_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_hkrt_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_hkrt_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_hkrt_isv_config.access_id IS '接入机构标识';
COMMENT ON COLUMN pay_hkrt_isv_config.wx_channel_no IS '微信渠道号';
COMMENT ON COLUMN pay_hkrt_isv_config.ali_channel_no IS '支付宝渠道号';
COMMENT ON COLUMN pay_hkrt_isv_config.access_key IS '密钥';
COMMENT ON COLUMN pay_hkrt_isv_config.transfer_key IS '传输密钥';
COMMENT ON COLUMN pay_hkrt_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_hkrt_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_hkrt_isv_config.trade_url IS '交易API网关';
COMMENT ON COLUMN pay_hkrt_isv_config.other_url IS '其他API网关';
COMMENT ON COLUMN pay_hkrt_isv_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_hkrt_isv_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_hkrt_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_hkrt_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_hkrt_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_hkrt_isv_config.hkrt_agent_no IS '服务商编号';
COMMENT ON TABLE pay_hkrt_isv_config IS '海科服务商配置';

-- ----------------------------
-- Table structure for pay_hkrt_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_hkrt_sub_config;
CREATE TABLE pay_hkrt_sub_config (
                                                id int8 NOT NULL,
                                                creator int8,
                                                create_time timestamp(6),
                                                last_modifier int8,
                                                last_modified_time timestamp(6),
                                                version int4 NOT NULL,
                                                deleted bool NOT NULL,
                                                merch_no varchar(64) COLLATE pg_catalog.default,
                                                pn varchar(32) COLLATE pg_catalog.default,
                                                enable bool,
                                                app_id varchar(32) COLLATE pg_catalog.default,
                                                read_system bool,
                                                wx_channel_auth bool,
                                                wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                mch_no varchar(32) COLLATE pg_catalog.default,
                                                isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_hkrt_sub_config.id IS '主键';
COMMENT ON COLUMN pay_hkrt_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_hkrt_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_hkrt_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_hkrt_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_hkrt_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_hkrt_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_hkrt_sub_config.merch_no IS '商户编号';
COMMENT ON COLUMN pay_hkrt_sub_config.pn IS 'SAAS终端号';
COMMENT ON COLUMN pay_hkrt_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_hkrt_sub_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_hkrt_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_hkrt_sub_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_hkrt_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_hkrt_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_hkrt_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_hkrt_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_hkrt_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_hkrt_sub_config IS '海科子商户配置';

-- ----------------------------
-- Table structure for pay_isv_aggregate_bar_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_aggregate_bar_pay_config;
CREATE TABLE pay_isv_aggregate_bar_pay_config (
                                                             id int8 NOT NULL,
                                                             creator varchar(64) COLLATE pg_catalog.default,
                                                             create_time timestamp(6),
                                                             last_modifier varchar(64) COLLATE pg_catalog.default,
                                                             last_modified_time timestamp(6),
                                                             version int4 NOT NULL DEFAULT 0,
                                                             deleted bool NOT NULL DEFAULT false,
                                                             wx_channel varchar(32) COLLATE pg_catalog.default,
                                                             wx_method varchar(32) COLLATE pg_catalog.default,
                                                             alipay_channel varchar(32) COLLATE pg_catalog.default,
                                                             alipay_method varchar(32) COLLATE pg_catalog.default,
                                                             union_channel varchar(32) COLLATE pg_catalog.default,
                                                             union_method varchar(32) COLLATE pg_catalog.default,
                                                             isv_no varchar(32) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.id IS '主键ID';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.creator IS '创建者';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.union_method IS '银联场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_bar_pay_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_isv_aggregate_bar_pay_config IS 'ISV网关聚合付款码支付配置';

-- ----------------------------
-- Table structure for pay_isv_aggregate_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_aggregate_pay_config;
CREATE TABLE pay_isv_aggregate_pay_config (
                                                         id int8 NOT NULL,
                                                         creator int8,
                                                         create_time timestamp(6),
                                                         last_modifier int8,
                                                         last_modified_time timestamp(6),
                                                         version int4 NOT NULL,
                                                         deleted bool NOT NULL,
                                                         aggregate_type varchar(32) COLLATE pg_catalog.default,
                                                         channel varchar(32) COLLATE pg_catalog.default,
                                                         pay_method varchar(32) COLLATE pg_catalog.default,
                                                         auto_launch bool,
                                                         other_method varchar(32) COLLATE pg_catalog.default,
                                                         mch_no varchar(32) COLLATE pg_catalog.default,
                                                         need_open_id bool,
                                                         call_type varchar(32) COLLATE pg_catalog.default,
                                                         open_id_get_type varchar(32) COLLATE pg_catalog.default,
                                                         isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_isv_aggregate_pay_config.id IS '主键';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.last_modifier IS '最后修者ID';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.version IS '乐观锁';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.aggregate_type IS '聚合支付类型';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.channel IS '通道';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.pay_method IS '支付方式';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.auto_launch IS '自动拉起支付';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.other_method IS '其他支付方式';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.need_open_id IS '需要获取OpenId';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.call_type IS '调用方式';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.open_id_get_type IS 'OpenId获取方式';
COMMENT ON COLUMN pay_isv_aggregate_pay_config.isv_no IS '所属服务商';
COMMENT ON TABLE pay_isv_aggregate_pay_config IS '网关聚合支付配置';

-- ----------------------------
-- Table structure for pay_isv_aggregate_qr_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_aggregate_qr_pay_config;
CREATE TABLE pay_isv_aggregate_qr_pay_config (
                                                            id int8 NOT NULL,
                                                            creator varchar(64) COLLATE pg_catalog.default,
                                                            create_time timestamp(6),
                                                            last_modifier varchar(64) COLLATE pg_catalog.default,
                                                            last_modified_time timestamp(6),
                                                            version int4 NOT NULL DEFAULT 0,
                                                            deleted bool NOT NULL DEFAULT false,
                                                            auto_launch bool NOT NULL DEFAULT false,
                                                            wx_channel varchar(32) COLLATE pg_catalog.default,
                                                            wx_method varchar(32) COLLATE pg_catalog.default,
                                                            alipay_channel varchar(32) COLLATE pg_catalog.default,
                                                            alipay_method varchar(32) COLLATE pg_catalog.default,
                                                            union_channel varchar(32) COLLATE pg_catalog.default,
                                                            union_method varchar(32) COLLATE pg_catalog.default,
                                                            isv_no varchar(32) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.id IS '主键ID';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.creator IS '创建者';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.auto_launch IS '自动拉起支付';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.wx_channel IS '微信场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.wx_method IS '微信场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.alipay_channel IS '支付宝场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.alipay_method IS '支付宝场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.union_channel IS '银联场景对应通道';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.union_method IS '银联场景对应支付方式';
COMMENT ON COLUMN pay_isv_aggregate_qr_pay_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_isv_aggregate_qr_pay_config IS 'ISV网关聚合扫码支付配置';

-- ----------------------------
-- Table structure for pay_isv_channel_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_channel_config;
CREATE TABLE pay_isv_channel_config (
                                                   id int8 NOT NULL,
                                                   channel varchar(32) COLLATE pg_catalog.default,
                                                   out_isv_no varchar(32) COLLATE pg_catalog.default,
                                                   enable bool,
                                                   ext text COLLATE pg_catalog.default,
                                                   creator int8,
                                                   create_time timestamp(6),
                                                   last_modifier int8,
                                                   last_modified_time timestamp(6),
                                                   version int4 NOT NULL,
                                                   deleted bool NOT NULL,
                                                   isv_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                   onb_apply bool,
                                                   settle_cycle int2
)
;
COMMENT ON COLUMN pay_isv_channel_config.id IS '主键';
COMMENT ON COLUMN pay_isv_channel_config.channel IS '支付通道';
COMMENT ON COLUMN pay_isv_channel_config.out_isv_no IS '通道服务商号';
COMMENT ON COLUMN pay_isv_channel_config.enable IS '是否启用';
COMMENT ON COLUMN pay_isv_channel_config.ext IS '扩展存储';
COMMENT ON COLUMN pay_isv_channel_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_isv_channel_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_channel_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_isv_channel_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_channel_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_channel_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_isv_channel_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_isv_channel_config.onb_apply IS '是否可以进件';
COMMENT ON COLUMN pay_isv_channel_config.settle_cycle IS '结算周期';
COMMENT ON TABLE pay_isv_channel_config IS '服务商通道配置';

-- ----------------------------
-- Table structure for pay_isv_checkout_counter_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_checkout_counter_config;
CREATE TABLE pay_isv_checkout_counter_config (
                                                            id int8 NOT NULL,
                                                            creator varchar(64) COLLATE pg_catalog.default,
                                                            create_time timestamp(6),
                                                            last_modifier varchar(64) COLLATE pg_catalog.default,
                                                            last_modified_time timestamp(6),
                                                            version int4 NOT NULL DEFAULT 0,
                                                            deleted bool NOT NULL DEFAULT false,
                                                            type varchar(32) COLLATE pg_catalog.default,
                                                            recommend bool NOT NULL DEFAULT false,
                                                            bg_color varchar(20) COLLATE pg_catalog.default,
                                                            border_color varchar(20) COLLATE pg_catalog.default,
                                                            font_color varchar(20) COLLATE pg_catalog.default,
                                                            icon varchar(200) COLLATE pg_catalog.default,
                                                            sort_no numeric(10,2),
                                                            channel varchar(32) COLLATE pg_catalog.default,
                                                            pay_method varchar(32) COLLATE pg_catalog.default,
                                                            isv_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                            name varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_isv_checkout_counter_config.id IS '主键ID';
COMMENT ON COLUMN pay_isv_checkout_counter_config.creator IS '创建者';
COMMENT ON COLUMN pay_isv_checkout_counter_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_checkout_counter_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_isv_checkout_counter_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_checkout_counter_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_checkout_counter_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_isv_checkout_counter_config.type IS '类型';
COMMENT ON COLUMN pay_isv_checkout_counter_config.recommend IS '是否推荐';
COMMENT ON COLUMN pay_isv_checkout_counter_config.bg_color IS '背景色';
COMMENT ON COLUMN pay_isv_checkout_counter_config.border_color IS '边框色';
COMMENT ON COLUMN pay_isv_checkout_counter_config.font_color IS '字体颜色';
COMMENT ON COLUMN pay_isv_checkout_counter_config.icon IS '图标';
COMMENT ON COLUMN pay_isv_checkout_counter_config.sort_no IS '排序';
COMMENT ON COLUMN pay_isv_checkout_counter_config.channel IS '支付通道';
COMMENT ON COLUMN pay_isv_checkout_counter_config.pay_method IS '支付方式';
COMMENT ON COLUMN pay_isv_checkout_counter_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_isv_checkout_counter_config.name IS '名称';
COMMENT ON TABLE pay_isv_checkout_counter_config IS 'ISV网关收银台配置项';

-- ----------------------------
-- Table structure for pay_isv_gateway_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_gateway_config;
CREATE TABLE pay_isv_gateway_config (
                                                   id int8 NOT NULL,
                                                   creator varchar(64) COLLATE pg_catalog.default,
                                                   create_time timestamp(6),
                                                   last_modifier varchar(64) COLLATE pg_catalog.default,
                                                   last_modified_time timestamp(6),
                                                   version int4 NOT NULL DEFAULT 0,
                                                   deleted bool NOT NULL DEFAULT false,
                                                   aggregate_show bool NOT NULL DEFAULT false,
                                                   h5_auto_upgrade bool NOT NULL DEFAULT false,
                                                   isv_no varchar(32) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN pay_isv_gateway_config.id IS '主键ID';
COMMENT ON COLUMN pay_isv_gateway_config.creator IS '创建者';
COMMENT ON COLUMN pay_isv_gateway_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_gateway_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_isv_gateway_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_gateway_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_gateway_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_isv_gateway_config.aggregate_show IS 'PC收银台是否同时显示聚合收银码';
COMMENT ON COLUMN pay_isv_gateway_config.h5_auto_upgrade IS 'h5收银台自动升级聚合支付';
COMMENT ON COLUMN pay_isv_gateway_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_isv_gateway_config IS 'ISV网关支付配置';

-- ----------------------------
-- Table structure for pay_isv_info
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_info;
CREATE TABLE pay_isv_info (
                                         id int8 NOT NULL,
                                         creator int8,
                                         create_time timestamp(6),
                                         last_modifier int8,
                                         last_modified_time timestamp(6),
                                         version int4 NOT NULL,
                                         deleted bool NOT NULL,
                                         status varchar(32) COLLATE pg_catalog.default,
                                         name varchar(64) COLLATE pg_catalog.default,
                                         isv_no varchar(32) COLLATE pg_catalog.default,
                                         short_name varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_isv_info.id IS '主键';
COMMENT ON COLUMN pay_isv_info.creator IS '创建者ID';
COMMENT ON COLUMN pay_isv_info.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_info.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_isv_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_info.version IS '版本号';
COMMENT ON COLUMN pay_isv_info.deleted IS '删除标志';
COMMENT ON COLUMN pay_isv_info.status IS '状态';
COMMENT ON COLUMN pay_isv_info.name IS '名称';
COMMENT ON COLUMN pay_isv_info.isv_no IS '服务商号';
COMMENT ON COLUMN pay_isv_info.short_name IS '简称';
COMMENT ON TABLE pay_isv_info IS '服务商信息';

-- ----------------------------
-- Table structure for pay_isv_mini_quickly_config
-- ----------------------------
DROP TABLE IF EXISTS pay_isv_mini_quickly_config;
CREATE TABLE pay_isv_mini_quickly_config (
                                                        id int8 NOT NULL,
                                                        creator varchar(64) COLLATE pg_catalog.default,
                                                        create_time timestamp(6),
                                                        last_modifier varchar(64) COLLATE pg_catalog.default,
                                                        last_modified_time timestamp(6),
                                                        version int4 NOT NULL DEFAULT 0,
                                                        deleted bool NOT NULL DEFAULT false,
                                                        mini_app_allocation bool NOT NULL DEFAULT false,
                                                        mini_app_auto_allocation bool NOT NULL DEFAULT false,
                                                        mini_app_limit_pay varchar(512) COLLATE pg_catalog.default,
                                                        terminal_no varchar(32) COLLATE pg_catalog.default,
                                                        isv_no varchar(32) COLLATE pg_catalog.default NOT NULL
)
;
COMMENT ON COLUMN pay_isv_mini_quickly_config.id IS '主键ID';
COMMENT ON COLUMN pay_isv_mini_quickly_config.creator IS '创建者';
COMMENT ON COLUMN pay_isv_mini_quickly_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_isv_mini_quickly_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_isv_mini_quickly_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_isv_mini_quickly_config.version IS '版本号';
COMMENT ON COLUMN pay_isv_mini_quickly_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_isv_mini_quickly_config.mini_app_allocation IS '小程序开启分账';
COMMENT ON COLUMN pay_isv_mini_quickly_config.mini_app_auto_allocation IS '小程序自动分账';
COMMENT ON COLUMN pay_isv_mini_quickly_config.mini_app_limit_pay IS '限制小程序支付方式';
COMMENT ON COLUMN pay_isv_mini_quickly_config.terminal_no IS '终端号';
COMMENT ON COLUMN pay_isv_mini_quickly_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_isv_mini_quickly_config IS 'ISV小程序快捷支付配置';

-- ----------------------------
-- Table structure for pay_lakala_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_lakala_isv_config;
CREATE TABLE pay_lakala_isv_config (
                                                  id int8 NOT NULL,
                                                  enable bool,
                                                  sandbox bool,
                                                  lkl_app_id varchar(32) COLLATE pg_catalog.default,
                                                  mch_serial_no varchar(32) COLLATE pg_catalog.default,
                                                  private_key text COLLATE pg_catalog.default,
                                                  public_key text COLLATE pg_catalog.default,
                                                  sm4_key text COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default,
                                                  version int8,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  deleted bool DEFAULT false
)
;
COMMENT ON COLUMN pay_lakala_isv_config.id IS '主键';
COMMENT ON COLUMN pay_lakala_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_lakala_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_lakala_isv_config.lkl_app_id IS '拉卡拉应用编号';
COMMENT ON COLUMN pay_lakala_isv_config.mch_serial_no IS '商户证书序列号';
COMMENT ON COLUMN pay_lakala_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_lakala_isv_config.public_key IS '公钥';
COMMENT ON COLUMN pay_lakala_isv_config.sm4_key IS 'sm密钥';
COMMENT ON COLUMN pay_lakala_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_lakala_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_lakala_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_lakala_isv_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_lakala_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_lakala_isv_config.creator IS '创建人';
COMMENT ON COLUMN pay_lakala_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_lakala_isv_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_lakala_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_lakala_isv_config.deleted IS '删除标识';
COMMENT ON TABLE pay_lakala_isv_config IS '拉卡拉服务商配置';

-- ----------------------------
-- Table structure for pay_lakala_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_lakala_sub_config;
CREATE TABLE pay_lakala_sub_config (
                                                  id int8 NOT NULL,
                                                  enable bool,
                                                  merchant_no varchar(32) COLLATE pg_catalog.default,
                                                  term_no varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  read_system bool,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  version int8,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  deleted bool DEFAULT false,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_lakala_sub_config.id IS '主键';
COMMENT ON COLUMN pay_lakala_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_lakala_sub_config.merchant_no IS '拉卡拉商户编号';
COMMENT ON COLUMN pay_lakala_sub_config.term_no IS '终端号';
COMMENT ON COLUMN pay_lakala_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_lakala_sub_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_lakala_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_lakala_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_lakala_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_lakala_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_lakala_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_lakala_sub_config.creator IS '创建人';
COMMENT ON COLUMN pay_lakala_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_lakala_sub_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_lakala_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_lakala_sub_config.deleted IS '删除标识';
COMMENT ON COLUMN pay_lakala_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_lakala_sub_config IS '拉卡拉子商户配置';

-- ----------------------------
-- Table structure for pay_leshua_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_leshua_isv_config;
CREATE TABLE pay_leshua_isv_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  ls_isv_no varchar(32) COLLATE pg_catalog.default,
                                                  trade_key varchar(128) COLLATE pg_catalog.default,
                                                  notify_key varchar(128) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  sandbox bool,
                                                  sign_type varchar(10) COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_leshua_isv_config.id IS '主键';
COMMENT ON COLUMN pay_leshua_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_leshua_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_leshua_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_leshua_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_leshua_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_leshua_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_leshua_isv_config.ls_isv_no IS '乐刷服务商号';
COMMENT ON COLUMN pay_leshua_isv_config.trade_key IS '交易KEY';
COMMENT ON COLUMN pay_leshua_isv_config.notify_key IS '异步通知key';
COMMENT ON COLUMN pay_leshua_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_leshua_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_leshua_isv_config.sign_type IS '签名类型 MD5/SM3';
COMMENT ON COLUMN pay_leshua_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_leshua_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_leshua_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_leshua_isv_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_leshua_isv_config IS '乐刷服务商配置';

-- ----------------------------
-- Table structure for pay_leshua_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_leshua_sub_config;
CREATE TABLE pay_leshua_sub_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  ls_mch_no varchar(32) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  read_system bool,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_leshua_sub_config.id IS '主键';
COMMENT ON COLUMN pay_leshua_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_leshua_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_leshua_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_leshua_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_leshua_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_leshua_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_leshua_sub_config.app_id IS '应用号';
COMMENT ON COLUMN pay_leshua_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_leshua_sub_config.ls_mch_no IS '乐刷商户号';
COMMENT ON COLUMN pay_leshua_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_leshua_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_leshua_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_leshua_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_leshua_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_leshua_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_leshua_sub_config IS '乐刷子商户配置';

-- ----------------------------
-- Table structure for pay_mch_app
-- ----------------------------
DROP TABLE IF EXISTS pay_mch_app;
CREATE TABLE pay_mch_app (
                                        id int8 NOT NULL,
                                        mch_no varchar(32) COLLATE pg_catalog.default,
                                        app_id varchar(32) COLLATE pg_catalog.default,
                                        app_name varchar(64) COLLATE pg_catalog.default,
                                        sign_type varchar(32) COLLATE pg_catalog.default,
                                        sign_secret varchar(500) COLLATE pg_catalog.default,
                                        req_sign bool,
                                        limit_amount numeric(16,4),
                                        status varchar(32) COLLATE pg_catalog.default,
                                        notify_type varchar(32) COLLATE pg_catalog.default,
                                        notify_url varchar(200) COLLATE pg_catalog.default,
                                        creator int8,
                                        create_time timestamp(6),
                                        last_modifier int8,
                                        last_modified_time timestamp(6),
                                        version int4 NOT NULL,
                                        deleted bool NOT NULL,
                                        order_timeout int2,
                                        merchant_type varchar(32) COLLATE pg_catalog.default,
                                        req_timeout bool,
                                        req_timeout_second int4,
                                        isv_no varchar(32) COLLATE pg_catalog.default,
                                        default_app bool
)
;
COMMENT ON COLUMN pay_mch_app.id IS '主键';
COMMENT ON COLUMN pay_mch_app.mch_no IS '商户号';
COMMENT ON COLUMN pay_mch_app.app_id IS '应用号';
COMMENT ON COLUMN pay_mch_app.app_name IS '应用名称';
COMMENT ON COLUMN pay_mch_app.sign_type IS '签名方式';
COMMENT ON COLUMN pay_mch_app.sign_secret IS '签名秘钥';
COMMENT ON COLUMN pay_mch_app.req_sign IS '是否对请求进行验签';
COMMENT ON COLUMN pay_mch_app.limit_amount IS '支付限额(元)';
COMMENT ON COLUMN pay_mch_app.status IS '应用状态';
COMMENT ON COLUMN pay_mch_app.notify_type IS '异步消息通知类型';
COMMENT ON COLUMN pay_mch_app.notify_url IS '通知地址';
COMMENT ON COLUMN pay_mch_app.creator IS '创建者ID';
COMMENT ON COLUMN pay_mch_app.create_time IS '创建时间';
COMMENT ON COLUMN pay_mch_app.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_mch_app.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_mch_app.version IS '版本号';
COMMENT ON COLUMN pay_mch_app.deleted IS '删除标志';
COMMENT ON COLUMN pay_mch_app.order_timeout IS '订单默认超时时间(分钟)';
COMMENT ON COLUMN pay_mch_app.merchant_type IS '商户类型';
COMMENT ON COLUMN pay_mch_app.req_timeout IS '是否验证请求时间是否超时';
COMMENT ON COLUMN pay_mch_app.req_timeout_second IS '请求超时时间(秒)';
COMMENT ON COLUMN pay_mch_app.isv_no IS '服务商号';
COMMENT ON COLUMN pay_mch_app.default_app IS '默认应用';
COMMENT ON TABLE pay_mch_app IS '商户应用';

-- ----------------------------
-- Table structure for pay_merchant
-- ----------------------------
DROP TABLE IF EXISTS pay_merchant;
CREATE TABLE pay_merchant (
                                         id int8 NOT NULL,
                                         mch_no varchar(64) COLLATE pg_catalog.default NOT NULL,
                                         mch_name varchar(100) COLLATE pg_catalog.default NOT NULL,
                                         company_name varchar(100) COLLATE pg_catalog.default,
                                         id_type varchar(32) COLLATE pg_catalog.default,
                                         id_no varchar COLLATE pg_catalog.default,
                                         contact varchar(100) COLLATE pg_catalog.default,
                                         legal_person varchar(100) COLLATE pg_catalog.default,
                                         status varchar(10) COLLATE pg_catalog.default,
                                         creator int8,
                                         create_time timestamp(0),
                                         last_modifier int8,
                                         last_modified_time timestamp(0),
                                         version int4 NOT NULL,
                                         deleted bool NOT NULL,
                                         company_contact varchar(150) COLLATE pg_catalog.default,
                                         company_address varchar(150) COLLATE pg_catalog.default,
                                         company_code varchar(50) COLLATE pg_catalog.default,
                                         administrator bool NOT NULL,
                                         admin_user_id int8,
                                         isv_no varchar(32) COLLATE pg_catalog.default,
                                         merchant_type varchar(32) COLLATE pg_catalog.default,
                                         mch_short_name varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_merchant.id IS '主键';
COMMENT ON COLUMN pay_merchant.mch_no IS '商户号';
COMMENT ON COLUMN pay_merchant.mch_name IS '商户名称';
COMMENT ON COLUMN pay_merchant.company_name IS '公司名称';
COMMENT ON COLUMN pay_merchant.id_type IS '法人证件类型';
COMMENT ON COLUMN pay_merchant.id_no IS '法人证件号';
COMMENT ON COLUMN pay_merchant.contact IS '法人联系方式';
COMMENT ON COLUMN pay_merchant.legal_person IS '法人名称';
COMMENT ON COLUMN pay_merchant.status IS '状态';
COMMENT ON COLUMN pay_merchant.creator IS '创建者ID';
COMMENT ON COLUMN pay_merchant.create_time IS '创建时间';
COMMENT ON COLUMN pay_merchant.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_merchant.version IS '版本号';
COMMENT ON COLUMN pay_merchant.deleted IS '删除标志';
COMMENT ON COLUMN pay_merchant.company_contact IS '公司联系方式';
COMMENT ON COLUMN pay_merchant.company_address IS '公司地址';
COMMENT ON COLUMN pay_merchant.company_code IS '公司信用编码';
COMMENT ON COLUMN pay_merchant.administrator IS '是否有关联管理员';
COMMENT ON COLUMN pay_merchant.admin_user_id IS '关联管理员用户';
COMMENT ON COLUMN pay_merchant.isv_no IS '所属服务商';
COMMENT ON COLUMN pay_merchant.merchant_type IS '商户类型';
COMMENT ON COLUMN pay_merchant.mch_short_name IS '商户简称';
COMMENT ON TABLE pay_merchant IS '商户';

-- ----------------------------
-- Table structure for pay_merchant_callback_record
-- ----------------------------
DROP TABLE IF EXISTS pay_merchant_callback_record;
CREATE TABLE pay_merchant_callback_record (
                                                         id int8 NOT NULL,
                                                         creator int8,
                                                         create_time timestamp(6),
                                                         mch_no varchar(32) COLLATE pg_catalog.default,
                                                         app_id varchar(32) COLLATE pg_catalog.default,
                                                         task_id int8,
                                                         req_count int4,
                                                         success bool,
                                                         send_type varchar(30) COLLATE pg_catalog.default,
                                                         error_code varchar(50) COLLATE pg_catalog.default,
                                                         error_msg varchar(500) COLLATE pg_catalog.default,
                                                         isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_merchant_callback_record.id IS '主键';
COMMENT ON COLUMN pay_merchant_callback_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_merchant_callback_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_merchant_callback_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_merchant_callback_record.app_id IS '应用号';
COMMENT ON COLUMN pay_merchant_callback_record.task_id IS '任务ID';
COMMENT ON COLUMN pay_merchant_callback_record.req_count IS '请求次数';
COMMENT ON COLUMN pay_merchant_callback_record.success IS '发送是否成功';
COMMENT ON COLUMN pay_merchant_callback_record.send_type IS '发送类型, 自动发送, 手动发送';
COMMENT ON COLUMN pay_merchant_callback_record.error_code IS '错误码';
COMMENT ON COLUMN pay_merchant_callback_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_merchant_callback_record.isv_no IS '所属服务商';
COMMENT ON TABLE pay_merchant_callback_record IS '客户回调消息发送记录';

-- ----------------------------
-- Table structure for pay_merchant_callback_task
-- ----------------------------
DROP TABLE IF EXISTS pay_merchant_callback_task;
CREATE TABLE pay_merchant_callback_task (
                                                       id int8 NOT NULL,
                                                       creator int8,
                                                       create_time timestamp(6),
                                                       last_modifier int8,
                                                       last_modified_time timestamp(6),
                                                       version int4 NOT NULL,
                                                       deleted bool NOT NULL,
                                                       trade_id int8,
                                                       trade_no varchar(32) COLLATE pg_catalog.default,
                                                       trade_type varchar(20) COLLATE pg_catalog.default,
                                                       content text COLLATE pg_catalog.default,
                                                       success bool,
                                                       next_time timestamp(6),
                                                       send_count int4,
                                                       delay_count int4,
                                                       latest_time timestamp(6),
                                                       mch_no varchar(32) COLLATE pg_catalog.default,
                                                       app_id varchar(32) COLLATE pg_catalog.default,
                                                       url varchar(200) COLLATE pg_catalog.default,
                                                       isv_no varchar(64) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_merchant_callback_task.id IS '主键';
COMMENT ON COLUMN pay_merchant_callback_task.creator IS '创建者ID';
COMMENT ON COLUMN pay_merchant_callback_task.create_time IS '创建时间';
COMMENT ON COLUMN pay_merchant_callback_task.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_merchant_callback_task.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_merchant_callback_task.version IS '版本号';
COMMENT ON COLUMN pay_merchant_callback_task.deleted IS '删除标志';
COMMENT ON COLUMN pay_merchant_callback_task.trade_id IS '本地交易ID';
COMMENT ON COLUMN pay_merchant_callback_task.trade_no IS '本地交易号';
COMMENT ON COLUMN pay_merchant_callback_task.trade_type IS '通知类型';
COMMENT ON COLUMN pay_merchant_callback_task.content IS '消息内容';
COMMENT ON COLUMN pay_merchant_callback_task.success IS '是否发送成功';
COMMENT ON COLUMN pay_merchant_callback_task.next_time IS '发送次数';
COMMENT ON COLUMN pay_merchant_callback_task.send_count IS '延迟次数';
COMMENT ON COLUMN pay_merchant_callback_task.delay_count IS '下次发送时间';
COMMENT ON COLUMN pay_merchant_callback_task.latest_time IS '最后发送时间';
COMMENT ON COLUMN pay_merchant_callback_task.mch_no IS '商户号';
COMMENT ON COLUMN pay_merchant_callback_task.app_id IS '应用号';
COMMENT ON COLUMN pay_merchant_callback_task.url IS '发送地址';
COMMENT ON COLUMN pay_merchant_callback_task.isv_no IS '所属服务商';
COMMENT ON TABLE pay_merchant_callback_task IS '客户回调通知消息任务';

-- ----------------------------
-- Table structure for pay_merchant_credential
-- ----------------------------
DROP TABLE IF EXISTS pay_merchant_credential;
CREATE TABLE pay_merchant_credential (
                                                    id int8 NOT NULL,
                                                    creator int8,
                                                    create_time timestamp(6),
                                                    last_modifier int8,
                                                    last_modified_time timestamp(6),
                                                    version int4 DEFAULT 0,
                                                    deleted bool DEFAULT false,
                                                    isv_no varchar(32) COLLATE pg_catalog.default,
                                                    mch_no varchar(32) COLLATE pg_catalog.default,
                                                    public_key text COLLATE pg_catalog.default,
                                                    secret_key text COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_merchant_credential.id IS '主键ID';
COMMENT ON COLUMN pay_merchant_credential.creator IS '创建者ID';
COMMENT ON COLUMN pay_merchant_credential.create_time IS '创建时间';
COMMENT ON COLUMN pay_merchant_credential.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_merchant_credential.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_merchant_credential.version IS '版本号';
COMMENT ON COLUMN pay_merchant_credential.deleted IS '删除标志';
COMMENT ON COLUMN pay_merchant_credential.isv_no IS '服务商号';
COMMENT ON COLUMN pay_merchant_credential.mch_no IS '商户号';
COMMENT ON COLUMN pay_merchant_credential.public_key IS '商户公钥';
COMMENT ON COLUMN pay_merchant_credential.secret_key IS '通信密钥';
COMMENT ON TABLE pay_merchant_credential IS '商户API对接配置';

-- ----------------------------
-- Table structure for pay_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS pay_merchant_user;
CREATE TABLE pay_merchant_user (
                                              id int8 NOT NULL,
                                              user_id int8 NOT NULL,
                                              mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                              creator int8,
                                              create_time timestamp(6),
                                              administrator bool NOT NULL
)
;
COMMENT ON COLUMN pay_merchant_user.id IS '主键';
COMMENT ON COLUMN pay_merchant_user.user_id IS '用户ID';
COMMENT ON COLUMN pay_merchant_user.mch_no IS '商户号';
COMMENT ON COLUMN pay_merchant_user.creator IS '创建者ID';
COMMENT ON COLUMN pay_merchant_user.create_time IS '创建时间';
COMMENT ON COLUMN pay_merchant_user.administrator IS '是否为商户管理员';
COMMENT ON TABLE pay_merchant_user IS '用户商户关联关系';

-- ----------------------------
-- Table structure for pay_mini_quickly_config
-- ----------------------------
DROP TABLE IF EXISTS pay_mini_quickly_config;
CREATE TABLE pay_mini_quickly_config (
                                                    id int8 NOT NULL,
                                                    creator varchar(64) COLLATE pg_catalog.default,
                                                    create_time timestamp(6),
                                                    last_modifier varchar(64) COLLATE pg_catalog.default,
                                                    last_modified_time timestamp(6),
                                                    version int4 NOT NULL DEFAULT 0,
                                                    deleted bool NOT NULL DEFAULT false,
                                                    isv_no varchar(32) COLLATE pg_catalog.default,
                                                    mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                    app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                                    allocation bool NOT NULL DEFAULT false,
                                                    auto_allocation bool NOT NULL DEFAULT false,
                                                    limit_pay varchar(512) COLLATE pg_catalog.default,
                                                    terminal_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_mini_quickly_config.id IS '主键ID';
COMMENT ON COLUMN pay_mini_quickly_config.creator IS '创建者';
COMMENT ON COLUMN pay_mini_quickly_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_mini_quickly_config.last_modifier IS '最后修改者';
COMMENT ON COLUMN pay_mini_quickly_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_mini_quickly_config.version IS '版本号';
COMMENT ON COLUMN pay_mini_quickly_config.deleted IS '删除标记';
COMMENT ON COLUMN pay_mini_quickly_config.isv_no IS 'ISV编号';
COMMENT ON COLUMN pay_mini_quickly_config.mch_no IS '商户编号';
COMMENT ON COLUMN pay_mini_quickly_config.app_id IS '应用ID';
COMMENT ON COLUMN pay_mini_quickly_config.allocation IS '小程序开启分账';
COMMENT ON COLUMN pay_mini_quickly_config.auto_allocation IS '小程序自动分账';
COMMENT ON COLUMN pay_mini_quickly_config.limit_pay IS '限制小程序支付方式';
COMMENT ON COLUMN pay_mini_quickly_config.terminal_no IS '小程序付款终端号';
COMMENT ON TABLE pay_mini_quickly_config IS '小程序快捷支付配置';

-- ----------------------------
-- Table structure for pay_onb_mch_info
-- ----------------------------
DROP TABLE IF EXISTS pay_onb_mch_info;
CREATE TABLE pay_onb_mch_info (
                                             id int8 NOT NULL,
                                             creator int8,
                                             create_time timestamp(0),
                                             last_modifier int8,
                                             last_modified_time timestamp(0),
                                             version int4 NOT NULL,
                                             deleted bool NOT NULL,
                                             isv_no varchar(32) COLLATE pg_catalog.default,
                                             onb_mch_no varchar(128) COLLATE pg_catalog.default,
                                             onb_mch_type varchar(32) COLLATE pg_catalog.default,
                                             onb_mch_name varchar(128) COLLATE pg_catalog.default,
                                             onb_channel varchar(32) COLLATE pg_catalog.default,
                                             mch_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_onb_mch_info.id IS '主键';
COMMENT ON COLUMN pay_onb_mch_info.creator IS '创建者ID';
COMMENT ON COLUMN pay_onb_mch_info.create_time IS '创建时间';
COMMENT ON COLUMN pay_onb_mch_info.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_onb_mch_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_onb_mch_info.version IS '版本号';
COMMENT ON COLUMN pay_onb_mch_info.deleted IS '删除标志';
COMMENT ON COLUMN pay_onb_mch_info.isv_no IS '服务商号';
COMMENT ON COLUMN pay_onb_mch_info.onb_mch_no IS '进件商户号';
COMMENT ON COLUMN pay_onb_mch_info.onb_mch_type IS '商户类型';
COMMENT ON COLUMN pay_onb_mch_info.onb_mch_name IS '商户全称';
COMMENT ON COLUMN pay_onb_mch_info.onb_channel IS '所属通道';
COMMENT ON COLUMN pay_onb_mch_info.mch_no IS '所属商户';
COMMENT ON TABLE pay_onb_mch_info IS '进件商户信息';

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS pay_order;
CREATE TABLE pay_order (
                                      id int8 NOT NULL,
                                      creator int8 NOT NULL,
                                      create_time timestamp(6) NOT NULL,
                                      last_modifier int8,
                                      last_modified_time timestamp(6),
                                      version int4 NOT NULL,
                                      deleted bool NOT NULL,
                                      biz_order_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                      order_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                      out_order_no varchar(150) COLLATE pg_catalog.default,
                                      title varchar(100) COLLATE pg_catalog.default NOT NULL,
                                      description varchar(500) COLLATE pg_catalog.default,
                                      allocation bool NOT NULL,
                                      auto_allocation bool NOT NULL,
                                      channel varchar(20) COLLATE pg_catalog.default,
                                      method varchar(20) COLLATE pg_catalog.default,
                                      amount numeric(16,4) NOT NULL,
                                      refundable_balance numeric(16,4) NOT NULL,
                                      status varchar(32) COLLATE pg_catalog.default NOT NULL,
                                      refund_status varchar(32) COLLATE pg_catalog.default NOT NULL,
                                      alloc_status varchar(32) COLLATE pg_catalog.default,
                                      error_msg varchar(500) COLLATE pg_catalog.default,
                                      mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                      app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                      expired_time timestamp(6),
                                      pay_time timestamp(6),
                                      close_time timestamp(6),
                                      isv_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                      other_method varchar(128) COLLATE pg_catalog.default,
                                      limit_pay varchar(128) COLLATE pg_catalog.default,
                                      terminal_no varchar(128) COLLATE pg_catalog.default,
                                      settle_status varchar(32) COLLATE pg_catalog.default,
                                      onb_mch_no varchar(32) COLLATE pg_catalog.default,
                                      payment_vendor varchar(32) COLLATE pg_catalog.default,
                                      relation_order_no varchar(150) COLLATE pg_catalog.default,
                                      trans_order_no varchar(150) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_order.id IS '主键';
COMMENT ON COLUMN pay_order.creator IS '创建者ID';
COMMENT ON COLUMN pay_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_order.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_order.version IS '版本号';
COMMENT ON COLUMN pay_order.deleted IS '删除标志';
COMMENT ON COLUMN pay_order.biz_order_no IS '商户订单号';
COMMENT ON COLUMN pay_order.order_no IS '订单号';
COMMENT ON COLUMN pay_order.out_order_no IS '通道订单号';
COMMENT ON COLUMN pay_order.title IS '标题';
COMMENT ON COLUMN pay_order.description IS '描述';
COMMENT ON COLUMN pay_order.allocation IS '是否支持分账';
COMMENT ON COLUMN pay_order.auto_allocation IS '自动分账';
COMMENT ON COLUMN pay_order.channel IS '支付通道';
COMMENT ON COLUMN pay_order.method IS '支付方式';
COMMENT ON COLUMN pay_order.amount IS '金额(元)';
COMMENT ON COLUMN pay_order.refundable_balance IS '可退金额(元)';
COMMENT ON COLUMN pay_order.status IS '支付状态';
COMMENT ON COLUMN pay_order.refund_status IS '退款状态';
COMMENT ON COLUMN pay_order.alloc_status IS '分账状态';
COMMENT ON COLUMN pay_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_order.app_id IS '应用号';
COMMENT ON COLUMN pay_order.expired_time IS '过期时间';
COMMENT ON COLUMN pay_order.pay_time IS '支付成功时间';
COMMENT ON COLUMN pay_order.close_time IS '关闭时间';
COMMENT ON COLUMN pay_order.isv_no IS '所属服务商';
COMMENT ON COLUMN pay_order.other_method IS '其他支付方式';
COMMENT ON COLUMN pay_order.limit_pay IS '限制支付类型';
COMMENT ON COLUMN pay_order.terminal_no IS '终端设备编码';
COMMENT ON COLUMN pay_order.settle_status IS '结算状态';
COMMENT ON COLUMN pay_order.onb_mch_no IS '进件商户号';
COMMENT ON COLUMN pay_order.payment_vendor IS '支付厂商';
COMMENT ON COLUMN pay_order.relation_order_no IS '特殊通道关联订单号';
COMMENT ON COLUMN pay_order.trans_order_no IS '透传订单号';
COMMENT ON TABLE pay_order IS '支付订单';

-- ----------------------------
-- Table structure for pay_order_expand
-- ----------------------------
DROP TABLE IF EXISTS pay_order_expand;
CREATE TABLE pay_order_expand (
                                             id int8 NOT NULL,
                                             creator int8 NOT NULL,
                                             create_time timestamp(6) NOT NULL,
                                             last_modifier int8 NOT NULL,
                                             last_modified_time timestamp(6) NOT NULL,
                                             version int4 NOT NULL,
                                             deleted bool NOT NULL,
                                             mch_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                             app_id varchar(32) COLLATE pg_catalog.default NOT NULL,
                                             isv_no varchar(32) COLLATE pg_catalog.default NOT NULL,
                                             buyer_id varchar(64) COLLATE pg_catalog.default,
                                             user_id varchar(64) COLLATE pg_catalog.default,
                                             trade_product varchar(64) COLLATE pg_catalog.default,
                                             trade_way varchar(64) COLLATE pg_catalog.default,
                                             bank_type varchar(64) COLLATE pg_catalog.default,
                                             promotion_type varchar(64) COLLATE pg_catalog.default,
                                             ext text COLLATE pg_catalog.default,
                                             relation_order_no varchar(64) COLLATE pg_catalog.default,
                                             bar_code varchar(64) COLLATE pg_catalog.default,
                                             return_url varchar(200) COLLATE pg_catalog.default,
                                             notify_url varchar(200) COLLATE pg_catalog.default,
                                             extra_param varchar(2048) COLLATE pg_catalog.default,
                                             attach varchar(500) COLLATE pg_catalog.default,
                                             req_time timestamp(6),
                                             client_ip varchar(64) COLLATE pg_catalog.default,
                                             auth_code varchar(128) COLLATE pg_catalog.default,
                                             real_amount numeric(16,4),
                                             terminal_no varchar(128) COLLATE pg_catalog.default,
                                             jsapi_open_id varchar(128) COLLATE pg_catalog.default,
                                             pay_body text COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_order_expand.id IS '主键';
COMMENT ON COLUMN pay_order_expand.creator IS '创建者ID';
COMMENT ON COLUMN pay_order_expand.create_time IS '创建时间';
COMMENT ON COLUMN pay_order_expand.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_order_expand.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_order_expand.version IS '版本号';
COMMENT ON COLUMN pay_order_expand.deleted IS '删除标志';
COMMENT ON COLUMN pay_order_expand.mch_no IS '商户号';
COMMENT ON COLUMN pay_order_expand.app_id IS '应用号';
COMMENT ON COLUMN pay_order_expand.isv_no IS '所属服务商';
COMMENT ON COLUMN pay_order_expand.buyer_id IS '付款用户ID';
COMMENT ON COLUMN pay_order_expand.user_id IS '用户标识';
COMMENT ON COLUMN pay_order_expand.trade_product IS '支付产品';
COMMENT ON COLUMN pay_order_expand.trade_way IS '交易方式';
COMMENT ON COLUMN pay_order_expand.bank_type IS '银行卡类型';
COMMENT ON COLUMN pay_order_expand.promotion_type IS '参加活动类型';
COMMENT ON COLUMN pay_order_expand.ext IS '扩展参数存储字段';
COMMENT ON COLUMN pay_order_expand.relation_order_no IS '特殊通道关联订单号';
COMMENT ON COLUMN pay_order_expand.bar_code IS '付款码';
COMMENT ON COLUMN pay_order_expand.return_url IS '同步跳转地址';
COMMENT ON COLUMN pay_order_expand.notify_url IS '异步通知地址';
COMMENT ON COLUMN pay_order_expand.extra_param IS '通道附加参数';
COMMENT ON COLUMN pay_order_expand.attach IS '商户扩展参数';
COMMENT ON COLUMN pay_order_expand.req_time IS '请求时间';
COMMENT ON COLUMN pay_order_expand.client_ip IS '支付终端ip';
COMMENT ON COLUMN pay_order_expand.auth_code IS '付款码';
COMMENT ON COLUMN pay_order_expand.real_amount IS '实收金额';
COMMENT ON COLUMN pay_order_expand.terminal_no IS '终端设备编码';
COMMENT ON COLUMN pay_order_expand.jsapi_open_id IS 'jsapi支付时OpenId';
COMMENT ON COLUMN pay_order_expand.pay_body IS '支付通道返回支付参数';
COMMENT ON TABLE pay_order_expand IS '支付订单扩展存储参数';

-- ----------------------------
-- Table structure for pay_platform_basic_config
-- ----------------------------
DROP TABLE IF EXISTS pay_platform_basic_config;
CREATE TABLE pay_platform_basic_config (
                                                      id int8 NOT NULL,
                                                      creator int8,
                                                      create_time timestamp(6),
                                                      last_modifier int8,
                                                      last_modified_time timestamp(6),
                                                      version int4 NOT NULL,
                                                      deleted bool NOT NULL,
                                                      single_limit_amount numeric(16,4),
                                                      monthly_limit_amount numeric(16,4),
                                                      daily_limit_amount numeric(16,4),
                                                      order_timeout int4,
                                                      default_isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_platform_basic_config.id IS '主键';
COMMENT ON COLUMN pay_platform_basic_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_basic_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_basic_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_platform_basic_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_basic_config.version IS '版本号';
COMMENT ON COLUMN pay_platform_basic_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_platform_basic_config.single_limit_amount IS '全局单笔限额';
COMMENT ON COLUMN pay_platform_basic_config.monthly_limit_amount IS '每月累计限额';
COMMENT ON COLUMN pay_platform_basic_config.daily_limit_amount IS '每日限额';
COMMENT ON COLUMN pay_platform_basic_config.order_timeout IS '订单超时时间';
COMMENT ON COLUMN pay_platform_basic_config.default_isv_no IS '默认服务商';
COMMENT ON TABLE pay_platform_basic_config IS '管理平台基础配置';



-- ----------------------------
-- Table structure for pay_platform_integration_config
-- ----------------------------
DROP TABLE IF EXISTS pay_platform_integration_config;
CREATE TABLE pay_platform_integration_config (
                                                            id int8 NOT NULL,
                                                            creator int8,
                                                            create_time timestamp(6),
                                                            last_modifier int8,
                                                            last_modified_time timestamp(6),
                                                            version int4 NOT NULL DEFAULT 0,
                                                            deleted bool NOT NULL DEFAULT false,
                                                            req_sign bool NOT NULL DEFAULT false,
                                                            req_timeout bool NOT NULL DEFAULT false,
                                                            api_req_timeout int4
)
;
COMMENT ON COLUMN pay_platform_integration_config.id IS '主键';
COMMENT ON COLUMN pay_platform_integration_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_integration_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_integration_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_platform_integration_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_integration_config.version IS '版本号';
COMMENT ON COLUMN pay_platform_integration_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_platform_integration_config.req_sign IS '是否对请求进行验签';
COMMENT ON COLUMN pay_platform_integration_config.req_timeout IS '是否验证请求时间是否超时';
COMMENT ON COLUMN pay_platform_integration_config.api_req_timeout IS '请求超时时间(秒)';
COMMENT ON TABLE pay_platform_integration_config IS '平台集成配置';

-- ----------------------------
-- Table structure for pay_platform_url_config
-- ----------------------------
DROP TABLE IF EXISTS pay_platform_url_config;
CREATE TABLE pay_platform_url_config (
                                                    id int8 NOT NULL,
                                                    creator int8,
                                                    create_time timestamp(6),
                                                    last_modifier int8,
                                                    last_modified_time timestamp(6),
                                                    version int4 NOT NULL,
                                                    deleted bool NOT NULL,
                                                    admin_web_url varchar(200) COLLATE pg_catalog.default,
                                                    mch_web_url varchar(200) COLLATE pg_catalog.default,
                                                    gateway_service_url varchar(200) COLLATE pg_catalog.default,
                                                    gateway_h5_url varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_platform_url_config.id IS '主键';
COMMENT ON COLUMN pay_platform_url_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_url_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_url_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_platform_url_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_url_config.version IS '版本号';
COMMENT ON COLUMN pay_platform_url_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_platform_url_config.admin_web_url IS '运营端网址';
COMMENT ON COLUMN pay_platform_url_config.mch_web_url IS '商户端网址';
COMMENT ON COLUMN pay_platform_url_config.gateway_service_url IS '支付网关地址';
COMMENT ON COLUMN pay_platform_url_config.gateway_h5_url IS '网关h5地址';
COMMENT ON TABLE pay_platform_url_config IS '系统地址配置';

-- ----------------------------
-- Table structure for pay_platform_website_config
-- ----------------------------
DROP TABLE IF EXISTS pay_platform_website_config;
CREATE TABLE pay_platform_website_config (
                                                        id int8 NOT NULL,
                                                        creator int8,
                                                        create_time timestamp(6),
                                                        last_modifier int8,
                                                        last_modified_time timestamp(6),
                                                        version int4 NOT NULL,
                                                        deleted bool NOT NULL,
                                                        system_name varchar(50) COLLATE pg_catalog.default,
                                                        company_name varchar(100) COLLATE pg_catalog.default,
                                                        company_phone varchar(32) COLLATE pg_catalog.default,
                                                        company_email varchar(64) COLLATE pg_catalog.default,
                                                        whole_logo varchar(200) COLLATE pg_catalog.default,
                                                        simple_logo varchar(200) COLLATE pg_catalog.default,
                                                        icp_info varchar(64) COLLATE pg_catalog.default,
                                                        icp_link varchar(200) COLLATE pg_catalog.default,
                                                        mps_info varchar(64) COLLATE pg_catalog.default,
                                                        mps_link varchar(200) COLLATE pg_catalog.default,
                                                        pcac_info varchar(64) COLLATE pg_catalog.default,
                                                        pcac_link varchar(200) COLLATE pg_catalog.default,
                                                        icp_plus_info varchar(64) COLLATE pg_catalog.default,
                                                        icp_plus_link varchar(200) COLLATE pg_catalog.default,
                                                        copyright varchar(64) COLLATE pg_catalog.default,
                                                        copyright_link varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_platform_website_config.id IS '主键';
COMMENT ON COLUMN pay_platform_website_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_website_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_website_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_platform_website_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_website_config.version IS '版本号';
COMMENT ON COLUMN pay_platform_website_config.deleted IS '删除标志';
COMMENT ON TABLE pay_platform_website_config IS '站点显示内容配置';

-- ----------------------------
-- Table structure for pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS pay_refund_order;
CREATE TABLE pay_refund_order (
                                             id int8 NOT NULL,
                                             order_id int8 NOT NULL,
                                             order_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                             biz_order_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                             out_order_no varchar(150) COLLATE pg_catalog.default NOT NULL,
                                             title varchar(100) COLLATE pg_catalog.default NOT NULL,
                                             refund_no varchar(150) COLLATE pg_catalog.default NOT NULL,
                                             biz_refund_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                             out_refund_no varchar(150) COLLATE pg_catalog.default,
                                             channel varchar(20) COLLATE pg_catalog.default NOT NULL,
                                             order_amount numeric(16,4) NOT NULL,
                                             amount numeric(16,4) NOT NULL,
                                             reason varchar(150) COLLATE pg_catalog.default,
                                             finish_time timestamp(6),
                                             status varchar(20) COLLATE pg_catalog.default NOT NULL,
                                             notify_url varchar(200) COLLATE pg_catalog.default,
                                             attach varchar(500) COLLATE pg_catalog.default,
                                             extra_param varchar(2048) COLLATE pg_catalog.default,
                                             req_time timestamp(6),
                                             client_ip varchar(64) COLLATE pg_catalog.default,
                                             error_code varchar(10) COLLATE pg_catalog.default,
                                             error_msg varchar(500) COLLATE pg_catalog.default,
                                             creator int8,
                                             create_time timestamp(6),
                                             last_modifier int8,
                                             last_modified_time timestamp(6),
                                             version int4 NOT NULL,
                                             deleted bool NOT NULL,
                                             mch_no varchar(32) COLLATE pg_catalog.default,
                                             app_id varchar(32) COLLATE pg_catalog.default,
                                             isv_no varchar(32) COLLATE pg_catalog.default,
                                             settle_status varchar(32) COLLATE pg_catalog.default,
                                             onb_mch_no varchar(32) COLLATE pg_catalog.default,
                                             payment_vendor varchar(32) COLLATE pg_catalog.default,
                                             relation_order_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_refund_order.id IS '主键';
COMMENT ON COLUMN pay_refund_order.order_id IS '支付订单ID';
COMMENT ON COLUMN pay_refund_order.order_no IS '支付订单号';
COMMENT ON COLUMN pay_refund_order.biz_order_no IS '商户支付订单号';
COMMENT ON COLUMN pay_refund_order.out_order_no IS '通道支付订单号';
COMMENT ON COLUMN pay_refund_order.title IS '支付标题';
COMMENT ON COLUMN pay_refund_order.refund_no IS '退款号';
COMMENT ON COLUMN pay_refund_order.biz_refund_no IS '商户退款号';
COMMENT ON COLUMN pay_refund_order.out_refund_no IS '通道退款交易号';
COMMENT ON COLUMN pay_refund_order.channel IS '支付通道';
COMMENT ON COLUMN pay_refund_order.order_amount IS '订单金额';
COMMENT ON COLUMN pay_refund_order.amount IS '退款金额';
COMMENT ON COLUMN pay_refund_order.reason IS '退款原因';
COMMENT ON COLUMN pay_refund_order.finish_time IS '退款完成时间';
COMMENT ON COLUMN pay_refund_order.status IS '退款状态';
COMMENT ON COLUMN pay_refund_order.notify_url IS '异步通知地址';
COMMENT ON COLUMN pay_refund_order.attach IS '商户扩展参数';
COMMENT ON COLUMN pay_refund_order.extra_param IS '附加参数';
COMMENT ON COLUMN pay_refund_order.req_time IS '请求时间，传输时间戳';
COMMENT ON COLUMN pay_refund_order.client_ip IS '支付终端ip';
COMMENT ON COLUMN pay_refund_order.error_code IS '错误码';
COMMENT ON COLUMN pay_refund_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_refund_order.creator IS '创建者ID';
COMMENT ON COLUMN pay_refund_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_refund_order.last_modifier IS '最后修者ID';
COMMENT ON COLUMN pay_refund_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_refund_order.version IS '乐观锁';
COMMENT ON COLUMN pay_refund_order.deleted IS '删除标志';
COMMENT ON COLUMN pay_refund_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_refund_order.app_id IS '应用号';
COMMENT ON COLUMN pay_refund_order.isv_no IS '所属服务商';
COMMENT ON COLUMN pay_refund_order.settle_status IS '结算状态';
COMMENT ON COLUMN pay_refund_order.onb_mch_no IS '进件商户号';
COMMENT ON COLUMN pay_refund_order.payment_vendor IS '支付厂商';
COMMENT ON COLUMN pay_refund_order.relation_order_no IS '特殊通道关联订单号';
COMMENT ON TABLE pay_refund_order IS '退款订单';

-- ----------------------------
-- Table structure for pay_sand_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_sand_isv_config;
CREATE TABLE pay_sand_isv_config (
                                                id int8 NOT NULL,
                                                enable bool,
                                                sandbox bool,
                                                sand_app_id varchar(32) COLLATE pg_catalog.default,
                                                private_key text COLLATE pg_catalog.default,
                                                public_key text COLLATE pg_catalog.default,
                                                wx_channel_auth bool,
                                                wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                isv_no varchar(32) COLLATE pg_catalog.default,
                                                version int8 NOT NULL,
                                                creator int8,
                                                create_time timestamp(6),
                                                last_modifier int8,
                                                last_modified_time timestamp(6) NOT NULL,
                                                deleted bool DEFAULT false,
                                                product_code varchar(64) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_sand_isv_config.id IS '主键';
COMMENT ON COLUMN pay_sand_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_sand_isv_config.sandbox IS '沙箱模式';
COMMENT ON COLUMN pay_sand_isv_config.sand_app_id IS '杉德代理产编号';
COMMENT ON COLUMN pay_sand_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_sand_isv_config.public_key IS '公钥';
COMMENT ON COLUMN pay_sand_isv_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_sand_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_sand_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_sand_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_sand_isv_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_sand_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_sand_isv_config.creator IS '创建人';
COMMENT ON COLUMN pay_sand_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_sand_isv_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_sand_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_sand_isv_config.deleted IS '删除标识';
COMMENT ON COLUMN pay_sand_isv_config.product_code IS '支付产品编号';
COMMENT ON TABLE pay_sand_isv_config IS '杉德服务商配置';

-- ----------------------------
-- Table structure for pay_sand_settle_bind_info
-- ----------------------------
DROP TABLE IF EXISTS pay_sand_settle_bind_info;
CREATE TABLE pay_sand_settle_bind_info (
                                                      id int8 NOT NULL,
                                                      creator int8,
                                                      create_time timestamp(6),
                                                      last_modifier int8,
                                                      last_modified_time timestamp(6),
                                                      version int4 NOT NULL,
                                                      deleted bool NOT NULL,
                                                      isv_no varchar(32) COLLATE pg_catalog.default,
                                                      mch_no varchar(32) COLLATE pg_catalog.default,
                                                      onb_mch_id int8,
                                                      sub_merchant_id varchar(32) COLLATE pg_catalog.default,
                                                      bank_account_type varchar(32) COLLATE pg_catalog.default,
                                                      bank_account_name varchar(100) COLLATE pg_catalog.default,
                                                      bank_card_no varchar(32) COLLATE pg_catalog.default,
                                                      bank_channel_no varchar(32) COLLATE pg_catalog.default,
                                                      bank_account_id_card_no varchar(32) COLLATE pg_catalog.default,
                                                      bank_account_phone varchar(20) COLLATE pg_catalog.default,
                                                      valid_date date
)
;
COMMENT ON COLUMN pay_sand_settle_bind_info.id IS '主键';
COMMENT ON COLUMN pay_sand_settle_bind_info.creator IS '创建者ID';
COMMENT ON COLUMN pay_sand_settle_bind_info.create_time IS '创建时间';
COMMENT ON COLUMN pay_sand_settle_bind_info.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_sand_settle_bind_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_sand_settle_bind_info.version IS '版本号';
COMMENT ON COLUMN pay_sand_settle_bind_info.deleted IS '删除标志';
COMMENT ON COLUMN pay_sand_settle_bind_info.isv_no IS '服务商号';
COMMENT ON COLUMN pay_sand_settle_bind_info.mch_no IS '商户号';
COMMENT ON COLUMN pay_sand_settle_bind_info.onb_mch_id IS '进件商户ID';
COMMENT ON COLUMN pay_sand_settle_bind_info.sub_merchant_id IS '杉德商户号';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_account_type IS '银行账户类型 PUBLIC_ACCOUNT：对公帐户 PRIVATE_DEBIT_ACCOUNT：对私借记卡';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_account_name IS '银行开户名';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_card_no IS '银行卡卡号';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_channel_no IS '银行联行号';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_account_id_card_no IS '银行开户身份证号码';
COMMENT ON COLUMN pay_sand_settle_bind_info.bank_account_phone IS '银行预留手机号';
COMMENT ON COLUMN pay_sand_settle_bind_info.valid_date IS '换卡生效时间';
COMMENT ON TABLE pay_sand_settle_bind_info IS '杉德商户结算银行卡绑定信息';

-- ----------------------------
-- Table structure for pay_sand_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_sand_sub_config;
CREATE TABLE pay_sand_sub_config (
                                                id int8 NOT NULL,
                                                enable bool,
                                                merchant_no varchar(32) COLLATE pg_catalog.default,
                                                store_id varchar(32) COLLATE pg_catalog.default,
                                                read_system bool,
                                                wx_channel_auth bool,
                                                wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                mch_no varchar(32) COLLATE pg_catalog.default,
                                                app_id varchar(32) COLLATE pg_catalog.default,
                                                version int8,
                                                creator int8,
                                                create_time timestamp(6),
                                                last_modifier int8,
                                                last_modified_time timestamp(6),
                                                deleted bool DEFAULT false,
                                                isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_sand_sub_config.id IS '主键';
COMMENT ON COLUMN pay_sand_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_sand_sub_config.merchant_no IS '杉德商户编号';
COMMENT ON COLUMN pay_sand_sub_config.store_id IS '门店号';
COMMENT ON COLUMN pay_sand_sub_config.read_system IS '读取服务商配置, 默认为true';
COMMENT ON COLUMN pay_sand_sub_config.wx_channel_auth IS '微信使用通道渠道认证';
COMMENT ON COLUMN pay_sand_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_sand_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_sand_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_sand_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_sand_sub_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_sand_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_sand_sub_config.creator IS '创建人';
COMMENT ON COLUMN pay_sand_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_sand_sub_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_sand_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_sand_sub_config.deleted IS '删除标识';
COMMENT ON COLUMN pay_sand_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_sand_sub_config IS '杉德子商户配置';

-- ----------------------------
-- Table structure for pay_trade_callback_record
-- ----------------------------
DROP TABLE IF EXISTS pay_trade_callback_record;
CREATE TABLE pay_trade_callback_record (
                                                      id int8 NOT NULL,
                                                      trade_no varchar(100) COLLATE pg_catalog.default,
                                                      out_trade_no varchar(150) COLLATE pg_catalog.default,
                                                      channel varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                      callback_type varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                      notify_info text COLLATE pg_catalog.default NOT NULL,
                                                      status varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                      error_code varchar(10) COLLATE pg_catalog.default,
                                                      error_msg varchar(500) COLLATE pg_catalog.default,
                                                      creator int8,
                                                      create_time timestamp(6),
                                                      mch_no varchar(32) COLLATE pg_catalog.default,
                                                      app_id varchar(32) COLLATE pg_catalog.default,
                                                      isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_trade_callback_record.id IS '主键';
COMMENT ON COLUMN pay_trade_callback_record.trade_no IS '本地交易号';
COMMENT ON COLUMN pay_trade_callback_record.out_trade_no IS '通道交易号';
COMMENT ON COLUMN pay_trade_callback_record.channel IS '支付通道';
COMMENT ON COLUMN pay_trade_callback_record.callback_type IS '回调类型';
COMMENT ON COLUMN pay_trade_callback_record.notify_info IS '通知消息';
COMMENT ON COLUMN pay_trade_callback_record.status IS '回调处理状态';
COMMENT ON COLUMN pay_trade_callback_record.error_code IS '错误码';
COMMENT ON COLUMN pay_trade_callback_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_trade_callback_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_trade_callback_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_trade_callback_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_trade_callback_record.app_id IS '应用号';
COMMENT ON COLUMN pay_trade_callback_record.isv_no IS '所属服务商';
COMMENT ON TABLE pay_trade_callback_record IS '网关回调通知';

-- ----------------------------
-- Table structure for pay_trade_flow_record
-- ----------------------------
DROP TABLE IF EXISTS pay_trade_flow_record;
CREATE TABLE pay_trade_flow_record (
                                                  id int8 NOT NULL,
                                                  title varchar(100) COLLATE pg_catalog.default NOT NULL,
                                                  amount numeric(16,4) NOT NULL,
                                                  type varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                  channel varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                  trade_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                                  biz_trade_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                                  out_trade_no varchar(150) COLLATE pg_catalog.default,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_trade_flow_record.id IS '主键';
COMMENT ON COLUMN pay_trade_flow_record.title IS '标题';
COMMENT ON COLUMN pay_trade_flow_record.amount IS '金额';
COMMENT ON COLUMN pay_trade_flow_record.type IS '业务类型';
COMMENT ON COLUMN pay_trade_flow_record.channel IS '支付通道';
COMMENT ON COLUMN pay_trade_flow_record.trade_no IS '本地交易号';
COMMENT ON COLUMN pay_trade_flow_record.biz_trade_no IS '商户交易号';
COMMENT ON COLUMN pay_trade_flow_record.out_trade_no IS '通道交易号';
COMMENT ON COLUMN pay_trade_flow_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_trade_flow_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_trade_flow_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_trade_flow_record.app_id IS '应用号';
COMMENT ON COLUMN pay_trade_flow_record.isv_no IS '所属服务商';
COMMENT ON TABLE pay_trade_flow_record IS '资金流水记录';

-- ----------------------------
-- Table structure for pay_trade_sync_record
-- ----------------------------
DROP TABLE IF EXISTS pay_trade_sync_record;
CREATE TABLE pay_trade_sync_record (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  trade_no varchar(100) COLLATE pg_catalog.default,
                                                  biz_trade_no varchar(100) COLLATE pg_catalog.default,
                                                  out_trade_no varchar(150) COLLATE pg_catalog.default,
                                                  out_trade_status varchar(32) COLLATE pg_catalog.default,
                                                  trade_type varchar(32) COLLATE pg_catalog.default,
                                                  channel varchar(32) COLLATE pg_catalog.default,
                                                  sync_info text COLLATE pg_catalog.default,
                                                  adjust bool NOT NULL,
                                                  error_code varchar(50) COLLATE pg_catalog.default,
                                                  error_msg varchar(500) COLLATE pg_catalog.default,
                                                  client_ip varchar(64) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_trade_sync_record.id IS '主键';
COMMENT ON COLUMN pay_trade_sync_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_trade_sync_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_trade_sync_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_trade_sync_record.app_id IS '应用号';
COMMENT ON COLUMN pay_trade_sync_record.trade_no IS '本地交易号';
COMMENT ON COLUMN pay_trade_sync_record.biz_trade_no IS '商户交易号';
COMMENT ON COLUMN pay_trade_sync_record.out_trade_no IS '通道交易号';
COMMENT ON COLUMN pay_trade_sync_record.out_trade_status IS '通道返回的状态';
COMMENT ON COLUMN pay_trade_sync_record.trade_type IS '同步类型';
COMMENT ON COLUMN pay_trade_sync_record.channel IS '同步通道';
COMMENT ON COLUMN pay_trade_sync_record.sync_info IS '网关返回的同步消息';
COMMENT ON COLUMN pay_trade_sync_record.adjust IS '是否进行调整';
COMMENT ON COLUMN pay_trade_sync_record.error_code IS '错误码';
COMMENT ON COLUMN pay_trade_sync_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_trade_sync_record.client_ip IS '终端ip';
COMMENT ON COLUMN pay_trade_sync_record.isv_no IS '所属服务商';
COMMENT ON TABLE pay_trade_sync_record IS '交易同步记录';

-- ----------------------------
-- Table structure for pay_transfer_order
-- ----------------------------
DROP TABLE IF EXISTS pay_transfer_order;
CREATE TABLE pay_transfer_order (
                                               id int8 NOT NULL,
                                               biz_transfer_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                               transfer_no varchar(100) COLLATE pg_catalog.default NOT NULL,
                                               out_transfer_no varchar(150) COLLATE pg_catalog.default,
                                               channel varchar(20) COLLATE pg_catalog.default NOT NULL,
                                               amount numeric(16,4) NOT NULL,
                                               title varchar(100) COLLATE pg_catalog.default,
                                               reason varchar(150) COLLATE pg_catalog.default,
                                               payee_type varchar(20) COLLATE pg_catalog.default,
                                               payee_account varchar(100) COLLATE pg_catalog.default,
                                               payee_name varchar(50) COLLATE pg_catalog.default,
                                               status varchar(20) COLLATE pg_catalog.default NOT NULL,
                                               finish_time timestamp(6),
                                               notify_url varchar(200) COLLATE pg_catalog.default,
                                               attach varchar(500) COLLATE pg_catalog.default,
                                               req_time timestamp(6),
                                               client_ip varchar(64) COLLATE pg_catalog.default,
                                               error_code varchar(10) COLLATE pg_catalog.default,
                                               error_msg varchar(500) COLLATE pg_catalog.default,
                                               creator int8,
                                               create_time timestamp(6),
                                               last_modifier int8,
                                               last_modified_time timestamp(6),
                                               version int4 NOT NULL,
                                               deleted bool NOT NULL,
                                               mch_no varchar(32) COLLATE pg_catalog.default,
                                               app_id varchar(32) COLLATE pg_catalog.default,
                                               extra_param varchar(2048) COLLATE pg_catalog.default,
                                               isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_transfer_order.id IS '主键';
COMMENT ON COLUMN pay_transfer_order.biz_transfer_no IS '商户转账号';
COMMENT ON COLUMN pay_transfer_order.transfer_no IS '转账号';
COMMENT ON COLUMN pay_transfer_order.out_transfer_no IS '通道转账号';
COMMENT ON COLUMN pay_transfer_order.channel IS '支付通道';
COMMENT ON COLUMN pay_transfer_order.amount IS '转账金额';
COMMENT ON COLUMN pay_transfer_order.title IS '标题';
COMMENT ON COLUMN pay_transfer_order.reason IS '转账原因/备注';
COMMENT ON COLUMN pay_transfer_order.payee_type IS '收款人类型';
COMMENT ON COLUMN pay_transfer_order.payee_account IS '收款人账号';
COMMENT ON COLUMN pay_transfer_order.payee_name IS '收款人姓名';
COMMENT ON COLUMN pay_transfer_order.status IS '状态';
COMMENT ON COLUMN pay_transfer_order.finish_time IS '成功时间';
COMMENT ON COLUMN pay_transfer_order.notify_url IS '异步通知地址';
COMMENT ON COLUMN pay_transfer_order.attach IS '商户扩展参数';
COMMENT ON COLUMN pay_transfer_order.req_time IS '请求时间，传输时间戳';
COMMENT ON COLUMN pay_transfer_order.client_ip IS '支付终端ip';
COMMENT ON COLUMN pay_transfer_order.error_code IS '错误码';
COMMENT ON COLUMN pay_transfer_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_transfer_order.creator IS '创建者ID';
COMMENT ON COLUMN pay_transfer_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_transfer_order.last_modifier IS '最后修者ID';
COMMENT ON COLUMN pay_transfer_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_transfer_order.version IS '乐观锁';
COMMENT ON COLUMN pay_transfer_order.deleted IS '删除标志';
COMMENT ON COLUMN pay_transfer_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_transfer_order.app_id IS '应用号';
COMMENT ON COLUMN pay_transfer_order.extra_param IS '通道附加参数';
COMMENT ON COLUMN pay_transfer_order.isv_no IS '所属服务商';
COMMENT ON TABLE pay_transfer_order IS '转账订单';

-- ----------------------------
-- Table structure for pay_vbill_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_vbill_isv_config;
CREATE TABLE pay_vbill_isv_config (
                                                 id int8 NOT NULL,
                                                 creator int8,
                                                 create_time timestamp(6),
                                                 last_modifier int8,
                                                 last_modified_time timestamp(6),
                                                 version int4 NOT NULL,
                                                 deleted bool NOT NULL,
                                                 org_id varchar(64) COLLATE pg_catalog.default,
                                                 public_key text COLLATE pg_catalog.default,
                                                 private_key text COLLATE pg_catalog.default,
                                                 enable bool,
                                                 sandbox bool,
                                                 wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                 wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                 wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                 isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_vbill_isv_config.id IS '主键';
COMMENT ON COLUMN pay_vbill_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_vbill_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_vbill_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_vbill_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_vbill_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_vbill_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_vbill_isv_config.org_id IS '天阙机构id';
COMMENT ON COLUMN pay_vbill_isv_config.public_key IS '公钥';
COMMENT ON COLUMN pay_vbill_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_vbill_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_vbill_isv_config.sandbox IS '沙箱';
COMMENT ON COLUMN pay_vbill_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_vbill_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_vbill_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_vbill_isv_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_vbill_isv_config IS '随行付服务商配置';

-- ----------------------------
-- Table structure for pay_vbill_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_vbill_sub_config;
CREATE TABLE pay_vbill_sub_config (
                                                 id int8 NOT NULL,
                                                 creator int8,
                                                 create_time timestamp(6),
                                                 last_modifier int8,
                                                 last_modified_time timestamp(6),
                                                 version int4 NOT NULL,
                                                 deleted bool NOT NULL,
                                                 mno varchar(64) COLLATE pg_catalog.default,
                                                 enable bool,
                                                 app_id varchar(32) COLLATE pg_catalog.default,
                                                 read_system bool,
                                                 wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                 wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                 wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                 mch_no varchar(32) COLLATE pg_catalog.default,
                                                 isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_vbill_sub_config.id IS '主键';
COMMENT ON COLUMN pay_vbill_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_vbill_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_vbill_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_vbill_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_vbill_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_vbill_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_vbill_sub_config.mno IS '天阙商户号';
COMMENT ON COLUMN pay_vbill_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_vbill_sub_config.app_id IS '商户AppId';
COMMENT ON COLUMN pay_vbill_sub_config.read_system IS '读取服务商配置';
COMMENT ON COLUMN pay_vbill_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_vbill_sub_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_vbill_sub_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_vbill_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_vbill_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_vbill_sub_config IS '随行付特约商户配置';

-- ----------------------------
-- Table structure for pay_wechat_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_wechat_isv_config;
CREATE TABLE pay_wechat_isv_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  wx_mch_id varchar(64) COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  api_version varchar(10) COLLATE pg_catalog.default,
                                                  api_key_v2 varchar(64) COLLATE pg_catalog.default,
                                                  api_key_v3 varchar(64) COLLATE pg_catalog.default,
                                                  app_secret varchar(64) COLLATE pg_catalog.default,
                                                  public_key text COLLATE pg_catalog.default,
                                                  public_key_id varchar(128) COLLATE pg_catalog.default,
                                                  private_key text COLLATE pg_catalog.default,
                                                  private_cert text COLLATE pg_catalog.default,
                                                  cert_serial_no varchar(128) COLLATE pg_catalog.default,
                                                  p12 text COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_wechat_isv_config.id IS '主键';
COMMENT ON COLUMN pay_wechat_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_wechat_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_wechat_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_wechat_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_wechat_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_wechat_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_wechat_isv_config.wx_mch_id IS '微信商户Id';
COMMENT ON COLUMN pay_wechat_isv_config.wx_app_id IS '微信应用appId';
COMMENT ON COLUMN pay_wechat_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_wechat_isv_config.api_version IS '接口版本, 使用v2还是v3接口';
COMMENT ON COLUMN pay_wechat_isv_config.api_key_v2 IS '商户平台「API安全」中的 APIv2 密钥';
COMMENT ON COLUMN pay_wechat_isv_config.api_key_v3 IS '商户平台「API安全」中的 APIv3 密钥';
COMMENT ON COLUMN pay_wechat_isv_config.app_secret IS 'APPID对应的接口密码，用于获取微信公众号jsapi支付时使用';
COMMENT ON COLUMN pay_wechat_isv_config.public_key IS '支付公钥(pub_key.pem)';
COMMENT ON COLUMN pay_wechat_isv_config.public_key_id IS '支付公钥ID';
COMMENT ON COLUMN pay_wechat_isv_config.private_key IS 'apiclient_key. pem证书base64编码';
COMMENT ON COLUMN pay_wechat_isv_config.private_cert IS 'apiclient_cert. pem证书base64编码';
COMMENT ON COLUMN pay_wechat_isv_config.cert_serial_no IS '证书序列号';
COMMENT ON COLUMN pay_wechat_isv_config.p12 IS 'API证书中p12证书Base64';
COMMENT ON COLUMN pay_wechat_isv_config.wx_app_secret IS '微信密钥';
COMMENT ON COLUMN pay_wechat_isv_config.wx_auth_url IS '微信授权认证地址';
COMMENT ON COLUMN pay_wechat_isv_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_wechat_isv_config IS '微信服务商配置';

-- ----------------------------
-- Table structure for pay_wechat_pay_config
-- ----------------------------
DROP TABLE IF EXISTS pay_wechat_pay_config;
CREATE TABLE pay_wechat_pay_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  auth_type varchar(32) COLLATE pg_catalog.default,
                                                  auth_url varchar(200) COLLATE pg_catalog.default,
                                                  api_version varchar(10) COLLATE pg_catalog.default,
                                                  api_key_v2 varchar(64) COLLATE pg_catalog.default,
                                                  api_key_v3 varchar(64) COLLATE pg_catalog.default,
                                                  app_secret varchar(64) COLLATE pg_catalog.default,
                                                  public_key text COLLATE pg_catalog.default,
                                                  public_key_id varchar(128) COLLATE pg_catalog.default,
                                                  private_cert text COLLATE pg_catalog.default,
                                                  private_key text COLLATE pg_catalog.default,
                                                  cert_serial_no varchar(128) COLLATE pg_catalog.default,
                                                  p12 text COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default,
                                                  wx_mch_id varchar(64) COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_wechat_pay_config.id IS '主键';
COMMENT ON COLUMN pay_wechat_pay_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_wechat_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_wechat_pay_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_wechat_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_wechat_pay_config.version IS '版本号';
COMMENT ON COLUMN pay_wechat_pay_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_wechat_pay_config.app_id IS '应用号';
COMMENT ON COLUMN pay_wechat_pay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_wechat_pay_config.enable IS '是否启用';
COMMENT ON COLUMN pay_wechat_pay_config.auth_type IS '授权类型';
COMMENT ON COLUMN pay_wechat_pay_config.auth_url IS '授权认证地址';
COMMENT ON COLUMN pay_wechat_pay_config.api_version IS '接口版本, 使用v2还是v3接口';
COMMENT ON COLUMN pay_wechat_pay_config.api_key_v2 IS '商户平台「API安全」中的 APIv2 密钥';
COMMENT ON COLUMN pay_wechat_pay_config.api_key_v3 IS '商户平台「API安全」中的 APIv3 密钥';
COMMENT ON COLUMN pay_wechat_pay_config.app_secret IS 'APPID对应的接口密码，用于获取微信公众号jsapi支付时使用';
COMMENT ON COLUMN pay_wechat_pay_config.public_key IS '支付公钥(pub_key.pem)';
COMMENT ON COLUMN pay_wechat_pay_config.public_key_id IS '支付公钥ID';
COMMENT ON COLUMN pay_wechat_pay_config.private_cert IS '商户API证书(apiclient_cert.pem)base64编码';
COMMENT ON COLUMN pay_wechat_pay_config.private_key IS '商户API证书私钥(apiclient_key.pem)证书base64编码';
COMMENT ON COLUMN pay_wechat_pay_config.cert_serial_no IS '商户API证书序列号';
COMMENT ON COLUMN pay_wechat_pay_config.p12 IS 'p12证书Base64';
COMMENT ON COLUMN pay_wechat_pay_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_wechat_pay_config.wx_mch_id IS '微信商户Id';
COMMENT ON COLUMN pay_wechat_pay_config.wx_app_id IS '微信应用appId';
COMMENT ON TABLE pay_wechat_pay_config IS '微信支付配置';

-- ----------------------------
-- Table structure for pay_wechat_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_wechat_sub_config;
CREATE TABLE pay_wechat_sub_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  sub_mch_id varchar(64) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  auth_type varchar(32) COLLATE pg_catalog.default,
                                                  sub_app_id varchar(64) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_wechat_sub_config.id IS '主键';
COMMENT ON COLUMN pay_wechat_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_wechat_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_wechat_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_wechat_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_wechat_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_wechat_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_wechat_sub_config.app_id IS '应用号';
COMMENT ON COLUMN pay_wechat_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_wechat_sub_config.sub_mch_id IS '微信特约商户号/二级商户号';
COMMENT ON COLUMN pay_wechat_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_wechat_sub_config.auth_type IS '授权类型';
COMMENT ON COLUMN pay_wechat_sub_config.sub_app_id IS '微信特约商户/二级商户AppId';
COMMENT ON COLUMN pay_wechat_sub_config.wx_app_secret IS '微信特约商户/二级商户密钥';
COMMENT ON COLUMN pay_wechat_sub_config.wx_auth_url IS '微信特约商户/二级商户授权认证地址';
COMMENT ON COLUMN pay_wechat_sub_config.isv_no IS '服务商号';
COMMENT ON TABLE pay_wechat_sub_config IS '微信特约商户配置';

-- ----------------------------
-- Table structure for pay_yeepay_isv_config
-- ----------------------------
DROP TABLE IF EXISTS pay_yeepay_isv_config;
CREATE TABLE pay_yeepay_isv_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  enable bool,
                                                  sandbox bool,
                                                  app_key varchar(500) COLLATE pg_catalog.default,
                                                  private_key text COLLATE pg_catalog.default,
                                                  yop_public_key text COLLATE pg_catalog.default,
                                                  merchant_no varchar(32) COLLATE pg_catalog.default,
                                                  parent_merchant_no varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default,
                                                  isv_no varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  yop_isv_no varchar(32) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_yeepay_isv_config.id IS '主键';
COMMENT ON COLUMN pay_yeepay_isv_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_yeepay_isv_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_yeepay_isv_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_yeepay_isv_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_yeepay_isv_config.version IS '版本号';
COMMENT ON COLUMN pay_yeepay_isv_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_yeepay_isv_config.enable IS '是否启用';
COMMENT ON COLUMN pay_yeepay_isv_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN pay_yeepay_isv_config.app_key IS '应用密钥';
COMMENT ON COLUMN pay_yeepay_isv_config.private_key IS '私钥';
COMMENT ON COLUMN pay_yeepay_isv_config.yop_public_key IS 'YOP公钥';
COMMENT ON COLUMN pay_yeepay_isv_config.merchant_no IS '商户号';
COMMENT ON COLUMN pay_yeepay_isv_config.parent_merchant_no IS '父商户号';
COMMENT ON COLUMN pay_yeepay_isv_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_yeepay_isv_config.wx_app_secret IS '微信AppSecret';
COMMENT ON COLUMN pay_yeepay_isv_config.wx_auth_url IS '微信授权地址';
COMMENT ON COLUMN pay_yeepay_isv_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_yeepay_isv_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_yeepay_isv_config.app_id IS '应用号';
COMMENT ON COLUMN pay_yeepay_isv_config.yop_isv_no IS '易宝服务商号';
COMMENT ON TABLE pay_yeepay_isv_config IS '易宝服务商支付配置';

-- ----------------------------
-- Table structure for pay_yeepay_sub_config
-- ----------------------------
DROP TABLE IF EXISTS pay_yeepay_sub_config;
CREATE TABLE pay_yeepay_sub_config (
                                                  id int8 NOT NULL,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL,
                                                  deleted bool NOT NULL,
                                                  isv_no varchar(32) COLLATE pg_catalog.default,
                                                  mch_no varchar(32) COLLATE pg_catalog.default,
                                                  app_id varchar(32) COLLATE pg_catalog.default,
                                                  enable bool,
                                                  merchant_no varchar(32) COLLATE pg_catalog.default,
                                                  read_system varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_id varchar(32) COLLATE pg_catalog.default,
                                                  wx_app_secret varchar(64) COLLATE pg_catalog.default,
                                                  wx_auth_url varchar(200) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN pay_yeepay_sub_config.id IS '主键';
COMMENT ON COLUMN pay_yeepay_sub_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_yeepay_sub_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_yeepay_sub_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_yeepay_sub_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_yeepay_sub_config.version IS '版本号';
COMMENT ON COLUMN pay_yeepay_sub_config.deleted IS '删除标志';
COMMENT ON COLUMN pay_yeepay_sub_config.isv_no IS '服务商号';
COMMENT ON COLUMN pay_yeepay_sub_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_yeepay_sub_config.app_id IS '应用号';
COMMENT ON COLUMN pay_yeepay_sub_config.enable IS '是否启用';
COMMENT ON COLUMN pay_yeepay_sub_config.merchant_no IS '商户号';
COMMENT ON COLUMN pay_yeepay_sub_config.read_system IS '读取系统';
COMMENT ON COLUMN pay_yeepay_sub_config.wx_app_id IS '微信AppId';
COMMENT ON COLUMN pay_yeepay_sub_config.wx_app_secret IS '微信AppSecret';
COMMENT ON COLUMN pay_yeepay_sub_config.wx_auth_url IS '微信授权地址';
COMMENT ON TABLE pay_yeepay_sub_config IS '易宝支付子商户配置';

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS starter_audit_login_log;
CREATE TABLE starter_audit_login_log (
                                                    id int8 NOT NULL,
                                                    user_id int8,
                                                    account varchar(100) COLLATE pg_catalog.default,
                                                    login bool,
                                                    client varchar(20) COLLATE pg_catalog.default,
                                                    login_type varchar(20) COLLATE pg_catalog.default,
                                                    ip varchar(80) COLLATE pg_catalog.default,
                                                    login_location varchar(100) COLLATE pg_catalog.default,
                                                    browser varchar(200) COLLATE pg_catalog.default,
                                                    os varchar(100) COLLATE pg_catalog.default,
                                                    msg text COLLATE pg_catalog.default,
                                                    login_time timestamp(6)
)
;
COMMENT ON COLUMN starter_audit_login_log.id IS '主键';
COMMENT ON COLUMN starter_audit_login_log.user_id IS '用户账号ID';
COMMENT ON COLUMN starter_audit_login_log.account IS '用户名称';
COMMENT ON COLUMN starter_audit_login_log.login IS '登录成功状态';
COMMENT ON COLUMN starter_audit_login_log.client IS '登录终端';
COMMENT ON COLUMN starter_audit_login_log.login_type IS '登录方式';
COMMENT ON COLUMN starter_audit_login_log.ip IS '登录IP地址';
COMMENT ON COLUMN starter_audit_login_log.login_location IS '登录地点';
COMMENT ON COLUMN starter_audit_login_log.browser IS '浏览器类型';
COMMENT ON COLUMN starter_audit_login_log.os IS '操作系统';
COMMENT ON COLUMN starter_audit_login_log.msg IS '提示消息';
COMMENT ON COLUMN starter_audit_login_log.login_time IS '访问时间';
COMMENT ON TABLE starter_audit_login_log IS '登录日志';

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS starter_audit_operate_log;
CREATE TABLE starter_audit_operate_log (
                                                      id int8 NOT NULL,
                                                      title varchar(100) COLLATE pg_catalog.default NOT NULL,
                                                      operate_id int8,
                                                      account varchar(100) COLLATE pg_catalog.default,
                                                      business_type varchar(50) COLLATE pg_catalog.default NOT NULL,
                                                      method varchar(100) COLLATE pg_catalog.default NOT NULL,
                                                      request_method varchar(20) COLLATE pg_catalog.default NOT NULL,
                                                      operate_url varchar(200) COLLATE pg_catalog.default NOT NULL,
                                                      operate_ip varchar(80) COLLATE pg_catalog.default,
                                                      operate_location varchar(50) COLLATE pg_catalog.default,
                                                      operate_param text COLLATE pg_catalog.default,
                                                      operate_return text COLLATE pg_catalog.default,
                                                      success bool,
                                                      error_msg text COLLATE pg_catalog.default,
                                                      operate_time timestamp(6) NOT NULL,
                                                      browser varchar(200) COLLATE pg_catalog.default,
                                                      os varchar(100) COLLATE pg_catalog.default,
                                                      client varchar(20) COLLATE pg_catalog.default
)
;
COMMENT ON COLUMN starter_audit_operate_log.id IS '主键';
COMMENT ON COLUMN starter_audit_operate_log.title IS '操作模块';
COMMENT ON COLUMN starter_audit_operate_log.operate_id IS '操作人员id';
COMMENT ON COLUMN starter_audit_operate_log.account IS '操作人员账号';
COMMENT ON COLUMN starter_audit_operate_log.business_type IS '业务类型';
COMMENT ON COLUMN starter_audit_operate_log.method IS '请求方法';
COMMENT ON COLUMN starter_audit_operate_log.request_method IS '请求方式';
COMMENT ON COLUMN starter_audit_operate_log.operate_url IS '请求url';
COMMENT ON COLUMN starter_audit_operate_log.operate_ip IS '操作ip';
COMMENT ON COLUMN starter_audit_operate_log.operate_location IS '操作地点';
COMMENT ON COLUMN starter_audit_operate_log.operate_param IS '请求参数';
COMMENT ON COLUMN starter_audit_operate_log.operate_return IS '返回参数';
COMMENT ON COLUMN starter_audit_operate_log.success IS '操作状态';
COMMENT ON COLUMN starter_audit_operate_log.error_msg IS '错误消息';
COMMENT ON COLUMN starter_audit_operate_log.operate_time IS '操作时间';
COMMENT ON COLUMN starter_audit_operate_log.browser IS '浏览器类型';
COMMENT ON COLUMN starter_audit_operate_log.os IS '操作系统';
COMMENT ON COLUMN starter_audit_operate_log.client IS '终端';
COMMENT ON TABLE starter_audit_operate_log IS '操作日志';

-- ----------------------------
-- Table structure for starter_file_platform
-- ----------------------------
DROP TABLE IF EXISTS starter_file_platform;
CREATE TABLE starter_file_platform (
                                                  id int8 NOT NULL,
                                                  type varchar(50) COLLATE pg_catalog.default,
                                                  name varchar(200) COLLATE pg_catalog.default,
                                                  url varchar(200) COLLATE pg_catalog.default,
                                                  default_platform bool,
                                                  creator int8,
                                                  create_time timestamp(6),
                                                  last_modifier int8,
                                                  last_modified_time timestamp(6),
                                                  version int4 NOT NULL DEFAULT 0,
                                                  frontend_upload bool
)
;
COMMENT ON COLUMN starter_file_platform.id IS '文件id';
COMMENT ON COLUMN starter_file_platform.type IS '平台类型';
COMMENT ON COLUMN starter_file_platform.name IS '名称';
COMMENT ON COLUMN starter_file_platform.url IS '访问地址';
COMMENT ON COLUMN starter_file_platform.default_platform IS '默认存储平台';
COMMENT ON COLUMN starter_file_platform.creator IS '创建者ID';
COMMENT ON COLUMN starter_file_platform.create_time IS '创建时间';
COMMENT ON COLUMN starter_file_platform.last_modifier IS '最后修改ID';
COMMENT ON COLUMN starter_file_platform.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN starter_file_platform.version IS '版本号';
COMMENT ON COLUMN starter_file_platform.frontend_upload IS '前端直传';
COMMENT ON TABLE starter_file_platform IS '文件存储平台';

-- ----------------------------
-- Table structure for starter_file_upload_info
-- ----------------------------
DROP TABLE IF EXISTS starter_file_upload_info;
CREATE TABLE starter_file_upload_info (
                                                     id int8 NOT NULL,
                                                     url varchar(512) COLLATE pg_catalog.default NOT NULL,
                                                     size int8,
                                                     filename varchar(256) COLLATE pg_catalog.default,
                                                     original_filename varchar(256) COLLATE pg_catalog.default,
                                                     base_path varchar(256) COLLATE pg_catalog.default,
                                                     path varchar(256) COLLATE pg_catalog.default,
                                                     ext varchar(32) COLLATE pg_catalog.default,
                                                     content_type varchar(128) COLLATE pg_catalog.default,
                                                     platform varchar(32) COLLATE pg_catalog.default,
                                                     th_url varchar(512) COLLATE pg_catalog.default,
                                                     th_filename varchar(256) COLLATE pg_catalog.default,
                                                     th_size int8,
                                                     th_content_type varchar(128) COLLATE pg_catalog.default,
                                                     object_id varchar(32) COLLATE pg_catalog.default,
                                                     object_type varchar(32) COLLATE pg_catalog.default,
                                                     metadata text COLLATE pg_catalog.default,
                                                     user_metadata text COLLATE pg_catalog.default,
                                                     th_metadata text COLLATE pg_catalog.default,
                                                     th_user_metadata text COLLATE pg_catalog.default,
                                                     attr text COLLATE pg_catalog.default,
                                                     file_acl varchar(32) COLLATE pg_catalog.default,
                                                     th_file_acl varchar(32) COLLATE pg_catalog.default,
                                                     create_time timestamp(6)
)
;
COMMENT ON COLUMN starter_file_upload_info.id IS '文件id';
COMMENT ON COLUMN starter_file_upload_info.url IS '文件访问地址';
COMMENT ON COLUMN starter_file_upload_info.size IS '文件大小，单位字节';
COMMENT ON COLUMN starter_file_upload_info.filename IS '文件名称';
COMMENT ON COLUMN starter_file_upload_info.original_filename IS '原始文件名';
COMMENT ON COLUMN starter_file_upload_info.base_path IS '基础存储路径';
COMMENT ON COLUMN starter_file_upload_info.path IS '存储路径';
COMMENT ON COLUMN starter_file_upload_info.ext IS '文件扩展名';
COMMENT ON COLUMN starter_file_upload_info.content_type IS 'MIME类型';
COMMENT ON COLUMN starter_file_upload_info.platform IS '存储平台';
COMMENT ON COLUMN starter_file_upload_info.th_url IS '缩略图访问路径';
COMMENT ON COLUMN starter_file_upload_info.th_filename IS '缩略图名称';
COMMENT ON COLUMN starter_file_upload_info.th_size IS '缩略图大小，单位字节';
COMMENT ON COLUMN starter_file_upload_info.th_content_type IS '缩略图MIME类型';
COMMENT ON COLUMN starter_file_upload_info.object_id IS '文件所属对象id';
COMMENT ON COLUMN starter_file_upload_info.object_type IS '文件所属对象类型，例如用户头像，评价图片';
COMMENT ON COLUMN starter_file_upload_info.metadata IS '文件元数据';
COMMENT ON COLUMN starter_file_upload_info.user_metadata IS '文件用户元数据';
COMMENT ON COLUMN starter_file_upload_info.th_metadata IS '缩略图元数据';
COMMENT ON COLUMN starter_file_upload_info.th_user_metadata IS '缩略图用户元数据';
COMMENT ON COLUMN starter_file_upload_info.attr IS '附加属性';
COMMENT ON COLUMN starter_file_upload_info.file_acl IS '文件ACL';
COMMENT ON COLUMN starter_file_upload_info.th_file_acl IS '缩略图文件ACL';
COMMENT ON COLUMN starter_file_upload_info.create_time IS '创建时间';
COMMENT ON TABLE starter_file_upload_info IS '文件记录表';

-- ----------------------------
-- Primary Key structure for table base_area
-- ----------------------------
ALTER TABLE base_area ADD CONSTRAINT base_area_pkey PRIMARY KEY (code);

-- ----------------------------
-- Primary Key structure for table base_city
-- ----------------------------
ALTER TABLE base_city ADD CONSTRAINT base_city_pkey PRIMARY KEY (code);

-- ----------------------------
-- Primary Key structure for table base_dict
-- ----------------------------
ALTER TABLE base_dict ADD CONSTRAINT base_dict_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table base_dict_item
-- ----------------------------
ALTER TABLE base_dict_item ADD CONSTRAINT base_dict_item_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table base_param
-- ----------------------------
ALTER TABLE base_param ADD CONSTRAINT base_param_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table base_province
-- ----------------------------
ALTER TABLE base_province ADD CONSTRAINT base_province_pkey PRIMARY KEY (code);

-- ----------------------------
-- Indexes structure for table base_street
-- ----------------------------
CREATE INDEX inx_area_code ON base_street USING btree (
                                                                    area_code COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX inx_area_code IS '县区';

-- ----------------------------
-- Primary Key structure for table base_street
-- ----------------------------
ALTER TABLE base_street ADD CONSTRAINT base_street_pkey PRIMARY KEY (code);

-- ----------------------------
-- Primary Key structure for table base_user_protocol
-- ----------------------------
ALTER TABLE base_user_protocol ADD CONSTRAINT base_user_protocol_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_client
-- ----------------------------
ALTER TABLE iam_client ADD CONSTRAINT iam_client_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_perm_code
-- ----------------------------
ALTER TABLE iam_perm_code ADD CONSTRAINT iam_perm_code_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE iam_perm_menu ADD CONSTRAINT iam_perm_menu_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_perm_path
-- ----------------------------
ALTER TABLE iam_perm_path ADD CONSTRAINT iam_perm_code_copy1_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_role
-- ----------------------------
ALTER TABLE iam_role ADD CONSTRAINT iam_role_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_role_code
-- ----------------------------
ALTER TABLE iam_role_code ADD CONSTRAINT iam_role_code_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_role_menu
-- ----------------------------
ALTER TABLE iam_role_menu ADD CONSTRAINT iam_role_menu_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_role_path
-- ----------------------------
ALTER TABLE iam_role_path ADD CONSTRAINT iam_role_path_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_user_expand_info
-- ----------------------------
ALTER TABLE iam_user_expand_info ADD CONSTRAINT iam_user_expand_info_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_user_info
-- ----------------------------
ALTER TABLE iam_user_info ADD CONSTRAINT iam_user_info_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table iam_user_role
-- ----------------------------
ALTER TABLE iam_user_role ADD CONSTRAINT iam_user_role_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_adapay_config
-- ----------------------------
CREATE INDEX idx_pay_adapay_config_app_id ON pay_adapay_config USING btree (
                                                                                         app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_adapay_config_app_id IS '汇付支付配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_adapay_config
-- ----------------------------
ALTER TABLE pay_adapay_config ADD CONSTRAINT pay_adapay_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_aggregate_bar_pay_config
-- ----------------------------
CREATE INDEX idx_aggregate_bar_pay_config_app_id ON pay_aggregate_bar_pay_config USING btree (
                                                                                                           app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_aggregate_bar_pay_config
-- ----------------------------
ALTER TABLE pay_aggregate_bar_pay_config ADD CONSTRAINT pk_aggregate_bar_pay_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_aggregate_pay_config
-- ----------------------------
ALTER TABLE pay_aggregate_pay_config ADD CONSTRAINT pay_checkout_aggregate_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_aggregate_qr_pay_config
-- ----------------------------
CREATE INDEX idx_aggregate_qr_pay_config_app_id ON pay_aggregate_qr_pay_config USING btree (
                                                                                                         app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_aggregate_qr_pay_config
-- ----------------------------
ALTER TABLE pay_aggregate_qr_pay_config ADD CONSTRAINT pk_aggregate_qr_pay_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_alipay_config
-- ----------------------------
CREATE INDEX idx_pay_alipay_config_app_id ON pay_alipay_config USING btree (
                                                                                         app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_alipay_config_app_id IS '支付宝配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_alipay_config
-- ----------------------------
ALTER TABLE pay_alipay_config ADD CONSTRAINT pay_alipay_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_alipay_isv_config
-- ----------------------------
CREATE INDEX idx_pay_alipay_isv_config_isv_no ON pay_alipay_isv_config USING btree (
                                                                                                 isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_alipay_isv_config_isv_no IS '支付宝服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_alipay_isv_config
-- ----------------------------
ALTER TABLE pay_alipay_isv_config ADD CONSTRAINT pay_alipay_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_alipay_sub_config
-- ----------------------------
CREATE INDEX idx_pay_alipay_sub_config_app_id ON pay_alipay_sub_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_alipay_sub_config_app_id IS '支付宝特约商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_alipay_sub_config
-- ----------------------------
ALTER TABLE pay_alipay_sub_config ADD CONSTRAINT pay_alipay_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_api_const
-- ----------------------------
ALTER TABLE pay_api_const ADD CONSTRAINT pay_channel_const_copy1_pkey1 PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_cashier_code
-- ----------------------------
CREATE INDEX idx_pay_cashier_code_app_id ON pay_cashier_code USING btree (
                                                                                       app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
CREATE INDEX idx_pay_cashier_code_code ON pay_cashier_code USING btree (
                                                                                     code COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
CREATE INDEX idx_pay_cashier_code_deleted ON pay_cashier_code USING btree (
                                                                                        deleted pg_catalog.bool_ops ASC NULLS LAST
    );
CREATE INDEX idx_pay_cashier_code_enable ON pay_cashier_code USING btree (
                                                                                       enable pg_catalog.bool_ops ASC NULLS LAST
    );
CREATE INDEX idx_pay_cashier_code_mch_no ON pay_cashier_code USING btree (
                                                                                       mch_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_cashier_code
-- ----------------------------
ALTER TABLE pay_cashier_code ADD CONSTRAINT pk_pay_cashier_code PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_cashier_code_config
-- ----------------------------
CREATE INDEX idx_cashier_code_config_app_id ON pay_cashier_code_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_cashier_code_config
-- ----------------------------
ALTER TABLE pay_cashier_code_config ADD CONSTRAINT pk_cashier_code_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_channel_config
-- ----------------------------
ALTER TABLE pay_channel_config ADD CONSTRAINT pay_channel_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_channel_const
-- ----------------------------
ALTER TABLE pay_channel_const ADD CONSTRAINT pay_channel_const_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_checkout_counter_config
-- ----------------------------
CREATE INDEX idx_checkout_counter_config_app_id ON pay_checkout_counter_config USING btree (
                                                                                                         app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_checkout_counter_config
-- ----------------------------
ALTER TABLE pay_checkout_counter_config ADD CONSTRAINT pk_checkout_counter_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_close_record
-- ----------------------------
CREATE INDEX biz_order_no ON pay_close_record USING btree (
                                                                        biz_order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX biz_order_no IS '商户支付订单号索引';
CREATE INDEX order_no ON pay_close_record USING btree (
                                                                    order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX order_no IS '支付订单号索引';

-- ----------------------------
-- Primary Key structure for table pay_close_record
-- ----------------------------
ALTER TABLE pay_close_record ADD CONSTRAINT pay_close_record_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_dougong_isv_config
-- ----------------------------
CREATE INDEX idx_pay_dougong_isv_config_isv_no ON pay_dougong_isv_config USING btree (
                                                                                                   isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_dougong_isv_config_isv_no IS '斗拱服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_dougong_isv_config
-- ----------------------------
ALTER TABLE pay_dougong_isv_config ADD CONSTRAINT pay_dougong_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_dougong_sub_config
-- ----------------------------
CREATE INDEX idx_pay_dougong_sub_config_app_id ON pay_dougong_sub_config USING btree (
                                                                                                   app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_dougong_sub_config_app_id IS '斗拱特约商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_dougong_sub_config
-- ----------------------------
ALTER TABLE pay_dougong_sub_config ADD CONSTRAINT pay_dougong_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_fuyou_isv_config
-- ----------------------------
CREATE INDEX idx_fuyou_isv_config_isv_no ON pay_fuyou_isv_config USING btree (
                                                                                           isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_fuyou_isv_config
-- ----------------------------
ALTER TABLE pay_fuyou_isv_config ADD CONSTRAINT pay_fuyou_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_fuyou_sub_config
-- ----------------------------
CREATE INDEX idx_fuyou_sub_config_app_id ON pay_fuyou_sub_config USING btree (
                                                                                           app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_fuyou_sub_config
-- ----------------------------
ALTER TABLE pay_fuyou_sub_config ADD CONSTRAINT pay_fuyou_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_gateway_pay_config
-- ----------------------------
CREATE INDEX idx_gateway_config_app_id ON pay_gateway_pay_config USING btree (
                                                                                           app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_gateway_pay_config
-- ----------------------------
ALTER TABLE pay_gateway_pay_config ADD CONSTRAINT pk_gateway_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_gateway_pay_read_config
-- ----------------------------
CREATE INDEX idx_gateway_read_config_app_id ON pay_gateway_pay_read_config USING btree (
                                                                                                     app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_gateway_pay_read_config
-- ----------------------------
ALTER TABLE pay_gateway_pay_read_config ADD CONSTRAINT pay_gateway_config_copy1_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_hkrt_isv_config
-- ----------------------------
CREATE INDEX idx_pay_hkrt_isv_config_isv_no ON pay_hkrt_isv_config USING btree (
                                                                                             isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_hkrt_isv_config_isv_no IS '海科服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_hkrt_isv_config
-- ----------------------------
ALTER TABLE pay_hkrt_isv_config ADD CONSTRAINT pay_hkrt_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_hkrt_sub_config
-- ----------------------------
CREATE INDEX idx_pay_hkrt_sub_config_app_id ON pay_hkrt_sub_config USING btree (
                                                                                             app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_hkrt_sub_config_app_id IS '海科子商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_hkrt_sub_config
-- ----------------------------
ALTER TABLE pay_hkrt_sub_config ADD CONSTRAINT pay_hkrt_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_isv_aggregate_bar_pay_config
-- ----------------------------
CREATE INDEX idx_isv_aggregate_bar_pay_config_isv_no ON pay_isv_aggregate_bar_pay_config USING btree (
                                                                                                                   isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_isv_aggregate_bar_pay_config
-- ----------------------------
ALTER TABLE pay_isv_aggregate_bar_pay_config ADD CONSTRAINT pk_isv_aggregate_bar_pay_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_isv_aggregate_pay_config
-- ----------------------------
ALTER TABLE pay_isv_aggregate_pay_config ADD CONSTRAINT pay_aggregate_pay_config_copy1_pkey1 PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_isv_aggregate_qr_pay_config
-- ----------------------------
CREATE INDEX idx_isv_aggregate_qr_pay_config_isv_no ON pay_isv_aggregate_qr_pay_config USING btree (
                                                                                                                 isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_isv_aggregate_qr_pay_config
-- ----------------------------
ALTER TABLE pay_isv_aggregate_qr_pay_config ADD CONSTRAINT pk_isv_aggregate_qr_pay_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_isv_channel_config
-- ----------------------------
ALTER TABLE pay_isv_channel_config ADD CONSTRAINT pay_channel_config_copy1_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_isv_checkout_counter_config
-- ----------------------------
CREATE INDEX idx_isv_checkout_counter_config_isv_no ON pay_isv_checkout_counter_config USING btree (
                                                                                                                 isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_isv_checkout_counter_config
-- ----------------------------
ALTER TABLE pay_isv_checkout_counter_config ADD CONSTRAINT pk_isv_checkout_counter_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_isv_gateway_config
-- ----------------------------
CREATE INDEX idx_isv_gateway_config_isv_no ON pay_isv_gateway_config USING btree (
                                                                                               isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_isv_gateway_config
-- ----------------------------
ALTER TABLE pay_isv_gateway_config ADD CONSTRAINT pk_isv_gateway_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_isv_info
-- ----------------------------
ALTER TABLE pay_isv_info ADD CONSTRAINT pay_mch_app_copy1_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_isv_mini_quickly_config
-- ----------------------------
CREATE INDEX idx_isv_mini_quickly_config_isv_no ON pay_isv_mini_quickly_config USING btree (
                                                                                                         isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_isv_mini_quickly_config
-- ----------------------------
ALTER TABLE pay_isv_mini_quickly_config ADD CONSTRAINT pk_isv_mini_quickly_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_lakala_isv_config
-- ----------------------------
CREATE INDEX idx_lakala_isv_config_isv_no ON pay_lakala_isv_config USING btree (
                                                                                             isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_lakala_isv_config
-- ----------------------------
ALTER TABLE pay_lakala_isv_config ADD CONSTRAINT pay_lakala_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_lakala_sub_config
-- ----------------------------
CREATE INDEX idx_lakala_sub_config_app_id ON pay_lakala_sub_config USING btree (
                                                                                             app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_lakala_sub_config
-- ----------------------------
ALTER TABLE pay_lakala_sub_config ADD CONSTRAINT pay_lakala_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_leshua_isv_config
-- ----------------------------
CREATE INDEX idx_pay_leshua_isv_config_isv_no ON pay_leshua_isv_config USING btree (
                                                                                                 isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_leshua_isv_config_isv_no IS '乐刷服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_leshua_isv_config
-- ----------------------------
ALTER TABLE pay_leshua_isv_config ADD CONSTRAINT pay_leshua_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_leshua_sub_config
-- ----------------------------
CREATE INDEX idx_pay_leshua_sub_config_app_id ON pay_leshua_sub_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_leshua_sub_config_app_id IS '乐刷子商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_leshua_sub_config
-- ----------------------------
ALTER TABLE pay_leshua_sub_config ADD CONSTRAINT pay_leshua_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_mch_app
-- ----------------------------
ALTER TABLE pay_mch_app ADD CONSTRAINT pay_mch_app_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_merchant
-- ----------------------------
ALTER TABLE pay_merchant ADD CONSTRAINT pay_merchant_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_record
-- ----------------------------
ALTER TABLE pay_merchant_callback_record ADD CONSTRAINT pay_merchant_callback_record_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_task
-- ----------------------------
ALTER TABLE pay_merchant_callback_task ADD CONSTRAINT pay_merchant_callback_task_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_merchant_credential
-- ----------------------------
CREATE INDEX idx_pay_merchant_credential_isv_no ON pay_merchant_credential USING btree (
                                                                                                     isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
CREATE INDEX idx_pay_merchant_credential_mch_no ON pay_merchant_credential USING btree (
                                                                                                     mch_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_merchant_credential
-- ----------------------------
ALTER TABLE pay_merchant_credential ADD CONSTRAINT pk_pay_merchant_credential PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_merchant_user
-- ----------------------------
ALTER TABLE pay_merchant_user ADD CONSTRAINT pay_user_merchant_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_mini_quickly_config
-- ----------------------------
CREATE INDEX idx_mini_quickly_config_app_id ON pay_mini_quickly_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_mini_quickly_config
-- ----------------------------
ALTER TABLE pay_mini_quickly_config ADD CONSTRAINT pk_mini_quickly_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_onb_mch_info
-- ----------------------------
ALTER TABLE pay_onb_mch_info ADD CONSTRAINT pay_isv_mch_info_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_order
-- ----------------------------
CREATE INDEX order_biz_order_order_no_idx ON pay_order USING btree (
                                                                                 biz_order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
CREATE INDEX order_pay_order_order_no_idx ON pay_order USING btree (
                                                                                 order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
CREATE INDEX order_pay_order_out_order_no_idx ON pay_order USING btree (
                                                                                     out_order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_order
-- ----------------------------
ALTER TABLE pay_order ADD CONSTRAINT pay_order_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_order_expand
-- ----------------------------
ALTER TABLE pay_order_expand ADD CONSTRAINT pay_order_expand_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_platform_basic_config
-- ----------------------------
ALTER TABLE pay_platform_basic_config ADD CONSTRAINT pay_platform_basic_config_pkey PRIMARY KEY (id);


-- ----------------------------
-- Primary Key structure for table pay_platform_integration_config
-- ----------------------------
ALTER TABLE pay_platform_integration_config ADD CONSTRAINT pk_platform_integration_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_platform_url_config
-- ----------------------------
ALTER TABLE pay_platform_url_config ADD CONSTRAINT pay_platform_url_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_platform_website_config
-- ----------------------------
ALTER TABLE pay_platform_website_config ADD CONSTRAINT pay_platform_website_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_refund_order
-- ----------------------------
CREATE INDEX refund_biz_order_no ON pay_refund_order USING btree (
                                                                               biz_order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_biz_order_no IS '商户支付订单号索引';
CREATE INDEX refund_biz_refund_no ON pay_refund_order USING btree (
                                                                                biz_refund_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_biz_refund_no IS '商户退款号索引';
CREATE INDEX refund_order_id ON pay_refund_order USING btree (
                                                                           order_id pg_catalog.int8_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_order_id IS '支付订单ID索引';
CREATE INDEX refund_order_no ON pay_refund_order USING btree (
                                                                           order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_order_no IS '支付订单号索引';
CREATE INDEX refund_out_order_no ON pay_refund_order USING btree (
                                                                               out_order_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_out_order_no IS '通道支付订单号索引';
CREATE INDEX refund_out_refund_no ON pay_refund_order USING btree (
                                                                                out_refund_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_out_refund_no IS '通道退款交易号索引';
CREATE INDEX refund_refund_no ON pay_refund_order USING btree (
                                                                            refund_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX refund_refund_no IS '退款号索引';

-- ----------------------------
-- Primary Key structure for table pay_refund_order
-- ----------------------------
ALTER TABLE pay_refund_order ADD CONSTRAINT pay_refund_order_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_sand_isv_config
-- ----------------------------
CREATE INDEX idx_sand_isv_config_isv_no ON pay_sand_isv_config USING btree (
                                                                                         isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_sand_isv_config
-- ----------------------------
ALTER TABLE pay_sand_isv_config ADD CONSTRAINT pay_sand_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_sand_settle_bind_info
-- ----------------------------
CREATE INDEX idx_sand_settle_bind_info_onb_mch_id ON pay_sand_settle_bind_info USING btree (
                                                                                                         onb_mch_id pg_catalog.int8_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_sand_settle_bind_info
-- ----------------------------
ALTER TABLE pay_sand_settle_bind_info ADD CONSTRAINT pay_sand_settle_bind_info_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_sand_sub_config
-- ----------------------------
CREATE INDEX idx_sand_sub_config_app_id ON pay_sand_sub_config USING btree (
                                                                                         app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_sand_sub_config
-- ----------------------------
ALTER TABLE pay_sand_sub_config ADD CONSTRAINT pay_sand_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_trade_callback_record
-- ----------------------------
CREATE INDEX out_trade_no ON pay_trade_callback_record USING btree (
                                                                                 out_trade_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX out_trade_no IS '通道交易号索引';
CREATE INDEX trade_no ON pay_trade_callback_record USING btree (
                                                                             trade_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX trade_no IS '本地交易号索引';

-- ----------------------------
-- Primary Key structure for table pay_trade_callback_record
-- ----------------------------
ALTER TABLE pay_trade_callback_record ADD CONSTRAINT pay_callback_record_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_trade_flow_record
-- ----------------------------
ALTER TABLE pay_trade_flow_record ADD CONSTRAINT pay_trade_flow_record_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table pay_trade_sync_record
-- ----------------------------
ALTER TABLE pay_trade_sync_record ADD CONSTRAINT pay_trade_sync_record_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_transfer_order
-- ----------------------------
CREATE INDEX transfer_biz_transfer_no ON pay_transfer_order USING btree (
                                                                                      biz_transfer_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX transfer_biz_transfer_no IS '商户转账号索引';
CREATE INDEX transfer_out_transfer_no ON pay_transfer_order USING btree (
                                                                                      out_transfer_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX transfer_out_transfer_no IS '通道转账号索引';
CREATE INDEX transfer_transfer_no ON pay_transfer_order USING btree (
                                                                                  transfer_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX transfer_transfer_no IS '转账号索引';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order
-- ----------------------------
ALTER TABLE pay_transfer_order ADD CONSTRAINT pay_transfer_order_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_vbill_isv_config
-- ----------------------------
CREATE INDEX idx_pay_vbill_isv_config_isv_no ON pay_vbill_isv_config USING btree (
                                                                                               isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_vbill_isv_config_isv_no IS '随行付服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_vbill_isv_config
-- ----------------------------
ALTER TABLE pay_vbill_isv_config ADD CONSTRAINT pay_vbill_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_vbill_sub_config
-- ----------------------------
CREATE INDEX idx_pay_vbill_sub_config_app_id ON pay_vbill_sub_config USING btree (
                                                                                               app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_vbill_sub_config_app_id IS '随行付子商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_vbill_sub_config
-- ----------------------------
ALTER TABLE pay_vbill_sub_config ADD CONSTRAINT pay_vbill_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_wechat_isv_config
-- ----------------------------
CREATE INDEX idx_pay_wechat_isv_config_isv_no ON pay_wechat_isv_config USING btree (
                                                                                                 isv_no COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_wechat_isv_config_isv_no IS '微信服务商配置服务商号索引';

-- ----------------------------
-- Primary Key structure for table pay_wechat_isv_config
-- ----------------------------
ALTER TABLE pay_wechat_isv_config ADD CONSTRAINT pay_wechat_isv_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_wechat_pay_config
-- ----------------------------
CREATE INDEX idx_pay_wechat_pay_config_app_id ON pay_wechat_pay_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_wechat_pay_config_app_id IS '微信支付配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_wechat_pay_config
-- ----------------------------
ALTER TABLE pay_wechat_pay_config ADD CONSTRAINT pay_wechat_pay_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_wechat_sub_config
-- ----------------------------
CREATE INDEX idx_pay_wechat_sub_config_app_id ON pay_wechat_sub_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );
COMMENT ON INDEX idx_pay_wechat_sub_config_app_id IS '微信特约商户配置应用号索引';

-- ----------------------------
-- Primary Key structure for table pay_wechat_sub_config
-- ----------------------------
ALTER TABLE pay_wechat_sub_config ADD CONSTRAINT pay_wechat_sub_config_pkey PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_yeepay_isv_config
-- ----------------------------
CREATE INDEX idx_pay_yeepay_config_app_id ON pay_yeepay_isv_config USING btree (
                                                                                             app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_yeepay_isv_config
-- ----------------------------
ALTER TABLE pay_yeepay_isv_config ADD CONSTRAINT pk_pay_yeepay_config PRIMARY KEY (id);

-- ----------------------------
-- Indexes structure for table pay_yeepay_sub_config
-- ----------------------------
CREATE INDEX idx_pay_yeepay_sub_config_app_id ON pay_yeepay_sub_config USING btree (
                                                                                                 app_id COLLATE pg_catalog.default pg_catalog.text_ops ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table pay_yeepay_sub_config
-- ----------------------------
ALTER TABLE pay_yeepay_sub_config ADD CONSTRAINT pk_pay_yeepay_sub_config PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table starter_audit_login_log
-- ----------------------------
ALTER TABLE starter_audit_login_log ADD CONSTRAINT starter_audit_login_log_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table starter_audit_operate_log
-- ----------------------------
ALTER TABLE starter_audit_operate_log ADD CONSTRAINT starter_audit_operate_log_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table starter_file_platform
-- ----------------------------
ALTER TABLE starter_file_platform ADD CONSTRAINT starter_file_platform_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table starter_file_upload_info
-- ----------------------------
ALTER TABLE starter_file_upload_info ADD CONSTRAINT starter_file_upload_info_pkey PRIMARY KEY (id);
