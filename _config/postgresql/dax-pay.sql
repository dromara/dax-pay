
-- ----------------------------
-- Table structure for base_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_dict";
CREATE TABLE "public"."base_dict" (
  "id" int8 NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "group_tag" varchar(50) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "remark" varchar(50) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "enable" bool,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."base_dict"."id" IS '主键';
COMMENT ON COLUMN "public"."base_dict"."name" IS '名称';
COMMENT ON COLUMN "public"."base_dict"."group_tag" IS '分类标签';
COMMENT ON COLUMN "public"."base_dict"."code" IS '编码';
COMMENT ON COLUMN "public"."base_dict"."remark" IS '备注';
COMMENT ON COLUMN "public"."base_dict"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_dict"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_dict"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_dict"."version" IS '版本号';
COMMENT ON COLUMN "public"."base_dict"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."base_dict"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."base_dict" IS '字典表';

-- ----------------------------
-- Records of base_dict
-- ----------------------------
INSERT INTO "public"."base_dict" VALUES (1810495414609829888, '123', '支付', '123', '', 0, '2024-07-09 10:05:18.884695', 1811365615815487488, '2024-08-14 19:48:05.212961', 0, 't', 't');
INSERT INTO "public"."base_dict" VALUES (1823688893108133888, '支付订单关闭类型', '支付', 'close_type', '', 1811365615815487488, '2024-08-14 19:51:29.275426', 1811365615815487488, '2024-08-14 19:51:29.277016', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823688398549360640, '支付通道', '支付', 'channel', '', 1811365615815487488, '2024-08-14 19:49:31.363417', 1811365615815487488, '2024-08-14 19:56:44.841238', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823696159936946176, '证件类型', '基础信息', 'id_type', '', 1811365615815487488, '2024-08-14 20:20:21.822676', 1811365615815487488, '2024-08-14 20:20:21.824229', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823708720229117952, '签名方式', '支付', 'sign_type', '', 1811365615815487488, '2024-08-14 21:10:16.430774', 1811365615815487488, '2024-08-14 21:10:16.433984', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823928239337771008, '商户状态', '支付', 'merchant_status', '', 1811365615815487488, '2024-08-15 11:42:33.863751', 1811365615815487488, '2024-08-15 11:42:33.865868', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823928536063807488, '商户应用状态', '支付', 'mch_app_status', '', 1811365615815487488, '2024-08-15 11:43:44.608309', 1811365615815487488, '2024-08-15 11:43:44.60988', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823969170598400000, '客户通知内容类型', '支付', 'notify_content_type', '', 1811365615815487488, '2024-08-15 14:25:12.636886', 1811365615815487488, '2024-08-15 15:48:34.51576', 2, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823969966421446656, '商户消息通知类型', '支付', 'merchant_notify_type', '', 1811365615815487488, '2024-08-15 14:28:22.375016', 1811365615815487488, '2024-08-15 15:48:43.031616', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1823991280205041664, '业务操作类型', '审计日志', 'log_business_type', '', 0, '2024-08-15 15:53:03.977005', 0, '2024-08-15 15:53:03.978035', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824277928167870464, '支付订单的退款状态', '支付', 'pay_refund_status', '', 1811365615815487488, '2024-08-16 10:52:06.177341', 1811365615815487488, '2024-08-16 10:52:06.179417', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824285303885008896, '支付方式', '支付', 'pay_method', '', 1811365615815487488, '2024-08-16 11:21:24.685429', 1811365615815487488, '2024-08-16 11:21:24.686997', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824050661785407488, '支付订单状态', '支付', 'pay_status', '', 0, '2024-08-15 19:49:01.649012', 1811365615815487488, '2024-08-16 11:24:05.396307', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824277760341184512, '支付订单分账状态', '支付', 'pay_alloc_status', '支付订单的分账状态', 1811365615815487488, '2024-08-16 10:51:26.166066', 1811365615815487488, '2024-08-16 14:42:42.689952', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824287281285435392, '退款状态枚举', '支付', 'refund_status', '', 1811365615815487488, '2024-08-16 11:29:16.134278', 1811365615815487488, '2024-08-16 15:03:53.495862', 7, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824342698652971008, '交易流水记录类型', '支付', 'trade_flow_type', '', 1811365615815487488, '2024-08-16 15:09:28.666504', 1811365615815487488, '2024-08-16 15:09:34.017606', 1, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1824791500966486016, '交易类型', '支付', 'trade_type', '', 1811365615815487488, '2024-08-17 20:52:51.47629', 1811365615815487488, '2024-08-17 20:52:51.532979', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825134996013670400, '转账状态', '支付', 'transfer_status', '', 1811365615815487488, '2024-08-18 19:37:47.076212', 1811365615815487488, '2024-08-18 19:37:47.137228', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825135068277334016, '转账接收方类型', '支付', 'transfer_payee_type', '', 1811365615815487488, '2024-08-18 19:38:04.304349', 1811365615815487488, '2024-08-18 19:38:04.306948', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825174702763966464, '支付回调处理状态', '支付', 'callback_status', '', 1811365615815487488, '2024-08-18 22:15:33.902812', 1811365615815487488, '2024-08-18 22:15:33.90384', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1825408039604842496, '消息发送类型', '支付', 'notice_send_type', '', 1811365615815487488, '2024-08-19 13:42:45.740594', 1811365615815487488, '2024-08-19 13:42:45.742689', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826059944274382848, '对账差异类型', '支付', 'reconcile_discrepancy_type', '', 1811365615815487488, '2024-08-21 08:53:11.927052', 1811365615815487488, '2024-08-21 08:53:11.931319', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826061072412135424, '对账结果', '支付', 'reconcile_result', '', 1811365615815487488, '2024-08-21 08:57:40.895007', 1811365615815487488, '2024-08-21 08:57:40.897049', 0, 't', 'f');
INSERT INTO "public"."base_dict" VALUES (1826143914542350336, '交易状态', '支付', 'trade_status', '', 1811365615815487488, '2024-08-21 14:26:52.000272', 1811365615815487488, '2024-08-21 14:26:52.005675', 0, 't', 'f');

-- ----------------------------
-- Table structure for base_dict_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_dict_item";
CREATE TABLE "public"."base_dict_item" (
  "id" int8 NOT NULL,
  "dict_id" int8 NOT NULL,
  "dict_code" varchar(50) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "sort_no" int4,
  "enable" bool,
  "remark" varchar(50) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."base_dict_item"."id" IS '主键';
COMMENT ON COLUMN "public"."base_dict_item"."dict_id" IS '字典ID';
COMMENT ON COLUMN "public"."base_dict_item"."dict_code" IS '字典编码';
COMMENT ON COLUMN "public"."base_dict_item"."code" IS '字典项编码';
COMMENT ON COLUMN "public"."base_dict_item"."name" IS '名称';
COMMENT ON COLUMN "public"."base_dict_item"."sort_no" IS '字典项排序';
COMMENT ON COLUMN "public"."base_dict_item"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."base_dict_item"."remark" IS '备注';
COMMENT ON COLUMN "public"."base_dict_item"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_dict_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_dict_item"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_dict_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_dict_item"."version" IS '版本号';
COMMENT ON COLUMN "public"."base_dict_item"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."base_dict_item" IS '字典项';

-- ----------------------------
-- Records of base_dict_item
-- ----------------------------
INSERT INTO "public"."base_dict_item" VALUES (1810550427952537600, 1810495414609829888, '123', 'ignore', '忽略', 0, 't', '', 0, '2024-07-09 13:43:55.081435', 1811365615815487488, '2024-08-14 19:48:01.827254', 1, 't');
INSERT INTO "public"."base_dict_item" VALUES (1824050515865571328, 1823688398549360640, 'channel', 'pay_status', '支付状态', 0, 't', '', 0, '2024-08-15 19:48:26.861144', 0, '2024-08-15 19:48:32.569552', 0, 't');
INSERT INTO "public"."base_dict_item" VALUES (1823688758965903360, 1823688398549360640, 'channel', 'union_pay', '云闪付', 5, 't', '', 1811365615815487488, '2024-08-14 19:50:57.293338', 1811365615815487488, '2024-08-14 19:50:57.294914', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823696988228100096, 1823696159936946176, 'id_type', 'IDCard', '身份证', 1, 't', '', 1811365615815487488, '2024-08-14 20:23:39.302492', 1811365615815487488, '2024-08-14 20:23:39.304061', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823697045249662976, 1823696159936946176, 'id_type', 'passport', '护照', 2, 't', '', 1811365615815487488, '2024-08-14 20:23:52.897578', 1811365615815487488, '2024-08-14 20:23:52.89914', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823697185523965952, 1823696159936946176, 'id_type', 'ForeignGreenCard', '外国人绿卡', 4, 't', '', 1811365615815487488, '2024-08-14 20:24:26.341242', 1811365615815487488, '2024-08-14 20:24:26.342281', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823697128431099904, 1823696159936946176, 'id_type', 'MilitaryID', '军人证', 3, 't', '', 1811365615815487488, '2024-08-14 20:24:12.729636', 1811365615815487488, '2024-08-14 20:24:33.016577', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823709978780364800, 1823708720229117952, 'sign_type', 'hmac_sha256', 'HMAC_SHA256', 1, 't', '', 1811365615815487488, '2024-08-14 21:15:16.491026', 1811365615815487488, '2024-08-14 21:15:16.493648', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823710019247009792, 1823708720229117952, 'sign_type', 'md5', 'MD5', 2, 't', '', 1811365615815487488, '2024-08-14 21:15:26.139263', 1811365615815487488, '2024-08-14 21:15:26.140825', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823710059575242752, 1823708720229117952, 'sign_type', 'sm3', 'SM3', 3, 't', '', 1811365615815487488, '2024-08-14 21:15:35.754963', 1811365615815487488, '2024-08-14 21:15:35.756536', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823928285231845376, 1823928239337771008, 'merchant_status', 'enable', '启用', 1, 't', '', 1811365615815487488, '2024-08-15 11:42:44.805431', 1811365615815487488, '2024-08-15 11:42:44.807006', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823928325820125184, 1823928239337771008, 'merchant_status', 'disabled', '禁用', 2, 't', '', 1811365615815487488, '2024-08-15 11:42:54.482892', 1811365615815487488, '2024-08-15 11:42:54.484445', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823928584776454144, 1823928536063807488, 'mch_app_status', 'enable', '启用', 1, 't', '', 1811365615815487488, '2024-08-15 11:43:56.222637', 1811365615815487488, '2024-08-15 11:43:56.2242', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823928622999146496, 1823928536063807488, 'mch_app_status', 'disabled', '禁用', 2, 't', '', 1811365615815487488, '2024-08-15 11:44:05.335689', 1811365615815487488, '2024-08-15 11:44:05.337294', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823969227796123648, 1823969170598400000, 'notify_content_type', 'pay', '支付订单变动通知', 1, 't', '', 1811365615815487488, '2024-08-15 14:25:26.273922', 1811365615815487488, '2024-08-15 14:25:26.276032', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823969260352311296, 1823969170598400000, 'notify_content_type', 'refund', '退款订单变动通知', 2, 't', '', 1811365615815487488, '2024-08-15 14:25:34.035515', 1811365615815487488, '2024-08-15 14:25:34.037117', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823969294699466752, 1823969170598400000, 'notify_content_type', 'transfer', '转账订单变动通知', 3, 't', '', 1811365615815487488, '2024-08-15 14:25:42.224889', 1811365615815487488, '2024-08-15 14:25:42.226456', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823970069660045312, 1823969966421446656, 'merchant_notify_type', 'none', '不启用', 0, 't', '', 1811365615815487488, '2024-08-15 14:28:46.98963', 1811365615815487488, '2024-08-15 14:28:46.991208', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823970114547486720, 1823969966421446656, 'merchant_notify_type', 'http', 'HTTP', 1, 't', '', 1811365615815487488, '2024-08-15 14:28:57.6917', 1811365615815487488, '2024-08-15 14:28:57.692751', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823970397553954816, 1823969966421446656, 'merchant_notify_type', 'websocket', 'WebSocket', 2, 't', '', 1811365615815487488, '2024-08-15 14:30:05.165149', 1811365615815487488, '2024-08-15 14:30:05.166201', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823970441921302528, 1823969966421446656, 'merchant_notify_type', 'mq', '消息队列', 3, 'f', '', 1811365615815487488, '2024-08-15 14:30:15.74388', 1811365615815487488, '2024-08-15 15:41:11.358752', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823991809660424192, 1823991280205041664, 'log_business_type', 'add', '新增', 1, 't', '', 0, '2024-08-15 15:55:10.209099', 0, '2024-08-15 15:55:10.210152', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992621597986816, 1823991280205041664, 'log_business_type', 'update', '修改', 2, 't', '', 0, '2024-08-15 15:58:23.790857', 0, '2024-08-15 15:58:34.534328', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992712492748800, 1823991280205041664, 'log_business_type', 'delete', '删除', 3, 't', '', 0, '2024-08-15 15:58:45.461362', 0, '2024-08-15 15:58:45.462464', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992755463393280, 1823991280205041664, 'log_business_type', 'grant', '授权', 4, 't', '', 0, '2024-08-15 15:58:55.70643', 0, '2024-08-15 15:58:55.707474', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992808680722432, 1823991280205041664, 'log_business_type', 'export', '导出', 5, 't', '', 0, '2024-08-15 15:59:08.394231', 0, '2024-08-15 15:59:08.395306', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992851131273216, 1823991280205041664, 'log_business_type', 'import', '导入', 6, 't', '', 0, '2024-08-15 15:59:18.515719', 0, '2024-08-15 15:59:18.516751', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992894504570880, 1823991280205041664, 'log_business_type', 'force', '强退', 7, 't', '', 0, '2024-08-15 15:59:28.856243', 0, '2024-08-15 15:59:28.857426', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992935814270976, 1823991280205041664, 'log_business_type', 'clean', '清空数据', 8, 't', '', 0, '2024-08-15 15:59:38.705444', 0, '2024-08-15 15:59:38.706503', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823992995331444736, 1823991280205041664, 'log_business_type', 'other', '其它', 9, 't', '', 0, '2024-08-15 15:59:52.895664', 0, '2024-08-15 15:59:52.896179', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823688520268062720, 1823688398549360640, 'channel', 'ali_pay', '支付宝(直连)', 1, 't', '支付宝 - 直连商户', 1811365615815487488, '2024-08-14 19:50:00.383209', 0, '2024-08-15 19:45:52.133311', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823688577201545216, 1823688398549360640, 'channel', 'ali_service', '支付宝(服务商)', 2, 't', '支付宝 - 服务商商户', 1811365615815487488, '2024-08-14 19:50:13.957732', 0, '2024-08-15 19:46:14.070089', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823688680352063488, 1823688398549360640, 'channel', 'wechat_pay', '微信支付(直连)', 3, 't', '', 1811365615815487488, '2024-08-14 19:50:38.551335', 0, '2024-08-15 19:46:22.364663', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1823688722165080064, 1823688398549360640, 'channel', 'wechat_service', '微信支付(服务商)', 4, 't', '', 1811365615815487488, '2024-08-14 19:50:48.519498', 0, '2024-08-15 19:46:30.624402', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824277977388027904, 1824277928167870464, 'pay_refund_status', 'no_refund', '未退款', 1, 't', '', 1811365615815487488, '2024-08-16 10:52:17.912141', 1811365615815487488, '2024-08-16 10:52:17.913687', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824278014436315136, 1824277928167870464, 'pay_refund_status', 'refunding', '退款中', 2, 't', '', 1811365615815487488, '2024-08-16 10:52:26.745134', 1811365615815487488, '2024-08-16 10:52:26.746691', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824278055448219648, 1824277928167870464, 'pay_refund_status', 'partial_refund', '部分退款', 3, 't', '', 1811365615815487488, '2024-08-16 10:52:36.523227', 1811365615815487488, '2024-08-16 10:52:36.525303', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824278092886577152, 1824277928167870464, 'pay_refund_status', 'refunded', '全部退款', 4, 't', '', 1811365615815487488, '2024-08-16 10:52:45.44961', 1811365615815487488, '2024-08-16 10:52:45.451171', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285613860851712, 1824285303885008896, 'pay_method', 'wap', 'wap支付', 1, 't', '', 1811365615815487488, '2024-08-16 11:22:38.589166', 1811365615815487488, '2024-08-16 11:22:38.591274', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285656248487936, 1824285303885008896, 'pay_method', 'app', '应用支付', 2, 't', '', 1811365615815487488, '2024-08-16 11:22:48.695658', 1811365615815487488, '2024-08-16 11:22:48.696706', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285688691429376, 1824285303885008896, 'pay_method', 'web', 'web支付', 3, 't', '', 1811365615815487488, '2024-08-16 11:22:56.430664', 1811365615815487488, '2024-08-16 11:22:56.431781', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285724460453888, 1824285303885008896, 'pay_method', 'qrcode', '扫码支付', 4, 't', '', 1811365615815487488, '2024-08-16 11:23:04.958598', 1811365615815487488, '2024-08-16 11:23:04.95963', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285781570097152, 1824285303885008896, 'pay_method', 'barcode', '付款码', 5, 't', '', 1811365615815487488, '2024-08-16 11:23:18.575096', 1811365615815487488, '2024-08-16 11:23:18.576136', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824285824247140352, 1824285303885008896, 'pay_method', 'jsapi', '小程序支付', 6, 't', '', 1811365615815487488, '2024-08-16 11:23:28.74936', 1811365615815487488, '2024-08-16 11:23:28.750936', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050709768245248, 1824050661785407488, 'pay_status', 'progress', '支付中', 1, 't', '', 0, '2024-08-15 19:49:13.08964', 0, '2024-08-15 19:49:13.090167', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050788306587648, 1824050661785407488, 'pay_status', 'close', '支付关闭', 3, 't', '', 0, '2024-08-15 19:49:31.814754', 0, '2024-08-15 19:49:31.815791', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050865460809728, 1824050661785407488, 'pay_status', 'cancel', '支付撤销', 4, 't', '', 0, '2024-08-15 19:49:50.209986', 0, '2024-08-15 19:49:50.211016', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824287549255323648, 1824287281285435392, 'refund_status', 'close', '退款关闭', 3, 't', '', 1811365615815487488, '2024-08-16 11:30:20.023606', 1811365615815487488, '2024-08-16 11:30:20.025187', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050750083895296, 1824050661785407488, 'pay_status', 'success', '支付成功', 2, 't', '', 0, '2024-08-15 19:49:22.701746', 1811365615815487488, '2024-08-16 11:24:17.998592', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050900651020288, 1824050661785407488, 'pay_status', 'fail', '支付失败', 5, 't', '', 0, '2024-08-15 19:49:58.599133', 1811365615815487488, '2024-08-16 11:24:23.458659', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824050942044606464, 1824050661785407488, 'pay_status', 'timeout', '支付超时', 6, 't', '', 0, '2024-08-15 19:50:08.468537', 1811365615815487488, '2024-08-16 11:24:46.91709', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824277802141618176, 1824277760341184512, 'pay_alloc_status', 'waiting', '待分账', 0, 't', '', 1811365615815487488, '2024-08-16 10:51:36.130124', 1811365615815487488, '2024-08-16 10:51:36.132199', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824287582604234752, 1824287281285435392, 'refund_status', 'fail', '退款失败', 4, 't', '', 1811365615815487488, '2024-08-16 11:30:27.975006', 1811365615815487488, '2024-08-16 11:30:27.976048', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824277833028472832, 1824277760341184512, 'pay_alloc_status', 'allocation', '已分账', 1, 't', '', 1811365615815487488, '2024-08-16 10:51:43.494007', 1811365615815487488, '2024-08-16 10:51:43.49503', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824342766219014144, 1824342698652971008, 'trade_flow_type', 'pay', '支付', 1, 't', '', 1811365615815487488, '2024-08-16 15:09:44.773758', 1811365615815487488, '2024-08-16 15:09:44.775326', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824355204762988544, 1824342698652971008, 'trade_flow_type', 'refund', '退款', 2, 't', '', 1811365615815487488, '2024-08-16 15:59:10.355171', 1811365615815487488, '2024-08-16 15:59:10.357981', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824355244650819584, 1824342698652971008, 'trade_flow_type', 'transfer', '转账', 3, 't', '', 1811365615815487488, '2024-08-16 15:59:19.863146', 1811365615815487488, '2024-08-16 15:59:19.864715', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824287473090957312, 1824287281285435392, 'refund_status', 'progress', '退款中', 1, 't', '', 1811365615815487488, '2024-08-16 11:30:01.864717', 1811365615815487488, '2024-08-16 14:41:25.615219', 3, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824287508851593216, 1824287281285435392, 'refund_status', 'success', '退款成功', 2, 't', '', 1811365615815487488, '2024-08-16 11:30:10.390503', 1811365615815487488, '2024-08-16 14:42:12.062594', 2, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824791684567949312, 1824791500966486016, 'trade_type', 'pay', '支付', 1, 't', '', 1811365615815487488, '2024-08-17 20:53:35.248127', 1811365615815487488, '2024-08-17 20:53:35.250234', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824791718273376256, 1824791500966486016, 'trade_type', 'refund', '退款', 2, 't', '', 1811365615815487488, '2024-08-17 20:53:43.284373', 1811365615815487488, '2024-08-17 20:53:43.286467', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1824791757586587648, 1824791500966486016, 'trade_type', 'transfer', '转账', 3, 't', '', 1811365615815487488, '2024-08-17 20:53:52.657076', 1811365615815487488, '2024-08-17 20:53:52.658634', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825135534352588800, 1825134996013670400, 'transfer_status', 'progress', '转账中', 1, 't', '', 1811365615815487488, '2024-08-18 19:39:55.425814', 1811365615815487488, '2024-08-18 19:39:55.426857', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825135567118491648, 1825134996013670400, 'transfer_status', 'success', '转账成功', 2, 't', '', 1811365615815487488, '2024-08-18 19:40:03.237571', 1811365615815487488, '2024-08-18 19:40:03.23965', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825135606624641024, 1825134996013670400, 'transfer_status', 'close', '转账关闭', 3, 't', '', 1811365615815487488, '2024-08-18 19:40:12.656436', 1811365615815487488, '2024-08-18 19:40:12.658005', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825135653311438848, 1825134996013670400, 'transfer_status', 'fail', '转账失败', 4, 't', '', 1811365615815487488, '2024-08-18 19:40:23.787206', 1811365615815487488, '2024-08-18 19:40:23.788928', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825171620147658752, 1825135068277334016, 'transfer_payee_type', 'wx_personal', 'OpenId', 1, 't', '', 1811365615815487488, '2024-08-18 22:03:18.9509', 1811365615815487488, '2024-08-18 22:03:19.010194', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825171656562606080, 1825135068277334016, 'transfer_payee_type', 'ali_user_id', '用户ID', 2, 't', '', 1811365615815487488, '2024-08-18 22:03:27.631478', 1811365615815487488, '2024-08-18 22:03:27.633552', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825171693782859776, 1825135068277334016, 'transfer_payee_type', 'ali_open_id', 'OpenId', 3, 't', '', 1811365615815487488, '2024-08-18 22:03:36.505827', 1811365615815487488, '2024-08-18 22:03:36.507388', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825171779002728448, 1825135068277334016, 'transfer_payee_type', 'ali_login_name', '账号', 4, 't', '', 1811365615815487488, '2024-08-18 22:03:56.823151', 1811365615815487488, '2024-08-18 22:03:56.824188', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825174775472226304, 1825174702763966464, 'callback_status', 'success', '成功', 1, 't', '', 1811365615815487488, '2024-08-18 22:15:51.237972', 1811365615815487488, '2024-08-18 22:15:51.239535', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825174809592889344, 1825174702763966464, 'callback_status', 'fail', '失败', 2, 't', '', 1811365615815487488, '2024-08-18 22:15:59.372735', 1811365615815487488, '2024-08-18 22:15:59.374281', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825174854148980736, 1825174702763966464, 'callback_status', 'ignore', '忽略', 3, 't', '', 1811365615815487488, '2024-08-18 22:16:09.995547', 1811365615815487488, '2024-08-18 22:16:09.997194', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825174898629574656, 1825174702763966464, 'callback_status', 'exception', '异常', 4, 't', '', 1811365615815487488, '2024-08-18 22:16:20.600541', 1811365615815487488, '2024-08-18 22:16:20.602111', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825174939188494336, 1825174702763966464, 'callback_status', 'not_found', '未找到', 5, 't', '', 1811365615815487488, '2024-08-18 22:16:30.270155', 1811365615815487488, '2024-08-18 22:16:30.271194', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825349495736041472, 1823688893108133888, 'close_type', 'close', '关闭', 1, 't', '', 1811365615815487488, '2024-08-19 09:50:07.795169', 1811365615815487488, '2024-08-19 09:50:07.800044', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825349620168458240, 1823688893108133888, 'close_type', 'cancel', '撤销', 2, 't', '', 1811365615815487488, '2024-08-19 09:50:37.461827', 1811365615815487488, '2024-08-19 09:50:37.463384', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825408094978043904, 1825408039604842496, 'notice_send_type', 'auto', '自动发送', 0, 't', '', 1811365615815487488, '2024-08-19 13:42:58.942888', 1811365615815487488, '2024-08-19 13:42:58.944443', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1825408129895624704, 1825408039604842496, 'notice_send_type', 'manual', '手动发送', 1, 't', '', 1811365615815487488, '2024-08-19 13:43:07.267986', 1811365615815487488, '2024-08-19 13:43:07.269622', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826059984439037952, 1826059944274382848, 'reconcile_discrepancy_type', 'consistent', '一致', 1, 't', '', 1811365615815487488, '2024-08-21 08:53:21.501721', 1811365615815487488, '2024-08-21 08:53:21.503286', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826060020057067520, 1826059944274382848, 'reconcile_discrepancy_type', 'local_not_exists', '本地订单不存在', 2, 't', '', 1811365615815487488, '2024-08-21 08:53:29.993418', 1811365615815487488, '2024-08-21 08:53:29.995625', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826060059160563712, 1826059944274382848, 'reconcile_discrepancy_type', 'remote_not_exists', '远程订单不存在', 3, 't', '', 1811365615815487488, '2024-08-21 08:53:39.317457', 1811365615815487488, '2024-08-21 08:53:39.319039', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826061121699401728, 1826061072412135424, 'reconcile_result', 'consistent', '一致', 1, 't', '', 1811365615815487488, '2024-08-21 08:57:52.645403', 1811365615815487488, '2024-08-21 08:57:52.647474', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826061151613177856, 1826061072412135424, 'reconcile_result', 'inconsistent', '不一致', 0, 't', '', 1811365615815487488, '2024-08-21 08:57:59.777037', 1811365615815487488, '2024-08-21 08:57:59.779755', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826143984713056256, 1826143914542350336, 'trade_status', 'success', '成功', 1, 't', '', 1811365615815487488, '2024-08-21 14:27:08.727788', 1811365615815487488, '2024-08-21 14:27:08.730433', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826144018259099648, 1826143914542350336, 'trade_status', 'fail', '失败', 2, 't', '', 1811365615815487488, '2024-08-21 14:27:16.725384', 1811365615815487488, '2024-08-21 14:27:16.728032', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826144059224866816, 1826143914542350336, 'trade_status', 'closed', '关闭', 3, 't', '', 1811365615815487488, '2024-08-21 14:27:26.492306', 1811365615815487488, '2024-08-21 14:27:26.494438', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826144095107137536, 1826143914542350336, 'trade_status', 'revoked', '撤销', 4, 't', '', 1811365615815487488, '2024-08-21 14:27:35.047073', 1811365615815487488, '2024-08-21 14:27:35.049164', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826060094858285056, 1826059944274382848, 'reconcile_discrepancy_type', 'not_match', '订单信息不一致', 4, 't', '', 1811365615815487488, '2024-08-21 08:53:47.827207', 1811365615815487488, '2024-08-21 15:04:30.959401', 1, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826521714671509504, 1826143914542350336, 'trade_status', 'progress', '执行中', 0, 't', '', 1811365615815487488, '2024-08-22 15:28:06.569615', 1811365615815487488, '2024-08-22 15:28:06.575032', 0, 'f');
INSERT INTO "public"."base_dict_item" VALUES (1826523501021388800, 1826143914542350336, 'trade_status', 'exception', '异常', 5, 't', '', 1811365615815487488, '2024-08-22 15:35:12.466889', 1811365615815487488, '2024-08-22 15:35:12.469054', 0, 'f');

-- ----------------------------
-- Table structure for base_param
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_param";
CREATE TABLE "public"."base_param" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "key" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(20) COLLATE "pg_catalog"."default",
  "enable" bool NOT NULL,
  "internal" bool NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."base_param"."id" IS '主键';
COMMENT ON COLUMN "public"."base_param"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_param"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_param"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_param"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_param"."version" IS '版本号';
COMMENT ON COLUMN "public"."base_param"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."base_param"."name" IS '参数名称';
COMMENT ON COLUMN "public"."base_param"."key" IS '参数键名';
COMMENT ON COLUMN "public"."base_param"."value" IS '参数值';
COMMENT ON COLUMN "public"."base_param"."type" IS '参数类型';
COMMENT ON COLUMN "public"."base_param"."enable" IS '启用状态';
COMMENT ON COLUMN "public"."base_param"."internal" IS '内置参数';
COMMENT ON COLUMN "public"."base_param"."remark" IS '备注';
COMMENT ON TABLE "public"."base_param" IS '系统参数';

-- ----------------------------
-- Records of base_param
-- ----------------------------
INSERT INTO "public"."base_param" VALUES (1811338448851234812, 0, '2024-07-11 17:55:13.904818', 0, '2024-07-11 17:55:13.904818', 0, 'f', 'cs', 'cs', 'cs', NULL, 't', 'f', '124123123123123');

-- ----------------------------
-- Table structure for iam_client
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_client";
CREATE TABLE "public"."iam_client" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "internal" bool NOT NULL,
  "remark" varchar(250) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_client"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_client"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_client"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_client"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_client"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_client"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_client"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_client"."code" IS '编码';
COMMENT ON COLUMN "public"."iam_client"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_client"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_client"."remark" IS '备注';
COMMENT ON TABLE "public"."iam_client" IS '认证终端';

-- ----------------------------
-- Records of iam_client
-- ----------------------------
INSERT INTO "public"."iam_client" VALUES (1810614511481892864, 0, '2024-07-09 17:58:33.786446', 0, '2024-07-09 17:58:33.788447', 0, 'f', 'dax-pay-admin', '运营管理端', 't', NULL);
INSERT INTO "public"."iam_client" VALUES (1826818975183966208, 1811365615815487488, '2024-08-23 11:09:18.99642', 1811365615815487488, '2024-08-23 11:09:18.999084', 0, 'f', 'dax-pay-merchant', '商户端', 't', NULL);

-- ----------------------------
-- Table structure for iam_perm_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_code";
CREATE TABLE "public"."iam_perm_code" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "remark" varchar(300) COLLATE "pg_catalog"."default",
  "leaf" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_perm_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_perm_code"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_code"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_code"."pid" IS '父ID';
COMMENT ON COLUMN "public"."iam_perm_code"."code" IS '权限码';
COMMENT ON COLUMN "public"."iam_perm_code"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_perm_code"."remark" IS '备注';
COMMENT ON COLUMN "public"."iam_perm_code"."leaf" IS '是否为叶子结点';
COMMENT ON TABLE "public"."iam_perm_code" IS '权限码';

-- ----------------------------
-- Records of iam_perm_code
-- ----------------------------

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_menu";
CREATE TABLE "public"."iam_perm_menu" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "client_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "icon" varchar(100) COLLATE "pg_catalog"."default",
  "hidden" bool NOT NULL,
  "hide_children_menu" bool NOT NULL,
  "component" varchar(300) COLLATE "pg_catalog"."default",
  "path" varchar(300) COLLATE "pg_catalog"."default",
  "redirect" varchar(300) COLLATE "pg_catalog"."default",
  "sort_no" float4,
  "root" bool NOT NULL,
  "keep_alive" bool,
  "target_outside" bool,
  "full_screen" bool,
  "remark" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_perm_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_menu"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_menu"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_menu"."pid" IS '父id';
COMMENT ON COLUMN "public"."iam_perm_menu"."client_code" IS '关联终端code';
COMMENT ON COLUMN "public"."iam_perm_menu"."title" IS '菜单标题';
COMMENT ON COLUMN "public"."iam_perm_menu"."name" IS '路由名称';
COMMENT ON COLUMN "public"."iam_perm_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."hidden" IS '是否隐藏';
COMMENT ON COLUMN "public"."iam_perm_menu"."hide_children_menu" IS '是否隐藏子菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."iam_perm_menu"."path" IS '访问路径';
COMMENT ON COLUMN "public"."iam_perm_menu"."redirect" IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN "public"."iam_perm_menu"."sort_no" IS '菜单排序';
COMMENT ON COLUMN "public"."iam_perm_menu"."root" IS '是否是一级菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."keep_alive" IS '是否缓存页面';
COMMENT ON COLUMN "public"."iam_perm_menu"."target_outside" IS '是否为外部打开';
COMMENT ON COLUMN "public"."iam_perm_menu"."full_screen" IS '是否全屏打开';
COMMENT ON COLUMN "public"."iam_perm_menu"."remark" IS '描述';
COMMENT ON TABLE "public"."iam_perm_menu" IS '菜单权限';

-- ----------------------------
-- Records of iam_perm_menu
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" VALUES (1810909511121862656, 0, '2024-07-10 13:30:47.186592', 0, '2024-07-10 13:48:12.256695', 6, 'f', 1810864706127790080, 'dax-pay-admin', '权限管理', 'Permission', '', 'f', 'f', 'Layout', '/system/permission', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810914501286580224, 0, '2024-07-10 13:50:36.93111', 0, '2024-07-10 13:50:36.93111', 0, 'f', 1810909511121862656, 'dax-pay-admin', '请求权限', 'PermPathList', '', 'f', 'f', 'iam/perm/path/PermPathList', '/system/permission/path', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810915172140339200, 0, '2024-07-10 13:53:16.872895', 0, '2024-07-10 13:53:53.635198', 1, 'f', 1810909511121862656, 'dax-pay-admin', '权限码', 'PermCodeList', '', 'f', 'f', 'iam/perm/code/PermCodeList', '/system/permission/code', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811336128809766912, 0, '2024-07-11 17:46:00.76599', 0, '2024-07-11 17:46:36.625626', 1, 'f', 1810910433264762880, 'dax-pay-admin', '系统参数', 'SystemParamList', '', 'f', 'f', '/baseapi/param/SystemParamList', '/system/config/param', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811283778967441408, 0, '2024-07-11 14:17:59.588051', 0, '2024-07-11 17:47:27.793518', 1, 'f', 1810910433264762880, 'dax-pay-admin', '终端管理', 'ClientList', '', 'f', 'f', '/iam/client/ClientList', '/system/config/client', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811283512855629824, 0, '2024-07-11 14:16:56.144236', 0, '2024-07-11 17:47:35.591847', 1, 'f', 1810910433264762880, 'dax-pay-admin', '数据字典', 'DictList', '', 'f', 'f', '/baseapi/dict/DictList', '/system/config/dict', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811360392644521984, 0, '2024-07-11 19:22:25.715575', 0, '2024-07-11 19:22:25.718122', 0, 'f', 1810909853691641856, 'dax-pay-admin', '用户管理', 'UserList', '', 'f', 'f', '/iam/user/UserList', '/system/user/info', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811667984159715328, 1811365615815487488, '2024-07-12 15:44:41.247497', 1811365615815487488, '2024-07-12 15:44:41.252052', 0, 'f', 1810864706127790080, 'dax-pay-admin', '审计日志', 'SystemLog', '', 'f', 'f', 'Layout', '/system/log', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811671741266243584, 1811365615815487488, '2024-07-12 15:59:37.00905', 1811365615815487488, '2024-07-12 15:59:37.011126', 0, 'f', 1811667984159715328, 'dax-pay-admin', '登录日志', 'LoginLogList', '', 'f', 'f', '/baseapi/log/login/LoginLogList', '/system/log/login', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1811672495767007232, 1811365615815487488, '2024-07-12 16:02:36.895766', 1811365615815487488, '2024-07-12 16:35:41.627109', 1, 'f', 1811667984159715328, 'dax-pay-admin', '操作日志', 'OperateLogList', '', 'f', 'f', '/baseapi/log/operate/OperateLogList', '/system/log/operate', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810871795650891776, 0, '2024-07-10 11:00:55.113616', 0, '2024-07-10 11:13:07.675214', 7, 'f', 1810864706127790080, 'dax-pay-admin', '菜单管理', 'MenuList', '', 'f', 'f', '/iam/perm/menu/MenuList', '/system/menu', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810909853691641856, 0, '2024-07-10 13:32:08.855417', 0, '2024-07-10 13:35:45.759284', 1, 'f', 1810864706127790080, 'dax-pay-admin', '用户信息', 'UserAuth', '', 'f', 'f', 'Layout', '/system/user', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810910433264762880, 0, '2024-07-10 13:34:27.036666', 0, '2024-07-10 13:35:56.758501', 1, 'f', 1810864706127790080, 'dax-pay-admin', '系统配置', 'SystemConfig', '', 'f', 'f', 'Layout', '/system/config', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810911232921423872, 0, '2024-07-10 13:37:37.692104', 0, '2024-07-10 13:37:37.693108', 0, 'f', 1810909511121862656, 'dax-pay-admin', '角色管理', 'RoleList', '', 'f', 'f', 'iam/role/RoleList.vue', '/system/permission/role', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810878374534152192, 0, '2024-07-10 11:27:03.641433', 1811365615815487488, '2024-07-13 21:16:24.212477', 5, 't', NULL, 'dax-pay-admin', '测试Iframe', 'Iframe', '', 't', 'f', 'Iframe', '/Iframe', 'https://www.antdv.com/components/overview-cn', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812113986291822592, 1811365615815487488, '2024-07-13 21:16:56.438162', 1811365615815487488, '2024-07-13 21:17:06.34038', 0, 't', NULL, 'dax-pay-admin', '系统管理', 'System', 'ant-design:setting-outlined', 'f', 'f', 'Layout', '/system', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1810864706127790080, 0, '2024-07-10 10:32:44.838967', 1811365615815487488, '2024-07-13 21:17:09.975339', 5, 'f', NULL, 'dax-pay-admin', '系统管理', 'System', 'ant-design:setting-outlined', 'f', 'f', 'Layout', '/system', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812114224679284736, 1811365615815487488, '2024-07-13 21:17:53.274452', 1811365615815487488, '2024-07-13 21:18:00.886679', 1, 'f', NULL, 'dax-pay-admin', '订单管理', 'PayOrder', 'ant-design:wallet-outlined', 'f', 'f', 'Layout', '/pay/order', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812114959231938560, 1811365615815487488, '2024-07-13 21:20:48.405236', 1811365615815487488, '2024-07-13 21:20:48.407298', 0, 'f', NULL, 'dax-pay-admin', '对账管理', 'Reconcile', 'ant-design:arrows-alt-outlined', 'f', 'f', 'Layout', '/pay/reconcile', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812115247342874624, 1811365615815487488, '2024-07-13 21:21:57.096469', 1811365615815487488, '2024-07-13 21:25:06.069746', 4, 'f', NULL, 'dax-pay-admin', '关于', '', 'ant-design:info-circle-outlined', 'f', 'f', 'Layout', '/about', '/about/index', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812113823376666624, 1811365615815487488, '2024-07-13 21:16:17.597391', 1811365615815487488, '2024-07-13 21:27:13.912267', 2, 'f', NULL, 'dax-pay-admin', '支付配置', 'PayConfig', 'ant-design:property-safety-twotone', 'f', 'f', 'Layout', '/pay/config', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812116972585938944, 1811365615815487488, '2024-07-13 21:28:48.426928', 1811365615815487488, '2024-07-13 21:28:48.429631', 0, 'f', 1812113823376666624, 'dax-pay-admin', '商户管理', '', '', 'f', 'f', 'Layout', '/pay/config/merchant', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812117366246535168, 1811365615815487488, '2024-07-13 21:30:22.282442', 1811365615815487488, '2024-07-13 21:30:22.284525', 0, 'f', 1812116972585938944, 'dax-pay-admin', '商户信息', 'MerchantList', '', 'f', 'f', '/daxpay/admin/merchant/info/MerchantList', '/pay/config/merchant/info', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812116779807338496, 1811365615815487488, '2024-07-13 21:28:02.464593', 1811365615815487488, '2024-07-17 14:13:02.263915', 1, 'f', 1812113823376666624, 'dax-pay-admin', '基础数据', '', '', 'f', 'f', 'Layout', '/pay/config/base', '', -2, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812114347454951424, 1811365615815487488, '2024-07-13 21:18:22.547038', 1811365615815487488, '2024-07-24 18:53:38.947244', 2, 'f', NULL, 'dax-pay-admin', '交易记录', 'PayRecord', 'ant-design:container-outlined', 'f', 'f', 'Layout', '/pay/record', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812114603047448576, 1811365615815487488, '2024-07-13 21:19:23.484406', 1811365615815487488, '2024-07-24 19:28:09.132339', 1, 'f', NULL, 'dax-pay-admin', '商户通知', 'PayNotic', 'ant-design:notification-twotone', 'f', 'f', 'Layout', '/pay/notice', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1813782811271446528, 1811365615815487488, '2024-07-18 11:48:15.321777', 1811365615815487488, '2024-08-14 19:30:02.876775', 1, 'f', 1813456708833087488, 'dax-pay-admin', '平台配置', 'PlatformConfig', '', 'f', 'f', '/daxpay/admin/config/platform/PlatformConfig', '/pay/config/basic/platfom', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812114725651148800, 1811365615815487488, '2024-07-13 21:19:52.715548', 1811365615815487488, '2024-08-14 19:30:12.386601', 1, 'f', NULL, 'dax-pay-admin', '分账管理', 'Allocation', 'ant-design:sliders-twotone', 't', 'f', 'Layout', '/pay/allocation', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812118417951485952, 1811365615815487488, '2024-07-13 21:34:33.029042', 1811365615815487488, '2024-08-27 16:13:38.870687', 2, 'f', 1812116779807338496, 'dax-pay-admin', '支付方式', 'MethodConstList', '', 'f', 'f', '/daxpay/common/constant/method/MethodConstList', '/pay/config/base/method', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812118635229016064, 1811365615815487488, '2024-07-13 21:35:24.831252', 1811365615815487488, '2024-08-27 16:13:42.461047', 2, 'f', 1812116779807338496, 'dax-pay-admin', '接口信息', 'ApiConstList', '', 'f', 'f', '/daxpay/common/constant/api/ApiConstList', '/pay/config/base/api', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1820432075829538816, 1811365615815487488, '2024-08-05 20:10:03.528744', 1811365615815487488, '2024-08-27 16:13:47.256843', 2, 'f', 1812116779807338496, 'dax-pay-admin', '订阅通知', 'NotifyConstList', '', 'f', 'f', '/daxpay/common/constant/notify/NotifyConstList', '/pay/config/base/notify', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812117498400665600, 1811365615815487488, '2024-07-13 21:30:53.790933', 1811365615815487488, '2024-08-27 19:10:41.603861', 1, 'f', 1812116972585938944, 'dax-pay-admin', '应用信息', 'MchAppList', '', 'f', 'f', '/daxpay/common/merchant/app/MchAppList', '/pay/config/merchant/app', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1814602527895957504, 1811365615815487488, '2024-07-20 18:05:30.994059', 1811365615815487488, '2024-08-27 19:10:46.604373', 2, 'f', 1812114224679284736, 'dax-pay-admin', '支付订单', 'PayOrderList', '', 'f', 'f', '/daxpay/common/order/pay/PayOrderList', '/pay/order/pay', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1814602684720984064, 1811365615815487488, '2024-07-20 18:06:08.382006', 1811365615815487488, '2024-08-27 19:10:52.224647', 2, 'f', 1812114224679284736, 'dax-pay-admin', '退款订单', 'RefundOrderList', '', 'f', 'f', '/daxpay/common/order/refund/RefundOrderList', '/pay/order/refund', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1814602840971390976, 1811365615815487488, '2024-07-20 18:06:45.63523', 1811365615815487488, '2024-08-27 19:11:12.990464', 2, 'f', 1812114224679284736, 'dax-pay-admin', '转账订单', 'TransferOrderList', '', 'f', 'f', '/daxpay/common/order/transfer/TransferOrderList', '/pay/order/transfer', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1816064154722365440, 1811365615815487488, '2024-07-24 18:53:29.993277', 1811365615815487488, '2024-08-27 19:11:37.433279', 5, 'f', 1812114347454951424, 'dax-pay-admin', '回调记录', 'TradeCallbackRecordList', '', 'f', 'f', '/daxpay/common/record/callback/TradeCallbackRecordList', '/pay/record/callback', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1816064344661422080, 1811365615815487488, '2024-07-24 18:54:15.268228', 1811365615815487488, '2024-08-27 19:11:43.063961', 2, 'f', 1812114347454951424, 'dax-pay-admin', '交易流水', 'TradeFlowRecordList', '', 'f', 'f', '/daxpay/common/record/flow/TradeFlowRecordList', '/pay/record/flow', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1820467440233127936, 1811365615815487488, '2024-08-05 22:30:35.05958', 1811365615815487488, '2024-08-27 19:12:08.408597', 2, 'f', 1812114603047448576, 'dax-pay-admin', '订阅消息', 'NotifyTaskList', '', 'f', 'f', '/daxpay/common/notice/notify/NotifyTaskList', '/pay/notice/notify', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1820467523628474368, 1811365615815487488, '2024-08-05 22:30:54.941602', 1811365615815487488, '2024-08-27 19:12:15.437882', 2, 'f', 1812114603047448576, 'dax-pay-admin', '回调消息', 'CallbackTaskList', '', 'f', 'f', '/daxpay/common/notice/callback/CallbackTaskList', '/pay/notice/callback', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1813456708833087488, 1811365615815487488, '2024-07-17 14:12:26.440633', 1811365615815487488, '2024-08-29 17:55:57.775029', 3, 'f', 1812113823376666624, 'dax-pay-admin', '基础配置', '', '', 't', 'f', 'Layout', '/pay/config/basic', '', -1, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812115119471128576, 1811365615815487488, '2024-07-13 21:21:26.609834', 1811365615815487488, '2024-08-27 19:22:59.848468', 2, 'f', NULL, 'dax-pay-admin', '演示模块', '', 'ant-design:appstore-twotone', 'f', 'f', 'Layout', '/pay/demo', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1822975425229004800, 1811365615815487488, '2024-08-12 20:36:25.279489', 1811365615815487488, '2024-08-12 20:36:25.282711', 0, 'f', 1810864706127790080, 'dax-pay-admin', '文件存储', '', '', 'f', 'f', 'Layout', '/system/file', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1822975777026252800, 1811365615815487488, '2024-08-12 20:37:49.152472', 1811365615815487488, '2024-08-12 20:37:49.154031', 0, 'f', 1822975425229004800, 'dax-pay-admin', '存储平台', 'FilePlatformList', '', 'f', 'f', '/baseapi/file/platform/FilePlatformList', '/system/file/platform', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1822975970266226688, 1811365615815487488, '2024-08-12 20:38:35.224183', 1811365615815487488, '2024-08-12 20:38:35.226288', 0, 'f', 1822975425229004800, 'dax-pay-admin', '文件管理', 'FileUploadList', '', 'f', 'f', '/baseapi/file/upload/FileUploadList', '/system/file/info', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1824779660136001536, 1811365615815487488, '2024-08-17 20:05:48.400494', 1811365615815487488, '2024-08-17 20:05:48.402062', 0, 'f', 1812114725651148800, 'dax-pay-admin', '分账接收者', '', '', 'f', 'f', '', '/pay/allocation', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1824779695900831744, 1811365615815487488, '2024-08-17 20:05:56.927619', 1811365615815487488, '2024-08-17 20:05:56.928667', 0, 'f', 1812114725651148800, 'dax-pay-admin', '分账组', '', '', 'f', 'f', '', '/pay/allocation', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1824779750208679936, 1811365615815487488, '2024-08-17 20:06:09.875297', 1811365615815487488, '2024-08-17 21:01:05.38493', 1, 'f', 1812114725651148800, 'dax-pay-admin', '分账单', '', '', 'f', 'f', '', '/pay/reconcile', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828321321655308288, 1811365615815487488, '2024-08-27 14:39:06.307149', 1811365615815487488, '2024-08-27 14:39:06.309784', 0, 'f', NULL, 'dax-pay-merchant', '系统管理', 'System', 'ant-design:setting-outlined', 'f', 'f', 'Layout', '/system', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828329559561965568, 1811365615815487488, '2024-08-27 15:11:50.377235', 1811365615815487488, '2024-08-27 15:11:50.378788', 0, 'f', NULL, 'dax-pay-merchant', '支付配置', 'PayConfig', 'ant-design:property-safety-twotone', 'f', 'f', 'Layout', '/pay/config', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828329734523162624, 1811365615815487488, '2024-08-27 15:12:32.091286', 1811365615815487488, '2024-08-27 15:12:32.093374', 0, 'f', NULL, 'dax-pay-merchant', '订单管理', 'PayOrder', 'ant-design:wallet-outlined', 'f', 'f', 'Layout', '/pay/order', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828329876961726464, 1811365615815487488, '2024-08-27 15:13:06.051401', 1811365615815487488, '2024-08-27 15:13:06.053461', 0, 'f', NULL, 'dax-pay-merchant', '交易记录', 'PayRecord', 'ant-design:container-outlined', 'f', 'f', 'Layout', '/pay/record', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828330011066208256, 1811365615815487488, '2024-08-27 15:13:38.024196', 1811365615815487488, '2024-08-27 15:13:38.026907', 0, 'f', NULL, 'dax-pay-merchant', '商户通知', 'PayNotic', 'ant-design:notification-twotone', 'f', 'f', 'Layout', '/pay/notice', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828330460724957184, 1811365615815487488, '2024-08-27 15:15:25.232433', 1811365615815487488, '2024-08-27 15:15:25.234529', 0, 'f', NULL, 'dax-pay-merchant', '分账管理', 'Allocation', 'ant-design:sliders-twotone', 't', 'f', 'Layout', '/pay/allocation', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828341845420773376, 1811365615815487488, '2024-08-27 16:00:39.554398', 1811365615815487488, '2024-08-27 16:00:39.557168', 0, 'f', 1828321321655308288, 'dax-pay-merchant', '请求权限', 'PermPathList', '', 'f', 'f', 'iam/perm/path/PermPathList', '/system/path', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828329089900580864, 1811365615815487488, '2024-08-27 15:09:58.401939', 1811365615815487488, '2024-08-27 16:01:05.442085', 1, 'f', 1828321321655308288, 'dax-pay-merchant', '角色管理', 'RoleList', '', 'f', 'f', 'iam/role/RoleList.vue', '/system/role', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828329206313488384, 1811365615815487488, '2024-08-27 15:10:26.156072', 1811365615815487488, '2024-08-27 16:01:43.282754', 1, 'f', 1828321321655308288, 'dax-pay-merchant', '用户管理', 'MerchantUserList', '', 'f', 'f', 'daxpay/merchant/user/MerchantUserList', '/system/user', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828343958704066560, 1811365615815487488, '2024-08-27 16:09:03.400397', 1811365615815487488, '2024-08-27 16:09:03.402746', 0, 'f', 1828329559561965568, 'dax-pay-merchant', '基础数据', '', '', 'f', 'f', 'Layout', '/pay/config/base', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828344228972433408, 1811365615815487488, '2024-08-27 16:10:07.837377', 1811365615815487488, '2024-08-27 16:10:07.839474', 0, 'f', 1828329559561965568, 'dax-pay-merchant', '商户管理', '', '', 'f', 'f', 'Layout', '/pay/config/merchant', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1812118306328473600, 1811365615815487488, '2024-07-13 21:34:06.415311', 1811365615815487488, '2024-08-27 16:13:34.678515', 4, 'f', 1812116779807338496, 'dax-pay-admin', '支付通道', 'ChannelConstList', '', 'f', 'f', '/daxpay/common/constant/channel/ChannelConstList', '/pay/config/base/channel', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828345367428173824, 1811365615815487488, '2024-08-27 16:14:39.266091', 1811365615815487488, '2024-08-27 16:14:39.268192', 0, 'f', 1828343958704066560, 'dax-pay-merchant', '支付通道', 'ChannelConstList', '', 'f', 'f', '/daxpay/common/constant/channel/ChannelConstList', '/pay/config/base/channel', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828345482972860416, 1811365615815487488, '2024-08-27 16:15:06.814769', 1811365615815487488, '2024-08-27 16:15:06.816427', 0, 'f', 1828343958704066560, 'dax-pay-merchant', '支付方式', 'MethodConstList', '', 'f', 'f', '/daxpay/common/constant/method/MethodConstList', '/pay/config/base/method', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828345668885385216, 1811365615815487488, '2024-08-27 16:15:51.139215', 1811365615815487488, '2024-08-27 16:15:51.141516', 0, 'f', 1828343958704066560, 'dax-pay-merchant', '接口信息', 'ApiConstList', '', 'f', 'f', '/daxpay/common/constant/api/ApiConstList', '/pay/config/base/api', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828345770836332544, 1811365615815487488, '2024-08-27 16:16:15.446234', 1811365615815487488, '2024-08-27 16:16:15.448516', 0, 'f', 1828343958704066560, 'dax-pay-merchant', '订阅通知', 'NotifyConstList', '', 'f', 'f', '/daxpay/common/constant/notify/NotifyConstList', '/pay/config/base/notify', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1816064593371066368, 1811365615815487488, '2024-07-24 18:55:14.565671', 1811365615815487488, '2024-08-27 19:11:47.317719', 4, 'f', 1812114347454951424, 'dax-pay-admin', '关闭记录(支付)', 'PayCloseRecordList', '', 'f', 'f', '/daxpay/common/record/close/PayCloseRecordList', '/pay/record/close', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1824779945365450752, 1811365615815487488, '2024-08-17 20:06:56.404481', 1811365615815487488, '2024-08-27 19:11:51.292144', 2, 'f', 1812114347454951424, 'dax-pay-admin', '同步记录', 'TradeSyncRecordList', '', 'f', 'f', '/daxpay/common/record/sync/TradeSyncRecordList', '/pay/record/sync', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1820468413097746432, 1811365615815487488, '2024-08-05 22:34:27.007057', 1811365615815487488, '2024-08-27 19:12:34.654805', 2, 'f', 1812114959231938560, 'dax-pay-admin', '差异记录', 'ReconcileDiscrepancyList', '', 'f', 'f', '/daxpay/common/reconcile/discrepancy/ReconcileDiscrepancyList', '/pay/reconcile/discrepancy', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1820467934552825856, 1811365615815487488, '2024-08-05 22:32:32.913035', 1811365615815487488, '2024-08-27 19:12:40.133331', 2, 'f', 1812114959231938560, 'dax-pay-admin', '对账单', 'ReconcileStatementList', '', 'f', 'f', '/daxpay/common/reconcile/statement/ReconcileStatementList', '/pay/reconcile/statement', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391048989757440, 1811365615815487488, '2024-08-27 19:16:10.599848', 1811365615815487488, '2024-08-27 19:16:10.602041', 0, 'f', 1828344228972433408, 'dax-pay-merchant', '应用信息', 'MchAppList', '', 'f', 'f', '/daxpay/common/merchant/app/MchAppList', '/pay/config/merchant/app', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391238714904576, 1811365615815487488, '2024-08-27 19:16:55.833768', 1811365615815487488, '2024-08-27 19:16:55.835858', 0, 'f', 1828329734523162624, 'dax-pay-merchant', '支付订单', 'PayOrderList', '', 'f', 'f', '/daxpay/common/order/pay/PayOrderList', '/pay/order/pay', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391326023536640, 1811365615815487488, '2024-08-27 19:17:16.649616', 1811365615815487488, '2024-08-27 19:17:16.652853', 0, 'f', 1828329734523162624, 'dax-pay-merchant', '退款订单', 'RefundOrderList', '', 'f', 'f', '/daxpay/common/order/refund/RefundOrderList', '/pay/order/refund', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391418604408832, 1811365615815487488, '2024-08-27 19:17:38.723375', 1811365615815487488, '2024-08-27 19:17:38.726373', 0, 'f', 1828329734523162624, 'dax-pay-merchant', '转账订单', 'TransferOrderList', '', 'f', 'f', '/daxpay/common/order/transfer/TransferOrderList', '/pay/order/transfer', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391587492253696, 1811365615815487488, '2024-08-27 19:18:18.988861', 1811365615815487488, '2024-08-27 19:18:18.990966', 0, 'f', 1828329876961726464, 'dax-pay-merchant', '回调记录', 'TradeCallbackRecordList', '', 'f', 'f', '/daxpay/common/record/callback/TradeCallbackRecordList', '/pay/record/callback', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391677497823232, 1811365615815487488, '2024-08-27 19:18:40.447225', 1811365615815487488, '2024-08-27 19:18:40.449864', 0, 'f', 1828329876961726464, 'dax-pay-merchant', '交易流水', 'TradeFlowRecordList', '', 'f', 'f', '/daxpay/common/record/flow/TradeFlowRecordList', '/pay/record/flow', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391753905459200, 1811365615815487488, '2024-08-27 19:18:58.664532', 1811365615815487488, '2024-08-27 19:18:58.666641', 0, 'f', 1828329876961726464, 'dax-pay-merchant', '关闭记录(支付)', 'PayCloseRecordList', '', 'f', 'f', '/daxpay/common/record/close/PayCloseRecordList', '/pay/record/close', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828391841390252032, 1811365615815487488, '2024-08-27 19:19:19.522428', 1811365615815487488, '2024-08-27 19:19:19.525582', 0, 'f', 1828329876961726464, 'dax-pay-merchant', '同步记录', 'TradeSyncRecordList', '', 'f', 'f', '/daxpay/common/record/sync/TradeSyncRecordList', '/pay/record/sync', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828392143598243840, 1811365615815487488, '2024-08-27 19:20:31.574606', 1811365615815487488, '2024-08-27 19:20:31.576698', 0, 'f', 1828330011066208256, 'dax-pay-merchant', '订阅消息', 'NotifyTaskList', '', 'f', 'f', '/daxpay/common/notice/notify/NotifyTaskList', '/pay/notice/notify', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828392258849329152, 1811365615815487488, '2024-08-27 19:20:59.052938', 1811365615815487488, '2024-08-27 19:21:08.423053', 1, 'f', 1828330011066208256, 'dax-pay-merchant', '回调消息', 'CallbackTaskList', '', 'f', 'f', '/daxpay/common/notice/callback/CallbackTaskList', '/pay/notice/callback', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828392553994113024, 1811365615815487488, '2024-08-27 19:22:09.420506', 1811365615815487488, '2024-08-27 19:22:09.423962', 0, 'f', NULL, 'dax-pay-merchant', '对账管理', 'Reconcile', 'ant-design:arrows-alt-outlined', 'f', 'f', 'Layout', '/pay/reconcile', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828392717093818368, 1811365615815487488, '2024-08-27 19:22:48.306499', 1811365615815487488, '2024-08-27 19:23:15.400818', 1, 'f', NULL, 'dax-pay-merchant', '演示模块', 'Demo', 'ant-design:appstore-twotone', 'f', 'f', 'Layout', '/pay/demo', '', 0, 't', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828393047642722304, 1811365615815487488, '2024-08-27 19:24:07.115223', 1811365615815487488, '2024-08-27 19:24:07.117321', 0, 'f', 1828392553994113024, 'dax-pay-merchant', '对账单', 'ReconcileStatementList', '', 'f', 'f', '/daxpay/common/reconcile/statement/ReconcileStatementList', '/pay/reconcile/statement', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828393120791384064, 1811365615815487488, '2024-08-27 19:24:24.555402', 1811365615815487488, '2024-08-27 19:24:24.557034', 0, 'f', 1828392553994113024, 'dax-pay-merchant', '差异记录', 'ReconcileDiscrepancyList', '', 'f', 'f', '/daxpay/common/reconcile/discrepancy/ReconcileDiscrepancyList', '/pay/reconcile/discrepancy', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828393198436339712, 1811365615815487488, '2024-08-27 19:24:43.06786', 1811365615815487488, '2024-08-27 19:24:43.071355', 0, 'f', 1828330460724957184, 'dax-pay-merchant', '分账接收者', '', '', 'f', 'f', '', '/pay/allocation', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828393240358408192, 1811365615815487488, '2024-08-27 19:24:53.06244', 1811365615815487488, '2024-08-27 19:24:53.064026', 0, 'f', 1828330460724957184, 'dax-pay-merchant', '分账组', '', '', 'f', 'f', '', '/pay/allocation', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828393290560032768, 1811365615815487488, '2024-08-27 19:25:05.031618', 1811365615815487488, '2024-08-27 19:25:05.033737', 0, 'f', 1828330460724957184, 'dax-pay-merchant', '分账单', '', '', 'f', 'f', '', '/pay/allocation', '', 0, 'f', 't', 'f', 'f', NULL);
INSERT INTO "public"."iam_perm_menu" VALUES (1828390931998035968, 1811365615815487488, '2024-08-27 19:15:42.709956', 1811365615815487488, '2024-08-29 10:51:18.205731', 1, 'f', 1828344228972433408, 'dax-pay-merchant', '商户信息', 'MerchantInfo', '', 'f', 'f', '/daxpay/merchant/info/MerchantInfo', '/pay/config/merchant/info', '', 0, 'f', 't', 'f', 'f', NULL);

-- ----------------------------
-- Table structure for iam_perm_path
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_path";
CREATE TABLE "public"."iam_perm_path" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "parent_code" varchar(50) COLLATE "pg_catalog"."default",
  "client_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "leaf" bool NOT NULL,
  "path" varchar(200) COLLATE "pg_catalog"."default",
  "method" varchar(10) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_perm_path"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_path"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_path"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_path"."parent_code" IS '上级编码';
COMMENT ON COLUMN "public"."iam_perm_path"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_perm_path"."code" IS '标识编码(模块、分组标识)';
COMMENT ON COLUMN "public"."iam_perm_path"."name" IS '名称(请求路径、模块、分组名称)';
COMMENT ON COLUMN "public"."iam_perm_path"."leaf" IS '叶子节点';
COMMENT ON COLUMN "public"."iam_perm_path"."path" IS '请求路径';
COMMENT ON COLUMN "public"."iam_perm_path"."method" IS '请求类型, 为全大写单词';
COMMENT ON TABLE "public"."iam_perm_path" IS '请求权限(url)';

-- ----------------------------
-- Records of iam_perm_path
-- ----------------------------

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role";
CREATE TABLE "public"."iam_role" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "pid" int8,
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "internal" bool,
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_role"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_role"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_role"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_role"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_role"."pid" IS '父级Id';
COMMENT ON COLUMN "public"."iam_role"."code" IS '编码';
COMMENT ON COLUMN "public"."iam_role"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_role"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_role"."remark" IS '备注';
COMMENT ON TABLE "public"."iam_role" IS '角色';

-- ----------------------------
-- Records of iam_role
-- ----------------------------
INSERT INTO "public"."iam_role" VALUES (1826818640033910784, 1811365615815487488, '2024-08-23 11:07:59.093723', 1811365615815487488, '2024-08-23 11:07:59.096792', 0, 'f', NULL, 'merchant_admin', '商户系统管理员', 't', '商户系统的管理员');
INSERT INTO "public"."iam_role" VALUES (1828680591068422144, 1827910922934001664, '2024-08-28 14:26:42.813', 1827910922934001664, '2024-08-28 14:26:42.814504', 0, 'f', 1826818640033910784, 'cs1', '商户子管理员', 'f', '');

-- ----------------------------
-- Table structure for iam_role_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_code";
CREATE TABLE "public"."iam_role_code" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "code_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_code"."role_id" IS '角色id';
COMMENT ON COLUMN "public"."iam_role_code"."code_id" IS '权限码';
COMMENT ON TABLE "public"."iam_role_code" IS '角色权限码关联关系';

-- ----------------------------
-- Records of iam_role_code
-- ----------------------------
INSERT INTO "public"."iam_role_code" VALUES (1812476422410076160, 1810203792869191680, 1810975149316100096);
INSERT INTO "public"."iam_role_code" VALUES (1812476422632374272, 1810208169436672000, 1812476422410076160);
INSERT INTO "public"."iam_role_code" VALUES (1812476422649151488, 1810205411434328064, 1812476422410076160);

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_menu";
CREATE TABLE "public"."iam_role_menu" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_menu"."role_id" IS '角色id';
COMMENT ON COLUMN "public"."iam_role_menu"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_role_menu"."menu_id" IS '菜单id';
COMMENT ON TABLE "public"."iam_role_menu" IS '角色菜单关联关系';

-- ----------------------------
-- Records of iam_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for iam_role_path
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_path";
CREATE TABLE "public"."iam_role_path" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "path_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_path"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_path"."role_id" IS '角色id';
COMMENT ON COLUMN "public"."iam_role_path"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_role_path"."path_id" IS '请求资源id';
COMMENT ON TABLE "public"."iam_role_path" IS '角色路径关联关系';

-- ----------------------------
-- Records of iam_role_path
-- ----------------------------

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_expand_info";
CREATE TABLE "public"."iam_user_expand_info" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "sex" varchar(10) COLLATE "pg_catalog"."default",
  "avatar" varchar(300) COLLATE "pg_catalog"."default",
  "birthday" date,
  "last_login_time" timestamp(0),
  "register_time" timestamp(6),
  "current_login_time" timestamp(6),
  "initial_password" bool,
  "expire_password" bool,
  "last_change_password_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."iam_user_expand_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_expand_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_expand_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_user_expand_info"."sex" IS '性别';
COMMENT ON COLUMN "public"."iam_user_expand_info"."avatar" IS '头像图片url';
COMMENT ON COLUMN "public"."iam_user_expand_info"."birthday" IS '生日';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_login_time" IS '上次登录时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_time" IS '注册时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."current_login_time" IS '本次登录时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."initial_password" IS '是否初始密码';
COMMENT ON COLUMN "public"."iam_user_expand_info"."expire_password" IS '密码是否过期';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_change_password_time" IS '上次修改密码时间';
COMMENT ON TABLE "public"."iam_user_expand_info" IS '用户扩展信息';

-- ----------------------------
-- Records of iam_user_expand_info
-- ----------------------------
INSERT INTO "public"."iam_user_expand_info" VALUES (1811365615815487488, 0, '2024-07-11 19:43:11.023201', 0, '2024-07-11 19:43:11.023201', 0, 'f', NULL, NULL, NULL, NULL, '2024-07-11 19:43:11.021597', NULL, 'f', 'f', NULL);

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_info";
CREATE TABLE "public"."iam_user_info" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "account" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "phone" varchar(50) COLLATE "pg_catalog"."default",
  "email" varchar(50) COLLATE "pg_catalog"."default",
  "administrator" bool NOT NULL,
  "status" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_user_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_user_info"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_user_info"."account" IS '账号';
COMMENT ON COLUMN "public"."iam_user_info"."password" IS '密码';
COMMENT ON COLUMN "public"."iam_user_info"."phone" IS '手机号';
COMMENT ON COLUMN "public"."iam_user_info"."email" IS '邮箱';
COMMENT ON COLUMN "public"."iam_user_info"."administrator" IS '是否管理员';
COMMENT ON COLUMN "public"."iam_user_info"."status" IS '账号状态';
COMMENT ON TABLE "public"."iam_user_info" IS '用户核心信息';

-- ----------------------------
-- Records of iam_user_info
-- ----------------------------
INSERT INTO "public"."iam_user_info" VALUES (1811365615815487488, 0, '2024-07-11 19:43:11.013754', 0, '2024-07-11 19:43:32.925167', 1, 'f', '超级管理员', 'bootx', '$2a$10$knt/I7FYX9YpWYfjv4ChhuOhiYGf3/fCvrHxm51pis.OnNPBDvSeC', '13333333333', 'bootx@bootx.cn', 't', 'normal');

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_role";
CREATE TABLE "public"."iam_user_role" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_user_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_role"."user_id" IS '用户';
COMMENT ON COLUMN "public"."iam_user_role"."role_id" IS '角色';
COMMENT ON TABLE "public"."iam_user_role" IS '用户角色关联关系';

-- ----------------------------
-- Records of iam_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for pay_api_const
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_api_const";
CREATE TABLE "public"."pay_api_const" (
  "id" int8 NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "api" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "enable" bool,
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_api_const"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_api_const"."code" IS '编码';
COMMENT ON COLUMN "public"."pay_api_const"."name" IS '接口名称';
COMMENT ON COLUMN "public"."pay_api_const"."api" IS '接口地址';
COMMENT ON COLUMN "public"."pay_api_const"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_api_const"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_api_const" IS '支付接口常量';

-- ----------------------------
-- Records of pay_api_const
-- ----------------------------

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_channel_config";
CREATE TABLE "public"."pay_channel_config" (
  "id" int8 NOT NULL,
  "channel" varchar(255) COLLATE "pg_catalog"."default",
  "out_mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "out_app_id" varchar(32) COLLATE "pg_catalog"."default",
  "enable" bool,
  "ext" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."pay_channel_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_channel_config"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_channel_config"."out_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."pay_channel_config"."out_app_id" IS '通道APPID';
COMMENT ON COLUMN "public"."pay_channel_config"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_channel_config"."ext" IS '扩展存储';
COMMENT ON COLUMN "public"."pay_channel_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_channel_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_channel_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_channel_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_channel_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_channel_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_channel_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_channel_config"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_channel_config" IS '通道支付配置';

-- ----------------------------
-- Records of pay_channel_config
-- ----------------------------

-- ----------------------------
-- Table structure for pay_channel_const
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_channel_const";
CREATE TABLE "public"."pay_channel_const" (
  "id" int8 NOT NULL,
  "code" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "enable" bool,
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_channel_const"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_channel_const"."code" IS '通道编码';
COMMENT ON COLUMN "public"."pay_channel_const"."name" IS '通道名称';
COMMENT ON COLUMN "public"."pay_channel_const"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_channel_const"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_channel_const" IS '支付通道常量';

-- ----------------------------
-- Records of pay_channel_const
-- ----------------------------
INSERT INTO "public"."pay_channel_const" VALUES (30000, 'union_pay', '云闪付', 't', NULL);
INSERT INTO "public"."pay_channel_const" VALUES (10000, 'ali_pay', '支付宝(直连商户)', 't', NULL);
INSERT INTO "public"."pay_channel_const" VALUES (20000, 'wechat_pay', '微信支付(直连商户)', 't', NULL);

-- ----------------------------
-- Table structure for pay_channel_reconcile_trade
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_channel_reconcile_trade";
CREATE TABLE "public"."pay_channel_reconcile_trade" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "reconcile_id" int8,
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "trade_no" varchar(32) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(100) COLLATE "pg_catalog"."default",
  "amount" numeric(13,2),
  "trade_status" varchar(32) COLLATE "pg_catalog"."default",
  "trade_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."reconcile_id" IS '关联对账单ID';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."trade_type" IS '交易类型';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."amount" IS '交易金额';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."trade_status" IS '交易状态';
COMMENT ON COLUMN "public"."pay_channel_reconcile_trade"."trade_time" IS '交易时间';
COMMENT ON TABLE "public"."pay_channel_reconcile_trade" IS '通道对账交易明细';

-- ----------------------------
-- Records of pay_channel_reconcile_trade
-- ----------------------------

-- ----------------------------
-- Table structure for pay_close_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_close_record";
CREATE TABLE "public"."pay_close_record" (
  "id" int8 NOT NULL,
  "order_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "close_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "closed" bool NOT NULL,
  "error_code" varchar(10) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_close_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_close_record"."order_no" IS '支付订单号';
COMMENT ON COLUMN "public"."pay_close_record"."biz_order_no" IS '商户支付订单号';
COMMENT ON COLUMN "public"."pay_close_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_close_record"."close_type" IS '关闭类型';
COMMENT ON COLUMN "public"."pay_close_record"."closed" IS '是否关闭成功';
COMMENT ON COLUMN "public"."pay_close_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_close_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_close_record"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "public"."pay_close_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_close_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_close_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_close_record"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_close_record" IS '支付关闭记录';

-- ----------------------------
-- Records of pay_close_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_mch_app";
CREATE TABLE "public"."pay_mch_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "app_name" varchar(255) COLLATE "pg_catalog"."default",
  "sign_type" varchar(255) COLLATE "pg_catalog"."default",
  "sign_secret" varchar(255) COLLATE "pg_catalog"."default",
  "req_sign" bool,
  "limit_amount" numeric(10,4),
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "notify_type" varchar(255) COLLATE "pg_catalog"."default",
  "notify_url" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "order_timeout" int2
)
;
COMMENT ON COLUMN "public"."pay_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_mch_app"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."pay_mch_app"."sign_type" IS '签名方式';
COMMENT ON COLUMN "public"."pay_mch_app"."sign_secret" IS '签名秘钥';
COMMENT ON COLUMN "public"."pay_mch_app"."req_sign" IS '是否对请求进行验签';
COMMENT ON COLUMN "public"."pay_mch_app"."limit_amount" IS '支付限额(元)';
COMMENT ON COLUMN "public"."pay_mch_app"."status" IS '应用状态';
COMMENT ON COLUMN "public"."pay_mch_app"."notify_type" IS '异步消息通知类型';
COMMENT ON COLUMN "public"."pay_mch_app"."notify_url" IS '通知地址';
COMMENT ON COLUMN "public"."pay_mch_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_mch_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_mch_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_mch_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_mch_app"."order_timeout" IS '订单默认超时时间(分钟)';
COMMENT ON TABLE "public"."pay_mch_app" IS '商户应用';

-- ----------------------------
-- Records of pay_mch_app
-- ----------------------------

-- ----------------------------
-- Table structure for pay_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant";
CREATE TABLE "public"."pay_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "company_name" varchar(100) COLLATE "pg_catalog"."default",
  "id_type" varchar(32) COLLATE "pg_catalog"."default",
  "id_no" varchar COLLATE "pg_catalog"."default",
  "contact" varchar(100) COLLATE "pg_catalog"."default",
  "legal_person" varchar(100) COLLATE "pg_catalog"."default",
  "status" varchar(10) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(0),
  "last_modifier" int8,
  "last_modified_time" timestamp(0),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "company_contact" varchar(255) COLLATE "pg_catalog"."default",
  "company_address" varchar(150) COLLATE "pg_catalog"."default",
  "company_code" varchar(50) COLLATE "pg_catalog"."default",
  "administrator" bool NOT NULL,
  "admin_user_id" int8
)
;
COMMENT ON COLUMN "public"."pay_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant"."mch_name" IS '商户名称';
COMMENT ON COLUMN "public"."pay_merchant"."company_name" IS '公司名称';
COMMENT ON COLUMN "public"."pay_merchant"."id_type" IS '法人证件类型';
COMMENT ON COLUMN "public"."pay_merchant"."id_no" IS '法人证件号';
COMMENT ON COLUMN "public"."pay_merchant"."contact" IS '法人联系方式';
COMMENT ON COLUMN "public"."pay_merchant"."legal_person" IS '法人名称';
COMMENT ON COLUMN "public"."pay_merchant"."status" IS '状态';
COMMENT ON COLUMN "public"."pay_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_merchant"."company_contact" IS '公司联系方式';
COMMENT ON COLUMN "public"."pay_merchant"."company_address" IS '公司地址';
COMMENT ON COLUMN "public"."pay_merchant"."company_code" IS '公司信用编码';
COMMENT ON COLUMN "public"."pay_merchant"."administrator" IS '是否有关联管理员';
COMMENT ON COLUMN "public"."pay_merchant"."admin_user_id" IS '关联管理员用户';
COMMENT ON TABLE "public"."pay_merchant" IS '商户';

-- ----------------------------
-- Table structure for pay_merchant_callback_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_callback_record";
CREATE TABLE "public"."pay_merchant_callback_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "task_id" int8,
  "req_count" int4,
  "success" bool,
  "send_type" varchar(30) COLLATE "pg_catalog"."default",
  "error_code" varchar(50) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."req_count" IS '请求次数';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."success" IS '发送是否成功';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."send_type" IS '发送类型, 自动发送, 手动发送';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_merchant_callback_record"."error_msg" IS '错误信息';
COMMENT ON TABLE "public"."pay_merchant_callback_record" IS '客户回调消息发送记录';

-- ----------------------------
-- Records of pay_merchant_callback_record
-- ----------------------------
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1825377527825821696, 1811365615815487488, '2024-08-19 11:41:31.165095', 'M1723635576766', 'M8088873888246277', 1824816742103404544, 1, 'f', 'auto', NULL, '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML><HEAD><META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<TITLE>ERROR');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1825377706616418304, 1811365615815487488, '2024-08-19 11:42:13.792621', 'M1723635576766', 'M8088873888246277', 1824816742103404544, 2, 'f', 'auto', NULL, '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML><HEAD><META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<TITLE>ERROR');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830441887547912192, 1811365615815487488, '2024-09-02 11:05:28.614748', 'M1723635576766', 'M8088873888246277', 1830440798513606656, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830441986235691008, 1811365615815487488, '2024-09-02 11:05:52.143841', 'M1723635576766', 'M8088873888246277', 1830440798513606656, 2, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830557581815873536, 0, '2024-09-02 18:45:12.277747', 'M1723635576766', 'M8088873888246277', 1830556346584948736, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830557581815873538, 0, '2024-09-02 18:45:12.277747', 'M1723635576766', 'M8088873888246277', 1830557566913511424, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830557581815873537, 0, '2024-09-02 18:45:12.277747', 'M1723635576766', 'M8088873888246277', 1830556346584948736, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830557581832650752, 0, '2024-09-02 18:45:12.281527', 'M1723635576766', 'M8088873888246277', 1830556346584948736, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830557927518797824, 0, '2024-09-02 18:46:34.699122', 'M1723635576766', 'M8088873888246277', 1830557921378336768, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830793503413366784, 0, '2024-09-03 10:22:40.371958', 'M1723635576766', 'M8088873888246277', 1830556346584948736, 2, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830793752412418048, 0, '2024-09-03 10:23:39.735082', 'M1723635576766', 'M8088873888246277', 1830793749526736896, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1830793756615110656, 0, '2024-09-03 10:23:40.737275', 'M1723635576766', 'M8088873888246277', 1830793753423245312, 1, 't', 'auto', NULL, NULL);
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831248957557895168, 0, '2024-09-04 16:32:29.100425', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249045638279168, 0, '2024-09-04 16:32:50.099068', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249121102196736, 0, '2024-09-04 16:33:08.09105', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249196708720640, 0, '2024-09-04 16:33:26.117327', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249209295826944, 0, '2024-09-04 16:33:29.118011', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249276291444736, 0, '2024-09-04 16:33:45.091067', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249360198496256, 0, '2024-09-04 16:34:05.096451', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249364447326208, 0, '2024-09-04 16:34:06.109471', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249368662601728, 0, '2024-09-04 16:34:07.114957', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249465110622208, 0, '2024-09-04 16:34:30.109114', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831249515425492992, 0, '2024-09-04 16:34:42.105449', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250235855962112, 0, '2024-09-04 16:37:33.869186', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250239806996480, 0, '2024-09-04 16:37:34.811943', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250242814312448, 0, '2024-09-04 16:37:35.528248', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250243460235264, 0, '2024-09-04 16:37:35.682954', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250247679705088, 0, '2024-09-04 16:37:36.688288', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250293741551616, 0, '2024-09-04 16:37:47.670539', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250335659425792, 0, '2024-09-04 16:37:57.66471', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831250390474784768, 0, '2024-09-04 16:38:10.733549', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254399243747328, 0, '2024-09-04 16:54:06.498153', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254421066711040, 0, '2024-09-04 16:54:11.701854', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254421066711042, 0, '2024-09-04 16:54:11.701854', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254421066711041, 0, '2024-09-04 16:54:11.701854', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254421066711044, 0, '2024-09-04 16:54:11.701854', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831254421066711043, 0, '2024-09-04 16:54:11.701854', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831255180105711616, 0, '2024-09-04 16:57:12.671189', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831255201106591744, 0, '2024-09-04 16:57:17.677071', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831255352198004736, 0, '2024-09-04 16:57:53.700166', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 7, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831256962672328704, 0, '2024-09-04 17:04:17.667461', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831257721904267264, 0, '2024-09-04 17:07:18.682658', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831257742833844224, 0, '2024-09-04 17:07:23.672563', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831259479242145792, 0, '2024-09-04 17:14:17.664544', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831259483440644096, 0, '2024-09-04 17:14:18.666147', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831262434045431809, 0, '2024-09-04 17:26:02.147162', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 8, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831262434045431808, 0, '2024-09-04 17:26:02.147162', 'M1723635576766', 'M7189826551600486', 1831249437478567936, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831262778242600960, 0, '2024-09-04 17:27:24.207906', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831262799260258304, 0, '2024-09-04 17:27:29.218295', 'M1723635576766', 'M7189826551600486', 1831250221469499392, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831262929224962048, 0, '2024-09-04 17:28:00.204216', 'M1723635576766', 'M7189826551600486', 1831249249733111808, 9, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831264860009791488, 0, '2024-09-04 17:35:40.539833', 'M1723635576766', 'M7189826551600486', 1831264836148396032, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831264948077592576, 0, '2024-09-04 17:36:01.536382', 'M1723635576766', 'M7189826551600486', 1831264836148396032, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265099080925184, 0, '2024-09-04 17:36:37.538064', 'M1723635576766', 'M7189826551600486', 1831264836148396032, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265430426746880, 0, '2024-09-04 17:37:56.537442', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265518511325184, 0, '2024-09-04 17:38:17.538747', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265669544017920, 0, '2024-09-04 17:38:53.547283', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265673721544704, 0, '2024-09-04 17:38:54.543355', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831265879259217920, 0, '2024-09-04 17:39:43.547446', 'M1723635576766', 'M7189826551600486', 1831264836148396032, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266525739978754, 0, '2024-09-04 17:42:17.683748', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266525739978755, 0, '2024-09-04 17:42:17.683748', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266525739978752, 0, '2024-09-04 17:42:17.683748', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266525739978753, 0, '2024-09-04 17:42:17.683748', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266567745933313, 0, '2024-09-04 17:42:27.696438', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266567745933312, 0, '2024-09-04 17:42:27.695948', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831266567741739008, 0, '2024-09-04 17:42:27.69488', 'M1723635576766', 'M7189826551600486', 1831266530206912512, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267247986561024, 0, '2024-09-04 17:45:09.877808', 'M1723635576766', 'M7189826551600486', 1831248932618563584, 7, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267247986561025, 0, '2024-09-04 17:45:09.877808', 'M1723635576766', 'M7189826551600486', 1831249096196419584, 7, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267255502753792, 0, '2024-09-04 17:45:11.669944', 'M1723635576766', 'M7189826551600486', 1831266530206912512, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267259659309056, 0, '2024-09-04 17:45:12.660513', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267363438972928, 0, '2024-09-04 17:45:37.403656', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267399245746176, 0, '2024-09-04 17:45:45.94103', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267420343095296, 0, '2024-09-04 17:45:50.970955', 'M1723635576766', 'M7189826551600486', 1831266530206912512, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267511334326272, 0, '2024-09-04 17:46:12.664764', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831267549024342016, 0, '2024-09-04 17:46:21.651226', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268200689127424, 0, '2024-09-04 17:48:57.019558', 'M1723635576766', 'M7189826551600486', 1831266530206912512, 4, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268310684749824, 0, '2024-09-04 17:49:23.244956', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268310684749825, 0, '2024-09-04 17:49:23.244956', 'M1723635576766', 'M7189826551600486', 1831268284864614400, 1, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268331769516032, 0, '2024-09-04 17:49:28.272354', 'M1723635576766', 'M7189826551600486', 1831267235743387648, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268397695586304, 0, '2024-09-04 17:49:43.989727', 'M1723635576766', 'M7189826551600486', 1831268284864614400, 2, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268419199782912, 0, '2024-09-04 17:49:49.116987', 'M1723635576766', 'M7189826551600486', 1831264836148396032, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831268548652781568, 0, '2024-09-04 17:50:19.980461', 'M1723635576766', 'M7189826551600486', 1831268284864614400, 3, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269064598310912, 0, '2024-09-04 17:52:22.991597', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269068780032000, 0, '2024-09-04 17:52:23.988095', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269072915615744, 0, '2024-09-04 17:52:24.974743', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269077151862784, 0, '2024-09-04 17:52:25.98482', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 5, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269106482630656, 0, '2024-09-04 17:52:32.9771', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269110660157440, 0, '2024-09-04 17:52:33.973576', 'M1723635576766', 'M7189826551600486', 1831265404921184256, 6, 'f', 'auto', NULL, 'Read timed out');
INSERT INTO "public"."pay_merchant_callback_record" VALUES (1831269328805908480, 0, '2024-09-04 17:53:25.98395', 'M1723635576766', 'M7189826551600486', 1831268284864614400, 4, 'f', 'auto', NULL, 'Read timed out');

