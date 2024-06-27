package cn.daxpay.single.service.dto.order.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账订单
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单")
public class AllocOrderDto extends BaseDto {

    /**
     * 分账订单号
     */
    @Schema(description = "分账订单号")
    private String allocNo;

    /**
     * 商户分账单号
     */
    @Schema(description = "商户分账单号")
    private String bizAllocNo;
    /**
     * 通道分账单号
     */
    @Schema(description = "通道分账单号")
    private String outAllocNo;

    /**
     * 支付订单ID
     */
    @Schema(description = "支付订单ID")
    private Long orderId;

    /**
     * 支付订单号
     */
    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     * 商户支付订单号
     */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /**
     * 通道订单号
     */
    @Schema(description = "通道订单号")
    private String outOrderNo;

    /**
     * 支付订单标题
     */
    @Schema(description = "支付订单标题")
    private String title;

    /**
     * 所属通道
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 总分账金额
     */
    @Schema(description = "总分账金额")
    private Integer amount;

    /**
     * 分账描述
     */
    @Schema(description = "分账描述")
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 分账处理结果
     * @see AllocOrderResultEnum
     */
    @Schema(description = "分账处理结果")
    private String result;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @Schema(description = "商户扩展参数")
    private String attach;

    /** 请求时间，时间戳转时间 */
    @Schema(description = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 终端ip */
    @Schema(description = "支付终端ip")
    private String clientIp;

    /**
     * 错误码
     */
    @Schema(description = "错误码")
    private String errorCode;

    /**
     * 错误原因
     */
    @Schema(description = "错误原因")
    private String errorMsg;

}
