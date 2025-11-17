package org.dromara.daxpay.payment.merchant.service.gateway;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.merchant.convert.gateway.GatewayPayReadConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.GatewayPayReadConfigManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.GatewayPayReadConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2025/10/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayReadConfigService {
    private final GatewayPayReadConfigManager gatewayPayReadConfigManager;
    private final MchAppManager mchAppManager;


    /**
     * 获取网关支付读取配置
     */
    public GatewayPayReadConfig findByAppId(String appId){
        var configOptional = gatewayPayReadConfigManager.findByAppId(appId);
        if (configOptional.isEmpty()){
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var entity = new GatewayPayReadConfig()
                    .setGatewayReadSystem(true)
                    .setH5ReadSystem(true)
                    .setPcReadSystem(true)
                    .setAggregateQrReadSystem(true)
                    .setAggregateBarReadSystem(true)
                    .setMiniQuicklyReadSystem(true);
            entity.setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            gatewayPayReadConfigManager.save(entity);
            return entity;
        }
        return configOptional.get();
    }

    /**
     * 更新
     */
    public void update(GatewayPayReadConfigParam entity){
        var config = this.findByAppId(entity.getAppId());
        GatewayPayReadConvert.CONVERT.copy(entity, config);
        gatewayPayReadConfigManager.updateById(config);
    }
}
