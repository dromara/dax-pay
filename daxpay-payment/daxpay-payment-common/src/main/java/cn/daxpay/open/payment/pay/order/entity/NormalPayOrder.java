package cn.daxpay.open.payment.pay.order.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.unipay.param.trade.pay.GoodsDetail;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;

import java.time.OffsetDateTime;
import java.util.List;

/// # 普通支付业务单容器
///
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

    /// 支付产品编码
    /// @see ProductEnum
    private String product;

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
}
