package cn.bootx.platform.daxpay.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付同步信息
 * @author xxm
 * @since 2024/1/24
 */
@Data
@Accessors(chain = true)
public class PaySyncLocal {

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     * 1. 如付款码支付直接成功时会出现
     */
    private String gatewayOrderNo;

    /** 支付完成时间(通常用于接收异步支付返回的时间) */
    private LocalDateTime payTime;

}
