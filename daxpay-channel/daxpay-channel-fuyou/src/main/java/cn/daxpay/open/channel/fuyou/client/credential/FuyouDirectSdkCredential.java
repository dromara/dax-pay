package cn.daxpay.open.channel.fuyou.client.credential;

import lombok.Data;

/// # 富友直连 SDK 凭证
///
/// 主应用从直连商户密钥配置(FuyouDirectKeyConfig)提取商户号 / 商户密钥 / 终端号后组装,
/// 下发给子应用发起富友「商户移动支付前置接口」(aggregatePay) 调用。
///
/// 与服务商凭证([FuyouSdkCredential])的差异: 无机构号(ins_cd)与 RSA 密钥对,
/// 以商户密钥(mchnt_key)做 MD5 签名。
@Data
public class FuyouDirectSdkCredential {

    /// 富友商户编号(mchnt_cd)
    private String merchantNo;

    /// 商户密钥(mchnt_key, MD5 签名用)
    private String mchntKey;

    /// 终端号(term_id, 8 位随机数字字母组合)
    private String termNo;

    /// 订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)
    private String orderPrefix;

    /// 是否沙箱环境
    private Boolean sandbox;
}
