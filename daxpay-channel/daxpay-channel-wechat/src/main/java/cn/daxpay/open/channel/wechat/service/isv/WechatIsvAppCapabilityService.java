package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.code.WechatAppTypeCode;
import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppCapability;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppCapabilityItem;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppCapabilityResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 微信服务商应用支付能力关联
///
/// 管理全局维度下「支付能力 → 服务商应用」的绑定关系(微信服务商应用全局共享)。
/// 支付时通过 [#resolveApp] 解析当前能力对应的应用：显式配置 > appType自动推导 > 取首个兜底。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppCapabilityService {

    private final WechatIsvAppCapabilityManager capabilityManager;
    private final WechatIsvAppManager wechatIsvAppManager;

    /// 查询能力关联列表，并填充应用展示信息(应用名称/AppId/类型)
    public List<WechatIsvAppCapabilityResult> listAll() {
        List<WechatIsvAppCapability> rels = capabilityManager.listAll();
        if (rels.isEmpty()) {
            return List.of();
        }
        // 批量查询全部服务商应用，构建 id → app 映射用于填充展示字段
        Map<Long, WechatIsvApp> appMap = wechatIsvAppManager.listAll().stream()
                .collect(Collectors.toMap(WechatIsvApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getWechatIsvAppId())))
                .toList();
    }

    /// 全量保存能力关联(先清后插)，校验应用存在与能力唯一
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(WechatIsvAppCapabilityBatchParam param) {
        List<WechatIsvAppCapabilityItem> items = param.getItems();
        // 先清空旧关联
        capabilityManager.deleteAll();
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 查询全部服务商应用，校验应用存在
        Map<Long, WechatIsvApp> appMap = wechatIsvAppManager.listAll().stream()
                .collect(Collectors.toMap(WechatIsvApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (WechatIsvAppCapabilityItem item : items) {
            // 能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 微信: 支付能力[{0}]重复配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.capabilityDuplicate", item.getCapability());
            }
            // 应用存在校验
            if (!appMap.containsKey(item.getWechatIsvAppId())) {
                // 微信: 服务商应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.appNotFound");
            }
            var entity = new WechatIsvAppCapability()
                    .setCapability(item.getCapability())
                    .setWechatIsvAppId(item.getWechatIsvAppId());
            capabilityManager.save(entity);
        }
    }

    /// 应用被删除时级联清理关联，避免悬空引用
    public void deleteByWechatIsvAppId(Long wechatIsvAppId) {
        capabilityManager.deleteByWechatIsvAppId(wechatIsvAppId);
    }

    /// 支付时解析当前支付能力对应的应用：显式配置 > appType自动推导（要求唯一命中）
    ///
    /// appType 推导要求该类型服务商应用全局唯一命中：
    /// - 唯一命中：返回该应用
    /// - 该类型存在多个应用：抛 appNotUnique，要求显式配置能力绑
    /// - 该类型无应用：返回 empty，由调用方走最终报错
    /// 不再"首个服务商应用"兜底。
    public Optional<WechatIsvApp> resolveApp(String capability) {
        if (StrUtil.isBlank(capability)) {
            return Optional.empty();
        }
        // 1. 显式配置优先
        var rel = capabilityManager.findOne(capability);
        if (rel.isPresent()) {
            return wechatIsvAppManager.findById(rel.get().getWechatIsvAppId());
        }
        // 2. appType 推导：要求该类型服务商应用唯一命中，>1 拒绝猜测
        String appType = WechatAppTypeCode.resolveAppType(capability);
        if (appType != null) {
            List<WechatIsvApp> apps = wechatIsvAppManager.listByAppType(appType);
            if (apps.size() > 1) {
                // 存在多个同类型服务商应用，请显式配置能力绑定以明确选择
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.appNotUnique", appType);
            }
            if (apps.size() == 1) {
                return Optional.of(apps.getFirst());
            }
        }
        // 3. 不再首个兜底，返回 empty
        return Optional.empty();
    }

    /// 查询微信服务商产品支持的支付能力候选列表(含国际化名称)
    public List<WechatCapabilityOption> listSupportedCapabilities() {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(
                ProductEnum.WECHAT_ISV.getCode(), AbsProductStrategy.class);
        return strategy.methodCapabilityMapping().values().stream()
                .flatMap(List::stream)
                .distinct()
                .map(cap -> new WechatCapabilityOption(cap.getCode(), I18nUtil.getEnumName(cap)))
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private WechatIsvAppCapabilityResult fillResult(WechatIsvAppCapability rel, WechatIsvApp app) {
        WechatIsvAppCapabilityResult result = WechatIsvAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setWxAppId(app.getWxAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}
