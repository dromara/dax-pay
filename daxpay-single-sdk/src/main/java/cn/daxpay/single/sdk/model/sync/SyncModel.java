package cn.daxpay.single.sdk.model.sync;

import cn.daxpay.single.sdk.code.PaySyncStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
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
@ToString
public class SyncModel extends DaxPayResponseModel {

    /**
     * 支付订单同步结果
     * @see PaySyncStatusEnum
     */
    private Boolean status;

}
