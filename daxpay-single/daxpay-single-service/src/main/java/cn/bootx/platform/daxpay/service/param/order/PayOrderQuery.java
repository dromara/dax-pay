package cn.bootx.platform.daxpay.service.param.order;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单查询参数
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam
@Accessors(chain = true)
@Schema(title = "支付订单查询参数")
public class PayOrderQuery extends QueryOrder {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付订单id")
    private Long id;

    /** 关联的业务号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "关联的业务号")
    private String businessNo;

    /** 标题 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "标题")
    private String title;

    /** 是否是异步支付 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否是异步支付")
    private Boolean asyncPay;

    /** 是否是组合支付 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否是组合支付")
    private Boolean combinationPay;

    /**
     * 异步支付通道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "异步支付通道")
    private String asyncChannel;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付状态")
    private String status;

}
