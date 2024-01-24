package cn.bootx.platform.daxpay.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调信息上下文
 * @author xxm
 * @since 2024/1/24
 */
@Data
@Accessors(chain = true)
public class CallbackLocal {

    /** 回调参数内容 */
    private final Map<String, String> callbackParam = new HashMap<>();

    /**
     * 第三方支付平台订单号
     * 1. 如付款码支付直接成功时会出现
     */
    private String gatewayOrderNo;

    /** 支付完成时间 */
    private LocalDateTime payTime;
}
