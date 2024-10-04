package org.dromara.daxpay.service.entity.order.allocation;

import org.dromara.daxpay.core.enums.AllocOrderResultEnum;
import org.dromara.daxpay.core.enums.AllocOrderStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账订单
 * @author xxm
 * @since 2024/6/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_order")
public class AllocOrder extends MchAppBaseEntity {

    /**
     * 分账单号
     */
    private String allocNo;

    /**
     * 商户分账单号
     */
    private String bizAllocNo;

    /**
     * 通道分账号
     */
    private String outAllocNo;

    /** 支付订单ID */
    private Long orderId;

    /**
     * 支付订单号
     */
    private String orderNo;

    /**
     * 商户支付订单号
     */
    private String bizOrderNo;

    /**
     * 通道系统支付订单号
     */
    private String outOrderNo;

    /**
     * 支付订单标题
     */
    private String title;

    /**
     * 所属通道
     * @see ChannelEnum
     */
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
     * @see AllocOrderStatusEnum
     */
    private String status;

    /**
     * 处理结果
     * @see AllocOrderResultEnum
     */
    private String result;

    /** 分账完成时间 */
    private LocalDateTime finishTime;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String attach;

    /** 支付终端ip 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String clientIp;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

}
