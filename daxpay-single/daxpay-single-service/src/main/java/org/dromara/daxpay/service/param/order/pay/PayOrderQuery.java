package org.dromara.daxpay.service.param.order.pay;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单查询参数
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "支付订单查询参数")
public class PayOrderQuery extends MchAppQuery {

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
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否需要分账")
    private Boolean allocation;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否开启自动分账")
    private Boolean autoAllocation;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "异步支付通道")
    private String channel;

    /**
     * 支付方式
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付方式")
    private String method;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付状态")
    private String status;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "退款状态")
    private String refundStatus;

    /**
     * 分账状态
     * @see PayAllocStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "分账状态")
    private String allocStatus;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

}
