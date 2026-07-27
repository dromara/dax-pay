package cn.daxpay.open.payment.wx.service;

import cn.daxpay.open.payment.wx.dao.WxChannelAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.WxMchAppManager;
import cn.daxpay.open.payment.wx.dao.WxPlatformAppManager;
import cn.daxpay.open.payment.wx.entity.WxMchApp;
import cn.daxpay.open.payment.wx.entity.WxPlatformApp;
import cn.daxpay.open.payment.wx.enums.WxAppScopeEnum;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 微信开放应用解析服务
///
/// 实现 [WxAppFacade]，供通道组装器统一解析 wxAppId + Secret。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxAppResolveService implements WxAppFacade {

    private final WxPlatformAppManager wxPlatformAppManager;
    private final WxMchAppManager wxMchAppManager;
    private final WxPlatformAppAuthConfigService wxPlatformAppAuthConfigService;
    private final WxMchAppAuthConfigService wxMchAppAuthConfigService;
    private final WxChannelAppCapabilityManager wxChannelAppCapabilityManager;

    /// 按档位与主键加载应用视图（含 Auth）
    @Override
    public WxAppView getById(WxAppScopeEnum scope, Long id) {
        if (scope == null || id == null) {
            // 微信: 档位不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.scopeNotExist");
        }
        if (scope == WxAppScopeEnum.PLATFORM) {
            WxPlatformApp app = wxPlatformAppManager.findById(id)
                    // 微信: 平台应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
            String secret = wxPlatformAppAuthConfigService.findByWxPlatformAppId(id).getAppSecret();
            return new WxAppView(scope, app.getId(), app.getWxAppId(), app.getAppType(), secret, app.getAppName());
        }
        if (scope == WxAppScopeEnum.MERCHANT) {
            WxMchApp app = wxMchAppManager.findById(id)
                    // 微信: 商户应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
            String secret = wxMchAppAuthConfigService.findByWxMchAppId(id).getAppSecret();
            return new WxAppView(scope, app.getId(), app.getWxAppId(), app.getAppType(), secret, app.getAppName());
        }
        // 微信: 档位不存在
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.scopeNotExist");
    }

    /// 解析单应用：显式 channelAppId → 通道能力绑 → appType 推导（平台应用唯一命中）
    @Override
    public WxAppView resolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product) {
        // 1. 显式 channelAppId
        if (StrUtil.isNotBlank(channelAppId)) {
            return this.resolveByChannelAppId(mchNo, channelAppId);
        }
        // 2. 通道能力绑定（同能力优先 merchant，其次 platform）
        if (StrUtil.isNotBlank(channelMchNo) && StrUtil.isNotBlank(capability)) {
            var merchantBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, WxAppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                return this.getById(WxAppScopeEnum.MERCHANT, merchantBind.get().getWxAppRefId());
            }
            var platformBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, WxAppScopeEnum.PLATFORM.getCode());
            if (platformBind.isPresent()) {
                return this.getById(WxAppScopeEnum.PLATFORM, platformBind.get().getWxAppRefId());
            }
        }
        // 3. appType 推导：要求该类型平台应用唯一命中
        WxAppView platformFallback = this.resolvePlatformFallback(capability);
        if (platformFallback != null) {
            return platformFallback;
        }
        // 微信: 未配置该能力对应的应用
        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                "error.payment.wx.appNotConfigured", capability);
    }

    /// 解析 ISV 双应用：platform(sp) 必填 + merchant(sub) 可选
    @Override
    public WxIsvAppPair resolveIsvPair(String mchNo, String channelMchNo, String capability, String channelAppId,
            String product) {
        WxAppView platform = null;
        WxAppView merchant = null;
        // 通道绑定：platform / merchant 各取一行
        if (StrUtil.isNotBlank(channelMchNo) && StrUtil.isNotBlank(capability)) {
            var platformBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, WxAppScopeEnum.PLATFORM.getCode());
            if (platformBind.isPresent()) {
                platform = this.getById(WxAppScopeEnum.PLATFORM, platformBind.get().getWxAppRefId());
            }
            var merchantBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, WxAppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                merchant = this.getById(WxAppScopeEnum.MERCHANT, merchantBind.get().getWxAppRefId());
            }
        }
        // 平台侧回退：appType 推导（平台应用唯一命中）
        if (platform == null) {
            platform = this.resolvePlatformFallback(capability);
        }
        // channelAppId 命中某侧时按命中侧覆盖
        if (StrUtil.isNotBlank(channelAppId)) {
            var hitPlatform = wxPlatformAppManager.findByWxAppId(channelAppId);
            if (hitPlatform.isPresent()) {
                platform = this.toPlatformView(hitPlatform.get());
            }
            else {
                var hitMerchant = wxMchAppManager.findByMchNoAndWxAppId(mchNo, channelAppId);
                if (hitMerchant.isPresent()) {
                    merchant = this.toMerchantView(hitMerchant.get());
                }
                else {
                    // 微信: 指定 AppId 未配置
                    throw new DataNotExistException("error.payment.wx.channelAppIdNotFound", channelAppId);
                }
            }
        }
        if (platform == null) {
            // 微信: 未配置该能力对应的平台应用（sp 必填）
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.appNotConfigured", capability);
        }
        return new WxIsvAppPair(platform, merchant);
    }

    /// 按 channelAppId 解析单应用
    private WxAppView resolveByChannelAppId(String mchNo, String channelAppId) {
        var platform = wxPlatformAppManager.findByWxAppId(channelAppId);
        if (platform.isPresent()) {
            return this.toPlatformView(platform.get());
        }
        var mchApp = wxMchAppManager.findByMchNoAndWxAppId(mchNo, channelAppId);
        if (mchApp.isPresent()) {
            return this.toMerchantView(mchApp.get());
        }
        // 微信: 指定 AppId 未配置
        throw new DataNotExistException("error.payment.wx.channelAppIdNotFound", channelAppId);
    }

    /// appType 推导兜底：要求该类型平台应用唯一命中
    ///
    /// - 唯一命中：返回该应用
    /// - 该类型存在多个平台应用：抛 notUnique，要求显式配置通道能力绑
    /// - 该类型无平台应用：返回 null，由调用方走最终报错
    private WxAppView resolvePlatformFallback(String capability) {
        if (StrUtil.isBlank(capability)) {
            return null;
        }
        // appType 推导：要求该类型平台应用唯一命中，>1 拒绝猜测
        String appType = WxAppTypeEnum.resolveAppType(capability);
        if (appType != null) {
            List<WxPlatformApp> apps = wxPlatformAppManager.listByAppType(appType);
            if (apps.size() > 1) {
                // 存在多个同类型平台应用，请显式配置通道能力绑定以明确选择
                throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                        "error.payment.wx.appNotUnique", appType);
            }
            if (apps.size() == 1) {
                return this.toPlatformView(apps.getFirst());
            }
        }
        return null;
    }

    /// 平台应用 → View
    private WxAppView toPlatformView(WxPlatformApp app) {
        String secret = wxPlatformAppAuthConfigService.findByWxPlatformAppId(app.getId()).getAppSecret();
        return new WxAppView(WxAppScopeEnum.PLATFORM, app.getId(), app.getWxAppId(),
                app.getAppType(), secret, app.getAppName());
    }

    /// 商户应用 → View
    private WxAppView toMerchantView(WxMchApp app) {
        String secret = wxMchAppAuthConfigService.findByWxMchAppId(app.getId()).getAppSecret();
        return new WxAppView(WxAppScopeEnum.MERCHANT, app.getId(), app.getWxAppId(),
                app.getAppType(), secret, app.getAppName());
    }
}
