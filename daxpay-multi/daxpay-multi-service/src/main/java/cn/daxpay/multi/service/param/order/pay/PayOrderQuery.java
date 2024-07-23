package cn.daxpay.multi.service.param.order.pay;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.PayAllocStatusEnum;
import cn.daxpay.multi.core.enums.PayRefundStatusEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付订单查询参数
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam
@Accessors(chain = true)
@Schema(title = "支付订单查询参数")
public class PayOrderQuery extends SortParam {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     *  通道系统交易号
     */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 是否支持分账 */
    @Schema(description = "是否需要分账")
    private Boolean allocation;

    @Schema(description = "是否开启自动分账")
    private Boolean autoAllocation;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "异步支付通道")
    private String channel;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String method;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String refundStatus;

    /**
     * 分账状态
     * @see PayAllocStatusEnum
     */
    @Schema(description = "分账状态")
    private String allocStatus;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;

}
