package cn.daxpay.single.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 异步支付信息
 * @author xxm
 * @since 2021/2/28
 */
@Data
@Accessors(chain = true)
public class PayLocal {

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     * 1. 如付款码支付直接成功时会出现
     * 2. 部分通道创建订单是会直接返回
     */
    private String outOrderNo;

    /** 是否支付完成 */
    private boolean complete;

    /** 完成时间 */
    private LocalDateTime finishTime;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

}
