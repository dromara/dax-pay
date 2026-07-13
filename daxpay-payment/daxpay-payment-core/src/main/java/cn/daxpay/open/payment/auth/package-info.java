/// # 支付通道 OAuth 认证(Channel OAuth)
///
/// **本包不是 IAM 登录**。职责仅为支付场景获取用户标识(微信 openId / 支付宝 userId 等)。
/// 与 [cn.daxpay.open.platform.capability.auth](Sa-Token 登录)、社交登录(capability-social) 无关。
///
/// ## 类职责
/// | 类 | 职责 |
/// |----|------|
/// | [AuthSession] | H5 OAuth 重定向的会话上下文 POJO(product/channelMchNo/source/queryCode 等) |
/// | [AuthSessionStore] | Redis: authToken→session、queryCode→AuthResult, TTL 5 分钟 |
/// | [ChannelAuthService] | **商户级**: 按支付产品路由 [cn.daxpay.open.payment.strategy.auth.AbsChannelAuthStrategy] |
/// | [PlatformAuthService] | **平台级**: 平台支付宝 / 系统公众号 / 抖音 H5 配置驱动的 OAuth |
/// | [ChannelAuthFacade] | 统一分发入口(generate / auth), Controller 只做协议适配 |
///
/// ## 时序
/// 1. generateAuthUrl → 写 session(authToken) + WAITING(queryCode) → 返回 OAuth URL
/// 2. 用户授权, 第三方回调 H5 固定路径(`/auth/wechat|alipay|douyin`), state=authToken
/// 3. H5 调 auth → Facade 按 source 分流 → code 换 openId → 写轮询结果 → 删除 session
/// 4. (调试) 管理端用 queryCode 轮询 AuthResult
///
/// ## 平台级 vs 商户级
/// - 平台级: 不读商户通道配置, session.source = platform_alipay / platform_mp / platform_douyin
/// - 商户级: 按 product + channelMchNo/capability 定位通道应用, source 为空
package cn.daxpay.open.payment.auth;
