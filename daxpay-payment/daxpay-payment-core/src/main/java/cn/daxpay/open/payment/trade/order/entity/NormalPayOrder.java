package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
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
/// 与 pay_trade 一对一关联（trade_type = normal）
/// 冗余存储金额/支付/时间线字段，便于后台查询无需 JOIN pay_trade
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "pay_normal_order", autoResultMap = true)
public class NormalPayOrder extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 商户业务单号
    private String bizOrderNo;

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

    /// 支付通道
    private String channel;

    /// 支付方式
    /// @see PayMethodEnum
    private String method;

    /// 其他支付方式，method=other 时生效
    private String otherMethod;

    /// 限制支付类型（如限制信用卡）
    /// @see cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum
    private String limitPay;

    /// 支付产品编码
    /// @see ProductEnum
    private String product;

    // ===== 支付请求参数（下单时写入）=====

    /// 微信 openid（jsapi/app/miniapp）
    private String openid;

    /// 付款码（被扫支付）
    private String barCode;

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

    // ===== 请求信息（低频，审计排查用）=====

    /// 通道附加参数
    private String extraParam;

    /// 订单商品明细列表（jsonb 存储）
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<GoodsDetail> goodsDetail;

    /// 客户端 IP
    private String clientIp;

    /// 终端设备编码
    private String terminalNo;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