-- ----------------------------
-- Table structure for pay_merchant_callback_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_callback_task";
CREATE TABLE "public"."pay_merchant_callback_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "trade_id" int8,
  "trade_no" varchar(32) COLLATE "pg_catalog"."default",
  "trade_type" varchar(20) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "success" bool,
  "next_time" timestamp(6),
  "send_count" int4,
  "delay_count" int4,
  "latest_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "url" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."trade_id" IS '本地交易ID';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."trade_type" IS '通知类型';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."content" IS '消息内容';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."next_time" IS '发送次数';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."send_count" IS '延迟次数';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."delay_count" IS '下次发送时间';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_merchant_callback_task"."url" IS '发送地址';
COMMENT ON TABLE "public"."pay_merchant_callback_task" IS '客户回调通知消息任务';

-- ----------------------------
-- Records of pay_merchant_callback_task
-- ----------------------------

-- ----------------------------
-- Table structure for pay_merchant_notify_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_notify_config";
CREATE TABLE "public"."pay_merchant_notify_config" (
  "id" int8 NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "subscribe" bool,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."code" IS '通知类型';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."subscribe" IS '是否订阅';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_notify_config"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_merchant_notify_config" IS '商户应用消息通知配置';

-- ----------------------------
-- Records of pay_merchant_notify_config
-- ----------------------------

-- ----------------------------
-- Table structure for pay_merchant_notify_const
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_notify_const";
CREATE TABLE "public"."pay_merchant_notify_const" (
  "id" int8 NOT NULL,
  "code" varchar(60) COLLATE "pg_catalog"."default",
  "name" varchar(80) COLLATE "pg_catalog"."default",
  "description" varchar(300) COLLATE "pg_catalog"."default",
  "enable" bool
)
;
COMMENT ON COLUMN "public"."pay_merchant_notify_const"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_notify_const"."code" IS '通道编码';
COMMENT ON COLUMN "public"."pay_merchant_notify_const"."name" IS '通道名称';
COMMENT ON COLUMN "public"."pay_merchant_notify_const"."description" IS '描述';
COMMENT ON COLUMN "public"."pay_merchant_notify_const"."enable" IS '是否启用';
COMMENT ON TABLE "public"."pay_merchant_notify_const" IS '商户订阅通知类型';

-- ----------------------------
-- Records of pay_merchant_notify_const
-- ----------------------------
INSERT INTO "public"."pay_merchant_notify_const" VALUES (10000, 'pay', '支付订单变动通知', '支付订单变动通知', 't');
INSERT INTO "public"."pay_merchant_notify_const" VALUES (20000, 'refund', '退款订单变动通知', '退款订单变动通知', 't');
INSERT INTO "public"."pay_merchant_notify_const" VALUES (30000, 'transfer', '转账订单变动通知', '转账订单变动通知', 't');

-- ----------------------------
-- Table structure for pay_merchant_notify_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_notify_record";
CREATE TABLE "public"."pay_merchant_notify_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "task_id" int8,
  "req_count" int4,
  "success" bool,
  "send_type" varchar(30) COLLATE "pg_catalog"."default",
  "error_code" varchar(50) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."req_count" IS '请求次数';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."success" IS '发送是否成功';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."send_type" IS '发送类型, 自动发送, 手动发送';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_merchant_notify_record"."error_msg" IS '错误信息';
COMMENT ON TABLE "public"."pay_merchant_notify_record" IS '客户订阅通知发送记录';

-- ----------------------------
-- Records of pay_merchant_notify_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_merchant_notify_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_notify_task";
CREATE TABLE "public"."pay_merchant_notify_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "trade_id" int8,
  "trade_no" varchar(32) COLLATE "pg_catalog"."default",
  "notify_type" varchar(20) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "success" bool,
  "next_time" timestamp(6),
  "send_count" int4,
  "delay_count" int4,
  "latest_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."trade_id" IS '本地交易ID';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."notify_type" IS '通知类型';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."content" IS '消息内容';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."next_time" IS '发送次数';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."send_count" IS '延迟次数';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."delay_count" IS '下次发送时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_notify_task"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_merchant_notify_task" IS '客户订阅通知消息任务';

-- ----------------------------
-- Records of pay_merchant_notify_task
-- ----------------------------

-- ----------------------------
-- Table structure for pay_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_merchant_user";
CREATE TABLE "public"."pay_merchant_user" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "administrator" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."pay_merchant_user"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_merchant_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."pay_merchant_user"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_merchant_user"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_merchant_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_merchant_user"."administrator" IS '是否为商户管理员';
COMMENT ON TABLE "public"."pay_merchant_user" IS '用户商户关联关系';

-- ----------------------------
-- Records of pay_merchant_user
-- ----------------------------

-- ----------------------------
-- Table structure for pay_method_const
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_method_const";
CREATE TABLE "public"."pay_method_const" (
  "id" int8 NOT NULL,
  "code" varchar(60) COLLATE "pg_catalog"."default",
  "name" varchar(80) COLLATE "pg_catalog"."default",
  "enable" bool,
  "remark" varchar(300) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_method_const"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_method_const"."code" IS '通道编码';
COMMENT ON COLUMN "public"."pay_method_const"."name" IS '通道名称';
COMMENT ON COLUMN "public"."pay_method_const"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_method_const"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_method_const" IS '支付方式常量';

-- ----------------------------
-- Records of pay_method_const
-- ----------------------------
INSERT INTO "public"."pay_method_const" VALUES (10000, 'normal', '常规支付', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (20000, 'wap', 'wap支付', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (30000, 'app', '应用支付', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (40000, 'web', 'web支付', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (50000, 'qrcode', '扫码支付', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (60000, 'barcode', '付款码', 't', NULL);
INSERT INTO "public"."pay_method_const" VALUES (70000, 'jsapi', '小程序支付', 't', NULL);

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_order";
CREATE TABLE "public"."pay_order" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "biz_order_no" varchar(32) COLLATE "pg_catalog"."default",
  "order_no" varchar(100) COLLATE "pg_catalog"."default",
  "out_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "allocation" bool,
  "auto_allocation" bool,
  "channel" varchar(20) COLLATE "pg_catalog"."default",
  "method" varchar(20) COLLATE "pg_catalog"."default",
  "amount" numeric(12,2),
  "refundable_balance" numeric(12,2),
  "status" varchar(32) COLLATE "pg_catalog"."default",
  "refund_status" varchar(32) COLLATE "pg_catalog"."default",
  "alloc_status" varchar(32) COLLATE "pg_catalog"."default",
  "return_url" varchar(200) COLLATE "pg_catalog"."default",
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "extra_param" varchar(2048) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "req_time" timestamp(6),
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "error_code" varchar(50) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "expired_time" timestamp(6),
  "pay_time" timestamp(6),
  "close_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_order"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_order"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_order"."biz_order_no" IS '商户订单号';
COMMENT ON COLUMN "public"."pay_order"."order_no" IS '订单号';
COMMENT ON COLUMN "public"."pay_order"."out_order_no" IS '通道订单号';
COMMENT ON COLUMN "public"."pay_order"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_order"."description" IS '描述';
COMMENT ON COLUMN "public"."pay_order"."allocation" IS '是否支持分账';
COMMENT ON COLUMN "public"."pay_order"."auto_allocation" IS '自动分账';
COMMENT ON COLUMN "public"."pay_order"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_order"."method" IS '支付方式';
COMMENT ON COLUMN "public"."pay_order"."amount" IS '金额(元)';
COMMENT ON COLUMN "public"."pay_order"."refundable_balance" IS '可退金额(元)';
COMMENT ON COLUMN "public"."pay_order"."status" IS '支付状态';
COMMENT ON COLUMN "public"."pay_order"."refund_status" IS '退款状态';
COMMENT ON COLUMN "public"."pay_order"."alloc_status" IS '分账状态';
COMMENT ON COLUMN "public"."pay_order"."return_url" IS '同步跳转地址';
COMMENT ON COLUMN "public"."pay_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_order"."extra_param" IS '通道附加参数';
COMMENT ON COLUMN "public"."pay_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "public"."pay_order"."req_time" IS '请求时间';
COMMENT ON COLUMN "public"."pay_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "public"."pay_order"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_order"."expired_time" IS '过期时间';
COMMENT ON COLUMN "public"."pay_order"."pay_time" IS '支付成功时间';
COMMENT ON COLUMN "public"."pay_order"."close_time" IS '关闭时间';
COMMENT ON TABLE "public"."pay_order" IS '支付订单';

-- ----------------------------
-- Records of pay_order
-- ----------------------------

-- ----------------------------
-- Table structure for pay_reconcile_discrepancy
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_reconcile_discrepancy";
CREATE TABLE "public"."pay_reconcile_discrepancy" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "reconcile_id" int8,
  "reconcile_no" varchar(32) COLLATE "pg_catalog"."default",
  "reconcile_date" date,
  "channel" varchar(20) COLLATE "pg_catalog"."default",
  "discrepancy_type" varchar(20) COLLATE "pg_catalog"."default",
  "trade_no" varchar(32) COLLATE "pg_catalog"."default",
  "biz_trade_no" varchar(100) COLLATE "pg_catalog"."default",
  "trade_type" varchar(100) COLLATE "pg_catalog"."default",
  "trade_amount" numeric(13,2),
  "trade_status" varchar(32) COLLATE "pg_catalog"."default",
  "trade_time" timestamp(0),
  "channel_trade_no" varchar COLLATE "pg_catalog"."default",
  "channel_trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "channel_trade_amount" numeric(13,2),
  "channel_trade_status" varchar(32) COLLATE "pg_catalog"."default",
  "channel_trade_time" timestamp(6),
  "out_trade_no" varchar(100) COLLATE "pg_catalog"."default",
  "channel_out_trade_no" varchar(100) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."reconcile_id" IS '对账单ID';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."reconcile_no" IS '对账号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."reconcile_date" IS '对账日期';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."discrepancy_type" IS '差异类型';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."trade_no" IS '平台交易号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."biz_trade_no" IS '商户订单号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."trade_type" IS '交易类型';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."trade_amount" IS '交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."trade_status" IS '交易状态';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."trade_time" IS '交易时间';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_trade_type" IS '通道交易类型';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_trade_amount" IS '通道交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_trade_status" IS '通道交易状态';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_trade_time" IS '通道交易时间';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."out_trade_no" IS '平台订单关联通道订单号';
COMMENT ON COLUMN "public"."pay_reconcile_discrepancy"."channel_out_trade_no" IS '通道关联订单号';
COMMENT ON TABLE "public"."pay_reconcile_discrepancy" IS '对账差异记录';

-- ----------------------------
-- Records of pay_reconcile_discrepancy
-- ----------------------------

-- ----------------------------
-- Table structure for pay_reconcile_statement
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_reconcile_statement";
CREATE TABLE "public"."pay_reconcile_statement" (
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "reconcile_no" varchar(32) COLLATE "pg_catalog"."default",
  "date" date,
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "down_or_upload" bool,
  "compare" bool,
  "order_count" varchar(8) COLLATE "pg_catalog"."default",
  "order_amount" numeric(13,2),
  "refund_count" varchar(8) COLLATE "pg_catalog"."default",
  "refund_amount" numeric(13,2),
  "channel_order_count" varchar(8) COLLATE "pg_catalog"."default",
  "channel_order_amount" numeric(13,2),
  "channel_refund_count" varchar(8) COLLATE "pg_catalog"."default",
  "channel_refund_amount" numeric(13,2),
  "result" varchar(32) COLLATE "pg_catalog"."default",
  "channel_file_url" varchar(500) COLLATE "pg_catalog"."default",
  "platform_file_url" varchar(500) COLLATE "pg_catalog"."default",
  "error_code" varchar(100) COLLATE "pg_catalog"."default",
  "error_msg" varchar(500) COLLATE "pg_catalog"."default",
  "id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."pay_reconcile_statement"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."name" IS '名称';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."reconcile_no" IS '对账号';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."date" IS '日期';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel" IS '通道';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."down_or_upload" IS '交易对账文件是否下载或上传成功';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."compare" IS '交易对账文件是否比对完成';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."order_count" IS '支付订单数';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."order_amount" IS '支付交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."refund_count" IS '退款订单数';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."refund_amount" IS '退款交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel_order_count" IS '通道支付订单数';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel_order_amount" IS '通道支付交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel_refund_count" IS '通道退款订单数';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel_refund_amount" IS '通道退款交易金额';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."result" IS '交易对账结果';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."channel_file_url" IS '原始通道对账单文件url';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."platform_file_url" IS '生成平台对账单文件url';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_reconcile_statement"."id" IS '主键';
COMMENT ON TABLE "public"."pay_reconcile_statement" IS '对账报告';

-- ----------------------------
-- Records of pay_reconcile_statement
-- ----------------------------

-- ----------------------------
-- Table structure for pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_refund_order";
CREATE TABLE "public"."pay_refund_order" (
  "id" int8 NOT NULL,
  "order_id" int8 NOT NULL,
  "order_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "out_order_no" varchar(150) COLLATE "pg_catalog"."default" NOT NULL,
  "title" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "refund_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_refund_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "out_refund_no" varchar(150) COLLATE "pg_catalog"."default",
  "channel" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "order_amount" numeric(32,2) NOT NULL,
  "amount" numeric(32,2) NOT NULL,
  "reason" varchar(150) COLLATE "pg_catalog"."default",
  "finish_time" timestamp(6),
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "extra_param" varchar(2048) COLLATE "pg_catalog"."default",
  "req_time" timestamp(6),
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "error_code" varchar(10) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_refund_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_refund_order"."order_id" IS '支付订单ID';
COMMENT ON COLUMN "public"."pay_refund_order"."order_no" IS '支付订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_order_no" IS '商户支付订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."out_order_no" IS '通道支付订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."title" IS '支付标题';
COMMENT ON COLUMN "public"."pay_refund_order"."refund_no" IS '退款号';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_refund_no" IS '商户退款号';
COMMENT ON COLUMN "public"."pay_refund_order"."out_refund_no" IS '通道退款交易号';
COMMENT ON COLUMN "public"."pay_refund_order"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_refund_order"."order_amount" IS '订单金额';
COMMENT ON COLUMN "public"."pay_refund_order"."amount" IS '退款金额';
COMMENT ON COLUMN "public"."pay_refund_order"."reason" IS '退款原因';
COMMENT ON COLUMN "public"."pay_refund_order"."finish_time" IS '退款完成时间';
COMMENT ON COLUMN "public"."pay_refund_order"."status" IS '退款状态';
COMMENT ON COLUMN "public"."pay_refund_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_refund_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "public"."pay_refund_order"."extra_param" IS '附加参数';
COMMENT ON COLUMN "public"."pay_refund_order"."req_time" IS '请求时间，传输时间戳';
COMMENT ON COLUMN "public"."pay_refund_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "public"."pay_refund_order"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_refund_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_refund_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_refund_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_refund_order"."version" IS '乐观锁';
COMMENT ON COLUMN "public"."pay_refund_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_refund_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_refund_order"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_refund_order" IS '退款订单';

-- ----------------------------
-- Records of pay_refund_order
-- ----------------------------

-- ----------------------------
-- Table structure for pay_trade_callback_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_trade_callback_record";
CREATE TABLE "public"."pay_trade_callback_record" (
  "id" int8 NOT NULL,
  "trade_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "out_trade_no" varchar(150) COLLATE "pg_catalog"."default",
  "channel" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "callback_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_info" text COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "error_code" varchar(10) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_trade_callback_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."callback_type" IS '回调类型';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."notify_info" IS '通知消息';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."status" IS '回调处理状态';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_trade_callback_record"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_trade_callback_record" IS '网关回调通知';

-- ----------------------------
-- Records of pay_trade_callback_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_trade_flow_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_trade_flow_record";
CREATE TABLE "public"."pay_trade_flow_record" (
  "id" int8 NOT NULL,
  "title" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "amount" numeric(12,2) NOT NULL,
  "type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "trade_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_trade_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "out_trade_no" varchar(150) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_trade_flow_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."amount" IS '金额';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."type" IS '业务类型';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."biz_trade_no" IS '商户交易号';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_trade_flow_record"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_trade_flow_record" IS '资金流水记录';

-- ----------------------------
-- Records of pay_trade_flow_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_trade_sync_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_trade_sync_record";
CREATE TABLE "public"."pay_trade_sync_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "trade_no" varchar(32) COLLATE "pg_catalog"."default",
  "biz_trade_no" varchar(100) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(150) COLLATE "pg_catalog"."default",
  "out_trade_status" varchar(32) COLLATE "pg_catalog"."default",
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "sync_info" text COLLATE "pg_catalog"."default",
  "adjust" bool NOT NULL,
  "error_code" varchar(50) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_trade_sync_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."trade_no" IS '本地交易号';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."biz_trade_no" IS '商户交易号';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."out_trade_status" IS '通道返回的状态';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."trade_type" IS '同步类型';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."channel" IS '同步通道';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."sync_info" IS '网关返回的同步消息';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."adjust" IS '是否进行调整';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_trade_sync_record"."client_ip" IS '终端ip';
COMMENT ON TABLE "public"."pay_trade_sync_record" IS '交易同步记录';

-- ----------------------------
-- Records of pay_trade_sync_record
-- ----------------------------

-- ----------------------------
-- Table structure for pay_transfer_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_transfer_order";
CREATE TABLE "public"."pay_transfer_order" (
  "id" int8 NOT NULL,
  "biz_transfer_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "transfer_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "out_transfer_no" varchar(150) COLLATE "pg_catalog"."default",
  "channel" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "amount" numeric(32,2) NOT NULL,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "reason" varchar(150) COLLATE "pg_catalog"."default",
  "payee_type" varchar(20) COLLATE "pg_catalog"."default",
  "payee_account" varchar(100) COLLATE "pg_catalog"."default",
  "payee_name" varchar(50) COLLATE "pg_catalog"."default",
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamp(6),
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "req_time" timestamp(6),
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "error_code" varchar(10) COLLATE "pg_catalog"."default",
  "error_msg" varchar(2048) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL,
  "deleted" bool NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_transfer_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_transfer_order"."biz_transfer_no" IS '商户转账号';
COMMENT ON COLUMN "public"."pay_transfer_order"."transfer_no" IS '转账号';
COMMENT ON COLUMN "public"."pay_transfer_order"."out_transfer_no" IS '通道转账号';
COMMENT ON COLUMN "public"."pay_transfer_order"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_transfer_order"."amount" IS '转账金额';
COMMENT ON COLUMN "public"."pay_transfer_order"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_transfer_order"."reason" IS '转账原因/备注';
COMMENT ON COLUMN "public"."pay_transfer_order"."payee_type" IS '收款人类型';
COMMENT ON COLUMN "public"."pay_transfer_order"."payee_account" IS '收款人账号';
COMMENT ON COLUMN "public"."pay_transfer_order"."payee_name" IS '收款人姓名';
COMMENT ON COLUMN "public"."pay_transfer_order"."status" IS '状态';
COMMENT ON COLUMN "public"."pay_transfer_order"."finish_time" IS '成功时间';
COMMENT ON COLUMN "public"."pay_transfer_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_transfer_order"."attach" IS '商户扩展参数';
COMMENT ON COLUMN "public"."pay_transfer_order"."req_time" IS '请求时间，传输时间戳';
COMMENT ON COLUMN "public"."pay_transfer_order"."client_ip" IS '支付终端ip';
COMMENT ON COLUMN "public"."pay_transfer_order"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_transfer_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_transfer_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_transfer_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_transfer_order"."last_modifier" IS '最后修者ID';
COMMENT ON COLUMN "public"."pay_transfer_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_transfer_order"."version" IS '乐观锁';
COMMENT ON COLUMN "public"."pay_transfer_order"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_transfer_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_transfer_order"."app_id" IS '应用号';
COMMENT ON TABLE "public"."pay_transfer_order" IS '转账订单';

-- ----------------------------
-- Records of pay_transfer_order
-- ----------------------------

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_login_log";
CREATE TABLE "public"."starter_audit_login_log" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "account" varchar(100) COLLATE "pg_catalog"."default",
  "login" bool,
  "client" varchar(20) COLLATE "pg_catalog"."default",
  "login_type" varchar(20) COLLATE "pg_catalog"."default",
  "ip" varchar(80) COLLATE "pg_catalog"."default",
  "login_location" varchar(100) COLLATE "pg_catalog"."default",
  "browser" varchar(200) COLLATE "pg_catalog"."default",
  "os" varchar(100) COLLATE "pg_catalog"."default",
  "msg" text COLLATE "pg_catalog"."default",
  "login_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."starter_audit_login_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_login_log"."user_id" IS '用户账号ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."account" IS '用户名称';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login" IS '登录成功状态';
COMMENT ON COLUMN "public"."starter_audit_login_log"."client" IS '登录终端';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_type" IS '登录方式';
COMMENT ON COLUMN "public"."starter_audit_login_log"."ip" IS '登录IP地址';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."starter_audit_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."starter_audit_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."starter_audit_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_time" IS '访问时间';
COMMENT ON TABLE "public"."starter_audit_login_log" IS '登录日志';

-- ----------------------------
-- Records of starter_audit_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_operate_log";
CREATE TABLE "public"."starter_audit_operate_log" (
  "id" int8 NOT NULL,
  "title" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "operate_id" int8,
  "account" varchar(100) COLLATE "pg_catalog"."default",
  "business_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "request_method" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "operate_url" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "operate_ip" varchar(80) COLLATE "pg_catalog"."default",
  "operate_location" varchar(50) COLLATE "pg_catalog"."default",
  "operate_param" text COLLATE "pg_catalog"."default",
  "operate_return" text COLLATE "pg_catalog"."default",
  "success" bool,
  "error_msg" text COLLATE "pg_catalog"."default",
  "operate_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."starter_audit_operate_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."title" IS '操作模块';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_id" IS '操作人员id';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."account" IS '操作人员账号';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."method" IS '请求方法';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_url" IS '请求url';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_ip" IS '操作ip';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_location" IS '操作地点';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_param" IS '请求参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_return" IS '返回参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."success" IS '操作状态';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_time" IS '操作时间';
COMMENT ON TABLE "public"."starter_audit_operate_log" IS '操作日志';

-- ----------------------------
-- Records of starter_audit_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for starter_file_platform
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_file_platform";
CREATE TABLE "public"."starter_file_platform" (
  "id" int8 NOT NULL,
  "type" varchar(50) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "default_platform" bool,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."starter_file_platform"."id" IS '文件id';
COMMENT ON COLUMN "public"."starter_file_platform"."type" IS '平台类型';
COMMENT ON COLUMN "public"."starter_file_platform"."name" IS '名称';
COMMENT ON COLUMN "public"."starter_file_platform"."url" IS '访问地址';
COMMENT ON COLUMN "public"."starter_file_platform"."default_platform" IS '默认存储平台';
COMMENT ON COLUMN "public"."starter_file_platform"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."starter_file_platform"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_file_platform"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."starter_file_platform"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_file_platform"."version" IS '版本号';
COMMENT ON TABLE "public"."starter_file_platform" IS '文件存储平台';

-- ----------------------------
-- Records of starter_file_platform
-- ----------------------------
INSERT INTO "public"."starter_file_platform" VALUES (1000, 'local', '本地存储', 'http://127.0.0.1:9999', 'f', 1811365615815487488, '2024-08-13 12:32:37', 1811365615815487488, '2024-08-13 11:14:49.335313', 0);
INSERT INTO "public"."starter_file_platform" VALUES (2000, 'minio', 'minio存储', 'http://127.0.0.1:9002/daxpay', 't', 1811365615815487488, '2024-08-13 12:32:40', 1811365615815487488, '2024-08-13 11:59:16.24726', 3);

-- ----------------------------
-- Table structure for starter_file_upload_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_file_upload_info";
CREATE TABLE "public"."starter_file_upload_info" (
  "id" int8 NOT NULL,
  "url" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "size" int8,
  "filename" varchar(256) COLLATE "pg_catalog"."default",
  "original_filename" varchar(256) COLLATE "pg_catalog"."default",
  "base_path" varchar(256) COLLATE "pg_catalog"."default",
  "path" varchar(256) COLLATE "pg_catalog"."default",
  "ext" varchar(32) COLLATE "pg_catalog"."default",
  "content_type" varchar(128) COLLATE "pg_catalog"."default",
  "platform" varchar(32) COLLATE "pg_catalog"."default",
  "th_url" varchar(512) COLLATE "pg_catalog"."default",
  "th_filename" varchar(256) COLLATE "pg_catalog"."default",
  "th_size" int8,
  "th_content_type" varchar(128) COLLATE "pg_catalog"."default",
  "object_id" varchar(32) COLLATE "pg_catalog"."default",
  "object_type" varchar(32) COLLATE "pg_catalog"."default",
  "metadata" text COLLATE "pg_catalog"."default",
  "user_metadata" text COLLATE "pg_catalog"."default",
  "th_metadata" text COLLATE "pg_catalog"."default",
  "th_user_metadata" text COLLATE "pg_catalog"."default",
  "attr" text COLLATE "pg_catalog"."default",
  "file_acl" varchar(32) COLLATE "pg_catalog"."default",
  "th_file_acl" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."starter_file_upload_info"."id" IS '文件id';
COMMENT ON COLUMN "public"."starter_file_upload_info"."url" IS '文件访问地址';
COMMENT ON COLUMN "public"."starter_file_upload_info"."size" IS '文件大小，单位字节';
COMMENT ON COLUMN "public"."starter_file_upload_info"."filename" IS '文件名称';
COMMENT ON COLUMN "public"."starter_file_upload_info"."original_filename" IS '原始文件名';
COMMENT ON COLUMN "public"."starter_file_upload_info"."base_path" IS '基础存储路径';
COMMENT ON COLUMN "public"."starter_file_upload_info"."path" IS '存储路径';
COMMENT ON COLUMN "public"."starter_file_upload_info"."ext" IS '文件扩展名';
COMMENT ON COLUMN "public"."starter_file_upload_info"."content_type" IS 'MIME类型';
COMMENT ON COLUMN "public"."starter_file_upload_info"."platform" IS '存储平台';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_url" IS '缩略图访问路径';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_filename" IS '缩略图名称';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_size" IS '缩略图大小，单位字节';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_content_type" IS '缩略图MIME类型';
COMMENT ON COLUMN "public"."starter_file_upload_info"."object_id" IS '文件所属对象id';
COMMENT ON COLUMN "public"."starter_file_upload_info"."object_type" IS '文件所属对象类型，例如用户头像，评价图片';
COMMENT ON COLUMN "public"."starter_file_upload_info"."metadata" IS '文件元数据';
COMMENT ON COLUMN "public"."starter_file_upload_info"."user_metadata" IS '文件用户元数据';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_metadata" IS '缩略图元数据';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_user_metadata" IS '缩略图用户元数据';
COMMENT ON COLUMN "public"."starter_file_upload_info"."attr" IS '附加属性';
COMMENT ON COLUMN "public"."starter_file_upload_info"."file_acl" IS '文件ACL';
COMMENT ON COLUMN "public"."starter_file_upload_info"."th_file_acl" IS '缩略图文件ACL';
COMMENT ON COLUMN "public"."starter_file_upload_info"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."starter_file_upload_info" IS '文件记录表';

-- ----------------------------
-- Records of starter_file_upload_info
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table base_dict
-- ----------------------------
ALTER TABLE "public"."base_dict" ADD CONSTRAINT "base_dict_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_dict_item
-- ----------------------------
ALTER TABLE "public"."base_dict_item" ADD CONSTRAINT "base_dict_item_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_param
-- ----------------------------
ALTER TABLE "public"."base_param" ADD CONSTRAINT "base_param_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_client
-- ----------------------------
ALTER TABLE "public"."iam_client" ADD CONSTRAINT "iam_client_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_code
-- ----------------------------
ALTER TABLE "public"."iam_perm_code" ADD CONSTRAINT "iam_perm_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "public"."iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_path
-- ----------------------------
ALTER TABLE "public"."iam_perm_path" ADD CONSTRAINT "iam_perm_code_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_code
-- ----------------------------
ALTER TABLE "public"."iam_role_code" ADD CONSTRAINT "iam_role_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_menu
-- ----------------------------
ALTER TABLE "public"."iam_role_menu" ADD CONSTRAINT "iam_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role_path
-- ----------------------------
ALTER TABLE "public"."iam_role_path" ADD CONSTRAINT "iam_role_path_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_expand_info
-- ----------------------------
ALTER TABLE "public"."iam_user_expand_info" ADD CONSTRAINT "iam_user_expand_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_info
-- ----------------------------
ALTER TABLE "public"."iam_user_info" ADD CONSTRAINT "iam_user_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_role
-- ----------------------------
ALTER TABLE "public"."iam_user_role" ADD CONSTRAINT "iam_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_api_const
-- ----------------------------
ALTER TABLE "public"."pay_api_const" ADD CONSTRAINT "pay_channel_const_copy1_pkey1" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_config
-- ----------------------------
ALTER TABLE "public"."pay_channel_config" ADD CONSTRAINT "pay_channel_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_const
-- ----------------------------
ALTER TABLE "public"."pay_channel_const" ADD CONSTRAINT "pay_channel_const_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_channel_reconcile_trade
-- ----------------------------
ALTER TABLE "public"."pay_channel_reconcile_trade" ADD CONSTRAINT "pay_channel_reconcile_trade_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_close_record
-- ----------------------------
CREATE INDEX "biz_order_no" ON "public"."pay_close_record" USING btree (
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."biz_order_no" IS '商户支付订单号索引';
CREATE INDEX "order_no" ON "public"."pay_close_record" USING btree (
  "order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."order_no" IS '支付订单号索引';

-- ----------------------------
-- Primary Key structure for table pay_close_record
-- ----------------------------
ALTER TABLE "public"."pay_close_record" ADD CONSTRAINT "pay_close_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_mch_app
-- ----------------------------
ALTER TABLE "public"."pay_mch_app" ADD CONSTRAINT "pay_mch_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant
-- ----------------------------
ALTER TABLE "public"."pay_merchant" ADD CONSTRAINT "pay_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_record
-- ----------------------------
ALTER TABLE "public"."pay_merchant_callback_record" ADD CONSTRAINT "pay_merchant_callback_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_callback_task
-- ----------------------------
ALTER TABLE "public"."pay_merchant_callback_task" ADD CONSTRAINT "pay_merchant_callback_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_config
-- ----------------------------
ALTER TABLE "public"."pay_merchant_notify_config" ADD CONSTRAINT "pay_merchant_notify_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_const
-- ----------------------------
ALTER TABLE "public"."pay_merchant_notify_const" ADD CONSTRAINT "pay_merchant_notify_const_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_record
-- ----------------------------
ALTER TABLE "public"."pay_merchant_notify_record" ADD CONSTRAINT "pay_merchant_notify_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_notify_task
-- ----------------------------
ALTER TABLE "public"."pay_merchant_notify_task" ADD CONSTRAINT "pay_merchant_notify_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_merchant_user
-- ----------------------------
ALTER TABLE "public"."pay_merchant_user" ADD CONSTRAINT "pay_user_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_method_const
-- ----------------------------
ALTER TABLE "public"."pay_method_const" ADD CONSTRAINT "pay_channel_const_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_order
-- ----------------------------
CREATE INDEX "order_biz_order_order_no_idx" ON "public"."pay_order" USING btree (
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "order_pay_order_order_no_idx" ON "public"."pay_order" USING btree (
  "order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "order_pay_order_out_order_no_idx" ON "public"."pay_order" USING btree (
  "out_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table pay_order
-- ----------------------------
ALTER TABLE "public"."pay_order" ADD CONSTRAINT "pay_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_reconcile_discrepancy
-- ----------------------------
ALTER TABLE "public"."pay_reconcile_discrepancy" ADD CONSTRAINT "pay_reconcile_discrepancy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_reconcile_statement
-- ----------------------------
ALTER TABLE "public"."pay_reconcile_statement" ADD CONSTRAINT "pay_reconcile_statement_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_refund_order
-- ----------------------------
CREATE INDEX "refund_biz_order_no" ON "public"."pay_refund_order" USING btree (
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_biz_order_no" IS '商户支付订单号索引';
CREATE INDEX "refund_biz_refund_no" ON "public"."pay_refund_order" USING btree (
  "biz_refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_biz_refund_no" IS '商户退款号索引';
CREATE INDEX "refund_order_id" ON "public"."pay_refund_order" USING btree (
  "order_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_order_id" IS '支付订单ID索引';
CREATE INDEX "refund_order_no" ON "public"."pay_refund_order" USING btree (
  "order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_order_no" IS '支付订单号索引';
CREATE INDEX "refund_out_order_no" ON "public"."pay_refund_order" USING btree (
  "out_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_out_order_no" IS '通道支付订单号索引';
CREATE INDEX "refund_out_refund_no" ON "public"."pay_refund_order" USING btree (
  "out_refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_out_refund_no" IS '通道退款交易号索引';
CREATE INDEX "refund_refund_no" ON "public"."pay_refund_order" USING btree (
  "refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."refund_refund_no" IS '退款号索引';

-- ----------------------------
-- Primary Key structure for table pay_refund_order
-- ----------------------------
ALTER TABLE "public"."pay_refund_order" ADD CONSTRAINT "pay_refund_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_trade_callback_record
-- ----------------------------
CREATE INDEX "out_trade_no" ON "public"."pay_trade_callback_record" USING btree (
  "out_trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."out_trade_no" IS '通道交易号索引';
CREATE INDEX "trade_no" ON "public"."pay_trade_callback_record" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."trade_no" IS '本地交易号索引';

-- ----------------------------
-- Primary Key structure for table pay_trade_callback_record
-- ----------------------------
ALTER TABLE "public"."pay_trade_callback_record" ADD CONSTRAINT "pay_callback_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_trade_sync_record
-- ----------------------------
ALTER TABLE "public"."pay_trade_sync_record" ADD CONSTRAINT "pay_trade_sync_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_order
-- ----------------------------
CREATE INDEX "transfer_biz_transfer_no" ON "public"."pay_transfer_order" USING btree (
  "biz_transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."transfer_biz_transfer_no" IS '商户转账号索引';
CREATE INDEX "transfer_out_transfer_no" ON "public"."pay_transfer_order" USING btree (
  "out_transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."transfer_out_transfer_no" IS '通道转账号索引';
CREATE INDEX "transfer_transfer_no" ON "public"."pay_transfer_order" USING btree (
  "transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."transfer_transfer_no" IS '转账号索引';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order
-- ----------------------------
ALTER TABLE "public"."pay_transfer_order" ADD CONSTRAINT "pay_transfer_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_audit_login_log
-- ----------------------------
ALTER TABLE "public"."starter_audit_login_log" ADD CONSTRAINT "starter_audit_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_audit_operate_log
-- ----------------------------
ALTER TABLE "public"."starter_audit_operate_log" ADD CONSTRAINT "starter_audit_operate_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_file_platform
-- ----------------------------
ALTER TABLE "public"."starter_file_platform" ADD CONSTRAINT "starter_file_platform_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_file_upload_info
-- ----------------------------
ALTER TABLE "public"."starter_file_upload_info" ADD CONSTRAINT "starter_file_upload_info_pkey" PRIMARY KEY ("id");
