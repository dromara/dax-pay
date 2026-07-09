package cn.daxpay.open.channel.wechat.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 微信服务商认证应用类型
///
/// 服务商模式下授权获取 openId 时, 控制使用服务商应用还是子商户应用。
/// 配置挂在特约商户维度([cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant]), 默认 SP_APP。
@Getter
@RequiredArgsConstructor
public enum WechatAuthAppTypeEnum {

    /// 服务商应用(WechatIsvApp.sp_appid)
    SP_APP("SP_APP"),
    /// 子商户应用(WechatIsvMchApp.sub_appid)
    SUB_APP("SUB_APP");

    /// 编码
    private final String code;
}
