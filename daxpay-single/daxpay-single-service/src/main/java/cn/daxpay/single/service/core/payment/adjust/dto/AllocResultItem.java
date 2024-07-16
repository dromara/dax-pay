package cn.daxpay.single.service.core.payment.adjust.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账结果明细
 * @author xxm
 * @since 2024/7/16
 */
@Data
@Accessors(chain = true)
public class AllocResultItem {

    /** 账号 */
    private String account;

    /** 金额 */
    private Integer amount;

    /** 状态 */
    private String result;

    /** 完成时间 */
    private LocalDateTime finishTime;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

}
