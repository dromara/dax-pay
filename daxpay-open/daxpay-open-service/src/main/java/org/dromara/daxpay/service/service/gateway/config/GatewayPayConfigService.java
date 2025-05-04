package org.dromara.daxpay.service.service.gateway.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.gateway.GatewayPayConfigManager;
import org.dromara.daxpay.service.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.GatewayPayConfigResult;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
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
    public GatewayPayConfigResult getConfig(String appId){
        return gatewayPayConfigManager.findByAppId(appId).map(GatewayPayConfig::toResult)
                .orElse(new GatewayPayConfigResult());
    }

    /**
     * 保存网关支付配置
     */
    public void save(GatewayPayConfigParam param){
        // 判断是否已经存在
        if (gatewayPayConfigManager.existsByAppId(param.getAppId())){
            throw new RepetitiveOperationException("该应用已存在网关支付配置");
        }
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        var entity = GatewayPayConfig.init(param);
        gatewayPayConfigManager.save(entity);
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

}
