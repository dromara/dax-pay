package cn.bootx.platform.daxpay.service.param.channel.union;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 云闪付同步回调参数
 * 不同支付方式回调的字段会不一样
 * @author xxm
 * @since 2024/2/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "云闪付同步回调参数")
public class UnionPayReturnParam {
    /** 条码支付返回的 */
    private String orderNo;
    /** Web/Wap支付返回的 */
    private String orderId;
}
