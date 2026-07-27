package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.code.DouyinAppTypeCode;
import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppCapabilityConvert;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppCapabilityManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppCapability;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppCapabilityItem;
import cn.daxpay.open.channel.douyin.result.DouyinCapabilityOption;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppCapabilityResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
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

/// # 抖音直连商户应用支付能力关联
///
/// 管理通道商户维度下「支付能力 → 直连应用」的绑定关系。
/// 支付时通过 [#resolveApp] 解析当前能力对应的应用：显式配置 > appType自动推导 > 通道商户首个兜底。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectAppCapabilityService {

    private final DouyinDirectAppCapabilityManager capabilityManager;
    private final DouyinDirectAppManager douyinDirectAppManager;

    /// 查询通道商户下的能力关联列表，并填充应用展示信息(应用名称/AppId/类型)
    public List<DouyinDirectAppCapabilityResult> listByChannelMchNo(String mchNo, String channelMchNo) {
        List<DouyinDirectAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        // 批量查询该通道商户下全部应用，构建 id → app 映射用于填充展示字段
        Map<Long, DouyinDirectApp> appMap = douyinDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .stream()
                .collect(Collectors.toMap(DouyinDirectApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getDouyinDirectAppId())))
                .toList();
    }

    /// 全量保存能力关联(先清后插)，校验应用归属与能力唯一
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(DouyinDirectAppCapabilityBatchParam param) {
        String mchNo = param.getMchNo();
        String channelMchNo = param.getChannelMchNo();
        List<DouyinDirectAppCapabilityItem> items = param.getItems();
        // 先清空旧关联
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 查询该通道商户下全部应用，校验应用归属
        List<DouyinDirectApp> apps = douyinDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        Map<Long, DouyinDirectApp> appMap = apps.stream()
                .collect(Collectors.toMap(DouyinDirectApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (DouyinDirectAppCapabilityItem item : items) {
            // 能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 抖音: 支付能力[{0}]重复配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.capabilityDuplicate", item.getCapability());
            }
            // 应用归属校验
            DouyinDirectApp app = appMap.get(item.getDouyinDirectAppId());
            if (app == null) {
                // 抖音: 直连商户应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.mchAppNotFound");
            }
            // 应用类型与支付能力强校验
            if (!DouyinAppTypeCode.isCompatible(app.getAppType(), item.getCapability())) {
                // 抖音: 应用类型与支付能力不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.appTypeCapabilityMismatch");
            }
            var entity = new DouyinDirectAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setDouyinDirectAppId(item.getDouyinDirectAppId());
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 应用被删除时级联清理关联，避免悬空引用
    public void deleteByDouyinDirectAppId(Long douyinDirectAppId) {
        capabilityManager.deleteByDouyinDirectAppId(douyinDirectAppId);
    }

    /// 支付/回调解析应用：显式配置 > appType 自动推导（须已装载 mchNo，租户内）
    ///
    /// appType 推导要求该通道商户下该类型应用唯一命中：
    /// - 唯一命中：返回该应用
    /// - 该类型存在多个应用：抛 appNotUnique，要求显式配置能力绑
    /// - 该类型无应用：返回 empty，由调用方走最终报错
    /// 不再"通道商户首个"兜底。
    ///
    /// 认证无上下文请用 [#resolveAppNotTenant]。
    public Optional<DouyinDirectApp> resolveApp(String channelMchNo, String capability) {
        if (StrUtil.hasBlank(channelMchNo, capability)) {
            return Optional.empty();
        }
        // 1. 显式配置优先
        var rel = capabilityManager.findOne(channelMchNo, capability);
        if (rel.isPresent()) {
            return douyinDirectAppManager.findById(rel.get().getDouyinDirectAppId());
        }
        // 2. appType 推导：要求该通道商户下该类型应用唯一命中，>1 拒绝猜测
        String appType = DouyinAppTypeCode.resolveAppType(capability);
        if (appType != null) {
            List<DouyinDirectApp> apps = douyinDirectAppManager.listByChannelMchNoAndAppType(channelMchNo, appType);
            if (apps.size() > 1) {
                // 存在多个同类型应用，请显式配置能力绑定以明确选择
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.appNotUnique", appType);
            }
            if (apps.size() == 1) {
                return Optional.of(apps.getFirst());
            }
        }
        // 3. 不再首个兜底，返回 empty 由调用方报错
        return Optional.empty();
    }

    /// 认证等无租户上下文时解析应用（忽略租户）
    @IgnoreTenant
    public Optional<DouyinDirectApp> resolveAppNotTenant(String channelMchNo, String capability) {
        return resolveApp(channelMchNo, capability);
    }

    /// H5 silent_auth / JS-SDK 验签用网站应用解析（忽略租户）
    ///
    /// 优先级: channelAppId 显式 > capability 命中且为 web_app > 该通道商户唯一 web_app。
    /// 不再"首个 web_app"兜底；web_app 不唯一或不存在均直接报错。
    @IgnoreTenant
    public DouyinDirectApp resolveWebAppForH5Auth(String channelMchNo, String capability, String channelAppId) {
        if (StrUtil.isNotBlank(channelAppId)) {
            return douyinDirectAppManager.findByChannelMchNoAndDouyinAppIdNotTenant(channelMchNo, channelAppId)
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.channel.douyin.channelAppIdNotFound", channelAppId));
        }
        if (StrUtil.isNotBlank(capability)) {
            var byCap = resolveAppNotTenant(channelMchNo, capability);
            if (byCap.isPresent() && DouyinAppTypeCode.WEB_APP.equals(byCap.get().getAppType())) {
                return byCap.get();
            }
        }
        // H5 验签需 web_app：要求该类型唯一命中，>1 抛 notUnique，==0 抛 notConfigured
        List<DouyinDirectApp> webApps = douyinDirectAppManager.listByChannelMchNoAndAppType(
                channelMchNo, DouyinAppTypeCode.WEB_APP);
        if (webApps.size() > 1) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.appNotUnique", DouyinAppTypeCode.WEB_APP);
        }
        if (webApps.size() == 1) {
            return webApps.getFirst();
        }
        // 抖音: 未配置 H5 验签所需的 web_app 类型应用
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "error.channel.douyin.appNotConfigured", DouyinAppTypeCode.WEB_APP);
    }

    /// 查询抖音直连产品支持的支付能力候选列表(含国际化名称)
    public List<DouyinCapabilityOption> listSupportedCapabilities() {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(
                ProductEnum.DOUYIN_PAY.getCode(), AbsProductStrategy.class);
        return strategy.methodCapabilityMapping().values().stream()
                .flatMap(List::stream)
                .distinct()
                .map(cap -> new DouyinCapabilityOption(cap.getCode(), I18nUtil.getEnumName(cap)))
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private DouyinDirectAppCapabilityResult fillResult(DouyinDirectAppCapability rel, DouyinDirectApp app) {
        DouyinDirectAppCapabilityResult result = DouyinDirectAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setDouyinAppId(app.getDouyinAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}
