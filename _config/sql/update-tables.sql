-- ============================================================
-- 支付宝转账场景预置数据(补跑段)
-- 表结构与索引(先删后增)已执行成功, 本段仅执行预置数据初始化:
-- 为现有直连通道商户预置8个转账场景行(enabled=false, is_default=false)
-- 场景为支付宝协议固定中文名称, 重复执行时靠场景唯一索引(部分索引)幂等跳过
-- ============================================================

-- ID 用时间戳毫秒*1000000 + 递增序号生成, 与应用运行时 Snowflake ID 范围相近且不冲突
DO $$
DECLARE
    base_id bigint;
    seq bigint := 0;
    mch_record RECORD;
    scene_name text;
BEGIN
    base_id := (extract(epoch from now()) * 1000)::bigint * 1000000;
    FOR mch_record IN
        SELECT mch_no, channel_mch_no
        FROM "public"."alipay_direct_channel_merchant"
        WHERE deleted = false
    LOOP
        FOREACH scene_name IN ARRAY ARRAY['现金营销', '企业退款', '佣金报酬', '业务结算',
                                         '二手回收', '公益补助', '行政补贴和退款', '保险理赔']
        LOOP
            INSERT INTO "public"."alipay_transfer_scene_config"
                ("id", "mch_no", "channel_mch_no", "scene_name", "enabled", "is_default",
                 "create_time", "last_modified_time", "version", "deleted")
            VALUES
                (base_id + seq, mch_record.mch_no, mch_record.channel_mch_no, scene_name, false, false,
                 now(), now(), 0, false)
            ON CONFLICT DO NOTHING;
            seq := seq + 1;
        END LOOP;
    END LOOP;
END $$;
