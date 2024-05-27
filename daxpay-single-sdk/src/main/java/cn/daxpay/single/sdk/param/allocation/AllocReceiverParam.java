package cn.daxpay.single.sdk.param.allocation;

import lombok.Getter;
import lombok.Setter;

/**
 * 分账接收方列表参数
 * @author xxm
 * @since 2024/5/21
 */
@Getter
@Setter
public class AllocReceiverParam {

    /** 分账接收方编号 */
    private String receiverNo;

    /** 分账金额 */
    private Integer amount;
}
