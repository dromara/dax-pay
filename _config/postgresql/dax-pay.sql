create table base_area
(
    code      char(6)     not null
        primary key,
    name      varchar(60) not null,
    city_code char(4)     not null
);

comment on table base_area is '县区表';

comment on column base_area.name is '区域名称';

comment on column base_area.city_code is '城市编码';



create index inx_city_code
    on base_area (city_code);

comment on index inx_city_code is '城市';


-----
create table base_china_word
(
    id                 bigint  not null
        primary key,
    word               varchar(255),
    type               varchar(255),
    description        varchar(255),
    enable             integer,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    white              boolean
);

comment on table base_china_word is '敏感词';

comment on column base_china_word.id is '主键';

comment on column base_china_word.word is '敏感词';

comment on column base_china_word.type is '分类';

comment on column base_china_word.description is '描述';

comment on column base_china_word.enable is '是否启用';

comment on column base_china_word.creator is '创建者ID';

comment on column base_china_word.create_time is '创建时间';

comment on column base_china_word.last_modifier is '最后修者ID';

comment on column base_china_word.last_modified_time is '最后修改时间';

comment on column base_china_word.version is '乐观锁';




-----
create table base_city
(
    code          char(4)     not null
        primary key,
    name          varchar(60) not null,
    province_code char(2)     not null
);

comment on table base_city is '城市表';

comment on column base_city.code is '城市编码';

comment on column base_city.name is '城市名称';

comment on column base_city.province_code is '省份编码';




-----
create table base_data_result_sql
(
    id                 bigint  not null
        primary key,
    database_id        bigint,
    name               varchar(255),
    is_list            boolean,
    sql                varchar(255),
    params             text,
    fields             text,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table base_data_result_sql is '数据集SQL语句';

comment on column base_data_result_sql.id is '主键';

comment on column base_data_result_sql.creator is '创建者ID';

comment on column base_data_result_sql.create_time is '创建时间';

comment on column base_data_result_sql.last_modifier is '最后修者ID';

comment on column base_data_result_sql.last_modified_time is '最后修改时间';

comment on column base_data_result_sql.version is '乐观锁';

comment on column base_data_result_sql.deleted is '删除标志';




-----
create table base_dict
(
    id                 bigint       not null
        primary key,
    code               varchar(255) not null,
    name               varchar(100) not null,
    enable             boolean      not null,
    group_tag          varchar(100),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean,
    version            integer
);

comment on table base_dict is '字典';

comment on column base_dict.code is '编码';

comment on column base_dict.name is '名称';

comment on column base_dict.enable is '启用状态';

comment on column base_dict.group_tag is '分类标签';

comment on column base_dict.remark is '备注';

comment on column base_dict.creator is '创建人';

comment on column base_dict.create_time is '创建时间';

comment on column base_dict.last_modifier is '更新人';

comment on column base_dict.last_modified_time is '更新时间';

comment on column base_dict.deleted is '0:未删除。1:已删除';

comment on column base_dict.version is '版本';



INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1422929378374828033, 'Sex', '性别', false, '基础属性', '性别', 0, '2021-08-04 22:36:15.000000', 1399985191002447872, '2022-05-11 19:48:40.000000', false, 6);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1425744045414772737, 'MenuType', '菜单类型', false, '系统属性', '菜单类型', 0, '2021-08-12 17:00:44.000000', 1399985191002447872, '2022-05-11 19:48:44.000000', false, 4);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1430063572491411456, 'loginType', '字典类型', false, '基础属性', '字典类型', 1399985191002447872, '2021-08-24 15:05:00.000000', 1399985191002447872, '2021-08-24 15:05:00.000000', true, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435829999592759296, 'UserStatusCode', '用户状态码', false, '系统属性', '用户状态码', 1399985191002447872, '2021-09-09 12:58:43.000000', 1399985191002447872, '2022-05-11 19:48:56.000000', false, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838066191458304, 'LogBusinessType', '业务操作类型', false, '系统属性', '操作日志记录的业务操作类型', 1399985191002447872, '2021-09-09 13:30:46.000000', 1399985191002447872, '2022-05-11 19:49:00.000000', false, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1438078864509317120, 'MailSecurityCode', '邮箱安全方式编码', false, '消息服务', '邮箱安全方式编码', 1399985191002447872, '2021-09-15 17:54:54.000000', 1399985191002447872, '2022-05-11 19:49:06.000000', false, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439961232651034624, 'MessageTemplateCode', '消息模板类型', false, '消息服务', '消息模板类型', 1399985191002447872, '2021-09-20 22:34:46.000000', 1399985191002447872, '2022-05-11 19:48:34.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452836604783845376, 'SocialType', '三方系统类型', false, '系统属性', '三方系统类型', 1399985191002447872, '2021-10-26 11:16:54.000000', 1399985191002447872, '2022-05-11 19:48:28.000000', false, 3);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452843488735621120, 'ParamType', '参数类型', false, '系统属性', '参数类型', 1399985191002447872, '2021-10-26 11:44:15.000000', 1399985191002447872, '2022-05-11 19:48:21.000000', false, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496024933900169216, 'Political', '政治面貌', false, '基础数据', '政治面貌', 1399985191002447872, '2022-02-22 15:31:54.000000', 1399985191002447872, '2022-05-11 19:48:04.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1556996322223968256, 'WeChatMediaType', '微信媒体类型', false, '微信', '微信媒体类型', 1399985191002447872, '2022-08-09 21:30:25.000000', 1399985191002447872, '2022-08-09 21:30:26.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003021674987520, 'SiteMessageReceive', '消息接收类型', false, '站内信', '站内信接收类型', 1399985191002447872, '2022-08-20 22:51:37.000000', 1399985191002447872, '2022-08-20 22:51:37.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003189111603200, 'SiteMessageState', '消息发布状态', false, '站内信', '站内信消息发布状态', 1399985191002447872, '2022-08-20 22:52:17.000000', 1399985191002447872, '2022-08-20 22:52:17.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589527951317389312, 'DataScopePerm', '数据权限类型', false, '系统属性', '数据权限类型', 1414143554414059520, '2022-11-07 15:59:30.000000', 1399985191002447872, '2023-11-28 23:14:24.000000', false, 4);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1633393287952257024, 'DatabaseType', '数据库类型', false, '开发', '数据库类型', 1414143554414059520, '2023-03-08 17:04:41.000000', 1414143554414059520, '2023-03-08 17:04:41.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742690398617600, 'SmsChannel', '短信渠道商', false, '消息服务', '短信渠道商', 1414143554414059520, '2023-08-08 10:43:27.000000', 1414143554414059520, '2023-08-12 20:24:03.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338321769918464, 'GeneralTemplateUseType', '通用模板类型', false, '系统属性', '通用模板类型', 1414143554414059520, '2023-08-12 20:23:56.000000', 1414143554414059520, '2023-08-12 20:24:22.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338545284378624, 'GeneralTemplateState', '通用模板状态', false, '系统属性', '通用模板状态', 1414143554414059520, '2023-08-12 20:24:49.000000', 1414143554414059520, '2023-08-12 20:24:49.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744996611615039488, 'AsyncPayChannel', '异步支付通道', false, '支付', '', 1399985191002447872, '2024-01-10 16:16:27.000000', 1399985191002447872, '2024-01-10 16:16:27.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744996845355212800, 'PayChannel', '支付通道', false, '支付', '', 1399985191002447872, '2024-01-10 16:17:23.000000', 1399985191002447872, '2024-01-10 16:17:23.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744996913667842048, 'PayStatus', '支付状态', false, '支付', '', 1399985191002447872, '2024-01-10 16:17:39.000000', 1399985191002447872, '2024-01-10 16:17:39.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745086859116224512, 'PayMethod', '支付方式', false, '支付', '', 1399985191002447872, '2024-01-10 22:15:04.000000', 1399985191002447872, '2024-05-02 12:38:01.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109350333173760, 'PayCallbackStatus', '支付回调处理状态', false, '支付', '', 1399985191002447872, '2024-01-10 23:44:26.000000', 1399985191002447872, '2024-01-10 23:44:26.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745128986382667776, 'PayRepairSource', '支付修复来源', false, '支付', '', 1399985191002447872, '2024-01-11 01:02:28.000000', 1399985191002447872, '2024-01-11 01:02:28.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134438772346880, 'PayRepairWay', '支付修复方式', false, '支付', '', 1399985191002447872, '2024-01-11 01:24:08.000000', 1399985191002447872, '2024-01-11 01:24:08.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745748188122554368, 'RefundStatus', '退款状态', false, '支付', '', 1399985191002447872, '2024-01-12 18:02:57.000000', 1399985191002447872, '2024-02-09 21:22:52.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208645341917184, 'PaySyncStatus', '支付同步结果', false, '支付', '', 1399985191002447872, '2024-01-14 00:32:39.000000', 1399985191002447872, '2024-01-14 00:32:39.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1749612665392541696, 'ReconcileTrade', '支付对账交易类型', false, '支付', '', 1399985191002447872, '2024-01-23 09:59:00.000000', 1399985191002447872, '2024-03-01 23:31:32.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751603996496453632, 'PaymentType', '支付系统行为类型', false, '支付', '支付系统中常见的操作类型, 如支付/退款/转账等', 1399985191002447872, '2024-01-28 21:51:51.000000', 1399985191002447872, '2024-01-28 21:51:51.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751613032260370432, 'PayCallbackType', '回调类型', false, '支付', '', 1399985191002447872, '2024-01-28 22:27:45.000000', 1399985191002447872, '2024-01-28 22:27:45.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1752560153120428032, 'RefundSyncStatus', '退款同步状态', false, '支付', '', 1399985191002447872, '2024-01-31 13:11:16.000000', 1399985191002447872, '2024-01-31 13:11:16.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1753047382185091072, 'RefundRepairWay', '退款修复方式', false, '支付', '', 1399985191002447872, '2024-02-01 21:27:21.000000', 1399985191002447872, '2024-02-01 21:27:21.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1758881354618220544, 'WalletStatus', '钱包状态', false, '支付', '', 1399985191002447872, '2024-02-17 23:49:28.000000', 1399985191002447872, '2024-02-17 23:49:28.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759189874194481152, 'VoucherStatus', '储值卡状态', false, '支付', '', 1399985191002447872, '2024-02-18 20:15:25.000000', 1399985191002447872, '2024-02-18 20:15:25.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190066511708160, 'WalletRecordType', '钱包记录类型', false, '支付', '', 1399985191002447872, '2024-02-18 20:16:11.000000', 1399985191002447872, '2024-02-18 20:16:11.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190427897135104, 'VoucherRecordType', '储值卡记录类型', false, '支付', '', 1399985191002447872, '2024-02-18 20:17:37.000000', 1399985191002447872, '2024-02-18 20:17:37.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190780252225536, 'CashRecordType', '现金记录类型', false, '支付', '', 1399985191002447872, '2024-02-18 20:19:01.000000', 1399985191002447872, '2024-02-19 22:07:09.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434095349624832, 'ClientNoticeType', '客户消息通知类型', false, '支付', '', 1399985191002447872, '2024-02-25 00:53:09.000000', 1399985191002447872, '2024-02-25 00:53:09.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434264858226688, 'ClientNoticeSendType', '客户消息通知发送类型', false, '支付', '', 1399985191002447872, '2024-02-25 00:53:49.000000', 1399985191002447872, '2024-02-25 00:53:49.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761581634023583744, 'AlipayRecordType', '支付宝流水记录类型', false, '支付', '', 1399985191002447872, '2024-02-25 10:39:25.000000', 1399985191002447872, '2024-02-25 10:39:25.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761588314480300032, 'WechatPayRecordType', '微信支付流水记录类型', false, '支付', '', 1399985191002447872, '2024-02-25 11:05:58.000000', 1399985191002447872, '2024-02-25 11:05:58.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1763588034467713024, 'ReconcileDiffType', '对账差异类型', false, '支付', '', 1399985191002447872, '2024-03-01 23:32:08.000000', 1399985191002447872, '2024-03-01 23:32:08.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1766713545981153280, 'UnionPaySignType', '云闪付签名类型', false, '支付', '', 1399985191002447872, '2024-03-10 14:31:48.000000', 1399985191002447872, '2024-03-10 14:31:48.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1768206399071809536, 'UnionPayRecordType', '云闪付流水类型', false, '支付', '', 1399985191002447872, '2024-03-14 17:23:52.000000', 1399985191002447872, '2024-03-14 17:23:52.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775112798259302400, 'AllocReceiverType', '分账接收方类型', false, '支付', '', 1399985191002447872, '2024-04-02 18:47:26.000000', 1399985191002447872, '2024-05-14 16:40:14.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122632706805760, 'AllocRelationType', '分账关系类型', false, '支付', '', 1399985191002447872, '2024-04-02 19:26:30.000000', 1399985191002447872, '2024-05-14 16:40:09.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777697358802530304, 'AllocOrderStatus', '分账状态', false, '支付', '', 1399985191002447872, '2024-04-09 21:57:33.000000', 1399985191002447872, '2024-05-14 16:40:04.000000', false, 2);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780163691808391168, 'AllocDetailResult', '支付分账明细处理结果', false, '支付', '', 1399985191002447872, '2024-04-16 17:17:53.000000', 1399985191002447872, '2024-05-14 16:39:57.000000', false, 3);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165499633106944, 'AllocOrderResult', '支付分账订单处理结果', false, '支付', '', 1399985191002447872, '2024-04-16 17:25:04.000000', 1399985191002447872, '2024-05-14 16:39:36.000000', false, 1);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165929528295424, 'PayOrderAllocationStatus', '支付订单分账状态', false, '支付', '', 1399985191002447872, '2024-04-16 17:26:46.000000', 1399985191002447872, '2024-04-16 17:26:46.000000', false, 0);
INSERT INTO base_dict (id, code, name, enable, group_tag, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1786399552686465024, 'ReconcileResult', '对账结果', false, '支付', '', 1399985191002447872, '2024-05-03 22:16:58.000000', 1399985191002447872, '2024-05-03 22:16:58.000000', false, 0);

-----
create table base_dict_item
(
    id                 bigint           not null
        primary key,
    dict_id            bigint           not null,
    dict_code          varchar(255)     not null,
    code               varchar(255)     not null,
    name               varchar(100)     not null,
    enable             boolean          not null,
    sort_no            double precision not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean          not null,
    version            integer          not null
);

comment on table base_dict_item is '字典项';

comment on column base_dict_item.dict_id is '字典id';

comment on column base_dict_item.dict_code is '字典code';

comment on column base_dict_item.code is '字典项code';

comment on column base_dict_item.name is '字典项名称';

comment on column base_dict_item.enable is '启用状态';

comment on column base_dict_item.sort_no is '排序';

comment on column base_dict_item.remark is '备注';

comment on column base_dict_item.creator is '创建人';

comment on column base_dict_item.create_time is '创建时间';

comment on column base_dict_item.last_modifier is '更新人';

comment on column base_dict_item.last_modified_time is '更新时间';

comment on column base_dict_item.deleted is '0:未删除。1:已删除';

comment on column base_dict_item.version is '版本';



create index idx_dictionary_id
    on base_dict_item (dict_id);

INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1422931375807242241, 1422929378374828033, 'Sex', '1', '男', false, 0, '男性', 0, '2021-08-04 22:44:11.000000', 0, '2021-08-04 22:44:11.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1425729455402401794, 1422929378374828033, 'Sex', '2', '女', false, 0, '女性', 0, '2021-08-12 16:02:46.000000', 0, '2021-08-12 16:02:46.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1425744258544136194, 1425744045414772737, 'MenuType', '0', '顶级菜单', false, 0, '顶级菜单', 0, '2021-08-12 17:01:35.000000', 0, '2021-08-12 17:01:35.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1425744436592340993, 1425744045414772737, 'MenuType', '1', '子菜单', false, 0, '子菜单', 0, '2021-08-12 17:02:17.000000', 0, '2021-08-12 17:02:17.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1425744470582980610, 1425744045414772737, 'MenuType', '2', '按钮权限', false, 0, '按钮权限', 0, '2021-08-12 17:02:26.000000', 0, '2021-08-12 17:02:26.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1430094707250413568, 1422929378374828033, 'Sex', '0', '未知', false, 0, '不确定性别', 1399985191002447872, '2021-08-24 17:08:43.000000', 1399985191002447872, '2021-08-24 17:08:43.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435830086406463488, 1435829999592759296, 'UserStatusCode', 'normal', '正常', false, 0, 'NORMAL', 1399985191002447872, '2021-09-09 12:59:04.000000', 1399985191002447872, '2023-11-25 15:32:04.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435830141855162368, 1435829999592759296, 'UserStatusCode', 'lock', '锁定', false, 0, 'LOCK, 多次登录失败被锁定', 1399985191002447872, '2021-09-09 12:59:17.000000', 1399985191002447872, '2023-11-25 15:32:14.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435830260503633920, 1435829999592759296, 'UserStatusCode', 'ban', '封禁', false, 0, 'BAN', 1399985191002447872, '2021-09-09 12:59:45.000000', 1399985191002447872, '2023-11-25 15:32:20.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838374749626368, 1435838066191458304, 'LogBusinessType', 'other', '其它', false, 0, '', 1399985191002447872, '2021-09-09 13:32:00.000000', 1399985191002447872, '2021-09-09 13:32:00.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838414436130816, 1435838066191458304, 'LogBusinessType', 'insert', '新增', false, 0, '', 1399985191002447872, '2021-09-09 13:32:09.000000', 1399985191002447872, '2021-09-09 13:32:09.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838467624099840, 1435838066191458304, 'LogBusinessType', 'update', '修改', false, 0, '', 1399985191002447872, '2021-09-09 13:32:22.000000', 1399985191002447872, '2021-09-09 13:32:22.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838502755590144, 1435838066191458304, 'LogBusinessType', 'delete', '删除', false, 0, '', 1399985191002447872, '2021-09-09 13:32:30.000000', 1399985191002447872, '2021-09-09 13:32:30.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838546934194176, 1435838066191458304, 'LogBusinessType', 'grant', '授权', false, 0, '', 1399985191002447872, '2021-09-09 13:32:41.000000', 1399985191002447872, '2021-09-09 13:32:41.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838605537009664, 1435838066191458304, 'LogBusinessType', 'export', '导出', false, 0, '', 1399985191002447872, '2021-09-09 13:32:55.000000', 1399985191002447872, '2021-09-09 13:32:55.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838705457913856, 1435838066191458304, 'LogBusinessType', 'import', '导入', false, 0, '', 1399985191002447872, '2021-09-09 13:33:19.000000', 1399985191002447872, '2021-09-09 13:33:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838745861644288, 1435838066191458304, 'LogBusinessType', 'force', '强退', false, 0, '', 1399985191002447872, '2021-09-09 13:33:28.000000', 1399985191002447872, '2021-09-09 13:33:28.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1435838786273763328, 1435838066191458304, 'LogBusinessType', 'clean', '清空数据', false, 0, '', 1399985191002447872, '2021-09-09 13:33:38.000000', 1399985191002447872, '2021-09-09 13:33:38.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1438079113630003200, 1438078864509317120, 'MailSecurityCode', '1', '普通方式', false, 0, 'SECURITY_TYPE_PLAIN', 1399985191002447872, '2021-09-15 17:55:54.000000', 1399985191002447872, '2021-09-15 17:55:54.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1438080323061755904, 1438078864509317120, 'MailSecurityCode', '2', 'TLS方式', false, 0, 'SECURITY_TYPE_TLS', 1399985191002447872, '2021-09-15 18:00:42.000000', 1399985191002447872, '2021-09-15 18:00:42.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1438080372231581696, 1438078864509317120, 'MailSecurityCode', '3', 'SSL方式', false, 0, 'SECURITY_TYPE_SSL', 1399985191002447872, '2021-09-15 18:00:54.000000', 1399985191002447872, '2021-09-15 18:00:54.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439961603914047488, 1439961232651034624, 'MessageTemplateCode', '5', '微信', false, -10, 'WECHAT', 1399985191002447872, '2021-09-20 22:36:14.000000', 1399985191002447872, '2021-09-20 22:36:14.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439961704321490944, 1439961232651034624, 'MessageTemplateCode', '4', 'Email', false, 0, 'EMAIL', 1399985191002447872, '2021-09-20 22:36:38.000000', 1399985191002447872, '2021-09-20 22:36:38.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439962132744478720, 1439961232651034624, 'MessageTemplateCode', '3', '短信', false, 0, 'SMS', 1399985191002447872, '2021-09-20 22:38:20.000000', 1399985191002447872, '2021-09-20 22:38:20.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439962205578567680, 1439961232651034624, 'MessageTemplateCode', '2', '钉钉机器人', false, 0, 'DING_TALK_ROBOT', 1399985191002447872, '2021-09-20 22:38:38.000000', 1399985191002447872, '2021-09-20 22:38:38.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1439962267511660544, 1439961232651034624, 'MessageTemplateCode', '1', '钉钉', false, 0, 'DING_TALK', 1399985191002447872, '2021-09-20 22:38:52.000000', 1399985191002447872, '2021-09-20 22:38:52.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452836696873984000, 1452836604783845376, 'SocialType', 'WeChat', '微信', false, 0, '', 1399985191002447872, '2021-10-26 11:17:16.000000', 1399985191002447872, '2021-10-26 11:17:16.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452837435482529792, 1452836604783845376, 'SocialType', 'QQ', 'QQ', false, 0, '', 1399985191002447872, '2021-10-26 11:20:12.000000', 1399985191002447872, '2021-10-26 11:20:12.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452837523030237184, 1452836604783845376, 'SocialType', 'DingTalk', '钉钉', false, 0, '', 1399985191002447872, '2021-10-26 11:20:33.000000', 1399985191002447872, '2021-10-26 11:20:33.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452844537911406592, 1452843488735621120, 'ParamType', '1', '系统参数', false, 0, '', 1399985191002447872, '2021-10-26 11:48:25.000000', 1399985191002447872, '2021-10-26 11:48:25.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452844565031776256, 1452843488735621120, 'ParamType', '2', '用户参数', false, 0, '', 1399985191002447872, '2021-10-26 11:48:32.000000', 1399985191002447872, '2021-10-26 11:48:32.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496026946344005632, 1496024933900169216, 'Political', '1', '中共党员', false, 1, '', 1399985191002447872, '2022-02-22 15:39:54.000000', 1399985191002447872, '2022-02-22 15:39:54.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027004560945152, 1496024933900169216, 'Political', '2', '中共预备党员', false, 2, '', 1399985191002447872, '2022-02-22 15:40:07.000000', 1399985191002447872, '2022-02-22 15:40:07.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027039264616448, 1496024933900169216, 'Political', '3', '共青团员', false, 3, '', 1399985191002447872, '2022-02-22 15:40:16.000000', 1399985191002447872, '2022-02-22 15:40:16.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027077550223360, 1496024933900169216, 'Political', '4', '民革党员', false, 4, '', 1399985191002447872, '2022-02-22 15:40:25.000000', 1399985191002447872, '2022-02-22 15:40:25.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027123461074944, 1496024933900169216, 'Political', '5', '民盟盟员', false, 5, '', 1399985191002447872, '2022-02-22 15:40:36.000000', 1399985191002447872, '2022-02-22 15:40:36.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027197566038016, 1496024933900169216, 'Political', '6', '民建会员', false, 6, '', 1399985191002447872, '2022-02-22 15:40:53.000000', 1399985191002447872, '2022-02-22 15:40:53.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027234803068928, 1496024933900169216, 'Political', '7', '民进会员', false, 7, '', 1399985191002447872, '2022-02-22 15:41:02.000000', 1399985191002447872, '2022-02-22 15:41:02.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027272941875200, 1496024933900169216, 'Political', '8', '农工党党员', false, 8, '', 1399985191002447872, '2022-02-22 15:41:11.000000', 1399985191002447872, '2022-02-22 15:41:11.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027306634719232, 1496024933900169216, 'Political', '9', '致公党党员', false, 9, '', 1399985191002447872, '2022-02-22 15:41:19.000000', 1399985191002447872, '2022-02-22 15:41:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027369796743168, 1496024933900169216, 'Political', '10', '九三学社社员', false, 10, '', 1399985191002447872, '2022-02-22 15:41:34.000000', 1399985191002447872, '2022-02-22 15:41:35.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027408141070336, 1496024933900169216, 'Political', '11', '台盟盟员', false, 11, '', 1399985191002447872, '2022-02-22 15:41:44.000000', 1399985191002447872, '2022-02-22 15:41:44.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027456849522688, 1496024933900169216, 'Political', '12', '无党派人士', false, 12, '', 1399985191002447872, '2022-02-22 15:41:55.000000', 1399985191002447872, '2022-02-22 15:41:55.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1496027516639326208, 1496024933900169216, 'Political', '13', '群众', false, 13, '', 1399985191002447872, '2022-02-22 15:42:09.000000', 1399985191002447872, '2022-02-22 15:42:10.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003368762032128, 1561003021674987520, 'SiteMessageReceive', 'user', '指定用户', false, 0, '', 1399985191002447872, '2022-08-20 22:53:00.000000', 1399985191002447872, '2022-08-20 22:53:00.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003399778910208, 1561003021674987520, 'SiteMessageReceive', 'all', '全部用户', false, 0, '', 1399985191002447872, '2022-08-20 22:53:07.000000', 1399985191002447872, '2022-08-20 22:53:24.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003539772194816, 1561003189111603200, 'SiteMessageState', 'sent', '已发送', false, 0, '', 1399985191002447872, '2022-08-20 22:53:41.000000', 1399985191002447872, '2022-08-20 22:53:41.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561003575608328192, 1561003189111603200, 'SiteMessageState', 'cancel', '撤销', false, 0, '', 1399985191002447872, '2022-08-20 22:53:49.000000', 1399985191002447872, '2022-08-20 22:53:49.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1561245469535080448, 1561003189111603200, 'SiteMessageState', 'draft', '草稿', false, 0, '', 1399985191002447872, '2022-08-21 14:55:01.000000', 1399985191002447872, '2022-08-21 14:55:01.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1573665422392098816, 1439961232651034624, 'MessageTemplateCode', '0', '站内信', false, -11, 'SITE', 1399985191002447872, '2022-09-24 21:27:29.000000', 1399985191002447872, '2022-09-24 21:27:39.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528254477488128, 1589527951317389312, 'DataScopePerm', 'self_dept_sub', '所在及下级部门', false, 0, '', 1414143554414059520, '2022-11-07 16:00:43.000000', 1399985191002447872, '2023-11-28 23:16:02.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528283539820544, 1589527951317389312, 'DataScopePerm', 'self_dept', '所在部门', false, 0, '', 1414143554414059520, '2022-11-07 16:00:49.000000', 1399985191002447872, '2023-11-28 23:15:52.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528315672383488, 1589527951317389312, 'DataScopePerm', 'all', '全部数据', false, 0, '', 1414143554414059520, '2022-11-07 16:00:57.000000', 1399985191002447872, '2023-11-25 22:51:28.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528340267782144, 1589527951317389312, 'DataScopePerm', 'dept_and_user', '自定义部门和用户', false, 0, '', 1414143554414059520, '2022-11-07 16:01:03.000000', 1399985191002447872, '2023-11-29 15:25:01.000000', false, 3);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528367228768256, 1589527951317389312, 'DataScopePerm', 'dept', '自定义部门', false, 0, '', 1414143554414059520, '2022-11-07 16:01:09.000000', 1399985191002447872, '2023-11-29 15:24:47.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528393292173312, 1589527951317389312, 'DataScopePerm', 'user', '指定用户', false, 0, '', 1414143554414059520, '2022-11-07 16:01:16.000000', 1399985191002447872, '2023-11-29 15:25:16.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1589528423956729856, 1589527951317389312, 'DataScopePerm', 'self', '自身数据', false, 0, '', 1414143554414059520, '2022-11-07 16:01:23.000000', 1399985191002447872, '2023-11-25 22:50:41.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1633403429028536320, 1633393287952257024, 'DatabaseType', 'mysql', 'MySQL', false, 1, '', 1414143554414059520, '2023-03-08 17:44:59.000000', 1414143554414059520, '2023-03-08 17:44:59.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1633403459470794752, 1633393287952257024, 'DatabaseType', 'oracle', 'Oracle', false, 2, '', 1414143554414059520, '2023-03-08 17:45:07.000000', 1414143554414059520, '2023-03-08 17:45:07.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1633403498695925760, 1633393287952257024, 'DatabaseType', 'mssql', 'SQLServer', false, 3, '', 1414143554414059520, '2023-03-08 17:45:16.000000', 1414143554414059520, '2023-03-08 17:45:16.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742732891111424, 1688742690398617600, 'SmsChannel', 'alibaba', '阿里云', false, 0, '', 1414143554414059520, '2023-08-08 10:43:38.000000', 1414143554414059520, '2023-08-08 10:43:38.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742768479780864, 1688742690398617600, 'SmsChannel', 'huawei', '华为云', false, 0, '', 1414143554414059520, '2023-08-08 10:43:46.000000', 1414143554414059520, '2023-08-08 10:43:46.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742808027873280, 1688742690398617600, 'SmsChannel', 'yunpian', '云片', false, 0, '', 1414143554414059520, '2023-08-08 10:43:55.000000', 1414143554414059520, '2023-08-08 10:43:56.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742840626003968, 1688742690398617600, 'SmsChannel', 'tencent', '腾讯云', false, 0, '', 1414143554414059520, '2023-08-08 10:44:03.000000', 1414143554414059520, '2023-08-08 10:44:03.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742872506908672, 1688742690398617600, 'SmsChannel', 'uni_sms', '合一短信', false, 0, '', 1414143554414059520, '2023-08-08 10:44:11.000000', 1414143554414059520, '2023-08-08 10:44:11.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742905553829888, 1688742690398617600, 'SmsChannel', 'jd_cloud', '京东云', false, 0, '', 1414143554414059520, '2023-08-08 10:44:19.000000', 1414143554414059520, '2023-08-08 10:44:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742954715267072, 1688742690398617600, 'SmsChannel', 'cloopen', '容联云', false, 0, '', 1414143554414059520, '2023-08-08 10:44:30.000000', 1414143554414059520, '2023-08-08 10:44:30.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688742990446542848, 1688742690398617600, 'SmsChannel', 'emay', '亿美软通', false, 0, '', 1414143554414059520, '2023-08-08 10:44:39.000000', 1414143554414059520, '2023-08-08 10:44:39.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688743032297308160, 1688742690398617600, 'SmsChannel', 'ctyun', '天翼云', false, 0, '', 1414143554414059520, '2023-08-08 10:44:49.000000', 1414143554414059520, '2023-08-08 10:44:49.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1688743065205817344, 1688742690398617600, 'SmsChannel', 'netease', '网易云信', false, 0, '', 1414143554414059520, '2023-08-08 10:44:57.000000', 1414143554414059520, '2023-08-08 10:44:57.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338619024437248, 1690338321769918464, 'GeneralTemplateUseType', 'import', '导入', false, 0, '', 1414143554414059520, '2023-08-12 20:25:06.000000', 1414143554414059520, '2023-08-12 20:25:06.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338653442895872, 1690338321769918464, 'GeneralTemplateUseType', 'export', '导出', false, 0, '', 1414143554414059520, '2023-08-12 20:25:15.000000', 1414143554414059520, '2023-08-12 20:25:15.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338707129987072, 1690338545284378624, 'GeneralTemplateState', 'enable', '启用', false, 0, '', 1414143554414059520, '2023-08-12 20:25:27.000000', 1414143554414059520, '2023-08-12 20:25:27.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1690338748032839680, 1690338545284378624, 'GeneralTemplateState', 'disable', '停用', false, 0, '', 1414143554414059520, '2023-08-12 20:25:37.000000', 1414143554414059520, '2023-08-12 20:25:37.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1729519341702086656, 1589527951317389312, 'DataScopePerm', 'dept_sub', '指定部门及下级部门', false, 0, '', 1399985191002447872, '2023-11-28 23:15:19.000000', 1399985191002447872, '2023-11-28 23:15:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997130102317056, 1744996845355212800, 'PayChannel', 'ali_pay', '支付宝', false, 1, '', 1399985191002447872, '2024-01-10 16:18:31.000000', 1399985191002447872, '2024-01-10 16:18:31.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997169239367680, 1744996845355212800, 'PayChannel', 'wechat_pay', '微信支付', false, 2, '', 1399985191002447872, '2024-01-10 16:18:40.000000', 1399985191002447872, '2024-01-10 16:18:40.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997213623492608, 1744996845355212800, 'PayChannel', 'union_pay', '云闪付', false, 3, '', 1399985191002447872, '2024-01-10 16:18:51.000000', 1399985191002447872, '2024-01-10 16:18:51.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997258951335936, 1744996845355212800, 'PayChannel', 'cash_pay', '现金支付', false, 4, '', 1399985191002447872, '2024-01-10 16:19:02.000000', 1399985191002447872, '2024-05-02 12:39:33.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997315557662720, 1744996845355212800, 'PayChannel', 'wallet_pay', '钱包支付', false, 5, '', 1399985191002447872, '2024-01-10 16:19:15.000000', 1399985191002447872, '2024-01-10 16:19:15.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997364404527104, 1744996845355212800, 'PayChannel', 'voucher_pay', '储值卡支付', false, 6, '', 1399985191002447872, '2024-01-10 16:19:27.000000', 1399985191002447872, '2024-05-02 12:39:29.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997501033979904, 1744996913667842048, 'PayStatus', 'progress', '支付中', false, 1, '', 1399985191002447872, '2024-01-10 16:19:59.000000', 1399985191002447872, '2024-01-10 16:19:59.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997541811003392, 1744996913667842048, 'PayStatus', 'success', '成功', false, 2, '', 1399985191002447872, '2024-01-10 16:20:09.000000', 1399985191002447872, '2024-01-10 16:20:09.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997578863484928, 1744996913667842048, 'PayStatus', 'close', '支付关闭', false, 3, '', 1399985191002447872, '2024-01-10 16:20:18.000000', 1399985191002447872, '2024-01-10 16:20:18.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997619665674240, 1744996913667842048, 'PayStatus', 'fail', '失败', false, 7, '', 1399985191002447872, '2024-01-10 16:20:28.000000', 1399985191002447872, '2024-01-27 00:52:05.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997658735616000, 1744996913667842048, 'PayStatus', 'partial_refund', '部分退款', false, 5, '', 1399985191002447872, '2024-01-10 16:20:37.000000', 1399985191002447872, '2024-01-10 16:20:44.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1744997746321072128, 1744996913667842048, 'PayStatus', 'refunded', '全部退款', false, 6, '', 1399985191002447872, '2024-01-10 16:20:58.000000', 1399985191002447872, '2024-01-10 16:20:58.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745085949967278080, 1744996611615039488, 'AsyncPayChannel', 'ali_pay', '支付宝', false, 1, '', 1399985191002447872, '2024-01-10 22:11:27.000000', 1399985191002447872, '2024-01-10 22:11:27.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745085991767711744, 1744996611615039488, 'AsyncPayChannel', 'wechat_pay', '微信支付', false, 2, '', 1399985191002447872, '2024-01-10 22:11:37.000000', 1399985191002447872, '2024-01-10 22:11:37.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745086048852189184, 1744996611615039488, 'AsyncPayChannel', 'union_pay', '云闪付', false, 3, '', 1399985191002447872, '2024-01-10 22:11:51.000000', 1399985191002447872, '2024-01-11 00:13:03.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745086902929924096, 1745086859116224512, 'PayMethod', 'normal', '常规支付', false, 1, '', 1399985191002447872, '2024-01-10 22:15:14.000000', 1399985191002447872, '2024-01-10 22:15:26.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745086940036931584, 1745086859116224512, 'PayMethod', 'wap', 'wap支付', false, 2, '', 1399985191002447872, '2024-01-10 22:15:23.000000', 1399985191002447872, '2024-01-10 22:15:23.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745087004062982144, 1745086859116224512, 'PayMethod', 'app', '应用支付', false, 3, '', 1399985191002447872, '2024-01-10 22:15:39.000000', 1399985191002447872, '2024-01-10 22:15:39.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745087058194669568, 1745086859116224512, 'PayMethod', 'web', 'web支付', false, 4, '', 1399985191002447872, '2024-01-10 22:15:51.000000', 1399985191002447872, '2024-01-10 22:15:51.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745087104050995200, 1745086859116224512, 'PayMethod', 'qrcode', '扫码支付', false, 5, '', 1399985191002447872, '2024-01-10 22:16:02.000000', 1399985191002447872, '2024-01-10 22:16:02.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745087163324899328, 1745086859116224512, 'PayMethod', 'barcode', '付款码', false, 6, '', 1399985191002447872, '2024-01-10 22:16:17.000000', 1399985191002447872, '2024-01-10 22:16:17.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745087221629919232, 1745086859116224512, 'PayMethod', 'jsapi', '公众号/小程序支付', false, 7, '', 1399985191002447872, '2024-01-10 22:16:30.000000', 1399985191002447872, '2024-01-10 22:16:37.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109413893656576, 1745109350333173760, 'PayCallbackStatus', 'success', '成功', false, 1, '', 1399985191002447872, '2024-01-10 23:44:41.000000', 1399985191002447872, '2024-01-10 23:44:41.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109449050312704, 1745109350333173760, 'PayCallbackStatus', 'fail', '失败', false, 2, '', 1399985191002447872, '2024-01-10 23:44:50.000000', 1399985191002447872, '2024-01-10 23:44:50.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109487289782272, 1745109350333173760, 'PayCallbackStatus', 'ignore', '忽略', false, 3, '', 1399985191002447872, '2024-01-10 23:44:59.000000', 1399985191002447872, '2024-01-10 23:44:59.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109526477164544, 1745109350333173760, 'PayCallbackStatus', 'exception', '异常', false, 4, '', 1399985191002447872, '2024-01-10 23:45:08.000000', 1399985191002447872, '2024-02-01 22:14:52.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745109571771453440, 1745109350333173760, 'PayCallbackStatus', 'not_found', '未找到', false, 5, '', 1399985191002447872, '2024-01-10 23:45:19.000000', 1399985191002447872, '2024-01-10 23:45:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745129035573465088, 1745128986382667776, 'PayRepairSource', 'sync', '同步', false, 1, '', 1399985191002447872, '2024-01-11 01:02:40.000000', 1399985191002447872, '2024-01-11 01:02:40.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745133530906857472, 1745128986382667776, 'PayRepairSource', 'callback', '回调', false, 2, '', 1399985191002447872, '2024-01-11 01:20:31.000000', 1399985191002447872, '2024-01-11 01:20:31.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745133583939637248, 1745128986382667776, 'PayRepairSource', 'reconcile', '对账', false, 3, '', 1399985191002447872, '2024-01-11 01:20:44.000000', 1399985191002447872, '2024-01-11 01:20:44.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134495319953408, 1745134438772346880, 'PayRepairWay', 'pay_success', '支付成功', false, 1, '', 1399985191002447872, '2024-01-11 01:24:21.000000', 1399985191002447872, '2024-02-01 21:29:32.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134589926674432, 1745134438772346880, 'PayRepairWay', 'pay_close_local', '关闭本地支付', false, 2, '', 1399985191002447872, '2024-01-11 01:24:44.000000', 1399985191002447872, '2024-02-01 21:29:36.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134628447162368, 1745134438772346880, 'PayRepairWay', 'pay_progress', '更改为支付中', false, 3, '', 1399985191002447872, '2024-01-11 01:24:53.000000', 1399985191002447872, '2024-02-01 22:02:53.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134664807583744, 1745134438772346880, 'PayRepairWay', 'pay_close_gateway', '关闭网关支付', false, 4, '', 1399985191002447872, '2024-01-11 01:25:02.000000', 1399985191002447872, '2024-02-01 21:29:43.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745134703416152064, 1745134438772346880, 'PayRepairWay', 'refund', '退款', false, 5, '', 1399985191002447872, '2024-01-11 01:25:11.000000', 1399985191002447872, '2024-01-28 21:55:55.000000', true, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745748239158845440, 1745748188122554368, 'RefundStatus', 'success', '成功', false, 1, '', 1399985191002447872, '2024-01-12 18:03:09.000000', 1399985191002447872, '2024-01-12 18:03:09.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1745748274525216768, 1745748188122554368, 'RefundStatus', 'fail', '失败', false, 3, '', 1399985191002447872, '2024-01-12 18:03:18.000000', 1399985191002447872, '2024-01-27 00:51:02.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208700446683136, 1746208645341917184, 'PaySyncStatus', 'pay_fail', '支付查询失败', false, 1, '', 1399985191002447872, '2024-01-14 00:32:52.000000', 1399985191002447872, '2024-02-01 21:59:17.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208760488144896, 1746208645341917184, 'PaySyncStatus', 'pay_success', '支付成功', false, 2, '', 1399985191002447872, '2024-01-14 00:33:06.000000', 1399985191002447872, '2024-02-01 21:59:23.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208802531848192, 1746208645341917184, 'PaySyncStatus', 'pay_progress', '支付中', false, 3, '', 1399985191002447872, '2024-01-14 00:33:16.000000', 1399985191002447872, '2024-02-01 21:59:33.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208853882712064, 1746208645341917184, 'PaySyncStatus', 'pay_closed', '支付已关闭', false, 4, '', 1399985191002447872, '2024-01-14 00:33:28.000000', 1399985191002447872, '2024-02-01 21:59:43.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208898396860416, 1746208645341917184, 'PaySyncStatus', 'pay_refund', '支付退款', false, 5, '', 1399985191002447872, '2024-01-14 00:33:39.000000', 1399985191002447872, '2024-02-01 21:59:53.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746208959721779200, 1746208645341917184, 'PaySyncStatus', 'pay_not_found', '交易不存在', false, 6, '', 1399985191002447872, '2024-01-14 00:33:54.000000', 1399985191002447872, '2024-02-01 22:00:05.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746224281904455680, 1746208645341917184, 'PaySyncStatus', 'pay_not_found_unknown', '交易不存在(特殊)', false, 7, '未查询到订单(具体类型未知), 区别于上面的未查询到订单，有些支付方式如支付宝，发起支付后并不能查询到订单，需要用户进行操作后才能查询到订单，所以查询为了区分，增加一个未知的状态, 用于处理这种特殊情况, 然后根据业务需要，关闭订单或者进行其他操作', 1399985191002447872, '2024-01-14 01:34:47.000000', 1399985191002447872, '2024-02-01 22:00:13.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1746224432131842048, 1746208645341917184, 'PaySyncStatus', 'pay_timeout', '支付超时', false, 8, '', 1399985191002447872, '2024-01-14 01:35:23.000000', 1399985191002447872, '2024-02-01 22:00:30.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1749612708363186176, 1749612665392541696, 'ReconcileTrade', 'pay', '支付', false, 1, '', 1399985191002447872, '2024-01-23 09:59:11.000000', 1399985191002447872, '2024-01-23 09:59:11.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1749612758531256320, 1749612665392541696, 'ReconcileTrade', 'refund', '退款', false, 2, '', 1399985191002447872, '2024-01-23 09:59:23.000000', 1399985191002447872, '2024-01-23 09:59:23.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1749612797680889856, 1749612665392541696, 'ReconcileTrade', 'revoked', '撤销', false, 3, '', 1399985191002447872, '2024-01-23 09:59:32.000000', 1399985191002447872, '2024-01-23 09:59:32.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1750924188674404352, 1745748188122554368, 'RefundStatus', 'progress', '退款中', false, 0, '接口调用成功不代表成功', 1399985191002447872, '2024-01-27 00:50:32.000000', 1399985191002447872, '2024-01-27 00:50:32.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1750924286401687552, 1745748188122554368, 'RefundStatus', 'part_success', '部分成功', false, 2, '', 1399985191002447872, '2024-01-27 00:50:55.000000', 1399985191002447872, '2024-01-27 00:50:55.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1750924518497693696, 1744996913667842048, 'PayStatus', 'refunding', '退款中', false, 4, '', 1399985191002447872, '2024-01-27 00:51:50.000000', 1399985191002447872, '2024-01-27 00:52:10.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751528739035111424, 1751603996496453632, 'PaymentType', 'pay', '支付', false, 1, '', 1399985191002447872, '2024-01-28 16:52:48.000000', 1399985191002447872, '2024-01-28 16:52:48.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751528773260632064, 1751603996496453632, 'PaymentType', 'refund', '退款', false, 2, '', 1399985191002447872, '2024-01-28 16:52:56.000000', 1399985191002447872, '2024-01-28 16:52:56.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751613076980039680, 1751613032260370432, 'PayCallbackType', 'pay', '支付回调', false, 1, '', 1399985191002447872, '2024-01-28 22:27:56.000000', 1399985191002447872, '2024-01-28 22:27:56.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1751613114254819328, 1751613032260370432, 'PayCallbackType', 'refund', '退款回调', false, 2, '', 1399985191002447872, '2024-01-28 22:28:04.000000', 1399985191002447872, '2024-01-28 22:28:04.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1752216855927226368, 1745748188122554368, 'RefundStatus', 'close', '关闭', false, 4, '', 1399985191002447872, '2024-01-30 14:27:08.000000', 1399985191002447872, '2024-01-30 14:27:08.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1752560213673594880, 1752560153120428032, 'RefundSyncStatus', 'refund_success', '退款成功', false, 1, '', 1399985191002447872, '2024-01-31 13:11:31.000000', 1399985191002447872, '2024-02-01 21:56:59.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1752560254228320256, 1752560153120428032, 'RefundSyncStatus', 'refund_fail', '退款失败', false, 2, '', 1399985191002447872, '2024-01-31 13:11:40.000000', 1399985191002447872, '2024-02-01 21:57:07.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1752560288546115584, 1752560153120428032, 'RefundSyncStatus', 'refund_progress', '退款中', false, 3, '', 1399985191002447872, '2024-01-31 13:11:48.000000', 1399985191002447872, '2024-02-01 21:57:45.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1753044707091554304, 1745128986382667776, 'PayRepairSource', 'task', '定时任务', false, 4, '', 1399985191002447872, '2024-02-01 21:16:43.000000', 1399985191002447872, '2024-02-01 21:16:43.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1753047429014495232, 1753047382185091072, 'RefundRepairWay', 'refund_success', '退款成功', false, 1, '', 1399985191002447872, '2024-02-01 21:27:32.000000', 1399985191002447872, '2024-02-01 21:29:13.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1753047491140526080, 1753047382185091072, 'RefundRepairWay', 'refund_fail', '退款失败', false, 2, '', 1399985191002447872, '2024-02-01 21:27:47.000000', 1399985191002447872, '2024-02-01 21:29:19.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1755939895162052608, 1745134438772346880, 'PayRepairWay', 'refund_success', '退款成功', false, 5, '会更新为部分退款和全部退款', 1399985191002447872, '2024-02-09 21:01:09.000000', 1399985191002447872, '2024-02-09 21:10:22.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1755942271772139520, 1745134438772346880, 'PayRepairWay', 'refund_fail', '退款失败', false, 6, '', 1399985191002447872, '2024-02-09 21:10:36.000000', 1399985191002447872, '2024-02-09 21:10:36.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1758881424411439104, 1758881354618220544, 'WalletStatus', 'normal', '正常', false, 1, '', 1399985191002447872, '2024-02-17 23:49:45.000000', 1399985191002447872, '2024-02-17 23:49:45.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1758881470334873600, 1758881354618220544, 'WalletStatus', 'forbidden', '禁用', false, 2, '', 1399985191002447872, '2024-02-17 23:49:56.000000', 1399985191002447872, '2024-02-17 23:49:56.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759189919170002944, 1759189874194481152, 'VoucherStatus', 'normal', '正常', false, 1, '', 1399985191002447872, '2024-02-18 20:15:36.000000', 1399985191002447872, '2024-02-18 20:15:36.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759189962564272128, 1759189874194481152, 'VoucherStatus', 'forbidden', '禁用', false, 2, '', 1399985191002447872, '2024-02-18 20:15:46.000000', 1399985191002447872, '2024-02-18 20:15:46.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190150464897024, 1759190066511708160, 'WalletRecordType', 'create', '创建', false, 1, '', 1399985191002447872, '2024-02-18 20:16:31.000000', 1399985191002447872, '2024-02-18 20:16:31.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190193783668736, 1759190066511708160, 'WalletRecordType', 'pay', '支付', false, 2, '', 1399985191002447872, '2024-02-18 20:16:41.000000', 1399985191002447872, '2024-02-18 20:16:41.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190227510067200, 1759190066511708160, 'WalletRecordType', 'refund', '退款', false, 3, '', 1399985191002447872, '2024-02-18 20:16:49.000000', 1399985191002447872, '2024-02-18 20:16:56.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190291980713984, 1759190066511708160, 'WalletRecordType', 'close_pay', '支付关闭', false, 4, '', 1399985191002447872, '2024-02-18 20:17:04.000000', 1399985191002447872, '2024-02-18 20:17:04.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190340517199872, 1759190066511708160, 'WalletRecordType', 'close_refund', '退款关闭', false, 5, '', 1399985191002447872, '2024-02-18 20:17:16.000000', 1399985191002447872, '2024-02-19 17:31:26.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190472654553088, 1759190427897135104, 'VoucherRecordType', 'import', '导入', false, 1, '', 1399985191002447872, '2024-02-18 20:17:48.000000', 1399985191002447872, '2024-02-18 20:17:48.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190506087350272, 1759190427897135104, 'VoucherRecordType', 'pay', '支付', false, 2, '', 1399985191002447872, '2024-02-18 20:17:55.000000', 1399985191002447872, '2024-02-18 20:18:16.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190574865547264, 1759190427897135104, 'VoucherRecordType', 'refund', '退款', false, 3, '', 1399985191002447872, '2024-02-18 20:18:12.000000', 1399985191002447872, '2024-02-18 20:18:12.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190633921347584, 1759190427897135104, 'VoucherRecordType', 'close_pay', '支付关闭', false, 4, '', 1399985191002447872, '2024-02-18 20:18:26.000000', 1399985191002447872, '2024-02-18 20:18:26.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190717757095936, 1759190427897135104, 'VoucherRecordType', 'close_refund', '退款关闭', false, 5, '', 1399985191002447872, '2024-02-18 20:18:46.000000', 1399985191002447872, '2024-02-19 17:31:20.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190820500766720, 1759190780252225536, 'CashRecordType', 'pay', '支付', false, 1, '', 1399985191002447872, '2024-02-18 20:19:10.000000', 1399985191002447872, '2024-02-18 20:19:39.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190850754281472, 1759190780252225536, 'CashRecordType', 'refund', '退款', false, 2, '', 1399985191002447872, '2024-02-18 20:19:18.000000', 1399985191002447872, '2024-02-18 20:19:42.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190884061249536, 1759190780252225536, 'CashRecordType', 'close_pay', '支付关闭', false, 3, '', 1399985191002447872, '2024-02-18 20:19:26.000000', 1399985191002447872, '2024-02-18 20:19:46.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759190913261993984, 1759190780252225536, 'CashrRecordType', 'close_refund', '退款关闭', false, 4, '', 1399985191002447872, '2024-02-18 20:19:33.000000', 1399985191002447872, '2024-02-19 17:31:15.000000', true, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759514962507554816, 1759190066511708160, 'WalletRecordType', 'recharge', '充值', false, 11, '', 1399985191002447872, '2024-02-19 17:47:12.000000', 1399985191002447872, '2024-02-19 17:47:12.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1759515000520531968, 1759190066511708160, 'WalletRecordType', 'deduct', '扣减', false, 12, '', 1399985191002447872, '2024-02-19 17:47:21.000000', 1399985191002447872, '2024-02-19 17:47:21.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434142275497984, 1761434095349624832, 'ClientNoticeType', 'pay', '支付通知', false, 1, '', 1399985191002447872, '2024-02-25 00:53:20.000000', 1399985191002447872, '2024-02-25 00:53:20.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434179445420032, 1761434095349624832, 'ClientNoticeType', 'refund', '退款通知', false, 2, '', 1399985191002447872, '2024-02-25 00:53:29.000000', 1399985191002447872, '2024-02-25 00:53:29.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434308537708544, 1761434264858226688, 'ClientNoticeSendType', 'auto', '自动发送', false, 0, '', 1399985191002447872, '2024-02-25 00:54:00.000000', 1399985191002447872, '2024-02-25 00:54:00.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761434346206752768, 1761434264858226688, 'ClientNoticeSendType', 'manual', '手动发送', false, 1, '', 1399985191002447872, '2024-02-25 00:54:09.000000', 1399985191002447872, '2024-02-25 00:54:09.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761581686381080576, 1761581634023583744, 'AlipayRecordType', 'pay', '支付', false, 0, '', 1399985191002447872, '2024-02-25 10:39:37.000000', 1399985191002447872, '2024-02-25 10:39:37.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761581731029446656, 1761581634023583744, 'AlipayRecordType', 'refund', '退款', false, 1, '', 1399985191002447872, '2024-02-25 10:39:48.000000', 1399985191002447872, '2024-02-25 10:39:48.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761588368863645696, 1761588314480300032, 'WechatPayRecordType', 'pay', '支付', false, 0, '', 1399985191002447872, '2024-02-25 11:06:11.000000', 1399985191002447872, '2024-02-25 11:06:11.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1761588397825314816, 1761588314480300032, 'WechatPayRecordType', 'refund', '退款', false, 0, '', 1399985191002447872, '2024-02-25 11:06:17.000000', 1399985191002447872, '2024-02-25 11:06:17.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1763588081838182400, 1763588034467713024, 'ReconcileDiffType', 'local_not_exists', '本地订单不存在', false, 0, '', 1399985191002447872, '2024-03-01 23:32:19.000000', 1399985191002447872, '2024-03-01 23:32:19.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1763588123143688192, 1763588034467713024, 'ReconcileDiffType', 'remote_not_exists', '远程订单不存在', false, 1, '', 1399985191002447872, '2024-03-01 23:32:29.000000', 1399985191002447872, '2024-03-01 23:32:29.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1763588174695878656, 1763588034467713024, 'ReconcileDiffType', 'not_match', '订单信息不一致', false, 2, '', 1399985191002447872, '2024-03-01 23:32:41.000000', 1399985191002447872, '2024-03-01 23:32:48.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1766713657021157376, 1766713545981153280, 'UnionPaySignType', 'RSA2', 'RSA2', false, 0, '', 1399985191002447872, '2024-03-10 14:32:14.000000', 1399985191002447872, '2024-03-10 14:32:14.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1768206502721449984, 1768206399071809536, 'UnionPayRecordType', 'pay', '支付', false, 1, '', 1399985191002447872, '2024-03-14 17:24:17.000000', 1399985191002447872, '2024-03-14 17:24:17.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1768206547285929984, 1768206399071809536, 'UnionPayRecordType', 'refund', '退款', false, 0, '', 1399985191002447872, '2024-03-14 17:24:27.000000', 1399985191002447872, '2024-03-14 17:24:27.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122126567559168, 1775112798259302400, 'AllocReceiverType', 'wx_personal', '个人', false, 1, '', 1399985191002447872, '2024-04-02 19:24:30.000000', 1399985191002447872, '2024-04-02 19:24:30.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122171861848064, 1775112798259302400, 'AllocReceiverType', 'wx_merchant', '商户', false, 2, '', 1399985191002447872, '2024-04-02 19:24:41.000000', 1399985191002447872, '2024-04-02 19:24:41.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122227956469760, 1775112798259302400, 'AllocReceiverType', 'ali_user_id', '用户ID', false, 3, '', 1399985191002447872, '2024-04-02 19:24:54.000000', 1399985191002447872, '2024-04-02 19:25:53.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122278170677248, 1775112798259302400, 'AllocReceiverType', 'ali_open_id', '登录号', false, 4, '', 1399985191002447872, '2024-04-02 19:25:06.000000', 1399985191002447872, '2024-04-02 19:25:06.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122427802472448, 1775112798259302400, 'AllocReceiverType', 'ali_login_name', '账号', false, 5, '', 1399985191002447872, '2024-04-02 19:25:42.000000', 1399985191002447872, '2024-04-02 19:25:42.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122672623996928, 1775122632706805760, 'AllocRelationType', 'SERVICE_PROVIDER', '服务商', false, 1, '', 1399985191002447872, '2024-04-02 19:26:40.000000', 1399985191002447872, '2024-04-02 19:26:40.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122710884438016, 1775122632706805760, 'AllocRelationType', 'STORE', '门店', false, 2, '', 1399985191002447872, '2024-04-02 19:26:49.000000', 1399985191002447872, '2024-04-02 19:26:49.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122750612885504, 1775122632706805760, 'AllocRelationType', 'STAFF', '员工', false, 3, '', 1399985191002447872, '2024-04-02 19:26:59.000000', 1399985191002447872, '2024-04-02 19:26:59.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122847035740160, 1775122632706805760, 'AllocRelationType', 'STORE_OWNER', '店主', false, 4, '', 1399985191002447872, '2024-04-02 19:27:22.000000', 1399985191002447872, '2024-04-02 19:27:22.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122892464246784, 1775122632706805760, 'AllocRelationType', 'PARTNER', '合作伙伴', false, 5, '', 1399985191002447872, '2024-04-02 19:27:32.000000', 1399985191002447872, '2024-04-02 19:27:32.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775122934138851328, 1775122632706805760, 'AllocRelationType', 'HEADQUARTER', '总部', false, 6, '', 1399985191002447872, '2024-04-02 19:27:42.000000', 1399985191002447872, '2024-04-02 19:27:42.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775123560512016384, 1775122632706805760, 'AllocRelationType', 'DISTRIBUTOR', '分销商', false, 7, '', 1399985191002447872, '2024-04-02 19:30:12.000000', 1399985191002447872, '2024-04-02 19:30:12.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775123607781822464, 1775122632706805760, 'AllocRelationType', 'USER', '用户', false, 8, '', 1399985191002447872, '2024-04-02 19:30:23.000000', 1399985191002447872, '2024-04-02 19:30:23.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775123654527340544, 1775122632706805760, 'AllocRelationType', 'SUPPLIER', '供应商', false, 9, '', 1399985191002447872, '2024-04-02 19:30:34.000000', 1399985191002447872, '2024-04-02 19:30:34.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1775123705886593024, 1775122632706805760, 'AllocRelationType', 'CUSTOM', '自定义', false, 10, '', 1399985191002447872, '2024-04-02 19:30:46.000000', 1399985191002447872, '2024-04-02 19:30:46.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700713809522688, 1777697358802530304, 'AllocOrderStatus', 'waiting', '待分账', false, 1, '', 1399985191002447872, '2024-04-09 22:10:53.000000', 1399985191002447872, '2024-04-09 22:10:53.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700750744563712, 1777697358802530304, 'AllocOrderStatus', 'allocation_processing', '分账处理中', false, 2, '', 1399985191002447872, '2024-04-09 22:11:02.000000', 1399985191002447872, '2024-04-16 17:14:53.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700787453112320, 1777697358802530304, 'AllocOrderStatus', 'allocation_end', '分账完成', false, 3, '', 1399985191002447872, '2024-04-09 22:11:10.000000', 1399985191002447872, '2024-04-16 17:36:25.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700835826020352, 1777697358802530304, 'AllocOrderStatus', 'allocation_failed', '分账失败', false, 4, '', 1399985191002447872, '2024-04-09 22:11:22.000000', 1399985191002447872, '2024-04-16 17:16:34.000000', false, 2);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700870613577728, 1777697358802530304, 'AllocOrderStatus', 'finish', '分账完结', false, 5, '', 1399985191002447872, '2024-04-09 22:11:30.000000', 1399985191002447872, '2024-04-16 17:17:06.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700915450687488, 1777697358802530304, 'AllocationStatus', 'partial_failed', '部分分账失败', false, 6, '', 1399985191002447872, '2024-04-09 22:11:41.000000', 1399985191002447872, '2024-04-16 17:16:49.000000', true, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777700964087836672, 1777697358802530304, 'AllocOrderStatus', 'finish_failed', '分账完结失败', false, 6, '', 1399985191002447872, '2024-04-09 22:11:53.000000', 1399985191002447872, '2024-04-16 17:17:25.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777701030081015808, 1777697358802530304, 'AllocationStatus', 'closed', '分账关闭', false, 8, '', 1399985191002447872, '2024-04-09 22:12:08.000000', 1399985191002447872, '2024-04-16 17:16:19.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1777701090676125696, 1777697358802530304, 'AllocationStatus', 'unknown', '分账状态未知', false, 9, '', 1399985191002447872, '2024-04-09 22:12:23.000000', 1399985191002447872, '2024-04-16 17:16:16.000000', true, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780164864510623744, 1780163691808391168, 'AllocDetailResult', 'pending', '待分账', false, 1, '', 1399985191002447872, '2024-04-16 17:22:32.000000', 1399985191002447872, '2024-04-16 17:22:32.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780164903886749696, 1780163691808391168, 'AllocDetailResult', 'success', '分账成功', false, 2, '', 1399985191002447872, '2024-04-16 17:22:42.000000', 1399985191002447872, '2024-04-16 17:22:42.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780164940712738816, 1780163691808391168, 'AllocDetailResult', 'fail', '分账失败', false, 0, '', 1399985191002447872, '2024-04-16 17:22:50.000000', 1399985191002447872, '2024-04-16 17:22:50.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165545665593344, 1780165499633106944, 'AllocOrderResult', 'all_pending', '全部处理中', false, 0, '', 1399985191002447872, '2024-04-16 17:25:15.000000', 1399985191002447872, '2024-05-14 16:40:30.000000', false, 1);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165581623361536, 1780165499633106944, 'AllocOrderResult', 'all_success', '全部成功', false, 1, '', 1399985191002447872, '2024-04-16 17:25:23.000000', 1399985191002447872, '2024-04-16 17:25:23.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165617413357568, 1780165499633106944, 'AllocOrderResult', 'part_success', '部分成功', false, 2, '', 1399985191002447872, '2024-04-16 17:25:32.000000', 1399985191002447872, '2024-04-16 17:25:32.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165653350154240, 1780165499633106944, 'AllocOrderResult', 'all_failed', '全部失败', false, 3, '', 1399985191002447872, '2024-04-16 17:25:40.000000', 1399985191002447872, '2024-04-16 17:25:40.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780165986231091200, 1780165929528295424, 'PayOrderAllocationStatus', 'waiting', '待分账', false, 1, '', 1399985191002447872, '2024-04-16 17:27:00.000000', 1399985191002447872, '2024-04-16 17:27:00.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1780166037149941760, 1780165929528295424, 'PayOrderAllocationStatus', 'allocation', '已分账', false, 2, '', 1399985191002447872, '2024-04-16 17:27:12.000000', 1399985191002447872, '2024-04-16 17:27:12.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1786399594956660736, 1786399552686465024, 'ReconcileResult', 'consistent', '一致', false, 0, '', 1399985191002447872, '2024-05-03 22:17:08.000000', 1399985191002447872, '2024-05-03 22:17:08.000000', false, 0);
INSERT INTO base_dict_item (id, dict_id, dict_code, code, name, enable, sort_no, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1786399681275437056, 1786399552686465024, 'ReconcileResult', 'inconsistent', '不一致', false, 1, '', 1399985191002447872, '2024-05-03 22:17:28.000000', 1399985191002447872, '2024-05-03 22:17:28.000000', false, 0);

