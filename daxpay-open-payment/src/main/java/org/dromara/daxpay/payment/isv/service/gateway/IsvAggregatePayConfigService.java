package org.dromara.daxpay.payment.isv.service.gateway;

import org.dromara.daxpay.payment.isv.convert.gateway.IsvAggregatePayConfigConvert;
import org.dromara.daxpay.payment.isv.dao.gateway.IsvAggregateBarPayConfigManager;
import org.dromara.daxpay.payment.isv.dao.gateway.IsvAggregatePayConfigManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregateBarPayConfig;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregatePayConfig;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.payment.isv.param.gateway.IsvAggregatePayConfigParam;
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
public class IsvAggregatePayConfigService {
    private final IsvAggregatePayConfigManager aggregatePayConfigManager;
    private final IsvAggregateBarPayConfigManager aggregateBarPayConfigManager;

    /**
     * 聚合扫码支付配置
     */
    public IsvAggregatePayConfig findPayByIsvNo(String isvNo){
        var optional = aggregatePayConfigManager.findByIsvNo(isvNo);
        if (optional.isEmpty()){
            var aggregatePayConfig = new IsvAggregatePayConfig()
                    .setIsvNo(isvNo);
            aggregatePayConfigManager.save(aggregatePayConfig);
            return aggregatePayConfig;
        }
        return optional.get();
    }

    /**
     *更新聚合支付配置
     */
    public void updateQrConfig(IsvAggregatePayConfigParam param){
        var config = this.findPayByIsvNo(param.getIsvNo());
        IsvAggregatePayConfigConvert.CONVERT.copy(param, config);
        aggregatePayConfigManager.updateById(config);
    }

    /**
     * 聚合付款码配置详情
     */
    public IsvAggregateBarPayConfig findBarByNo(String isvNo){
        var optional = aggregateBarPayConfigManager.findByIsvNo(isvNo);
        if (optional.isEmpty()){
            var barPayConfig = new IsvAggregateBarPayConfig()
                    .setIsvNo(isvNo);
            aggregateBarPayConfigManager.save(barPayConfig);
            return barPayConfig;
        }
        return optional.get();
    }

    /**
     * 更新聚合付款码支付配置
     */
    public void updateBarConfig(IsvAggregateBarPayConfigParam param){
        var barPayConfig = this.findBarByNo(param.getIsvNo());
        IsvAggregatePayConfigConvert.CONVERT.copy(param, barPayConfig);
        aggregateBarPayConfigManager.updateById(barPayConfig);
    }
}
