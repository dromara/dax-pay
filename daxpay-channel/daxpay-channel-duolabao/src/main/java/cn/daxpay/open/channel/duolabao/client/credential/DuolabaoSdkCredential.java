package cn.daxpay.open.channel.duolabao.client.credential;

import lombok.Data;

/// # 哆啦宝支付 SDK 凭证(主应用侧)
///
/// 主应用从直连密钥配置([cn.daxpay.open.channel.duolabao.entity.direct.DuolabaoDirectKeyConfig])
/// 提取商户编号/店铺编号/代理商编号与 accessKey/secretKey 密钥对后组装,
/// 经声明式 HTTP 客户端下发给子应用 dax-pay-channel-two 发起哆啦宝开放平台调用。
/// 字段与子应用 cn.daxpay.open.channel.duolabao.config.DuolabaoSdkCredential 保持一致(序列化互通)。
@Data
public class DuolabaoSdkCredential {

    /// 商户编号(customerNum, 哆啦宝商户唯一标识)
    private String customerNum;

    /// 店铺编号(shopNum, 可空)
    private String shopNum;

    /// 代理商编号(agentNum, 可空, 非代理商不需要填写)
    private String agentNum;

    /// 公钥(accessKey, Header 上送的身份标识)
    private String accessKey;

    /// 私钥(secretKey, token 签名与回调验签用, 不上送)
    private String secretKey;

    /// 是否沙箱环境(哆啦宝无独立沙箱网关, 该标识仅透传记录)
    private Boolean sandbox;
}
