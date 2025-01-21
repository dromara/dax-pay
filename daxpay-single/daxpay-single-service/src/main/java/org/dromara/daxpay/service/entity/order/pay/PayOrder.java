package org.dromara.daxpay.service.entity.order.pay;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.service.result.order.pay.PayOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 支付订单
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order")
public class PayOrder extends MchAppBaseEntity implements ToResult<PayOrderVo> {

    /** 商户订单号 */
    private String bizOrderNo;

    /** 订单号 */
    private String orderNo;

    /** 通道订单号 */
    private String outOrderNo;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否支持分账 */
    private Boolean allocation;

    /** 自动分账 */
    private Boolean autoAllocation;

    /**
     * 支付通道, 以最后一次为准
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式, 以最后一次为准
     */
    private String method;

    /** 金额(元) */
    private BigDecimal amount;

    /** 可退金额(元) */
    private BigDecimal refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    private String status;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    private String refundStatus;

    /**
     * 分账状态
     * @see PayAllocStatusEnum
     */
    private String allocStatus;

    /** 过期时间 */
    private LocalDateTime expiredTime;

    /** 支付成功时间 */
    private LocalDateTime payTime;

    /** 关闭时间 */
    private LocalDateTime closeTime;

    /** 同步跳转地址, 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String returnUrl;

    /** 异步通知地址,以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /**
     * 通道附加参数序列化为Json字符串 以最后一次为准
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String attach;

    /** 请求时间 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime reqTime;

    /** 支付终端ip 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String clientIp;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    public boolean getAllocation() {
        return Objects.equals(true, allocation);
    }

    public boolean getAutoAllocation() {
        return Objects.equals(true, autoAllocation);
    }

    public PayOrder setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg,0,300);
        return this;
    }

    @Override
    public PayOrderVo toResult() {
        return PayOrderConvert.CONVERT.toVo(this);
    }
}
