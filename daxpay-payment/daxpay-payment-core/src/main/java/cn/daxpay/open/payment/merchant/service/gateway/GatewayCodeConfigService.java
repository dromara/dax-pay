package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeConfig;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeClientEnvParam;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCodeConfigParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeClientEnvResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCodeConfigResult;
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

/// # 码牌支付策略配置服务
///
/// 管理主表(level)与子表(clientEnv × payForm), 与聚合配置独立。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayCodeConfigService {

    private final GatewayCodeConfigManager configManager;
    private final GatewayCodeClientEnvManager clientEnvManager;

    /// 按应用查询, 不存在返回空对象(level 默认 AUTO)
    public GatewayCodeConfigResult findByAppId(String appId) {
        return configManager.findByAppId(appId)
                .map(this::toResultWithClientEnvs)
                .orElseGet(() -> new GatewayCodeConfigResult()
                        .setAppId(appId)
                        .setLevel(AggregateConfigLevelEnum.AUTO.getCode())
                        .setClientEnvs(List.of()));
    }

    /// 保存或更新(主表 + 子表替换)
    ///
    /// METHOD/DIRECT 支持**部分配置**: 只落库已填写的环境×形态, 未配组合支付时再报错。
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(GatewayCodeConfigParam param) {
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(param.getLevel());
        List<GatewayCodeClientEnvParam> filledEnvs = filterFilledClientEnvs(level, param.getClientEnvs());
        validateClientEnvs(level, filledEnvs);

        GatewayCodeConfig config = configManager.findByAppId(param.getAppId())
                .orElseGet(() -> new GatewayCodeConfig().setAppId(param.getAppId()));
        config.setLevel(level.getCode());
        // 运营端无商户上下文, 主表/子表均需显式 mchNo(MchBaseEntity insert 填充依赖上下文会失败)
        if (StrUtil.isBlank(config.getMchNo())) {
            config.setMchNo(param.getMchNo());
        }
        if (StrUtil.isBlank(config.getMchNo())) {
            // 码牌: 商户上下文未装载
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.assist.mchContextMissing");
        }
        configManager.saveOrUpdate(config);

        clientEnvManager.deleteByConfigId(config.getId());
        if (CollUtil.isNotEmpty(filledEnvs)) {
            String mchNo = config.getMchNo();
            List<GatewayCodeClientEnv> rows = new ArrayList<>();
            for (GatewayCodeClientEnvParam envParam : filledEnvs) {
                GatewayCodeClientEnv env = new GatewayCodeClientEnv()
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
    private List<GatewayCodeClientEnvParam> filterFilledClientEnvs(
            AggregateConfigLevelEnum level, List<GatewayCodeClientEnvParam> clientEnvs) {
        if (level == AggregateConfigLevelEnum.AUTO || CollUtil.isEmpty(clientEnvs)) {
            return List.of();
        }
        List<GatewayCodeClientEnvParam> filled = new ArrayList<>();
        for (GatewayCodeClientEnvParam env : clientEnvs) {
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
    private void validateClientEnvs(AggregateConfigLevelEnum level, List<GatewayCodeClientEnvParam> filledEnvs) {
        if (level == AggregateConfigLevelEnum.AUTO) {
            return;
        }
        // METHOD/DIRECT 至少配置一行
        if (CollUtil.isEmpty(filledEnvs)) {
            // 码牌: 请至少配置一项环境与形态
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeClientEnvsRequired");
        }
        for (GatewayCodeClientEnvParam env : filledEnvs) {
            // 校验 payForm 合法
            CodePayFormEnum.findByCode(env.getPayForm());
            if (level == AggregateConfigLevelEnum.METHOD && StrUtil.isBlank(env.getMethod())) {
                // 码牌: 已填写的环境与形态须选择支付方式
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.codeClientEnvMethodRequired");
            }
            if (level == AggregateConfigLevelEnum.DIRECT) {
                if (StrUtil.isBlank(env.getChannelMchNo())) {
                    // 码牌: 直接指定时已填写行的通道商户号必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvChannelMchRequired");
                }
                if (StrUtil.isBlank(env.getCapability())) {
                    // 码牌: 直接指定时已填写行的支付能力必填
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvCapabilityRequired");
                }
            }
        }
    }

    /// 将码牌配置主表与子表组装为带客户端环境列表的结果对象
    private GatewayCodeConfigResult toResultWithClientEnvs(GatewayCodeConfig entity) {
        GatewayCodeConfigResult result = new GatewayCodeConfigResult();
        BeanUtil.copyProperties(entity, result);
        List<GatewayCodeClientEnv> clientEnvs = clientEnvManager.findByConfigId(entity.getId());
        List<GatewayCodeClientEnvResult> envResults = clientEnvs.stream()
                .map(s -> new GatewayCodeClientEnvResult()
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
