package cn.daxpay.open.channel.umfpay.strategy.merchant;

import cn.daxpay.open.channel.umfpay.dao.direct.UmfpayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 联动优势直连通道商户清理策略
///
/// 在通道商户删除时清理联动优势直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final UmfpayDirectKeyConfigManager umfpayDirectKeyConfigManager;

    /// 对应产品: 联动优势
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMF_PAY;
    }

    /// 清理指定通道商户号下联动优势直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        umfpayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
