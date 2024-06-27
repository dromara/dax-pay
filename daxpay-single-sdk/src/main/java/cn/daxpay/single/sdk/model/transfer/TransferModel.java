package cn.daxpay.single.sdk.model.transfer;

import cn.daxpay.single.sdk.code.TransferStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 转账结果
 * @author xxm
 * @since 2024/6/19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TransferModel extends DaxPayResponseModel {

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    private String status;
}
