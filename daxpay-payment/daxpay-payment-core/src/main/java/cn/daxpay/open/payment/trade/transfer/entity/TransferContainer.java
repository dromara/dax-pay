package cn.daxpay.open.payment.trade.transfer.entity;

import java.time.OffsetDateTime;

/// # 转账容器契约
///
/// 微信/支付宝/抖音三通道转账容器([WechatTransferOrder]/[AlipayTransferOrder]/[DouyinTransferOrder])
/// 的同名字段集合, 解除转账编排辅助服务对三容器的平行复制依赖(建单/凭证组装/终态镜像/上下文装配)。
/// 通道特有字段(微信 payeeOpenid/transferBody/wxAppId, 支付宝与抖音的 payeeType/reportInfos 等)
/// 留在具体容器类型, 由各通道路由的差异钩子操作。
/// setter 返回容器自身, 与实体 @Accessors(chain=true) 链式风格协变。
public interface TransferContainer {

    /// 主键(建单落库后回填, 组装公共凭证 containerId 用)
    Long getId();

    /// 商户号
    String getMchNo();

    /// 平台转账单号
    String getTransferNo();

    /// 商户转账号
    String getBizTransferNo();

    /// 通道商户号
    String getChannelMchNo();

    /// 通道转账单号
    String getOutTransferNo();

    /// 转账金额(最小货币单位)
    Long getAmount();

    /// 币种
    String getCurrency();

    /// 标题
    String getTitle();

    /// 转账原因
    String getReason();

    /// 业务状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    String getStatus();

    /// 完成时间
    OffsetDateTime getFinishTime();

    /// 商户异步通知地址
    String getNotifyUrl();

    TransferContainer setTransferNo(String transferNo);

    TransferContainer setBizTransferNo(String bizTransferNo);

    TransferContainer setChannelMchNo(String channelMchNo);

    TransferContainer setOutTransferNo(String outTransferNo);

    TransferContainer setAmount(Long amount);

    TransferContainer setCurrency(String currency);

    TransferContainer setTitle(String title);

    TransferContainer setReason(String reason);

    TransferContainer setStatus(String status);

    TransferContainer setFinishTime(OffsetDateTime finishTime);

    /// 商户附加参数
    TransferContainer setAttach(String attach);

    /// 商户异步通知地址
    TransferContainer setNotifyUrl(String notifyUrl);

    /// 请求时间
    TransferContainer setReqTime(OffsetDateTime reqTime);

    TransferContainer setErrorMsg(String errorMsg);
}
