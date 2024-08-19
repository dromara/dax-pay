package cn.daxpay.multi.service.param.record;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付回调信息记录
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "支付回调信息记录")
public class TradeCallbackRecordQuery extends SortParam {

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;

    /**
     * 支付通道
     * @see ChannelEnum#getCode()
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 回调类型
     * @see TradeTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调类型")
    private String callbackType;

    /**
     * 回调处理状态
     * @see CallbackStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调处理状态")
    private String status;

    /** 商户号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "应用号")
    private String appId;
}
