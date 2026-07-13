package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateSceneManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateScene;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateSceneParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateSceneResult;
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
/// 管理聚合扫码配置的主表(配置深度)与场景子表(每场景支付方式/通道),
/// 按 level 校验场景子表填充完整性。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAggregateConfigService {

    private final GatewayAggregateConfigManager configManager;
    private final GatewayAggregateSceneManager sceneManager;

    /// 按应用查询, 不存在返回空对象(含 level 默认 AUTO)
    public GatewayAggregateConfigResult findByAppId(String appId) {
        return configManager.findByAppId(appId)
                .map(this::toResultWithScenes)
                .orElseGet(() -> new GatewayAggregateConfigResult()
                        .setAppId(appId)
                        .setLevel(AggregateConfigLevelEnum.AUTO.getCode())
                        .setAutoLaunch(false)
                        .setScenes(List.of()));
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
        validateScenes(level, param.getScenes());

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
        sceneManager.deleteByConfigId(config.getId());
        if (CollUtil.isNotEmpty(param.getScenes())) {
            List<GatewayAggregateScene> scenes = new ArrayList<>();
            for (GatewayAggregateSceneParam sceneParam : param.getScenes()) {
                GatewayAggregateScene scene = new GatewayAggregateScene()
                        .setConfigId(config.getId())
                        .setScene(sceneParam.getScene())
                        .setMethod(sceneParam.getMethod())
                        .setChannelMchNo(sceneParam.getChannelMchNo())
                        .setCapability(sceneParam.getCapability());
                scenes.add(scene);
            }
            sceneManager.saveAll(scenes);
        }
    }

    /// 按 level 校验场景子表填充完整性
    private void validateScenes(AggregateConfigLevelEnum level, List<GatewayAggregateSceneParam> scenes) {
        if (level == AggregateConfigLevelEnum.AUTO) {
            // AUTO 模式不校验子表
            return;
        }
        if (CollUtil.isEmpty(scenes)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.aggregateScenesRequired");
        }
        for (GatewayAggregateSceneParam scene : scenes) {
            if (StrUtil.isBlank(scene.getScene())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateSceneBlank");
            }
            if (level == AggregateConfigLevelEnum.METHOD && StrUtil.isBlank(scene.getMethod())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.aggregateSceneMethodRequired");
            }
            if (level == AggregateConfigLevelEnum.DIRECT) {
                if (StrUtil.isBlank(scene.getChannelMchNo())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.aggregateSceneChannelMchRequired");
                }
                if (StrUtil.isBlank(scene.getCapability())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.aggregateSceneCapabilityRequired");
                }
            }
        }
    }

    /// 组装主表 + 场景子表为 Result
    private GatewayAggregateConfigResult toResultWithScenes(GatewayAggregateConfig entity) {
        GatewayAggregateConfigResult result = new GatewayAggregateConfigResult();
        BeanUtil.copyProperties(entity, result);
        List<GatewayAggregateScene> scenes = sceneManager.findByConfigId(entity.getId());
        List<GatewayAggregateSceneResult> sceneResults = scenes.stream()
                .map(s -> new GatewayAggregateSceneResult()
                        .setScene(s.getScene())
                        .setMethod(s.getMethod())
                        .setChannelMchNo(s.getChannelMchNo())
                        .setCapability(s.getCapability()))
                .toList();
        result.setScenes(sceneResults);
        return result;
    }
}
