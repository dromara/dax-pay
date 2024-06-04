package cn.daxpay.single.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.code.TradeFlowRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 流水记录查询类
 * @author xxm
 * @since 2024/5/17
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "流水记录查询类")
public class TradeFlowRecordQuery {

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String title;

    /** 金额 */
    @Schema(description = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see TradeFlowRecordTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务类型")
    private String type;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 商户交易号 */
    @Schema(description = "商户交易号")
    private String bizTradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String outTradeNo;

}
