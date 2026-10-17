package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 富友互联网直连回调解析请求(主应用侧)
@Data
@Schema(title = "富友互联网直连回调解析请求")
public class FuyouInternetCallbackParseReq {

    /// 通道调用凭证(用于商户私钥解密)
    private FuyouRsaSdkCredential credential;

    /// 回调原始报文体(JSON 字符串, 含外层 mchnt_cd 与 message 密文)
    private String reqParam;
}
