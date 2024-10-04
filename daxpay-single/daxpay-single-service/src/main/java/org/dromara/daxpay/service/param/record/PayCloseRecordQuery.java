package org.dromara.daxpay.service.param.record;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/9
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordQuery extends MchAppQuery {

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see ChannelEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "关闭的支付通道")
    private String channel;

    /**
     * 是否关闭成功
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否关闭成功")
    private Boolean closed;
}
