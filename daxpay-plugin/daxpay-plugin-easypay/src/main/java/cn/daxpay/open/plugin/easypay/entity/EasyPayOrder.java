package cn.daxpay.open.plugin.easypay.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.easypay.convert.EasyPayOrderConvert;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayOrderResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 易支付协议订单
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_easy_pay_order")
public class EasyPayOrder extends MchBaseEntity implements ToResult<EasyPayOrderResult> {

    /// 关联内核容器 ID（NormalPayOrder.id）
    private Long orderId;

    /// 易支付商户号
    private Integer pid;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 平台业务单号（对外 trade_no，约定=容器 orderNo）
    private String tradeNo;

    /// 商户订单号 out_trade_no
    private String outTradeNo;

    /// 通道订单号
    private String apiTradeNo;

    /// 协议支付方式 alipay/wxpay/aggregate
    private String type;

    /// 协议状态 0待付 1成功
    private Integer status;

    /// 创建时间
    private OffsetDateTime addTime;

    /// 完成时间
    private OffsetDateTime endTime;

    /// 商品名称
    private String name;

    /// 金额（元）
    private BigDecimal money;

    /// 已退款金额（元）
    private BigDecimal refundMoney;

    /// 异步通知地址（本期仅落库不发送）
    private String notifyUrl;

    /// 同步跳转
    private String returnUrl;

    /// 业务扩展参数
    private String param;

    /// 支付用户标识
    private String buyer;

    /// 客户端 IP
    private String clientIp;

    /// API 版本 v1/v2
    private String apiVersion;

    /// PC 调用方式
    private String pcCallType;

    /// 支付链接
    private String payUrl;

    /// 支付参数体
    private String payBody;

    @Override
    public EasyPayOrderResult toResult() {
        return EasyPayOrderConvert.CONVERT.toResult(this);
    }
}
