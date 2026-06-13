-- 支付产品配置表 产品配置状态存储
DROP TABLE IF EXISTS pay_product_config;
CREATE TABLE pay_product_config (
    id int8 NOT NULL,
    product varchar(32) COLLATE pg_catalog.default NOT NULL,
    channel varchar(32) COLLATE pg_catalog.default NOT NULL,
    active_env varchar(32) COLLATE pg_catalog.default DEFAULT 'prod',
    configured bool DEFAULT false,
    remark varchar(255) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE pay_product_config IS '支付产品配置';
COMMENT ON COLUMN pay_product_config.id IS '主键';
COMMENT ON COLUMN pay_product_config.product IS '产品编码';
COMMENT ON COLUMN pay_product_config.channel IS '通道编码';
COMMENT ON COLUMN pay_product_config.active_env IS '生效环境: prod/sandbox';
COMMENT ON COLUMN pay_product_config.configured IS '是否已配置参数';
COMMENT ON COLUMN pay_product_config.remark IS '备注';
COMMENT ON COLUMN pay_product_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_product_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_product_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN pay_product_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_product_config.version IS '版本号';
COMMENT ON COLUMN pay_product_config.deleted IS '删除标志';

ALTER TABLE pay_product_config ADD CONSTRAINT pk_pay_product_config PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_ppc_product ON pay_product_config(product) WHERE deleted = false;
