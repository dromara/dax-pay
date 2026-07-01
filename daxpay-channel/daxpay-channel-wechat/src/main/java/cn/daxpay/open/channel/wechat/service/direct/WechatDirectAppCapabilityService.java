package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.code.WechatAppTypeCode;
import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppCapability;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppCapabilityItem;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppCapabilityResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
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

/// # 微信直连商户应用支付能力关联
///
/// 管理通道商户维度下「支付能力 → 直连应用」的绑定关系。
/// 支付时通过 [resolveApp] 解析当前能力对应的应用：显式配置 > appType自动推导 > 通道商户首个兜底。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectAppCapabilityService {

    private final WechatDirectAppCapabilityManager capabilityManager;
    private final WechatDirectAppManager wechatDirectAppManager;

    /// 查询通道商户下的能力关联列表，并填充应用展示信息(应用名称/AppId/类型)
    public List<WechatDirectAppCapabilityResult> listByChannelMchNo(String mchNo, String channelMchNo) {
        List<WechatDirectAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        // 批量查询该通道商户下全部应用，构建 id → app 映射用于填充展示字段
        Map<Long, WechatDirectApp> appMap = wechatDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .stream()
                .collect(Collectors.toMap(WechatDirectApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getWechatDirectAppId())))
                .toList();
    }

    /// 全量保存能力关联(先清后插)，校验应用归属与能力唯一
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(WechatDirectAppCapabilityBatchParam param) {
        String mchNo = param.getMchNo();
        String channelMchNo = param.getChannelMchNo();
        List<WechatDirectAppCapabilityItem> items = param.getItems();
        // 先清空旧关联
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 查询该通道商户下全部应用，校验应用归属
        List<WechatDirectApp> apps = wechatDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        Map<Long, WechatDirectApp> appMap = apps.stream()
                .collect(Collectors.toMap(WechatDirectApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (WechatDirectAppCapabilityItem item : items) {
            // 能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 微信: 支付能力[{0}]重复配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.capabilityDuplicate", item.getCapability());
            }
            // 应用归属校验
            if (!appMap.containsKey(item.getWechatDirectAppId())) {
                // 微信: 直连商户应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.mchAppNotFound");
            }
            var entity = new WechatDirectAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setWechatDirectAppId(item.getWechatDirectAppId());
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 应用被删除时级联清理关联，避免悬空引用
    public void deleteByWechatDirectAppId(Long wechatDirectAppId) {
        capabilityManager.deleteByWechatDirectAppId(wechatDirectAppId);
    }

    /// 支付时解析当前支付能力对应的应用：显式配置 > appType自动推导 > 通道商户首个兜底
    ///
    /// @param channelMchNo 通道商户号
    /// @param capability    支付能力编码
    /// @return 命中的应用；三者均未命中返回 empty，由调用方兜底回退
    public Optional<WechatDirectApp> resolveApp(String channelMchNo, String capability) {
        if (StrUtil.hasBlank(channelMchNo, capability)) {
            return Optional.empty();
        }
        // 1. 显式配置优先
        var rel = capabilityManager.findOne(channelMchNo, capability);
        if (rel.isPresent()) {
            return wechatDirectAppManager.findById(rel.get().getWechatDirectAppId());
        }
        // 2. 未配置则按能力 → appType 自动推导
        String appType = WechatAppTypeCode.resolveAppType(capability);
        if (appType != null) {
            Optional<WechatDirectApp> byType = wechatDirectAppManager.findFirstByChannelMchNoAndAppType(channelMchNo, appType);
            if (byType.isPresent()) {
                return byType;
            }
        }
        // 3. 最终兜底：按通道商户号取首个应用
        return wechatDirectAppManager.findFirstByChannelMchNo(channelMchNo);
    }

    /// 查询微信直连产品支持的支付能力候选列表(含国际化名称)
    public List<WechatCapabilityOption> listSupportedCapabilities() {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(
                ProductEnum.WECHAT_PAY.getCode(), AbsProductStrategy.class);
        return strategy.methodCapabilityMapping().values().stream()
                .flatMap(List::stream)
                .distinct()
                .map(cap -> new WechatCapabilityOption(cap.getCode(), I18nUtil.getEnumName(cap)))
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private WechatDirectAppCapabilityResult fillResult(WechatDirectAppCapability rel, WechatDirectApp app) {
        WechatDirectAppCapabilityResult result = WechatDirectAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setWxAppId(app.getWxAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}
