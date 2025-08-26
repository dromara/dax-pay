
-- SQLINES FOR EVALUATION USE ONLY (14 DAYS)
CREATE TABLE `base_area` (
                             `code` varchar(6) NOT NULL,
                             `name` varchar(60) NOT NULL COMMENT '区域名称',
                             `city_code` varchar(4) NOT NULL COMMENT '城市编码'
) COMMENT '县区表';

CREATE TABLE `base_city` (
                             `code` varchar(4) NOT NULL COMMENT '城市编码',
                             `name` varchar(60) NOT NULL COMMENT '城市名称',
                             `province_code` varchar(2) NOT NULL COMMENT '省份编码'
) COMMENT '城市表';

CREATE TABLE `base_dict` (
                             `id` int8 NOT NULL COMMENT '主键',
                             `name` varchar(50) COMMENT '名称',
                             `group_tag` varchar(50) COMMENT '分类标签',
                             `code` varchar(50) COMMENT '编码',
                             `remark` varchar(50) COMMENT '备注',
                             `creator` int8 comment '创建者ID',
                             `create_time` datetime(6) COMMENT '创建时间',
                             `last_modifier` int8 comment '最后修改ID',
                             `last_modified_time` datetime(6) COMMENT '最后修改时间',
                             `version` int4 NOT NULL COMMENT '版本号',
                             `enable` bool comment '是否启用',
                             `deleted` bool NOT NULL COMMENT '删除标志'
) COMMENT '字典表';

CREATE TABLE `base_dict_item` (
                                  `id` int8 NOT NULL COMMENT '主键',
                                  `dict_id` int8 NOT NULL COMMENT '字典ID',
                                  `dict_code` varchar(50) COMMENT '字典编码',
                                  `code` varchar(50) COMMENT '字典项编码',
                                  `name` varchar(50) COMMENT '名称',
                                  `sort_no` int4 comment '字典项排序',
                                  `enable` bool comment '是否启用',
                                  `remark` varchar(50) COMMENT '备注',
                                  `creator` int8 comment '创建者ID',
                                  `create_time` datetime(6) COMMENT '创建时间',
                                  `last_modifier` int8 comment '最后修改ID',
                                  `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                  `version` int4 NOT NULL COMMENT '版本号',
                                  `deleted` bool NOT NULL COMMENT '删除标志'
) COMMENT '字典项';

CREATE TABLE `base_param` (
                              `id` int8 NOT NULL COMMENT '主键',
                              `creator` int8 comment '创建者ID',
                              `create_time` datetime(6) COMMENT '创建时间',
                              `last_modifier` int8 comment '最后修改ID',
                              `last_modified_time` datetime(6) COMMENT '最后修改时间',
                              `version` int4 NOT NULL COMMENT '版本号',
                              `deleted` bool NOT NULL COMMENT '删除标志',
                              `name` varchar(50) NOT NULL COMMENT '参数名称',
                              `key` varchar(50) NOT NULL COMMENT '参数键名',
                              `value` varchar(500) NOT NULL COMMENT '参数值',
                              `type` varchar(20) COMMENT '参数类型',
                              `enable` bool NOT NULL COMMENT '启用状态',
                              `internal` bool NOT NULL COMMENT '内置参数',
                              `remark` varchar(200) COMMENT '备注'
) COMMENT '系统参数';

CREATE TABLE `base_province` (
                                 `code` varchar(2) NOT NULL COMMENT '省份编码',
                                 `name` varchar(30) NOT NULL COMMENT '省份名称'
) COMMENT '省份表';

CREATE TABLE `base_street` (
                               `code` varchar(9) NOT NULL COMMENT '编码',
                               `name` varchar(60) NOT NULL COMMENT '街道名称',
                               `area_code` varchar(6) NOT NULL COMMENT '县区编码'
) COMMENT '街道表';

CREATE TABLE `base_user_protocol` (
                                      `id` int8 NOT NULL COMMENT '主键',
                                      `creator` int8 comment '创建者ID',
                                      `create_time` datetime(6) COMMENT '创建时间',
                                      `last_modifier` int8 comment '最后修改ID',
                                      `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                      `version` int4 NOT NULL COMMENT '版本号',
                                      `deleted` bool NOT NULL COMMENT '删除标志',
                                      `name` varchar(32) COMMENT '名称',
                                      `show_name` varchar(64) COMMENT '显示名称',
                                      `type` varchar(32) COMMENT '类型',
                                      `default_protocol` bool comment '默认协议',
                                      `content` longtext comment '内容'
) COMMENT '用户协议';

CREATE TABLE `iam_client` (
                              `id` int8 NOT NULL COMMENT '主键',
                              `creator` int8 comment '创建者ID',
                              `create_time` datetime(6) COMMENT '创建时间',
                              `last_modifier` int8 comment '最后修改ID',
                              `last_modified_time` datetime(6) COMMENT '最后修改时间',
                              `version` int4 NOT NULL COMMENT '版本号',
                              `deleted` bool NOT NULL COMMENT '删除标志',
                              `code` varchar(50) NOT NULL COMMENT '编码',
                              `name` varchar(50) NOT NULL COMMENT '名称',
                              `internal` bool NOT NULL COMMENT '是否系统内置',
                              `remark` varchar(250) COMMENT '备注'
) COMMENT '认证终端';

CREATE TABLE `iam_perm_code` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `creator` int8 comment '创建者ID',
                                 `create_time` datetime(6) COMMENT '创建时间',
                                 `last_modifier` int8 comment '最后修改ID',
                                 `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                 `version` int4 NOT NULL COMMENT '版本号',
                                 `deleted` bool NOT NULL COMMENT '删除标志',
                                 `pid` int8 comment '父ID',
                                 `code` varchar(50) COMMENT '权限码',
                                 `name` varchar(50) COMMENT '名称',
                                 `remark` varchar(300) COMMENT '备注',
                                 `leaf` bool NOT NULL COMMENT '是否为叶子结点'
) COMMENT '权限码';

CREATE TABLE `iam_perm_menu` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `creator` int8 comment '创建者ID',
                                 `create_time` datetime(6) COMMENT '创建时间',
                                 `last_modifier` int8 comment '最后修改ID',
                                 `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                 `version` int4 NOT NULL COMMENT '版本号',
                                 `deleted` bool NOT NULL COMMENT '删除标志',
                                 `pid` int8 comment '父id',
                                 `client_code` varchar(50) NOT NULL COMMENT '关联终端code',
                                 `title` varchar(100) COMMENT '菜单标题',
                                 `name` varchar(100) COMMENT '路由名称',
                                 `icon` varchar(100) COMMENT '菜单图标',
                                 `hidden` bool NOT NULL COMMENT '是否隐藏',
                                 `hide_children_menu` bool NOT NULL COMMENT '是否隐藏子菜单',
                                 `component` varchar(300) COMMENT '组件',
                                 `path` varchar(300) COMMENT '访问路径',
                                 `redirect` varchar(300) COMMENT '菜单跳转地址(重定向)',
                                 `sort_no` float4 comment '菜单排序',
                                 `root` bool NOT NULL COMMENT '是否是一级菜单',
                                 `keep_alive` bool comment '是否缓存页面',
                                 `target_outside` bool comment '是否为外部打开',
                                 `full_screen` bool comment '是否全屏打开',
                                 `remark` varchar(200) COMMENT '描述'
) COMMENT '菜单权限';

CREATE TABLE `iam_perm_path` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `creator` int8 comment '创建者ID',
                                 `create_time` datetime(6) COMMENT '创建时间',
                                 `parent_code` varchar(50) COMMENT '上级编码',
                                 `client_code` varchar(50) NOT NULL COMMENT '终端编码',
                                 `code` varchar(50) NOT NULL COMMENT '标识编码(模块、分组标识)',
                                 `name` varchar(50) COMMENT '名称(请求路径、模块、分组名称)',
                                 `leaf` bool NOT NULL COMMENT '叶子节点',
                                 `path` varchar(200) COMMENT '请求路径',
                                 `method` varchar(10) COMMENT '请求类型, 为全大写单词'
) COMMENT '请求权限(url)';

