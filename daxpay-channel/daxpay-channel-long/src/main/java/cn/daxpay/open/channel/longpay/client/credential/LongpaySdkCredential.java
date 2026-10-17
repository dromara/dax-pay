package cn.daxpay.open.channel.longpay.client.credential;

import lombok.Data;

/// # 建行龙支付 SDK 凭证(主应用侧)
///
/// 从直连密钥配置(LongpayDirectKeyConfig)提取商户三要素与两把公钥后组装, 下发给子应用。
@Data
public class LongpaySdkCredential {

    /// 建行商户号(MERCHANTID)
    private String merchantId;

    /// 建行柜台号(POSID)
    private String posId;

    /// 建行分行号(BRANCHID)
    private String branchId;

    /// 柜台公钥(请求 MD5 摘要用, 取后 30 位参与 MAC)
    private String merchantPublicKey;

    /// 建行公钥(异步通知 RSA 验签用, DER hex 或 PEM)
    private String ccbPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;
}
