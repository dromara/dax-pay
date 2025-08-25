package org.dromara.daxpay.service.merchant.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.merchant.dao.gateway.AggregateBarPayConfigManager;
import org.dromara.daxpay.service.merchant.dao.gateway.AggregatePayConfigManager;
import org.dromara.daxpay.service.merchant.entity.gateway.AggregateBarPayConfig;
import org.dromara.daxpay.service.merchant.entity.gateway.AggregatePayConfig;
import org.dromara.daxpay.service.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.merchant.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.service.merchant.param.gateway.AggregatePayConfigParam;
import org.dromara.daxpay.service.merchant.result.gateway.AggregateBarPayConfigResult;
import org.dromara.daxpay.service.merchant.result.gateway.AggregatePayConfigResult;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateConfigService {
    private final AggregatePayConfigManager aggregatePayConfigManager;
    private final AggregateBarPayConfigManager aggregateBarPayConfigManager;
    private final PaymentAssistService paymentAssistService;

    /**
     * 支付配置列表
     */
    public List<AggregatePayConfigResult> listByPay(String appId){
        return MpUtil.toListResult(aggregatePayConfigManager.findAllByAppId(appId));
    }

    /**
     * 付款码配置列表
     */
    public List<AggregateBarPayConfigResult> listByBar(String appId){
        return MpUtil.toListResult(aggregateBarPayConfigManager.findAllByAppId(appId));
    }

    /**
     * 支付配置详情
     */
    public AggregatePayConfigResult findPayById(Long id){
        return aggregatePayConfigManager.findById(id).map(AggregatePayConfig::toResult).orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
    }

    /**
     * 付款码配置详情
     */
    public AggregateBarPayConfigResult findBarById(Long id){
        return aggregateBarPayConfigManager.findById(id).map(AggregateBarPayConfig::toResult).orElseThrow(() -> new DataNotExistException("聚合付款码支付不存在"));
    }

    /**
     *保存聚合支付配置
     */
    public void savePay(AggregatePayConfigParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getReqInfo();
        if (aggregatePayConfigManager.existsByAppIdAndType(param.getAppId(), param.getAggregateType())){
            throw new OperationFailException("聚合支付配置已存在");
        }
        var entity = AggregatePayConfig.init(param);
        entity.setMchNo(mchApp.getMchNo());
        aggregatePayConfigManager.save(entity);
    }

    /**
     *保存聚合付款码支付配置
     */
    public void saveBar(AggregateBarPayConfigParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        if (aggregateBarPayConfigManager.existsByAppIdAndType(param.getAppId(), param.getBarPayType())){
            throw new OperationFailException("聚合付款码支付配置已存在");
        }
        aggregateBarPayConfigManager.save(AggregateBarPayConfig.init(param));
    }

    /**
     *更新聚合支付配置
     */
    public void updatePay(AggregatePayConfigParam param){
        var payConfig = aggregatePayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
        if (!Objects.equals(param.getAggregateType(), payConfig.getAggregateType()) && aggregatePayConfigManager.existsByAppIdAndType(param.getAppId(), param.getAggregateType())){
            throw new OperationFailException("聚合支付类型已存在");
        }
        BeanUtil.copyProperties(param, payConfig, CopyOptions.create().ignoreNullValue());
        aggregatePayConfigManager.updateById(payConfig);
    }

    /**
     *更新聚合付款码支付配置
     */
    public void updateBar(AggregateBarPayConfigParam param){
        var barPayConfig = aggregateBarPayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("聚合付款码支付配置不存在"));
        if (!Objects.equals(param.getBarPayType(), barPayConfig.getBarPayType()) && aggregateBarPayConfigManager.existsByAppIdAndType(param.getAppId(), param.getBarPayType())){
            throw new OperationFailException("聚合付款码支付类型已存在");
        }
        BeanUtil.copyProperties(param, barPayConfig, CopyOptions.create().ignoreNullValue());
        aggregateBarPayConfigManager.updateById(barPayConfig);
    }

    /**
     *删除聚合支付配置
     */
    public void deletePay(Long id){
        aggregatePayConfigManager.deleteById(id);
    }

    /**
     *删除聚合付款码支付配置
     */
    public void deleteBar(Long id){
        aggregateBarPayConfigManager.deleteById(id);
    }

    /**
     * 根据应用ID和类型查询支付配置是否存在
     */
    public boolean existsPay(String appId, String type){
        return aggregatePayConfigManager.existsByAppIdAndType(appId, type);
    }

    /**
     * 根据应用ID和类型查询支付配置是否存在
     */
    public boolean existsPay(String appId, String type, Long id){
        return aggregatePayConfigManager.existsByAppIdAndType(appId, type, id);
    }

    /**
     * 根据应用ID和类型查询付款码支付配置是否存在
     */
    public boolean existsBar(String appId, String type){
        return aggregateBarPayConfigManager.existsByAppIdAndType(appId, type);
    }

    /**
     * 根据应用ID和类型查询付款码支付配置是否存在
     */
    public boolean existsBar(String appId, String type, Long id){
        return aggregateBarPayConfigManager.existsByAppIdAndType(appId, type, id);
    }

    /**
     * 获取聚合支付配置
     */
    public AggregatePayConfig getAggregatePayConfig(String appId, String aggregateType, GatewayPayConfig gatewayPayConfig) {
        return aggregatePayConfigManager.findByAppIdAndType(appId, aggregateType)
                .orElseThrow(() -> new ConfigNotExistException("聚合支付配置项不存在"));
    }

    /**
     * 获取聚合付款码
     */
    public List<AggregateBarPayConfig> getAggregateBarPayConfigs(String appId, String barPayType, GatewayPayConfig gatewayPayConfig) {
        return aggregateBarPayConfigManager.findByAppIdAndType(appId, barPayType);
    }

}
