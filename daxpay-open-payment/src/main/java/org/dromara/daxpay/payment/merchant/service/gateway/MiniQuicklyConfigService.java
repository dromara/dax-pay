package org.dromara.daxpay.payment.merchant.service.gateway;

import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
import org.dromara.daxpay.payment.isv.service.gateway.IsvMiniQuicklyConfigService;
import org.dromara.daxpay.payment.merchant.convert.gateway.MiniQuicklyConfigConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.MiniQuicklyConfigManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.MiniQuicklyConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.MiniQuicklyConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiniQuicklyConfigService {
    private final MiniQuicklyConfigManager miniQuicklyConfigManager;
    private final MchAppManager mchAppManager;
    private final IsvMiniQuicklyConfigService isvMiniQuicklyConfigService;

    /**
     * 聚合扫码支付配置
     */
    public MiniQuicklyConfig findConfigByAppId(String appId){
        var optional = miniQuicklyConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            var app = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotExistException("商户应用不存在"));
            var qrPayConfig = new MiniQuicklyConfig();
            qrPayConfig.setAppId(appId)
                    .setMchNo(app.getMchNo())
                    .setIsvNo(app.getIsvNo());
            miniQuicklyConfigManager.save(qrPayConfig);
            return qrPayConfig;
        }
        return optional.get();
    }

    /**
     *更新小程序支付配置
     */
    public void updateConfig(MiniQuicklyConfigParam param){
        var config = this.findConfigByAppId(param.getAppId());
        MiniQuicklyConfigConvert.CONVERT.copy(param, config);
        miniQuicklyConfigManager.updateById(config);
    }

    /**
     * 获取配置
     */
    public MiniQuicklyConfig getConfig(String appId, GatewayPayReadConfig readConfig){
        var quicklyConfig = this.findConfigByAppId(appId);
        if (readConfig.isMiniQuicklyReadSystem()){
            var isvQuicklyConfig = isvMiniQuicklyConfigService.findByIsvNo(readConfig.getIsvNo());
            MiniQuicklyConfigConvert.CONVERT.copy(isvQuicklyConfig, quicklyConfig);
            return quicklyConfig;
        } else {
            return quicklyConfig;
        }
    }
}
