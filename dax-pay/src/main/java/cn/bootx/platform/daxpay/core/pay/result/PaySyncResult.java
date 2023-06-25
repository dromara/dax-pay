package cn.bootx.platform.daxpay.core.pay.result;

import cn.bootx.platform.daxpay.code.pay.PaySyncStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

import static cn.bootx.platform.daxpay.code.pay.PaySyncStatus.NOT_SYNC;

/**
 * 支付网关通知状态对象
 *
 * @author xxm
 * @since 2021/4/21
 */
@Data
@Accessors(chain = true)
public class PaySyncResult {

    /**
     * 支付网关同步状态
     * @see PaySyncStatus#NOT_SYNC
     */
    private String paySyncStatus = NOT_SYNC;

    /** 网关返回参数 */
    private Map<String, String> map;

}
