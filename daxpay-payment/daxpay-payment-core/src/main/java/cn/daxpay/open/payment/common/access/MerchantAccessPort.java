package cn.daxpay.open.payment.common.access;

/// # 商户接入信息查询端口（运行态 Access）
///
/// 定义在 common，供装载器 / 验签 / 登录检查等横切能力依赖，
/// 避免 common 直接依赖 merchant DAO 实现。
/// 实现见 `merchant.service.access.MerchantAccessQueryService`。
///
/// 引导类读路径统一走 `*NotTenant`；`initMch` 之后的业务读（如公钥）走租户内查询。
public interface MerchantAccessPort {

    /// 根据商户号查询商户接入信息（引导用，忽略租户）
    MerchantAccessInfo getMerchantByMchNo(String mchNo);

    /// 根据商户号查询默认应用（引导用，忽略租户）
    MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo);

    /// 根据应用号查询应用（引导用，忽略租户）
    MchAppInfoAccessInfo getAppByAppId(String appId);

    /// 根据商户号查询商户公钥
    ///
    /// 调用方须已完成 `initMch`（或回调控制器已 `bindMchNoForCallback`），走**租户内**查询。
    String findMerchantPublicKey(String mchNo);
}
