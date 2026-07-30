package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayPayClientEnvParam;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayPayConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayPayClientEnvResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayPayConfigResult;
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

/// # 网关支付配置服务(码牌/聚合共用)
///
/// 管理主表(level + autoLaunch)与子表(clientEnv × payForm)。
/// 一个应用一份配置, 码牌支付与聚合扫码共用。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayConfigService {

    private final GatewayPayConfigManager configManager;
    private final GatewayPayClientEnvManager clientEnvManager;

    /// 按应用查询, 不存在返回空对象(level 默认 AUTO)
    public GatewayPayConfigResult findByAppId(String appId) {
        return configManager.findByAppId(appId)
                .map(this::toResultWithClientEnvs)
                .orElseGet(() -> new GatewayPayConfigResult()
                        .setAppId(appId)
                        .setLevel(AggregateConfigLevelEnum.AUTO.getCode())
                        .setAutoLaunch(false)
                        .setClientEnvs(List.of()));
    }

    /// 保存或更新(主表 + 子表替换)
    ///
    /// METHOD/DIRECT 支持**部分配置**: 只落库已填写的环境×形态, 未配组合支付时再报错。
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(GatewayPayConfigParam param) {
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(param.getLevel());
        List<GatewayPayClientEnvParam> filledEnvs = filterFilledClientEnvs(level, param.getClientEnvs());
        validateClientEnvs(level, filledEnvs);

        // 主表保存或更新
        GatewayPayConfig config = configManager.findByAppId(param.getAppId())
                .orElseGet(() -> new GatewayPayConfig().setAppId(param.getAppId()));
        config.setLevel(level.getCode());
        config.setAutoLaunch(param.getAutoLaunch());
        if (config.getAutoLaunch() == null) {
            config.setAutoLaunch(false);
        }
        // 运营端无商户上下文, 主表/子表均需显式 mchNo(MchBaseEntity insert 填充依赖上下文会失败)
        if (StrUtil.isBlank(config.getMchNo())) {
            config.setMchNo(param.getMchNo());
        }
        if (StrUtil.isBlank(config.getMchNo())) {
            // 网关: 商户上下文未装载
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.assist.mchContextMissing");
        }
        configManager.saveOrUpdate(config);

        // 子表替换: 先删后插(仅写入已填环境×形态)
        clientEnvManager.deleteByConfigId(config.getId());
        if (CollUtil.isNotEmpty(filledEnvs)) {
            String mchNo = config.getMchNo();
            List<GatewayPayClientEnv> rows = new ArrayList<>();
            for (GatewayPayClientEnvParam envParam : filledEnvs) {
                GatewayPayClientEnv env = new GatewayPayClientEnv()
                        .setConfigId(config.getId())
                        .setClientEnv(envParam.getClientEnv())
                        .setPayForm(envParam.getPayForm())
                        .setMethod(envParam.getMethod())
                        .setChannelMchNo(envParam.getChannelMchNo())
                        .setCapability(envParam.getCapability());
                // 父类 MchBaseEntity#setMchNo 链式返回类型非本类; 运营端无上下文须显式赋值
                env.setMchNo(mchNo);
                rows.add(env);
            }
            clientEnvManager.saveAll(rows);
        }
    }

    /// 过滤出已填写的环境×形态行(配多少存多少)
    private List<GatewayPayClientEnvParam> filterFilledClientEnvs(
            AggregateConfigLevelEnum level, List<GatewayPayClientEnvParam> clientEnvs) {
        if (level == AggregateConfigLevelEnum.AUTO || CollUtil.isEmpty(clientEnvs)) {
            return List.of();
        }
        List<GatewayPayClientEnvParam> filled = new ArrayList<>();
        for (GatewayPayClientEnvParam env : clientEnvs) {
            if (env == null || StrUtil.isBlank(env.getClientEnv()) || StrUtil.isBlank(env.getPayForm())) {
                continue;
            }
            if (level == AggregateConfigLevelEnum.METHOD) {
                if (StrUtil.isNotBlank(env.getMethod())) {
                    filled.add(env);
                }
                continue;
            }
            if (StrUtil.isNotBlank(env.getChannelMchNo()) || StrUtil.isNotBlank(env.getCapability())) {
                filled.add(env);
            }
        }
        return filled;
    }

    /// 按 level 校验已填行(不要求四环境×两形态齐)
    private void validateClientEnvs(AggregateConfigLevelEnum level, List<GatewayPayClientEnvParam> filledEnvs) {
        if (level == AggregateConfigLevelEnum.AUTO) {
            return;
        }
        // METHOD/DIRECT 至少配置一行
        if (CollUtil.isEmpty(filledEnvs)) {
            // 网关: 请至少配置一项环境与形态
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvsRequired");
        }
        for (GatewayPayClientEnvParam env : filledEnvs) {
            // 校验 payForm 合法
            CodePayFormEnum.findByCode(env.getPayForm());
            if (level == AggregateConfigLevelEnum.METHOD && StrUtil.isBlank(env.getMethod())) {
                // 网关: 已填写的环境与形态须选择支付方式
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.clientEnvMethodRequired");
            }
            if (level == AggregateConfigLevelEnum.DIRECT) {
                if (StrUtil.isBlank(env.getChannelMchNo())) {
                    // 网关: 直接指定时已填写行的通道商户号必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvChannelMchRequired");
                }
                if (StrUtil.isBlank(env.getCapability())) {
                    // 网关: 直接指定时已填写行的支付能力必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvCapabilityRequired");
                }
            }
        }
    }

    /// 组装主表 + 客户端环境子表为 Result
    private GatewayPayConfigResult toResultWithClientEnvs(GatewayPayConfig entity) {
        GatewayPayConfigResult result = new GatewayPayConfigResult();
        BeanUtil.copyProperties(entity, result);
        List<GatewayPayClientEnv> clientEnvs = clientEnvManager.findByConfigId(entity.getId());
        List<GatewayPayClientEnvResult> envResults = clientEnvs.stream()
                .map(s -> new GatewayPayClientEnvResult()
                        .setClientEnv(s.getClientEnv())
                        .setPayForm(s.getPayForm())
                        .setMethod(s.getMethod())
                        .setChannelMchNo(s.getChannelMchNo())
                        .setCapability(s.getCapability()))
                .toList();
        result.setClientEnvs(envResults);
        return result;
    }
}
