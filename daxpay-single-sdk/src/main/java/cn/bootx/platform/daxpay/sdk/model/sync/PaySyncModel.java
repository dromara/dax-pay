package cn.bootx.platform.daxpay.sdk.model.sync;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付单同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Getter
@Setter
@ToString
public class PaySyncModel extends DaxPayResponseModel {

    /**
     * 支付网关同步状态
     * @see PaySyncStatusEnum
     */
    private String gatewayStatus;

    /** 是否同步成功 */
    private boolean success;

    /** 是否进行了修复 */
    private boolean repair;

    /** 修复号 */
    private String repairOrderNo;

    /** 失败原因 */
    private String errorMsg;

}
