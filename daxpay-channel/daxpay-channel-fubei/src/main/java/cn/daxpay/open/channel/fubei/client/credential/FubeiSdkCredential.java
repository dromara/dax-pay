package cn.daxpay.open.channel.fubei.client.credential;

import lombok.Data;

/// # 付呗 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置(FubeiDirectKeyConfig)提取付呗开放平台应用ID(app_id)、接口密钥(app_secret)、
/// 商户ID(merchant_id, 服务商形态)与门店ID(store_id)后组装, 下发给子应用发起付呗开放平台网关调用。
///
/// 仅承载通道调用所需的身份与密钥信息, 不含任何业务字段。
@Data
public class FubeiSdkCredential {

    /// 付呗开放平台应用ID(app_id)
    private String appId;

    /// 接口密钥(app_secret, MD5 签名与回调验签共用)
    private String appSecret;

    /// 付呗商户ID(merchant_id, 服务商形态时必传)
    private String merchantId;

    /// 付呗门店ID(store_id, 交易必传)
    private String storeId;

    /// 是否沙箱环境(付呗无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
