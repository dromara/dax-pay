package cn.daxpay.open.payment.strategy.transfer;

import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.param.TransferReportInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 转账策略执行上下文(请求级,显式传参)
///
/// 与支付策略 [cn.daxpay.open.payment.strategy.pay.PayStrategyContext] 对称:
/// 持有单笔转账管线的"公共资金凭证 + 通道特有字段", 由编排层按通道从具体容器装配,
/// 通道策略侧无实体依赖、无需强转。
///
/// 通道特有字段扁平化存放, 未用通道恒为 null; 其中 [transferScene] 允许策略回写
/// (抖音发起时从通道商户配置读取), 编排层在"处理中"镜像落库。
///
/// 与线程级身份上下文严格区分:
/// - 本类 = 请求级数据载体(函数传参)
/// - `common.context.PaymentContext` = 线程级身份(商户号)
@Getter
@Setter
@Accessors(chain = true)
public class TransferStrategyContext {

    /// 公共资金凭证(容器+凭证双写/状态流转用; 编排层装配后填入)
    private TransferTrade trade;

    /// 通道编码(wechat/alipay/douyin)
    private String channel;

    /// 商户号
    private String mchNo;

    /// 通道商户号(凭证组装用)
    private String channelMchNo;

    /// 平台转账单号
    private String transferNo;

    /// 商户转账号(幂等键)
    private String bizTransferNo;

    /// 通道转账单号
    private String outTransferNo;

    /// 转账金额(最小货币单位/分)
    private Long amount;

    /// 币种
    private String currency;

    /// 转账标题
    private String title;

    /// 转账原因/备注
    private String reason;

    /// 商户异步通知地址
    private String notifyUrl;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    private String status;

    /// 转账完成时间
    private OffsetDateTime finishTime;

    // ===== 通道特有(扁平化, 由编排层从具体容器装配; 未用通道恒空) =====

    /// 收款人微信 openid
    private String payeeOpenid;

    /// 转账场景(微信/抖音, 抖音发起时可经本字段回写)
    private String transferScene;

    /// 收款人姓名(微信金额档位校验用)
    private String userName;

    /// 收款人账号类型(支付宝/抖音)
    private String payeeType;

    /// 收款人账号(支付宝/抖音)
    private String payeeAccount;

    /// 收款人姓名(支付宝/抖音)
    private String payeeName;

    /// 转账场景配置ID(支付宝专用,发起时指定用哪个场景; 空则用通道商户默认)
    private String transferSceneConfigId;

    /// 转账场景报备信息(微信/抖音转账必填, 各场景要求不同)
    private List<TransferReportInfo> reportInfos;

    /// 用户收款感知(抖音专用, 收款人在抖音中看到的文案)
    private String userRecvPerception;
}
