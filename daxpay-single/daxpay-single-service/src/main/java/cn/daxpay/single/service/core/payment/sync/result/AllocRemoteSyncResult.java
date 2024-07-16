package cn.daxpay.single.service.core.payment.sync.result;

import cn.daxpay.single.service.core.payment.adjust.dto.AllocResultItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账同步结果
 * @author xxm
 * @since 2024/5/23
 */
@Data
@Accessors(chain = true)
public class AllocRemoteSyncResult {
    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /** 通用分账结果明细 */
    private List<AllocResultItem> resultItems;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;

}
