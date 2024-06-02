package cn.daxpay.multi.service.entity.pay;

import cn.daxpay.multi.service.code.PayChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order")
public class PayOrder extends MchEntity {

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
     * @see PayChannelEnum
     */
    private String channel;

    /**
     * 支付方式, 以最后一次为准
     */
    private String method;

    /** 金额(元) */
    private BigDecimal amount;

    /** 可退金额(元) */
    private Integer refundableBalance;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 分账状态
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
     * 附加参数 以最后一次为准
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String extraParam;

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
