package cn.daxpay.open.payment.wx.service;

import cn.daxpay.open.payment.wx.dao.channel.WxChannelAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppManager;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.wx.entity.merchant.WxMchApp;
import cn.daxpay.open.payment.wx.entity.platform.WxPlatformApp;
import cn.daxpay.open.payment.wx.entity.platform.WxPlatformAppCapability;

import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final WxChannelAppCapabilityManager wxChannelAppCapabilityManager;
    private final WxPlatformAppCapabilityManager wxPlatformAppCapabilityManager;

    /// 按档位与主键加载应用视图（含 Auth）
    @Override
    public WxAppView getById(AppScopeEnum scope, Long id) {
        if (scope == null || id == null) {
            // 微信: 档位不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.scopeNotExist");
        }
        if (scope == AppScopeEnum.PLATFORM) {
            WxPlatformApp app = wxPlatformAppManager.findById(id)
                    // 微信: 平台应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
            return new WxAppView(scope, app.getId(), app.getWxAppId(), app.getAppType(), app.getAppSecret(), app.getAppName());
        }
        if (scope == AppScopeEnum.MERCHANT) {
            WxMchApp app = wxMchAppManager.findById(id)
                    // 微信: 商户应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
            return new WxAppView(scope, app.getId(), app.getWxAppId(), app.getAppType(), app.getAppSecret(), app.getAppName());
        }
        // 微信: 档位不存在
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.wx.scopeNotExist");
    }

    /// 解析单应用：显式 channelAppId → 通道能力绑 → 产品级平台默认绑
    ///
    /// 直连产品(WECHAT_PAY)禁止回退到平台档应用: OAuth 拿到的 openId 必须与
    /// 直连商户号同主体, 使用平台应用会导致 appid 与 mchid 主体不一致。
    /// 仅服务商(WECHAT_ISV)及其他产品允许平台档兜底。
    @Override
    public WxAppView resolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product) {
        Optional<WxAppView> resolved = this.tryResolve(mchNo, channelMchNo, capability, channelAppId, product);
        if (resolved.isPresent()) {
            return resolved.get();
        }
        // 解析失败, 按场景抛对应异常(语义与重构前完全一致)
        boolean direct = ProductEnum.WECHAT_PAY.getCode().equals(product);
        // 显式指定了 channelAppId 但未命中
        if (StrUtil.isNotBlank(channelAppId)) {
            if (direct) {
                // 直连商户未配置商户档应用
                throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                        "error.payment.wx.directMchAppNotConfigured", channelAppId);
            }
            // 微信: 指定 AppId 未配置
            throw new DataNotExistException("error.payment.wx.channelAppIdNotFound", channelAppId);
        }
        // 未指定 channelAppId, 能力绑定/默认绑均未命中
        if (direct) {
            // 微信: 直连商户未配置商户档应用
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.directMchAppNotConfigured", capability);
        }
        // 微信: 未配置该能力对应的应用
        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                "error.payment.wx.appNotConfigured", capability);
    }

    /// 尽力解析单应用，解析不到返回 [Optional.empty] 而非抛异常
    ///
    /// 解析顺序与 [resolve] 完全一致, 供聚合通道(如 Adapay)在 wxAppId 可选场景使用。
    @Override
    public Optional<WxAppView> resolveOptional(String mchNo, String channelMchNo, String capability, String channelAppId, String product) {
        return this.tryResolve(mchNo, channelMchNo, capability, channelAppId, product);
    }

    /// 尝试解析单应用, 解析失败返回 empty(不抛异常)
    ///
    /// 解析顺序: 显式 channelAppId → 通道能力绑 → 产品级平台默认绑。
    /// [resolve] 与 [resolveOptional] 的共享内核, 区别仅在上层是否对 empty 抛异常。
    private Optional<WxAppView> tryResolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product) {
        boolean direct = ProductEnum.WECHAT_PAY.getCode().equals(product);
        // 1. 显式 channelAppId
        if (StrUtil.isNotBlank(channelAppId)) {
            return this.tryResolveByChannelAppId(mchNo, channelAppId, direct);
        }
        // 2. 通道能力绑定（同能力优先 merchant，其次 platform）
        if (StrUtil.isNotBlank(channelMchNo) && StrUtil.isNotBlank(capability)) {
            var merchantBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                return Optional.of(this.getById(AppScopeEnum.MERCHANT, merchantBind.get().getWxAppRefId()));
            }
            // 直连商户不使用平台档应用, 跳过平台能力绑定
            if (!direct) {
                var platformBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                        channelMchNo, capability, AppScopeEnum.PLATFORM.getCode());
                if (platformBind.isPresent()) {
                    return Optional.of(this.getById(AppScopeEnum.PLATFORM, platformBind.get().getWxAppRefId()));
                }
            }
        }
        // 3. 产品级平台默认能力绑（wx_platform_app_capability 按 (product, capability) 取；仅非直连）
        if (!direct) {
            WxAppView productDefault = this.resolveProductDefault(product, capability);
            if (productDefault != null) {
                return Optional.of(productDefault);
            }
        }
        return Optional.empty();
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
                    channelMchNo, capability, AppScopeEnum.PLATFORM.getCode());
            if (platformBind.isPresent()) {
                platform = this.getById(AppScopeEnum.PLATFORM, platformBind.get().getWxAppRefId());
            }
            var merchantBind = wxChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                merchant = this.getById(AppScopeEnum.MERCHANT, merchantBind.get().getWxAppRefId());
            }
        }
        // 平台侧回退：产品级平台默认绑（按 product）
        if (platform == null) {
            platform = this.resolveProductDefault(product, capability);
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

    /// 按 channelAppId 尝试解析, 未命中返回 empty(不抛异常)
    ///
    /// @param direct 是否直连产品(直连时商户表优先且不回退平台表)
    private Optional<WxAppView> tryResolveByChannelAppId(String mchNo, String channelAppId, boolean direct) {
        // 直连商户优先查商户表, 且不回退到平台表
        if (direct) {
            return wxMchAppManager.findByMchNoAndWxAppId(mchNo, channelAppId)
                    .map(this::toMerchantView);
        }
        var platform = wxPlatformAppManager.findByWxAppId(channelAppId);
        if (platform.isPresent()) {
            return Optional.of(this.toPlatformView(platform.get()));
        }
        return wxMchAppManager.findByMchNoAndWxAppId(mchNo, channelAppId)
                .map(this::toMerchantView);
    }

    /// 产品级平台默认能力绑兜底：按 (product, capability) 查 wx_platform_app_capability
    ///
    /// 供服务商/聚合类产品(如 leshua_pay)在通道商户未绑平台应用时, 回退到运营端配置的产品默认平台应用;
    /// product 为空(如 UMS 传 null)或未配置绑定返回 null, 由调用方报错。
    private WxAppView resolveProductDefault(String product, String capability) {
        if (StrUtil.hasBlank(product, capability)) {
            return null;
        }
        Optional<WxPlatformAppCapability> rel = wxPlatformAppCapabilityManager.findByProductAndCapability(product, capability);
        if (rel.isEmpty()) {
            return null;
        }
        WxPlatformApp app = wxPlatformAppManager.findById(rel.get().getWxPlatformAppId())
                // 微信: 平台应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.appNotFound"));
        return this.toPlatformView(app);
    }

    /// 平台应用 → View
    private WxAppView toPlatformView(WxPlatformApp app) {
        return new WxAppView(AppScopeEnum.PLATFORM, app.getId(), app.getWxAppId(),
                app.getAppType(), app.getAppSecret(), app.getAppName());
    }

    /// 商户应用 → View
    private WxAppView toMerchantView(WxMchApp app) {
        return new WxAppView(AppScopeEnum.MERCHANT, app.getId(), app.getWxAppId(),
                app.getAppType(), app.getAppSecret(), app.getAppName());
    }

    /// 按真实 wxAppId 解析：商户档优先, 平台档兜底
    ///
    /// 供开放接口认证场景: 对接方传入真实微信 AppId, 系统自行定位到对应应用。
    /// 与 [tryResolveByChannelAppId] 的平台优先不同, 此方法商户优先(更严格安全边界)。
    @Override
    public WxAppView resolveByWxAppId(String mchNo, String wxAppId) {
        var mchApp = wxMchAppManager.findByMchNoAndWxAppId(mchNo, wxAppId);
        if (mchApp.isPresent()) {
            return this.toMerchantView(mchApp.get());
        }
        var platform = wxPlatformAppManager.findByWxAppId(wxAppId);
        if (platform.isPresent()) {
            return this.toPlatformView(platform.get());
        }
        // 微信: 指定 AppId 未配置
        throw new DataNotExistException("error.payment.wx.channelAppIdNotFound", wxAppId);
    }
}
