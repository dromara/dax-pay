package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.code.AlipayDirectAppTypeCode;
import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppCapabilityConvert;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppCapabilityManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppCapabilityItem;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppCapabilityResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectCapabilityOption;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 支付宝直连商户应用支付能力关联
///
/// 管理通道商户维度下「支付能力 → 应用」的绑定关系。
/// 支付时通过 [#resolveApp] 解析当前能力对应的应用：显式配置 > appType自动推导。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppCapabilityService {

    private final AlipayDirectAppCapabilityManager capabilityManager;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 查询通道商户下的能力关联列表，并填充应用展示信息(应用名称/ID/类型)
    public List<AlipayDirectAppCapabilityResult> listByChannelMchNo(String mchNo, String channelMchNo) {
        List<AlipayDirectAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        // 批量查询该通道商户下全部应用，构建 id → app 映射用于填充展示字段
        Map<Long, AlipayDirectApp> appMap = alipayDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .stream()
                .collect(Collectors.toMap(AlipayDirectApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getAlipayDirectAppId())))
                .toList();
    }

    /// 全量保存能力关联(先清后插)，校验应用归属与能力唯一
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(AlipayDirectAppCapabilityBatchParam param) {
        String mchNo = param.getMchNo();
        String channelMchNo = param.getChannelMchNo();
        List<AlipayDirectAppCapabilityItem> items = param.getItems();
        // 先清空旧关联
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 查询该通道商户下全部应用，校验应用归属
        List<AlipayDirectApp> apps = alipayDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        Map<Long, AlipayDirectApp> appMap = apps.stream()
                .collect(Collectors.toMap(AlipayDirectApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (AlipayDirectAppCapabilityItem item : items) {
            // 能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 支付能力[{0}]重复配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.capabilityDuplicate", item.getCapability());
            }
            // 应用归属校验
            AlipayDirectApp app = appMap.get(item.getAlipayDirectAppId());
            if (app == null) {
                // 支付宝: 直连商户应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.mchAppNotFound");
            }
            // 应用类型与支付能力强校验
            if (!AlipayDirectAppTypeCode.isCompatible(app.getAppType(), item.getCapability())) {
                // 支付宝: 应用类型与支付能力不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.appTypeCapabilityMismatch");
            }
            var entity = new AlipayDirectAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setAlipayDirectAppId(item.getAlipayDirectAppId());
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 应用被删除时级联清理关联，避免悬空引用
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        capabilityManager.deleteByAlipayDirectAppId(alipayDirectAppId);
    }

    /// 支付/回调解析应用：显式配置 > appType 自动推导（须已装载 mchNo，租户内）
    ///
    /// 认证无上下文请用 [#resolveAppNotTenant]。
    ///
    /// @param channelMchNo 通道商户号
    /// @param capability    支付能力编码
    /// @return 命中的应用；两者均未命中返回 empty，由调用方兜底回退
    public Optional<AlipayDirectApp> resolveApp(String channelMchNo, String capability) {
        if (StrUtil.hasBlank(channelMchNo, capability)) {
            return Optional.empty();
        }
        // 1. 显式配置优先
        var rel = capabilityManager.findOne(channelMchNo, capability);
        if (rel.isPresent()) {
            return alipayDirectAppManager.findById(rel.get().getAlipayDirectAppId());
        }
        // 2. 未配置则按能力 → appType 自动推导
        String appType = AlipayDirectAppTypeCode.resolveAppType(capability);
        if (appType != null) {
            return alipayDirectAppManager.findFirstByChannelMchNoAndAppType(channelMchNo, appType);
        }
        return Optional.empty();
    }

    /// 认证等无租户上下文时解析应用（忽略租户）
    @IgnoreTenant
    public Optional<AlipayDirectApp> resolveAppNotTenant(String channelMchNo, String capability) {
        return resolveApp(channelMchNo, capability);
    }

    /// 查询支付宝直连产品支持的支付能力候选列表(含国际化名称)
    public List<AlipayDirectCapabilityOption> listSupportedCapabilities() {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(
                ProductEnum.ALIPAY.getCode(), AbsProductStrategy.class);
        return strategy.methodCapabilityMapping().values().stream()
                .flatMap(List::stream)
                .distinct()
                .map(cap -> new AlipayDirectCapabilityOption(cap.getCode(), I18nUtil.getEnumName(cap)))
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private AlipayDirectAppCapabilityResult fillResult(AlipayDirectAppCapability rel, AlipayDirectApp app) {
        AlipayDirectAppCapabilityResult result = AlipayDirectAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setAliAppId(app.getAliAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}
