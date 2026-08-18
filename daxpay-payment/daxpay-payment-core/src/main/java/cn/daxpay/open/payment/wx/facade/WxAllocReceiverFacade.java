package cn.daxpay.open.payment.wx.facade;

/// # 微信分账接收方引用查询门面
///
/// 供微信应用删除前校验是否被分账接收方记录引用, 避免删除后接收方记录的应用引用悬空
/// (微信/抖音接收方记录存的是原始 wxAppId 字符串, 应用删除后重绑时报"应用未配置")。
///
/// 实现在 channel-wechat 模块(接收方档案所在处), 本接口放 core 解除反向依赖, 运行期由单体装配注入。
public interface WxAllocReceiverFacade {

    /// 商户档应用是否被分账接收方引用
    ///
    /// 引用点: 微信直连接收方的 channelAppId 与微信服务商接收方的 subAppId(按 mchNo + wxAppId 匹配)。
    boolean existsReceiverByMchApp(String mchNo, String wxAppId);

    /// 平台档应用是否被分账接收方引用
    ///
    /// 引用点: 微信服务商接收方的 spAppId(平台应用无商户维度, 按 wxAppId 全局匹配)。
    boolean existsReceiverByPlatformApp(String wxAppId);
}
