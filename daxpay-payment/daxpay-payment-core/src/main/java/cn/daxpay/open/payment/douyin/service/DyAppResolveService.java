package cn.daxpay.open.payment.douyin.service;

import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.douyin.dao.channel.DyChannelAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppManager;
import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.payment.douyin.facade.DyIsvAppPair;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 抖音开放应用解析服务
///
/// 实现 [DouyinAppFacade]，供通道组装器统一解析 douyinAppId + Secret。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyAppResolveService implements DouyinAppFacade {

    private final DyPlatformAppManager dyPlatformAppManager;
    private final DyMchAppManager dyMchAppManager;
    private final DyChannelAppCapabilityManager dyChannelAppCapabilityManager;

    /// 按档位与主键加载应用视图（含 Auth）
    @Override
    public DyAppView getById(AppScopeEnum scope, Long id) {
        if (scope == null || id == null) {
            // 抖音: 档位不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.douyin.scopeNotExist");
        }
        if (scope == AppScopeEnum.PLATFORM) {
            DyPlatformApp app = dyPlatformAppManager.findById(id)
                    // 抖音: 平台应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.douyin.appNotFound"));
            return new DyAppView(scope, app.getId(), app.getDouyinAppId(), app.getAppType(), app.getAppSecret(), app.getAppName());
        }
        if (scope == AppScopeEnum.MERCHANT) {
            DyMchApp app = dyMchAppManager.findById(id)
                    // 抖音: 商户应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.douyin.mchAppNotFound"));
            return new DyAppView(scope, app.getId(), app.getDouyinAppId(), app.getAppType(), app.getAppSecret(), app.getAppName());
        }
        // 抖音: 档位不存在
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.douyin.scopeNotExist");
    }

    /// 解析单应用：显式 channelAppId → 通道能力绑 → appType 推导（平台应用唯一命中）
    @Override
    public DyAppView resolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product) {
        // 1. 显式 channelAppId
        if (StrUtil.isNotBlank(channelAppId)) {
            return this.resolveByChannelAppId(mchNo, channelAppId);
        }
        // 2. 通道能力绑定（同能力优先 merchant，其次 platform）
        if (StrUtil.isNotBlank(channelMchNo) && StrUtil.isNotBlank(capability)) {
            var merchantBind = dyChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                return this.getById(AppScopeEnum.MERCHANT, merchantBind.get().getDyAppRefId());
            }
            var platformBind = dyChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.PLATFORM.getCode());
            if (platformBind.isPresent()) {
                return this.getById(AppScopeEnum.PLATFORM, platformBind.get().getDyAppRefId());
            }
        }
        // 3. appType 推导：要求该类型平台应用唯一命中
        DyAppView platformFallback = this.resolvePlatformFallback(capability);
        if (platformFallback != null) {
            return platformFallback;
        }
        // 抖音: 未配置该能力对应的应用
        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                "error.payment.douyin.appNotConfigured", capability);
    }

    /// 解析 ISV 双应用：platform(sp) 必填 + merchant(sub) 可选
    @Override
    public DyIsvAppPair resolveIsvPair(String mchNo, String channelMchNo, String capability, String channelAppId,
            String product) {
        DyAppView platform = null;
        DyAppView merchant = null;
        // 通道绑定：platform / merchant 各取一行
        if (StrUtil.isNotBlank(channelMchNo) && StrUtil.isNotBlank(capability)) {
            var platformBind = dyChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.PLATFORM.getCode());
            if (platformBind.isPresent()) {
                platform = this.getById(AppScopeEnum.PLATFORM, platformBind.get().getDyAppRefId());
            }
            var merchantBind = dyChannelAppCapabilityManager.findByChannelMchNoAndCapabilityAndScope(
                    channelMchNo, capability, AppScopeEnum.MERCHANT.getCode());
            if (merchantBind.isPresent()) {
                merchant = this.getById(AppScopeEnum.MERCHANT, merchantBind.get().getDyAppRefId());
            }
        }
        // 平台侧回退：appType 推导（平台应用唯一命中）
        if (platform == null) {
            platform = this.resolvePlatformFallback(capability);
        }
        // channelAppId 命中某侧时按命中侧覆盖
        if (StrUtil.isNotBlank(channelAppId)) {
            var hitPlatform = dyPlatformAppManager.findByDouyinAppId(channelAppId);
            if (hitPlatform.isPresent()) {
                platform = this.toPlatformView(hitPlatform.get());
            }
            else {
                var hitMerchant = dyMchAppManager.findByMchNoAndDouyinAppId(mchNo, channelAppId);
                if (hitMerchant.isPresent()) {
                    merchant = this.toMerchantView(hitMerchant.get());
                }
                else {
                    // 抖音: 指定 AppId 未配置
                    throw new DataNotExistException("error.payment.douyin.channelAppIdNotFound", channelAppId);
                }
            }
        }
        if (platform == null) {
            // 抖音: 未配置该能力对应的平台应用（sp 必填）
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.douyin.appNotConfigured", capability);
        }
        return new DyIsvAppPair(platform, merchant);
    }

    /// 按 channelAppId 解析单应用
    private DyAppView resolveByChannelAppId(String mchNo, String channelAppId) {
        var platform = dyPlatformAppManager.findByDouyinAppId(channelAppId);
        if (platform.isPresent()) {
            return this.toPlatformView(platform.get());
        }
        var mchApp = dyMchAppManager.findByMchNoAndDouyinAppId(mchNo, channelAppId);
        if (mchApp.isPresent()) {
            return this.toMerchantView(mchApp.get());
        }
        // 抖音: 指定 AppId 未配置
        throw new DataNotExistException("error.payment.douyin.channelAppIdNotFound", channelAppId);
    }

    /// appType 推导兜底：按兼容类型优先级遍历, 首个唯一命中的平台应用返回
    ///
    /// - 某类型恰好 1 个：返回该应用
    /// - 某类型多个/无：跳过, 继续下一个兼容类型
    /// - 全部兼容类型均未唯一命中：返回 null, 由调用方走最终报错
    private DyAppView resolvePlatformFallback(String capability) {
        if (StrUtil.isBlank(capability)) {
            return null;
        }
        // 遍历兼容 appType(按优先级), 首个唯一命中即返回; 某类型存在多个则跳过, 避免猜测
        for (String appType : DyAppTypeEnum.resolveCompatibleAppTypes(capability)) {
            List<DyPlatformApp> apps = dyPlatformAppManager.listByAppType(appType);
            if (apps.size() == 1) {
                return this.toPlatformView(apps.getFirst());
            }
        }
        return null;
    }

    /// 平台应用 → View
    private DyAppView toPlatformView(DyPlatformApp app) {
        return new DyAppView(AppScopeEnum.PLATFORM, app.getId(), app.getDouyinAppId(),
                app.getAppType(), app.getAppSecret(), app.getAppName());
    }

    /// 商户应用 → View
    private DyAppView toMerchantView(DyMchApp app) {
        return new DyAppView(AppScopeEnum.MERCHANT, app.getId(), app.getDouyinAppId(),
                app.getAppType(), app.getAppSecret(), app.getAppName());
    }

    /// H5 silent_auth / JS-SDK 验签用网站应用解析(抖音特有)
    ///
    /// 优先级: channelAppId 显式 → 商户档 web_app 首个 → 平台档 web_app 唯一命中。
    @Override
    public DyAppView resolveWebAppForH5Auth(String mchNo, String channelMchNo, String channelAppId) {
        // 1. channelAppId 显式
        if (StrUtil.isNotBlank(channelAppId)) {
            return this.resolveByChannelAppId(mchNo, channelAppId);
        }
        // 2. 商户档 web_app
        if (StrUtil.isNotBlank(mchNo)) {
            var mchApp = dyMchAppManager.findFirstByMchNoAndAppType(mchNo, DyAppTypeEnum.WEB_APP.getCode());
            if (mchApp.isPresent()) {
                return this.toMerchantView(mchApp.get());
            }
        }
        // 3. 平台档 web_app 唯一命中
        List<DyPlatformApp> webApps = dyPlatformAppManager.listByAppType(DyAppTypeEnum.WEB_APP.getCode());
        // 网站应用类型本地化名称
        String webAppTypeLabel = I18nUtil.getEnumName(DyAppTypeEnum.WEB_APP);
        if (webApps.size() > 1) {
            // 存在多个同类型平台应用，请显式配置能力绑定以明确选择
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.douyin.appNotUnique", webAppTypeLabel);
        }
        if (webApps.size() == 1) {
            return this.toPlatformView(webApps.getFirst());
        }
        // 抖音: 未配置 H5 验签所需的 web_app 类型应用
        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                "error.payment.douyin.appNotConfigured", webAppTypeLabel);
    }

    /// 按真实 douyinAppId 解析：商户档优先, 平台档兜底
    ///
    /// 供开放接口认证场景: 对接方传入真实抖音 AppId, 系统自行定位到对应应用。
    /// 与 [resolveByChannelAppId] 的平台优先不同, 此方法商户优先(更严格安全边界)。
    @Override
    public DyAppView resolveByDouyinAppId(String mchNo, String douyinAppId) {
        var mchApp = dyMchAppManager.findByMchNoAndDouyinAppId(mchNo, douyinAppId);
        if (mchApp.isPresent()) {
            return this.toMerchantView(mchApp.get());
        }
        var platform = dyPlatformAppManager.findByDouyinAppId(douyinAppId);
        if (platform.isPresent()) {
            return this.toPlatformView(platform.get());
        }
        // 抖音: 指定 AppId 未配置
        throw new DataNotExistException("error.payment.douyin.channelAppIdNotFound", douyinAppId);
    }
}
