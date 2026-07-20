package cn.daxpay.open.channel.ums.strategy.merchant;

import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(公众号)通道商户清理策略
///
/// 对应 [ProductEnum#UMS_JSAPI], 共享 [UmsDirectChannelMerchantCleanupStrategy] 的删除逻辑。
@Service
public class UmsJsapiChannelMerchantCleanupStrategy extends UmsDirectChannelMerchantCleanupStrategy {

    public UmsJsapiChannelMerchantCleanupStrategy(UmsDirectKeyConfigManager umsDirectKeyConfigManager) {
        super(umsDirectKeyConfigManager);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_JSAPI;
    }
}