CREATE TABLE `iam_role` (
                            `id` int8 NOT NULL COMMENT '主键',
                            `creator` int8 comment '创建者ID',
                            `create_time` datetime(6) COMMENT '创建时间',
                            `last_modifier` int8 comment '最后修改ID',
                            `last_modified_time` datetime(6) COMMENT '最后修改时间',
                            `version` int4 NOT NULL COMMENT '版本号',
                            `deleted` bool NOT NULL COMMENT '删除标志',
                            `pid` int8 comment '父级Id',
                            `code` varchar(50) COMMENT '编码',
                            `name` varchar(100) COMMENT '名称',
                            `internal` bool comment '是否系统内置',
                            `remark` varchar(255) COMMENT '备注'
) COMMENT '角色';

CREATE TABLE `iam_role_code` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `role_id` int8 NOT NULL COMMENT '角色id',
                                 `code_id` int8 NOT NULL COMMENT '权限码'
) COMMENT '角色权限码关联关系';

CREATE TABLE `iam_role_menu` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `role_id` int8 NOT NULL COMMENT '角色id',
                                 `client_code` varchar(50) NOT NULL COMMENT '终端编码',
                                 `menu_id` int8 NOT NULL COMMENT '菜单id'
) COMMENT '角色菜单关联关系';

CREATE TABLE `iam_role_path` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `role_id` int8 NOT NULL COMMENT '角色id',
                                 `client_code` varchar(50) NOT NULL COMMENT '终端编码',
                                 `path_id` int8 NOT NULL COMMENT '请求资源id'
) COMMENT '角色路径关联关系';

CREATE TABLE `iam_user_expand_info` (
                                        `id` int8 NOT NULL COMMENT '主键',
                                        `creator` int8 comment '创建者ID',
                                        `create_time` datetime(6) COMMENT '创建时间',
                                        `last_modifier` int8 comment '最后修改ID',
                                        `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                        `version` int4 NOT NULL COMMENT '版本号',
                                        `deleted` bool NOT NULL COMMENT '删除标志',
                                        `sex` varchar(10) COMMENT '性别',
                                        `avatar` varchar(300) COMMENT '头像图片url',
                                        `birthday` date comment '生日',
                                        `last_login_time` datetime(0) COMMENT '上次登录时间',
                                        `register_time` datetime(6) COMMENT '注册时间',
                                        `current_login_time` datetime(6) COMMENT '本次登录时间',
                                        `initial_password` bool comment '是否初始密码',
                                        `expire_password` bool comment '密码是否过期',
                                        `last_change_password_time` datetime(6) COMMENT '上次修改密码时间'
) COMMENT '用户扩展信息';

CREATE TABLE `iam_user_info` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `creator` int8 comment '创建者ID',
                                 `create_time` datetime(6) COMMENT '创建时间',
                                 `last_modifier` int8 comment '最后修改ID',
                                 `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                 `version` int4 NOT NULL COMMENT '版本号',
                                 `deleted` bool NOT NULL COMMENT '删除标志',
                                 `name` varchar(50) COMMENT '名称',
                                 `account` varchar(50) NOT NULL COMMENT '账号',
                                 `password` varchar(120) NOT NULL COMMENT '密码',
                                 `phone` varchar(50) COMMENT '手机号',
                                 `email` varchar(50) COMMENT '邮箱',
                                 `administrator` bool NOT NULL COMMENT '是否管理员',
                                 `status` varchar(50) COMMENT '账号状态'
) COMMENT '用户核心信息';

CREATE TABLE `iam_user_role` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `user_id` int8 NOT NULL COMMENT '用户',
                                 `role_id` int8 NOT NULL COMMENT '角色'
) COMMENT '用户角色关联关系';

