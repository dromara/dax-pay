package cn.daxpay.open.payment.common.check.checker.merchant;

import cn.daxpay.open.payment.common.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 通道商户检查器
///
/// 检测商户下是否存在启用状态的通道商户。无启用通道商户则视为未配置。
@Component
@RequiredArgsConstructor
public class ChannelMerchantChecker implements MerchantConfigChecker {

    private final ChannelMerchantManager channelMerchantManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.CHANNEL_MERCHANT;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        List<ChannelMerchant> channels = channelMerchantManager.findAllByMchNo(mchNo);
        // 无通道商户 或 无启用的通道商户 => 告警
        long enabledCount = channels.stream()
                .filter(ChannelMerchant::getEnable)
                .count();
        if (enabledCount == 0) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.CHANNEL_MERCHANT,
                    ConfigCheckCategoryEnum.CHANNEL_MERCHANT.getCode(),
                    "configCheck.channelMerchant.title",
                    "configCheck.channelMerchant.description",
                    "ChannelMerchant"
            ).setCount(channels.size());
        }
        return null;
    }
}
