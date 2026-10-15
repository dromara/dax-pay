package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import lombok.Data;

/// # 富友直连回调验签解析请求
///
/// 主应用接收到富友直连异步通知后, 将原始报文体(JSON, UTF-8) 连同直连商户凭证转发到子应用,
/// 由子应用解析字段后用商户密钥(mchnt_key) MD5 验签,
/// 返回结构化业务数据 [cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp]。
///
/// 富友回调用 `mchnt_order_no`(关联订单号) 反查平台交易号, 该映射由主应用凭 DB 完成,
/// 子应用仅返回 `mchnt_order_no` 作为 outTradeNo。
@Data
public class FuyouDirectCallbackParseReq {

    /// 通道调用凭证(用于获取商户密钥验签)
    private FuyouDirectSdkCredential credential;

    /// 回调原始报文体(JSON 字符串, UTF-8; 兼容 form 编码兜底解析)
    private String reqParam;
}
