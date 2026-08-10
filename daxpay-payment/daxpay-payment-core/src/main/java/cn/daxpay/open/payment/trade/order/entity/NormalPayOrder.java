package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;

import java.time.OffsetDateTime;
import java.util.List;

/// # 普通支付业务单容器
///
/// 业务容器统一落在 trade.order（与 GatewayPayOrder 等同包，纯持久化无编排 service）。
/// 普通支付场景的容器，承载商户业务单信息（bizOrderNo / 商品标题 / 回调地址 等）
/// 与 pay_trade 一对一关联（trade_type = normal，当前实现；模型上 orderNo 与 tradeNo 身份分离）。
/// 冗余存储金额/支付/时间线字段，便于后台查询无需 JOIN pay_trade。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_normal_order", autoResultMap = true)
public class NormalPayOrder extends MchBaseEntity {

    /// 平台业务单号（容器身份，与 tradeNo 独立生成；普通通道默认作为上送号）
    private String orderNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 订单来源(业务入口权威)
    /// @see cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum
    private String source;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 业务状态
    /// @see NormalPayOrderStatusEnum
    private String status;

    /// 异步通知地址（出站商户通知用）
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数（回调原样返回）
    private String attach;

    /// 业务单过期时间
    private OffsetDateTime expiredTime;

    // ===== 金额（冗余自 PayTrade，方便查询）=====

    /// 业务单金额（最小货币单位）
    private Long amount;

    /// 币种
    /// @see CurrencyEnum
    private String currency;

    // ===== 支付信息（冗余，查询过滤用）=====

    /// 支付通道编码（冗余自 product → ProductEnum#getChannel，对应 ChannelEnum；非 PayProviderEnum）
    /// @see ChannelEnum
    private String channel;

    /// 支付方式
    /// @see PayMethodEnum
    private String method;

    /// 限制支付类型（如限制信用卡）
    /// @see PayLimitPayEnum
    private String limitPay;

    /// 支付产品编码
    /// @see ProductEnum
    private String product;

    // ===== 支付请求参数（下单时写入，审计保留）=====

    /// 微信 openid（jsapi/app/miniapp）
    private String openid;

    /// 付款码（被扫支付，终态后仍保留供审计）
    private String authCode;

    // ===== 时间线（冗余，查询展示用）=====

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    // ===== 通道路由（同步时用于解析通道应用凭证）=====

    /// 通道商户号(路由回填)
    private String channelMchNo;

    /// 支付能力编码(路由回填)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    private String capability;

    /// 通道应用 AppId（本笔交易实际使用的微信/通道侧 AppId 快照；解析后写入，关退同步复用）
    private String channelAppId;

    // ===== 通道回执（支付成功/同步后写入）=====

    /// 支付渠道（微信/支付宝/银联等，三方通道透传时填）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 付款用户标识（支付宝 user_id、微信 openid 等，非通道 AppId）
    private String buyerId;

    /// 通道方记录的支付产品
    private String tradeProduct;

    /// 通道方记录的交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 活动类型
    private String promotionType;

    /// 支付参数体（如微信 prepay_id 组装串，非空表示已拉起支付，免重复请求通道；仅落容器）
    private String payBody;

    /// 支付参数体类型（jsapi/sdk/app）
    private String payBodyType;

    // ===== 关联订单号 =====

    /// 透传订单号（三方通道产生的透传订单号）
    private String transOrderNo;

    /// 实际上送通道的商户订单号（展示冗余；反查权威在 pay_trade.relation_order_no）
    /// 普通通道与 orderNo 一致；特殊通道为变形号
    private String relationOrderNo;

    // ===== 请求信息（关单/同步/退款透传依赖 + 审计）=====

    /// 通道附加参数
    private String extraParam;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 订单商品明细列表（jsonb 存储）
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<GoodsDetail> goodsDetail;

    /// 下单客户端 IP；关单/同步/退款透传通道的单一事实源，兼审计排查
    private String clientIp;

    /// 终端设备编码
    private String terminalNo;

    /// 门店号（线下经营归属，可空；对应 [MchStoreInfo#storeNo]）
    private String storeNo;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /// 是否分账订单(下单时透传通道分账标识用, 如微信 profit_sharing / 支付宝 royalty_freeze)
    private Boolean allocation;
}