-----
create table base_dynamic_data_source
(
    id                 bigint  not null
        primary key,
    code               varchar(255),
    name               varchar(255),
    database_type      varchar(255),
    auto_load          boolean,
    db_driver          varchar(255),
    db_url             varchar(255),
    db_name            varchar(255),
    db_username        varchar(255),
    db_password        varchar(255),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table base_dynamic_data_source is '动态数据源管理';

comment on column base_dynamic_data_source.code is '数据源编码';

comment on column base_dynamic_data_source.name is '数据源名称';

comment on column base_dynamic_data_source.database_type is '数据库类型';

comment on column base_dynamic_data_source.auto_load is '是否启动自动加载';

comment on column base_dynamic_data_source.db_driver is '驱动类';

comment on column base_dynamic_data_source.db_url is '数据库地址';

comment on column base_dynamic_data_source.db_name is '数据库名称';

comment on column base_dynamic_data_source.db_username is '用户名';

comment on column base_dynamic_data_source.db_password is '密码';

comment on column base_dynamic_data_source.remark is '备注';

comment on column base_dynamic_data_source.creator is '创建人';

comment on column base_dynamic_data_source.create_time is '创建时间';

comment on column base_dynamic_data_source.last_modifier is '最后修改人';

comment on column base_dynamic_data_source.last_modified_time is '最后修改时间';

comment on column base_dynamic_data_source.version is '版本';

comment on column base_dynamic_data_source.deleted is '0:未删除。1:已删除';




-----
create table base_key_value
(
    id                 bigint       not null
        primary key,
    key                varchar(255) not null,
    value              text         not null,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean      not null,
    version            integer      not null
);

comment on table base_key_value is 'kv存储';

comment on column base_key_value.key is '参数键名';

comment on column base_key_value.value is '参数值';

comment on column base_key_value.creator is '创建人';

comment on column base_key_value.create_time is '创建时间';

comment on column base_key_value.last_modifier is '更新人';

comment on column base_key_value.last_modified_time is '更新时间';

comment on column base_key_value.deleted is '0:未删除。1:已删除';

comment on column base_key_value.version is '版本';




-----
create table base_param
(
    id                 bigint       not null
        primary key,
    name               varchar(50),
    param_key          varchar(255) not null,
    value              varchar(255) not null,
    type               integer,
    enable             boolean      not null,
    internal           boolean      not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean      not null,
    version            integer      not null
);

comment on table base_param is '系统参数配置';

comment on column base_param.name is '参数名称';

comment on column base_param.param_key is '参数键名';

comment on column base_param.value is '参数值';

comment on column base_param.type is '参数类型';

comment on column base_param.enable is '启用状态';

comment on column base_param.internal is '内置参数';

comment on column base_param.remark is '备注';

comment on column base_param.creator is '创建人';

comment on column base_param.create_time is '创建时间';

comment on column base_param.last_modifier is '更新人';

comment on column base_param.last_modified_time is '更新时间';

comment on column base_param.deleted is '0:未删除。1:已删除';

comment on column base_param.version is '版本';



INSERT INTO base_param (id, name, param_key, value, type, enable, internal, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1452842684284891136, '测试', 'test.v1', '123', 1, true, false, null, 1399985191002447872, '2021-10-26 11:41:03.000000', 1399985191002447872, '2024-01-07 23:31:08.000000', true, 0);
INSERT INTO base_param (id, name, param_key, value, type, enable, internal, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1545765299880448000, '服务器地址', 'ServerUrl', 'https://doc.daxpay.cn', 1, true, true, '优先级高于配置文件内进行的配置', 1399985191002447872, '2022-07-09 21:42:21.000000', 1399985191002447872, '2024-05-31 21:00:39.399000', false, 3);
INSERT INTO base_param (id, name, param_key, value, type, enable, internal, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1529281530059161600, 'websocket服务器地址', 'WebsocketServerUrl', 'ws://doc.daxpay.cn:9000', 1, true, true, '', 1399985191002447872, '2022-05-25 10:01:44.000000', 1399985191002447872, '2024-05-31 21:00:59.754000', false, 5);

-----
create table base_province
(
    code char(2)     not null
        primary key,
    name varchar(30) not null
);

comment on table base_province is '省份表';

comment on column base_province.code is '省份编码';

comment on column base_province.name is '省份名称';




-----
create table base_street
(
    code      char(9)     not null
        primary key,
    name      varchar(60) not null,
    area_code char(6)     not null
);

comment on table base_street is '街道表';

comment on column base_street.code is '编码';

comment on column base_street.name is '街道名称';

comment on column base_street.area_code is '县区编码';



create index inx_area_code
    on base_street (area_code);

comment on index inx_area_code is '县区';


-----
create table base_village
(
    code        char(12)    not null
        primary key,
    name        varchar(55) not null,
    street_code char(9)     not null
);

comment on table base_village is '村庄/社区';

comment on column base_village.code is '编码';

comment on column base_village.name is '名称';

comment on column base_village.street_code is '社区/乡镇编码';



create index inx_street_code
    on base_village (street_code);

comment on index inx_street_code is '所属街道索引';


-----
create table common_sequence_range
(
    id                 bigint       not null
        primary key,
    range_key          varchar(255) not null,
    range_value        bigint       not null,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer      not null,
    deleted            boolean      not null
);

comment on table common_sequence_range is '序列生成器队列区间管理';

comment on column common_sequence_range.id is '主键';

comment on column common_sequence_range.range_key is '区间key';

comment on column common_sequence_range.range_value is '区间开始值';

comment on column common_sequence_range.creator is '创建人';

comment on column common_sequence_range.create_time is '创建时间';

comment on column common_sequence_range.last_modifier is '最后修改人';

comment on column common_sequence_range.last_modified_time is '最后修改时间';

comment on column common_sequence_range.version is '版本';

comment on column common_sequence_range.deleted is '0:未删除。1:已删除';




-----
create table ddl_history
(
    script  varchar(500) not null
        primary key,
    type    varchar(30)  not null,
    version varchar(30)  not null
);

comment on table ddl_history is 'DDL 版本';

comment on column ddl_history.script is '脚本';

comment on column ddl_history.type is '类型';

comment on column ddl_history.version is '版本';




-----
create table iam_client
(
    id                 bigint      not null
        primary key,
    code               varchar(21) not null,
    name               varchar(50),
    internal           boolean     not null,
    enable             boolean     not null,
    default_endow      boolean,
    login_type_ids     text,
    description        varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer     not null,
    deleted            boolean     not null
);

comment on table iam_client is '认证终端';

comment on column iam_client.id is '主键';

comment on column iam_client.code is '编码';

comment on column iam_client.name is '名称';

comment on column iam_client.internal is '是否系统内置';

comment on column iam_client.enable is '是否可用';

comment on column iam_client.default_endow is '新注册的用户是否默认赋予该终端';

comment on column iam_client.login_type_ids is '关联登录方式
';

comment on column iam_client.description is '描述';

comment on column iam_client.creator is '创建者ID';

comment on column iam_client.create_time is '创建时间';

comment on column iam_client.last_modifier is '最后修者ID';

comment on column iam_client.last_modified_time is '最后修改时间';

comment on column iam_client.version is '乐观锁';

comment on column iam_client.deleted is '删除标志';



INSERT INTO iam_client (id, code, name, internal, enable, default_endow, login_type_ids, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1580487061605175296, 'dax-pay', '支付网关', false, true, true, '1430430071299207168,1430478946919653376,1435138582839009280,1542091599907115008,1542804450312122368,1543126042909016064', '支付网关管理端', 1399985191002447872, '2022-10-13 17:14:14.000000', 1399985191002447872, '2023-10-19 19:58:21.000000', 3, false);

-----
create table iam_data_role
(
    id                 bigint      not null
        primary key,
    code               varchar(50),
    name               varchar(150),
    type               varchar(20) not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer     not null,
    deleted            boolean     not null
);

comment on table iam_data_role is '数据范围权限';

comment on column iam_data_role.id is '角色ID';

comment on column iam_data_role.code is '编码';

comment on column iam_data_role.name is '名称';

comment on column iam_data_role.type is '类型';

comment on column iam_data_role.remark is '说明';

comment on column iam_data_role.creator is '创建人';

comment on column iam_data_role.create_time is '创建时间';

comment on column iam_data_role.last_modifier is '最后修改人';

comment on column iam_data_role.last_modified_time is '最后修改时间';

comment on column iam_data_role.version is '版本';

comment on column iam_data_role.deleted is '0:未删除。1:已删除';




-----
create table iam_data_role_dept
(
    id      bigint not null
        primary key,
    role_id bigint not null,
    dept_id bigint not null
);

comment on table iam_data_role_dept is '数据范围部门关联配置';

comment on column iam_data_role_dept.role_id is '数据角色id';

comment on column iam_data_role_dept.dept_id is '部门id';




-----
create table iam_data_role_user
(
    id      bigint not null
        primary key,
    role_id bigint not null,
    user_id bigint not null
);

comment on table iam_data_role_user is '数据范围用户关联配置';

comment on column iam_data_role_user.role_id is '数据角色id';

comment on column iam_data_role_user.user_id is '用户id';




-----
create table iam_dept
(
    id                 bigint       not null
        primary key,
    parent_id          bigint,
    dept_name          varchar(100) not null,
    sort_no            integer      not null,
    org_category       varchar(10)  not null,
    org_code           varchar(64)  not null,
    mobile             varchar(32),
    fax                varchar(32),
    address            varchar(100),
    remark             varchar(500),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer      not null,
    deleted            boolean      not null
);

comment on table iam_dept is '部门组织机构表';

comment on column iam_dept.id is 'ID';

comment on column iam_dept.parent_id is '父机构ID';

comment on column iam_dept.dept_name is '机构/部门名称';

comment on column iam_dept.sort_no is '排序';

comment on column iam_dept.org_category is '机构类别 1公司 2部门 3岗位';

comment on column iam_dept.org_code is '机构编码';

comment on column iam_dept.mobile is '手机号';

comment on column iam_dept.fax is '传真';

comment on column iam_dept.address is '地址';

comment on column iam_dept.remark is '备注';

comment on column iam_dept.creator is '创建人';

comment on column iam_dept.create_time is '创建时间';

comment on column iam_dept.last_modifier is '最后修改人';

comment on column iam_dept.last_modified_time is '最后修改时间';

comment on column iam_dept.version is '版本';

comment on column iam_dept.deleted is '0:未删除。1:已删除';




-----
create table iam_login_security_config
(
    id                         bigint  not null
        primary key,
    client_id                  bigint,
    require_login_change_pwd   boolean,
    captcha_enable             boolean,
    max_captcha_error_count    integer,
    allow_multi_login          boolean,
    allow_multi_terminal_login boolean,
    creator                    bigint,
    create_time                timestamp(6),
    last_modifier              bigint,
    last_modified_time         timestamp(6),
    version                    integer not null,
    deleted                    boolean not null
);

comment on table iam_login_security_config is '登录安全策略';

comment on column iam_login_security_config.id is '主键';

comment on column iam_login_security_config.client_id is '关联终端ID';

comment on column iam_login_security_config.require_login_change_pwd is '修改密码是否需要重新登录';

comment on column iam_login_security_config.captcha_enable is '默认启用验证码';

comment on column iam_login_security_config.max_captcha_error_count is '出现验证码的错误次数';

comment on column iam_login_security_config.allow_multi_login is '同端是否允许同时登录';

comment on column iam_login_security_config.allow_multi_terminal_login is '多终端是否允许同时登录';

comment on column iam_login_security_config.creator is '创建者ID';

comment on column iam_login_security_config.create_time is '创建时间';

comment on column iam_login_security_config.last_modifier is '最后修者ID';

comment on column iam_login_security_config.last_modified_time is '最后修改时间';

comment on column iam_login_security_config.version is '乐观锁';

comment on column iam_login_security_config.deleted is '删除标志';




-----
create table iam_login_type
(
    id                 bigint      not null
        primary key,
    code               varchar(21) not null,
    name               varchar(50),
    type               varchar(50),
    internal           boolean     not null,
    timeout            bigint,
    captcha_type       varchar(8),
    captcha            boolean     not null,
    enable             boolean     not null,
    description        varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer     not null,
    deleted            boolean     not null
);

comment on table iam_login_type is '登录方式';

comment on column iam_login_type.id is '主键';

comment on column iam_login_type.code is '编码';

comment on column iam_login_type.name is '名称';

comment on column iam_login_type.type is '类型';

comment on column iam_login_type.internal is '是否系统内置';

comment on column iam_login_type.timeout is '在线时长(秒)';

comment on column iam_login_type.captcha_type is '验证码类型';

comment on column iam_login_type.captcha is '启用验证码';

comment on column iam_login_type.enable is '是否可用';

comment on column iam_login_type.description is '描述';

comment on column iam_login_type.creator is '创建者ID';

comment on column iam_login_type.create_time is '创建时间';

comment on column iam_login_type.last_modifier is '最后修者ID';

comment on column iam_login_type.last_modified_time is '最后修改时间';

comment on column iam_login_type.version is '乐观锁';

comment on column iam_login_type.deleted is '删除标志';



INSERT INTO iam_login_type (id, code, name, type, internal, timeout, captcha_type, captcha, enable, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1430430071299207168, 'password', '账号密码登陆', 'password', true, 3600, '-1', false, true, null, 1399985191002447872, '2021-08-25 15:21:20.000000', 1399985191002447872, '2022-11-03 22:24:53.000000', 20, false);
INSERT INTO iam_login_type (id, code, name, type, internal, timeout, captcha_type, captcha, enable, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1435138582839009280, 'phone', '手机短信登录', 'openId', false, 3600, '0', false, true, null, 1399985191002447872, '2021-09-07 15:11:16.000000', 1399985191002447872, '2022-07-16 12:32:19.000000', 5, false);
INSERT INTO iam_login_type (id, code, name, type, internal, timeout, captcha_type, captcha, enable, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1542091599907115008, 'dingTalk', '钉钉', 'openId', false, 5, '-1', false, true, '', 1399985191002447872, '2022-06-29 18:24:23.000000', 1399985191002447872, '2022-07-02 14:55:01.000000', 5, false);
INSERT INTO iam_login_type (id, code, name, type, internal, timeout, captcha_type, captcha, enable, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1542804450312122368, 'weCom', '企业微信', 'openId', false, 5, '-1', false, true, '', 1399985191002447872, '2022-07-01 17:37:00.000000', 1399985191002447872, '2022-07-01 17:37:00.000000', 0, false);
INSERT INTO iam_login_type (id, code, name, type, internal, timeout, captcha_type, captcha, enable, description, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1543126042909016064, 'weChat', '微信登录', 'openId', false, 5, '-1', false, true, '', 1399985191002447872, '2022-07-02 14:54:53.000000', 0, '2022-10-12 22:15:05.000000', 2, false);

-----
create table iam_password_change_history
(
    id          bigint not null
        primary key,
    user_id     bigint,
    password    varchar(255),
    creator     bigint,
    create_time timestamp(6)
);

comment on table iam_password_change_history is '密码更改历史';

comment on column iam_password_change_history.id is '主键';

comment on column iam_password_change_history.user_id is '用户Id';

comment on column iam_password_change_history.password is '密码';

comment on column iam_password_change_history.creator is '创建者ID';

comment on column iam_password_change_history.create_time is '创建时间';




-----
create table iam_password_login_fail_record
(
    id                 bigint  not null
        primary key,
    user_id            bigint,
    fail_count         integer,
    fail_time          timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table iam_password_login_fail_record is '密码登录失败记录';

comment on column iam_password_login_fail_record.id is '主键';

comment on column iam_password_login_fail_record.user_id is '用户id';

comment on column iam_password_login_fail_record.fail_count is '登录失败次数';

comment on column iam_password_login_fail_record.fail_time is '登录失败时间';

comment on column iam_password_login_fail_record.creator is '创建者ID';

comment on column iam_password_login_fail_record.create_time is '创建时间';

comment on column iam_password_login_fail_record.last_modifier is '最后修者ID';

comment on column iam_password_login_fail_record.last_modified_time is '最后修改时间';

comment on column iam_password_login_fail_record.version is '乐观锁';

comment on column iam_password_login_fail_record.deleted is '删除标志';




-----
create table iam_password_security_config
(
    id                  bigint  not null
        primary key,
    max_pwd_error_count integer,
    error_lock_time     integer,
    require_change_pwd  boolean,
    update_frequency    integer,
    expire_remind       integer,
    same_as_login_name  boolean,
    recent_password     integer,
    creator             bigint,
    create_time         timestamp(6),
    last_modifier       bigint,
    last_modified_time  timestamp(6),
    version             integer not null,
    deleted             boolean not null
);

comment on table iam_password_security_config is '密码安全策略';

comment on column iam_password_security_config.id is '主键';

comment on column iam_password_security_config.max_pwd_error_count is '最大密码错误数';

comment on column iam_password_security_config.error_lock_time is '密码错误锁定时间(分钟)';

comment on column iam_password_security_config.require_change_pwd is '强制修改初始密码';

comment on column iam_password_security_config.update_frequency is '更新频率';

comment on column iam_password_security_config.expire_remind is '到期提醒(天数)';

comment on column iam_password_security_config.same_as_login_name is '与登录名相同';

comment on column iam_password_security_config.recent_password is '不能与近期多少次密码相同';

comment on column iam_password_security_config.creator is '创建者ID';

comment on column iam_password_security_config.create_time is '创建时间';

comment on column iam_password_security_config.last_modifier is '最后修者ID';

comment on column iam_password_security_config.last_modified_time is '最后修改时间';

comment on column iam_password_security_config.version is '乐观锁';

comment on column iam_password_security_config.deleted is '删除标志';



INSERT INTO iam_password_security_config (id, max_pwd_error_count, error_lock_time, require_change_pwd, update_frequency, expire_remind, same_as_login_name, recent_password, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1714844168393515008, 9999999, 10, true, 9999999, 14, false, 5, 1399985191002447872, '2023-10-19 11:21:25.000000', 1399985191002447872, '2023-11-29 12:10:21.000000', 2, false);

-----
create table iam_perm_menu
(
    id                    bigint           not null
        primary key,
    client_code           varchar(50)      not null,
    parent_id             bigint,
    title                 varchar(255)     not null,
    name                  varchar(255),
    perm_code             varchar(255),
    effect                boolean,
    icon                  varchar(255),
    hidden                boolean          not null,
    hide_children_in_menu boolean          not null,
    component             varchar(255),
    component_name        varchar(255),
    path                  varchar(255),
    redirect              varchar(255),
    sort_no               double precision not null,
    menu_type             integer          not null,
    leaf                  boolean,
    keep_alive            boolean,
    target_outside        boolean,
    hidden_header_content boolean,
    internal                 boolean          not null,
    remark                varchar(255),
    creator               bigint,
    create_time           timestamp(6),
    last_modifier         bigint,
    last_modified_time    timestamp(6),
    version               integer          not null,
    deleted               boolean          not null
);

comment on table iam_perm_menu is '权限_菜单';

comment on column iam_perm_menu.client_code is '终端code';

comment on column iam_perm_menu.parent_id is '父id';

comment on column iam_perm_menu.title is '菜单名称';

comment on column iam_perm_menu.name is '路由名称';

comment on column iam_perm_menu.perm_code is '菜单权限编码';

comment on column iam_perm_menu.effect is '是否有效';

comment on column iam_perm_menu.icon is '菜单图标';

comment on column iam_perm_menu.hidden is '是否隐藏';

comment on column iam_perm_menu.hide_children_in_menu is '是否隐藏子菜单';

comment on column iam_perm_menu.component is '组件';

comment on column iam_perm_menu.component_name is '组件名字';

comment on column iam_perm_menu.path is '路径';

comment on column iam_perm_menu.redirect is '菜单跳转地址(重定向)';

comment on column iam_perm_menu.sort_no is '菜单排序';

comment on column iam_perm_menu.menu_type is '类型（0：一级菜单；1：子菜单 ；2：按钮权限）';

comment on column iam_perm_menu.leaf is '是否叶子节点';

comment on column iam_perm_menu.keep_alive is '是否缓存页面';

comment on column iam_perm_menu.target_outside is '是否外部打开方式';

comment on column iam_perm_menu.hidden_header_content is '隐藏的标题内容';

comment on column iam_perm_menu.internal is '系统内置菜单';

comment on column iam_perm_menu.remark is '描述';

comment on column iam_perm_menu.creator is '创建人';

comment on column iam_perm_menu.create_time is '创建时间';

comment on column iam_perm_menu.last_modifier is '最后修改人';

comment on column iam_perm_menu.last_modified_time is '最后修改时间';

comment on column iam_perm_menu.version is '版本';

comment on column iam_perm_menu.deleted is '0:未删除。1:已删除';



INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1580740450633101312, 'dax-pay', null, '系统管理', 'System', null, false, 'ant-design:setting-outlined', false, false, 'Layout', null, '/system', '/system1/client', -99999, 0, null, true, false, false, false, null, 1399985191002447872, '2022-10-14 10:01:07.000000', 1414143554414059520, '2022-10-18 15:32:09.000000', 4, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1580740637841666048, 'dax-pay', 1582253306356649984, '终端管理', 'ClientList', null, false, '', false, false, '/modules/system/client/ClientList.vue', null, '/system/client', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2022-10-14 10:01:51.000000', 1414143554414059520, '2022-10-18 14:13:27.000000', 5, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1580740758629232640, 'dax-pay', 1582253306356649984, '登录方式', 'LoginTypeList', null, false, '', false, false, '/modules/system/loginType/LoginTypeList.vue', null, '/system/loginType', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2022-10-14 10:02:20.000000', 1414143554414059520, '2022-10-18 14:13:40.000000', 5, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1580928436300337152, 'dax-pay', 1580740450633101312, '菜单管理', 'MenuList', null, false, '', false, false, '/modules/system/menu/MenuList.vue', null, '/system/menu', '', -99, 1, null, true, false, false, false, null, 1399985191002447872, '2022-10-14 22:28:06.000000', 1399985191002447872, '2022-10-14 22:28:32.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582249924602580992, 'dax-pay', 1580740450633101312, '权限管理', 'Permission', null, false, '', false, false, 'Layout', null, '/system/permission', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 13:59:13.000000', 1414143554414059520, '2022-10-18 13:59:13.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582253011803262976, 'dax-pay', 1580740450633101312, '用户信息', 'UserAuth', null, false, '', false, false, 'Layout', null, '/system/user', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 14:11:30.000000', 1414143554414059520, '2022-10-18 14:11:30.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582253152903843840, 'dax-pay', 1580740450633101312, '系统配置', 'SystemConfig', null, false, '', false, false, 'Layout', null, '/system/config', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 14:12:03.000000', 1414143554414059520, '2022-10-18 14:12:03.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582253306356649984, 'dax-pay', 1580740450633101312, '认证管理', 'Auth', null, false, '', false, false, 'Layout', null, '/system/auth', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 14:12:40.000000', 1414143554414059520, '2022-10-18 14:13:13.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582275875424129024, 'dax-pay', null, '系统监控', 'Monitor', null, false, 'ant-design:monitor-outlined', false, false, 'Layout', null, '/monitor', '', 0, 0, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 15:42:21.000000', 1414143554414059520, '2022-10-19 17:29:29.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582275984849326080, 'dax-pay', null, '通知管理', 'Notice', null, false, 'ant-design:message-outlined', false, false, 'Layout', null, '/notice', '', 0, 0, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 15:42:47.000000', 1414143554414059520, '2022-10-19 17:30:06.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582276092038959104, 'dax-pay', null, '第三方对接', 'Third', null, false, 'ant-design:api-twotone', false, false, 'Layout', null, '/third', '', 0, 0, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 15:43:12.000000', 1414143554414059520, '2022-10-19 17:32:04.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582276341792985088, 'dax-pay', null, '开发管理', 'Develop', null, false, 'ant-design:apartment-outlined', false, false, 'Layout', null, '/develop', '', 0, 0, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 15:44:12.000000', 1414143554414059520, '2022-10-19 15:24:22.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582277076421136384, 'dax-pay', 1582249924602580992, '角色管理', 'RoleList', null, false, '', false, false, '/modules/system/role/RoleList.vue', null, '/system/permission/role', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 15:47:07.000000', 1414143554414059520, '2022-10-18 15:59:37.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582301940364308480, 'dax-pay', 1582249924602580992, '请求权限', 'PermPathList', null, false, '', false, false, '/modules/system/path/PermPathList.vue', null, '/system/permission/path', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:25:55.000000', 1399985191002447872, '2023-11-29 13:57:56.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582302180999917568, 'dax-pay', 1582249924602580992, '数据角色', 'DataRoleList', null, false, '', false, false, '/modules/system/scope/DataRoleList.vue', null, '/system/permission/data', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:26:52.000000', 1399985191002447872, '2023-11-28 21:02:57.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582302542955769856, 'dax-pay', 1582253011803262976, '用户管理', 'UserList', null, false, '', false, false, '/modules/system/user/UserList.vue', null, '/system/user/info', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:28:19.000000', 1414143554414059520, '2022-10-18 17:28:19.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582302764129808384, 'dax-pay', 1582253011803262976, '部门管理', 'DeptList', null, false, '', false, false, '/modules/system/dept/DeptList.vue', null, '/system/user/dept', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:29:11.000000', 1414143554414059520, '2022-10-18 17:32:26.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582303143110340608, 'dax-pay', 1582253152903843840, '数据字典', 'DictList', null, false, '', false, false, '/modules/system/dict/DictList.vue', null, '/system/config/dict', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:30:42.000000', 1414143554414059520, '2022-10-18 17:30:42.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582303290070364160, 'dax-pay', 1582253152903843840, '定时任务', 'QuartzJobList', null, false, '', false, false, '/modules/baseapi/quartz/QuartzJobList.vue', null, '/system/config/quartz', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:31:17.000000', 1414143554414059520, '2023-08-09 15:50:46.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582303447428067328, 'dax-pay', 1582253152903843840, '系统参数', 'SystemParamList', null, false, '', false, false, '/modules/system/param/SystemParamList.vue', null, '/system/config/param', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-18 17:31:54.000000', 1414143554414059520, '2022-10-19 23:14:16.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1582632873244172288, 'dax-pay', 1582276341792985088, '文件管理', 'FileUploadList', null, false, '', false, false, '/modules/develop/file/FileUploadList.vue', null, '/develop/file', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-19 15:20:56.000000', 1414143554414059520, '2022-10-19 15:20:56.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583074308040925184, 'dax-pay', 1582275875424129024, '接口文档', 'ApiSwagger', null, false, '', false, false, '', null, 'http://127.0.0.1:9999/doc.html', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:35:02.000000', 1414143554414059520, '2022-11-23 13:59:09.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583075229563068416, 'dax-pay', 1582275875424129024, '审计日志', 'AuditLog', null, false, '', false, false, 'Layout', null, '/monitor/log', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:38:42.000000', 1414143554414059520, '2022-10-20 20:41:38.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583076217481043968, 'dax-pay', 1583075229563068416, '登录日志', 'LoginLogList', null, false, '', false, false, '/modules/monitor/login/LoginLogList.vue', null, '/monitor/log/login', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:42:37.000000', 1414143554414059520, '2022-10-20 20:43:36.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583076424935514112, 'dax-pay', 1583075229563068416, '操作日志', 'OperateLogList', null, false, '', false, false, '/modules/monitor/operate/OperateLogList.vue', null, '/monitor/log/operate', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:43:26.000000', 1414143554414059520, '2022-10-20 20:43:26.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583076670881112064, 'dax-pay', 1583075229563068416, '数据版本日志', 'DataVersionLogList', null, false, '', false, false, '/modules/monitor/data/DataVersionLogList.vue', null, '/monitor/log/data', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:44:25.000000', 1414143554414059520, '2022-10-20 20:44:25.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583076878956339200, 'dax-pay', 1582275875424129024, 'ELK日志', 'ELK', null, false, '', true, false, '', null, 'http://elk.dev.bootx.cn:5601/app/discover', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:45:15.000000', 1414143554414059520, '2023-08-12 19:26:12.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583077015434797056, 'dax-pay', 1582275875424129024, 'PlumeLog日志', 'PlumeLog', null, false, '', false, false, '', null, 'http://127.0.0.1:9999/plumelog/#/', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:45:47.000000', 1414143554414059520, '2022-10-20 20:45:47.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583077198772019200, 'dax-pay', 1582275875424129024, '系统信息', 'SystemInfoMonitor', null, false, '', false, false, '/modules/monitor/system/SystemInfoMonitor.vue', null, '/monitor/sysinfo', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:46:31.000000', 1414143554414059520, '2022-10-20 20:46:31.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1583077360827342848, 'dax-pay', 1582275875424129024, 'Redis监控', 'RedisInfoMonitor', null, false, '', false, false, '/modules/monitor/redis/RedisInfoMonitor.vue', null, '/monitor/redis', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-20 20:47:10.000000', 1414143554414059520, '2022-10-20 20:47:10.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584378294652051456, 'dax-pay', 1582275984849326080, '邮件配置', 'MailConfigList', null, false, '', false, false, '/modules/notice/mail/MailConfigList.vue', null, '/notice/notice', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 10:56:36.000000', 1414143554414059520, '2022-10-24 16:14:34.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584378497824137216, 'dax-pay', 1582275984849326080, '消息模板', 'MessageTemplateList', null, false, '', false, false, '/modules/notice/template/MessageTemplateList.vue', null, '/notice/template', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 10:57:25.000000', 1414143554414059520, '2022-10-25 22:14:14.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584378671266996224, 'dax-pay', 1582275984849326080, '站内信', 'SiteMessageList', null, false, '', false, false, '/modules/notice/site/sender/SiteMessageList.vue', null, '/notice/siteMessage', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 10:58:06.000000', 1414143554414059520, '2022-10-24 10:58:06.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584379602188574720, 'dax-pay', 1582276092038959104, '微信', 'WeChat', null, false, '', false, false, 'Layout', null, '/third/wechat', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:01:48.000000', 1414143554414059520, '2022-10-24 11:01:48.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584379704122744832, 'dax-pay', 1582276092038959104, '企业微信', 'WeCom', null, false, '', false, false, 'Layout', null, '/third/wecom', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:02:12.000000', 1414143554414059520, '2022-10-24 11:02:12.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584380087805091840, 'dax-pay', 1582276092038959104, '钉钉', 'DingTalk', null, false, '', false, false, 'Layout', null, '/third/dingtalk', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:03:44.000000', 1414143554414059520, '2022-10-24 11:03:44.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584380527829524480, 'dax-pay', 1584379602188574720, '消息模板', 'WechatTemplateList', null, false, '', false, false, '/modules/third/wechat/template/WechatTemplateList.vue', null, '/third/wechat/template', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:05:29.000000', 1414143554414059520, '2022-10-26 15:58:56.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584380679478779904, 'dax-pay', 1584379602188574720, '自定义菜单', 'WechatMenuList', null, false, '', false, false, '/modules/third/wechat/menu/WechatMenuList.vue', null, '/third/wechat/menu', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:06:05.000000', 1414143554414059520, '2022-10-27 10:15:24.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584380824308097024, 'dax-pay', 1584379602188574720, '素材管理', 'WechatMediaList', null, false, '', false, false, '/modules/third/wechat/media/WechatMediaList.vue', null, '/third/wechat/media', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:06:40.000000', 1414143554414059520, '2022-10-27 16:38:47.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584381134950834176, 'dax-pay', 1584379704122744832, '企微机器人', 'WeComRobotConfigList', null, false, '', false, false, '/modules/third/wecom/robot/WecomRobotConfigList.vue', null, '/third/wecom/robot', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:07:54.000000', 1414143554414059520, '2022-11-12 20:58:25.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1584381322184564736, 'dax-pay', 1584380087805091840, '钉钉机器人', 'DingRobotConfigList', null, false, '', false, false, '/modules/third/dingtalk/robot/DingRobotConfigList.vue', null, '/third/dingTalk/robot', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2022-10-24 11:08:38.000000', 1414143554414059520, '2022-11-12 20:58:37.000000', 6, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1599337250200440832, 'dax-pay', null, '关于', '', null, false, 'ant-design:info-circle-outlined', false, false, '', null, '/about/index', '', 99, 0, null, true, false, false, false, null, 1414143554414059520, '2022-12-04 17:38:09.000000', 1399985191002447872, '2024-01-14 23:09:56.000000', 6, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1623494586215579648, 'dax-pay', 1552207982510706688, '行政区划', 'ChinaRegion', null, false, '', true, false, 'develop/region/ChinaRegionList', null, '/develop/region', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2023-02-09 09:30:47.000000', 1399985191002447872, '2023-02-09 17:50:05.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1631946120891707392, 'dax-pay', 1552207982510706688, '可视化大屏', 'ProjectInfoList', null, false, '', false, false, 'develop/report/ProjectInfoList', null, '/develop/report', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2023-03-04 17:14:10.000000', 1399985191002447872, '2023-03-04 17:14:10.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1687369862646558720, 'dax-pay', 1582275984849326080, '短信管理', 'Sms', null, false, '', false, false, 'Layout', null, '/notice/sms', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2023-08-04 15:48:20.000000', 1414143554414059520, '2023-08-04 15:48:32.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1687370142234669056, 'dax-pay', 1687369862646558720, '短信配置', 'SmsChannelConfigList', null, false, '', false, false, '/modules/notice/sms/config/SmsChannelConfigList', null, '/notice/sms/config', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2023-08-04 15:49:26.000000', 1414143554414059520, '2023-08-04 15:49:26.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1687370277496778752, 'dax-pay', 1687369862646558720, '短信模板', 'SmsTemplateList', null, false, '', false, false, '/modules/notice/sms/template/SmsTemplateList', null, '/notice/sms/template', '', 0, 1, null, true, false, false, false, null, 1414143554414059520, '2023-08-04 15:49:59.000000', 1414143554414059520, '2023-08-04 15:50:38.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1713931084759293952, 'dax-pay', 1582253306356649984, '密码安全', 'PassowrdSecurity', null, false, '', false, false, '/modules/system/security/password/PasswordSecurityConfig.vue', null, '/system/config/passowrd', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2023-10-16 22:53:09.000000', 1399985191002447872, '2023-10-16 22:58:59.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1733829906427682816, 'dax-pay', 1582275875424129024, '在线用户', 'OnlineUserList', null, false, '', false, false, '/modules/monitor/user/online/OnlineUserList', null, '/monitor/user/online', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2023-12-10 20:43:57.000000', 1399985191002447872, '2023-12-10 20:43:57.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744271715476684800, 'dax-pay', null, '支付配置', 'PayConfig', null, false, 'ant-design:setting-filled', false, false, 'Layout', null, '/pay/config', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-01-08 16:15:59.000000', 1399985191002447872, '2024-01-09 15:38:06.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744276101384880128, 'dax-pay', 1744271715476684800, '支付通道', 'PayChannelConfigList', null, false, '', false, false, 'payment/system/channel/PayChannelConfigList', null, '/pay/config/channel', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-08 16:33:24.000000', 1399985191002447872, '2024-01-18 14:49:15.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744372631231995904, 'dax-pay', 1744271715476684800, '支付方式', 'PayMethodInfoList', null, false, '', false, false, 'payment/system/method/PayMethodInfoList', null, '/pay/config/method', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-08 22:56:59.000000', 1399985191002447872, '2024-05-01 22:53:13.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744624886658318336, 'dax-pay', 1744271715476684800, '支付接口', 'PayApiConfigList', null, false, '', false, false, 'payment/system/api/PayApiConfigList', null, '/pay/config/api', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-09 15:39:21.000000', 1399985191002447872, '2024-01-10 11:52:20.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744642856348520448, 'dax-pay', null, '订单管理', 'PayOrder', null, false, 'ant-design:wallet-outlined', false, false, 'Layout', null, '/pay/order', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-01-09 16:50:46.000000', 1399985191002447872, '2024-01-09 16:53:35.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744643265142165504, 'dax-pay', null, '数据记录', 'PayRecord', null, false, 'ant-design:profile-outlined', false, false, 'Layout', null, '/pay/record', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-01-09 16:52:23.000000', 1399985191002447872, '2024-01-09 16:53:50.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1744930046228017152, 'dax-pay', 1744643265142165504, '回调记录', 'PayCallbackRecordList', null, false, '', false, false, 'payment/record/callback/PayCallbackRecordList', null, '/pay/record/callback', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-10 11:51:57.000000', 1399985191002447872, '2024-05-01 23:15:23.000000', 10, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745126072389963776, 'dax-pay', 1744643265142165504, '关闭记录', 'PayCloseRecordList', null, false, '', false, false, 'payment/record/close/PayCloseRecordList', null, '/pay/record/close', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-11 00:50:53.000000', 1399985191002447872, '2024-01-11 00:50:53.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745136155962347520, 'dax-pay', 1744643265142165504, '修复记录', 'PayRepairRecordList', null, false, '', false, false, 'payment/record/repair/PayRepairRecordList', null, '/pay/record/repair', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-11 01:30:57.000000', 1399985191002447872, '2024-01-11 01:30:57.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745143528663781376, 'dax-pay', 1744643265142165504, '同步记录', 'PaySyncRecordList', null, false, '', false, false, 'payment/record/sync/PaySyncRecordList', null, '/pay/record/sync', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-11 02:00:15.000000', 1399985191002447872, '2024-01-11 02:00:33.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745457623493496832, 'dax-pay', 1744642856348520448, '支付订单', 'PayOrderList', null, false, '', false, false, 'payment/order/pay/PayOrderList', null, '/pay/order/pay', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-11 22:48:21.000000', 1399985191002447872, '2024-01-11 22:48:21.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745457746529210368, 'dax-pay', 1744642856348520448, '退款订单', 'RefundOrderList', null, false, '', false, false, 'payment/order/refund/RefundOrderList.vue', null, '/pay/order/refund', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-11 22:48:50.000000', 1399985191002447872, '2024-01-21 22:50:29.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1745822093382230016, 'dax-pay', 1744271715476684800, '通道配置', 'ChannelPayConfigList', null, false, '', false, false, 'payment/channel/config/ChannelPayConfigList', null, '/pay/config/channelpay', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-12 22:56:38.000000', 1399985191002447872, '2024-02-06 14:32:08.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1746194891925561344, 'dax-pay', 1744271715476684800, '平台配置', 'PayPlatformConfig', null, false, '', false, false, 'payment/system/platform/PayPlatformConfig', null, '/pay/config/platform', '', -1, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-13 23:38:00.000000', 1399985191002447872, '2024-01-13 23:38:00.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1749262518385082368, 'dax-pay', 1786808188825194496, '对账单', 'ReconcileOrderList', null, false, '', false, false, 'payment/reconcile/order/ReconcileOrderList.vue', null, '/pay/reconcile/order', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-01-22 10:47:39.000000', 1399985191002447872, '2024-05-05 17:09:14.000000', 9, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1759768820429352960, 'dax-pay', null, '演示模块', '', null, false, 'ant-design:crown-outlined', false, false, 'Layout', null, '/pay/demo', '', 9, 0, null, true, false, false, false, null, 1399985191002447872, '2024-02-20 10:35:56.000000', 1399985191002447872, '2024-02-20 10:35:56.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1759769092698402816, 'dax-pay', 1759768820429352960, '收银台演示', '', null, false, '', false, false, '', null, 'outside:///cashier', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-02-20 10:37:01.000000', 1399985191002447872, '2024-02-20 10:37:01.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1761429304959528960, 'dax-pay', null, '支付通知', 'PayNotic', null, false, 'ant-design:clock-circle-outlined', false, false, 'Layout', null, '/pay/notice', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-02-25 00:34:07.000000', 1399985191002447872, '2024-05-08 15:31:04.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1761429682618855424, 'dax-pay', 1761429304959528960, '通知记录', 'ClientNoticeTaskList', null, false, '', false, false, 'payment/task/notice/ClientNoticeTaskList', null, '/pay/notice/record', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-02-25 00:35:37.000000', 1399985191002447872, '2024-05-08 15:30:56.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1775089099078553600, 'dax-pay', null, '分账管理', 'Allocation', null, false, 'ant-design:sliders-twotone', false, false, 'Layout', null, '/pay/allocation', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-04-02 17:13:15.000000', 1399985191002447872, '2024-04-02 17:13:15.000000', 0, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1775089820368818176, 'dax-pay', 1775089099078553600, '分账接收者', 'AllocationReceiverList', null, false, '', false, false, 'payment/allocation/receiver/AllocationReceiverList', null, '/pay/allocation/receiver', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-04-02 17:16:07.000000', 1399985191002447872, '2024-04-02 17:23:13.000000', 2, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1775091561835450368, 'dax-pay', 1775089099078553600, '分账组', 'AllocationGroupList', null, false, '', false, false, 'payment/allocation/group/AllocationGroupList', null, '/pay/allocation/group', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-04-02 17:23:03.000000', 1399985191002447872, '2024-04-02 17:23:27.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1777688382748700672, 'dax-pay', 1775089099078553600, '分账订单', 'AllocationOrderList', null, false, '', false, false, 'payment/allocation/order/AllocationOrderList', null, '/pay/allocation/order', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-04-09 21:21:53.000000', 1399985191002447872, '2024-05-05 17:06:17.000000', 3, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1786808188825194496, 'dax-pay', null, '对账管理', 'Reconcile', null, false, 'ant-design:arrows-alt-outlined', false, false, 'Layout', null, '/pay/reconcile', '', 0, 0, null, true, false, false, false, null, 1399985191002447872, '2024-05-05 01:20:44.000000', 1399985191002447872, '2024-05-05 01:20:54.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1786810890951020544, 'dax-pay', 1786808188825194496, '对账差异', 'ReconcileDiffList', null, false, '', false, false, 'payment/reconcile/diff/ReconcileDiffList.vue', null, '/pay/reconcile/diff', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-05-05 01:31:28.000000', 1399985191002447872, '2024-05-05 17:08:38.000000', 1, false);
INSERT INTO iam_perm_menu (id, client_code, parent_id, title, name, perm_code, effect, icon, hidden, hide_children_in_menu, component, component_name, path, redirect, sort_no, menu_type, leaf, keep_alive, target_outside, hidden_header_content, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1786811341285052416, 'dax-pay', 1786808188825194496, '外部交易明细', 'ReconcileDetailList', null, false, '', true, false, 'payment/reconcile/detail/ReconcileDetailList.vue', null, '/pay/reconcile/detail', '', 0, 1, null, true, false, false, false, null, 1399985191002447872, '2024-05-05 01:33:16.000000', 1399985191002447872, '2024-05-05 20:24:24.000000', 2, false);

-----
create table iam_perm_path
(
    id                 bigint       not null
        primary key,
    code               varchar(200) not null,
    name               varchar(255),
    request_type       varchar(10)  not null,
    path               varchar(255) not null,
    group_name         varchar(255),
    enable             boolean      not null,
    generate           boolean      not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean      not null,
    version            integer      not null
);

comment on table iam_perm_path is '权限_请求';

comment on column iam_perm_path.code is '权限标识';

comment on column iam_perm_path.name is '权限名称';

comment on column iam_perm_path.request_type is '请求类型';

comment on column iam_perm_path.path is '请求路径';

comment on column iam_perm_path.group_name is '分组名称';

comment on column iam_perm_path.enable is '启用状态';

comment on column iam_perm_path.generate is '是否通过系统生成的权限';

comment on column iam_perm_path.remark is '描述';



INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556416, 'UserInfoController#existsUsername', '账号是否被使用', 'GET', '/user/existsUsername', '用户管理', false, true, '用户管理 账号是否被使用', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556417, 'FIleUpLoadController#page', '分页', 'GET', '/file/page', '文件上传', false, true, '文件上传 分页', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556418, 'AllocationOrderController#retryAllocation', '分账重试', 'POST', '/order/allocation/retry', '分账订单控制器', false, true, '分账订单控制器 分账重试', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556419, 'WeChatPayConfigController#toBase64', '将文件转换成base64', 'POST', '/wechat/pay/config/toBase64', '微信支付配置', false, true, '微信支付配置 将文件转换成base64', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556420, 'AllocationReceiverController#removeByGateway', '从三方支付系统中删除', 'POST', '/allocation/receiver/removeByGateway', '分账接收方控制器', false, true, '分账接收方控制器 从三方支付系统中删除', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556421, 'UserThirdController#unbind', '解绑第三方账号', 'POST', '/user/third/unbind', '用户三方登录管理', false, true, '用户三方登录管理 解绑第三方账号', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556422, 'ChinaRegionController#findAllProvince', '获取一级行政区', 'GET', '/china/region/findAllProvince', '中国行政区划', false, true, '中国行政区划 获取一级行政区', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556423, 'AllocationGroupController#unbindReceivers', '批量取消绑定接收者', 'POST', '/allocation/group/unbindReceivers', '分账组', false, true, '分账组 批量取消绑定接收者', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556424, 'DataRoleController#findUsersByDataRoleId', '获取关联的用户列表', 'GET', '/data/role/findUsersByDataRoleId', '数据角色配置', false, true, '数据角色配置 获取关联的用户列表', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556425, 'AggregateController#getInfo', '获取聚合支付信息', 'GET', '/demo/aggregate/getInfo', '聚合支付', false, true, '聚合支付 获取聚合支付信息', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556426, 'UniQueryController#queryRefundOrder', '退款订单查询接口', 'POST', '/uni/query/refundOrder', '统一查询接口', false, true, '统一查询接口 退款订单查询接口', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556427, 'SmsTemplateController#update', '修改', 'POST', '/sms/template/update', '短信模板配置', false, true, '短信模板配置 修改', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556428, 'AllocationReceiverController#findById', '查询详情', 'GET', '/allocation/receiver/findById', '分账接收方控制器', false, true, '分账接收方控制器 查询详情', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556429, 'PayMethodInfoController#update', '更新', 'POST', '/pay/method/info/update', '支付方式管理', false, true, '支付方式管理 更新', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556430, 'CashierController#getUniCashierUrl', '获取手机收银台链接', 'GET', '/demo/cashier/getUniCashierUrl', '结算台演示', false, true, '结算台演示 获取手机收银台链接', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556431, 'ReconcileOrderController#pageDiff', '对账差异分页', 'GET', '/order/reconcile/diff/page', '对账控制器', false, true, '对账控制器 对账差异分页', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556432, 'OnlineUserController#page', '分页', 'GET', '/online/user/page', '在线用户', false, true, '在线用户 分页', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556433, 'UserInfoController#existsPhone', '手机号是否被使用(不包含自己)', 'GET', '/user/existsPhoneNotId', '用户管理', false, true, '用户管理 手机号是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556434, 'DataRoleController#add', '添加', 'POST', '/data/role/add', '数据角色配置', false, true, '数据角色配置 添加', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556435, 'PlatformConfigController#getConfig', '获取平台配置', 'GET', '/platform/config/getConfig', '支付平台配置控制器', false, true, '支付平台配置控制器 获取平台配置', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556436, 'SmsTemplateController#delete', '删除', 'DELETE', '/sms/template/delete', '短信模板配置', false, true, '短信模板配置 删除', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556437, 'DataRoleController#findById', '获取', 'GET', '/data/role/findById', '数据角色配置', false, true, '数据角色配置 获取', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556438, 'DingRobotConfigController#delete', '删除', 'DELETE', '/ding/robot/config/delete', '钉钉机器人配置', false, true, '钉钉机器人配置 删除', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556439, 'UserDataRoleController#saveAssign', '给用户分配数据角色', 'POST', '/user/data/role/saveAssign', '用户数据角色配置', false, true, '用户数据角色配置 给用户分配数据角色', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556440, 'SiteMessageController#read', '标为已读', 'POST', '/site/message/read', '站内信', false, true, '站内信 标为已读', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556441, 'SystemParamController#findByParamKey', '根据键名获取键值', 'GET', '/system/param/findByParamKey', '系统参数', false, true, '系统参数 根据键名获取键值', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556442, 'UserInfoController#bindEmail', '绑定邮箱', 'POST', '/user/bindEmail', '用户管理', false, true, '用户管理 绑定邮箱', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556443, 'RoleMenuController#save', '保存请求权限关系', 'POST', '/role/menu/save', '角色菜单权限关系', false, true, '角色菜单权限关系 保存请求权限关系', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556444, 'RoleController#findAll', '查询所有的角色', 'GET', '/role/findAll', '角色管理', false, true, '角色管理 查询所有的角色', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556445, 'SmsChannelConfigController#findById', '通过ID查询', 'GET', '/sms/config/findById', '短信渠道配置', false, true, '短信渠道配置 通过ID查询', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556446, 'PermPathController#add', '添加权限', 'POST', '/perm/path/add', '请求权限管理', false, true, '请求权限管理 添加权限', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556447, 'AuthAssistController#sendSmsCaptcha', '发送短信验证码', 'POST', '/auth/sendSmsCaptcha', '认证支撑接口', false, true, '认证支撑接口 发送短信验证码', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556448, 'CockpitReportController#getRefundAmount', '退款金额(分)', 'GET', '/report/cockpit/getRefundAmount', '驾驶舱接口', false, true, '驾驶舱接口 退款金额(分)', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556449, 'PermMenuController#existsByPermCode', '编码是否被使用', 'GET', '/perm/menu/existsByPermCode', '菜单和权限码', false, true, '菜单和权限码 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556450, 'WeChatTemplateController#findById', '通过ID查询', 'GET', '/wechat/template/findById', '微信模板消息', false, true, '微信模板消息 通过ID查询', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556451, 'UserInfoController#updateEmail', '修改邮箱', 'POST', '/user/updateEmail', '用户管理', false, true, '用户管理 修改邮箱', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556452, 'AggregateController#createUrl', '创建聚合支付码', 'POST', '/demo/aggregate/createUrl', '聚合支付', false, true, '聚合支付 创建聚合支付码', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556453, 'MessageTemplateController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/message/template/existsByCodeNotId', '消息模板', false, true, '消息模板 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556454, 'DingRobotConfigController#update', '修改机器人配置', 'POST', '/ding/robot/config/update', '钉钉机器人配置', false, true, '钉钉机器人配置 修改机器人配置', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556455, 'WeChatPortalController#auth', 'auth', 'GET', '/wechat/portal', '微信接入入口', false, true, '微信接入入口 auth', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556456, 'SystemParamController#add', '添加', 'POST', '/system/param/add', '系统参数', false, true, '系统参数 添加', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556457, 'DingRobotConfigController#page', '分页', 'GET', '/ding/robot/config/page', '钉钉机器人配置', false, true, '钉钉机器人配置 分页', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556458, 'QuartzJobController#findById', '单条', 'GET', '/quartz/findById', '定时任务', false, true, '定时任务 单条', 1399985191002447872, '2024-05-13 19:16:54.069000', 1399985191002447872, '2024-05-13 19:16:54.069000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117562556459, 'RoleController#existsByName', '名称是否被使用', 'GET', '/role/existsByName', '角色管理', false, true, '角色管理 名称是否被使用', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750720, 'WeChatMenuController#findById', '通过ID查询', 'GET', '/wechat/menu/findById', '微信菜单管理', false, true, '微信菜单管理 通过ID查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750721, 'UserInfoController#existsEmail', '邮箱是否被使用(不包含自己)', 'GET', '/user/existsEmailNotId', '用户管理', false, true, '用户管理 邮箱是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750722, 'UniPayController#syncPay', '支付同步接口', 'POST', '/unipay/syncPay', '统一支付接口', false, true, '统一支付接口 支付同步接口', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750723, 'PermPathController#page', '权限分页', 'GET', '/perm/path/page', '请求权限管理', false, true, '请求权限管理 权限分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750724, 'WeChatPortalController#post', 'post', 'POST', '/wechat/portal', '微信接入入口', false, true, '微信接入入口 post', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750725, 'DeptController#findById', '获取', 'GET', '/dept/findById', '部门管理', false, true, '部门管理 获取', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750726, 'PayOrderController#allocation', '发起分账', 'POST', '/order/pay/allocation', '支付订单控制器', false, true, '支付订单控制器 发起分账', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750727, 'DictionaryController#add', '添加', 'POST', '/dict/add', '字典', false, true, '字典 添加', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750728, 'RefundOrderController#getTotalAmount', '查询金额汇总', 'GET', '/order/refund/getTotalAmount', '支付退款控制器', false, true, '支付退款控制器 查询金额汇总', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750729, 'UserInfoController#getUserBaseInfo', '查询用户基础信息', 'GET', '/user/getUserBaseInfo', '用户管理', false, true, '用户管理 查询用户基础信息', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750730, 'CockpitReportController#getRefundOrderCount', '退款订单数量', 'GET', '/report/cockpit/getRefundOrderCount', '驾驶舱接口', false, true, '驾驶舱接口 退款订单数量', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750731, 'UserinternalController#update', '修改用户', 'POST', '/user/internal/update', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 修改用户', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750732, 'WeChatMenuController#importMenu', '导入微信自定义菜单到系统中', 'POST', '/wechat/menu/importMenu', '微信菜单管理', false, true, '微信菜单管理 导入微信自定义菜单到系统中', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750733, 'WalletConfigController#update', '更新', 'POST', '/wallet/config/update', '钱包配置', false, true, '钱包配置 更新', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750734, 'DeptController#deleteAndChildren', '强制级联删除', 'DELETE', '/dept/deleteAndChildren', '部门管理', false, true, '部门管理 强制级联删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750735, 'PermPathController#deleteBatch', '批量删除', 'DELETE', '/perm/path/deleteBatch', '请求权限管理', false, true, '请求权限管理 批量删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750736, 'PayGatewayNoticeController#wechatPayNotice', '微信消息通知', 'POST', '/gateway/notice/wechat', '三方支付网关消息通知', false, true, '三方支付网关消息通知 微信消息通知', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750737, 'DataRoleController#saveDeptAssign', '保存关联部门', 'POST', '/data/role/saveDeptAssign', '数据角色配置', false, true, '数据角色配置 保存关联部门', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750738, 'PayCallbackController#wechatPayNotify', '微信支付信息回调', 'POST', '/callback/pay/wechat', '支付通道信息回调', false, true, '支付通道信息回调 微信支付信息回调', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750739, 'WeChatMediaController#uploadFile', '上传素材', 'POST', '/wechat/media/uploadFile', '微信素材管理', false, true, '微信素材管理 上传素材', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750740, 'AllocationReceiverController#registerByGateway', '同步到三方支付系统中', 'POST', '/allocation/receiver/registerByGateway', '分账接收方控制器', false, true, '分账接收方控制器 同步到三方支付系统中', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750741, 'WalletController#recharge', '充值', 'POST', '/wallet/recharge', '钱包管理', false, true, '钱包管理 充值', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750742, 'UniPayAssistController#getWxAccessToken', '获取微信AccessToken', 'POST', '/unipay/assist/getWxAccessToken', '支付支撑接口', false, true, '支付支撑接口 获取微信AccessToken', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750743, 'FIleUpLoadController#getFileDownloadUrl', '获取文件下载地址(流量会经过后端)', 'GET', '/file/getFileDownloadUrl', '文件上传', false, true, '文件上传 获取文件下载地址(流量会经过后端)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750744, 'AllocationGroupController#clearDefault', '清除默认分账组', 'POST', '/allocation/group/clearDefault', '分账组', false, true, '分账组 清除默认分账组', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750745, 'DictionaryController#findById', '根据id获取', 'GET', '/dict/findById', '字典', false, true, '字典 根据id获取', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750746, 'ReconcileOrderController#compare', '手动触发交易对账单比对', 'POST', '/order/reconcile/compare', '对账控制器', false, true, '对账控制器 手动触发交易对账单比对', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750747, 'TestController#lock1', '锁测试1', 'GET', '/test/lock1', '测试', false, true, '测试 锁测试1', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750748, 'UserInfoController#updatePhone', '修改手机号', 'POST', '/user/updatePhone', '用户管理', false, true, '用户管理 修改手机号', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750749, 'UserInfoController#getLoginAfterUserInfo', '登录后获取用户信息', 'GET', '/user/getLoginAfterUserInfo', '用户管理', false, true, '用户管理 登录后获取用户信息', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750750, 'ClientController#delete', '删除', 'DELETE', '/client/delete', '认证终端', false, true, '认证终端 删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750751, 'PayChannelConfigController#findAll', '查询全部', 'GET', '/pay/channel/config/findAll', '支付通道信息', false, true, '支付通道信息 查询全部', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750752, 'SmsChannelConfigController#add', '添加', 'POST', '/sms/config/add', '短信渠道配置', false, true, '短信渠道配置 添加', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750753, 'PayCloseRecordController#page', '分页查询', 'GET', '/record/close/page', '支付订单关闭记录', false, true, '支付订单关闭记录 分页查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750754, 'UserInfoController#bindPhone', '绑定手机', 'POST', '/user/bindPhone', '用户管理', false, true, '用户管理 绑定手机', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750755, 'DictionaryItemController#update', '修改字典项（返回字典项对象）', 'POST', '/dict/item/update', '字典项', false, true, '字典项 修改字典项（返回字典项对象）', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750756, 'WeChatMediaController#pageFile', '非图文素材分页', 'GET', '/wechat/media/pageFile', '微信素材管理', false, true, '微信素材管理 非图文素材分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750757, 'ChinaRegionController#findAllProvinceAndCityAndArea', ' 获取省市区县三级联动列表', 'GET', '/china/region/findAllProvinceAndCityAndArea', '中国行政区划', false, true, '中国行政区划  获取省市区县三级联动列表', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750758, 'WalletConfigController#findPayWays', '支付宝支持支付方式', 'GET', '/wallet/config/findPayWays', '钱包配置', false, true, '钱包配置 支付宝支持支付方式', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750759, 'AllocationGroupController#bindReceivers', '批量绑定接收者', 'POST', '/allocation/group/bindReceivers', '分账组', false, true, '分账组 批量绑定接收者', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750760, 'ReconcileOrderController#upload', '手动上传交易对账单文件', 'POST', '/order/reconcile/upload', '对账控制器', false, true, '对账控制器 手动上传交易对账单文件', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750761, 'SystemParamController#update', '更新', 'POST', '/system/param/update', '系统参数', false, true, '系统参数 更新', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750762, 'PayCallbackRecordController#page', '分页查询', 'GET', '/record/callback/page', '支付回调信息记录', false, true, '支付回调信息记录 分页查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750763, 'RefundOrderController#findByRefundNo', '查询退款订单详情', 'GET', '/order/refund/findByOrderNo', '支付退款控制器', false, true, '支付退款控制器 查询退款订单详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750764, 'ClientController#page', '分页查询', 'GET', '/client/page', '认证终端', false, true, '认证终端 分页查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750765, 'UserAssistController#sendPhoneForgetCaptcha', '发送找回密码手机验证码', 'POST', '/user/sendPhoneForgetCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 发送找回密码手机验证码', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750766, 'CashierController#wxAuthCallback', '微信授权回调页面', 'GET', '/demo/cashier/wxAuthCallback', '结算台演示', false, true, '结算台演示 微信授权回调页面', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750767, 'UserInfoController#getUserSecurityInfo', '查询用户安全信息', 'GET', '/user/getUserSecurityInfo', '用户管理', false, true, '用户管理 查询用户安全信息', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750768, 'PaySyncRecordController#findById', '查询单条', 'GET', '/record/sync/findById', '支付同步记录控制器', false, true, '支付同步记录控制器 查询单条', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750769, 'DataRoleController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/data/role/existsByCodeNotId', '数据角色配置', false, true, '数据角色配置 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750770, 'DataRoleController#existsByName', '名称是否被使用(不包含自己)', 'GET', '/data/role/existsByNameNotId', '数据角色配置', false, true, '数据角色配置 名称是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750771, 'UniQueryController#queryPayOrder', '支付订单查询接口', 'POST', '/uni/query/payOrder', '统一查询接口', false, true, '统一查询接口 支付订单查询接口', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750772, 'QuartzJobController#delete', '删除', 'DELETE', '/quartz/delete', '定时任务', false, true, '定时任务 删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750773, 'PermMenuController#add', '添加菜单权限', 'POST', '/perm/menu/add', '菜单和权限码', false, true, '菜单和权限码 添加菜单权限', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750774, 'ChinaRegionController#findAllProvinceAndCity', '获取省市二级联动列表', 'GET', '/china/region/findAllProvinceAndCity', '中国行政区划', false, true, '中国行政区划 获取省市二级联动列表', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750775, 'WeChatMenuController#page', '分页查询', 'GET', '/wechat/menu/page', '微信菜单管理', false, true, '微信菜单管理 分页查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750776, 'PermPathController#findById', '获取详情', 'GET', '/perm/path/findById', '请求权限管理', false, true, '请求权限管理 获取详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750777, 'QuartzJobController#stop', '停止', 'POST', '/quartz/stop', '定时任务', false, true, '定时任务 停止', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750778, 'UserAssistController#sendEmailChangeCaptcha', '发送更改/绑定邮箱验证码', 'POST', '/user/sendEmailChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 发送更改/绑定邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750779, 'DictionaryController#page', '分页', 'GET', '/dict/page', '字典', false, true, '字典 分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750780, 'OnlineUserController#kickOut', '踢出用户', 'GET', '/online/user/kickOut', '在线用户', false, true, '在线用户 踢出用户', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750781, 'UniPayController#refund', '统一退款接口', 'POST', '/unipay/refund', '统一支付接口', false, true, '统一支付接口 统一退款接口', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750782, 'PayCallbackController#unionPayNotify', '云闪付支付信息回调', 'POST', '/callback/pay/union', '支付通道信息回调', false, true, '支付通道信息回调 云闪付支付信息回调', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750783, 'ReconcileOrderController#downLocalCsv', '下载系统对账单(CSV格式)', 'GET', '/order/reconcile/downLocalCsv', '对账控制器', false, true, '对账控制器 下载系统对账单(CSV格式)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750784, 'DataRoleController#delete', '删除', 'DELETE', '/data/role/delete', '数据角色配置', false, true, '数据角色配置 删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750785, 'DataRoleController#deleteUserAssigns', '批量删除数据角色关联用户', 'DELETE', '/data/role/deleteUserAssigns', '数据角色配置', false, true, '数据角色配置 批量删除数据角色关联用户', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750786, 'ClientController#findById', '通过ID查询', 'GET', '/client/findById', '认证终端', false, true, '认证终端 通过ID查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750787, 'ClientController#existsByCode', '编码是否被使用', 'GET', '/client/existsByCode', '认证终端', false, true, '认证终端 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750788, 'FIleUpLoadController#local', '上传', 'POST', '/file/upload', '文件上传', false, true, '文件上传 上传', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750789, 'AllocationGroupController#findById', '查询详情', 'GET', '/allocation/group/findById', '分账组', false, true, '分账组 查询详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750790, 'PermMenuController#menuAndPermCodeTree', '获取菜单和权限码树', 'GET', '/perm/menu/menuAndPermCodeTree', '菜单和权限码', false, true, '菜单和权限码 获取菜单和权限码树', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750791, 'WeChatArticleController#page', '分页', 'GET', '/wechat/article/page', '微信文章管理', false, true, '微信文章管理 分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750792, 'UserInfoController#updatePassword', '修改密码', 'POST', '/user/updatePassword', '用户管理', false, true, '用户管理 修改密码', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750793, 'MessageTemplateController#existsByCode', '编码是否被使用', 'GET', '/message/template/existsByCode', '消息模板', false, true, '消息模板 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750794, 'LoginTypeController#superPage', '超级查询', 'POST', '/loginType/superPage', '登录方式管理', false, true, '登录方式管理 超级查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750795, 'WalletController#page', '钱包分页', 'GET', '/wallet/page', '钱包管理', false, true, '钱包管理 钱包分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750796, 'UserinternalController#getUserInfoWhole', '查询用户详情', 'GET', '/user/internal/getUserInfoWhole', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 查询用户详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750797, 'AlipayConfigController#update', '更新', 'POST', '/alipay/config/update', '支付宝配置', false, true, '支付宝配置 更新', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750798, 'UniPayController#syncRefund', '退款同步接口', 'POST', '/unipay/syncRefund', '统一支付接口', false, true, '统一支付接口 退款同步接口', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750799, 'QuartzJobController#add', '添加', 'POST', '/quartz/add', '定时任务', false, true, '定时任务 添加', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750800, 'OperateLogController#page', '分页', 'GET', '/log/operate/page', '操作日志', false, true, '操作日志 分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750801, 'ClientNoticeTaskController#findRecordById', '查询单条', 'GET', '/task/notice/record/findById', '客户系统通知任务', false, true, '客户系统通知任务 查询单条', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750802, 'OpenApiWebMvcResource#openapiYaml', 'openapiYaml', 'GET', '/v3/api-docs.yaml', 'OpenApiWebMvcResource', false, true, 'OpenApiWebMvcResource openapiYaml', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750803, 'PayOrderController#findById', '查询订单详情', 'GET', '/order/pay/findById', '支付订单控制器', false, true, '支付订单控制器 查询订单详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750804, 'WeChatQrLoginController#getStatus', '获取扫码状态', 'GET', '/token/wechat/qr/getStatus', '微信扫码登录', false, true, '微信扫码登录 获取扫码状态', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750805, 'DeptController#tree', '树状展示', 'GET', '/dept/tree', '部门管理', false, true, '部门管理 树状展示', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750806, 'WeChatMenuController#publish', '发布菜单', 'POST', '/wechat/menu/publish', '微信菜单管理', false, true, '微信菜单管理 发布菜单', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750807, 'AllocationGroupController#findReceiversByGroups', '查询分账接收方信息', 'GET', '/allocation/group/findReceiversByGroups', '分账组', false, true, '分账组 查询分账接收方信息', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750808, 'LoginLogController#deleteByDay', '清除指定天数之前的日志', 'DELETE', '/log/login/deleteByDay', '登录日志', false, true, '登录日志 清除指定天数之前的日志', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750809, 'PayOrderController#syncById', '同步支付状态', 'POST', '/order/pay/syncByOrderNo', '支付订单控制器', false, true, '支付订单控制器 同步支付状态', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750810, 'WeChatTemplateController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/wechat/template/existsByCodeNotId', '微信模板消息', false, true, '微信模板消息 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750811, 'WeChatMenuController#add', '添加', 'POST', '/wechat/menu/add', '微信菜单管理', false, true, '微信菜单管理 添加', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750812, 'WeChatMenuController#clearMenu', '清空微信自定义菜单', 'POST', '/wechat/menu/clearMenu', '微信菜单管理', false, true, '微信菜单管理 清空微信自定义菜单', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750813, 'QuartzJobLogController#page', '分页', 'GET', '/quartz/log/page', '定时任务执行日志', false, true, '定时任务执行日志 分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750814, 'UserInfoController#updateBaseInfo', '修改用户基础信息', 'POST', '/user/updateBaseInfo', '用户管理', false, true, '用户管理 修改用户基础信息', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750815, 'PayCallbackController#aliPayNotify', '支付宝信息回调', 'POST', '/callback/pay/alipay', '支付通道信息回调', false, true, '支付通道信息回调 支付宝信息回调', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750816, 'DataRoleController#existsByCode', '编码是否被使用', 'GET', '/data/role/existsByCode', '数据角色配置', false, true, '数据角色配置 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750817, 'MessageTemplateController#delete', '删除', 'DELETE', '/message/template/delete', '消息模板', false, true, '消息模板 删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750818, 'LoginTypeController#findById', '通过ID查询登录方式', 'GET', '/loginType/findById', '登录方式管理', false, true, '登录方式管理 通过ID查询登录方式', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750819, 'ReconcileOrderController#page', '对账单分页', 'GET', '/order/reconcile/page', '对账控制器', false, true, '对账控制器 对账单分页', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750820, 'UnionPayConfigController#findPayWays', '支持的支付方式', 'GET', '/union/pay/config/findPayWays', '云闪付配置', false, true, '云闪付配置 支持的支付方式', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750821, 'DataRoleController#getDeptIds', '获取关联部门id', 'GET', '/data/role/getDeptIds', '数据角色配置', false, true, '数据角色配置 获取关联部门id', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750822, 'PayOrderController#close', '关闭支付记录', 'POST', '/order/pay/close', '支付订单控制器', false, true, '支付订单控制器 关闭支付记录', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750823, 'DingRobotConfigController#findById', '获取详情', 'GET', '/ding/robot/config/findById', '钉钉机器人配置', false, true, '钉钉机器人配置 获取详情', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750824, 'MessageTemplateController#update', '更新', 'POST', '/message/template/update', '消息模板', false, true, '消息模板 更新', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750825, 'CaptchaController#imgCaptcha', '获取图片验证码', 'POST', '/captcha/imgCaptcha', '验证码服务', false, true, '验证码服务 获取图片验证码', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750826, 'SystemParamController#delete', '删除', 'DELETE', '/system/param/delete', '系统参数', false, true, '系统参数 删除', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750827, 'UserInfoController#register', '注册账号', 'POST', '/user/register', '用户管理', false, true, '用户管理 注册账号', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750828, 'DictionaryItemController#add', '添加字典项（返回字典项对象）', 'POST', '/dict/item/add', '字典项', false, true, '字典项 添加字典项（返回字典项对象）', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750829, 'UserinternalController#restartPassword', '重置密码', 'POST', '/user/internal/restartPassword', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 重置密码', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750830, 'ClientController#update', '修改', 'POST', '/client/update', '认证终端', false, true, '认证终端 修改', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750831, 'SmsTemplateController#page', '分页查询', 'GET', '/sms/template/page', '短信模板配置', false, true, '短信模板配置 分页查询', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750832, 'MailConfigController#updateMailConfig', '更新邮箱配置', 'POST', '/mail/config/update', '邮箱配置', false, true, '邮箱配置 更新邮箱配置', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117566750833, 'UniPayController#allocation', '开启分账接口', 'POST', '/unipay/allocation', '统一支付接口', false, true, '统一支付接口 开启分账接口', 1399985191002447872, '2024-05-13 19:16:54.070000', 1399985191002447872, '2024-05-13 19:16:54.070000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945024, 'ReconcileOrderController#downAndSave', '手动触发对账文件下载', 'POST', '/order/reconcile/downAndSave', '对账控制器', false, true, '对账控制器 手动触发对账文件下载', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945025, 'ForwardFrontController#toH5', 'toH5', 'GET', '/front/', 'ForwardFrontController', false, true, 'ForwardFrontController toH5', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945026, 'RoleController#page', '分页查询角色', 'GET', '/role/page', '角色管理', false, true, '角色管理 分页查询角色', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945027, 'TokenEndpoint#logout', '退出', 'POST', '/token/logout', '认证相关', false, true, '认证相关 退出', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945028, 'PayChannelConfigController#findById', '根据ID获取', 'GET', '/pay/channel/config/findById', '支付通道信息', false, true, '支付通道信息 根据ID获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945029, 'ReconcileOrderController#pageDetail', '对账明细分页', 'GET', '/order/reconcile/detail/page', '对账控制器', false, true, '对账控制器 对账明细分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945030, 'PasswordSecurityConfigController#getDefault', '获取配置', 'GET', '/security/password/getDefault', '密码安全策略', false, true, '密码安全策略 获取配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945031, 'DictionaryItemController#findById', '根据字典项ID查询', 'GET', '/dict/item/findById', '字典项', false, true, '字典项 根据字典项ID查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945032, 'DictionaryItemController#findByDictionaryId', '查询指定字典ID下的所有字典项', 'GET', '/dict/item/findByDictionaryId', '字典项', false, true, '字典项 查询指定字典ID下的所有字典项', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945033, 'ForwardH5Controller#toH5', 'toH5', 'GET', '/h5/', 'ForwardH5Controller', false, true, 'ForwardH5Controller toH5', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945034, 'SiteMessageController#findById', '消息详情', 'GET', '/site/message/findById', '站内信', false, true, '站内信 消息详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945035, 'DictionaryController#update', '更新', 'POST', '/dict/update', '字典', false, true, '字典 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945036, 'FIleUpLoadController#download', '下载文件(流量会经过后端)', 'GET', '/file/download/{id}', '文件上传', false, true, '文件上传 下载文件(流量会经过后端)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945037, 'SiteMessageController#pageBySender', '发送消息分页', 'GET', '/site/message/pageBySender', '站内信', false, true, '站内信 发送消息分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945038, 'AllocationGroupController#unbindReceiver', '取消绑定接收者', 'POST', '/allocation/group/unbindReceiver', '分账组', false, true, '分账组 取消绑定接收者', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945039, 'AlipayConfigController#readPem', '读取证书文件内容', 'POST', '/alipay/config/readPem', '支付宝配置', false, true, '支付宝配置 读取证书文件内容', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945040, 'ClientNoticeReceiveController#refund', '退款消息(对象)', 'POST', '/demo/callback/refundObject', '回调测试', false, true, '回调测试 退款消息(对象)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945041, 'PermMenuController#update', '修改菜单权限', 'POST', '/perm/menu/update', '菜单和权限码', false, true, '菜单和权限码 修改菜单权限', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945042, 'QuartzJobController#update', '更新', 'POST', '/quartz/update', '定时任务', false, true, '定时任务 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945043, 'RoleController#dropdown', '角色下拉框', 'GET', '/role/dropdown', '角色管理', false, true, '角色管理 角色下拉框', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945044, 'AggregateController#aliH5Pay', '支付宝通过聚合支付码发起支付', 'POST', '/demo/aggregate/aliH5Pay', '聚合支付', false, true, '聚合支付 支付宝通过聚合支付码发起支付', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945045, 'UserRoleController#findRolesByUser', '根据用户ID获取到角色集合', 'GET', '/user/role/findRolesByUser', '用户角色管理', false, true, '用户角色管理 根据用户ID获取到角色集合', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945046, 'UserInfoController#existsPhone', '手机号是否被使用', 'GET', '/user/existsPhone', '用户管理', false, true, '用户管理 手机号是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945047, 'ClientNoticeReceiveController#pay', '支付消息(对象接收)', 'POST', '/demo/callback/payObject', '回调测试', false, true, '回调测试 支付消息(对象接收)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945048, 'PayMethodInfoController#findAll', '获取全部', 'GET', '/pay/method/info/findAll', '支付方式管理', false, true, '支付方式管理 获取全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945049, 'LoginTypeController#page', '分页查询登录方式', 'GET', '/loginType/page', '登录方式管理', false, true, '登录方式管理 分页查询登录方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945050, 'DictionaryItemController#findAllByEnable', '获取启用的字典项列表', 'GET', '/dict/item/findAllByEnable', '字典项', false, true, '字典项 获取启用的字典项列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945051, 'RolePathController#save', '保存角色请求权限关联关系', 'POST', '/role/path/save', '角色请求权限消息关系', false, true, '角色请求权限消息关系 保存角色请求权限关联关系', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945052, 'WalletController#findById', '查询钱包详情', 'GET', '/wallet/findById', '钱包管理', false, true, '钱包管理 查询钱包详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945053, 'OnlineUserController#pageByLogin', '登录用户分页', 'GET', '/online/user/pageByLogin', '在线用户', false, true, '在线用户 登录用户分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945054, 'MailConfigController#delete', '删除', 'DELETE', '/mail/config/delete', '邮箱配置', false, true, '邮箱配置 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945055, 'DictionaryController#delete', '根据id删除', 'DELETE', '/dict/delete', '字典', false, true, '字典 根据id删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945056, 'PayOrderController#getExtraById', '查询支付订单扩展信息', 'GET', '/order/pay/getExtraById', '支付订单控制器', false, true, '支付订单控制器 查询支付订单扩展信息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945057, 'SiteMessageController#send', '发送站内信', 'POST', '/site/message/send', '站内信', false, true, '站内信 发送站内信', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945058, 'SiteMessageController#delete', '删除消息', 'DELETE', '/site/message/delete', '站内信', false, true, '站内信 删除消息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945059, 'AlipayConfigController#findPayWays', '支付宝支持支付方式', 'GET', '/alipay/config/findPayWays', '支付宝配置', false, true, '支付宝配置 支付宝支持支付方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945060, 'SiteMessageController#listByReceiveNotRead', '小程序获取未读的接收消息标题列表', 'GET', '/site/message/listByReceiveNotRead', '站内信', false, true, '站内信 小程序获取未读的接收消息标题列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945061, 'ClientNoticeReceiveController#refund', '退款消息', 'POST', '/demo/callback/refund', '回调测试', false, true, '回调测试 退款消息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945062, 'CashierController#getPayEnv', '获取支付环境', 'GET', '/demo/cashier/getPayEnv', '结算台演示', false, true, '结算台演示 获取支付环境', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945063, 'UserinternalController#banBatch', '批量封禁用户', 'POST', '/user/internal/banBatch', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 批量封禁用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945064, 'PayReturnController#wechat', '微信同步通知', 'GET', '/return/pay/wechat', '支付同步通知', false, true, '支付同步通知 微信同步通知', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945065, 'DictionaryItemController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/dict/item/existsByCodeNotId', '字典项', false, true, '字典项 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945066, 'DictionaryItemController#pageByDictionaryId', '分页查询指定字典下的字典项', 'GET', '/dict/item/pageByDictionaryId', '字典项', false, true, '字典项 分页查询指定字典下的字典项', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945067, 'AggregateController#wxAuthCallback', '微信授权回调页面', 'GET', '/demo/aggregate/wxAuthCallback', '聚合支付', false, true, '聚合支付 微信授权回调页面', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945068, 'LoginTypeController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/loginType/existsByCodeNotId', '登录方式管理', false, true, '登录方式管理 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945069, 'MessageTemplateController#page', '分页', 'GET', '/message/template/page', '消息模板', false, true, '消息模板 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945070, 'FIleUpLoadController#getFilePreviewUrl', '获取文件预览地址(流量会经过后端)', 'GET', '/file/getFilePreviewUrl', '文件上传', false, true, '文件上传 获取文件预览地址(流量会经过后端)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945071, 'LoginTypeController#findByCode', '通过code查询登录方式', 'GET', '/loginType/findByCode', '登录方式管理', false, true, '登录方式管理 通过code查询登录方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945072, 'PayReturnController#alipay', '支付宝同步跳转连接', 'GET', '/return/pay/alipay', '支付同步通知', false, true, '支付同步通知 支付宝同步跳转连接', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945073, 'PayOrderController#findByOrderNo', '查询订单详情', 'GET', '/order/pay/findByOrderNo', '支付订单控制器', false, true, '支付订单控制器 查询订单详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945074, 'ReconcileOrderController#findById', '对账单详情', 'GET', '/order/reconcile/findById', '对账控制器', false, true, '对账控制器 对账单详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945075, 'UserAssistController#sendPhoneChangeCaptcha', '发送更改/绑定手机号验证码', 'POST', '/user/sendPhoneChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 发送更改/绑定手机号验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945076, 'MultipleOpenApiWebMvcResource#openapiYaml', 'openapiYaml', 'GET', '/v3/api-docs.yaml/{group}', 'MultipleOpenApiWebMvcResource', false, true, 'MultipleOpenApiWebMvcResource openapiYaml', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945077, 'PermMenuController#delete', '删除', 'DELETE', '/perm/menu/delete', '菜单和权限码', false, true, '菜单和权限码 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945078, 'AggregateController#barCodePay', '通过付款码发起支付', 'POST', '/demo/aggregate/barCodePay', '聚合支付', false, true, '聚合支付 通过付款码发起支付', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945079, 'SiteMessageController#pageByReceive', '接收消息分页', 'GET', '/site/message/pageByReceive', '站内信', false, true, '站内信 接收消息分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945080, 'ReconcileOrderController#downDiffCsv', '下载对账差异单(CSV格式)', 'GET', '/order/reconcile/downDiffCsv', '对账控制器', false, true, '对账控制器 下载对账差异单(CSV格式)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945081, 'WechatNoticeConfigController#update', '更新微信消息通知配置', 'POST', '/wx/notice/update', '微信消息通知配置', false, true, '微信消息通知配置 更新微信消息通知配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945082, 'AllocationReceiverController#page', '分页', 'GET', '/allocation/receiver/page', '分账接收方控制器', false, true, '分账接收方控制器 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945083, 'ThirdLoginController#toLoginUrl', '跳转到登陆页', 'GET', '/auth/third/toLoginUrl/{loginType}', '三方登录', false, true, '三方登录 跳转到登陆页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945084, 'AlipayConfigController#getConfig', '获取配置', 'GET', '/alipay/config/getConfig', '支付宝配置', false, true, '支付宝配置 获取配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945085, 'AllocationOrderController#findChannels', '获取可以分账的通道', 'GET', '/order/allocation/findChannels', '分账订单控制器', false, true, '分账订单控制器 获取可以分账的通道', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945086, 'MailConfigController#page', '分页', 'GET', '/mail/config/page', '邮箱配置', false, true, '邮箱配置 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945087, 'CashierController#simplePayCashier', '创建支付订单并发起', 'POST', '/demo/cashier/simplePayCashier', '结算台演示', false, true, '结算台演示 创建支付订单并发起', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945088, 'ClientController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/client/existsByCodeNotId', '认证终端', false, true, '认证终端 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945089, 'UserInfoController#existsEmail', '邮箱是否被使用', 'GET', '/user/existsEmail', '用户管理', false, true, '用户管理 邮箱是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945090, 'WecomRobotConfigController#existsByCode', '编码是否被使用', 'GET', '/wecom/robot/config/existsByCode', '企业微信机器人配置', false, true, '企业微信机器人配置 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945091, 'ClientNoticeReceiveController#pay', '支付消息(map接收)', 'POST', '/demo/callback/pay', '回调测试', false, true, '回调测试 支付消息(map接收)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945092, 'RoleMenuController#findTreeByRole', '获取当前角色下可见的菜单和权限码树(分配时用)', 'GET', '/role/menu/findTreeByRole', '角色菜单权限关系', false, true, '角色菜单权限关系 获取当前角色下可见的菜单和权限码树(分配时用)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945093, 'MessageTemplateController#add', '添加', 'POST', '/message/template/add', '消息模板', false, true, '消息模板 添加', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945094, 'WalletConfigController#getConfig', '获取配置', 'GET', '/wallet/config/getConfig', '钱包配置', false, true, '钱包配置 获取配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945095, 'WechatNoticeConfigController#getConfig', '获取微信消息通知配置', 'GET', '/wx/notice/getConfig', '微信消息通知配置', false, true, '微信消息通知配置 获取微信消息通知配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945096, 'DictionaryController#existsByCode', '编码是否被使用', 'GET', '/dict/existsByCode', '字典', false, true, '字典 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945097, 'RoleMenuController#findMenuIds', '获取权限菜单id列表,不包含资源权限', 'GET', '/role/menu/findMenuIds', '角色菜单权限关系', false, true, '角色菜单权限关系 获取权限菜单id列表,不包含资源权限', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945098, 'PayRepairRecordController#findById', '查询单条', 'GET', '/record/repair/findById', '支付修复记录', false, true, '支付修复记录 查询单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945099, 'RoleController#delete', '删除角色', 'DELETE', '/role/delete', '角色管理', false, true, '角色管理 删除角色', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945100, 'AllocationGroupController#page', '分页', 'GET', '/allocation/group/page', '分账组', false, true, '分账组 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945101, 'UserDeptController#findIdsByUser', '根据用户ID获取到部门id集合', 'GET', '/user/dept/findIdsByUser', '用户部门关联关系', false, true, '用户部门关联关系 根据用户ID获取到部门id集合', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945102, 'SwaggerWelcomeWebMvc#redirectToUi', 'redirectToUi', 'GET', '/swagger-ui.html', 'SwaggerWelcomeWebMvc', false, true, 'SwaggerWelcomeWebMvc redirectToUi', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945103, 'RoleController#update', '修改角色（返回角色对象）', 'POST', '/role/update', '角色管理', false, true, '角色管理 修改角色（返回角色对象）', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945104, 'UniPayAssistController#getWxAuthUrl', '获取微信OAuth2授权链接', 'POST', '/unipay/assist/getWxAuthUrl', '支付支撑接口', false, true, '支付支撑接口 获取微信OAuth2授权链接', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945105, 'PermMenuController#resourceList', '资源(权限码)列表', 'GET', '/perm/menu/resourceList', '菜单和权限码', false, true, '菜单和权限码 资源(权限码)列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945106, 'PasswordSecurityConfigController#addOrUpdate', '新增或添加密码安全配置', 'POST', '/security/password/addOrUpdate', '密码安全策略', false, true, '密码安全策略 新增或添加密码安全配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945107, 'PayGatewayNoticeController#aliPayNotice', '支付宝消息通知', 'POST', '/gateway/notice/alipay', '三方支付网关消息通知', false, true, '三方支付网关消息通知 支付宝消息通知', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945108, 'SmsTemplateController#findAll', '查询所有', 'GET', '/sms/template/findAll', '短信模板配置', false, true, '短信模板配置 查询所有', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945109, 'PasswordSecurityConfigController#check', '登录后检查密码相关的情况', 'GET', '/security/password/check', '密码安全策略', false, true, '密码安全策略 登录后检查密码相关的情况', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945110, 'SystemMonitorController#getRedisInfo', '获取Redis信息', 'GET', '/monitor/system/getRedisInfo', '系统信息监控', false, true, '系统信息监控 获取Redis信息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945111, 'AllocationGroupController#delete', '删除', 'POST', '/allocation/group/delete', '分账组', false, true, '分账组 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945112, 'SystemMonitorController#getSystemInfo', '获取系统消息', 'GET', '/monitor/system/getSystemInfo', '系统信息监控', false, true, '系统信息监控 获取系统消息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945113, 'MailConfigController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/mail/config/existsByCodeNotId', '邮箱配置', false, true, '邮箱配置 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945114, 'AllocationReceiverController#add', '新增', 'POST', '/allocation/receiver/add', '分账接收方控制器', false, true, '分账接收方控制器 新增', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945115, 'AllocationOrderController#findById', '查询详情', 'GET', '/order/allocation/findById', '分账订单控制器', false, true, '分账订单控制器 查询详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945116, 'AllocationOrderController#findDetailById', '查询明细详情', 'GET', '/order/allocation/detail/findById', '分账订单控制器', false, true, '分账订单控制器 查询明细详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945117, 'SystemParamController#existsByKeyNotId', '判断编码是否存在(不包含自己)', 'GET', '/system/param/existsByKeyNotId', '系统参数', false, true, '系统参数 判断编码是否存在(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945118, 'WeChatMediaController#pageNews', '图文素材分页', 'GET', '/wechat/media/pageNews', '微信素材管理', false, true, '微信素材管理 图文素材分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945119, 'RoleController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/role/existsByCodeNotId', '角色管理', false, true, '角色管理 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945120, 'DataRoleController#page', '分页', 'GET', '/data/role/page', '数据角色配置', false, true, '数据角色配置 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945121, 'OperateLogController#deleteByDay', '清除指定天数的日志', 'DELETE', '/log/operate/deleteByDay', '操作日志', false, true, '操作日志 清除指定天数的日志', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945122, 'OnlineUserController#getSessionByUserId', '获取用户链接信息', 'GET', '/online/user/getSessionByUserId', '在线用户', false, true, '在线用户 获取用户链接信息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945123, 'LoginTypeController#add', '添加登录方式', 'POST', '/loginType/add', '登录方式管理', false, true, '登录方式管理 添加登录方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945124, 'UnionPayConfigController#getConfig', '获取配置', 'GET', '/union/pay/config/getConfig', '云闪付配置', false, true, '云闪付配置 获取配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945125, 'WeChatMenuController#delete', '删除', 'DELETE', '/wechat/menu/delete', '微信菜单管理', false, true, '微信菜单管理 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945126, 'BaseApiController#authEcho', '回声测试(必须要进行登录)', 'GET', '/auth/echo', '系统基础接口', false, true, '系统基础接口 回声测试(必须要进行登录)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945127, 'WeChatQrLoginController#applyQrCode', '申请登录用QR码', 'POST', '/token/wechat/qr/applyQrCode', '微信扫码登录', false, true, '微信扫码登录 申请登录用QR码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945128, 'FIleUpLoadController#delete', '删除', 'DELETE', '/file/delete', '文件上传', false, true, '文件上传 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945129, 'CockpitReportController#getRefundChannelInfo', '显示通道退款订单金额和订单数', 'GET', '/report/cockpit/getRefundChannelInfo', '驾驶舱接口', false, true, '驾驶舱接口 显示通道退款订单金额和订单数', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945130, 'RoleController#add', '添加角色（返回角色对象）', 'POST', '/role/add', '角色管理', false, true, '角色管理 添加角色（返回角色对象）', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945131, 'ClientController#add', '添加', 'POST', '/client/add', '认证终端', false, true, '认证终端 添加', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945132, 'UserInfoController#forgetPasswordByPhone', '通过手机号重置密码', 'POST', '/user/forgetPasswordByPhone', '用户管理', false, true, '用户管理 通过手机号重置密码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945133, 'RolePathController#findPathsByRole', '获取当前用户角色下可见的请求权限列表(分配时用)', 'GET', '/role/path/findPathsByRole', '角色请求权限消息关系', false, true, '角色请求权限消息关系 获取当前用户角色下可见的请求权限列表(分配时用)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945134, 'WeChatMenuController#findAll', '查询所有', 'GET', '/wechat/menu/findAll', '微信菜单管理', false, true, '微信菜单管理 查询所有', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945135, 'ClientController#findAll', '查询所有', 'GET', '/client/findAll', '认证终端', false, true, '认证终端 查询所有', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945136, 'RefundOrderController#findExtraById', '查询扩展信息', 'GET', '/order/refund/findExtraById', '支付退款控制器', false, true, '支付退款控制器 查询扩展信息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945137, 'UserDataRoleController#saveAssignBatch', '给用户分配权限(批量)', 'POST', '/user/data/role/saveAssignBatch', '用户数据角色配置', false, true, '用户数据角色配置 给用户分配权限(批量)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945138, 'LoginLogController#page', '分页', 'GET', '/log/login/page', '登录日志', false, true, '登录日志 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945139, 'FIleUpLoadController#findById', '获取单条详情', 'GET', '/file/findById', '文件上传', false, true, '文件上传 获取单条详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945140, 'MessageTemplateController#findById', '获取详情', 'GET', '/message/template/findById', '消息模板', false, true, '消息模板 获取详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945141, 'DataRoleController#saveUserAssign', '保存数据角色关联用户权限', 'POST', '/data/role/saveUserAssign', '数据角色配置', false, true, '数据角色配置 保存数据角色关联用户权限', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945142, 'ThirdLoginController#callback', '扫码后回调', 'GET', '/auth/third/callback/{loginType}', '三方登录', false, true, '三方登录 扫码后回调', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945143, 'SmsChannelConfigController#findByCode', '通过Code查询', 'GET', '/sms/config/findByCode', '短信渠道配置', false, true, '短信渠道配置 通过Code查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945144, 'PayApiConfigController#findById', '根据ID获取', 'GET', '/pay/api/config/findById', '支付接口配置', false, true, '支付接口配置 根据ID获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945145, 'DataVersionLogController#findById', '获取', 'GET', '/log/dataVersion/findById', '数据版本日志', false, true, '数据版本日志 获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945146, 'PermPathController#update', '更新权限', 'POST', '/perm/path/update', '请求权限管理', false, true, '请求权限管理 更新权限', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945147, 'MailConfigController#setUpActivity', '设置启用的邮箱配置', 'POST', '/mail/config/setUpActivity', '邮箱配置', false, true, '邮箱配置 设置启用的邮箱配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945148, 'SmsTemplateController#findById', '通过ID查询', 'GET', '/sms/template/findById', '短信模板配置', false, true, '短信模板配置 通过ID查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945149, 'DingRobotConfigController#add', '新增机器人配置', 'POST', '/ding/robot/config/add', '钉钉机器人配置', false, true, '钉钉机器人配置 新增机器人配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945150, 'PayCloseRecordController#findById', '查询单条', 'GET', '/record/close/findById', '支付订单关闭记录', false, true, '支付订单关闭记录 查询单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945151, 'DataVersionLogController#page', '分页', 'GET', '/log/dataVersion/page', '数据版本日志', false, true, '数据版本日志 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945152, 'OpenApiWebMvcResource#openapiJson', 'openapiJson', 'GET', '/v3/api-docs', 'OpenApiWebMvcResource', false, true, 'OpenApiWebMvcResource openapiJson', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945153, 'PermPathController#findAll', '权限列表', 'GET', '/perm/path/findAll', '请求权限管理', false, true, '请求权限管理 权限列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945154, 'UserinternalController#add', '添加用户', 'POST', '/user/internal/add', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 添加用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945155, 'PayCallbackRecordController#findById', '查询单条', 'GET', '/record/callback/findById', '支付回调信息记录', false, true, '支付回调信息记录 查询单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945156, 'WecomRobotConfigController#page', '分页', 'GET', '/wecom/robot/config/page', '企业微信机器人配置', false, true, '企业微信机器人配置 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945157, 'WeChatMenuController#update', '修改', 'POST', '/wechat/menu/update', '微信菜单管理', false, true, '微信菜单管理 修改', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945158, 'TestController#lock2', '锁测试2', 'GET', '/test/lock2', '测试', false, true, '测试 锁测试2', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945159, 'DataRoleController#findAll', '查询全部', 'GET', '/data/role/findAll', '数据角色配置', false, true, '数据角色配置 查询全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945160, 'UserInfoController#forgetPasswordByEmail', '通过邮箱重置密码', 'POST', '/user/forgetPasswordByEmail', '用户管理', false, true, '用户管理 通过邮箱重置密码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945161, 'RefundOrderController#page', '分页查询', 'GET', '/order/refund/page', '支付退款控制器', false, true, '支付退款控制器 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945162, 'AllocationOrderController#finish', '分账完结', 'POST', '/order/allocation/finish', '分账订单控制器', false, true, '分账订单控制器 分账完结', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945163, 'PayApiConfigController#findAll', '获取全部', 'GET', '/pay/api/config/findAll', '支付接口配置', false, true, '支付接口配置 获取全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945164, 'ClientNoticeTaskController#resetSend', '重新发送消息通知', 'POST', '/task/notice/resetSend', '客户系统通知任务', false, true, '客户系统通知任务 重新发送消息通知', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945165, 'PlatformConfigController#update', '更新平台配置项', 'POST', '/platform/config/update', '支付平台配置控制器', false, true, '支付平台配置控制器 更新平台配置项', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945166, 'UserRoleController#saveAssign', '给用户分配角色', 'POST', '/user/role/saveAssign', '用户角色管理', false, true, '用户角色管理 给用户分配角色', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945167, 'UniPayController#close', '支付关闭接口', 'POST', '/unipay/close', '统一支付接口', false, true, '统一支付接口 支付关闭接口', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945168, 'UserDataRoleController#findDataRoleIdByUser', '根据用户ID获取到数据角色Id', 'GET', '/user/data/role/findDataRoleIdByUser', '用户数据角色配置', false, true, '用户数据角色配置 根据用户ID获取到数据角色Id', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945169, 'MailConfigController#existsByCode', '编码是否被使用', 'GET', '/mail/config/existsByCode', '邮箱配置', false, true, '邮箱配置 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945170, 'AllocationGroupController#updateRate', '修改分账比例', 'POST', '/allocation/group/updateRate', '分账组', false, true, '分账组 修改分账比例', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945171, 'WeChatPayConfigController#getConfig', '获取配置', 'GET', '/wechat/pay/config/getConfig', '微信支付配置', false, true, '微信支付配置 获取配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945172, 'AllocationReceiverController#findChannels', '获取可以分账的通道', 'GET', '/allocation/receiver/findChannels', '分账接收方控制器', false, true, '分账接收方控制器 获取可以分账的通道', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945173, 'UserAssistController#validateCurrentChangeEmailCaptcha', '验证当前用户发送更改邮箱验证码', 'GET', '/user/validateCurrentChangeEmailCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证当前用户发送更改邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945174, 'UserRoleController#saveAssignBatch', '给用户分配角色(批量)', 'POST', '/user/role/saveAssignBatch', '用户角色管理', false, true, '用户角色管理 给用户分配角色(批量)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945175, 'ClientNoticeTaskController#page', '分页查询', 'GET', '/task/notice/page', '客户系统通知任务', false, true, '客户系统通知任务 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945176, 'UserinternalController#getByEmail', '根据邮箱查询用户', 'GET', '/user/internal/getByEmail', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 根据邮箱查询用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945177, 'RefundOrderController#syncByRefundNo', '退款同步', 'POST', '/order/refund/syncByRefundNo', '支付退款控制器', false, true, '支付退款控制器 退款同步', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945178, 'DeptController#delete', '普通删除', 'DELETE', '/dept/delete', '部门管理', false, true, '部门管理 普通删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945179, 'SystemParamController#page', '分页', 'GET', '/system/param/page', '系统参数', false, true, '系统参数 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945180, 'DingRobotConfigController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/ding/robot/config/existsByCodeNotId', '钉钉机器人配置', false, true, '钉钉机器人配置 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945181, 'AllocationReceiverController#update', '修改', 'POST', '/allocation/receiver/update', '分账接收方控制器', false, true, '分账接收方控制器 修改', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945182, 'AggregateController#qrPayPage', '聚合支付扫码跳转中间页', 'GET', '/demo/aggregate/qrPayPage/{code}', '聚合支付', false, true, '聚合支付 聚合支付扫码跳转中间页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945183, 'UserinternalController#getByPhone', '根据手机号查询用户', 'GET', '/user/internal/getByPhone', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 根据手机号查询用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945184, 'AllocationOrderController#findDetailsByOrderId', '分账明细列表', 'GET', '/order/allocation/detail/findAll', '分账订单控制器', false, true, '分账订单控制器 分账明细列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945185, 'LoginLogController#findById', '获取', 'GET', '/log/login/findById', '登录日志', false, true, '登录日志 获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945186, 'WalletController#existsByUserId', '判断用户是否开通了钱包', 'GET', '/wallet/existsByUserId', '钱包管理', false, true, '钱包管理 判断用户是否开通了钱包', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945187, 'MailConfigController#add', '增加新邮箱配置', 'POST', '/mail/config/add', '邮箱配置', false, true, '邮箱配置 增加新邮箱配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945188, 'RoleController#existsByName', '名称是否被使用(不包含自己)', 'GET', '/role/existsByNameNotId', '角色管理', false, true, '角色管理 名称是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945189, 'PayMethodInfoController#findById', '根据ID获取', 'GET', '/pay/method/info/findById', '支付方式管理', false, true, '支付方式管理 根据ID获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945190, 'UserinternalController#restartPasswordBatch', '批量重置密码', 'POST', '/user/internal/restartPasswordBatch', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 批量重置密码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945191, 'DictionaryItemController#existsByCode', '编码是否被使用', 'GET', '/dict/item/existsByCode', '字典项', false, true, '字典项 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945192, 'AllocationReceiverController#delete', '删除', 'POST', '/allocation/receiver/delete', '分账接收方控制器', false, true, '分账接收方控制器 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945193, 'UserDeptController#findAllByUser', '根据用户ID获取到部门集合', 'GET', '/user/dept/findAllByUser', '用户部门关联关系', false, true, '用户部门关联关系 根据用户ID获取到部门集合', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945194, 'ClientNoticeTaskController#findById', '查询单条', 'GET', '/task/notice/findById', '客户系统通知任务', false, true, '客户系统通知任务 查询单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945195, 'UserThirdController#getThirdBindInfo', '获取绑定详情', 'GET', '/user/third/getThirdBindInfo', '用户三方登录管理', false, true, '用户三方登录管理 获取绑定详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945196, 'OperateLogController#findById', '获取', 'GET', '/log/operate/findById', '操作日志', false, true, '操作日志 获取', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945197, 'MailConfigController#findById', '通过 id 获取指定邮箱配置', 'GET', '/mail/config/findById', '邮箱配置', false, true, '邮箱配置 通过 id 获取指定邮箱配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945198, 'AggregateController#getWxJsapiPay', '获取微信支付调起Jsapi支付的信息', 'POST', '/demo/aggregate/getWxJsapiPay', '聚合支付', false, true, '聚合支付 获取微信支付调起Jsapi支付的信息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945199, 'PermPathController#delete', '删除权限', 'DELETE', '/perm/path/delete', '请求权限管理', false, true, '请求权限管理 删除权限', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945200, 'WeChatPayConfigController#findPayWays', '微信支持支付方式', 'GET', '/wechat/pay/config/findPayWays', '微信支付配置', false, true, '微信支付配置 微信支持支付方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945201, 'UserAssistController#validatePhoneChangeCaptcha', '验证改/绑定手机验证码', 'GET', '/user/validatePhoneChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证改/绑定手机验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945202, 'UserAssistController#validateEmailForgetCaptcha', '验证找回密码邮箱验证码', 'GET', '/user/validateEmailForgetCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证找回密码邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945203, 'ReconcileOrderController#downOriginal', '下载原始交易对账单文件', 'GET', '/order/reconcile/downOriginal', '对账控制器', false, true, '对账控制器 下载原始交易对账单文件', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945204, 'SmsChannelConfigController#update', '修改', 'POST', '/sms/config/update', '短信渠道配置', false, true, '短信渠道配置 修改', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945205, 'LoginTypeController#delete', '删除登录方式', 'DELETE', '/loginType/delete', '登录方式管理', false, true, '登录方式管理 删除登录方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945206, 'DeptController#update', '更新', 'POST', '/dept/update', '部门管理', false, true, '部门管理 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945207, 'WeChatTemplateController#sync', '同步消息模板数据', 'POST', '/wechat/template/sync', '微信模板消息', false, true, '微信模板消息 同步消息模板数据', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945208, 'UserDeptController#saveAssignBatch', '给用户分配部门(批量)', 'POST', '/user/dept/saveAssignBatch', '用户部门关联关系', false, true, '用户部门关联关系 给用户分配部门(批量)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945209, 'UserinternalController#unlockBatch', '批量解锁用户', 'POST', '/user/internal/unlockBatch', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 批量解锁用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945210, 'WecomRobotConfigController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/wecom/robot/config/existsByCodeNotId', '企业微信机器人配置', false, true, '企业微信机器人配置 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945211, 'WeChatPayConfigController#update', '更新', 'POST', '/wechat/pay/config/update', '微信支付配置', false, true, '微信支付配置 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945212, 'UserThirdController#findById', '获取详情', 'POST', '/user/third/findById', '用户三方登录管理', false, true, '用户三方登录管理 获取详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945213, 'RefundOrderController#resetRefund', '重新发起退款', 'POST', '/order/refund/resetRefund', '支付退款控制器', false, true, '支付退款控制器 重新发起退款', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945214, 'LoginTypeController#update', '修改登录方式（返回登录方式对象）', 'POST', '/loginType/update', '登录方式管理', false, true, '登录方式管理 修改登录方式（返回登录方式对象）', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945215, 'ReconcileOrderController#findDetailById', '对账明细详情', 'GET', '/order/reconcile/detail/findById', '对账控制器', false, true, '对账控制器 对账明细详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945216, 'QuartzJobController#execute', '立即执行', 'POST', '/quartz/execute', '定时任务', false, true, '定时任务 立即执行', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945217, 'QuartzJobController#start', '启动', 'POST', '/quartz/start', '定时任务', false, true, '定时任务 启动', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945218, 'DictionaryController#findAll', '查询全部', 'GET', '/dict/findAll', '字典', false, true, '字典 查询全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945219, 'WeChatMediaController#deleteFile', '删除素材', 'DELETE', '/wechat/media/deleteFile', '微信素材管理', false, true, '微信素材管理 删除素材', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945220, 'UniPayController#allocationFinish', '分账完结接口', 'POST', '/unipay/allocationFinish', '统一支付接口', false, true, '统一支付接口 分账完结接口', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945221, 'DataRoleController#update', '更新', 'POST', '/data/role/update', '数据角色配置', false, true, '数据角色配置 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945222, 'UnionPayConfigController#toBase64', '读取证书文件内容', 'POST', '/union/pay/config/toBase64', '云闪付配置', false, true, '云闪付配置 读取证书文件内容', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945223, 'LoginTypeController#findAll', '查询所有的登录方式', 'GET', '/loginType/findAll', '登录方式管理', false, true, '登录方式管理 查询所有的登录方式', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945258, 'AllocationOrderController#sync', '同步分账结果', 'POST', '/order/allocation/sync', '分账订单控制器', false, true, '分账订单控制器 同步分账结果', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945224, 'UserInfoController#findUsernameByPhoneCaptcha', '根据手机验证码查询账号', 'GET', '/user/findUsernameByPhoneCaptcha', '用户管理', false, true, '用户管理 根据手机验证码查询账号', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945225, 'UserAssistController#sendCurrentPhoneChangeCaptcha', '给当前用户发送更改手机号验证码', 'POST', '/user/sendCurrentPhoneChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 给当前用户发送更改手机号验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945226, 'DeptController#add', '添加', 'POST', '/dept/add', '部门管理', false, true, '部门管理 添加', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945227, 'SystemParamController#findById', '获取单条', 'GET', '/system/param/findById', '系统参数', false, true, '系统参数 获取单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945228, 'QuartzJobLogController#findById', '单条', 'GET', '/quartz/log/findById', '定时任务执行日志', false, true, '定时任务执行日志 单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945229, 'RoleController#existsByCode', '编码是否被使用', 'GET', '/role/existsByCode', '角色管理', false, true, '角色管理 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945230, 'CockpitReportController#getPayChannelInfo', '显示通道支付订单金额和订单数', 'GET', '/report/cockpit/getPayChannelInfo', '驾驶舱接口', false, true, '驾驶舱接口 显示通道支付订单金额和订单数', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945231, 'AllocationReceiverController#findReceiverTypeByChannel', '根据通道获取分账接收方类型', 'GET', '/allocation/receiver/findReceiverTypeByChannel', '分账接收方控制器', false, true, '分账接收方控制器 根据通道获取分账接收方类型', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945232, 'UserinternalController#unlock', '解锁用户', 'POST', '/user/internal/unlock', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 解锁用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945233, 'WecomRobotConfigController#add', '新增机器人配置', 'POST', '/wecom/robot/config/add', '企业微信机器人配置', false, true, '企业微信机器人配置 新增机器人配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945234, 'AllocationGroupController#setDefault', '设置默认分账组', 'POST', '/allocation/group/setDefault', '分账组', false, true, '分账组 设置默认分账组', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945235, 'UserinternalController#findById', '根据用户id查询用户', 'GET', '/user/internal/findById', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 根据用户id查询用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945236, 'PayRepairRecordController#page', '分页查询', 'GET', '/record/repair/page', '支付修复记录', false, true, '支付修复记录 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945237, 'AllocationOrderController#page', '分页', 'GET', '/order/allocation/page', '分账订单控制器', false, true, '分账订单控制器 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945238, 'PasswordSecurityConfigController#isRecentlyUsed', '查看要修改的密码是否重复', 'GET', '/security/password/isRecentlyUsed', '密码安全策略', false, true, '密码安全策略 查看要修改的密码是否重复', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945239, 'WalletController#deduct', '扣减', 'POST', '/wallet/deduct', '钱包管理', false, true, '钱包管理 扣减', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945240, 'FIleUpLoadController#getFilePreviewUrlPrefix', '获取文件预览地址前缀', 'GET', '/file/getFilePreviewUrlPrefix', '文件上传', false, true, '文件上传 获取文件预览地址前缀', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945241, 'PermMenuController#existsByPermCode', '编码是否被使用(不包含自己)', 'GET', '/perm/menu/existsByPermCodeNotId', '菜单和权限码', false, true, '菜单和权限码 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945242, 'WeChatTemplateController#update', '修改', 'POST', '/wechat/template/update', '微信模板消息', false, true, '微信模板消息 修改', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945243, 'CashierController#queryPayOrder', '查询支付订单', 'GET', '/demo/cashier/queryPayOrderSuccess', '结算台演示', false, true, '结算台演示 查询支付订单', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945244, 'ReconcileOrderController#downOriginal2Csv', '下载原始交易对账单记录(CSV格式)', 'GET', '/order/reconcile/downOriginal2Csv', '对账控制器', false, true, '对账控制器 下载原始交易对账单记录(CSV格式)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945245, 'SmsChannelConfigController#findAll', '查询所有', 'GET', '/sms/config/findAll', '短信渠道配置', false, true, '短信渠道配置 查询所有', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945246, 'RoleMenuController#getPermissions', '获取菜单和权限码(根据用户进行筛选)', 'GET', '/role/menu/getPermissions', '角色菜单权限关系', false, true, '角色菜单权限关系 获取菜单和权限码(根据用户进行筛选)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945247, 'UserRoleController#findRoleIdsByUser', '根据用户ID获取到角色id集合', 'GET', '/user/role/findRoleIdsByUser', '用户角色管理', false, true, '用户角色管理 根据用户ID获取到角色id集合', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945248, 'AllocationGroupController#update', '修改', 'POST', '/allocation/group/update', '分账组', false, true, '分账组 修改', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945249, 'FIleUpLoadController#preview', '预览文件(流量会经过后端)', 'GET', '/file/preview/{id}', '文件上传', false, true, '文件上传 预览文件(流量会经过后端)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945250, 'QuartzJobController#judgeJobClass', '判断是否是定时任务类', 'GET', '/quartz/judgeJobClass', '定时任务', false, true, '定时任务 判断是否是定时任务类', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945251, 'PayOrderController#getTotalAmount', '查询金额汇总', 'GET', '/order/pay/getTotalAmount', '支付订单控制器', false, true, '支付订单控制器 查询金额汇总', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945252, 'UserAssistController#sendCurrentEmailChangeCaptcha', '给当前用户发送更改邮箱验证码', 'POST', '/user/sendCurrentEmailChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 给当前用户发送更改邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945253, 'WecomRobotConfigController#update', '修改机器人配置', 'POST', '/wecom/robot/config/update', '企业微信机器人配置', false, true, '企业微信机器人配置 修改机器人配置', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945254, 'UserThirdController#bind', '绑定第三方账号', 'POST', '/user/third/bind', '用户三方登录管理', false, true, '用户三方登录管理 绑定第三方账号', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945255, 'RoleController#tree', '角色树', 'GET', '/role/tree', '角色管理', false, true, '角色管理 角色树', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945256, 'UniPayController#pay', '统一支付接口', 'POST', '/unipay/pay', '统一支付接口', false, true, '统一支付接口 统一支付接口', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945257, 'LoginTypeController#existsByCode', '编码是否被使用', 'GET', '/loginType/existsByCode', '登录方式管理', false, true, '登录方式管理 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945259, 'UserinternalController#page', '分页', 'GET', '/user/internal/page', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945260, 'RolePathController#findIdsByRole', '根据角色id获取关联权限id', 'GET', '/role/path/findIdsByRole', '角色请求权限消息关系', false, true, '角色请求权限消息关系 根据角色id获取关联权限id', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945261, 'MultipleOpenApiWebMvcResource#openapiJson', 'openapiJson', 'GET', '/v3/api-docs/{group}', 'MultipleOpenApiWebMvcResource', false, true, 'MultipleOpenApiWebMvcResource openapiJson', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945262, 'UserThirdController#page', '分页', 'GET', '/user/third/page', '用户三方登录管理', false, true, '用户三方登录管理 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945263, 'RoleController#findById', '通过ID查询角色', 'GET', '/role/findById', '角色管理', false, true, '角色管理 通过ID查询角色', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945264, 'ReconcileOrderController#create', '手动创建对账订单', 'POST', '/order/reconcile/create', '对账控制器', false, true, '对账控制器 手动创建对账订单', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945265, 'PayApiConfigController#update', '更新', 'POST', '/pay/api/config/update', '支付接口配置', false, true, '支付接口配置 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945266, 'PayOrderController#page', '分页查询', 'GET', '/order/pay/page', '支付订单控制器', false, true, '支付订单控制器 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945267, 'UserInfoController#existsUsername', '账号是否被使用(不包含自己)', 'GET', '/user/existsUsernameNotId', '用户管理', false, true, '用户管理 账号是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945268, 'UserDeptController#saveAssign', '给用户分配部门', 'POST', '/user/dept/saveAssign', '用户部门关联关系', false, true, '用户部门关联关系 给用户分配部门', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945269, 'CockpitReportController#getPayOrderCount', '支付订单数量', 'GET', '/report/cockpit/getPayOrderCount', '驾驶舱接口', false, true, '驾驶舱接口 支付订单数量', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945270, 'UserAssistController#sendEmailForgetCaptcha', '发送找回密码邮箱验证码', 'POST', '/user/sendEmailForgetCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 发送找回密码邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945271, 'WecomRobotConfigController#findById', '获取详情', 'GET', '/wecom/robot/config/findById', '企业微信机器人配置', false, true, '企业微信机器人配置 获取详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945272, 'ClientNoticeTaskController#recordPage', '分页查询', 'GET', '/task/notice/record/page', '客户系统通知任务', false, true, '客户系统通知任务 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945273, 'RefundOrderController#findById', '查询单条', 'GET', '/order/refund/findById', '支付退款控制器', false, true, '支付退款控制器 查询单条', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945274, 'UserDataRoleController#findDataRoleByUser', '根据用户ID获取到数据角色列表', 'GET', '/user/data/role/findDataRoleByUser', '用户数据角色配置', false, true, '用户数据角色配置 根据用户ID获取到数据角色列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945275, 'ChinaRegionController#findAllRegionByParentCode', '根据编码获取下一级行政区划的列表', 'GET', '/china/region/findAllRegionByParentCode', '中国行政区划', false, true, '中国行政区划 根据编码获取下一级行政区划的列表', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945276, 'DataRoleController#existsByName', '名称是否被使用', 'GET', '/data/role/existsByName', '数据角色配置', false, true, '数据角色配置 名称是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945277, 'AllocationGroupController#create', '创建', 'POST', '/allocation/group/create', '分账组', false, true, '分账组 创建', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945278, 'UserAssistController#validatePhoneForgetCaptcha', '验证找回密码手机验证码', 'GET', '/user/validatePhoneForgetCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证找回密码手机验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945279, 'PayChannelConfigController#update', '更新', 'POST', '/pay/channel/config/update', '支付通道信息', false, true, '支付通道信息 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945280, 'DictionaryController#existsByCode', '编码是否被使用(不包含自己)', 'GET', '/dict/existsByCodeNotId', '字典', false, true, '字典 编码是否被使用(不包含自己)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945281, 'PermMenuController#menuTree', '获取菜单树', 'GET', '/perm/menu/menuTree', '菜单和权限码', false, true, '菜单和权限码 获取菜单树', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945282, 'DictionaryItemController#delete', '删除字典项', 'DELETE', '/dict/item/delete', '字典项', false, true, '字典项 删除字典项', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945283, 'DingRobotConfigController#existsByCode', '编码是否被使用', 'GET', '/ding/robot/config/existsByCode', '钉钉机器人配置', false, true, '钉钉机器人配置 编码是否被使用', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945284, 'SiteMessageController#countByReceiveNotRead', '获取未读的接收消息条数', 'GET', '/site/message/countByReceiveNotRead', '站内信', false, true, '站内信 获取未读的接收消息条数', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945285, 'WecomRobotConfigController#findAll', '查询全部', 'GET', '/wecom/robot/config/findAll', '企业微信机器人配置', false, true, '企业微信机器人配置 查询全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945286, 'PayReturnController#union', '云闪付同步通知', 'POST', '/return/pay/union', '支付同步通知', false, true, '支付同步通知 云闪付同步通知', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945287, 'UserAssistController#validateCurrentPhoneChangeCaptcha', '验证当前用户发送更改手机号验证码', 'GET', '/user/validateCurrentPhoneChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证当前用户发送更改手机号验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945288, 'WeChatTemplateController#page', '分页查询', 'GET', '/wechat/template/page', '微信模板消息', false, true, '微信模板消息 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945289, 'SmsTemplateController#add', '添加', 'POST', '/sms/template/add', '短信模板配置', false, true, '短信模板配置 添加', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945290, 'PermPathController#batchUpdateEnable', '批量更新状态', 'POST', '/perm/path/batchUpdateEnable', '请求权限管理', false, true, '请求权限管理 批量更新状态', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945291, 'DingRobotConfigController#findAll', '查询全部', 'GET', '/ding/robot/config/findAll', '钉钉机器人配置', false, true, '钉钉机器人配置 查询全部', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945292, 'QuartzJobController#syncJobStatus', '同步定时任务状态', 'POST', '/quartz/syncJobStatus', '定时任务', false, true, '定时任务 同步定时任务状态', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945293, 'SiteMessageController#cancel', '撤回消息', 'POST', '/site/message/cancel', '站内信', false, true, '站内信 撤回消息', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945294, 'DictionaryItemController#findAll', '获取全部字典项', 'GET', '/dict/item/findAll', '字典项', false, true, '字典项 获取全部字典项', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945295, 'UserAssistController#validateEmailCaptcha', '验证更改/绑定邮箱验证码', 'GET', '/user/validateEmailChangeCaptcha', '用户操作支撑服务', false, true, '用户操作支撑服务 验证更改/绑定邮箱验证码', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945296, 'PermMenuController#findById', '根据id查询', 'GET', '/perm/menu/findById', '菜单和权限码', false, true, '菜单和权限码 根据id查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945297, 'RoleMenuController#findPermissionIdsByRole', '获取当前角色下关联权限id集合(包含权限码和菜单)', 'GET', '/role/menu/findPermissionIdsByRole', '角色菜单权限关系', false, true, '角色菜单权限关系 获取当前角色下关联权限id集合(包含权限码和菜单)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945298, 'SwaggerConfigResource#openapiJson', 'openapiJson', 'GET', '/v3/api-docs/swagger-config', 'SwaggerConfigResource', false, true, 'SwaggerConfigResource openapiJson', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945299, 'UnionPayConfigController#update', '更新', 'POST', '/union/pay/config/update', '云闪付配置', false, true, '云闪付配置 更新', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945300, 'WecomRobotConfigController#delete', '删除', 'DELETE', '/wecom/robot/config/delete', '企业微信机器人配置', false, true, '企业微信机器人配置 删除', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945301, 'TokenEndpoint#login', '普通登录', 'POST', '/token/login', '认证相关', false, true, '认证相关 普通登录', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945302, 'RefundOrderController#refund', '手动发起退款', 'POST', '/order/refund/refund', '支付退款控制器', false, true, '支付退款控制器 手动发起退款', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945303, 'QuartzJobController#page', '分页', 'GET', '/quartz/page', '定时任务', false, true, '定时任务 分页', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945304, 'CockpitReportController#getPayAmount', '支付金额(分)', 'GET', '/report/cockpit/getPayAmount', '驾驶舱接口', false, true, '驾驶舱接口 支付金额(分)', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945305, 'SiteMessageController#saveOrUpdate', '保存站内信草稿', 'POST', '/site/message/saveOrUpdate', '站内信', false, true, '站内信 保存站内信草稿', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945306, 'UserinternalController#ban', '封禁用户', 'POST', '/user/internal/ban', '管理用户(管理员级别)', false, true, '管理用户(管理员级别) 封禁用户', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945307, 'MessageTemplateController#rendering', '渲染模板', 'POST', '/message/template/rendering', '消息模板', false, true, '消息模板 渲染模板', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945308, 'WalletController#create', '创建钱包', 'POST', '/wallet/create', '钱包管理', false, true, '钱包管理 创建钱包', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945309, 'PermPathController#syncSystem', '同步系统请求资源', 'POST', '/perm/path/syncSystem', '请求权限管理', false, true, '请求权限管理 同步系统请求资源', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945310, 'SystemParamController#existsByKey', '判断编码是否存在', 'GET', '/system/param/existsByKey', '系统参数', false, true, '系统参数 判断编码是否存在', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945311, 'BaseApiController#echo', '回声测试', 'GET', '/echo', '系统基础接口', false, true, '系统基础接口 回声测试', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945312, 'ReconcileOrderController#findDiffById', '对账差异详情', 'GET', '/order/reconcile/diff/findById', '对账控制器', false, true, '对账控制器 对账差异详情', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945313, 'PaySyncRecordController#page', '分页查询', 'GET', '/record/sync/page', '支付同步记录控制器', false, true, '支付同步记录控制器 分页查询', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);
INSERT INTO iam_perm_path (id, code, name, request_type, path, group_name, enable, generate, remark, creator, create_time, last_modifier, last_modified_time, deleted, version) VALUES (1789978117570945314, 'CashierController#getWxAuthUrl', '获取微信授权链接', 'GET', '/demo/cashier/getWxAuthUrl', '结算台演示', false, true, '结算台演示 获取微信授权链接', 1399985191002447872, '2024-05-13 19:16:54.071000', 1399985191002447872, '2024-05-13 19:16:54.071000', false, 0);

-----
create table iam_role
(
    id                 bigint  not null
        primary key,
    code               varchar(50),
    pid                bigint,
    name               varchar(150),
    internal           boolean not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table iam_role is '角色';

comment on column iam_role.id is '角色ID';

comment on column iam_role.code is '编码';

comment on column iam_role.pid is '父ID';

comment on column iam_role.name is '名称';

comment on column iam_role.internal is '是否系统内置';

comment on column iam_role.remark is '说明';

comment on column iam_role.creator is '创建人';

comment on column iam_role.create_time is '创建时间';

comment on column iam_role.last_modifier is '最后修改人';

comment on column iam_role.last_modified_time is '最后修改时间';

comment on column iam_role.version is '版本';

comment on column iam_role.deleted is '0:未删除。1:已删除';



INSERT INTO iam_role (id, code, pid, name, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757297023118462976, 'daxpayinternal', null, '支付网关管理员', false, '', 1399985191002447872, '2024-02-13 14:53:54.000000', 1399985191002447872, '2024-02-13 14:53:54.000000', 0, false);
INSERT INTO iam_role (id, code, pid, name, internal, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757298887092326400, 'daxpayDemo', 1757297023118462976, '支付演示角色', false, '用于进行演示的角色, 没有修改和删除的权限', 1399985191002447872, '2024-02-13 15:01:18.000000', 1399985191002447872, '2024-02-13 15:01:18.000000', 0, false);

-----
create table iam_role_menu
(
    id            bigint      not null
        primary key,
    role_id       bigint      not null,
    client_code   varchar(50) not null,
    permission_id bigint      not null
);

comment on table iam_role_menu is '角色菜单权限表';

comment on column iam_role_menu.role_id is '角色id';

comment on column iam_role_menu.client_code is '终端code';

comment on column iam_role_menu.permission_id is '菜单权限id';



INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520576, 1757297023118462976, 'dax-pay', 1744271715476684800);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520577, 1757297023118462976, 'dax-pay', 1744642856348520448);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520578, 1757297023118462976, 'dax-pay', 1745457623493496832);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520579, 1757297023118462976, 'dax-pay', 1745457746529210368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520580, 1757297023118462976, 'dax-pay', 1749262518385082368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520582, 1757297023118462976, 'dax-pay', 1744643265142165504);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520583, 1757297023118462976, 'dax-pay', 1745126072389963776);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520584, 1757297023118462976, 'dax-pay', 1745136155962347520);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520585, 1757297023118462976, 'dax-pay', 1745143528663781376);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520586, 1757297023118462976, 'dax-pay', 1745822093382230016);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520587, 1757297023118462976, 'dax-pay', 1744624886658318336);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520588, 1757297023118462976, 'dax-pay', 1744372631231995904);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520589, 1757297023118462976, 'dax-pay', 1744276101384880128);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298674730520590, 1757297023118462976, 'dax-pay', 1746194891925561344);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059200, 1757298887092326400, 'dax-pay', 1744271715476684800);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059201, 1757298887092326400, 'dax-pay', 1746194891925561344);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059202, 1757298887092326400, 'dax-pay', 1744276101384880128);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059203, 1757298887092326400, 'dax-pay', 1744372631231995904);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059204, 1757298887092326400, 'dax-pay', 1744624886658318336);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059205, 1757298887092326400, 'dax-pay', 1745822093382230016);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059206, 1757298887092326400, 'dax-pay', 1744642856348520448);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059207, 1757298887092326400, 'dax-pay', 1745457623493496832);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059208, 1757298887092326400, 'dax-pay', 1745457746529210368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059209, 1757298887092326400, 'dax-pay', 1749262518385082368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059210, 1757298887092326400, 'dax-pay', 1744643265142165504);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059212, 1757298887092326400, 'dax-pay', 1745126072389963776);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059213, 1757298887092326400, 'dax-pay', 1745136155962347520);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059214, 1757298887092326400, 'dax-pay', 1745143528663781376);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059215, 1757298887092326400, 'dax-pay', 1744271715476684800);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059216, 1757298887092326400, 'dax-pay', 1746194891925561344);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059217, 1757298887092326400, 'dax-pay', 1744276101384880128);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059218, 1757298887092326400, 'dax-pay', 1744372631231995904);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059219, 1757298887092326400, 'dax-pay', 1744624886658318336);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059220, 1757298887092326400, 'dax-pay', 1745822093382230016);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059221, 1757298887092326400, 'dax-pay', 1744642856348520448);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059222, 1757298887092326400, 'dax-pay', 1745457623493496832);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059223, 1757298887092326400, 'dax-pay', 1745457746529210368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059224, 1757298887092326400, 'dax-pay', 1749262518385082368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059225, 1757298887092326400, 'dax-pay', 1744643265142165504);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059227, 1757298887092326400, 'dax-pay', 1745126072389963776);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059228, 1757298887092326400, 'dax-pay', 1745136155962347520);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059229, 1757298887092326400, 'dax-pay', 1745143528663781376);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059230, 1757298887092326400, 'dax-pay', 1744271715476684800);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059231, 1757298887092326400, 'dax-pay', 1746194891925561344);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059232, 1757298887092326400, 'dax-pay', 1744276101384880128);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059233, 1757298887092326400, 'dax-pay', 1744372631231995904);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059234, 1757298887092326400, 'dax-pay', 1744624886658318336);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059235, 1757298887092326400, 'dax-pay', 1745822093382230016);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059236, 1757298887092326400, 'dax-pay', 1744642856348520448);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059237, 1757298887092326400, 'dax-pay', 1745457623493496832);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059238, 1757298887092326400, 'dax-pay', 1745457746529210368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059239, 1757298887092326400, 'dax-pay', 1749262518385082368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059240, 1757298887092326400, 'dax-pay', 1744643265142165504);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059242, 1757298887092326400, 'dax-pay', 1745126072389963776);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059243, 1757298887092326400, 'dax-pay', 1745136155962347520);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059244, 1757298887092326400, 'dax-pay', 1745143528663781376);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059245, 1757298887092326400, 'dax-pay', 1744271715476684800);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059246, 1757298887092326400, 'dax-pay', 1746194891925561344);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059247, 1757298887092326400, 'dax-pay', 1744276101384880128);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059248, 1757298887092326400, 'dax-pay', 1744372631231995904);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059249, 1757298887092326400, 'dax-pay', 1744624886658318336);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059250, 1757298887092326400, 'dax-pay', 1745822093382230016);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059251, 1757298887092326400, 'dax-pay', 1744642856348520448);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059252, 1757298887092326400, 'dax-pay', 1745457623493496832);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059253, 1757298887092326400, 'dax-pay', 1745457746529210368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059254, 1757298887092326400, 'dax-pay', 1749262518385082368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059255, 1757298887092326400, 'dax-pay', 1744643265142165504);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059257, 1757298887092326400, 'dax-pay', 1745126072389963776);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059258, 1757298887092326400, 'dax-pay', 1745136155962347520);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1757298924107059259, 1757298887092326400, 'dax-pay', 1745143528663781376);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282006073350, 1757297023118462976, 'dax-pay', 1761429304959528960);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282006073351, 1757297023118462976, 'dax-pay', 1761429682618855424);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282006073352, 1757297023118462976, 'dax-pay', 1759768820429352960);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282006073353, 1757297023118462976, 'dax-pay', 1759769092698402816);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282232565766, 1757298887092326400, 'dax-pay', 1761429304959528960);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282232565767, 1757298887092326400, 'dax-pay', 1761429682618855424);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282232565768, 1757298887092326400, 'dax-pay', 1759768820429352960);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1762112282232565769, 1757298887092326400, 'dax-pay', 1759769092698402816);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732268605440, 1757297023118462976, 'dax-pay', 1775089099078553600);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732268605441, 1757297023118462976, 'dax-pay', 1775089820368818176);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732268605442, 1757297023118462976, 'dax-pay', 1775091561835450368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732268605443, 1757297023118462976, 'dax-pay', 1777688382748700672);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732574789632, 1757298887092326400, 'dax-pay', 1775089099078553600);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732574789633, 1757298887092326400, 'dax-pay', 1775089820368818176);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732574789634, 1757298887092326400, 'dax-pay', 1775091561835450368);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1780416732574789635, 1757298887092326400, 'dax-pay', 1777688382748700672);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742020689920, 1757297023118462976, 'dax-pay', 1786808188825194496);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742020689921, 1757297023118462976, 'dax-pay', 1786810890951020544);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742020689922, 1757297023118462976, 'dax-pay', 1744930046228017152);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742314291200, 1757298887092326400, 'dax-pay', 1786808188825194496);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742314291201, 1757298887092326400, 'dax-pay', 1786810890951020544);
INSERT INTO iam_role_menu (id, role_id, client_code, permission_id) VALUES (1789979742314291202, 1757298887092326400, 'dax-pay', 1744930046228017152);

