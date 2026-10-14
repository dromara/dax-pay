-- ----------------------------
-- 增量表结构脚本(上一版本 -> 当前版本)
-- 发版时本文件无需改动, 保持空白即可; 下一版本周期内的表结构变化再追加于此
-- ----------------------------

-- 2026-10-14 补建用户协议条款项表 (UserProtocolItem 实体与 /user/protocol/item 接口此前已存在, 表结构缺失, 全新建表对存量数据无影响)
CREATE TABLE public.base_user_protocol_item (
    id bigint NOT NULL,
    protocol_id bigint NOT NULL,
    sort_no double precision,
    content text NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.base_user_protocol_item IS '用户协议条款项';
COMMENT ON COLUMN public.base_user_protocol_item.id IS '主键';
COMMENT ON COLUMN public.base_user_protocol_item.protocol_id IS '协议ID';
COMMENT ON COLUMN public.base_user_protocol_item.sort_no IS '排序号';
COMMENT ON COLUMN public.base_user_protocol_item.content IS '协议内容';
COMMENT ON COLUMN public.base_user_protocol_item.creator IS '创建者ID';
COMMENT ON COLUMN public.base_user_protocol_item.create_time IS '创建时间';
COMMENT ON COLUMN public.base_user_protocol_item.last_modifier IS '最后修改ID';
COMMENT ON COLUMN public.base_user_protocol_item.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.base_user_protocol_item.version IS '版本号, 使用乐观锁';
COMMENT ON COLUMN public.base_user_protocol_item.deleted IS '逻辑删除标志';

ALTER TABLE ONLY public.base_user_protocol_item
    ADD CONSTRAINT base_user_protocol_item_pkey PRIMARY KEY (id);

CREATE INDEX idx_user_protocol_item_protocol ON public.base_user_protocol_item USING btree (protocol_id);
COMMENT ON INDEX public.idx_user_protocol_item_protocol IS '按协议 ID 查询条款项列表';

-- 2026-10-14 删除支付宝应用授权认证配置两张表 (该配置仅存 user_id_type 字段且从不参与运行时请求, 属死配置; 同步删除前后端代码与页面, 存量数据无需保留)
DROP TABLE IF EXISTS public.alipay_direct_app_auth_config;
DROP TABLE IF EXISTS public.alipay_isv_app_auth_config;
