package cn.daxpay.multi.service.entity.order.allocation;

import cn.daxpay.multi.service.code.AllocOrderResultEnum;
import cn.daxpay.multi.service.code.AllocOrderStatusEnum;
import cn.daxpay.multi.service.code.PayChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class AllocOrder extends MchEntity {

    /**
     * 分账单号
     */
    private String allocationNo;

    /**
     * 商户分账单号
     */
    private String bizAllocationNo;

    /**
     * 通道分账号
     */
    private String outAllocationNo;

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
     * @see PayChannelEnum
     */
    private String channel;

    /**
     * 总分账金额
     */
    private Integer amount;

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
