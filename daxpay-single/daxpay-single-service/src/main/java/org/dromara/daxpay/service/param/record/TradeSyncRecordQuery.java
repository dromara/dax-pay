package org.dromara.daxpay.service.param.record;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 交易同步记录查询参数
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "交易同步记录查询参数")
public class TradeSyncRecordQuery extends MchAppQuery {

    /** 平台交易号 */
    @Schema(description = "平台交易号")
    private String tradeNo;

    /** 商户交易号 */
    @Schema(description = "商户交易号")
    private String bizTradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String outTradeNo;

    /**
     * 同步结果
     */
    @Schema(description = "同步结果")
    private String outTradeStatus;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易类型")
    private String tradeType;

    /**
     * 同步通道
     * @see ChannelEnum#getCode()
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "同步通道")
    private String channel;

    /**
     * 支付单如果状态不一致, 是否进行调整
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否进行调整")
    private Boolean adjust;
}