CREATE TABLE `pay_aggregate_bar_pay_config` (
                                                `id` int8 NOT NULL COMMENT '主键',
                                                `creator` int8 comment '创建者ID',
                                                `create_time` datetime(6) COMMENT '创建时间',
                                                `last_modifier` int8 comment '最后修者ID',
                                                `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                                `version` int4 NOT NULL COMMENT '乐观锁',
                                                `deleted` bool NOT NULL COMMENT '删除标志',
                                                `app_id` varchar(32) COMMENT '应用号',
                                                `bar_pay_type` varchar(32) COMMENT '付款码类型',
                                                `channel` varchar(32) COMMENT '通道',
                                                `pay_method` varchar(32) COMMENT '支付方式',
                                                `other_method` varchar(32) COMMENT '其他支付方式',
                                                `mch_no` varchar(32) COMMENT '商户号',
                                                `agent_no` varchar(32) COMMENT '所属代理商',
                                                `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '网关聚合付款码支付配置';

CREATE TABLE `pay_aggregate_pay_config` (
                                            `id` int8 NOT NULL COMMENT '主键',
                                            `creator` int8 comment '创建者ID',
                                            `create_time` datetime(6) COMMENT '创建时间',
                                            `last_modifier` int8 comment '最后修者ID',
                                            `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                            `version` int4 NOT NULL COMMENT '乐观锁',
                                            `deleted` bool NOT NULL COMMENT '删除标志',
                                            `app_id` varchar(32) COMMENT '应用号',
                                            `aggregate_type` varchar(32) COMMENT '聚合支付类型',
                                            `channel` varchar(32) COMMENT '通道',
                                            `pay_method` varchar(32) COMMENT '支付方式',
                                            `auto_launch` bool comment '自动拉起支付',
                                            `other_method` varchar(32) COMMENT '其他支付方式',
                                            `mch_no` varchar(32) COMMENT '商户号',
                                            `need_open_id` bool comment '需要获取OpenId',
                                            `call_type` varchar(32) COMMENT '调用方式',
                                            `open_id_get_type` varchar(32) COMMENT 'OpenId获取方式',
                                            `isv_no` varchar(32) COMMENT '所属服务商',
                                            `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '网关聚合支付配置';

CREATE TABLE `pay_api_const` (
                                 `id` int8 NOT NULL COMMENT '主键',
                                 `code` varchar(50) NOT NULL COMMENT '编码',
                                 `name` varchar(50) NOT NULL COMMENT '接口名称',
                                 `api` varchar(200) NOT NULL COMMENT '接口地址',
                                 `enable` bool comment '是否启用',
                                 `remark` varchar(200) COMMENT '备注'
) COMMENT '支付接口常量';

CREATE TABLE `pay_audio_device` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `creator` int8 comment '创建者ID',
                                    `create_time` datetime(0) COMMENT '创建时间',
                                    `last_modifier` int8 comment '最后修改ID',
                                    `last_modified_time` datetime(0) COMMENT '最后修改时间',
                                    `version` int4 NOT NULL COMMENT '版本号',
                                    `deleted` bool NOT NULL COMMENT '删除标志',
                                    `isv_no` varchar(32) COMMENT '服务商号',
                                    `agent_no` varchar(32) COMMENT '代理商号',
                                    `mch_no` varchar(32) COMMENT '商户号',
                                    `app_id` varchar(32) COMMENT '应用号',
                                    `device_no` varchar(32) COMMENT '设备号',
                                    `batch_no` varchar(32) COMMENT '批次号',
                                    `bind_type` varchar(32) COMMENT '绑定类型',
                                    `cashier_code_id` int8 comment '绑定码牌ID',
                                    `vendor` varchar(32) COMMENT '设备厂商',
                                    `config_id` int8 comment '配置ID',
                                    `enable` bool comment '状态',
                                    `name` varchar(64) COMMENT '设备名称'
) COMMENT '音箱设备';

CREATE TABLE `pay_audio_device_config` (
                                           `id` int8 NOT NULL COMMENT '主键',
                                           `creator` int8 comment '创建者ID',
                                           `create_time` datetime(6) COMMENT '创建时间',
                                           `last_modifier` int8 comment '最后修改ID',
                                           `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                           `version` int4 NOT NULL COMMENT '版本号',
                                           `deleted` bool NOT NULL COMMENT '删除标志',
                                           `name` varchar(50) COMMENT '码牌名称',
                                           `vendor` varchar(32) COMMENT '厂商',
                                           `enable` bool NOT NULL COMMENT '是否启用',
                                           `config` varchar(5000) COMMENT '配置Json'
) COMMENT '音箱厂商配置';

CREATE TABLE `pay_cashier_code` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `creator` int8 comment '创建者ID',
                                    `create_time` datetime(0) COMMENT '创建时间',
                                    `last_modifier` int8 comment '最后修改ID',
                                    `last_modified_time` datetime(0) COMMENT '最后修改时间',
                                    `version` int4 NOT NULL COMMENT '版本号',
                                    `deleted` bool NOT NULL COMMENT '删除标志',
                                    `isv_no` varchar(32) COMMENT '服务商号',
                                    `agent_no` varchar(32) COMMENT '代理商号',
                                    `mch_no` varchar(32) COMMENT '商户号',
                                    `app_id` varchar(32) COMMENT '应用号',
                                    `amount_type` varchar(16) COMMENT '金额类型',
                                    `amount` numeric(16,4) COMMENT '金额',
                                    `name` varchar(64) COMMENT '名称',
                                    `code` varchar(32) COMMENT '编号',
                                    `config_id` int8 comment '配置Id',
                                    `template_id` int8 comment '模板ID',
                                    `enable` bool comment '状态',
                                    `batch_no` varchar(32) COMMENT '批次号'
) COMMENT '收款码牌';

CREATE TABLE `pay_cashier_code_config` (
                                           `id` int8 NOT NULL COMMENT '主键',
                                           `creator` int8 comment '创建者ID',
                                           `create_time` datetime(6) COMMENT '创建时间',
                                           `last_modifier` int8 comment '最后修改ID',
                                           `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                           `version` int4 NOT NULL COMMENT '版本号',
                                           `name` varchar(50) COMMENT '码牌名称',
                                           `remark` varchar(200) COMMENT '备注',
                                           `enable` bool NOT NULL COMMENT '是否启用',
                                           `deleted` bool NOT NULL COMMENT '删除标志',
                                           `allocation` bool comment '是否开启分账',
                                           `auto_allocation` bool comment '自动分账',
                                           `limit_pay` varchar(32) COMMENT '限制可用的支付方式'
) COMMENT '收银码牌配置';

CREATE TABLE `pay_cashier_code_scene_config` (
                                                 `id` int8 NOT NULL COMMENT '主键',
                                                 `creator` int8 comment '创建者ID',
                                                 `create_time` datetime(6) COMMENT '创建时间',
                                                 `last_modifier` int8 comment '最后修改ID',
                                                 `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                                 `version` int4 NOT NULL COMMENT '版本号',
                                                 `config_id` int8 NOT NULL COMMENT '码牌ID',
                                                 `scene` varchar(32) NOT NULL COMMENT '收银场景',
                                                 `channel` varchar(32) COMMENT '支付通道',
                                                 `pay_method` varchar(32) COMMENT '支付方式',
                                                 `mch_no` varchar(32) COMMENT '商户号',
                                                 `deleted` bool NOT NULL COMMENT '删除标志',
                                                 `need_open_id` bool comment '需要获取OpenId',
                                                 `call_type` varchar(32) COMMENT '发起调用的类型',
                                                 `other_method` varchar(32) COMMENT '其他支付方式',
                                                 `open_id_get_type` varchar(32) COMMENT 'OpenId获取方式'
) COMMENT '各类型码牌配置';

CREATE TABLE `pay_cashier_group_config` (
                                            `id` int8 NOT NULL COMMENT '主键',
                                            `creator` int8 comment '创建者ID',
                                            `create_time` datetime(6) COMMENT '创建时间',
                                            `last_modifier` int8 comment '最后修者ID',
                                            `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                            `version` int4 NOT NULL COMMENT '乐观锁',
                                            `deleted` bool NOT NULL COMMENT '删除标志',
                                            `app_id` varchar(32) COMMENT '应用号',
                                            `cashier_type` varchar(32) COMMENT '网关收银台类型',
                                            `name` varchar(50) COMMENT '名称',
                                            `icon` varchar(100) COMMENT '图标',
                                            `sort_no` numeric(16,4) COMMENT '排序',
                                            `recommend` bool comment '是否推荐',
                                            `mch_no` varchar(32) COMMENT '商户号',
                                            `bg_color` varchar(32) COMMENT '背景色',
                                            `isv_no` varchar(32) COMMENT '所属服务商',
                                            `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '网关收银台类目配置';

CREATE TABLE `pay_cashier_item_config` (
                                           `id` int8 NOT NULL COMMENT '主键',
                                           `creator` int8 comment '创建者ID',
                                           `create_time` datetime(6) COMMENT '创建时间',
                                           `last_modifier` int8 comment '最后修者ID',
                                           `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                           `version` int4 NOT NULL COMMENT '乐观锁',
                                           `deleted` bool NOT NULL COMMENT '删除标志',
                                           `app_id` varchar(32) COMMENT '应用号',
                                           `group_id` int8 comment '分组配置ID',
                                           `channel` varchar(32) COMMENT '通道',
                                           `pay_method` varchar(32) COMMENT '支付方式',
                                           `name` varchar(50) COMMENT '名称',
                                           `icon` varchar(100) COMMENT '图标',
                                           `sort_no` numeric(16,4) COMMENT '排序',
                                           `call_type` varchar(32) COMMENT '调用方式',
                                           `recommend` bool comment '是否推荐',
                                           `bg_color` varchar(32) COMMENT '背景色',
                                           `border_color` varchar(32) COMMENT '边框色',
                                           `font_color` varchar(32) COMMENT '字体颜色',
                                           `other_method` varchar(32) COMMENT '其他支付方式',
                                           `mch_no` varchar(32) COMMENT '商户号',
                                           `pay_env_types` varchar(5000) COMMENT '生效的支付环境列表',
                                           `isv_no` varchar(32) COMMENT '所属服务商',
                                           `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '网关收银台配置项';

CREATE TABLE `pay_channel_config` (
                                      `id` int8 NOT NULL COMMENT '主键',
                                      `channel` varchar(32) COMMENT '支付通道',
                                      `out_mch_no` varchar(128) COMMENT '通道商户号',
                                      `out_app_id` varchar(256) COMMENT '通道APPID',
                                      `enable` bool comment '是否启用',
                                      `ext` longtext comment '扩展存储',
                                      `creator` int8 comment '创建者ID',
                                      `create_time` datetime(6) COMMENT '创建时间',
                                      `last_modifier` int8 comment '最后修改ID',
                                      `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                      `version` int4 NOT NULL COMMENT '版本号',
                                      `deleted` bool NOT NULL COMMENT '删除标志',
                                      `mch_no` varchar(32) NOT NULL COMMENT '商户号',
                                      `app_id` varchar(32) NOT NULL COMMENT '应用号',
                                      `isv_no` varchar(64) COMMENT '所属服务商',
                                      `sub` bool comment '是否属于特约商户',
                                      `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '支付通道配置';

CREATE TABLE `pay_channel_const` (
                                     `id` int8 NOT NULL COMMENT '主键',
                                     `code` varchar(32) NOT NULL COMMENT '通道编码',
                                     `name` varchar(32) NOT NULL COMMENT '通道名称',
                                     `enable` bool NOT NULL DEFAULT false comment '是否启用',
                                     `isv` bool NOT NULL DEFAULT false comment '是否支持服务商模式',
                                     `allocatable` bool NOT NULL DEFAULT false comment '是否支持分账',
                                     `terminal` bool NOT NULL DEFAULT false comment '终端报备',
                                     `apply` bool NOT NULL DEFAULT false comment '进件申请'
) COMMENT '支付通道常量';

CREATE TABLE `pay_channel_terminal` (
                                        `id` int8 NOT NULL COMMENT '主键',
                                        `creator` int8 comment '创建者ID',
                                        `create_time` datetime(6) COMMENT '创建时间',
                                        `last_modifier` int8 comment '最后修者ID',
                                        `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                        `version` int4 NOT NULL COMMENT '乐观锁',
                                        `deleted` bool NOT NULL COMMENT '删除标志',
                                        `mch_no` varchar(32) COMMENT '商户号',
                                        `app_id` varchar(32) COMMENT '应用号',
                                        `name` varchar(64) COMMENT '终端名称',
                                        `terminal_id` int8 comment '终端ID',
                                        `terminal_no` varchar(64) COMMENT '终端编码',
                                        `channel` varchar(32) COMMENT '通道',
                                        `status` varchar(32) COMMENT '状态',
                                        `out_terminal_no` varchar(64) COMMENT '通道终端号',
                                        `error_msg` varchar(500) COMMENT '错误信息',
                                        `extra` varchar(5000) COMMENT '扩展信息',
                                        `type` varchar(32) COMMENT '报送类型',
                                        `isv_no` varchar(32) COMMENT '所属服务商',
                                        `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '通道终端设备上报记录';

CREATE TABLE `pay_close_record` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `order_no` varchar(32) NOT NULL COMMENT '支付订单号',
                                    `biz_order_no` varchar(100) NOT NULL COMMENT '商户支付订单号',
                                    `channel` varchar(20) COMMENT '支付通道',
                                    `close_type` varchar(20) NOT NULL COMMENT '关闭类型',
                                    `closed` bool NOT NULL COMMENT '是否关闭成功',
                                    `error_code` varchar(10) COMMENT '错误码',
                                    `error_msg` varchar(500) COMMENT '错误信息',
                                    `client_ip` varchar(64) COMMENT '支付终端ip',
                                    `creator` int8 comment '创建者ID',
                                    `create_time` datetime(6) COMMENT '创建时间',
                                    `mch_no` varchar(32) COMMENT '商户号',
                                    `app_id` varchar(32) COMMENT '应用号',
                                    `isv_no` varchar(32) COMMENT '所属服务商',
                                    `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '支付关闭记录';

CREATE TABLE `pay_gateway_config` (
                                      `id` int8 NOT NULL COMMENT '主键',
                                      `creator` int8 comment '创建者ID',
                                      `create_time` datetime(6) COMMENT '创建时间',
                                      `last_modifier` int8 comment '最后修者ID',
                                      `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                      `version` int4 NOT NULL COMMENT '乐观锁',
                                      `deleted` bool NOT NULL COMMENT '删除标志',
                                      `app_id` varchar(32) COMMENT '应用号',
                                      `name` varchar(55) COMMENT '收银台名称',
                                      `aggregate_show` bool comment 'PC收银台是否同时显示聚合收银码',
                                      `h5_auto_upgrade` bool comment 'h5收银台自动升级聚合支付',
                                      `mch_no` varchar(32) COMMENT '商户号',
                                      `bar_pay_show` bool comment 'PC收银台是否显示聚合条码支付',
                                      `mini_app_allocation` bool comment '小程序开启分账',
                                      `mini_app_auto_allocation` bool comment '小程序自动分账',
                                      `mini_app_limit_pay` varchar(32) COMMENT '限制小程序支付方式',
                                      `mini_app_terminal_no` varchar(32) COMMENT '小程序终端号',
                                      `isv_no` varchar(32) COMMENT '所属服务商',
                                      `agent_no` varchar(32) COMMENT '所属代理商',
                                      `read_system` bool comment '读取系统配置'
) COMMENT '网关收银配置';

CREATE TABLE `pay_isv_aggregate_bar_pay_config` (
                                                    `id` int8 NOT NULL COMMENT '主键',
                                                    `creator` int8 comment '创建者ID',
                                                    `create_time` datetime(6) COMMENT '创建时间',
                                                    `last_modifier` int8 comment '最后修者ID',
                                                    `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                                    `version` int4 NOT NULL COMMENT '乐观锁',
                                                    `deleted` bool NOT NULL COMMENT '删除标志',
                                                    `bar_pay_type` varchar(32) COMMENT '付款码类型',
                                                    `channel` varchar(32) COMMENT '通道',
                                                    `pay_method` varchar(32) COMMENT '支付方式',
                                                    `other_method` varchar(32) COMMENT '其他支付方式',
                                                    `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '网关聚合付款码支付配置';

CREATE TABLE `pay_isv_aggregate_pay_config` (
                                                `id` int8 NOT NULL COMMENT '主键',
                                                `creator` int8 comment '创建者ID',
                                                `create_time` datetime(6) COMMENT '创建时间',
                                                `last_modifier` int8 comment '最后修者ID',
                                                `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                                `version` int4 NOT NULL COMMENT '乐观锁',
                                                `deleted` bool NOT NULL COMMENT '删除标志',
                                                `aggregate_type` varchar(32) COMMENT '聚合支付类型',
                                                `channel` varchar(32) COMMENT '通道',
                                                `pay_method` varchar(32) COMMENT '支付方式',
                                                `auto_launch` bool comment '自动拉起支付',
                                                `other_method` varchar(32) COMMENT '其他支付方式',
                                                `mch_no` varchar(32) COMMENT '商户号',
                                                `need_open_id` bool comment '需要获取OpenId',
                                                `call_type` varchar(32) COMMENT '调用方式',
                                                `open_id_get_type` varchar(32) COMMENT 'OpenId获取方式',
                                                `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '网关聚合支付配置';

CREATE TABLE `pay_isv_cashier_group_config` (
                                                `id` int8 NOT NULL COMMENT '主键',
                                                `creator` int8 comment '创建者ID',
                                                `create_time` datetime(6) COMMENT '创建时间',
                                                `last_modifier` int8 comment '最后修者ID',
                                                `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                                `version` int4 NOT NULL COMMENT '乐观锁',
                                                `deleted` bool NOT NULL COMMENT '删除标志',
                                                `cashier_type` varchar(32) COMMENT '网关收银台类型',
                                                `name` varchar(50) COMMENT '名称',
                                                `icon` varchar(100) COMMENT '图标',
                                                `sort_no` numeric(16,4) COMMENT '排序',
                                                `recommend` bool comment '是否推荐',
                                                `mch_no` varchar(32) COMMENT '商户号',
                                                `bg_color` varchar(32) COMMENT '背景色',
                                                `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '网关收银台类目配置';

CREATE TABLE `pay_isv_cashier_item_config` (
                                               `id` int8 NOT NULL COMMENT '主键',
                                               `creator` int8 comment '创建者ID',
                                               `create_time` datetime(6) COMMENT '创建时间',
                                               `last_modifier` int8 comment '最后修者ID',
                                               `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                               `version` int4 NOT NULL COMMENT '乐观锁',
                                               `deleted` bool NOT NULL COMMENT '删除标志',
                                               `group_id` int8 comment '分组配置ID',
                                               `channel` varchar(32) COMMENT '通道',
                                               `pay_method` varchar(32) COMMENT '支付方式',
                                               `name` varchar(50) COMMENT '名称',
                                               `icon` varchar(100) COMMENT '图标',
                                               `sort_no` numeric(16,4) COMMENT '排序',
                                               `call_type` varchar(32) COMMENT '调用方式',
                                               `recommend` bool comment '是否推荐',
                                               `bg_color` varchar(32) COMMENT '背景色',
                                               `border_color` varchar(32) COMMENT '边框色',
                                               `font_color` varchar(32) COMMENT '字体颜色',
                                               `other_method` varchar(32) COMMENT '其他支付方式',
                                               `mch_no` varchar(32) COMMENT '商户号',
                                               `pay_env_types` varchar(5000) COMMENT '生效的支付环境列表',
                                               `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '网关收银台配置项';

CREATE TABLE `pay_isv_channel_config` (
                                          `id` int8 NOT NULL COMMENT '主键',
                                          `channel` varchar(32) COMMENT '支付通道',
                                          `out_isv_no` varchar(32) COMMENT '通道服务商号',
                                          `enable` bool comment '是否启用',
                                          `ext` longtext comment '扩展存储',
                                          `creator` int8 comment '创建者ID',
                                          `create_time` datetime(6) COMMENT '创建时间',
                                          `last_modifier` int8 comment '最后修改ID',
                                          `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                          `version` int4 NOT NULL COMMENT '版本号',
                                          `deleted` bool NOT NULL COMMENT '删除标志',
                                          `isv_no` varchar(32) NOT NULL COMMENT '服务商号'
) COMMENT '通道支付配置';

CREATE TABLE `pay_isv_gateway_config` (
                                          `id` int8 NOT NULL COMMENT '主键',
                                          `creator` int8 comment '创建者ID',
                                          `create_time` datetime(6) COMMENT '创建时间',
                                          `last_modifier` int8 comment '最后修者ID',
                                          `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                          `version` int4 NOT NULL COMMENT '乐观锁',
                                          `deleted` bool NOT NULL COMMENT '删除标志',
                                          `app_id` varchar(32) COMMENT '应用号',
                                          `name` varchar(55) COMMENT '收银台名称',
                                          `aggregate_show` bool comment 'PC收银台是否同时显示聚合收银码',
                                          `h5_auto_upgrade` bool comment 'h5收银台自动升级聚合支付',
                                          `mch_no` varchar(32) COMMENT '商户号',
                                          `bar_pay_show` bool comment 'PC收银台是否显示聚合条码支付',
                                          `mini_app_allocation` bool comment '小程序开启分账',
                                          `mini_app_auto_allocation` bool comment '小程序自动分账',
                                          `mini_app_limit_pay` varchar(32) COMMENT '限制小程序支付方式',
                                          `mini_app_terminal_no` varchar(32) COMMENT '小程序终端号',
                                          `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '服务商网关收银配置';

CREATE TABLE `pay_mch_app` (
                               `id` int8 NOT NULL COMMENT '主键',
                               `mch_no` varchar(32) COMMENT '商户号',
                               `app_id` varchar(32) COMMENT '应用号',
                               `app_name` varchar(64) COMMENT '应用名称',
                               `sign_type` varchar(32) COMMENT '签名方式',
                               `sign_secret` varchar(500) COMMENT '签名秘钥',
                               `req_sign` bool comment '是否对请求进行验签',
                               `limit_amount` numeric(16,4) COMMENT '支付限额(元)',
                               `status` varchar(32) COMMENT '应用状态',
                               `notify_type` varchar(32) COMMENT '异步消息通知类型',
                               `notify_url` varchar(200) COMMENT '通知地址',
                               `creator` int8 comment '创建者ID',
                               `create_time` datetime(6) COMMENT '创建时间',
                               `last_modifier` int8 comment '最后修改ID',
                               `last_modified_time` datetime(6) COMMENT '最后修改时间',
                               `version` int4 NOT NULL COMMENT '版本号',
                               `deleted` bool NOT NULL COMMENT '删除标志',
                               `order_timeout` int2 comment '订单默认超时时间(分钟)',
                               `merchant_type` varchar(32) COMMENT '商户类型',
                               `req_timeout` bool comment '是否验证请求时间是否超时',
                               `req_timeout_second` int4 comment '请求超时时间(秒)',
                               `isv_no` varchar(32) COMMENT '服务商号',
                               `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '商户应用';

CREATE TABLE `pay_merchant` (
                                `id` int8 NOT NULL COMMENT '主键',
                                `mch_no` varchar(64) NOT NULL COMMENT '商户号',
                                `mch_name` varchar(100) NOT NULL COMMENT '商户名称',
                                `company_name` varchar(100) COMMENT '公司名称',
                                `id_type` varchar(32) COMMENT '法人证件类型',
                                `id_no` varchar(1) comment '法人证件号',
                                `contact` varchar(100) COMMENT '法人联系方式',
                                `legal_person` varchar(100) COMMENT '法人名称',
                                `status` varchar(10) COMMENT '状态',
                                `creator` int8 comment '创建者ID',
                                `create_time` datetime(0) COMMENT '创建时间',
                                `last_modifier` int8 comment '最后修改ID',
                                `last_modified_time` datetime(0) COMMENT '最后修改时间',
                                `version` int4 NOT NULL COMMENT '版本号',
                                `deleted` bool NOT NULL COMMENT '删除标志',
                                `company_contact` varchar(150) COMMENT '公司联系方式',
                                `company_address` varchar(150) COMMENT '公司地址',
                                `company_code` varchar(50) COMMENT '公司信用编码',
                                `administrator` bool NOT NULL COMMENT '是否有关联管理员',
                                `admin_user_id` int8 comment '关联管理员用户',
                                `isv_no` varchar(32) COMMENT '所属服务商',
                                `merchant_type` varchar(32) COMMENT '商户类型',
                                `agent_no` varchar(32) COMMENT '所属代理商',
                                `mch_short_name` varchar(32) COMMENT '商户简称'
) COMMENT '商户';

CREATE TABLE `pay_merchant_callback_record` (
                                                `id` int8 NOT NULL COMMENT '主键',
                                                `creator` int8 comment '创建者ID',
                                                `create_time` datetime(6) COMMENT '创建时间',
                                                `mch_no` varchar(32) COMMENT '商户号',
                                                `app_id` varchar(32) COMMENT '应用号',
                                                `task_id` int8 comment '任务ID',
                                                `req_count` int4 comment '请求次数',
                                                `success` bool comment '发送是否成功',
                                                `send_type` varchar(30) COMMENT '发送类型, 自动发送, 手动发送',
                                                `error_code` varchar(50) COMMENT '错误码',
                                                `error_msg` varchar(500) COMMENT '错误信息',
                                                `isv_no` varchar(32) COMMENT '所属服务商',
                                                `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '客户回调消息发送记录';

CREATE TABLE `pay_merchant_callback_task` (
                                              `id` int8 NOT NULL COMMENT '主键',
                                              `creator` int8 comment '创建者ID',
                                              `create_time` datetime(6) COMMENT '创建时间',
                                              `last_modifier` int8 comment '最后修改ID',
                                              `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                              `version` int4 NOT NULL COMMENT '版本号',
                                              `deleted` bool NOT NULL COMMENT '删除标志',
                                              `trade_id` int8 comment '本地交易ID',
                                              `trade_no` varchar(32) COMMENT '本地交易号',
                                              `trade_type` varchar(20) COMMENT '通知类型',
                                              `content` longtext comment '消息内容',
                                              `success` bool comment '是否发送成功',
                                              `next_time` datetime(6) COMMENT '发送次数',
                                              `send_count` int4 comment '延迟次数',
                                              `delay_count` int4 comment '下次发送时间',
                                              `latest_time` datetime(6) COMMENT '最后发送时间',
                                              `mch_no` varchar(32) COMMENT '商户号',
                                              `app_id` varchar(32) COMMENT '应用号',
                                              `url` varchar(200) COMMENT '发送地址',
                                              `isv_no` varchar(64) COMMENT '所属服务商',
                                              `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '客户回调通知消息任务';

CREATE TABLE `pay_merchant_notify_config` (
                                              `id` int8 NOT NULL COMMENT '主键',
                                              `code` varchar(50) COMMENT '通知类型',
                                              `subscribe` bool comment '是否订阅',
                                              `creator` int8 comment '创建者ID',
                                              `create_time` datetime(6) COMMENT '创建时间',
                                              `last_modifier` int8 comment '最后修改ID',
                                              `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                              `version` int4 NOT NULL COMMENT '版本号',
                                              `deleted` bool NOT NULL COMMENT '删除标志',
                                              `mch_no` varchar(32) COMMENT '商户号',
                                              `app_id` varchar(32) COMMENT '应用号',
                                              `isv_no` varchar(64) COMMENT '所属服务商',
                                              `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '商户应用消息通知配置';

CREATE TABLE `pay_merchant_notify_const` (
                                             `id` int8 NOT NULL COMMENT '主键',
                                             `code` varchar(60) COMMENT '通道编码',
                                             `name` varchar(80) COMMENT '通道名称',
                                             `description` varchar(300) COMMENT '描述',
                                             `enable` bool comment '是否启用'
) COMMENT '商户订阅通知类型';

CREATE TABLE `pay_merchant_notify_record` (
                                              `id` int8 NOT NULL COMMENT '主键',
                                              `creator` int8 comment '创建者ID',
                                              `create_time` datetime(6) COMMENT '创建时间',
                                              `mch_no` varchar(32) COMMENT '商户号',
                                              `app_id` varchar(32) COMMENT '应用号',
                                              `task_id` int8 comment '任务ID',
                                              `req_count` int4 comment '请求次数',
                                              `success` bool comment '发送是否成功',
                                              `send_type` varchar(30) COMMENT '发送类型, 自动发送, 手动发送',
                                              `error_code` varchar(50) COMMENT '错误码',
                                              `error_msg` varchar(500) COMMENT '错误信息',
                                              `isv_no` varchar(32) COMMENT '所属服务商',
                                              `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '客户订阅通知发送记录';

CREATE TABLE `pay_merchant_notify_task` (
                                            `id` int8 NOT NULL COMMENT '主键',
                                            `creator` int8 comment '创建者ID',
                                            `create_time` datetime(6) COMMENT '创建时间',
                                            `last_modifier` int8 comment '最后修改ID',
                                            `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                            `version` int4 NOT NULL COMMENT '版本号',
                                            `deleted` bool NOT NULL COMMENT '删除标志',
                                            `trade_id` int8 comment '本地交易ID',
                                            `trade_no` varchar(32) COMMENT '本地交易号',
                                            `notify_type` varchar(20) COMMENT '通知类型',
                                            `content` longtext comment '消息内容',
                                            `success` bool comment '是否发送成功',
                                            `next_time` datetime(6) COMMENT '发送次数',
                                            `send_count` int4 comment '延迟次数',
                                            `delay_count` int4 comment '下次发送时间',
                                            `latest_time` datetime(6) COMMENT '最后发送时间',
                                            `mch_no` varchar(32) COMMENT '商户号',
                                            `app_id` varchar(32) COMMENT '应用号',
                                            `isv_no` varchar(64) COMMENT '所属服务商',
                                            `agent_no` varchar(32) COMMENT '所属代理商'
) COMMENT '客户订阅通知消息任务';

CREATE TABLE `pay_merchant_rate_config` (
                                            `id` int8 NOT NULL COMMENT '主键',
                                            `channel` varchar(32) COMMENT '支付通道',
                                            `enable` bool comment '是否启用',
                                            `ext` longtext comment '扩展存储',
                                            `creator` int8 comment '创建者ID',
                                            `create_time` datetime(6) COMMENT '创建时间',
                                            `last_modifier` int8 comment '最后修改ID',
                                            `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                            `version` int4 NOT NULL COMMENT '版本号',
                                            `deleted` bool NOT NULL COMMENT '删除标志',
                                            `mch_no` varchar(32) NOT NULL COMMENT '商户商号',
                                            `agent_no` varchar(32) COMMENT '代理商号',
                                            `isv_no` varchar(32) COMMENT '服务商号'
) COMMENT '商户费率配置';

CREATE TABLE `pay_merchant_user` (
                                     `id` int8 NOT NULL COMMENT '主键',
                                     `user_id` int8 NOT NULL COMMENT '用户ID',
                                     `mch_no` varchar(32) NOT NULL COMMENT '商户号',
                                     `creator` int8 comment '创建者ID',
                                     `create_time` datetime(6) COMMENT '创建时间',
                                     `administrator` bool NOT NULL COMMENT '是否为商户管理员'
) COMMENT '用户商户关联关系';

CREATE TABLE `pay_method_const` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `code` varchar(60) COMMENT '通道编码',
                                    `name` varchar(80) COMMENT '通道名称',
                                    `enable` bool comment '是否启用',
                                    `remark` varchar(300) COMMENT '备注'
) COMMENT '支付方式常量';

CREATE TABLE `pay_order` (
                             `id` int8 NOT NULL COMMENT '主键',
                             `creator` int8 comment '创建者ID',
                             `create_time` datetime(6) COMMENT '创建时间',
                             `last_modifier` int8 comment '最后修改ID',
                             `last_modified_time` datetime(6) COMMENT '最后修改时间',
                             `version` int4 NOT NULL COMMENT '版本号',
                             `deleted` bool NOT NULL COMMENT '删除标志',
                             `biz_order_no` varchar(100) COMMENT '商户订单号',
                             `order_no` varchar(100) COMMENT '订单号',
                             `out_order_no` varchar(150) COMMENT '通道订单号',
                             `title` varchar(100) COMMENT '标题',
                             `description` varchar(500) COMMENT '描述',
                             `allocation` bool comment '是否支持分账',
                             `auto_allocation` bool comment '自动分账',
                             `channel` varchar(20) COMMENT '支付通道',
                             `method` varchar(20) COMMENT '支付方式',
                             `amount` numeric(16,4) COMMENT '金额(元)',
                             `refundable_balance` numeric(16,4) COMMENT '可退金额(元)',
                             `status` varchar(32) COMMENT '支付状态',
                             `refund_status` varchar(32) COMMENT '退款状态',
                             `alloc_status` varchar(32) COMMENT '分账状态',
                             `return_url` varchar(200) COMMENT '同步跳转地址',
                             `notify_url` varchar(200) COMMENT '异步通知地址',
                             `extra_param` varchar(2048) COMMENT '通道附加参数',
                             `attach` varchar(500) COMMENT '商户扩展参数',
                             `req_time` datetime(6) COMMENT '请求时间',
                             `client_ip` varchar(64) COMMENT '支付终端ip',
                             `error_code` varchar(50) COMMENT '错误码',
                             `error_msg` varchar(500) COMMENT '错误信息',
                             `mch_no` varchar(32) COMMENT '商户号',
                             `app_id` varchar(32) COMMENT '应用号',
                             `expired_time` datetime(6) COMMENT '过期时间',
                             `pay_time` datetime(6) COMMENT '支付成功时间',
                             `close_time` datetime(6) COMMENT '关闭时间',
                             `isv_no` varchar(32) COMMENT '所属服务商',
                             `other_method` varchar(128) COMMENT '其他支付方式',
                             `auth_code` varchar(128) COMMENT '付款码',
                             `limit_pay` varchar(128) COMMENT '限制支付类型',
                             `terminal_no` varchar(128) COMMENT '终端设备编码',
                             `agent_no` varchar(32) COMMENT '代理商号',
                             `real_amount` numeric(16,4) COMMENT '实收金额',
                             `settle_status` varchar(32) COMMENT '结算状态'
) COMMENT '支付订单';

CREATE TABLE `pay_order_expand` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `creator` int8 comment '创建者ID',
                                    `create_time` datetime(6) COMMENT '创建时间',
                                    `last_modifier` int8 comment '最后修改ID',
                                    `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                    `version` int4 NOT NULL COMMENT '版本号',
                                    `deleted` bool NOT NULL COMMENT '删除标志',
                                    `mch_no` varchar(32) COMMENT '商户号',
                                    `app_id` varchar(32) COMMENT '应用号',
                                    `isv_no` varchar(32) COMMENT '所属服务商',
                                    `agent_no` varchar(32) COMMENT '代理商号',
                                    `buyer_id` varchar(64) COMMENT '付款用户ID',
                                    `user_id` varchar(64) COMMENT '用户标识',
                                    `trade_product` varchar(64) COMMENT '支付产品',
                                    `trade_way` varchar(64) COMMENT '交易方式',
                                    `bank_type` varchar(64) COMMENT '银行卡类型',
                                    `trans_order_no` varchar(64) COMMENT '透传订单号',
                                    `promotion_type` varchar(64) COMMENT '参加活动类型',
                                    `ext` varchar(5000) COMMENT '扩展参数存储字段'
) COMMENT '支付订单扩展存储参数';

CREATE TABLE `pay_platform_basic_config` (
                                             `id` int8 NOT NULL COMMENT '主键',
                                             `creator` int8 comment '创建者ID',
                                             `create_time` datetime(6) COMMENT '创建时间',
                                             `last_modifier` int8 comment '最后修改ID',
                                             `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                             `version` int4 NOT NULL COMMENT '版本号',
                                             `deleted` bool NOT NULL COMMENT '删除标志',
                                             `single_limit_amount` numeric(16,4) COMMENT '全局单笔限额',
                                             `monthly_limit_amount` numeric(16,4) COMMENT '每月累计限额',
                                             `daily_limit_amount` numeric(16,4) COMMENT '每日限额'
) COMMENT '管理平台基础配置';

CREATE TABLE `pay_platform_cashouts_config` (
                                                `id` int8 NOT NULL COMMENT '主键',
                                                `creator` int8 comment '创建者ID',
                                                `create_time` datetime(0) COMMENT '创建时间',
                                                `last_modifier` int8 comment '最后修改ID',
                                                `last_modified_time` datetime(0) COMMENT '最后修改时间',
                                                `version` int4 NOT NULL COMMENT '版本号',
                                                `deleted` bool NOT NULL COMMENT '删除标志',
                                                `start_amount` numeric(16,4) COMMENT '起始提现额度',
                                                `fee_formula` varchar(32) COMMENT '手续费计算公式',
                                                `fixed_fee` numeric(16,4) COMMENT '单笔固定',
                                                `fixed_rate` numeric(16,4) COMMENT '单笔费率',
                                                `fixed_fee_combined` numeric(16,4) COMMENT '组合 固定手续费',
                                                `fixed_rate_combined` numeric(16,4) COMMENT '组合 费率',
                                                `freeze_amount` numeric(16,4) COMMENT '冻结金额'
) COMMENT '平台代理商默认提现配置';

CREATE TABLE `pay_platform_url_config` (
                                           `id` int8 NOT NULL COMMENT '主键',
                                           `creator` int8 comment '创建者ID',
                                           `create_time` datetime(6) COMMENT '创建时间',
                                           `last_modifier` int8 comment '最后修改ID',
                                           `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                           `version` int4 NOT NULL COMMENT '版本号',
                                           `deleted` bool NOT NULL COMMENT '删除标志',
                                           `admin_web_url` varchar(200) COMMENT '运营端网址',
                                           `agent_web_url` varchar(200) COMMENT '代理商端网址',
                                           `mch_web_url` varchar(200) COMMENT '商户端网址',
                                           `gateway_service_url` varchar(200) COMMENT '支付网关地址',
                                           `gateway_h5_url` varchar(200) COMMENT '网关h5地址'
) COMMENT '系统地址配置';

CREATE TABLE `pay_platform_website_config` (
                                               `id` int8 NOT NULL COMMENT '主键',
                                               `creator` int8 comment '创建者ID',
                                               `create_time` datetime(6) COMMENT '创建时间',
                                               `last_modifier` int8 comment '最后修改ID',
                                               `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                               `version` int4 NOT NULL COMMENT '版本号',
                                               `deleted` bool NOT NULL COMMENT '删除标志',
                                               `system_name` varchar(50),
                                               `company_name` varchar(100),
                                               `company_phone` varchar(32),
                                               `company_email` varchar(64),
                                               `whole_logo` varchar(200),
                                               `simple_logo` varchar(200),
                                               `icp_info` varchar(64),
                                               `icp_link` varchar(200),
                                               `mps_info` varchar(64),
                                               `mps_link` varchar(200),
                                               `pcac_info` varchar(64),
                                               `pcac_link` varchar(200),
                                               `icp_plus_info` varchar(64),
                                               `icp_plus_link` varchar(200),
                                               `copyright` varchar(64),
                                               `copyright_link` varchar(200)
) COMMENT '站点显示内容配置';

CREATE TABLE `pay_refund_order` (
                                    `id` int8 NOT NULL COMMENT '主键',
                                    `order_id` int8 NOT NULL COMMENT '支付订单ID',
                                    `order_no` varchar(100) NOT NULL COMMENT '支付订单号',
                                    `biz_order_no` varchar(100) NOT NULL COMMENT '商户支付订单号',
                                    `out_order_no` varchar(150) NOT NULL COMMENT '通道支付订单号',
                                    `title` varchar(100) NOT NULL COMMENT '支付标题',
                                    `refund_no` varchar(150) NOT NULL COMMENT '退款号',
                                    `biz_refund_no` varchar(100) NOT NULL COMMENT '商户退款号',
                                    `out_refund_no` varchar(150) COMMENT '通道退款交易号',
                                    `channel` varchar(20) NOT NULL COMMENT '支付通道',
                                    `order_amount` numeric(16,4) NOT NULL COMMENT '订单金额',
                                    `amount` numeric(16,4) NOT NULL COMMENT '退款金额',
                                    `reason` varchar(150) COMMENT '退款原因',
                                    `finish_time` datetime(6) COMMENT '退款完成时间',
                                    `status` varchar(20) NOT NULL COMMENT '退款状态',
                                    `notify_url` varchar(200) COMMENT '异步通知地址',
                                    `attach` varchar(500) COMMENT '商户扩展参数',
                                    `extra_param` varchar(2048) COMMENT '附加参数',
                                    `req_time` datetime(6) COMMENT '请求时间，传输时间戳',
                                    `client_ip` varchar(64) COMMENT '支付终端ip',
                                    `error_code` varchar(10) COMMENT '错误码',
                                    `error_msg` varchar(500) COMMENT '错误信息',
                                    `creator` int8 comment '创建者ID',
                                    `create_time` datetime(6) COMMENT '创建时间',
                                    `last_modifier` int8 comment '最后修者ID',
                                    `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                    `version` int4 NOT NULL COMMENT '乐观锁',
                                    `deleted` bool NOT NULL COMMENT '删除标志',
                                    `mch_no` varchar(32) COMMENT '商户号',
                                    `app_id` varchar(32) COMMENT '应用号',
                                    `isv_no` varchar(32) COMMENT '所属服务商',
                                    `agent_no` varchar(32) COMMENT '代理商号',
                                    `settle_status` varchar(32) COMMENT '结算状态'
) COMMENT '退款订单';

CREATE TABLE `pay_terminal_const` (
                                      `id` int8 NOT NULL COMMENT '主键',
                                      `channel` varchar(32) NOT NULL COMMENT '通道编码',
                                      `type` varchar(32) NOT NULL COMMENT '终端报送类型',
                                      `name` varchar(32) NOT NULL COMMENT '终端报送名称',
                                      `enable` bool NOT NULL COMMENT '是否启用',
                                      `remark` varchar(200) COMMENT '备注'
) COMMENT '通道终端报送类型';

CREATE TABLE `pay_terminal_device` (
                                       `id` int8 NOT NULL COMMENT '主键',
                                       `creator` int8 comment '创建者ID',
                                       `create_time` datetime(6) COMMENT '创建时间',
                                       `last_modifier` int8 comment '最后修者ID',
                                       `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                       `version` int4 NOT NULL COMMENT '乐观锁',
                                       `deleted` bool NOT NULL COMMENT '删除标志',
                                       `mch_no` varchar(32) COMMENT '商户号',
                                       `app_id` varchar(32) COMMENT '应用号',
                                       `name` varchar(64) COMMENT '终端名称',
                                       `terminal_no` varchar(64) COMMENT '终端编码',
                                       `type` varchar(32) COMMENT '终端类型',
                                       `serial_num` varchar(64) COMMENT '终端序列号',
                                       `area_code` varchar(64) COMMENT '省市区编码',
                                       `address` varchar(256) COMMENT '终端发放地址',
                                       `company_name` varchar(128) COMMENT '终端厂商名称',
                                       `put_date` datetime(6) COMMENT '发放日期',
                                       `gps` bool comment '支持终端定位',
                                       `machine_type` varchar(64) COMMENT '终端机具型号',
                                       `longitude` varchar(32) COMMENT '经度，浮点型, 小数点后最多保留6位',
                                       `latitude` varchar(32) COMMENT '纬度，浮点型,小数点后最多保留6位',
                                       `ip` varchar(128) COMMENT '设备 IP 地址',
                                       `network_license` varchar(128) COMMENT '银行卡受理终端产品入网认证编号',
                                       `agent_no` varchar(32) COMMENT '所属代理商',
                                       `isv_no` varchar(32) COMMENT '所属服务商'
) COMMENT '支付终端设备管理';

CREATE TABLE `pay_trade_callback_record` (
                                             `id` int8 NOT NULL COMMENT '主键',
                                             `trade_no` varchar(100) COMMENT '本地交易号',
                                             `out_trade_no` varchar(150) COMMENT '通道交易号',
                                             `channel` varchar(20) NOT NULL COMMENT '支付通道',
                                             `callback_type` varchar(20) NOT NULL COMMENT '回调类型',
                                             `notify_info` longtext NOT NULL COMMENT '通知消息',
                                             `status` varchar(20) NOT NULL COMMENT '回调处理状态',
                                             `error_code` varchar(10) COMMENT '错误码',
                                             `error_msg` varchar(500) COMMENT '错误信息',
                                             `creator` int8 comment '创建者ID',
                                             `create_time` datetime(6) COMMENT '创建时间',
                                             `mch_no` varchar(32) COMMENT '商户号',
                                             `app_id` varchar(32) COMMENT '应用号',
                                             `isv_no` varchar(32) COMMENT '所属服务商',
                                             `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '网关回调通知';

CREATE TABLE `pay_trade_flow_record` (
                                         `id` int8 NOT NULL COMMENT '主键',
                                         `title` varchar(100) NOT NULL COMMENT '标题',
                                         `amount` numeric(16,4) NOT NULL COMMENT '金额',
                                         `type` varchar(20) NOT NULL COMMENT '业务类型',
                                         `channel` varchar(20) NOT NULL COMMENT '支付通道',
                                         `trade_no` varchar(100) NOT NULL COMMENT '本地交易号',
                                         `biz_trade_no` varchar(100) NOT NULL COMMENT '商户交易号',
                                         `out_trade_no` varchar(150) COMMENT '通道交易号',
                                         `creator` int8 comment '创建者ID',
                                         `create_time` datetime(6) COMMENT '创建时间',
                                         `mch_no` varchar(32) COMMENT '商户号',
                                         `app_id` varchar(32) COMMENT '应用号',
                                         `isv_no` varchar(32) COMMENT '所属服务商',
                                         `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '资金流水记录';

CREATE TABLE `pay_trade_sync_record` (
                                         `id` int8 NOT NULL COMMENT '主键',
                                         `creator` int8 comment '创建者ID',
                                         `create_time` datetime(6) COMMENT '创建时间',
                                         `mch_no` varchar(32) COMMENT '商户号',
                                         `app_id` varchar(32) COMMENT '应用号',
                                         `trade_no` varchar(100) COMMENT '本地交易号',
                                         `biz_trade_no` varchar(100) COMMENT '商户交易号',
                                         `out_trade_no` varchar(150) COMMENT '通道交易号',
                                         `out_trade_status` varchar(32) COMMENT '通道返回的状态',
                                         `trade_type` varchar(32) COMMENT '同步类型',
                                         `channel` varchar(32) COMMENT '同步通道',
                                         `sync_info` longtext comment '网关返回的同步消息',
                                         `adjust` bool NOT NULL COMMENT '是否进行调整',
                                         `error_code` varchar(50) COMMENT '错误码',
                                         `error_msg` varchar(500) COMMENT '错误信息',
                                         `client_ip` varchar(64) COMMENT '终端ip',
                                         `isv_no` varchar(32) COMMENT '所属服务商',
                                         `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '交易同步记录';

CREATE TABLE `pay_transfer_order` (
                                      `id` int8 NOT NULL COMMENT '主键',
                                      `biz_transfer_no` varchar(100) NOT NULL COMMENT '商户转账号',
                                      `transfer_no` varchar(100) NOT NULL COMMENT '转账号',
                                      `out_transfer_no` varchar(150) COMMENT '通道转账号',
                                      `channel` varchar(20) NOT NULL COMMENT '支付通道',
                                      `amount` numeric(16,4) NOT NULL COMMENT '转账金额',
                                      `title` varchar(100) COMMENT '标题',
                                      `reason` varchar(150) COMMENT '转账原因/备注',
                                      `payee_type` varchar(20) COMMENT '收款人类型',
                                      `payee_account` varchar(100) COMMENT '收款人账号',
                                      `payee_name` varchar(50) COMMENT '收款人姓名',
                                      `status` varchar(20) NOT NULL COMMENT '状态',
                                      `finish_time` datetime(6) COMMENT '成功时间',
                                      `notify_url` varchar(200) COMMENT '异步通知地址',
                                      `attach` varchar(500) COMMENT '商户扩展参数',
                                      `req_time` datetime(6) COMMENT '请求时间，传输时间戳',
                                      `client_ip` varchar(64) COMMENT '支付终端ip',
                                      `error_code` varchar(10) COMMENT '错误码',
                                      `error_msg` varchar(500) COMMENT '错误信息',
                                      `creator` int8 comment '创建者ID',
                                      `create_time` datetime(6) COMMENT '创建时间',
                                      `last_modifier` int8 comment '最后修者ID',
                                      `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                      `version` int4 NOT NULL COMMENT '乐观锁',
                                      `deleted` bool NOT NULL COMMENT '删除标志',
                                      `mch_no` varchar(32) COMMENT '商户号',
                                      `app_id` varchar(32) COMMENT '应用号',
                                      `extra_param` varchar(2048) COMMENT '通道附加参数',
                                      `isv_no` varchar(32) COMMENT '所属服务商',
                                      `agent_no` varchar(32) COMMENT '代理商号'
) COMMENT '转账订单';

CREATE TABLE `starter_audit_login_log` (
                                           `id` int8 NOT NULL COMMENT '主键',
                                           `user_id` int8 comment '用户账号ID',
                                           `account` varchar(100) COMMENT '用户名称',
                                           `login` bool comment '登录成功状态',
                                           `client` varchar(20) COMMENT '登录终端',
                                           `login_type` varchar(20) COMMENT '登录方式',
                                           `ip` varchar(80) COMMENT '登录IP地址',
                                           `login_location` varchar(100) COMMENT '登录地点',
                                           `browser` varchar(200) COMMENT '浏览器类型',
                                           `os` varchar(100) COMMENT '操作系统',
                                           `msg` longtext comment '提示消息',
                                           `login_time` datetime(6) COMMENT '访问时间'
) COMMENT '登录日志';

CREATE TABLE `starter_audit_operate_log` (
                                             `id` int8 NOT NULL COMMENT '主键',
                                             `title` varchar(100) NOT NULL COMMENT '操作模块',
                                             `operate_id` int8 comment '操作人员id',
                                             `account` varchar(100) COMMENT '操作人员账号',
                                             `business_type` varchar(50) NOT NULL COMMENT '业务类型',
                                             `method` varchar(100) NOT NULL COMMENT '请求方法',
                                             `request_method` varchar(20) NOT NULL COMMENT '请求方式',
                                             `operate_url` varchar(200) NOT NULL COMMENT '请求url',
                                             `operate_ip` varchar(80) COMMENT '操作ip',
                                             `operate_location` varchar(50) COMMENT '操作地点',
                                             `operate_param` longtext comment '请求参数',
                                             `operate_return` longtext comment '返回参数',
                                             `success` bool comment '操作状态',
                                             `error_msg` longtext comment '错误消息',
                                             `operate_time` datetime(6) NOT NULL COMMENT '操作时间',
                                             `browser` varchar(200) COMMENT '浏览器类型',
                                             `os` varchar(100) COMMENT '操作系统',
                                             `client` varchar(20) COMMENT '终端'
) COMMENT '操作日志';

CREATE TABLE `starter_file_platform` (
                                         `id` int8 NOT NULL COMMENT '文件id',
                                         `type` varchar(50) COMMENT '平台类型',
                                         `name` varchar(200) COMMENT '名称',
                                         `url` varchar(200) COMMENT '访问地址',
                                         `default_platform` bool comment '默认存储平台',
                                         `creator` int8 comment '创建者ID',
                                         `create_time` datetime(6) COMMENT '创建时间',
                                         `last_modifier` int8 comment '最后修改ID',
                                         `last_modified_time` datetime(6) COMMENT '最后修改时间',
                                         `version` int4 NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `frontend_upload` bool comment '前端直传'
) COMMENT '文件存储平台';

CREATE TABLE `starter_file_upload_info` (
                                            `id` int8 NOT NULL COMMENT '文件id',
                                            `url` varchar(512) NOT NULL COMMENT '文件访问地址',
                                            `size` int8 comment '文件大小，单位字节',
                                            `filename` varchar(256) COMMENT '文件名称',
                                            `original_filename` varchar(256) COMMENT '原始文件名',
                                            `base_path` varchar(256) COMMENT '基础存储路径',
                                            `path` varchar(256) COMMENT '存储路径',
                                            `ext` varchar(32) COMMENT '文件扩展名',
                                            `content_type` varchar(128) COMMENT 'MIME类型',
                                            `platform` varchar(32) COMMENT '存储平台',
                                            `th_url` varchar(512) COMMENT '缩略图访问路径',
                                            `th_filename` varchar(256) COMMENT '缩略图名称',
                                            `th_size` int8 comment '缩略图大小，单位字节',
                                            `th_content_type` varchar(128) COMMENT '缩略图MIME类型',
                                            `object_id` varchar(32) COMMENT '文件所属对象id',
                                            `object_type` varchar(32) COMMENT '文件所属对象类型，例如用户头像，评价图片',
                                            `metadata` longtext comment '文件元数据',
                                            `user_metadata` longtext comment '文件用户元数据',
                                            `th_metadata` longtext comment '缩略图元数据',
                                            `th_user_metadata` longtext comment '缩略图用户元数据',
                                            `attr` longtext comment '附加属性',
                                            `file_acl` varchar(32) COMMENT '文件ACL',
                                            `th_file_acl` varchar(32) COMMENT '缩略图文件ACL',
                                            `create_time` datetime(6) COMMENT '创建时间'
);
