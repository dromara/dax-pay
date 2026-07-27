package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 认证调试服务(运营端 / 商户端共用)
///
/// 调试入口, 按认证来源分别委托:
/// - **支付宝(平台级)**: 委托 [PlatformAuthService] 生成 OAuth 授权链接, 轮询 queryCode 取结果
/// - **微信公众号配置(平台级)**: 委托 [PlatformAuthService], OAuth 重定向取 openId, 仅验证配置是否正确
/// - **抖音H5(平台级)**: 委托 [PlatformAuthService], silent_auth 静默授权取 openId, 仅验证配置是否正确
/// - **微信支付(直连/服务商)**: 委托 [ChannelProductAuthService] 按支付产品路由认证策略,
///   依赖商户上下文(channelMchNo/产品/能力)
/// - **支付宝小程序**: 暂未实现
/// - **微信小程序(商户端/运营端)**: 暂未实现
///
/// 已实现项共用 queryCode 轮询机制(由 [AuthSessionStore#queryAuthResult] 统一查询)。
///
/// ## 重构说明
/// 原 `DevelopAuthAdminService` 与 `MchDevelopAuthService` 逐行相同, 已合并到 payment-core 本类,
/// 运营端/商户端 Controller 直接注入, 消除冗余中间层。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopAuthService {

    private final PlatformAuthService platformAuthService;
    private final ChannelProductAuthService channelProductAuthService;
    private final AuthSessionStore authSessionStore;

    /// 生成支付宝授权链接(平台级 OAuth + queryCode 轮询)
    public AuthUrlResult generateAlipayAuthUrl() {
        return platformAuthService.generateAlipayAuthUrl();
    }

    /// 生成微信公众号配置授权链接(平台级, 仅调试)
    public AuthUrlResult generateWechatMpAuthUrl() {
        return platformAuthService.generateWechatMpAuthUrl();
    }

    /// 生成抖音 H5 授权链接(平台级, 仅调试)
    public AuthUrlResult generateDouyinAuthUrl() {
        return platformAuthService.generateDouyinAuthUrl();
    }

    /// 生成微信支付(直连/服务商)授权链接
    ///
    /// 委托 [ChannelProductAuthService#generateAuthUrl], 按支付产品路由对应认证策略:
    /// 直连(WECHAT_PAY) → WechatDirectAuthStrategy; 服务商(WECHAT_ISV) → WechatIsvAuthStrategy。
    public AuthUrlResult generateChannelAuthUrl(GenerateAuthUrlParam param) {
        return channelProductAuthService.generateAuthUrl(param);
    }

    /// 通过查询码获取认证结果
    public AuthResult queryAuthResult(String queryCode) {
        return authSessionStore.queryAuthResult(queryCode);
    }
}
