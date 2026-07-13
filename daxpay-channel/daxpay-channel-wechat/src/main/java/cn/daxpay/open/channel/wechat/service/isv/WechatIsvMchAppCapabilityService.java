package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppCapabilityConvert;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppCapability;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppCapabilityItem;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppCapabilityResult;
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

/// # 微信服务商通道商户应用支付能力关联
///
/// 管理通道商户(特约商户)维度下「支付能力 → 子商户应用」的绑定关系。
/// 本表只存子商户显式选择自己应用的记录;某能力未配置时,支付解析返回 empty,
/// 由调用方(未来的微信服务商支付策略/配置组装器)回退到全局服务商应用配置
/// ([cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppCapabilityService])。
///
/// 支付时通过 [#resolveApp] 解析当前能力对应的应用：仅读取显式配置, 未配置返回 empty
/// (语义为"该能力未在子商户维度配置, sub_appid 留空, 走纯服务商应用模式")。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvMchAppCapabilityService {

    private final WechatIsvMchAppCapabilityManager capabilityManager;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;

    /// 查询通道商户下的能力关联列表,并填充子商户应用展示信息(应用名称/AppId/类型)
    public List<WechatIsvMchAppCapabilityResult> listByChannelMchNo(String mchNo, String channelMchNo) {
        List<WechatIsvMchAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        // 批量查询该通道商户下全部子商户应用,构建 id → app 映射用于填充展示字段
        Map<Long, WechatIsvMchApp> appMap = wechatIsvMchAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .stream()
                .collect(Collectors.toMap(WechatIsvMchApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getWechatIsvMchAppId())))
                .toList();
    }

    /// 全量保存能力关联(先清后插),校验应用归属与能力唯一
    ///
    /// items 仅含子商户显式选择自己应用的项;选「服务商默认应用」的能力不在此参数中,
    /// 先清后插时会自然移除其历史记录,支付时自动回退到全局服务商配置。
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(WechatIsvMchAppCapabilityBatchParam param) {
        String mchNo = param.getMchNo();
        String channelMchNo = param.getChannelMchNo();
        List<WechatIsvMchAppCapabilityItem> items = param.getItems();
        // 先清空旧关联
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        // 查询该通道商户下全部子商户应用,校验应用归属
        List<WechatIsvMchApp> apps = wechatIsvMchAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        Map<Long, WechatIsvMchApp> appMap = apps.stream()
                .collect(Collectors.toMap(WechatIsvMchApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (WechatIsvMchAppCapabilityItem item : items) {
            // 能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 微信: 支付能力[{0}]重复配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.capabilityDuplicate", item.getCapability());
            }
            // 应用归属校验
            if (!appMap.containsKey(item.getWechatIsvMchAppId())) {
                // 微信: 服务商通道商户应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.mchAppNotFound");
            }
            var entity = new WechatIsvMchAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setWechatIsvMchAppId(item.getWechatIsvMchAppId());
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 应用被删除时级联清理关联,避免悬空引用
    public void deleteByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        capabilityManager.deleteByWechatIsvMchAppId(wechatIsvMchAppId);
    }

    /// 支付/回调解析子商户应用(仅读取显式配置，须已装载 mchNo，租户内)
    ///
    /// 子商户应用(sub_appid)为可选项, 不做 appType 推导与首个兜底。
    /// 未配置返回 empty, 由调用方留空 sub_appid。认证无上下文请用 [#resolveAppNotTenant]。
    ///
    /// @param channelMchNo 通道商户号(服务商特约商户)
    /// @param capability   支付能力编码
    /// @return 命中的子商户应用;未配置返回 empty(sub_appid 留空)
    public Optional<WechatIsvMchApp> resolveApp(String channelMchNo, String capability) {
        if (StrUtil.hasBlank(channelMchNo, capability)) {
            return Optional.empty();
        }
        return capabilityManager.findOne(channelMchNo, capability)
                .flatMap(rel -> wechatIsvMchAppManager.findById(rel.getWechatIsvMchAppId()));
    }

    /// 认证等无租户上下文时解析子商户应用（忽略租户）
    @IgnoreTenant
    public Optional<WechatIsvMchApp> resolveAppNotTenant(String channelMchNo, String capability) {
        return resolveApp(channelMchNo, capability);
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
    private WechatIsvMchAppCapabilityResult fillResult(WechatIsvMchAppCapability rel, WechatIsvMchApp app) {
        WechatIsvMchAppCapabilityResult result = WechatIsvMchAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setWxAppId(app.getWxAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}
