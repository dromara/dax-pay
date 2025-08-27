package org.dromara.daxpay.service.merchant.service.gateway;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.service.merchant.dao.gateway.GatewayPayConfigManager;
import org.dromara.daxpay.service.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.merchant.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
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
    private final PaymentAssistService paymentAssistService;

    /**
     * 获取网关支付配置
     */
    public GatewayPayConfig findByAppId(String appId){
        var configOptional = gatewayPayConfigManager.findByAppId(appId);
        if (configOptional.isEmpty()){
            paymentAssistService.initMchAndApp(appId);
            var mchApp = PaymentContextLocal.get().getReqInfo();
            // 特约商户
            var entity = new GatewayPayConfig().setReadSystem(true);
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
        var gatewayPayConfig = gatewayPayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银台网关支付不存在"));
        BeanUtil.copyProperties(param, gatewayPayConfig);
        gatewayPayConfigManager.updateById(gatewayPayConfig);
    }

    /**
     * 获取网关支付配置, 根据配置使用自定义或读取服务商配置
     */
    public GatewayPayConfig getGatewayConfig(String appId){
        return this.findByAppId(appId);
    }

}
