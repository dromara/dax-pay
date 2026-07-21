package cn.daxpay.open.platform.capability.douyin.auth.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音 JS-SDK config 验签结果
///
/// 返回给前端的 `window.DouyinOpenJSBridge.config()` 必需参数包,
/// 前端拿到后直接透传给 `sdk.config({params: {...}})` 完成鉴权。
///
/// 参考文档:
/// - JS 接入指南: https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/js/js-access
/// - 验证签名:   https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/web-app/js/signature
@Data
@Accessors(chain = true)
public class DouyinJsapiConfigResult {

    /// 抖音开放平台 Client Key(网站应用 appid)
    private String clientKey;

    /// 时间戳(秒, 字符串)
    private String timestamp;

    /// 随机字符串
    private String nonceStr;

    /// 服务端计算的 MD5 签名(基于 jsapi_ticket + nonce_str + timestamp + url)
    private String signature;
}
