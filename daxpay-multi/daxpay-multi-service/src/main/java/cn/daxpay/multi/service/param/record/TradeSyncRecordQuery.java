package cn.daxpay.multi.service.param.record;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 交易同步记录查询参数
 * @author xxm
 * @since 2024/1/9
 */
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "交易同步记录查询参数")
public class TradeSyncRecordQuery extends SortParam {

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
    @Schema(description = "交易类型")
    private String tradeType;

    /**
     * 同步通道
     * @see ChannelEnum#getCode()
     */
    @Schema(description = "同步通道")
    private String channel;

    /** 三方支付返回的消息内容 */
    @Schema(description = "消息内容")
    private String syncInfo;

    /**
     * 支付单如果状态不一致, 是否进行调整
     */
    @Schema(description = "是否进行调整")
    private boolean adjust;

    /** 调整单号 */
    @Schema(description = "调整单号")
    private String adjustNo;

    /** 客户端IP */
    @Schema(description = "客户端IP")
    private String clientIp;
}
