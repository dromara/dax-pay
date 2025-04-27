package org.dromara.daxpay.service.bo.allocation;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账同步结果
 * @author xxm
 * @since 2024/5/23
 */
@Data
@Accessors(chain = true)
public class AllocSyncResultBo {
    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;
}
