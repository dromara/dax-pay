package cn.daxpay.single.sdk.model.notice;

import cn.daxpay.single.sdk.code.AllocDetailResultEnum;
import cn.daxpay.single.sdk.code.AllocReceiverTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账明细
 * @author xxm
 * @since 2024/5/30
 */
@Getter
@Setter
@ToString
public class AllocDetailNoticeModel {

    /** 分账接收方编号 */
    private String receiverNo;

    /** 分账金额 */
    private Integer amount;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方姓名 */
    private String receiverName;

    /**
     * 分账结果
     * @see AllocDetailResultEnum
     */
    private String result;

    /** 错误代码 */
    private String errorCode;

    /** 错误原因 */
    private String errorMsg;

    /** 分账完成时间 */
    private Long finishTime;
}
