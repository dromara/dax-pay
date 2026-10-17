package cn.daxpay.open.channel.ysep.strategy.merchant;

import cn.daxpay.open.channel.ysep.dao.YsepKeyConfigManager;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 银盛通道商户清理策略
///
/// 在通道商户删除时清理银盛的所有扩展数据(直连密钥配置表, 一通道两产品共用)。
///
/// 注意: 银盛一通道两产品(ysep_pay / yseqt_pay)共用一张密钥表, 本策略仅注册 YSEP_PAY 产品;
/// YSEQT_PAY 商户与 YSEP_PAY 商户同属 ysep_pay 通道, 通道商户删除走通道维度清理, 无需重复注册。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final YsepKeyConfigManager ysepKeyConfigManager;

    /// 对应产品: 银盛(通道 ysep_pay 下两产品共用清理逻辑)
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YSEP_PAY;
    }

    /// 清理指定通道商户号下银盛的所有扩展数据(密钥配置表)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        ysepKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
