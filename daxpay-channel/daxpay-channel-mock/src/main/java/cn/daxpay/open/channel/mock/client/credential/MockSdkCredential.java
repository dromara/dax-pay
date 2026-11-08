package cn.daxpay.open.channel.mock.client.credential;

import lombok.Data;

/// # Mock 通道调用凭证
///
/// 模拟网关协议凭证: 业务字段(sandbox/isvNo) + 网关接入密钥(全局一套或主体级独立一套)。
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.config.MockSdkCredential` 字段镜像, 经 JSON 传输。
/// 密钥存 DB(全局单行 mock_channel_credential / 直连商户行 mock_direct_credential /
/// 服务商行 mock_isv_credential), 由 [cn.daxpay.open.channel.mock.service.MockCredentialSupport]
/// 在产出凭证时统一注入:
/// 直连产品(mock_pay)下按通道商户号取商户级两件套(商户私钥+平台公钥)齐时整行下发
/// (私钥/公钥/主体编号/网关地址, one 侧将主体编号随请求报文或查询串的 key_owner_no 字段携带、
/// mock 子应用按主体编号定位登记公钥, 行内网关地址供 one 侧优先选址), 未配置直接报错。
/// 服务商产品(mock_isv)下优先取服务商级两件套(主体编号取服务商编号), 未配置回落全局单行。
/// 仅保留 sandbox 布尔作为业务数据标记(不承担装配防线, 防线在
/// [cn.daxpay.open.channel.mock.MockChannelApp] 的 deployment.mode 条件装配)。
@Data
public class MockSdkCredential {

    /// 是否沙箱环境(业务标记, mock 恒为 true 语义)
    private Boolean sandbox;

    /// 服务商编号(MISV+雪花ID, 仅服务商产品填充; 直连产品为空)
    private String isvNo;

    /// 商户私钥(PEM PKCS#8 原文, 网关请求签名用; 主体级独立密钥时为该主体行私钥)
    private String merchantPrivateKey;

    /// 平台公钥(PEM SPKI 原文, 网关响应/回调验签用; 主体级独立密钥时为该主体行公钥)
    private String platformPublicKey;

    /// 密钥主体编号(仅主体级独立密钥时填充, 直连=通道商户号/服务商=服务商编号, 全局密钥时为空;
    /// 非空时 one 侧随请求报文或查询串携带 key_owner_no, mock 子应用按主体编号查公钥登记表验签, 空时按全局公钥验签)
    private String keyOwnerNo;

    /// 接入网关地址(仅主体级独立密钥时填充: 直连=商户行/服务商=服务商行, 可空; one 侧空回落 yml base-url)
    private String gatewayBaseUrl;
}
