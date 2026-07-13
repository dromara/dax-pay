package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款订单
///
/// 记录每笔退款交易, 与原支付订单(pay_normal_order / pay_trade)通过 orderNo 关联。
/// 同一笔支付订单可多次部分退款, 每笔退款生成一条 refund_order 记录, 退款号 refundNo 唯一。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_refund_order")
public class PayRefundOrder extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 退款号(平台统一生成, 全局唯一, 对应通道 out_request_no)
    private String refundNo;

    /// 商户退款号(商户传入, 商户侧唯一)
    private String bizRefundNo;

    /// 标题
    private String title;

    /// 关联支付订单号(平台支付交易号 tradeNo, 对应通道 out_trade_no)
    private String orderNo;

    /// 商户业务订单号
    private String bizOrderNo;

    /// 通道支付订单号(三方通道返回的 trade_no, 对应支付宝 trade_no)
    private String outOrderNo;

    /// 通道退款流水号(退款成功后由通道返回)
    private String outRefundNo;

    /// 退款金额(最小货币单位, 分)
    private Long amount;

    /// 订单总金额(冗余自原支付订单, 便于查询)
    private Long orderAmount;

    /// 币种
    private String currency;

    /// 退款原因
    private String reason;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    private String status;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 支付通道
    private String channel;

    /// 支付产品编码
    private String product;

    /// 支付方式
    private String method;

    /// 通道商户号(路由回填)
    private String channelMchNo;

    /// 支付能力编码(路由回填)
    private String capability;

    /// 异步通知地址(出站商户通知用)
    private String notifyUrl;

    /// 商户附加参数(回调原样返回)
    private String attach;

    /// 客户端 IP
    private String clientIp;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