-----
create table iam_role_path
(
    id            bigint not null
        primary key,
    role_id       bigint not null,
    permission_id bigint not null
);

comment on table iam_role_path is '角色请求权限表';

comment on column iam_role_path.role_id is '角色id';

comment on column iam_role_path.permission_id is '请求权限id';



INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343680, 1757297023118462976, 1789978117570945313);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343681, 1757297023118462976, 1789978117566750768);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343682, 1757297023118462976, 1789978117570945312);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343683, 1757297023118462976, 1789978117570945264);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343684, 1757297023118462976, 1789978117570945244);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343685, 1757297023118462976, 1789978117570945215);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343686, 1757297023118462976, 1789978117570945203);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343687, 1757297023118462976, 1789978117570945080);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343688, 1757297023118462976, 1789978117570945074);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343689, 1757297023118462976, 1789978117570945029);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343690, 1757297023118462976, 1789978117570945024);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343691, 1757297023118462976, 1789978117566750819);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343692, 1757297023118462976, 1789978117566750783);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343693, 1757297023118462976, 1789978117566750760);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343694, 1757297023118462976, 1789978117566750746);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343695, 1757297023118462976, 1789978117562556431);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343696, 1757297023118462976, 1789978117570945311);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343697, 1757297023118462976, 1789978117570945126);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343698, 1757297023118462976, 1789978117570945304);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343699, 1757297023118462976, 1789978117570945269);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343700, 1757297023118462976, 1789978117570945230);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343701, 1757297023118462976, 1789978117570945129);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343702, 1757297023118462976, 1789978117566750730);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343703, 1757297023118462976, 1789978117562556448);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343704, 1757297023118462976, 1789978117570945302);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343705, 1757297023118462976, 1789978117570945273);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343706, 1757297023118462976, 1789978117570945213);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343707, 1757297023118462976, 1789978117570945177);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343708, 1757297023118462976, 1789978117570945161);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343709, 1757297023118462976, 1789978117570945136);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343710, 1757297023118462976, 1789978117566750763);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343711, 1757297023118462976, 1789978117566750728);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343712, 1757297023118462976, 1789978117570945299);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343713, 1757297023118462976, 1789978117570945222);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343714, 1757297023118462976, 1789978117570945124);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343715, 1757297023118462976, 1789978117566750820);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343716, 1757297023118462976, 1789978117570945286);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343717, 1757297023118462976, 1789978117570945072);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343718, 1757297023118462976, 1789978117570945064);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343719, 1757297023118462976, 1789978117570945279);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343720, 1757297023118462976, 1789978117570945028);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343721, 1757297023118462976, 1789978117566750751);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343722, 1757297023118462976, 1789978117570945277);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343723, 1757297023118462976, 1789978117570945248);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343724, 1757297023118462976, 1789978117570945234);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343725, 1757297023118462976, 1789978117570945170);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343726, 1757297023118462976, 1789978117570945111);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343727, 1757297023118462976, 1789978117570945100);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343728, 1757297023118462976, 1789978117570945038);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343729, 1757297023118462976, 1789978117566750807);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343730, 1757297023118462976, 1789978117566750789);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343731, 1757297023118462976, 1789978117566750759);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343732, 1757297023118462976, 1789978117566750744);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343733, 1757297023118462976, 1789978117562556423);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343734, 1757297023118462976, 1789978117570945266);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343735, 1757297023118462976, 1789978117570945251);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343736, 1757297023118462976, 1789978117570945073);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343737, 1757297023118462976, 1789978117570945056);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343738, 1757297023118462976, 1789978117566750822);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343739, 1757297023118462976, 1789978117566750809);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343740, 1757297023118462976, 1789978117566750803);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343741, 1757297023118462976, 1789978117566750726);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343742, 1757297023118462976, 1789978117570945265);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343743, 1757297023118462976, 1789978117570945163);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343744, 1757297023118462976, 1789978117570945144);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343745, 1757297023118462976, 1789978117570945258);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343746, 1757297023118462976, 1789978117570945237);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343747, 1757297023118462976, 1789978117570945184);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343748, 1757297023118462976, 1789978117570945162);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343749, 1757297023118462976, 1789978117570945116);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343750, 1757297023118462976, 1789978117570945115);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343751, 1757297023118462976, 1789978117570945085);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343752, 1757297023118462976, 1789978117562556418);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343753, 1757297023118462976, 1789978117570945236);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343754, 1757297023118462976, 1789978117570945098);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343755, 1757297023118462976, 1789978117570945231);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343756, 1757297023118462976, 1789978117570945192);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343757, 1757297023118462976, 1789978117570945181);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343758, 1757297023118462976, 1789978117570945172);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343759, 1757297023118462976, 1789978117570945114);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343760, 1757297023118462976, 1789978117570945082);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343761, 1757297023118462976, 1789978117566750740);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343762, 1757297023118462976, 1789978117562556428);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343763, 1757297023118462976, 1789978117562556420);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343764, 1757297023118462976, 1789978117570945198);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343765, 1757297023118462976, 1789978117570945182);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343766, 1757297023118462976, 1789978117570945078);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343767, 1757297023118462976, 1789978117570945067);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343768, 1757297023118462976, 1789978117570945044);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343769, 1757297023118462976, 1789978117562556452);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343770, 1757297023118462976, 1789978117562556425);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343771, 1757297023118462976, 1789978117570945211);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343772, 1757297023118462976, 1789978117570945200);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343773, 1757297023118462976, 1789978117570945171);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343774, 1757297023118462976, 1789978117562556419);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343775, 1757297023118462976, 1789978117570945189);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343776, 1757297023118462976, 1789978117570945048);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343777, 1757297023118462976, 1789978117562556429);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343778, 1757297023118462976, 1789978117570945165);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343779, 1757297023118462976, 1789978117562556435);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343780, 1757297023118462976, 1789978117570945155);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343781, 1757297023118462976, 1789978117566750762);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343782, 1757297023118462976, 1789978117570945150);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343783, 1757297023118462976, 1789978117566750753);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343784, 1757297023118462976, 1789978117570945107);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343785, 1757297023118462976, 1789978117566750736);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343786, 1757297023118462976, 1789978117570945104);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979321814343787, 1757297023118462976, 1789978117566750742);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636736, 1757298887092326400, 1789978117570945313);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636737, 1757298887092326400, 1789978117566750768);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636738, 1757298887092326400, 1789978117570945312);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636739, 1757298887092326400, 1789978117570945264);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636740, 1757298887092326400, 1789978117570945244);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636741, 1757298887092326400, 1789978117570945215);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636742, 1757298887092326400, 1789978117570945203);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636743, 1757298887092326400, 1789978117570945080);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636744, 1757298887092326400, 1789978117570945074);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636745, 1757298887092326400, 1789978117570945029);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636746, 1757298887092326400, 1789978117570945024);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636747, 1757298887092326400, 1789978117566750819);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636748, 1757298887092326400, 1789978117566750783);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636749, 1757298887092326400, 1789978117566750760);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636750, 1757298887092326400, 1789978117566750746);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636751, 1757298887092326400, 1789978117562556431);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636752, 1757298887092326400, 1789978117570945311);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636753, 1757298887092326400, 1789978117570945126);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636754, 1757298887092326400, 1789978117570945304);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636755, 1757298887092326400, 1789978117570945269);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636756, 1757298887092326400, 1789978117570945230);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636757, 1757298887092326400, 1789978117570945129);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636758, 1757298887092326400, 1789978117566750730);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636759, 1757298887092326400, 1789978117562556448);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636760, 1757298887092326400, 1789978117570945302);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636761, 1757298887092326400, 1789978117570945273);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636762, 1757298887092326400, 1789978117570945213);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636763, 1757298887092326400, 1789978117570945177);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636764, 1757298887092326400, 1789978117570945161);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636765, 1757298887092326400, 1789978117570945136);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636766, 1757298887092326400, 1789978117566750763);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636767, 1757298887092326400, 1789978117566750728);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636769, 1757298887092326400, 1789978117570945222);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636771, 1757298887092326400, 1789978117566750820);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636772, 1757298887092326400, 1789978117570945286);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636773, 1757298887092326400, 1789978117570945072);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636774, 1757298887092326400, 1789978117570945064);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636775, 1757298887092326400, 1789978117570945279);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636776, 1757298887092326400, 1789978117570945028);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636777, 1757298887092326400, 1789978117566750751);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636778, 1757298887092326400, 1789978117570945277);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636783, 1757298887092326400, 1789978117570945100);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636786, 1757298887092326400, 1789978117566750789);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636790, 1757298887092326400, 1789978117570945266);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636791, 1757298887092326400, 1789978117570945251);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636792, 1757298887092326400, 1789978117570945073);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636793, 1757298887092326400, 1789978117570945056);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636794, 1757298887092326400, 1789978117566750822);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636795, 1757298887092326400, 1789978117566750809);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636796, 1757298887092326400, 1789978117566750803);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636797, 1757298887092326400, 1789978117566750726);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636799, 1757298887092326400, 1789978117570945163);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636800, 1757298887092326400, 1789978117570945144);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636801, 1757298887092326400, 1789978117570945258);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636802, 1757298887092326400, 1789978117570945237);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636803, 1757298887092326400, 1789978117570945184);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636804, 1757298887092326400, 1789978117570945162);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636805, 1757298887092326400, 1789978117570945116);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636806, 1757298887092326400, 1789978117570945115);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636807, 1757298887092326400, 1789978117570945085);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636808, 1757298887092326400, 1789978117562556418);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636809, 1757298887092326400, 1789978117570945236);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636810, 1757298887092326400, 1789978117570945098);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636811, 1757298887092326400, 1789978117570945231);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636812, 1757298887092326400, 1789978117570945192);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636813, 1757298887092326400, 1789978117570945181);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636814, 1757298887092326400, 1789978117570945172);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636815, 1757298887092326400, 1789978117570945114);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636816, 1757298887092326400, 1789978117570945082);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636817, 1757298887092326400, 1789978117566750740);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636818, 1757298887092326400, 1789978117562556428);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636819, 1757298887092326400, 1789978117562556420);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636820, 1757298887092326400, 1789978117570945198);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636821, 1757298887092326400, 1789978117570945182);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636822, 1757298887092326400, 1789978117570945078);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636823, 1757298887092326400, 1789978117570945067);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636824, 1757298887092326400, 1789978117570945044);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636825, 1757298887092326400, 1789978117562556452);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636826, 1757298887092326400, 1789978117562556425);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636828, 1757298887092326400, 1789978117570945200);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636829, 1757298887092326400, 1789978117570945171);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636830, 1757298887092326400, 1789978117562556419);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636831, 1757298887092326400, 1789978117570945189);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636832, 1757298887092326400, 1789978117570945048);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636835, 1757298887092326400, 1789978117562556435);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636836, 1757298887092326400, 1789978117570945155);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636837, 1757298887092326400, 1789978117566750762);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636838, 1757298887092326400, 1789978117570945150);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636839, 1757298887092326400, 1789978117566750753);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636840, 1757298887092326400, 1789978117570945107);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636841, 1757298887092326400, 1789978117566750736);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636842, 1757298887092326400, 1789978117570945104);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979322187636843, 1757298887092326400, 1789978117566750742);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908043825152, 1757297023118462976, 1789978117570945084);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908043825153, 1757297023118462976, 1789978117570945059);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908043825154, 1757297023118462976, 1789978117570945039);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908043825155, 1757297023118462976, 1789978117566750797);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908324843520, 1757298887092326400, 1789978117570945084);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908324843521, 1757298887092326400, 1789978117570945059);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789979908324843522, 1757298887092326400, 1789978117570945039);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303872, 1757297023118462976, 1789978117570945308);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303873, 1757297023118462976, 1789978117570945239);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303874, 1757297023118462976, 1789978117570945186);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303875, 1757297023118462976, 1789978117570945052);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303876, 1757297023118462976, 1789978117566750795);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303877, 1757297023118462976, 1789978117566750741);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303878, 1757297023118462976, 1789978117570945094);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303879, 1757297023118462976, 1789978117566750758);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023252303880, 1757297023118462976, 1789978117566750733);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127937, 1757298887092326400, 1789978117570945239);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127938, 1757298887092326400, 1789978117570945186);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127939, 1757298887092326400, 1789978117570945052);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127940, 1757298887092326400, 1789978117566750795);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127941, 1757298887092326400, 1789978117566750741);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127942, 1757298887092326400, 1789978117570945094);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789982023529127943, 1757298887092326400, 1789978117566750758);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1789986071959429120, 1757298887092326400, 1789978117570945124);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320001957888, 1757297023118462976, 1789978117570945272);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320001957889, 1757297023118462976, 1789978117570945194);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320001957890, 1757297023118462976, 1789978117570945175);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320001957891, 1757297023118462976, 1789978117570945164);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320001957892, 1757297023118462976, 1789978117566750801);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320102621184, 1757298887092326400, 1789978117570945272);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320102621185, 1757298887092326400, 1789978117570945194);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320102621186, 1757298887092326400, 1789978117570945175);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320102621187, 1757298887092326400, 1789978117570945164);
INSERT INTO iam_role_path (id, role_id, permission_id) VALUES (1790355320102621188, 1757298887092326400, 1789978117566750801);

