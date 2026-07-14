package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateClientEnvManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateClientEnv;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateClientEnvParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateClientEnvResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/// # 网关聚合扫码配置服务
///
/// 管理聚合扫码配置的主表(配置深度)与客户端环境子表(每环境支付方式/通道),
/// 按 level 校验客户端环境子表填充完整性。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAggregateConfigService {

    private final GatewayAggregateConfigManager configManager;
    private final GatewayAggregateClientEnvManager clientEnvManager;

    /// 按应用查询, 不存在返回空对象(含 level 默认 AUTO)
    public GatewayAggregateConfigResult findByAppId(String appId) {
        return configManager.findByAppId(appId)
                .map(this::toResultWithClientEnvs)
                .orElseGet(() -> new GatewayAggregateConfigResult()
                        .setAppId(appId)
                        .setLevel(AggregateConfigLevelEnum.AUTO.getCode())
                        .setAutoLaunch(false)
                        .setClientEnvs(List.of()));
    }

    /// 支付时必须已配置
    public GatewayAggregateConfig getRequiredByAppId(String appId) {
        return configManager.findByAppId(appId)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateConfigMissing"));
    }

    /// 保存或更新(主表 + 子表替换)
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(GatewayAggregateConfigParam param) {
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(param.getLevel());
        validateClientEnvs(level, param.getClientEnvs());

        // 主表保存或更新
        GatewayAggregateConfig config = configManager.findByAppId(param.getAppId())
                .orElseGet(() -> new GatewayAggregateConfig().setAppId(param.getAppId()));
        config.setLevel(level.getCode());
        config.setAutoLaunch(param.getAutoLaunch());
        if (config.getAutoLaunch() == null) {
            config.setAutoLaunch(false);
        }
        // 新建时需要设置 mchNo(BaseManager 的 save 会通过 MchBaseEntity 的 FieldFill.INSERT 填充)
        if (config.getId() == null) {
            config.setMchNo(param.getMchNo());
        }
        configManager.saveOrUpdate(config);

        // 子表替换: 先删后插
        clientEnvManager.deleteByConfigId(config.getId());
        if (CollUtil.isNotEmpty(param.getClientEnvs())) {
            List<GatewayAggregateClientEnv> clientEnvs = new ArrayList<>();
            for (GatewayAggregateClientEnvParam envParam : param.getClientEnvs()) {
                GatewayAggregateClientEnv env = new GatewayAggregateClientEnv()
                        .setConfigId(config.getId())
                        .setClientEnv(envParam.getClientEnv())
                        .setMethod(envParam.getMethod())
                        .setChannelMchNo(envParam.getChannelMchNo())
                        .setCapability(envParam.getCapability());
                clientEnvs.add(env);
            }
            clientEnvManager.saveAll(clientEnvs);
        }
    }

    /// 按 level 校验客户端环境子表填充完整性
    private void validateClientEnvs(AggregateConfigLevelEnum level, List<GatewayAggregateClientEnvParam> clientEnvs) {
        if (level == AggregateConfigLevelEnum.AUTO) {
            // AUTO 模式不校验子表
            return;
        }
        if (CollUtil.isEmpty(clientEnvs)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.aggregateClientEnvsRequired");
        }
        for (GatewayAggregateClientEnvParam env : clientEnvs) {
            if (StrUtil.isBlank(env.getClientEnv())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateClientEnvBlank");
            }
            if (level == AggregateConfigLevelEnum.METHOD && StrUtil.isBlank(env.getMethod())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateClientEnvMethodRequired");
            }
            if (level == AggregateConfigLevelEnum.DIRECT) {
                if (StrUtil.isBlank(env.getChannelMchNo())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.aggregateClientEnvChannelMchRequired");
                }
                if (StrUtil.isBlank(env.getCapability())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.aggregateClientEnvCapabilityRequired");
                }
            }
        }
    }

    /// 组装主表 + 客户端环境子表为 Result
    private GatewayAggregateConfigResult toResultWithClientEnvs(GatewayAggregateConfig entity) {
        GatewayAggregateConfigResult result = new GatewayAggregateConfigResult();
        BeanUtil.copyProperties(entity, result);
        List<GatewayAggregateClientEnv> clientEnvs = clientEnvManager.findByConfigId(entity.getId());
        List<GatewayAggregateClientEnvResult> envResults = clientEnvs.stream()
                .map(s -> new GatewayAggregateClientEnvResult()
                        .setClientEnv(s.getClientEnv())
                        .setMethod(s.getMethod())
                        .setChannelMchNo(s.getChannelMchNo())
                        .setCapability(s.getCapability()))
                .toList();
        result.setClientEnvs(envResults);
        return result;
    }
}
