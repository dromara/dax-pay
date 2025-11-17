package org.dromara.daxpay.payment.merchant.service.gateway;

import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
import org.dromara.daxpay.payment.isv.service.gateway.IsvAggregatePayConfigService;
import org.dromara.daxpay.payment.merchant.convert.gateway.AggregatePayConfigConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.AggregateBarPayConfigManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.AggregateQrPayConfigManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateBarPayConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateQrPayConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.payment.merchant.param.gateway.AggregateQrPayConfigParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateConfigService {
    private final AggregateQrPayConfigManager aggregateQrPayConfigManager;
    private final AggregateBarPayConfigManager aggregateBarPayConfigManager;
    private final IsvAggregatePayConfigService isvAggregatePayConfigService;
    private final MchAppManager mchAppManager;

    /**
     * 聚合扫码支付配置
     */
    public AggregateQrPayConfig findQrConfigByAppId(String appId){
        var optional = aggregateQrPayConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            var app = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotExistException("商户应用不存在"));
            var qrPayConfig = new AggregateQrPayConfig();
            qrPayConfig.setAppId(appId)
                    .setMchNo(app.getMchNo())
                    .setIsvNo(app.getIsvNo());
            aggregateQrPayConfigManager.save(qrPayConfig);
            return qrPayConfig;
        }
        return optional.get();
    }

    /**
     *更新聚合支付配置
     */
    public void updateQrConfig(AggregateQrPayConfigParam param){
        var config = this.findQrConfigByAppId(param.getAppId());
        AggregatePayConfigConvert.CONVERT.copy(param, config);
        aggregateQrPayConfigManager.updateById(config);
    }

    /**
     * 聚合付款码配置详情
     */
    public AggregateBarPayConfig findBarConfigByAppId(String appId){
        var optional = aggregateBarPayConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            var app = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotExistException("商户应用不存在"));
            var barPayConfig = new AggregateBarPayConfig();
            barPayConfig.setAppId(appId)
                    .setMchNo(app.getMchNo())
                    .setIsvNo(app.getIsvNo());
            aggregateBarPayConfigManager.save(barPayConfig);
            return barPayConfig;
        }
        return optional.get();
    }

    /**
     * 更新聚合付款码支付配置
     */
    public void updateBarConfig(AggregateBarPayConfigParam param){
        var barPayConfig = this.findBarConfigByAppId(param.getAppId());
        AggregatePayConfigConvert.CONVERT.copy(param, barPayConfig);
        aggregateBarPayConfigManager.updateById(barPayConfig);
    }

    /**
     * 获取聚合扫码支付配置
     */
    public AggregateQrPayConfig getQrPayConfig(String appId, GatewayPayReadConfig readConfig) {
        // 使用自定义配置还是读取服务商配置
        if (readConfig.isAggregateQrReadSystem()){
            return AggregatePayConfigConvert.CONVERT.toEntity(isvAggregatePayConfigService.findPayByIsvNo(readConfig.getIsvNo()));
        } else {
            return aggregateQrPayConfigManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotExistException("聚合扫码支付配置项不存在"));
        }
    }

    /**
     * 获取聚合付款码配置
     */
    public AggregateBarPayConfig getBarPayConfig(String appId, GatewayPayReadConfig readConfig) {
        // 使用自定义配置还是读取服务商配置
        if (readConfig.isAggregateBarReadSystem()) {
            return AggregatePayConfigConvert.CONVERT.toEntity(isvAggregatePayConfigService.findBarByNo(readConfig.getIsvNo()));
        } else {
            return aggregateBarPayConfigManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotExistException("聚合扫码支付配置项不存在"));
        }
    }

}
