-- ============================
-- 修正 adapay 产品沙箱支持标志
-- 种子数据 pay_md_product.sandbox=false 与 AdapayDirectProductStrategy.isSandbox()=true 声明不一致,
-- 导致通道商户列表"环境状态"列显示为"-"。ChannelMerchantService 已改为统一读取产品策略, 此处同步修正存量数据。
-- ============================
UPDATE pay_md_product SET sandbox = true WHERE code = 'ada_pay';