-----
create table iam_user_data_role
(
    id      bigint not null
        primary key,
    user_id bigint not null,
    role_id bigint not null
);

comment on table iam_user_data_role is '用户数据范围关系
';

comment on column iam_user_data_role.id is '主键';

comment on column iam_user_data_role.user_id is '用户ID';

comment on column iam_user_data_role.role_id is '数据角色ID';




-----
create table iam_user_dept
(
    id      bigint not null
        primary key,
    user_id bigint not null,
    dept_id bigint not null
);

comment on table iam_user_dept is '用户部门关联表';

comment on column iam_user_dept.user_id is '用户id';

comment on column iam_user_dept.dept_id is '部门id';




-----
create table iam_user_expand_info
(
    id                        bigint       not null
        primary key,
    sex                       integer,
    birthday                  date,
    avatar                    varchar(255),
    last_login_time           timestamp(6),
    current_login_time        timestamp(6),
    initial_password          boolean      not null,
    expire_password           boolean      not null,
    last_change_password_time timestamp(6),
    register_time             timestamp(6) not null,
    creator                   bigint,
    create_time               timestamp(6),
    last_modifier             bigint,
    last_modified_time        timestamp(6),
    version                   integer      not null,
    deleted                   boolean      not null
);

comment on table iam_user_expand_info is '用户扩展信息';

