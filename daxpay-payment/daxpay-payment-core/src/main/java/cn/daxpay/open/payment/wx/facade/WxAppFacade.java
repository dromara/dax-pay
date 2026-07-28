package cn.daxpay.open.payment.wx.facade;

import cn.daxpay.open.payment.wx.enums.WxAppScopeEnum;

/// # 微信开放应用解析门面
///
/// 供微信/拉卡拉/易宝等通道统一解析 wxAppId + Secret，避免依赖通道私有应用表。
///
public interface WxAppFacade {

    /// 按档位与主键加载应用视图(含 Auth)
    WxAppView getById(WxAppScopeEnum scope, Long id);

    /// 解析单应用：显式 channelAppId → 通道能力绑 → 产品级平台默认能力绑
    ///
    /// @param product 支付产品编码（PayProduct.code），平台默认绑按产品隔离
    WxAppView resolve(String mchNo, String channelMchNo, String capability, String channelAppId, String product);

    /// 解析 ISV 双应用：platform(sp) + optional merchant(sub)
    ///
    /// @param product 支付产品编码（PayProduct.code）
    WxIsvAppPair resolveIsvPair(String mchNo, String channelMchNo, String capability, String channelAppId, String product);

    /// 按真实 wxAppId 解析：商户档优先, 平台档兜底
    ///
    /// 供开放接口认证场景使用: 对接方传入真实微信 AppId, 系统自行定位到对应应用。
    /// @param mchNo 商户号(商户档查询条件)
    /// @param wxAppId 微信 AppId(真实值, 非复合格式)
    WxAppView resolveByWxAppId(String mchNo, String wxAppId);
}
