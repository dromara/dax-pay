package org.dromara.daxpay.service.entity.allocation.order;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocOrderConvert;
import org.dromara.daxpay.service.result.allocation.order.AllocOrderVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账交易记录
 * @author xxm
 * @since 2024/11/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_order")
public class AllocOrder extends MchAppBaseEntity implements ToResult<AllocOrderVo> {

    /** 分账单号 */
    private String allocNo;

    /** 商户分账单号 */
    private String bizAllocNo;

    /** 通道分账号 */
    private String outAllocNo;

    /** 支付订单ID */
    private Long orderId;

    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 通道支付订单号 */
    private String outOrderNo;

    /** 支付标题 */
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
     * @see AllocationStatusEnum
     */
    private String status;

    /**
     * 处理结果
     * @see AllocationResultEnum
     */
    private String result;

    /** 分账完成时间 */
    private LocalDateTime finishTime;

    /** 异步通知地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    private String attach;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

    /** 终端ip */
    private String clientIp;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public AllocOrderVo toResult() {
        return AllocOrderConvert.CONVERT.toVo(this);
    }
}
