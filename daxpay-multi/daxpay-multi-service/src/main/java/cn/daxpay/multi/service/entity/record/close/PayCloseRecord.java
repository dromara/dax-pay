package cn.daxpay.multi.service.entity.record.close;

import cn.daxpay.multi.service.code.PayChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/6/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PayCloseRecord extends MchEntity {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 是否关闭成功 */
    private boolean closed;

    /**
     * 关闭类型
     * @see cn.daxpay.multi.service.code.PayCloseTypeEnum
     */
    private String closeType;

    /** 错误码 */
    private String errorCode;

    /** 错误消息 */
    private String errorMsg;

    /** 客户端IP */
    private String clientIp;
}
