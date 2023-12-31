package cn.bootx.platform.daxpay.core.payment.sync.result;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import static cn.bootx.platform.daxpay.code.PaySyncStatusEnum.NOT_SYNC;

/**
 * 支付网关同步状态记录对象
 *
 * @author xxm
 * @since 2021/4/21
 */
@Data
@Accessors(chain = true)
public class GatewaySyncResult {

    /**
     * 支付网关同步状态
     * @see PaySyncStatusEnum#NOT_SYNC
     */
    private String syncStatus = NOT_SYNC.getCode();

    /** 网关返回对象的json字符串 */
    private String json;

    /** 错误提示 */
    private String msg;

}
