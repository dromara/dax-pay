package cn.daxpay.open.payment.core.trade.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
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

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付能力(路由回填)
    private String capability;

    /// 通道商户号(路由回填)
    private String channelMchNo;

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

    /// 商品明细
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<GoodsDetail> goodsDetail;
}
