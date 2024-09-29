package cn.daxpay.multi.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.config.ChannelCashierConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelCashierConfigManage extends BaseManager<ChannelCashierConfigMapper, ChannelCashierConfig> {

    /**
     * 根据类型查询
     */
    public Optional<ChannelCashierConfig> findByCashierType(String cashierType) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return lambdaQuery().eq(ChannelCashierConfig::getCashierType, cashierType)
                .eq(ChannelCashierConfig::getMchNo, mchAppInfo.getMchNo())
                .eq(ChannelCashierConfig::getAppId, mchAppInfo.getAppId())
                .oneOpt();
    }
}
