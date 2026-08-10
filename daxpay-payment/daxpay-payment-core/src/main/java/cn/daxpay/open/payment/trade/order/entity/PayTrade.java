package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;

import java.time.OffsetDateTime;

/// # 资金交易凭证
///
/// 统一资金交易表，记录每一笔资金动作（普通支付/预授权冻结/预授权捕获/周期代扣/合单子单）
/// 与容器层（业务单/协议）分离，通过 trade_type + 容器关联字段建立联系。
/// 保留资金动作固有属性、通道反查命脉字段，以及 **轻量组织冗余**
///（source / channel / channelMchNo / storeNo / provider），便于资金列表与汇总免 JOIN 容器；
/// 完整业务上下文/路由细节/payBody/回执仍以容器为准。
/// 过期时间只在容器，本表不存 expiredTime。
/// channel(接入通道, B端机构维度) 与 provider(支付渠道, C端钱包维度) 是正交两个维度:
/// channel 用于按接入通道(拉卡拉/银联商务等聚合)维度看资金, provider 用于按付款钱包(微信/支付宝等)维度看资金;
/// 与 [cn.daxpay.open.payment.trade.transfer.entity.TransferTrade] 的 channel+provider 双冗余口径对齐。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_trade")
public class PayTrade extends MchBaseEntity {

    /// 资金交易号（平台生成，与业务单 orderNo 独立）
    private String tradeNo;

    /// 订单标题(冗余自容器, 资金列表/工作台免JOIN; 权威在容器)
    private String title;

    /// 交易形态
    /// @see PayTradeTypeEnum
    private String tradeType;

    /// 关联容器ID
    /// 根据 tradeType 决定关联哪种容器表：
    /// normal → pay_normal_order / gateway → pay_gateway_order / authorize/capture → pay_auth / recurring → pay_recurring / combine_sub → pay_combine
    private Long containerId;

    /// 本次交易金额（最小货币单位）
    private Long amount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 入账金额（最小货币单位）
    /// 结算类动作(normal/gateway/capture 等)且资金态 SUCCESS 时 = amount；
    /// 预授权冻结(authorize)等非结算动作恒为 0。对账/成交汇总用此字段，勿直接 sum(amount)。
    private Long postedAmount;

    /// 可退金额（最小货币单位）
    private Long refundableBalance;

    /// 资金状态
    /// @see PayFundStatusEnum
    private String status;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 订单来源(冗余自容器, 插件/资金列表免回表; 权威在容器 source)
    /// @see cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum
    private String source;

    /// 通道商户号(冗余自业务容器, 路由确定后写入; 权威在容器 channelMchNo)
    private String channelMchNo;

    /// 支付渠道(冗余自业务容器; 下单时由 method→[PayProviderEnum] 写入, 成功路径兜底补齐)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 支付通道(冗余自容器 product→[ChannelEnum]; 创建即终值, 与 provider 不同无需成功路径兜底)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    private String channel;

    /// 门店号(冗余自业务容器, 可空; 权威在容器 storeNo)
    private String storeNo;

    /// 通道订单号（三方通道返回的订单号，通道 close/sync 调用参数 + 回调反查索引）
    private String outOrderNo;

    /// 实际上送通道的商户订单号（回调/关退同步反查权威）
    /// 普通通道 = 容器 orderNo；特殊通道（前缀/长度限制）= 变形号，可与 orderNo 不同
    private String relationOrderNo;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 分账状态(none-未分账 / processing-分账中 / done-已分账)
    /// 防止重复分账, 分账发起前置校验
    private String allocStatus;
}
