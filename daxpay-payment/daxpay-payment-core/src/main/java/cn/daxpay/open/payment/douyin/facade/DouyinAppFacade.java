package cn.daxpay.open.payment.douyin.facade;

import cn.daxpay.open.payment.auth.core.AppScopeEnum;

/// # 抖音开放应用解析门面
///
/// 供抖音/拉卡拉/易宝等通道统一解析 douyinAppId + Secret，避免依赖通道私有应用表。
///
public interface DouyinAppFacade {

    /// 按档位与主键加载应用视图(含 Auth)
    DyAppView getById(AppScopeEnum scope, Long id);

    /// 解析单应用：显式 channelAppId → 通道能力绑 → 产品级平台默认能力绑
    ///
    /// @param product 支付产品编码（PayProduct.code），平台默认绑按产品隔离
    DyAppView resolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product);

    /// 解析 ISV 双应用：platform(sp) + optional merchant(sub)
    ///
    /// @param product 支付产品编码（PayProduct.code）
    DyIsvAppPair resolveIsvPair(String mchNo, String channelMchNo, String capability, String channelAppId, String product);

    /// H5 silent_auth / JS-SDK 验签用网站应用解析(抖音特有)
    ///
    /// 抖音 H5 授权固定使用网站应用(web_app), 与支付能力按 capability 解析的小程序/移动应用不同。
    /// 优先级: channelAppId 显式 → 商户档 web_app 首个 → 平台档 web_app 唯一命中。
    ///
    /// @param mchNo        商户号(商户档应用查询条件)
    /// @param channelMchNo 通道商户号(保留对齐签名, 当前 web_app 解析按 mchNo 隔离)
    /// @param channelAppId 显式抖音 AppId(可空)
    DyAppView resolveWebAppForH5Auth(String mchNo, String channelMchNo, String channelAppId);

    /// 按真实 douyinAppId 解析：商户档优先, 平台档兜底
    ///
    /// 供开放接口认证场景使用: 对接方传入真实抖音 AppId, 系统自行定位到对应应用。
    /// @param mchNo 商户号(商户档查询条件)
    /// @param douyinAppId 抖音 AppId(真实值, 非复合格式)
    DyAppView resolveByDouyinAppId(String mchNo, String douyinAppId);
}
