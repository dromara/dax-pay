package cn.daxpay.multi.sdk.model.sync;

import cn.daxpay.multi.sdk.code.RefundSyncStatusEnum;
import cn.daxpay.multi.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RefundSyncModel extends DaxPayResponseModel {

    /**
     * 同步结果
     * @see RefundSyncStatusEnum
     */
    private String status;

}
