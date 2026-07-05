package cn.daxpay.open.channel.wechat.code;

/// # 微信支付常量
///
/// 定义微信支付相关的常量，包括认证方式(仅API V3证书模式)和用户标识类型等。
///
public interface WechatCode {

    /// 回调签名 header: 平台证书序列号 / 支付公钥ID
    String HEADER_SERIAL = "Wechatpay-Serial";
    /// 回调签名 header: 随机串
    String HEADER_NONCE = "Wechatpay-Nonce";
    /// 回调签名 header: 签名
    String HEADER_SIGNATURE = "Wechatpay-Signature";
    /// 回调签名 header: 时间戳
    String HEADER_TIMESTAMP = "Wechatpay-Timestamp";

    /// 回调成功应答(微信要求 JSON 格式)
    String NOTIFY_SUCCESS = "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";
    /// 回调失败应答
    String NOTIFY_FAIL = "{\"code\":\"FAIL\",\"message\":\"失败\"}";

    /// 交易状态-支付成功
    String TRADE_STATE_SUCCESS = "SUCCESS";
    /// 交易状态-转入退款
    String TRADE_STATE_REFUND = "REFUND";
    /// 交易状态-未支付
    String TRADE_STATE_NOTPAY = "NOTPAY";
    /// 交易状态-已关闭
    String TRADE_STATE_CLOSED = "CLOSED";
    /// 交易状态-已撤销
    String TRADE_STATE_REVOKED = "REVOKED";
    /// 交易状态-用户支付中
    String TRADE_STATE_USERPAYING = "USERPAYING";
    /// 交易状态-支付失败
    String TRADE_STATE_PAYERROR = "PAYERROR";

    /// 退款状态-成功
    String REFUND_STATUS_SUCCESS = "SUCCESS";
    /// 退款状态-退款异常
    String REFUND_STATUS_ABNORMAL = "ABNORMAL";
    /// 退款状态-退款关闭
    String REFUND_STATUS_CLOSED = "CLOSED";
    /// 退款状态-退款中
    String REFUND_STATUS_PROCESSING = "PROCESSING";

    /// # 认证方式
    ///
    interface AuthType {
        /// API V3 证书模式
        String AUTH_TYPE_CERT = "cert";
    }

    /// # 用户标识类型
    ///
    interface UserIdType {
        /// 用户标识(OpenId)
        String OPENID = "openid";
    }
}