comment on column iam_user_expand_info.id is '主键';

comment on column iam_user_expand_info.sex is '性别';

comment on column iam_user_expand_info.birthday is '生日';

comment on column iam_user_expand_info.avatar is '头像';

comment on column iam_user_expand_info.last_login_time is '上次登录时间';

comment on column iam_user_expand_info.current_login_time is '本次登录时间';

comment on column iam_user_expand_info.initial_password is '是否初始密码';

comment on column iam_user_expand_info.expire_password is '密码是否过期';

comment on column iam_user_expand_info.last_change_password_time is '上次修改密码时间';

comment on column iam_user_expand_info.register_time is '注册时间';

comment on column iam_user_expand_info.creator is '创建者ID';

comment on column iam_user_expand_info.create_time is '创建时间';

comment on column iam_user_expand_info.last_modifier is '最后修者ID';

comment on column iam_user_expand_info.last_modified_time is '最后修改时间';

comment on column iam_user_expand_info.version is '乐观锁';

comment on column iam_user_expand_info.deleted is '删除标志';



INSERT INTO iam_user_expand_info (id, sex, birthday, avatar, last_login_time, current_login_time, initial_password, expire_password, last_change_password_time, register_time, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757317255899869184, null, null, null, '2024-04-25 22:10:31.000000', '2024-04-26 09:22:54.000000', false, false, null, '2024-02-13 16:14:18.000000', 1399985191002447872, '2024-02-13 16:14:18.000000', 0, '2024-04-26 09:22:54.000000', 9, false);
INSERT INTO iam_user_expand_info (id, sex, birthday, avatar, last_login_time, current_login_time, initial_password, expire_password, last_change_password_time, register_time, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757299137932677120, 1, '2024-02-13', null, '2024-05-26 00:09:21.000000', '2024-05-31 00:04:11.682000', false, false, null, '2024-02-13 15:02:18.000000', 1399985191002447872, '2024-02-13 15:02:18.000000', 0, '2024-05-31 00:04:11.686000', 8, false);
INSERT INTO iam_user_expand_info (id, sex, birthday, avatar, last_login_time, current_login_time, initial_password, expire_password, last_change_password_time, register_time, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1399985191002447872, 1, '1996-12-01', null, '2024-05-31 20:50:02.184000', '2024-05-31 21:50:08.425000', false, false, '2023-10-19 14:14:08.000000', '2021-08-01 18:52:37.000000', 1, '2021-06-02 15:04:15.000000', 0, '2024-05-31 21:50:08.426000', 457, false);

-----
create table iam_user_info
(
    id                 bigint       not null
        primary key,
    name               varchar(200) not null,
    username           varchar(50)  not null,
    password           varchar(255) not null,
    phone              varchar(32),
    email              varchar(255),
    client_ids         text,
    internalistrator      boolean      not null,
    status             varchar(55)  not null,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer      not null,
    deleted            boolean      not null
);

comment on table iam_user_info is '用户信息';

comment on column iam_user_info.name is '名称';

comment on column iam_user_info.username is '账号';

comment on column iam_user_info.password is '密码';

comment on column iam_user_info.phone is '手机号';

comment on column iam_user_info.email is '邮箱';

comment on column iam_user_info.client_ids is '关联终端ds';

comment on column iam_user_info.internalistrator is '是否超级管理员';

comment on column iam_user_info.status is '账号状态';

comment on column iam_user_info.creator is '创建人';

comment on column iam_user_info.create_time is '创建时间';

comment on column iam_user_info.last_modifier is '最后修改人';

comment on column iam_user_info.last_modified_time is '最后修改时间';

comment on column iam_user_info.version is '版本';

comment on column iam_user_info.deleted is '0:未删除。1:已删除';



INSERT INTO iam_user_info (id, name, username, password, phone, email, client_ids, internalistrator, status, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1399985191002447872, 'Bootx', 'bootx', 'f52020dca765fd3943ed40a615dc2c5c', '13333333333', 'bootx@bootx.cn', '1430430071299207168,1430430071299207169,1626840094767714304,1580487061605175296', true, 'normal', 1, '2021-06-02 15:04:15.000000', 1399985191002447872, '2024-04-11 18:55:34.000000', 65, false);
INSERT INTO iam_user_info (id, name, username, password, phone, email, client_ids, internalistrator, status, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757299137932677120, 'DaxPay演示', 'daxpay', 'f52020dca765fd3943ed40a615dc2c5c', '14443332251', 'daxpay@qq.com', '1580487061605175296', false, 'normal', 1399985191002447872, '2024-02-13 15:02:18.000000', 1757299137932677120, '2024-02-13 16:09:44.000000', 2, false);
INSERT INTO iam_user_info (id, name, username, password, phone, email, client_ids, internalistrator, status, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1757317255899869184, 'DaxPay管理员', 'daxpayinternal', '1e9e3c3deaa4a06d08326e82a49d1b63', '12222333223', 'daxpayinternal@qq.com', '1580487061605175296', false, 'normal', 1399985191002447872, '2024-02-13 16:14:18.000000', 1399985191002447872, '2024-02-13 16:14:18.000000', 0, false);

-----
create table iam_user_role
(
    id      bigint not null
        primary key,
    user_id bigint not null,
    role_id bigint not null
);

comment on table iam_user_role is '用户角色关系
';

comment on column iam_user_role.id is '主键';

comment on column iam_user_role.user_id is '用户ID';

comment on column iam_user_role.role_id is '角色ID';



INSERT INTO iam_user_role (id, user_id, role_id) VALUES (1757299293314863104, 1757299137932677120, 1757298887092326400);
INSERT INTO iam_user_role (id, user_id, role_id) VALUES (1757317495407210496, 1757317255899869184, 1757297023118462976);

-----
create table iam_user_third
(
    id                 bigint  not null
        primary key,
    user_id            bigint,
    we_chat_id         varchar(50),
    we_chat_open_id    varchar(50),
    qq_id              varchar(50),
    weibo_id           varchar(50),
    gitee_id           varchar(50),
    ding_talk_id       varchar(50),
    we_com_id          varchar(50),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table iam_user_third is '用户三方登录绑定';

comment on column iam_user_third.id is '主键';

comment on column iam_user_third.user_id is '用户id';

comment on column iam_user_third.we_chat_id is '微信openId';

comment on column iam_user_third.we_chat_open_id is '微信开放平台id';

comment on column iam_user_third.qq_id is 'qqId';

comment on column iam_user_third.weibo_id is '微博Id';

comment on column iam_user_third.gitee_id is '码云唯一标识';

comment on column iam_user_third.ding_talk_id is '钉钉唯一标识';

comment on column iam_user_third.we_com_id is '企业微信唯一标识';

comment on column iam_user_third.creator is '创建人';

comment on column iam_user_third.create_time is '创建时间';

comment on column iam_user_third.last_modifier is '最后修改人';

comment on column iam_user_third.last_modified_time is '最后修改时间';

comment on column iam_user_third.version is '版本';

comment on column iam_user_third.deleted is '0:未删除。1:已删除';



create index pk_user_index
    on iam_user_third (user_id);

comment on index pk_user_index is '用户id索引';


-----
create table iam_user_third_info
(
    id                 bigint      not null
        primary key,
    user_id            bigint      not null,
    client_code        varchar(50) not null,
    username           varchar(255),
    nickname           varchar(255),
    avatar             varchar(255),
    third_user_id      varchar(64),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer     not null
);

comment on table iam_user_third_info is '用户三方登录绑定详情';

comment on column iam_user_third_info.id is '主键';

comment on column iam_user_third_info.user_id is '用户id';

comment on column iam_user_third_info.client_code is '第三方终端类型';

comment on column iam_user_third_info.username is '用户名';

comment on column iam_user_third_info.nickname is '用户昵称';

comment on column iam_user_third_info.avatar is '用户头像';

comment on column iam_user_third_info.third_user_id is '关联第三方平台的用户id';

comment on column iam_user_third_info.creator is '创建人';

comment on column iam_user_third_info.create_time is '创建时间';

comment on column iam_user_third_info.last_modifier is '最后修改人';

comment on column iam_user_third_info.last_modified_time is '最后修改时间';

comment on column iam_user_third_info.version is '版本';



create index pk_user_client
    on iam_user_third_info (user_id, client_code);

comment on index pk_user_client is '用户id和终端code';


-----
create table notice_mail_config
(
    id                 bigint       not null
        primary key,
    code               varchar(32),
    name               varchar(64),
    host               varchar(128) not null,
    port               integer      not null,
    username           varchar(128) not null,
    password           varchar(128) not null,
    sender             varchar(128),
    from_              varchar(128),
    activity           boolean,
    security_type      integer,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean,
    version            integer
);

comment on table notice_mail_config is '邮件配置';

comment on column notice_mail_config.code is '编号';

comment on column notice_mail_config.name is '名称';

comment on column notice_mail_config.host is '邮箱服务器host';

comment on column notice_mail_config.port is '邮箱服务器 port';

comment on column notice_mail_config.username is '邮箱服务器 username';

comment on column notice_mail_config.password is '邮箱服务器 password';

comment on column notice_mail_config.sender is '邮箱服务器 sender';

comment on column notice_mail_config.from_ is '邮箱服务器 from';

comment on column notice_mail_config.activity is '是否默认配置，0:否。1:是';

comment on column notice_mail_config.security_type is '安全传输方式 1:plain 2:tls 3:ssl';

comment on column notice_mail_config.creator is '创建人';

comment on column notice_mail_config.create_time is '创建时间';

comment on column notice_mail_config.last_modifier is '最后修改人';

comment on column notice_mail_config.last_modified_time is '最后修改时间';

comment on column notice_mail_config.deleted is '0:未删除。1:已删除';

comment on column notice_mail_config.version is '版本';




-----
create table notice_message_template
(
    id                 bigint       not null
        primary key,
    code               varchar(255) not null,
    name               varchar(255),
    data               text,
    type               varchar(50),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer      not null,
    deleted            boolean      not null
);

comment on table notice_message_template is '消息模板';

comment on column notice_message_template.code is '编码';

comment on column notice_message_template.name is '名称';

comment on column notice_message_template.data is '模板数据';

comment on column notice_message_template.type is '模板类型';

comment on column notice_message_template.remark is '备注';

comment on column notice_message_template.creator is '创建人';

comment on column notice_message_template.create_time is '创建时间';

comment on column notice_message_template.last_modifier is '最后修改人';

comment on column notice_message_template.last_modified_time is '最后修改时间';

comment on column notice_message_template.version is '版本';

comment on column notice_message_template.deleted is '0:未删除。1:已删除';



INSERT INTO notice_message_template (id, code, name, data, type, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1424936204932169730, 'cs', '测试', 'hello ${msg}6666666666666666666666666666', '1', '测试模板', 0, '2021-08-10 11:30:40.000000', 0, '2021-08-10 11:30:40.000000', 0, false);

-----
create table notice_site_message
(
    id                 bigint  not null
        primary key,
    title              varchar(255),
    content            text,
    sender_id          bigint,
    sender_name        varchar(255),
    sender_time        timestamp(6),
    receive_type       varchar(50),
    send_state         varchar(50),
    efficient_time     timestamp(6),
    cancel_time        timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table notice_site_message is '站内信';

comment on column notice_site_message.id is '主键';

comment on column notice_site_message.title is '消息标题';

comment on column notice_site_message.content is '消息内容';

comment on column notice_site_message.sender_id is '发送者id';

comment on column notice_site_message.sender_name is '发送者姓名';

comment on column notice_site_message.sender_time is '发送时间';

comment on column notice_site_message.receive_type is '消息类型';

comment on column notice_site_message.send_state is '发布状态';

comment on column notice_site_message.efficient_time is '截至有效期';

comment on column notice_site_message.cancel_time is '撤回时间';

comment on column notice_site_message.creator is '创建人';

comment on column notice_site_message.create_time is '创建时间';

comment on column notice_site_message.last_modifier is '最后修改人';

comment on column notice_site_message.last_modified_time is '最后修改时间';

comment on column notice_site_message.version is '版本';

comment on column notice_site_message.deleted is '0:未删除。1:已删除';



INSERT INTO notice_site_message (id, title, content, sender_id, sender_name, sender_time, receive_type, send_state, efficient_time, cancel_time, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1796213354295259136, 'aaa', '<p>aaaa</p>', 1399985191002447872, 'Bootx', '2024-05-31 00:16:27.231000', 'all', 'sent', '2024-06-30 00:00:00.000000', null, 1399985191002447872, '2024-05-31 00:13:30.354000', 1399985191002447872, '2024-05-31 00:16:27.238000', 1, false);
INSERT INTO notice_site_message (id, title, content, sender_id, sender_name, sender_time, receive_type, send_state, efficient_time, cancel_time, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1796527782318743552, '已完成PGSQL的适配', '<p>已完成PGSQL的适配</p>', 1399985191002447872, 'Bootx', '2024-05-31 21:02:59.349000', 'all', 'sent', '2024-06-30 00:00:00.000000', null, 1399985191002447872, '2024-05-31 21:02:55.833000', 1399985191002447872, '2024-05-31 21:02:59.351000', 1, false);

-----
create table notice_site_message_user
(
    id          bigint                not null
        primary key,
    message_id  bigint                not null,
    receive_id  bigint                not null,
    have_read   boolean               not null,
    read_time   timestamp(6),
    creator     bigint,
    create_time timestamp(6),
    deleted     boolean default false not null
);

comment on table notice_site_message_user is '消息用户关联';

comment on column notice_site_message_user.id is '主键';

comment on column notice_site_message_user.message_id is '消息id';

comment on column notice_site_message_user.receive_id is '接收者id';

comment on column notice_site_message_user.have_read is '已读/未读';

comment on column notice_site_message_user.read_time is '已读时间';

comment on column notice_site_message_user.creator is '创建人';

comment on column notice_site_message_user.create_time is '创建时间';

comment on column notice_site_message_user.deleted is '0:未删除。1:已删除';



create index inx_message
    on notice_site_message_user (message_id);

comment on index inx_message is '消息索引';

create unique index uni_receive_message
    on notice_site_message_user (receive_id, message_id);

comment on index uni_receive_message is '接收人和消息联合索引';

INSERT INTO notice_site_message_user (id, message_id, receive_id, have_read, read_time, creator, create_time, deleted) VALUES (1796214068241297408, 1796213354295259136, 1399985191002447872, true, '2024-05-31 00:16:20.565000', 1399985191002447872, '2024-05-31 00:16:20.570000', false);
INSERT INTO notice_site_message_user (id, message_id, receive_id, have_read, read_time, creator, create_time, deleted) VALUES (1796539685040463872, 1796527782318743552, 1399985191002447872, true, '2024-05-31 21:50:13.662000', 1399985191002447872, '2024-05-31 21:50:13.663000', false);

-----
create table notice_sms_channel_config
(
    id                 bigint  not null
        primary key,
    code               varchar(255),
    name               varchar(255),
    state              varchar(255),
    access_key         varchar(255),
    config             text,
    remark             varchar(255),
    access_secret      varchar(255),
    image              bigint,
    sort_no            double precision,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table notice_sms_channel_config is '短信渠道配置';

comment on column notice_sms_channel_config.id is '主键';

comment on column notice_sms_channel_config.code is '渠道类型编码';

comment on column notice_sms_channel_config.name is '渠道类型名称';

comment on column notice_sms_channel_config.state is '状态';

comment on column notice_sms_channel_config.access_key is 'AccessKey';

comment on column notice_sms_channel_config.config is '配置字符串';

comment on column notice_sms_channel_config.remark is '备注';

comment on column notice_sms_channel_config.access_secret is 'AccessSecret';

comment on column notice_sms_channel_config.image is '图片';

comment on column notice_sms_channel_config.sort_no is '排序';

comment on column notice_sms_channel_config.creator is '创建者ID';

comment on column notice_sms_channel_config.create_time is '创建时间';

comment on column notice_sms_channel_config.last_modifier is '最后修者ID';

comment on column notice_sms_channel_config.last_modified_time is '最后修改时间';

comment on column notice_sms_channel_config.version is '乐观锁';

comment on column notice_sms_channel_config.deleted is '删除标志';



INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688432603289337856, 'alibaba', '阿里云短信', 'normal', '1', '{"accessKeyId":"1231231231232111","accessKeySecret":"12312312321111","signature":"1231","templateId":"2312313","templateName":"123","requestUrl":"五千二无二","action":"SendSms","version":"11","regionId":"cn-hangzhou"}', null, '1', null, 0, 1414143554414059520, '2023-08-07 14:11:17.000000', 1414143554414059520, '2023-08-07 15:35:34.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688456604610953216, 'huawei', '华为云短信', 'normal', '1', '{"appKey":"1231231231232","appSecret":"1111","signature":"1","sender":"1","templateId":"12","statusCallBack":"1","url":"1"}', null, '1', null, 0, 1414143554414059520, '2023-08-07 15:46:39.000000', 1414143554414059520, '2023-08-07 15:46:49.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688461302302732288, 'yunpian', '云片短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"callbackUrl":null,"templateName":null}', null, '1', null, 0, 1414143554414059520, '2023-08-07 16:05:19.000000', 1414143554414059520, '2023-08-08 14:12:23.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788784751001600, 'tencent', '腾讯短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"sdkAppId":null,"territory":"ap-guangzhou","connTimeout":60,"requestUrl":"sms.tencentcloudapi.com","action":"SendSms","version":"2021-01-11","service":"sms"}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:46:37.000000', 1414143554414059520, '2023-08-08 13:46:37.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788807228276736, 'uni_sms', '合一短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"isSimple":true,"templateName":null}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:46:42.000000', 1414143554414059520, '2023-08-08 13:46:43.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788823900635136, 'netease', '网易云短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"templateName":null,"templateUrl":"https://api.netease.im/sms/sendtemplate.action","codeUrl":"https://api.netease.im/sms/sendcode.action","verifyUrl":"https://api.netease.im/sms/verifycode.action","needUp":null}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:46:46.000000', 1414143554414059520, '2023-08-08 13:46:47.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788846944141312, 'ctyun', '天翼云短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"templateName":null,"requestUrl":"https://sms-global.ctapi.ctyun.cn/sms/api/v1","action":"SendSms"}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:46:52.000000', 1414143554414059520, '2023-08-08 13:46:52.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788862987354112, 'emay', '亿美短信', 'normal', '1', '{"appId":"1","secretKey":"1","requestUrl":null}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:46:56.000000', 1414143554414059520, '2023-08-08 14:03:04.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788885141667840, 'cloopen', '容联短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"appId":null,"baseUrl":"https://app.cloopen.com:8883/2013-12-26","serverIp":null,"serverPort":null}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:47:01.000000', 1414143554414059520, '2023-08-08 13:47:01.000000', 0, false);
INSERT INTO notice_sms_channel_config (id, code, name, state, access_key, config, remark, access_secret, image, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1688788904481603584, 'jd_cloud', '京东短信', 'normal', '1', '{"accessKeyId":"1","accessKeySecret":"1","signature":null,"templateId":null,"region":"cn-north-1"}', null, '1', null, 0, 1414143554414059520, '2023-08-08 13:47:06.000000', 1414143554414059520, '2023-08-08 13:47:06.000000', 0, false);

-----
create table notice_sms_config
(
    id                 bigint       not null
        primary key,
    tid                bigint       not null,
    code               varchar(32)  not null,
    name               varchar(64),
    account_sid        varchar(512) not null,
    path_sid           varchar(512),
    auth_token         varchar(512) not null,
    from_num           varchar(512) not null,
    is_default         boolean,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer,
    deleted            boolean,
    secret             varchar(512),
    isp                varchar(64),
    reply_msg          varchar(512)
);

comment on table notice_sms_config is '短信配置';

comment on column notice_sms_config.tid is '租户id';

comment on column notice_sms_config.path_sid is '发送号码的唯一标识(基于twillio的命名风格)';

comment on column notice_sms_config.is_default is '是否默认配置，0:否。1:是';

comment on column notice_sms_config.deleted is '0:未删除。1:已删除';




