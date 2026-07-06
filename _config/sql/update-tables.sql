-- 拉卡拉通道商户表新增终端号字段
ALTER TABLE lakala_isv_channel_merchant ADD COLUMN IF NOT EXISTS term_no varchar(64);
COMMENT ON COLUMN lakala_isv_channel_merchant.term_no IS '终端号';
