package cn.bootx.platform.daxpay.core.refund.record.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.refund.record.convert.RefundConvert;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.dto.refund.RefundRecordDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_refund_record", autoResultMap = true)
public class RefundRecord extends MpBaseEntity implements EntityBaseFunction<RefundRecordDto> {

    /** 支付单号 */
    private Long paymentId;

    /** 关联的业务id */
    private String businessId;

    /** 异步方式关联退款请求号(部分退款情况) */
    private String refundRequestNo;

    /** 用户ID */
    private Long userId;

    /** 标题 */
    private String title;

    /** 退款金额 */
    private BigDecimal amount;

    /** 剩余可退 */
    private BigDecimal refundableBalance;

    /** 退款终端ip */
    private String clientIp;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /**
     * 退款信息列表
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    private List<RefundableInfo> refundableInfo;

    /**
     * 退款状态
     * @see PayStatusCode#REFUND_PROCESS_FAIL
     */
    private String refundStatus;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    @Override
    public RefundRecordDto toDto() {
        return RefundConvert.CONVERT.convert(this);
    }

}
