package cn.bootx.platform.daxpay.sdk.model.sync;

import cn.bootx.platform.daxpay.sdk.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款信息同步结果
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
@ToString
public class RefundSyncModel extends DaxPayResponseModel {

    /**
     * 支付网关同步结果
     * @see RefundSyncStatusEnum
     */
    private String gatewayStatus;

    /** 是否进行了修复 */
    private boolean repair;

    /** 修复号 */
    private String repairOrderNo;

}
