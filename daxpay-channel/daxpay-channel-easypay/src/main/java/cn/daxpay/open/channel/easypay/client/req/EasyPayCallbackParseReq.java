package cn.daxpay.open.channel.easypay.client.req;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import lombok.Data;

import java.util.Map;

/// # 易支付回调验签解析请求
///
/// 主应用接收到易支付异步通知(form 参数形态)后, 将参数集合(含 sign 字段)连同通道凭证转发到子应用,
/// 由子应用用平台公钥 SHA256withRSA 验签(待签串规则与请求签名一致: 字典序, 排除 sign/sign_type),
/// 返回结构化业务数据 [cn.daxpay.open.channel.easypay.resp.EasyPayCallbackParseResp]。
///
/// 注意: 易支付异步通知应答为纯文本 `success`(见 [cn.daxpay.open.channel.easypay.code.EasyPayCode#NOTIFY_ACK_SUCCESS]),
/// 应答动作由主应用回调入口完成, 子应用仅负责验签与解析。
@Data
public class EasyPayCallbackParseReq {

    /// 通道调用凭证(用于获取平台公钥验签)
    private EasyPaySdkCredential credential;

    /// 回调参数集合(易支付异步通知 form 参数, 键值均为字符串, 含 sign 字段)
    private Map<String, String> params;
}
