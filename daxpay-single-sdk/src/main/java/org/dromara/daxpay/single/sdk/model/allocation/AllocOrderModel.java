package org.dromara.daxpay.single.sdk.model.allocation;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.AllocationResultEnum;
import org.dromara.daxpay.single.sdk.code.AllocationStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分账订单
 * @author xxm
 * @since 2024/5/21
 */
@Data
@Accessors(chain = true)
public class AllocOrderModel {

    /** 分账单号 */
    private String allocNo;

    /** 商户分账单号 */
    private String bizAllocNo;

    /** 通道分账号 */
    private String outAllocNo;

    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 通道支付订单号 */
    private String outOrderNo;

    /** 分账标题 */
    private String title;

    /** 所属通道 */
    private String channel;

    /**
     * 总分账金额
     */
    private BigDecimal amount;

    /**
     * 分账描述
     */
    private String description;

    /**
     * 状态
     * @see AllocationStatusEnum
     */
    private String status;

    /**
     * 处理结果
     * @see AllocationResultEnum
     */
    private String result;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /** 分账订单完成时间 */
    private LocalDateTime finishTime;

    /** 分账明细 */
    private List<AllocDetailModel> details;

}
