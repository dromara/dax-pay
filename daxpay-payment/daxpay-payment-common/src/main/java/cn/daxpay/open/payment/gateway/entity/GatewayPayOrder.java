package cn.daxpay.open.payment.gateway.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.common.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.GatewayPayTypeEnum;
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
/// 聚合扫码/收银台预下单场景: 创建时不知具体通道, 仅承载收款意图。
/// 用户真正支付时再创建 pay_trade(trade_type=gateway) 并回填 channel/product/method。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_gateway_order", autoResultMap = true)
public class GatewayPayOrder extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 平台网关单号(URL 用)
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

    /// 支付通道(支付后冗余)
    private String channel;

    /// 支付方式
    /// @see PayMethodEnum
    private String method;

    /// 其他支付方式，method=other 时生效
    private String otherMethod;

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

    /// 客户端 IP
    private String clientIp;

    /// 终端设备编码
    private String terminalNo;

    // ===== 通道回执（支付成功/同步后写入）=====

    /// 支付渠道（微信/支付宝/银联等，三方通道透传时填）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 付款用户 ID（支付宝 buyer_user_id 等）
    private String buyerId;

    /// 买家登录账号（支付宝手机号/邮箱）
    private String buyerLogonId;

    /// 通道方记录的支付产品
    private String tradeProduct;

    /// 通道方记录的交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 活动类型
    private String promotionType;

    // ===== 通道关联订单号（部分通道专用）=====

    /// 透传订单号（三方通道产生的透传订单号）
    private String transOrderNo;

    /// 特殊通道关联订单号（部分通道订单号有前缀/长度限制时使用）
    private String relationOrderNo;

    /// 商品明细
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<GoodsDetail> goodsDetail;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
