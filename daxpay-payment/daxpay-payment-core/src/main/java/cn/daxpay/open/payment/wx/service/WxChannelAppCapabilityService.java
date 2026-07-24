package cn.daxpay.open.payment.wx.service;

import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.wx.convert.WxChannelAppCapabilityConvert;
import cn.daxpay.open.payment.wx.dao.WxChannelAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.WxMchAppManager;
import cn.daxpay.open.payment.wx.dao.WxPlatformAppManager;
import cn.daxpay.open.payment.wx.entity.WxChannelAppCapability;
import cn.daxpay.open.payment.wx.entity.WxMchApp;
import cn.daxpay.open.payment.wx.entity.WxPlatformApp;
import cn.daxpay.open.payment.wx.enums.WxAppScopeEnum;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.param.WxChannelAppCapabilityParam;
import cn.daxpay.open.payment.wx.result.WxChannelAppCapabilityResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 通道商户微信应用能力绑定
///
/// 管理通道商户 × 支付能力 × 档位 对主数据的引用；先删后插全量覆盖。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxChannelAppCapabilityService {

    private final WxChannelAppCapabilityManager capabilityManager;
    private final WxPlatformAppManager wxPlatformAppManager;
    private final WxMchAppManager wxMchAppManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 按通道商户号查询能力绑定列表，并填充应用展示信息
    public List<WxChannelAppCapabilityResult> listByChannelMchNo(String channelMchNo) {
        List<WxChannelAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        Map<Long, WxPlatformApp> platformMap = wxPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(WxPlatformApp::getId, Function.identity()));
        // 通道绑定同属一商户，用首行 mchNo 批量加载商户应用
        String mchNo = rels.getFirst().getMchNo();
        Map<Long, WxMchApp> mchAppMap = wxMchAppManager.listByMchNo(mchNo).stream()
                .collect(Collectors.toMap(WxMchApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, platformMap, mchAppMap))
                .toList();
    }

    /// 按通道商户全量保存能力绑定（先清后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(String mchNo, String channelMchNo, List<WxChannelAppCapabilityParam> items) {
        // 校验通道商户存在且 mchNo 匹配
        ChannelMerchant channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.channelMerchantMismatch"));
        if (!mchNo.equals(channelMerchant.getMchNo())) {
            // 微信: 通道商户与商户号不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.wx.channelMerchantMismatch");
        }
        // 先删该通道商户下全部绑定
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        Map<Long, WxPlatformApp> platformMap = wxPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(WxPlatformApp::getId, Function.identity()));
        Map<Long, WxMchApp> mchAppMap = wxMchAppManager.listByMchNo(mchNo).stream()
                .collect(Collectors.toMap(WxMchApp::getId, Function.identity()));
        // capability + scope 唯一
        HashSet<String> uniq = new HashSet<>();
        for (WxChannelAppCapabilityParam item : items) {
            WxAppScopeEnum scope = WxAppScopeEnum.findByCode(item.getAppScope());
            if (scope == null) {
                // 微信: 档位不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.scopeNotExist");
            }
            // 通道能力绑仅允许商户档；服务商应用走产品级默认绑
            if (scope != WxAppScopeEnum.MERCHANT) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.scopeNotExist");
            }
            String uniqKey = item.getCapability() + "#" + scope.getCode();
            if (!uniq.add(uniqKey)) {
                // 微信: 能力+档位重复
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appTypeCapabilityMismatch");
            }
            String appType = this.resolveAndValidateRef(scope, item.getWxAppRefId(), mchNo, platformMap, mchAppMap);
            // 应用类型与能力强校验
            if (!WxAppTypeEnum.isCompatible(appType, item.getCapability())) {
                // 微信: 应用类型与支付能力不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appTypeCapabilityMismatch");
            }
            var entity = new WxChannelAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setAppScope(scope.getCode())
                    .setWxAppRefId(item.getWxAppRefId());
            // 运营端写 MchBaseEntity 必须显式 setMchNo
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 删除通道商户下全部能力绑定
    public void deleteByChannelMchNo(String channelMchNo) {
        capabilityManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 校验档位引用存在，返回应用类型
    private String resolveAndValidateRef(WxAppScopeEnum scope, Long refId, String mchNo,
                                         Map<Long, WxPlatformApp> platformMap, Map<Long, WxMchApp> mchAppMap) {
        if (scope == WxAppScopeEnum.PLATFORM) {
            WxPlatformApp app = platformMap.get(refId);
            if (app == null) {
                // 微信: 平台应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appNotFound");
            }
            return app.getAppType();
        }
        WxMchApp app = mchAppMap.get(refId);
        if (app == null || !mchNo.equals(app.getMchNo())) {
            // 微信: 商户应用不存在或不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.wx.mchAppNotFound");
        }
        return app.getAppType();
    }

    /// 填充冗余展示字段
    private WxChannelAppCapabilityResult fillResult(WxChannelAppCapability rel,
                                                    Map<Long, WxPlatformApp> platformMap,
                                                    Map<Long, WxMchApp> mchAppMap) {
        WxChannelAppCapabilityResult result = WxChannelAppCapabilityConvert.CONVERT.toResult(rel);
        if (WxAppScopeEnum.PLATFORM.getCode().equals(rel.getAppScope())) {
            WxPlatformApp app = platformMap.get(rel.getWxAppRefId());
            if (app != null) {
                result.setAppName(app.getAppName())
                        .setWxAppId(app.getWxAppId())
                        .setAppType(app.getAppType());
            }
        }
        else {
            WxMchApp app = mchAppMap.get(rel.getWxAppRefId());
            if (app != null) {
                result.setAppName(app.getAppName())
                        .setWxAppId(app.getWxAppId())
                        .setAppType(app.getAppType());
            }
        }
        return result;
    }
}
