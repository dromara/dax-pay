package cn.bootx.platform.daxpay.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 修复相关信息
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class RepairLocal {

    /** 触发来源 */
    private String source;

    /** 支付完成/退款时间 */
    private LocalDateTime finishTime;

}
