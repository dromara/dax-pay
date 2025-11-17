package org.dromara.daxpay.payment.merchant.service.gateway;

import org.dromara.daxpay.payment.isv.service.gateway.IsvGatewayPayConfigService;
import org.dromara.daxpay.payment.merchant.convert.gateway.GatewayPayConfigConvert;
import org.dromara.daxpay.payment.merchant.dao.gateway.GatewayPayConfigManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayConfigService {
    private final GatewayPayConfigManager gatewayPayConfigManager;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;
    private final PaymentAssistService paymentAssistService;
    private final IsvGatewayPayConfigService isvGatewayPayConfigService;

    /**
     * 获取网关支付配置
     */
    public GatewayPayConfig findByAppId(String appId){
        var configOptional = gatewayPayConfigManager.findByAppId(appId);
        if (configOptional.isEmpty()){
            paymentAssistService.initMchAndApp(appId);
            var entity = new GatewayPayConfig()
                    .setAggregateQrShow(true)
                    .setH5AutoUpgrade(true);
            gatewayPayConfigManager.save(entity);
            return entity;
        } else {
            return configOptional.get();
        }
    }

    /**
     * 更新网关支付配置
     */
    public void update(GatewayPayConfigParam param){
        GatewayPayConfig config = this.findByAppId(param.getAppId());
        GatewayPayConfigConvert.CONVERT.copy(param, config);
        gatewayPayConfigManager.updateById(config);
    }

    /**
     * 获取网关支付配置, 根据配置使用自定义或读取服务商配置
     */
    public GatewayPayConfig getGatewayConfig(String appId){
        var readConfig = gatewayPayReadConfigService.findByAppId(appId);
        // 是否需要读取服务商配置
        if (readConfig.isGatewayReadSystem()){
            var isvGatewayPayConfig = isvGatewayPayConfigService.getConfig(readConfig.getIsvNo());
            return GatewayPayConfigConvert.CONVERT.toEntity(isvGatewayPayConfig);
        }
        return this.findByAppId(appId);
    }
}
