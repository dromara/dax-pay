package cn.daxpay.single.service.core.order.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.param.channel.AliPayParam;
import cn.daxpay.single.core.param.channel.WalletPayParam;
import cn.daxpay.single.core.param.channel.WeChatPayParam;
import cn.daxpay.single.service.core.order.refund.convert.RefundOrderConvert;
import cn.daxpay.single.service.dto.order.refund.RefundOrderDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "退款订单")
@TableName(value = "pay_refund_order", autoResultMap = true)
public class RefundOrder extends MpBaseEntity implements EntityBaseFunction<RefundOrderDto> {

    /** 支付订单ID */
    @DbMySqlIndex(comment = "支付订单ID索引")
    @DbColumn(comment = "支付订单ID", isNull = false)
    private Long orderId;

    /** 支付订单号 */
    @DbMySqlIndex(comment = "支付订单号索引")
    @DbColumn(comment = "支付订单号", length = 32, isNull = false)
    private String orderNo;

    /** 商户支付订单号 */
    @DbMySqlIndex(comment = "商户支付订单号索引")
    @DbColumn(comment = "商户支付订单号", length = 100, isNull = false)
    private String bizOrderNo;

    /** 通道支付订单号 */
    @DbMySqlIndex(comment = "通道支付订单号索引")
    @DbColumn(comment = "通道支付订单号", length = 150, isNull = false)
    private String outOrderNo;

    /** 支付标题 */
    @DbColumn(comment = "支付标题", length = 100, isNull = false)
    private String title;

    /** 退款号 */
    @DbMySqlIndex(comment = "退款号索引")
    @DbColumn(comment = "退款号", length = 32, isNull = false)
    private String refundNo;

    /** 商户退款号 */
    @DbMySqlIndex(comment = "商户退款号索引")
    @DbColumn(comment = "商户退款号", length = 100, isNull = false)
    private String bizRefundNo;

    /** 通道退款交易号 */
    @DbMySqlIndex(comment = "通道退款交易号索引")
    @DbColumn(comment = "通道退款交易号", length = 150)
    private String outRefundNo;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道", length = 20, isNull = false)
    private String channel;

    /** 订单金额 */
    @DbColumn(comment = "订单金额", length = 8, isNull = false)
    private Integer orderAmount;

    /** 退款金额 */
    @DbColumn(comment = "退款金额", length = 8, isNull = false)
    private Integer amount;

    /** 退款原因 */
    @DbColumn(comment = "退款原因", length = 150)
    private String reason;

    /** 退款完成时间 */
    @DbColumn(comment = "退款完成时间")
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @DbColumn(comment = "退款状态", length = 20, isNull = false)
    private String status;

    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址", length = 200)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @DbColumn(comment = "商户扩展参数", length = 500)
    private String attach;

    /**
     * 附加参数 以最后一次为准
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @DbColumn(comment = "附加参数", length = 2048)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String extraParam;

    /** 请求时间，时间戳转时间 */
    @DbColumn(comment = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip", length = 64)
    private String clientIp;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 2048)
    private String errorMsg;

    @Override
    public RefundOrderDto toDto() {
        return RefundOrderConvert.CONVERT.convert(this);
    }

}
