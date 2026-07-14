package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 网关支付业务单容器
///
/// 业务容器统一落在 trade.order（与 NormalPayOrder 同包）。
/// 聚合扫码/收银台预下单场景: 创建时不知具体通道, 仅承载收款意图。
/// 用户真正支付时再创建 pay_trade(trade_type=gateway) 并回填 channel/product/method。
/// 网关 orderNo 为预下单 URL 号, 与资金 tradeNo 身份分离。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_gateway_order", autoResultMap = true)
public class GatewayPayOrder extends MchBaseEntity {

    /// 平台网关业务单号(URL 用, 预下单即生成, 可无 trade)
    private String orderNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 网关类型
    /// @see GatewayPayTypeEnum
    private String gatewayType;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 业务状态
    /// @see GatewayOrderStatusEnum
    private String status;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数
    private String attach;

    /// 过期时间
    private OffsetDateTime expiredTime;

    /// 金额(最小货币单位)
    private Long amount;

    /// 币种
    /// @see CurrencyEnum
    private String currency;

    /// 支付通道编码（冗余自 product → ProductEnum#getChannel，对应 ChannelEnum；非 PayProviderEnum）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    private String channel;

    /// 支付方式
    /// @see PayMethodEnum
    private String method;

    /// 限制支付类型（如限制信用卡）
    /// @see cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum
    private String limitPay;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付能力(路由回填)
    private String capability;

    /// 通道商户号(路由回填)
    private String channelMchNo;

    /// 通道应用 AppId（本笔交易实际使用的微信/通道侧 AppId 快照；解析后写入，关退同步复用）
    private String channelAppId;

    // ===== 支付请求参数（支付时写入）=====

    /// 微信 openid（jsapi/app/miniapp）
    private String openid;

    /// 付款码（被扫支付）
    private String barCode;

    /// 收银场景 wechat_pay/alipay/union_pay
    private String scene;

    /// 最后发起设备 mobile/pc
    private String device;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 下单客户端 IP；关单/同步/退款透传通道的单一事实源，兼审计排查
    private String clientIp;

    /// 终端设备编码
    private String terminalNo;

    // ===== 通道回执（支付成功/同步后写入）=====

    /// 支付渠道（微信/支付宝/银联等，三方通道透传时填）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 付款用户 ID（支付宝 buyer_user_id 等）
    private String buyerId;

    /// 通道方记录的支付产品
    private String tradeProduct;

    /// 通道方记录的交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 活动类型
    private String promotionType;

    /// 支付参数体（已拉起缓存，仅落容器）
    private String payBody;

    /// 支付参数体类型
    private String payBodyType;

    // ===== 通道关联订单号（部分通道专用）=====

    /// 透传订单号（三方通道产生的透传订单号）
    private String transOrderNo;

    /// 实际上送通道的商户订单号（展示冗余；反查权威在 pay_trade.relation_order_no）
    private String relationOrderNo;


    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 商品明细
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<GoodsDetail> goodsDetail;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
