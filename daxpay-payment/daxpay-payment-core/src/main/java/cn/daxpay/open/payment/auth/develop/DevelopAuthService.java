package cn.daxpay.open.payment.auth.develop;

import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.platform.AlipayAuthProvider;
import cn.daxpay.open.payment.auth.platform.DouyinH5AuthProvider;
import cn.daxpay.open.payment.auth.platform.WechatMpAuthProvider;
import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;

/// # 认证调试服务(运营端 / 商户端共用)
///
/// 调试入口, 按认证来源分别委托:
/// - **支付宝(平台级)**: 委托 [AlipayAuthProvider] 生成 OAuth 授权链接, 轮询 queryCode 取结果
/// - **微信公众号配置(平台级)**: 委托 [WechatMpAuthProvider], OAuth 重定向取 openId, 仅验证配置是否正确
/// - **抖音H5(平台级)**: 委托 [DouyinH5AuthProvider], silent_auth 静默授权取 openId, 仅验证配置是否正确
/// - **微信支付(直连/服务商)**: 委托 [MerchantChannelAuthService] 按支付产品路由认证策略,
///   依赖商户上下文(channelMchNo/产品/能力)
/// - **抖音支付(直连/服务商)**: 同微信支付, 显式选抖音网站应用 → [MerchantChannelAuthService]
///
/// 小程序(微信/支付宝/抖音)获取 openId 已迁至收银台小程序运行时
/// ([cn.daxpay.open.payment.trade.runtime.service.pay.gateway.CashierAuthService]),
/// Web 认证调试不再提供小程序 code 直换入口。
///
/// 已实现项共用 queryCode 轮询机制(由 [AuthSessionStore#queryAuthResult] 统一查询)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopAuthService {

    private final AlipayAuthProvider alipayAuthProvider;
    private final WechatMpAuthProvider wechatMpAuthProvider;
    private final DouyinH5AuthProvider douyinH5AuthProvider;
    private final MerchantChannelAuthService merchantChannelAuthService;
    private final AuthSessionStore authSessionStore;
    private final WxAppFacade wxAppFacade;
    private final DouyinAppFacade douyinAppFacade;

    /// 生成支付宝授权链接(平台级 OAuth + queryCode 轮询)
    public AuthUrlResult generateAlipayAuthUrl() {
        return alipayAuthProvider.generateAuthUrl(null);
    }

    /// 生成微信公众号配置授权链接(平台级, 仅调试)
    public AuthUrlResult generateWechatMpAuthUrl() {
        return wechatMpAuthProvider.generateAuthUrl(null);
    }

    /// 生成抖音 H5 授权链接(平台级, 仅调试)
    public AuthUrlResult generateDouyinAuthUrl() {
        return douyinH5AuthProvider.generateAuthUrl(null);
    }

    /// 生成微信支付(直连/服务商)授权链接
    ///
    /// 防腐层: 调试专用参数 → [GenerateAuthUrlParam], 再委托 [MerchantChannelAuthService#generateAuthUrl]。
    /// authType 固定补 wechat(调试仅支持微信支付, 抖音走 generateDouyinAuthUrl)。
    /// 应用解析: 显式 scope + appId → [WxAppFacade#getById] 精确加载,
    /// 把档位(appScope)+主键(appRefId)塞入 param 供策略查密钥, 不再做路由推断。
    public AuthUrlResult generateChannelAuthUrl(DevelopChannelAuthParam param) {
        // String code → 枚举, 非法值返回 null 由 getById 兜底抛 error.payment.wx.scopeNotExist
        AppScopeEnum scope = AppScopeEnum.findByCode(param.getScope());
        WxAppView app = wxAppFacade.getById(scope, param.getAppId());
        GenerateAuthUrlParam inner = new GenerateAuthUrlParam();
        inner.setAuthType(ChannelAuthTypeEnum.WECHAT.getCode());
        inner.setMchNo(param.getMchNo());
        // 调试入口已显式指定 scope+appId, 精确加载后把档位+主键塞入 param 供策略查密钥
        inner.setAppScope(app.scope().getCode());
        inner.setAppRefId(app.id());
        return merchantChannelAuthService.generateAuthUrl(inner);
    }

    /// 生成抖音支付(直连/服务商)授权链接
    ///
    /// 防腐层: 调试专用参数 → [GenerateAuthUrlParam], 再委托 [MerchantChannelAuthService#generateAuthUrl]。
    /// authType 固定补 douyin(平台级 H5 调试走 generateDouyinAuthUrl)。
    /// 应用解析: 显式 scope + appId → [DouyinAppFacade#getById] 精确加载,
    /// 把档位(appScope)+主键(appRefId)塞入 param 供策略查密钥, 不再做路由推断。
    public AuthUrlResult generateDouyinChannelAuthUrl(DevelopChannelAuthParam param) {
        // String code → 枚举, 非法值返回 null 由 getById 兜底抛 error.payment.douyin.scopeNotExist
        AppScopeEnum scope = AppScopeEnum.findByCode(param.getScope());
        DyAppView app = douyinAppFacade.getById(scope, param.getAppId());
        GenerateAuthUrlParam inner = new GenerateAuthUrlParam();
        inner.setAuthType(ChannelAuthTypeEnum.DOUYIN.getCode());
        inner.setMchNo(param.getMchNo());
        // 调试入口已显式指定 scope+appId, 精确加载后把档位+主键塞入 param 供策略查密钥
        inner.setAppScope(app.scope().getCode());
        inner.setAppRefId(app.id());
        return merchantChannelAuthService.generateAuthUrl(inner);
    }

    /// 通过查询码获取认证结果
    public AuthResult queryAuthResult(String queryCode) {
        return authSessionStore.queryAuthResult(queryCode);
    }
}
