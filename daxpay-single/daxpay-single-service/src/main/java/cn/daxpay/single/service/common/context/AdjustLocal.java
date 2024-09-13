package cn.daxpay.single.service.common.context;

import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调整相关信息
 * @author xxm
 * @since 2024/1/25
 */
@Data
@Accessors(chain = true)
public class AdjustLocal {

    /**
     * 触发来源
     */
    private TradeAdjustSourceEnum source;

    /** 完成时间 */
    private LocalDateTime finishTime;

}
