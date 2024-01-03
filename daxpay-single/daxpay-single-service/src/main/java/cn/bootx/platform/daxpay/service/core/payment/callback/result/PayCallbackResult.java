package cn.bootx.platform.daxpay.service.core.payment.callback.result;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付回调处理结果
 *
 * @author xxm
 * @since 2021/6/22
 */
@Data
@Accessors(chain = true)
public class PayCallbackResult {

    /**
     * 处理状态
     * @see PayStatusEnum
     */
    private String status;

    /** 提示信息 */
    private String msg;

}
