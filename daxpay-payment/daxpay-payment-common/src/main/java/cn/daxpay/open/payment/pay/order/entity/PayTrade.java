package cn.daxpay.open.payment.pay.order.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;

import java.time.OffsetDateTime;

/// # 资金交易凭证
///
/// 统一资金交易表，记录每一笔资金动作（普通支付/预授权冻结/预授权捕获/周期代扣/合单子单）
/// 与容器层（业务单/协议）分离，通过 trade_type + 容器关联字段建立联系
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_trade")
public class PayTrade extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 支付交易号（统一雪花，无形态前缀）
    private String tradeNo;

    /// 交易形态
    /// @see PayTradeTypeEnum
    private String tradeType;

    /// 关联容器ID
    /// 根据 tradeType 决定关联哪种容器表：
    /// normal → pay_normal_order / authorize/capture → pay_auth / recurring → pay_recurring / combine_sub → pay_combine
    private Long containerId;

    /// 支付产品编码，策略工厂通过此字段创建策略
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 支付通道
    private String channel;

    /// 支付方式
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    private String method;

    /// 其他支付方式，method=other 时生效
    private String otherMethod;

    /// 限制支付类型（如限制信用卡）
    /// @see cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum
    private String limitPay;

    /// 支付渠道（微信/支付宝/银联等，三方通道透传时填）
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    private String provider;

    /// 本次交易金额（最小货币单位）
    private Long amount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 可退金额（最小货币单位）
    private Long refundableBalance;

    /// 资金状态
    /// @see PayFundStatusEnum
    private String status;

    /// 过期时间
    private OffsetDateTime expiredTime;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 订单来源
    private String source;

    /// 通道订单号（三方通道返回的订单号）
    private String outOrderNo;

    /// 透传订单号（三方通道产生的透传订单号）
    private String transOrderNo;

    /// 特殊通道关联订单号（部分通道订单号有前缀/长度限制时使用）
    private String relationOrderNo;

    /// 付款用户 ID（支付宝 buyer_user_id 等）
    private String buyerId;

    /// 买家登录账号（支付宝手机号/邮箱）
    private String buyerLogonId;

    /// 微信 openid（jsapi/app/miniapp）
    private String openid;

    /// 通道方记录的支付产品
    private String tradeProduct;

    /// 通道方记录的交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 付款码（被扫支付）
    private String barCode;

    /// 活动类型
    private String promotionType;

    /// 支付参数体（如微信 prepay_id 组装串）
    private String payBody;

    /// 支付参数体类型（jsapi/sdk/app）
    private String payBodyType;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
