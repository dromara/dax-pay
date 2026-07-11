package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 网关聚合扫码配置服务
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAggregateConfigService {

    private final GatewayAggregateConfigManager configManager;

    /// 按应用查询, 不存在返回空对象字段
    public GatewayAggregateConfigResult findByAppId(String appId) {
        return configManager.findByAppId(appId)
                .map(this::toResult)
                .orElseGet(() -> new GatewayAggregateConfigResult().setAppId(appId));
    }

    /// 支付时必须已配置
    public GatewayAggregateConfig getRequiredByAppId(String appId) {
        return configManager.findByAppId(appId)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateConfigMissing"));
    }

    /// 保存或更新
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(GatewayAggregateConfigParam param) {
        var existing = configManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            GatewayAggregateConfig entity = existing.get();
            BeanUtil.copyProperties(param, entity, "id", "appId", "mchNo", "version", "deleted");
            configManager.updateById(entity);
        } else {
            GatewayAggregateConfig entity = new GatewayAggregateConfig();
            BeanUtil.copyProperties(param, entity);
            configManager.save(entity);
        }
    }

    private GatewayAggregateConfigResult toResult(GatewayAggregateConfig entity) {
        GatewayAggregateConfigResult result = new GatewayAggregateConfigResult();
        BeanUtil.copyProperties(entity, result);
        return result;
    }
}