-----
create table notice_sms_template
(
    id                 bigint  not null
        primary key,
    supplier_type      varchar(255),
    template_id        varchar(255),
    name               varchar(255),
    content            varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table notice_sms_template is '短信模板配置';

comment on column notice_sms_template.id is '主键';

comment on column notice_sms_template.supplier_type is '短信渠道商类型';

comment on column notice_sms_template.template_id is '短信渠道商类型';

comment on column notice_sms_template.name is '短信模板名称';

comment on column notice_sms_template.content is '短信模板内容';

comment on column notice_sms_template.creator is '创建者ID';

comment on column notice_sms_template.create_time is '创建时间';

comment on column notice_sms_template.last_modifier is '最后修者ID';

comment on column notice_sms_template.last_modified_time is '最后修改时间';

comment on column notice_sms_template.version is '乐观锁';

comment on column notice_sms_template.deleted is '删除标志';




-----
create table notice_wechat_config
(
    id                 bigint       not null
        primary key,
    tid                bigint       not null,
    code               varchar(32)  not null,
    name               varchar(64),
    corp_id            varchar(512) not null,
    corp_secret        varchar(512) not null,
    is_default         boolean,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer,
    deleted            boolean
);

comment on table notice_wechat_config is '微信消息配置';

comment on column notice_wechat_config.tid is '租户id';

comment on column notice_wechat_config.is_default is '是否默认配置，0:否。1:是';

comment on column notice_wechat_config.deleted is '0:未删除。1:已删除';




-----
create table pay_alipay_config
(
    id                 bigint  not null
        primary key,
    app_id             varchar(255),
    enable             boolean,
    notify_url         varchar(255),
    return_url         varchar(255),
    server_url         varchar(255),
    auth_type          varchar(255),
    sign_type          varchar(255),
    alipay_public_key  text,
    private_key        text,
    app_cert           text,
    alipay_cert        text,
    alipay_root_cert   text,
    sandbox            boolean,
    single_limit       integer,
    pay_ways           varchar(255),
    allocation         boolean,
    alipay_user_id     varchar(255),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_alipay_config is '支付宝支付配置';

comment on column pay_alipay_config.id is '主键';

comment on column pay_alipay_config.app_id is '支付宝商户appId';

comment on column pay_alipay_config.enable is '是否启用';

comment on column pay_alipay_config.notify_url is '异步通知页面路径';

comment on column pay_alipay_config.return_url is '同步通知页面路径';

comment on column pay_alipay_config.server_url is '支付网关地址';

comment on column pay_alipay_config.auth_type is '认证类型';

comment on column pay_alipay_config.sign_type is '签名类型 RSA2';

comment on column pay_alipay_config.alipay_public_key is '支付宝公钥';

comment on column pay_alipay_config.private_key is '私钥';

comment on column pay_alipay_config.app_cert is '应用公钥证书';

comment on column pay_alipay_config.alipay_cert is '支付宝公钥证书';

comment on column pay_alipay_config.alipay_root_cert is '支付宝CA根证书';

comment on column pay_alipay_config.sandbox is '是否沙箱环境';

comment on column pay_alipay_config.single_limit is '支付限额';

comment on column pay_alipay_config.pay_ways is '可用支付方式';

comment on column pay_alipay_config.allocation is '是否支付分账';

comment on column pay_alipay_config.alipay_user_id is '合作者身份ID';

comment on column pay_alipay_config.remark is '备注';

comment on column pay_alipay_config.creator is '创建者ID';

comment on column pay_alipay_config.create_time is '创建时间';

comment on column pay_alipay_config.last_modifier is '最后修者ID';

comment on column pay_alipay_config.last_modified_time is '最后修改时间';

comment on column pay_alipay_config.version is '乐观锁';

comment on column pay_alipay_config.deleted is '删除标志';



-----
create table pay_alipay_reconcile_bill_detail
(
    id                     bigint not null
        primary key,
    trade_no               varchar(255),
    out_trade_no           varchar(255),
    trade_type             varchar(255),
    subject                varchar(255),
    create_time            varchar(255),
    end_time               varchar(255),
    store_id               varchar(255),
    store_name             varchar(255),
    operator               varchar(255),
    terminal_id            varchar(255),
    other_account          varchar(255),
    order_amount           varchar(255),
    real_amount            varchar(255),
    alipay_amount          varchar(255),
    jfb_amount             varchar(255),
    alipay_discount_amount varchar(255),
    discount_amount        varchar(255),
    coupon_discount_amount varchar(255),
    coupon_name            varchar(255),
    coupon_amount          varchar(255),
    card_amount            varchar(255),
    batch_no               varchar(255),
    service_amount         varchar(255),
    split_amount           varchar(255),
    remark                 varchar(255),
    reconcile_id           bigint
);

comment on table pay_alipay_reconcile_bill_detail is '支付宝业务明细对账单';

comment on column pay_alipay_reconcile_bill_detail.id is '主键';

comment on column pay_alipay_reconcile_bill_detail.trade_no is '支付宝交易号';

comment on column pay_alipay_reconcile_bill_detail.out_trade_no is '商户订单号';

comment on column pay_alipay_reconcile_bill_detail.trade_type is '业务类型';

comment on column pay_alipay_reconcile_bill_detail.subject is '商品名称';

comment on column pay_alipay_reconcile_bill_detail.create_time is '创建时间';

comment on column pay_alipay_reconcile_bill_detail.end_time is '完成时间';

comment on column pay_alipay_reconcile_bill_detail.store_id is '门店编号';

comment on column pay_alipay_reconcile_bill_detail.store_name is '门店名称';

comment on column pay_alipay_reconcile_bill_detail.operator is '操作员';

comment on column pay_alipay_reconcile_bill_detail.terminal_id is '终端号';

comment on column pay_alipay_reconcile_bill_detail.other_account is '对方账户';

comment on column pay_alipay_reconcile_bill_detail.order_amount is '订单金额（元）';

comment on column pay_alipay_reconcile_bill_detail.real_amount is '商家实收（元）';

comment on column pay_alipay_reconcile_bill_detail.alipay_amount is '支付宝红包（元）';

comment on column pay_alipay_reconcile_bill_detail.jfb_amount is '集分宝（元）';

comment on column pay_alipay_reconcile_bill_detail.alipay_discount_amount is '支付宝优惠（元）';

comment on column pay_alipay_reconcile_bill_detail.discount_amount is '商家优惠（元）';

comment on column pay_alipay_reconcile_bill_detail.coupon_discount_amount is '券核销金额（元）';

comment on column pay_alipay_reconcile_bill_detail.coupon_name is '券名称';

comment on column pay_alipay_reconcile_bill_detail.coupon_amount is '商家红包消费金额（元）';

comment on column pay_alipay_reconcile_bill_detail.card_amount is '卡消费金额（元）';

comment on column pay_alipay_reconcile_bill_detail.batch_no is '退款批次号/请求号';

comment on column pay_alipay_reconcile_bill_detail.service_amount is '服务费（元）';

comment on column pay_alipay_reconcile_bill_detail.split_amount is '分润（元）';

comment on column pay_alipay_reconcile_bill_detail.remark is '备注';

comment on column pay_alipay_reconcile_bill_detail.reconcile_id is '关联对账订单ID';




-----
create table pay_alipay_reconcile_bill_total
(
    id                    bigint not null
        primary key,
    record_order_id       bigint,
    store_id              varchar(255),
    store_name            varchar(255),
    total_num             varchar(255),
    total_refund_num      varchar(255),
    total_order_amount    varchar(255),
    total_amount          varchar(255),
    total_discount_amount varchar(255),
    total_coupon_amount   varchar(255),
    total_consume_amount  varchar(255),
    total_service_amount  varchar(255),
    total_share_amount    varchar(255),
    total_net_amount      varchar(255)
);

comment on table pay_alipay_reconcile_bill_total is '支付宝业务汇总对账单';

comment on column pay_alipay_reconcile_bill_total.id is '主键';

comment on column pay_alipay_reconcile_bill_total.record_order_id is '关联对账订单ID';

comment on column pay_alipay_reconcile_bill_total.store_id is '门店编号';

comment on column pay_alipay_reconcile_bill_total.store_name is '门店名称';

comment on column pay_alipay_reconcile_bill_total.total_num is '交易订单总笔数';

comment on column pay_alipay_reconcile_bill_total.total_refund_num is '退款订单总笔数';

comment on column pay_alipay_reconcile_bill_total.total_order_amount is '订单金额（元）';

comment on column pay_alipay_reconcile_bill_total.total_amount is '商家实收（元）';

comment on column pay_alipay_reconcile_bill_total.total_discount_amount is '支付宝优惠（元）';

comment on column pay_alipay_reconcile_bill_total.total_coupon_amount is '商家优惠（元）';

comment on column pay_alipay_reconcile_bill_total.total_consume_amount is '卡消费金额（元）';

comment on column pay_alipay_reconcile_bill_total.total_service_amount is '服务费（元）';

comment on column pay_alipay_reconcile_bill_total.total_share_amount is '分润（元）';

comment on column pay_alipay_reconcile_bill_total.total_net_amount is '实收净额（元）';




-----
create table pay_allocation_group
(
    id                 bigint  not null
        primary key,
    name               varchar(255),
    channel            varchar(255),
    total_rate         integer,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    default_group      boolean
);

comment on table pay_allocation_group is '分账组';

comment on column pay_allocation_group.id is '主键';

comment on column pay_allocation_group.name is '名称';

comment on column pay_allocation_group.channel is '通道';

comment on column pay_allocation_group.total_rate is '总分账比例(万分之多少)';

comment on column pay_allocation_group.remark is '备注';

comment on column pay_allocation_group.creator is '创建者ID';

comment on column pay_allocation_group.create_time is '创建时间';

comment on column pay_allocation_group.last_modifier is '最后修者ID';

comment on column pay_allocation_group.last_modified_time is '最后修改时间';

comment on column pay_allocation_group.version is '乐观锁';

comment on column pay_allocation_group.deleted is '删除标志';

comment on column pay_allocation_group.default_group is '默认分账组';




-----
create table pay_allocation_group_receiver
(
    id          bigint not null
        primary key,
    group_id    bigint,
    receiver_id bigint,
    rate        integer,
    creator     bigint,
    create_time timestamp(6)
);

comment on table pay_allocation_group_receiver is '分账接收组关系';

comment on column pay_allocation_group_receiver.id is '主键';

comment on column pay_allocation_group_receiver.group_id is '分账组ID';

comment on column pay_allocation_group_receiver.receiver_id is '接收者ID';

comment on column pay_allocation_group_receiver.rate is '分账比例(万分之多少)';

comment on column pay_allocation_group_receiver.creator is '创建者ID';

comment on column pay_allocation_group_receiver.create_time is '创建时间';




-----
create table pay_allocation_order
(
    id                 bigint  not null
        primary key,
    allocation_no      varchar(255),
    biz_allocation_no  varchar(255),
    out_allocation_no  varchar(255),
    order_id           bigint,
    order_no           varchar(255),
    biz_order_no       varchar(255),
    out_order_no       varchar(255),
    title              varchar(255),
    channel            varchar(255),
    amount             integer,
    description        varchar(255),
    status             varchar(255),
    result             varchar(255),
    error_code         varchar(255),
    error_msg          varchar(255),
    finish_time        timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_allocation_order is '分账订单';

comment on column pay_allocation_order.id is '主键';

comment on column pay_allocation_order.allocation_no is '分账单号';

comment on column pay_allocation_order.biz_allocation_no is '商户分账单号';

comment on column pay_allocation_order.out_allocation_no is '通道分账号';

comment on column pay_allocation_order.order_id is '支付订单ID';

comment on column pay_allocation_order.order_no is '支付订单号';

comment on column pay_allocation_order.biz_order_no is '商户支付订单号';

comment on column pay_allocation_order.out_order_no is '通道支付订单号';

comment on column pay_allocation_order.channel is '所属通道';

comment on column pay_allocation_order.amount is '总分账金额';

comment on column pay_allocation_order.description is '分账描述';

comment on column pay_allocation_order.status is '状态';

comment on column pay_allocation_order.result is '分账处理结果';

comment on column pay_allocation_order.error_code is '错误码';

comment on column pay_allocation_order.error_msg is '错误原因';

comment on column pay_allocation_order.finish_time is '分账完成时间';

comment on column pay_allocation_order.creator is '创建者ID';

comment on column pay_allocation_order.create_time is '创建时间';

comment on column pay_allocation_order.last_modifier is '最后修者ID';

comment on column pay_allocation_order.last_modified_time is '最后修改时间';

comment on column pay_allocation_order.version is '乐观锁';

comment on column pay_allocation_order.deleted is '删除标志';




-----
create table pay_allocation_order_detail
(
    id                 bigint  not null
        primary key,
    allocation_id      bigint,
    receiver_id        bigint,
    rate               integer,
    amount             integer,
    receiver_type      varchar(255),
    receiver_account   varchar(255),
    receiver_name      varchar(255),
    result             varchar(255),
    error_code         varchar(255),
    error_msg          varchar(255),
    finish_time        timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_allocation_order_detail is '分账订单明细';

comment on column pay_allocation_order_detail.id is '主键';

comment on column pay_allocation_order_detail.allocation_id is '分账订单ID';

comment on column pay_allocation_order_detail.receiver_id is '接收者ID';

comment on column pay_allocation_order_detail.rate is '分账比例(万分之多少)';

comment on column pay_allocation_order_detail.amount is '分账金额';

comment on column pay_allocation_order_detail.receiver_type is '分账接收方类型';

comment on column pay_allocation_order_detail.receiver_account is '接收方账号';

comment on column pay_allocation_order_detail.receiver_name is '接收方姓名';

comment on column pay_allocation_order_detail.result is '分账结果';

comment on column pay_allocation_order_detail.error_code is '错误代码';

comment on column pay_allocation_order_detail.error_msg is '错误原因';

comment on column pay_allocation_order_detail.finish_time is '分账完成时间';

comment on column pay_allocation_order_detail.creator is '创建者ID';

comment on column pay_allocation_order_detail.create_time is '创建时间';

comment on column pay_allocation_order_detail.last_modifier is '最后修者ID';

comment on column pay_allocation_order_detail.last_modified_time is '最后修改时间';

comment on column pay_allocation_order_detail.version is '乐观锁';

comment on column pay_allocation_order_detail.deleted is '删除标志';




-----
create table pay_allocation_receiver
(
    id                 bigint  not null
        primary key,
    name               varchar(255),
    channel            varchar(255),
    receiver_type      varchar(255),
    receiver_account   varchar(255),
    receiver_name      varchar(255),
    relation_type      varchar(255),
    relation_name      varchar(255),
    sync               boolean,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_allocation_receiver is '分账接收方';

comment on column pay_allocation_receiver.id is '主键';

comment on column pay_allocation_receiver.name is '账号别名';

comment on column pay_allocation_receiver.channel is '所属通道';

comment on column pay_allocation_receiver.receiver_type is '分账接收方类型';

comment on column pay_allocation_receiver.receiver_account is '接收方账号';

comment on column pay_allocation_receiver.receiver_name is '接收方姓名';

comment on column pay_allocation_receiver.relation_type is '分账关系类型';

comment on column pay_allocation_receiver.relation_name is '关系名称';

comment on column pay_allocation_receiver.sync is '是否已经同步到网关';

comment on column pay_allocation_receiver.remark is '备注';

comment on column pay_allocation_receiver.creator is '创建者ID';

comment on column pay_allocation_receiver.create_time is '创建时间';

comment on column pay_allocation_receiver.last_modifier is '最后修者ID';

comment on column pay_allocation_receiver.last_modified_time is '最后修改时间';

comment on column pay_allocation_receiver.version is '乐观锁';

comment on column pay_allocation_receiver.deleted is '删除标志';




-----
create table pay_api_config
(
    id                 bigint  not null
        primary key,
    code               varchar(255),
    api                varchar(255),
    name               varchar(255),
    notice_support     boolean,
    enable             boolean,
    notice             boolean,
    notice_url         varchar(255),
    req_sign           boolean,
    res_sign           boolean,
    notice_sign        boolean,
    record             boolean,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_api_config is '支付接口配置';

comment on column pay_api_config.id is '主键';

comment on column pay_api_config.code is '编码';

comment on column pay_api_config.api is '接口地址';

comment on column pay_api_config.name is '名称';

comment on column pay_api_config.notice_support is '支持回调通知';

comment on column pay_api_config.enable is '是否启用';

comment on column pay_api_config.notice is '是否开启回调通知';

comment on column pay_api_config.notice_url is '默认通知地址';

comment on column pay_api_config.req_sign is '请求参数是否签名';

comment on column pay_api_config.res_sign is '响应参数是否签名';

comment on column pay_api_config.notice_sign is '回调信息是否签名';

comment on column pay_api_config.record is '是否记录请求的信息';

comment on column pay_api_config.remark is '备注';

comment on column pay_api_config.creator is '创建者ID';

comment on column pay_api_config.create_time is '创建时间';

comment on column pay_api_config.last_modifier is '最后修者ID';

comment on column pay_api_config.last_modified_time is '最后修改时间';

comment on column pay_api_config.version is '乐观锁';

comment on column pay_api_config.deleted is '删除标志';



INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (3, 'close', '/uniPay/close', '支付关闭接口', false, true, true, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (6, 'syncPay', '/uniPay/syncPay', '支付同步接口', false, true, true, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (7, 'syncRefund', '/uniPay/syncRefund', '退款同步接口', false, true, true, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (8, 'transfer', '/uniPay/transfer', '统一转账接口', true, true, true, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (9, 'allocation', '/uniPay/allocation', '统一分账接口', true, true, true, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (10, 'queryPayOrder', '/uniPay/queryPayOrder', '支付订单查询接口', false, true, false, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (11, 'queryRefundOrder', '/uniPay/queryRefundOrder', '退款订单查询接口', false, true, false, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (12, 'getWxAuthUrl', '/unipay/assist/getWxAuthUrl', '获取微信OAuth2授权链接', false, true, false, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (13, 'getWxAccessToken', '/unipay/assist/getWxAccessToken', '获取微信AccessToken', false, true, false, null, true, false, false, false, null, 0, '2024-01-03 14:25:53.000000', 0, '2024-01-03 14:25:53.000000', 0, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1, 'pay', '/uniPay/pay', e'统一支付接口	', true, true, true, 'https://doc.daxpay.cn/server/demo/callback/payObject', true, false, true, false, null, 0, '2024-01-03 14:25:53.000000', 1399985191002447872, '2024-05-31 20:56:40.077000', 1, false);
INSERT INTO pay_api_config (id, code, api, name, notice_support, enable, notice, notice_url, req_sign, res_sign, notice_sign, record, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (4, 'refund', '/uniPay/refund', '统一退款接口', true, true, true, 'https://doc.daxpay.cn/server/demo/callback/refundObject', true, false, true, false, null, 0, '2024-01-03 14:25:53.000000', 1399985191002447872, '2024-05-31 20:56:54.917000', 1, false);

-----
create table pay_callback_record
(
    id              bigint not null
        primary key,
    trade_no        varchar(255),
    out_trade_no    varchar(255),
    channel         varchar(255),
    callback_type   varchar(255),
    notify_info     text,
    status          varchar(255),
    error_code      varchar(255),
    creator         bigint,
    create_time     timestamp(6),
    error_msg       varchar(255)
);

comment on table pay_callback_record is '网关回调通知';

comment on column pay_callback_record.id is '主键';

comment on column pay_callback_record.trade_no is '本地交易号';

comment on column pay_callback_record.out_trade_no is '通道交易号';

comment on column pay_callback_record.channel is '支付通道';

comment on column pay_callback_record.callback_type is '回调类型';

comment on column pay_callback_record.notify_info is '通知消息';

comment on column pay_callback_record.status is '回调处理状态';

comment on column pay_callback_record.error_code is '错误码';

comment on column pay_callback_record.creator is '创建者ID';

comment on column pay_callback_record.create_time is '创建时间';

comment on column pay_callback_record.error_msg is '提示信息';




-----
create table pay_channel_config
(
    id                 bigint  not null
        primary key,
    code               varchar(255),
    name               varchar(255),
    icon_id            bigint,
    bg_color           varchar(255),
    enable             boolean,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_channel_config is '支付通道配置';

comment on column pay_channel_config.id is '主键';

comment on column pay_channel_config.code is '代码';

comment on column pay_channel_config.name is '名称';

comment on column pay_channel_config.icon_id is 'ICON图片';

comment on column pay_channel_config.bg_color is '卡牌背景色';

comment on column pay_channel_config.enable is '是否启用';

comment on column pay_channel_config.remark is '备注';

comment on column pay_channel_config.creator is '创建者ID';

comment on column pay_channel_config.create_time is '创建时间';

comment on column pay_channel_config.last_modifier is '最后修者ID';

comment on column pay_channel_config.last_modified_time is '最后修改时间';

comment on column pay_channel_config.version is '乐观锁';

comment on column pay_channel_config.deleted is '删除标志';



INSERT INTO pay_channel_config (id, code, name, icon_id, bg_color, enable, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (2, 'wechat_pay', '微信支付', null, null, false, '', 0, '2024-01-08 16:47:07.000000', 1399985191002447872, '2024-02-13 15:38:24.000000', 7, false);
INSERT INTO pay_channel_config (id, code, name, icon_id, bg_color, enable, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (3, 'union_pay', '云闪付', null, null, false, null, 0, '2024-01-08 16:47:07.000000', 1399985191002447872, '2024-03-10 15:04:36.000000', 2, false);
INSERT INTO pay_channel_config (id, code, name, icon_id, bg_color, enable, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (5, 'wallet_pay', '钱包支付', null, null, false, null, 0, '2024-01-08 16:47:07.000000', 0, '2024-01-08 16:47:11.000000', 0, false);
INSERT INTO pay_channel_config (id, code, name, icon_id, bg_color, enable, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1, 'ali_pay', '支付宝', null, null, true, '', 0, '2024-01-08 16:47:07.000000', 1399985191002447872, '2024-05-31 20:56:00.196000', 13, false);

-----
create table pay_client_notice_record
(
    id          bigint not null
        primary key,
    task_id     bigint,
    req_count   integer,
    success     boolean,
    send_type   varchar(255),
    error_msg   varchar(255),
    creator     bigint,
    create_time timestamp(6),
    error_code  varchar(255)
);

comment on table pay_client_notice_record is '消息通知任务记录';

comment on column pay_client_notice_record.id is '主键';

comment on column pay_client_notice_record.task_id is '任务ID';

comment on column pay_client_notice_record.req_count is '请求次数';

comment on column pay_client_notice_record.success is '发送是否成功';

comment on column pay_client_notice_record.send_type is '发送类型';

comment on column pay_client_notice_record.error_msg is '错误信息';

comment on column pay_client_notice_record.creator is '创建者ID';

comment on column pay_client_notice_record.create_time is '创建时间';

comment on column pay_client_notice_record.error_code is '错误编码';



INSERT INTO pay_client_notice_record (id, task_id, req_count, success, send_type, error_msg, creator, create_time, error_code) VALUES (1796526918107570176, 1796526917679751168, 1, false, 'auto', e'<!DOCTYPE html>
<html lang="zh-CN">
  <head>

    <meta charset="UTF-8" />
    <link
      rel="', 1399985191002447872, '2024-05-31 20:59:29.789000', null);
INSERT INTO pay_client_notice_record (id, task_id, req_count, success, send_type, error_msg, creator, create_time, error_code) VALUES (1796528223580495872, 1796528223437889536, 1, false, 'auto', e'<!DOCTYPE html>
<html lang="zh-CN">
  <head>

    <meta charset="UTF-8" />
    <link
      rel="', 1399985191002447872, '2024-05-31 21:04:41.039000', null);

-----
create table pay_client_notice_task
(
    id                 bigint  not null
        primary key,
    content            text,
    success            boolean,
    send_count         integer,
    url                varchar(255),
    latest_time        timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    notice_type        varchar(255),
    trade_id           bigint,
    trade_no           varchar(255),
    trade_status       varchar(255)
);

comment on table pay_client_notice_task is '消息通知任务';

comment on column pay_client_notice_task.id is '主键';

comment on column pay_client_notice_task.content is '消息内容';

comment on column pay_client_notice_task.success is '是否发送成功';

comment on column pay_client_notice_task.send_count is '发送次数';

comment on column pay_client_notice_task.url is '发送地址';

comment on column pay_client_notice_task.latest_time is '最后发送时间';

comment on column pay_client_notice_task.creator is '创建者ID';

comment on column pay_client_notice_task.create_time is '创建时间';

comment on column pay_client_notice_task.last_modifier is '最后修者ID';

comment on column pay_client_notice_task.last_modified_time is '最后修改时间';

comment on column pay_client_notice_task.version is '乐观锁';

comment on column pay_client_notice_task.deleted is '删除标志';

comment on column pay_client_notice_task.notice_type is '消息类型';

comment on column pay_client_notice_task.trade_id is '本地交易ID';

comment on column pay_client_notice_task.trade_no is '本地交易号';

comment on column pay_client_notice_task.trade_status is '交易状态';



-----
create table pay_close_record
(
    id           bigint not null
        primary key,
    order_no     varchar(255),
    biz_order_no varchar(255),
    channel      varchar(255),
    closed       boolean,
    error_code   varchar(255),
    error_msg    varchar(255),
    client_ip    varchar(255),
    creator      bigint,
    create_time  timestamp(6)
);

comment on table pay_close_record is '支付关闭记录';

comment on column pay_close_record.id is '主键';

comment on column pay_close_record.order_no is '订单号';

comment on column pay_close_record.biz_order_no is '商户订单号';

comment on column pay_close_record.channel is '关闭的支付通道';

comment on column pay_close_record.closed is '是否关闭成功';

comment on column pay_close_record.error_code is '错误码';

comment on column pay_close_record.error_msg is '错误消息';

comment on column pay_close_record.client_ip is '客户端IP';

comment on column pay_close_record.creator is '创建者ID';

comment on column pay_close_record.create_time is '创建时间';




-----
create table pay_method_info
(
    id                 bigint  not null
        primary key,
    code               varchar(255),
    name               varchar(255),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_method_info is '支付方式';

comment on column pay_method_info.id is '主键';

comment on column pay_method_info.code is '代码';

comment on column pay_method_info.name is '名称';

comment on column pay_method_info.remark is '备注';

comment on column pay_method_info.creator is '创建者ID';

comment on column pay_method_info.create_time is '创建时间';

comment on column pay_method_info.last_modifier is '最后修者ID';

comment on column pay_method_info.last_modified_time is '最后修改时间';

comment on column pay_method_info.version is '乐观锁';

comment on column pay_method_info.deleted is '删除标志';



INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1, 'normal', '常规支付', '同步支付使用这个', 0, '2024-01-09 10:01:21.000000', 1399985191002447872, '2024-02-13 15:39:16.000000', 1, false);
INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (2, 'wap', 'wap支付', null, 0, '2024-01-09 10:01:21.000000', 0, '2024-01-09 10:01:25.000000', 0, false);
INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (3, 'app', '应用支付', null, 0, '2024-01-09 10:01:21.000000', 0, '2024-01-09 10:01:25.000000', 0, false);
INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (4, 'web', 'web支付', null, 0, '2024-01-09 10:01:21.000000', 0, '2024-01-09 10:01:25.000000', 0, false);
INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (6, 'barcode', '付款码', null, 0, '2024-01-09 10:01:21.000000', 0, '2024-01-09 10:01:25.000000', 0, false);
INSERT INTO pay_method_info (id, code, name, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (7, 'jsapi', '公众号/小程序支付', '主要是微信使用', 0, '2024-01-09 10:01:21.000000', 1399985191002447872, '2024-02-13 15:38:57.000000', 1, false);

-----
create table pay_order
(
    id                 bigint  not null
        primary key,
    biz_order_no       varchar(255),
    order_no           varchar(255),
    out_order_no       varchar(255),
    title              varchar(255),
    description        varchar(255),
    allocation         boolean,
    channel            varchar(255),
    method             varchar(255),
    amount             integer,
    refundable_balance integer,
    status             varchar(255),
    allocation_status  varchar(255),
    pay_time           timestamp(6),
    close_time         timestamp(6),
    expired_time       timestamp(6),
    error_code         varchar(255),
    error_msg          varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    auto_allocation    boolean
);

comment on table pay_order is '支付订单';

comment on column pay_order.id is '主键';

comment on column pay_order.biz_order_no is '商户订单号';

comment on column pay_order.order_no is '支付订单号';

comment on column pay_order.out_order_no is '通道支付订单号';

comment on column pay_order.title is '标题';

comment on column pay_order.description is '描述';

comment on column pay_order.allocation is '是否需要分账';

comment on column pay_order.channel is '异步支付通道';

comment on column pay_order.method is '支付方式';

comment on column pay_order.amount is '金额';

comment on column pay_order.refundable_balance is '可退款余额';

comment on column pay_order.status is '支付状态';

comment on column pay_order.allocation_status is '分账状态';

comment on column pay_order.pay_time is '支付时间';

comment on column pay_order.close_time is '关闭时间';

comment on column pay_order.expired_time is '过期时间';

comment on column pay_order.error_code is '错误码';

comment on column pay_order.error_msg is '错误信息';

comment on column pay_order.creator is '创建者ID';

comment on column pay_order.create_time is '创建时间';

comment on column pay_order.last_modifier is '最后修者ID';

comment on column pay_order.last_modified_time is '最后修改时间';

comment on column pay_order.version is '乐观锁';

comment on column pay_order.deleted is '删除标志';

comment on column pay_order.auto_allocation is '自动分账';



create unique index biz_order_no
    on pay_order (biz_order_no);

comment on index biz_order_no is '商户订单号索引';

INSERT INTO pay_order (id, biz_order_no, order_no, out_order_no, title, description, allocation, channel, method, amount, refundable_balance, status, allocation_status, pay_time, close_time, expired_time, error_code, error_msg, creator, create_time, last_modifier, last_modified_time, version, deleted, auto_allocation) VALUES (1796526514816851968, 'P1717160268917', 'nullP240531205753null000001', '2024053122001488181401741346', '测试支付', null, false, 'ali_pay', 'web', 1, 0, 'refunded', null, '2024-05-31 20:58:12.000000', null, '2024-05-31 21:27:53.586000', null, null, 0, '2024-05-31 20:57:53.639000', 1399985191002447872, '2024-05-31 21:04:40.979000', 6, false, null);

-----
create table pay_order_extra
(
    id                 bigint  not null
        primary key,
    return_url         varchar(255),
    notify_url         varchar(255),
    extra_param        varchar(255),
    attach             varchar(255),
    req_time           timestamp(6),
    client_ip          varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_order_extra is '支付订单扩展信息';

comment on column pay_order_extra.id is '主键';

comment on column pay_order_extra.return_url is '同步跳转地址';

comment on column pay_order_extra.notify_url is '异步通知地址';

comment on column pay_order_extra.extra_param is '附加参数';

comment on column pay_order_extra.attach is '商户扩展参数';

comment on column pay_order_extra.req_time is '请求时间，传输时间戳，以最后一次为准';

comment on column pay_order_extra.client_ip is '支付终端ip';

comment on column pay_order_extra.creator is '创建者ID';

comment on column pay_order_extra.create_time is '创建时间';

comment on column pay_order_extra.last_modifier is '最后修者ID';

comment on column pay_order_extra.last_modified_time is '最后修改时间';

comment on column pay_order_extra.version is '乐观锁';

comment on column pay_order_extra.deleted is '删除标志';



-----
create table pay_platform_config
(
    id                 bigint  not null
        primary key,
    website_url        varchar(255),
    sign_type          varchar(255),
    sign_secret        varchar(255),
    notify_url         varchar(255),
    return_url         varchar(255),
    order_timeout      integer,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    limit_amount       integer
);

comment on table pay_platform_config is '支付平台配置';

comment on column pay_platform_config.id is '主键';

comment on column pay_platform_config.website_url is '网站地址';

comment on column pay_platform_config.sign_type is '签名方式';

comment on column pay_platform_config.sign_secret is '签名秘钥';

comment on column pay_platform_config.notify_url is '支付通知地址';

comment on column pay_platform_config.return_url is '同步支付跳转地址';

comment on column pay_platform_config.order_timeout is '订单默认超时时间(分钟)';

comment on column pay_platform_config.creator is '创建者ID';

comment on column pay_platform_config.create_time is '创建时间';

comment on column pay_platform_config.last_modifier is '最后修者ID';

comment on column pay_platform_config.last_modified_time is '最后修改时间';

comment on column pay_platform_config.version is '乐观锁';

comment on column pay_platform_config.deleted is '删除标志';

comment on column pay_platform_config.limit_amount is '支付限额';



INSERT INTO pay_platform_config (id, website_url, sign_type, sign_secret, notify_url, return_url, order_timeout, creator, create_time, last_modifier, last_modified_time, version, deleted, limit_amount) VALUES (0, 'http://localhost/', 'HMAC_SHA256', '123456', 'http://localhost/h5/#/result/success', 'http://localhost/h5/#/result/success', 30, 0, '2024-01-02 20:23:19.000000', 1399985191002447872, '2024-03-24 11:34:20.000000', 13, false, 20000);

-----
create table pay_reconcile_detail
(
    id           bigint not null
        primary key,
    title        varchar(255),
    amount       integer,
    type         varchar(255),
    creator      bigint,
    create_time  timestamp(6),
    reconcile_id bigint,
    trade_no     varchar(255),
    out_trade_no varchar(255),
    trade_time   timestamp(6)
);

comment on table pay_reconcile_detail is '支付对账记录';

comment on column pay_reconcile_detail.id is '主键';

comment on column pay_reconcile_detail.title is '商品名称';

comment on column pay_reconcile_detail.amount is '交易金额';

comment on column pay_reconcile_detail.type is '交易类型';

comment on column pay_reconcile_detail.creator is '创建者ID';

comment on column pay_reconcile_detail.create_time is '创建时间';

comment on column pay_reconcile_detail.reconcile_id is '关联对账订单ID';

comment on column pay_reconcile_detail.trade_no is '本地交易号';

comment on column pay_reconcile_detail.out_trade_no is '外部交易号';

comment on column pay_reconcile_detail.trade_time is '交易时间';




-----
create table pay_reconcile_diff_record
(
    id                 bigint  not null
        primary key,
    detail_id          bigint,
    title              varchar(255),
    diff_type          varchar(255),
    diffs              text,
    amount             integer,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    reconcile_id       bigint,
    trade_no           varchar(255),
    trade_time         timestamp(6),
    out_amount         integer,
    reconcile_no       varchar(255),
    reconcile_date     date,
    trade_type         varchar(255),
    channel            varchar(255),
    out_trade_no       varchar(255)
);

comment on table pay_reconcile_diff_record is '对账差异单';

comment on column pay_reconcile_diff_record.id is '主键';

comment on column pay_reconcile_diff_record.detail_id is '对账单明细ID';

comment on column pay_reconcile_diff_record.title is '订单标题';

comment on column pay_reconcile_diff_record.diff_type is '差异类型';

comment on column pay_reconcile_diff_record.diffs is '差异内容';

comment on column pay_reconcile_diff_record.amount is '本地交易金额';

comment on column pay_reconcile_diff_record.creator is '创建者ID';

comment on column pay_reconcile_diff_record.create_time is '创建时间';

comment on column pay_reconcile_diff_record.last_modifier is '最后修者ID';

comment on column pay_reconcile_diff_record.last_modified_time is '最后修改时间';

comment on column pay_reconcile_diff_record.version is '乐观锁';

comment on column pay_reconcile_diff_record.deleted is '删除标志';

comment on column pay_reconcile_diff_record.reconcile_id is '对账单ID';

comment on column pay_reconcile_diff_record.trade_no is '本地交易号';

comment on column pay_reconcile_diff_record.trade_time is '交易时间';

comment on column pay_reconcile_diff_record.out_amount is '通道交易金额';

comment on column pay_reconcile_diff_record.reconcile_no is '对账号';

comment on column pay_reconcile_diff_record.reconcile_date is '对账日期';

comment on column pay_reconcile_diff_record.trade_type is '交易类型';

comment on column pay_reconcile_diff_record.channel is '通道';

comment on column pay_reconcile_diff_record.out_trade_no is '通道交易号';




-----
create table pay_reconcile_file
(
    id           bigint not null
        primary key,
    reconcile_id bigint,
    type         varchar(255),
    file_id      bigint
);

comment on table pay_reconcile_file is '原始对账单文件,';

comment on column pay_reconcile_file.id is '主键';

comment on column pay_reconcile_file.reconcile_id is '对账单ID';

comment on column pay_reconcile_file.type is '类型';

comment on column pay_reconcile_file.file_id is '对账单文件';




-----
create table pay_reconcile_order
(
    id             bigint not null
        primary key,
    date           date,
    channel        varchar(255),
    compare        boolean,
    error_msg      varchar(255),
    creator        bigint,
    create_time    timestamp(6),
    reconcile_no   varchar(255),
    result         varchar(255),
    error_code     varchar(255),
    down_or_upload boolean
);

comment on table pay_reconcile_order is '支付对账单订单';

comment on column pay_reconcile_order.id is '主键';

comment on column pay_reconcile_order.date is '日期';

comment on column pay_reconcile_order.channel is '通道';

comment on column pay_reconcile_order.compare is '明细对账单比对';

comment on column pay_reconcile_order.error_msg is '错误信息';

comment on column pay_reconcile_order.creator is '创建者ID';

comment on column pay_reconcile_order.create_time is '创建时间';

comment on column pay_reconcile_order.reconcile_no is '对账号';

comment on column pay_reconcile_order.result is '对账结果';

comment on column pay_reconcile_order.error_code is '错误码';

comment on column pay_reconcile_order.down_or_upload is '明细对账单下载';




-----
create table pay_reconcile_trade_detail
(
    id           bigint not null
        primary key,
    reconcile_id bigint,
    title        varchar(255),
    amount       integer,
    type         varchar(255),
    trade_no     varchar(255),
    out_trade_no varchar(255),
    trade_time   timestamp(6),
    creator      bigint,
    create_time  timestamp(6)
);

comment on table pay_reconcile_trade_detail is '对账-第三方交易明细';

comment on column pay_reconcile_trade_detail.id is '主键';

comment on column pay_reconcile_trade_detail.reconcile_id is '关联对账订单ID';

comment on column pay_reconcile_trade_detail.title is '商品名称';

comment on column pay_reconcile_trade_detail.amount is '交易金额';

comment on column pay_reconcile_trade_detail.type is '交易类型';

comment on column pay_reconcile_trade_detail.trade_no is '本地交易号';

comment on column pay_reconcile_trade_detail.out_trade_no is '通道交易号';

comment on column pay_reconcile_trade_detail.trade_time is '交易时间';

comment on column pay_reconcile_trade_detail.creator is '创建者ID';

comment on column pay_reconcile_trade_detail.create_time is '创建时间';




-----
create table pay_refund_order
(
    id                 bigint  not null
        primary key,
    order_id           bigint,
    order_no           varchar(255),
    biz_order_no       varchar(255),
    title              varchar(255),
    refund_no          varchar(255),
    biz_refund_no      varchar(255),
    out_refund_no      varchar(255),
    channel            varchar(255),
    order_amount       integer,
    amount             integer,
    reason             varchar(255),
    finish_time        timestamp(6),
    status             varchar(255),
    error_code         varchar(255),
    error_msg          varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null,
    out_order_no       varchar(255)
);

comment on table pay_refund_order is '退款订单';

comment on column pay_refund_order.id is '主键';

comment on column pay_refund_order.order_id is '支付订单ID';

comment on column pay_refund_order.order_no is '支付订单号';

comment on column pay_refund_order.biz_order_no is '商户支付订单号';

comment on column pay_refund_order.title is '支付标题';

comment on column pay_refund_order.refund_no is '退款号';

comment on column pay_refund_order.biz_refund_no is '商户退款号';

comment on column pay_refund_order.out_refund_no is '三方支付系统退款交易号';

comment on column pay_refund_order.channel is '支付通道';

comment on column pay_refund_order.order_amount is '订单金额';

comment on column pay_refund_order.amount is '退款金额';

comment on column pay_refund_order.reason is '退款原因';

comment on column pay_refund_order.finish_time is '退款完成时间';

comment on column pay_refund_order.status is '退款状态';

comment on column pay_refund_order.error_code is '错误码';

comment on column pay_refund_order.error_msg is '错误信息';

comment on column pay_refund_order.creator is '创建者ID';

comment on column pay_refund_order.create_time is '创建时间';

comment on column pay_refund_order.last_modifier is '最后修者ID';

comment on column pay_refund_order.last_modified_time is '最后修改时间';

comment on column pay_refund_order.version is '乐观锁';

comment on column pay_refund_order.deleted is '删除标志';

comment on column pay_refund_order.out_order_no is '商户支付订单号';



INSERT INTO pay_refund_order (id, order_id, order_no, biz_order_no, title, refund_no, biz_refund_no, out_refund_no, channel, order_amount, amount, reason, finish_time, status, error_code, error_msg, creator, create_time, last_modifier, last_modified_time, version, deleted, out_order_no) VALUES (1796528218027237376, 1796526514816851968, 'nullP240531205753null000001', 'P1717160268917', '测试支付', 'nullR240531210439null000003', 'nullR240531210439null000002', '2024053122001488181401741346', 'ali_pay', 1, 1, '测试退款', '2024-05-31 21:04:40.971000', 'success', null, null, 1399985191002447872, '2024-05-31 21:04:39.714000', 1399985191002447872, '2024-05-31 21:04:40.996000', 1, false, '2024053122001488181401741346');

-----
create table pay_refund_order_extra
(
    id                 bigint  not null
        primary key,
    notify_url         varchar(255),
    attach             varchar(255),
    extra_param        varchar(255),
    req_time           timestamp(6),
    client_ip          varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_refund_order_extra is '退款订单扩展信息';

comment on column pay_refund_order_extra.id is '主键';

comment on column pay_refund_order_extra.notify_url is '异步通知地址';

comment on column pay_refund_order_extra.attach is '商户扩展参数';

comment on column pay_refund_order_extra.extra_param is '附加参数';

comment on column pay_refund_order_extra.req_time is '请求时间，传输时间戳';

comment on column pay_refund_order_extra.client_ip is '支付终端ip';

comment on column pay_refund_order_extra.creator is '创建者ID';

comment on column pay_refund_order_extra.create_time is '创建时间';

comment on column pay_refund_order_extra.last_modifier is '最后修者ID';

comment on column pay_refund_order_extra.last_modified_time is '最后修改时间';

comment on column pay_refund_order_extra.version is '乐观锁';

comment on column pay_refund_order_extra.deleted is '删除标志';



-----
create table pay_repair_record
(
    id            bigint not null
        primary key,
    repair_no     varchar(255),
    trade_id      bigint,
    trade_no      varchar(255),
    repair_type   varchar(255),
    repair_source varchar(255),
    repair_way    varchar(255),
    channel       varchar(255),
    before_status varchar(255),
    after_status  varchar(255),
    creator       bigint,
    create_time   timestamp(6)
);

comment on table pay_repair_record is '支付修复记录';

comment on column pay_repair_record.id is '主键';

comment on column pay_repair_record.repair_no is '修复号';

comment on column pay_repair_record.trade_id is '本地订单ID';

comment on column pay_repair_record.trade_no is '本地业务号';

comment on column pay_repair_record.repair_type is '修复类型';

comment on column pay_repair_record.repair_source is '修复来源';

comment on column pay_repair_record.repair_way is '修复方式';

comment on column pay_repair_record.channel is '修复的通道';

comment on column pay_repair_record.before_status is '修复前状态';

comment on column pay_repair_record.after_status is '修复后状态';

comment on column pay_repair_record.creator is '创建者ID';

comment on column pay_repair_record.create_time is '创建时间';



INSERT INTO pay_repair_record (id, repair_no, trade_id, trade_no, repair_type, repair_source, repair_way, channel, before_status, after_status, creator, create_time) VALUES (1796526917654585344, '1796526917600059392', 1796526514816851968, 'nullP240531205753null000001', 'pay', 'sync', 'pay_success', 'ali_pay', 'progress', 'success', 1399985191002447872, '2024-05-31 20:59:29.681000');

-----
create table pay_sync_record
(
    id               bigint not null
        primary key,
    trade_no         varchar(255),
    biz_trade_no     varchar(255),
    out_trade_no     varchar(255),
    out_trade_status varchar(255),
    sync_type        varchar(255),
    channel          varchar(255),
    sync_info        text,
    repair           boolean,
    repair_no        varchar(255),
    error_code       varchar(255),
    error_msg        varchar(255),
    client_ip        varchar(255),
    creator          bigint,
    create_time      timestamp(6)
);

comment on table pay_sync_record is '支付同步订单';

comment on column pay_sync_record.id is '主键';

comment on column pay_sync_record.trade_no is '本地交易号';

comment on column pay_sync_record.biz_trade_no is '商户交易号';

comment on column pay_sync_record.out_trade_no is '通道交易号';

comment on column pay_sync_record.out_trade_status is '网关返回状态';

comment on column pay_sync_record.sync_type is '同步类型';

comment on column pay_sync_record.channel is '同步的异步通道';

comment on column pay_sync_record.sync_info is '同步消息';

comment on column pay_sync_record.repair is '是否进行修复';

comment on column pay_sync_record.repair_no is '修复单号';

comment on column pay_sync_record.error_code is '错误码';

comment on column pay_sync_record.error_msg is '错误消息';

comment on column pay_sync_record.client_ip is '客户端IP';

comment on column pay_sync_record.creator is '创建者ID';

comment on column pay_sync_record.create_time is '创建时间';



INSERT INTO pay_sync_record (id, trade_no, biz_trade_no, out_trade_no, out_trade_status, sync_type, channel, sync_info, repair, repair_no, error_code, error_msg, client_ip, creator, create_time) VALUES (1796526917725888512, 'nullP240531205753null000001', 'P1717160268917', '2024053122001488181401741346', 'pay_success', 'pay', 'ali_pay', '{"body":"{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"155******67\",\"buyer_pay_amount\":\"0.00\",\"invoice_amount\":\"0.00\",\"out_trade_no\":\"nullP240531205753null000001\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2024-05-31 20:58:12\",\"total_amount\":\"0.01\",\"trade_no\":\"2024053122001488181401741346\",\"trade_status\":\"TRADE_SUCCESS\",\"buyer_open_id\":\"018qp9iBRAuHV4z50B6UGkpJ9GzUjmlbcVBlLdI47BwcgEb\"},\"sign\":\"NCfdXQtJYtlAttjha1jEW9bsi6g9RX9odDss/yWoj087/gxt/+lILwVasiYxZmxW9jKZ8ZlkAgF09WkNj1Ex0yaJ+MSWMro+ezL1w/6prvVgMO2+Tw6z217aV8GjaH8IBXprmJejI+8qLKKoQimvaeDG/xOzhT5H3NK9nj/nL+9UevzeRJkXAu6Ee6oqc4h7qoZpQ21bZWeCXazxb3xbx74aELStoaf/7NNqsKAVNBAQkh+hhbtubOGdCRtGV2AcmKuVjFjT+Hsg9PeJPcfvnEQJynbclPi43Wj1cIepc70qbBryq6GRowYDYYeq3mmDUq5wf7geLgvQ4r3fQ7SgXw==\"}","buyerLogonId":"155******67","buyerOpenId":"018qp9iBRAuHV4z50B6UGkpJ9GzUjmlbcVBlLdI47BwcgEb","buyerPayAmount":"0.00","invoiceAmount":"0.00","outTradeNo":"nullP240531205753null000001","pointAmount":"0.00","receiptAmount":"0.00","sendPayDate":1717160292000,"totalAmount":"0.01","tradeNo":"2024053122001488181401741346","tradeStatus":"TRADE_SUCCESS","code":"10000","msg":"Success","params":{"biz_content":"{\"out_trade_no\":\"nullP240531205753null000001\"}"}}', true, '1796526917600059392', null, null, null, 1399985191002447872, '2024-05-31 20:59:29.698000');

-----
create table pay_transfer_order
(
    id                 bigint  not null
        primary key,
    out_trade_no       varchar(255),
    channel            varchar(255),
    amount             integer,
    status             varchar(255),
    payer              varchar(255),
    payee              varchar(255),
    success_time       timestamp(6),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_transfer_order is '转账订单';

comment on column pay_transfer_order.id is '主键';

comment on column pay_transfer_order.creator is '创建者ID';

comment on column pay_transfer_order.create_time is '创建时间';

comment on column pay_transfer_order.last_modifier is '最后修者ID';

comment on column pay_transfer_order.last_modified_time is '最后修改时间';

comment on column pay_transfer_order.version is '乐观锁';

comment on column pay_transfer_order.deleted is '删除标志';




-----
create table pay_union_pay_config
(
    id                   bigint  not null
        primary key,
    mach_id              varchar(255),
    enable               boolean,
    server_url           varchar(255),
    notify_url           varchar(255),
    pay_ways             text,
    single_limit         integer,
    remark               varchar(255),
    seller               varchar(255),
    sign_type            varchar(255),
    cert_sign            boolean,
    key_private_cert     text,
    key_private_cert_pwd varchar(255),
    acp_middle_cert      text,
    acp_root_cert        text,
    sandbox              boolean,
    return_url           varchar(255),
    creator              bigint,
    create_time          timestamp(6),
    last_modifier        bigint,
    last_modified_time   timestamp(6),
    version              integer not null,
    deleted              boolean not null
);

comment on table pay_union_pay_config is '云闪付支付配置';

comment on column pay_union_pay_config.id is '主键';

comment on column pay_union_pay_config.mach_id is '商户号';

comment on column pay_union_pay_config.enable is '是否启用';

comment on column pay_union_pay_config.server_url is '支付网关地址';

comment on column pay_union_pay_config.notify_url is '异步通知路径';

comment on column pay_union_pay_config.pay_ways is '可用支付方式';

comment on column pay_union_pay_config.single_limit is '支付限额';

comment on column pay_union_pay_config.remark is '备注';

comment on column pay_union_pay_config.seller is '商户收款账号';

comment on column pay_union_pay_config.sign_type is '签名类型';

comment on column pay_union_pay_config.cert_sign is '是否为证书签名';

comment on column pay_union_pay_config.key_private_cert is '应用私钥证书';

comment on column pay_union_pay_config.key_private_cert_pwd is '私钥证书对应的密码';

comment on column pay_union_pay_config.acp_middle_cert is '中级证书';

comment on column pay_union_pay_config.acp_root_cert is '根证书';

comment on column pay_union_pay_config.sandbox is '是否沙箱环境';

comment on column pay_union_pay_config.return_url is '同步通知页面路径';

comment on column pay_union_pay_config.creator is '创建者ID';

comment on column pay_union_pay_config.create_time is '创建时间';

comment on column pay_union_pay_config.last_modifier is '最后修者ID';

comment on column pay_union_pay_config.last_modified_time is '最后修改时间';

comment on column pay_union_pay_config.version is '乐观锁';

comment on column pay_union_pay_config.deleted is '删除标志';



INSERT INTO pay_union_pay_config (id, mach_id, enable, server_url, notify_url, pay_ways, single_limit, remark, seller, sign_type, cert_sign, key_private_cert, key_private_cert_pwd, acp_middle_cert, acp_root_cert, sandbox, return_url, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (0, null, null, null, null, 'wap,web,qrcode,app,barcode', null, null, null, null, null, null, null, null, null, null, null, 0, '2024-03-06 22:56:22.000000', 1399985191002447872, '2024-03-23 23:15:20.000000', 12, false);

-----
create table pay_union_reconcile_bill_detail
(
    trade_type   varchar(255),
    txn_time     varchar(255),
    txn_amt      varchar(255),
    query_id     varchar(255),
    order_id     varchar(255),
    reconcile_id bigint
);

comment on table pay_union_reconcile_bill_detail is '云闪付业务明细对账单';

comment on column pay_union_reconcile_bill_detail.trade_type is '交易代码';

comment on column pay_union_reconcile_bill_detail.txn_time is '交易传输时间';

comment on column pay_union_reconcile_bill_detail.txn_amt is '交易金额';

comment on column pay_union_reconcile_bill_detail.query_id is '查询流水号';

comment on column pay_union_reconcile_bill_detail.order_id is '商户订单号';

comment on column pay_union_reconcile_bill_detail.reconcile_id is '关联对账订单ID';




-----
create table pay_wallet
(
    id                 bigint  not null
        primary key,
    user_id            varchar(255),
    name               varchar(255),
    balance            integer,
    status             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_wallet is '钱包';

comment on column pay_wallet.id is '主键';

comment on column pay_wallet.user_id is '关联用户id';

comment on column pay_wallet.name is '钱包名称';

comment on column pay_wallet.balance is '余额';

comment on column pay_wallet.status is '状态';

comment on column pay_wallet.creator is '创建者ID';

comment on column pay_wallet.create_time is '创建时间';

comment on column pay_wallet.last_modifier is '最后修者ID';

comment on column pay_wallet.last_modified_time is '最后修改时间';

comment on column pay_wallet.version is '乐观锁';

comment on column pay_wallet.deleted is '删除标志';



create index 用户id索引
    on pay_wallet (user_id);


-----
create table pay_wallet_config
(
    id                 bigint  not null
        primary key,
    enable             boolean,
    single_limit       integer,
    pay_ways           varchar(255),
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_wallet_config is '钱包配置';

comment on column pay_wallet_config.id is '主键';

comment on column pay_wallet_config.enable is '是否启用';

comment on column pay_wallet_config.single_limit is '单次支付最多多少金额 ';

comment on column pay_wallet_config.pay_ways is '可用支付方式';

comment on column pay_wallet_config.remark is '备注';

comment on column pay_wallet_config.creator is '创建者ID';

comment on column pay_wallet_config.create_time is '创建时间';

comment on column pay_wallet_config.last_modifier is '最后修者ID';

comment on column pay_wallet_config.last_modified_time is '最后修改时间';

comment on column pay_wallet_config.version is '乐观锁';

comment on column pay_wallet_config.deleted is '删除标志';



INSERT INTO pay_wallet_config (id, enable, single_limit, pay_ways, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (0, false, 2000, 'normal', null, 0, '2024-02-17 14:36:28.000000', 1399985191002447872, '2024-02-17 14:40:45.000000', 4, false);

-----
create table pay_wallet_record
(
    id          bigint not null
        primary key,
    wallet_id   bigint,
    title       varchar(255),
    type        varchar(255),
    amount      integer,
    old_amount  integer,
    new_amount  integer,
    order_id    varchar(255),
    ip          varchar(255),
    remark      varchar(255),
    creator     bigint,
    create_time timestamp(6)
);

comment on table pay_wallet_record is '钱包记录';

comment on column pay_wallet_record.id is '主键';

comment on column pay_wallet_record.wallet_id is '钱包id';

comment on column pay_wallet_record.title is '标题';

comment on column pay_wallet_record.type is '业务类型';

comment on column pay_wallet_record.amount is '金额';

comment on column pay_wallet_record.old_amount is '变动之前的金额';

comment on column pay_wallet_record.new_amount is '变动之后的金额';

comment on column pay_wallet_record.order_id is '交易订单号';

comment on column pay_wallet_record.ip is '终端ip';

comment on column pay_wallet_record.remark is '备注';

comment on column pay_wallet_record.creator is '创建者ID';

comment on column pay_wallet_record.create_time is '创建时间';



create index wallet_id
    on pay_wallet_record (wallet_id);

comment on index wallet_id is '钱包ID';

-----
create table pay_wechat_pay_config
(
    id                 bigint  not null
        primary key,
    wx_mch_id          varchar(255),
    wx_app_id          varchar(255),
    enable             boolean,
    notify_url         varchar(255),
    return_url         varchar(255),
    allocation         boolean,
    single_limit       integer,
    api_version        varchar(255),
    api_key_v2         varchar(255),
    api_key_v3         varchar(255),
    app_secret         varchar(255),
    p12                text,
    sandbox            boolean,
    pay_ways           text,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table pay_wechat_pay_config is '微信支付配置';

comment on column pay_wechat_pay_config.id is '主键';

comment on column pay_wechat_pay_config.wx_mch_id is '微信商户号';

comment on column pay_wechat_pay_config.wx_app_id is '微信应用appId';

comment on column pay_wechat_pay_config.enable is '是否启用';

comment on column pay_wechat_pay_config.notify_url is '异步通知路径';

comment on column pay_wechat_pay_config.return_url is '同步通知路径';

comment on column pay_wechat_pay_config.allocation is '是否支付分账';

comment on column pay_wechat_pay_config.single_limit is '支付限额';

comment on column pay_wechat_pay_config.api_version is '接口版本';

comment on column pay_wechat_pay_config.api_key_v2 is 'APIv2 密钥';

comment on column pay_wechat_pay_config.api_key_v3 is 'APIv3 密钥';

comment on column pay_wechat_pay_config.app_secret is 'APPID对应的接口密码';

comment on column pay_wechat_pay_config.p12 is 'API证书中p12证书Base64';

comment on column pay_wechat_pay_config.sandbox is '是否沙箱环境';

comment on column pay_wechat_pay_config.pay_ways is '可用支付方式';

comment on column pay_wechat_pay_config.remark is '备注';

comment on column pay_wechat_pay_config.creator is '创建者ID';

comment on column pay_wechat_pay_config.create_time is '创建时间';

comment on column pay_wechat_pay_config.last_modifier is '最后修者ID';

comment on column pay_wechat_pay_config.last_modified_time is '最后修改时间';

comment on column pay_wechat_pay_config.version is '乐观锁';

comment on column pay_wechat_pay_config.deleted is '删除标志';



INSERT INTO pay_wechat_pay_config (id, wx_mch_id, wx_app_id, enable, notify_url, return_url, allocation, single_limit, api_version, api_key_v2, api_key_v3, app_secret, p12, sandbox, pay_ways, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (0, null, null, null, null, null, null, null, null, null, null, null, null, null, 'wap,app,jsapi,qrcode,barcode', null, 0, '2024-01-03 23:13:11.000000', 1399985191002447872, '2024-04-02 17:07:37.000000', 23, false);

-----
create table pay_wechat_pay_record
(
    id               bigint not null
        primary key,
    title            varchar(255),
    amount           integer,
    type             varchar(255),
    order_id         bigint,
    gateway_order_no varchar(255),
    gateway_time     timestamp(6),
    creator          bigint,
    create_time      timestamp(6)
);

comment on table pay_wechat_pay_record is '微信支付记录';

comment on column pay_wechat_pay_record.id is '主键';

comment on column pay_wechat_pay_record.title is '标题';

comment on column pay_wechat_pay_record.amount is '金额';

comment on column pay_wechat_pay_record.type is '业务类型';

comment on column pay_wechat_pay_record.order_id is '本地订单号';

comment on column pay_wechat_pay_record.gateway_order_no is '网关订单号';

comment on column pay_wechat_pay_record.gateway_time is '网关完成时间';

comment on column pay_wechat_pay_record.creator is '创建者ID';

comment on column pay_wechat_pay_record.create_time is '创建时间';




-----
create table pay_wechat_reconcile_bill_detail
(
    id                   bigint not null
        primary key,
    record_order_id      bigint,
    transaction_time     varchar(255),
    app_id               varchar(255),
    merchant_id          varchar(255),
    sub_merchant_id      varchar(255),
    wei_xin_order_no     varchar(255),
    mch_order_no         varchar(255),
    user_id              varchar(255),
    device_info          varchar(255),
    type                 varchar(255),
    status               varchar(255),
    bank                 varchar(255),
    currency             varchar(255),
    amount               varchar(255),
    envelope_amount      varchar(255),
    wx_refund_no         varchar(255),
    mch_refund_no        varchar(255),
    refund_amount        varchar(255),
    coupon_refund_amount varchar(255),
    refund_type          varchar(255),
    refund_status        varchar(255),
    subject              varchar(255),
    mch_data_packet      varchar(255),
    premium              varchar(255),
    rates                varchar(255),
    order_amount         varchar(255),
    apply_refund_amount  varchar(255),
    rates_remark         varchar(255)
);

comment on table pay_wechat_reconcile_bill_detail is '微信对账单明细';

comment on column pay_wechat_reconcile_bill_detail.id is '主键';

comment on column pay_wechat_reconcile_bill_detail.record_order_id is '关联对账订单ID';

comment on column pay_wechat_reconcile_bill_detail.transaction_time is '交易时间';

comment on column pay_wechat_reconcile_bill_detail.app_id is '公众账号ID';

comment on column pay_wechat_reconcile_bill_detail.merchant_id is '商户号';

comment on column pay_wechat_reconcile_bill_detail.sub_merchant_id is '特约商户号';

comment on column pay_wechat_reconcile_bill_detail.wei_xin_order_no is '微信订单号';

comment on column pay_wechat_reconcile_bill_detail.mch_order_no is '商户订单号';

comment on column pay_wechat_reconcile_bill_detail.user_id is '用户标识';

comment on column pay_wechat_reconcile_bill_detail.device_info is '设备号';

comment on column pay_wechat_reconcile_bill_detail.type is '交易类型';

comment on column pay_wechat_reconcile_bill_detail.status is '交易状态';

comment on column pay_wechat_reconcile_bill_detail.bank is '付款银行';

comment on column pay_wechat_reconcile_bill_detail.currency is '货币种类';

comment on column pay_wechat_reconcile_bill_detail.amount is '应结订单金额';

comment on column pay_wechat_reconcile_bill_detail.envelope_amount is '代金券金额';

comment on column pay_wechat_reconcile_bill_detail.wx_refund_no is '微信退款单号';

comment on column pay_wechat_reconcile_bill_detail.mch_refund_no is '商户退款单号';

comment on column pay_wechat_reconcile_bill_detail.refund_amount is '退款金额';

comment on column pay_wechat_reconcile_bill_detail.coupon_refund_amount is '充值券退款金额';

comment on column pay_wechat_reconcile_bill_detail.refund_type is '退款类型';

comment on column pay_wechat_reconcile_bill_detail.refund_status is '退款状态';

comment on column pay_wechat_reconcile_bill_detail.subject is '商品名称';

comment on column pay_wechat_reconcile_bill_detail.mch_data_packet is '商户数据包';

comment on column pay_wechat_reconcile_bill_detail.premium is '手续费';

comment on column pay_wechat_reconcile_bill_detail.rates is '费率';

comment on column pay_wechat_reconcile_bill_detail.order_amount is '订单金额';

comment on column pay_wechat_reconcile_bill_detail.apply_refund_amount is '申请退款金额';

comment on column pay_wechat_reconcile_bill_detail.rates_remark is '费率备注';




-----
create table pay_wechat_reconcile_bill_total
(
    id                         bigint not null
        primary key,
    record_order_id            bigint,
    total_num                  varchar(255),
    total_amount               varchar(255),
    total_refund_amount        varchar(255),
    total_refund_coupon_amount varchar(255),
    total_fee                  varchar(255),
    total_order_amount         varchar(255),
    apply_total_refund_amount  varchar(255)
);

comment on table pay_wechat_reconcile_bill_total is '微信对账单汇总';

comment on column pay_wechat_reconcile_bill_total.id is '主键';

comment on column pay_wechat_reconcile_bill_total.record_order_id is '关联对账订单ID';

comment on column pay_wechat_reconcile_bill_total.total_num is '总交易单数';

comment on column pay_wechat_reconcile_bill_total.total_amount is '应结订单总金额';

comment on column pay_wechat_reconcile_bill_total.total_refund_amount is '退款总金额';

comment on column pay_wechat_reconcile_bill_total.total_refund_coupon_amount is '充值券退款总金额';

comment on column pay_wechat_reconcile_bill_total.total_fee is '手续费总金额';

comment on column pay_wechat_reconcile_bill_total.total_order_amount is '订单总金额';

comment on column pay_wechat_reconcile_bill_total.apply_total_refund_amount is '申请退款总金额';




-----
create table starter_audit_data_version
(
    id             bigint       not null
        primary key,
    table_name     varchar(300) not null,
    data_name      varchar(100),
    data_id        varchar(30)  not null,
    data_content   text,
    change_content text,
    version        integer      not null,
    creator        bigint,
    create_time    timestamp(6)
);

comment on table starter_audit_data_version is '数据版本日志';

comment on column starter_audit_data_version.table_name is '数据表名称';

comment on column starter_audit_data_version.data_name is '数据名称';

comment on column starter_audit_data_version.data_id is '数据主键';

comment on column starter_audit_data_version.data_content is '数据内容';

comment on column starter_audit_data_version.change_content is '数据更新内容';

comment on column starter_audit_data_version.version is '版本';

comment on column starter_audit_data_version.creator is '创建人';

comment on column starter_audit_data_version.create_time is '创建时间';




-----
create table starter_audit_login_log
(
    id             bigint not null
        primary key,
    user_id        bigint,
    account        varchar(50),
    login          boolean,
    client         varchar(50),
    login_type     varchar(50),
    ip             varchar(32),
    login_location varchar(50),
    os             varchar(50),
    browser        varchar(100),
    msg            varchar(255),
    login_time     timestamp(6)
);

comment on table starter_audit_login_log is '登陆日志';

comment on column starter_audit_login_log.user_id is '用户id';

comment on column starter_audit_login_log.account is '用户名称';

comment on column starter_audit_login_log.login is '登录成功状态';

comment on column starter_audit_login_log.client is '终端';

comment on column starter_audit_login_log.login_type is '登录方式';

comment on column starter_audit_login_log.ip is '登录IP地址';

comment on column starter_audit_login_log.login_location is '登录地点';

comment on column starter_audit_login_log.os is '操作系统';

comment on column starter_audit_login_log.browser is '浏览器类型';

comment on column starter_audit_login_log.msg is '提示消息';

comment on column starter_audit_login_log.login_time is '访问时间';



INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789981400523988992, 1757299137932677120, 'daxpay', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:29:57.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789981854330904576, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:31:45.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789982145977638912, 1757299137932677120, 'daxpay', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:32:55.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789985967902941184, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:48:06.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789986099583115264, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:48:37.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1789986131078144000, 1757299137932677120, 'daxpay', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'Chrome 122.0.6261.95', null, '2024-05-13 19:48:45.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1794400374968598528, 1757299137932677120, 'daxpay', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-26 00:09:21.000000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1796211013793935360, 1757299137932677120, 'daxpay', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-31 00:04:11.654000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1796213171591376896, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-31 00:12:46.789000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1796214423381405696, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '0:0:0:0:0:0:0:1', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-31 00:17:45.240000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1796524537340928000, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '117.147.8.49', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-31 20:50:02.134000');
INSERT INTO starter_audit_login_log (id, user_id, account, login, client, login_type, ip, login_location, os, browser, msg, login_time) VALUES (1796539663007784960, 1399985191002447872, 'bootx', true, 'dax-pay', 'password', '117.147.8.49', '未知', 'Windows 10 or Windows Server 2016', 'MSEdge 125.0.0.0', null, '2024-05-31 21:50:08.409000');

-----
create table starter_audit_operate_log
(
    id               bigint not null
        primary key,
    title            varchar(255),
    operate_id       bigint,
    username         varchar(50),
    business_type    varchar(10),
    method           varchar(100),
    request_method   varchar(10),
    operate_url      varchar(100),
    operate_ip       varchar(32),
    operate_location varchar(50),
    operate_param    varchar(1024),
    operate_return   varchar(1024),
    success          boolean,
    error_msg        text,
    operate_time     timestamp(6)
);

comment on table starter_audit_operate_log is '操作日志';

comment on column starter_audit_operate_log.title is '操作模块';

comment on column starter_audit_operate_log.operate_id is '操作人员id';

comment on column starter_audit_operate_log.username is '操作人员账号';

comment on column starter_audit_operate_log.business_type is '业务类型';

comment on column starter_audit_operate_log.method is '请求方法';

comment on column starter_audit_operate_log.request_method is '请求方式';

comment on column starter_audit_operate_log.operate_url is '请求url';

comment on column starter_audit_operate_log.operate_ip is '操作ip';

comment on column starter_audit_operate_log.operate_location is '操作地点';

comment on column starter_audit_operate_log.operate_param is '请求参数';

comment on column starter_audit_operate_log.operate_return is '返回参数';

comment on column starter_audit_operate_log.success is '是否成功';

comment on column starter_audit_operate_log.error_msg is '错误提示';

comment on column starter_audit_operate_log.operate_time is '操作时间';



INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1764663811019894784, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-04 22:46:53.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1764932301155672064, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-05 16:33:46.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1764932527790702592, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-05 16:34:39.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1764933649263722496, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-05 16:39:06.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1768219996095213568, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-14 18:17:54.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1772446742462001152, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-03-26 10:13:29.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1780216506568273920, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-04-16 20:47:45.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1780417659281174528, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-04-17 10:07:03.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1780417857529147392, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-04-17 10:07:50.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1780418146982260736, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-04-17 10:08:59.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1780418343170830336, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-04-17 10:09:46.000000');
INSERT INTO starter_audit_operate_log (id, title, operate_id, username, business_type, method, request_method, operate_url, operate_ip, operate_location, operate_param, operate_return, success, error_msg, operate_time) VALUES (1789978119609376768, '同步系统请求资源', 1399985191002447872, 'bootx', 'other', 'cn.bootx.platform.iam.core.permission.service.PermPathService#syncSystem', 'POST', '/perm/path/syncSystem', '未知', '未知', null, null, true, null, '2024-05-13 19:16:55.000000');

-----
create table starter_ding_media_md5
(
    id          bigint       not null
        primary key,
    media_id    varchar(50)  not null,
    md5         varchar(255) not null,
    creator     bigint,
    create_time timestamp(6)
);

comment on table starter_ding_media_md5 is '钉钉媒体文件MD5值关联关系';

comment on column starter_ding_media_md5.media_id is '媒体id';

comment on column starter_ding_media_md5.md5 is 'md5值';

comment on column starter_ding_media_md5.creator is '创建人';

comment on column starter_ding_media_md5.create_time is '创建时间';




-----
create table starter_ding_robot_config
(
    id                     bigint      not null
        primary key,
    name                   varchar(50),
    code                   varchar(50) not null,
    access_token           varchar(200),
    enable_signature_check boolean     not null,
    sign_secret            varchar(255),
    remark                 varchar(255),
    creator                bigint,
    create_time            timestamp(6),
    last_modifier          bigint,
    last_modified_time     timestamp(6),
    version                integer     not null,
    deleted                boolean     not null
);

comment on table starter_ding_robot_config is '钉钉机器人配置';

comment on column starter_ding_robot_config.name is '名称';

comment on column starter_ding_robot_config.code is '编号';

comment on column starter_ding_robot_config.access_token is '钉钉机器人访问token';

comment on column starter_ding_robot_config.enable_signature_check is '是否开启验签';

comment on column starter_ding_robot_config.sign_secret is '钉钉机器人私钥';

comment on column starter_ding_robot_config.remark is '备注';

comment on column starter_ding_robot_config.creator is '创建人';

comment on column starter_ding_robot_config.create_time is '创建时间';

comment on column starter_ding_robot_config.last_modifier is '最后修改人';

comment on column starter_ding_robot_config.last_modified_time is '最后修改时间';

comment on column starter_ding_robot_config.version is '版本';

comment on column starter_ding_robot_config.deleted is '0:未删除。1:已删除';




-----
create table starter_file_data
(
    id     bigint not null
        primary key,
    base64 text,
    data   bytea
);

comment on table starter_file_data is '上传文件数据';

comment on column starter_file_data.id is '主键';

comment on column starter_file_data.base64 is 'base64方式存储';

comment on column starter_file_data.data is '数据方式存储';




-----
create table starter_file_upload_info
(
    id                bigint       not null
        primary key,
    url               varchar(512) not null,
    size              bigint,
    filename          varchar(256),
    original_filename varchar(256),
    base_path         varchar(256),
    path              varchar(256),
    ext               varchar(32),
    content_type      varchar(128),
    platform          varchar(32),
    th_url            varchar(512),
    th_filename       varchar(256),
    th_size           bigint,
    th_content_type   varchar(128),
    object_id         varchar(32),
    object_type       varchar(32),
    metadata          text,
    user_metadata     text,
    th_metadata       text,
    th_user_metadata  text,
    attr              text,
    file_acl          varchar(32),
    th_file_acl       varchar(32),
    create_time       timestamp(6)
);

comment on table starter_file_upload_info is '文件记录表';

comment on column starter_file_upload_info.id is '文件id';

comment on column starter_file_upload_info.url is '文件访问地址';

comment on column starter_file_upload_info.size is '文件大小，单位字节';

comment on column starter_file_upload_info.filename is '文件名称';

comment on column starter_file_upload_info.original_filename is '原始文件名';

comment on column starter_file_upload_info.base_path is '基础存储路径';

comment on column starter_file_upload_info.path is '存储路径';

comment on column starter_file_upload_info.ext is '文件扩展名';

comment on column starter_file_upload_info.content_type is 'MIME类型';

comment on column starter_file_upload_info.platform is '存储平台';

comment on column starter_file_upload_info.th_url is '缩略图访问路径';

comment on column starter_file_upload_info.th_filename is '缩略图名称';

comment on column starter_file_upload_info.th_size is '缩略图大小，单位字节';

comment on column starter_file_upload_info.th_content_type is '缩略图MIME类型';

comment on column starter_file_upload_info.object_id is '文件所属对象id';

comment on column starter_file_upload_info.object_type is '文件所属对象类型，例如用户头像，评价图片';

comment on column starter_file_upload_info.metadata is '文件元数据';

comment on column starter_file_upload_info.user_metadata is '文件用户元数据';

comment on column starter_file_upload_info.th_metadata is '缩略图元数据';

comment on column starter_file_upload_info.th_user_metadata is '缩略图用户元数据';

comment on column starter_file_upload_info.attr is '附加属性';

comment on column starter_file_upload_info.file_acl is '文件ACL';

comment on column starter_file_upload_info.th_file_acl is '缩略图文件ACL';

comment on column starter_file_upload_info.create_time is '创建时间';




-----
create table starter_quartz_job
(
    id                 bigint  not null
        primary key,
    name               varchar(255),
    job_class_name     varchar(255),
    cron               varchar(255),
    parameter          text,
    state              integer,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer not null,
    deleted            boolean not null
);

comment on table starter_quartz_job is '定时任务';

comment on column starter_quartz_job.id is '主键';

comment on column starter_quartz_job.name is '任务名称';

comment on column starter_quartz_job.job_class_name is '任务类名';

comment on column starter_quartz_job.cron is 'cron表达式';

comment on column starter_quartz_job.parameter is '参数';

comment on column starter_quartz_job.state is '状态';

comment on column starter_quartz_job.remark is '备注';

comment on column starter_quartz_job.creator is '创建人';

comment on column starter_quartz_job.create_time is '创建时间';

comment on column starter_quartz_job.last_modifier is '最后修改人';

comment on column starter_quartz_job.last_modified_time is '最后修改时间';

comment on column starter_quartz_job.version is '版本';

comment on column starter_quartz_job.deleted is '0:未删除。1:已删除';



INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1456579473573867520, '测试任务', 'cn.bootx.platform.starter.quartz.task.TestTask', '0/3 0 * * * ? *', '{"aaa":"5255"}', 0, '测试任务', 1399985191002447872, '2021-11-05 19:09:43.000000', 1399985191002447872, '2024-02-24 23:57:57.000000', 29, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1546857070483939328, '支付单超时检测', 'cn.daxpay.single.service.task.PayExpiredTimeTask', '0/5 * * * * ? *', null, 0, '检测超时的支付单, 超时后调用同步事件状态修复', 1399985191002447872, '2022-07-12 22:00:39.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 7, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1761419490908958720, '客户系统通知重发任务', 'cn.daxpay.single.service.task.ClientNoticeSendTask', '0/1 * * * * ? *', '', 0, '每秒调用一下当前需要进行通知的任务', 1399985191002447872, '2024-02-24 23:55:07.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 5, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1764664552203743232, '支付宝定时对账', 'cn.daxpay.single.service.task.ReconcileTask', '0 0 11 * * ? *', '{"channel":"ali_pay","n":1}', 0, '', 1399985191002447872, '2024-03-04 22:49:50.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 6, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1764667388106887168, '微信支付定时对账', 'cn.daxpay.single.service.task.ReconcileTask', '0 0 11 * * ? *', '{"channel":"wechat_pay","n":1}', 0, '', 1399985191002447872, '2024-03-04 23:01:06.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 6, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1767553847839141888, '退款订单定时同步', 'cn.daxpay.single.service.task.RefundSyncTask', '0 * * * * ? *', '', 0, '', 1399985191002447872, '2024-03-12 22:10:52.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 4, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1787464504578744320, '自动分账定时任务', 'cn.daxpay.single.service.task.AllocationAutoStartTask', '0/10 * * * * ? *', '', 0, '', 1399985191002447872, '2024-05-06 20:48:42.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 2, false);
INSERT INTO starter_quartz_job (id, name, job_class_name, cron, parameter, state, remark, creator, create_time, last_modifier, last_modified_time, version, deleted) VALUES (1787470450809954304, '自动分账信息同步和完结接口', 'cn.daxpay.single.service.task.AllocationSyncTask', '0/10 * * * * ? *', '', 0, '', 1399985191002447872, '2024-05-06 21:12:20.000000', 1399985191002447872, '2024-05-09 17:13:10.000000', 2, false);

-----
create table starter_quartz_job_log
(
    id            bigint  not null
        primary key,
    handler_name  varchar(255),
    class_name    varchar(255),
    success       boolean not null,
    error_message varchar(255),
    start_time    timestamp(6),
    end_time      timestamp(6),
    duration      bigint,
    create_time   timestamp(6)
);

comment on table starter_quartz_job_log is '任务执行日志';

comment on column starter_quartz_job_log.id is '主键';

comment on column starter_quartz_job_log.handler_name is '处理器名称';

comment on column starter_quartz_job_log.class_name is '处理器全限定名';

comment on column starter_quartz_job_log.success is '是否执行成功';

comment on column starter_quartz_job_log.error_message is '错误信息';

comment on column starter_quartz_job_log.start_time is '开始时间';

comment on column starter_quartz_job_log.end_time is '结束时间';

comment on column starter_quartz_job_log.duration is '执行时长';

comment on column starter_quartz_job_log.create_time is '创建时间';




-----
create table starter_wecom_robot_config
(
    id                 bigint       not null
        primary key,
    name               varchar(50)  not null,
    code               varchar(50)  not null,
    webhook_key        varchar(150) not null,
    remark             varchar(255),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer      not null,
    deleted            boolean      not null
);

comment on table starter_wecom_robot_config is '企业微信机器人配置';

comment on column starter_wecom_robot_config.name is '名称';

comment on column starter_wecom_robot_config.code is '编号';

comment on column starter_wecom_robot_config.webhook_key is 'webhook地址的key值';

comment on column starter_wecom_robot_config.remark is '备注';

comment on column starter_wecom_robot_config.creator is '创建人';

comment on column starter_wecom_robot_config.create_time is '创建时间';

comment on column starter_wecom_robot_config.last_modifier is '最后修改人';

comment on column starter_wecom_robot_config.last_modified_time is '最后修改时间';

comment on column starter_wecom_robot_config.version is '版本';

comment on column starter_wecom_robot_config.deleted is '0:未删除。1:已删除';




-----
create table starter_wx_fans
(
    id               bigint not null
        primary key,
    openid           varchar(50),
    subscribe_status boolean,
    subscribe_time   timestamp(6),
    nickname         varchar(50),
    sex              varchar(3),
    language         varchar(255),
    country          varchar(255),
    province         varchar(255),
    city             varchar(255),
    avatar_url       varchar(255),
    remark           varchar(255)
);

comment on table starter_wx_fans is '微信公众号粉丝';

comment on column starter_wx_fans.openid is '关联OpenId';

comment on column starter_wx_fans.subscribe_status is '订阅状态';

comment on column starter_wx_fans.subscribe_time is '订阅时间';

comment on column starter_wx_fans.nickname is '昵称';

comment on column starter_wx_fans.sex is '性别';

comment on column starter_wx_fans.language is '语言';

comment on column starter_wx_fans.country is '国家';

comment on column starter_wx_fans.province is '省份';

comment on column starter_wx_fans.city is '城市';

comment on column starter_wx_fans.avatar_url is '头像地址';

comment on column starter_wx_fans.remark is '备注';




-----
create table starter_wx_menu
(
    id                 bigint  not null
        primary key,
    name               varchar(50),
    menu_info          text,
    remark             varchar(200),
    publish            boolean not null,
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    deleted            boolean not null,
    version            integer not null
);

comment on table starter_wx_menu is '微信自定义菜单';

comment on column starter_wx_menu.id is '主键';

comment on column starter_wx_menu.name is '名称';

comment on column starter_wx_menu.menu_info is '菜单信息';

comment on column starter_wx_menu.remark is '备注';

comment on column starter_wx_menu.publish is '是否发布';

comment on column starter_wx_menu.creator is '创建人';

comment on column starter_wx_menu.create_time is '创建时间';

comment on column starter_wx_menu.last_modifier is '最后修改人';

comment on column starter_wx_menu.last_modified_time is '最后修改时间';

comment on column starter_wx_menu.deleted is '0:未删除。1:已删除';

comment on column starter_wx_menu.version is '版本';




-----
create table starter_wx_template
(
    id                 bigint      not null
        primary key,
    name               varchar(50),
    code               varchar(50),
    enable             boolean     not null,
    template_id        varchar(50) not null,
    title              varchar(100),
    primary_industry   varchar(50),
    deputy_industry    varchar(50),
    content            varchar(500),
    example            varchar(500),
    creator            bigint,
    create_time        timestamp(6),
    last_modifier      bigint,
    last_modified_time timestamp(6),
    version            integer     not null
);

comment on table starter_wx_template is '微信消息模板';

comment on column starter_wx_template.name is '名称';

comment on column starter_wx_template.code is '编码';

comment on column starter_wx_template.enable is '是否启用';

comment on column starter_wx_template.template_id is '模板ID';

comment on column starter_wx_template.title is '模板标题';

comment on column starter_wx_template.primary_industry is '模板所属行业的一级行业';

comment on column starter_wx_template.deputy_industry is '模板所属行业的二级行业';

comment on column starter_wx_template.content is '模板内容';

comment on column starter_wx_template.example is '示例';

comment on column starter_wx_template.creator is '创建人';

comment on column starter_wx_template.create_time is '创建时间';

comment on column starter_wx_template.last_modifier is '最后修改人';

comment on column starter_wx_template.last_modified_time is '最后修改时间';

comment on column starter_wx_template.version is '版本';



create unique index inx_
    on starter_wx_template (template_id);

comment on index inx_ is '模板id';

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        BOOL         NOT NULL,
    IS_NONCONCURRENT  BOOL         NOT NULL,
    IS_UPDATE_DATA    BOOL         NOT NULL,
    REQUESTS_RECOVERY BOOL         NOT NULL,
    JOB_DATA          BYTEA        NULL,
    PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT       NULL,
    PREV_FIRE_TIME BIGINT       NULL,
    PRIORITY       INTEGER      NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT       NOT NULL,
    END_TIME       BIGINT       NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT     NULL,
    JOB_DATA       BYTEA        NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    BIGINT       NOT NULL,
    REPEAT_INTERVAL BIGINT       NOT NULL,
    TIMES_TRIGGERED BIGINT       NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80),
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INT            NULL,
    INT_PROP_2    INT            NULL,
    LONG_PROP_1   BIGINT         NULL,
    LONG_PROP_2   BIGINT         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   BOOL           NULL,
    BOOL_PROP_2   BOOL           NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BYTEA        NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BYTEA        NOT NULL,
    PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT       NOT NULL,
    SCHED_TIME        BIGINT       NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  BOOL         NULL,
    REQUESTS_RECOVERY BOOL         NULL,
    PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT       NOT NULL,
    CHECKIN_INTERVAL  BIGINT       NOT NULL,
    PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL,
    PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY
    ON QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP
    ON QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J
    ON QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG
    ON QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C
    ON QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G
    ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE
    ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE
    ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE
    ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME
    ON QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST
    ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE
    ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE
    ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
    ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG
    ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);


COMMIT;


