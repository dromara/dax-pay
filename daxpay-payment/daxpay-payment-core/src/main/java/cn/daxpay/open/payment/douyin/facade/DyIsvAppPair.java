package cn.daxpay.open.payment.douyin.facade;

/// # 抖音 ISV 双应用对
///
/// platform 为必填(sp_appid)；merchant 可选(sub_appid)。
///
/// @param platform 平台档应用(必填)
/// @param merchant 商户档应用(可选，可为 null)
///
public record DyIsvAppPair(
        DyAppView platform,
        DyAppView merchant
) {
}
