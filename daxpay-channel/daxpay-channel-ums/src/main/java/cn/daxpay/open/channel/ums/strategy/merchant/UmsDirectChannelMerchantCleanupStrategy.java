package cn.daxpay.open.channel.ums.strategy.merchant;

import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/// # 银联商务直连通道商户清理策略基类
///
/// UMS 通道有 6 个 product(QRCODE/JSAPI/APP/MINI/H5/BARCODE), 共享同一份扩展表
/// `ums_direct_key_config`, 删除逻辑完全相同。本类封装共享删除逻辑, 由 6 个具体产品子类
/// 各自实现 [cn.daxpay.open.payment.strategy.PaymentStrategy#getProduct] 返回对应
/// [cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum], 注册为独立 Spring Bean。
///
/// 这样设计的原因:每个 channelMerchant 记录的 product 字段是创建时由用户从 6 个中选一个,
/// 删除时按 product 一对一查找策略, 必须每个 product 都有对应实现, 否则该 product 创建的
/// 商户删除时 cleanup 不会触发。
///
/// @see UmsQrcodeChannelMerchantCleanupStrategy
/// @see UmsJsapiChannelMerchantCleanupStrategy
/// @see UmsAppChannelMerchantCleanupStrategy
/// @see UmsMiniChannelMerchantCleanupStrategy
/// @see UmsH5ChannelMerchantCleanupStrategy
/// @see UmsBarcodeChannelMerchantCleanupStrategy
@RequiredArgsConstructor
public abstract class UmsDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    protected final UmsDirectKeyConfigManager umsDirectKeyConfigManager;

    /// 清理指定通道商户号下银联商务直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
