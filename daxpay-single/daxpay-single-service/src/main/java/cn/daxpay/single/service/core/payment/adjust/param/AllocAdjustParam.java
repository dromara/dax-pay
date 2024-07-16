package cn.daxpay.single.service.core.payment.adjust.param;

import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.adjust.dto.AllocResultItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账调整参数
 * @author xxm
 * @since 2024/7/16
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账调整参数")
public class AllocAdjustParam {


    /** 触发来源 */
    @Schema(description = "触发来源")
    private TradeAdjustSourceEnum source;

    /** 分账订单 */
    private AllocOrder order;

    /** 分账明细订单 */
    private List<AllocOrderDetail> details;

    /** 分账结果明细列表 */
    private List<AllocResultItem> resultItems;
}
