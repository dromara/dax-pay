package cn.daxpay.open.payment.douyin.facade;

/// # 抖音分账接收方引用查询门面
///
/// 供抖音应用删除前校验是否被分账接收方记录引用, 避免删除后接收方记录的应用引用悬空
/// (接收方记录存的是原始 douyinAppId 字符串, 应用删除后重绑时报"应用未配置")。
///
/// 实现在 channel-douyin 模块(接收方档案所在处), 本接口放 core 解除反向依赖, 运行期由单体装配注入。
public interface DouyinAllocReceiverFacade {

    /// 商户档应用是否被分账接收方引用(抖音直连接收方的 channelAppId, 按 mchNo + douyinAppId 匹配)
    boolean existsReceiverByMchApp(String mchNo, String douyinAppId);
}
