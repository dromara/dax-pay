package cn.daxpay.single.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账上下文
 * @author xxm
 * @since 2024/4/15
 */
@Data
@Accessors(chain = true)
public class AllocationLocal {

    /** 通道分账号 */
    private String outAllocNo;
}
