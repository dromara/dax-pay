package org.dromara.daxpay.payment.merchant.service.gateway;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.merchant.convert.gateway.CashierCodeConfigConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.CashierCodeConfigManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.CashierCodeConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 收银码牌配置
 *
 * @author xxm
 * @since 2025/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeConfigService {

    private final CashierCodeConfigManager cashierCodeConfigManager;

    private final MchAppManager mchAppManager;

    /**
     * 添加
     */
    public CashierCodeConfig findByAppId(String appId) {
        var optional = cashierCodeConfigManager.findByAppId(appId);
        if (optional.isEmpty()) {
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var payConfig = new CashierCodeConfig();
            payConfig.setAppId(mchApp.getAppId())
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            cashierCodeConfigManager.save(payConfig);
            return payConfig;
        }
        return optional.get();
    }

    /**
     * 更新
     */
    public void update(CashierCodeConfigParam param) {
        var channelCashierConfig = this.findByAppId(param.getAppId());
        CashierCodeConfigConvert.CONVERT.copy(param, channelCashierConfig);
        cashierCodeConfigManager.updateById(channelCashierConfig);
    }
}
