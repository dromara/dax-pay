package org.dromara.daxpay.service.param.order.transfer;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.TransferPayeeTypeEnum;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账订单查询参数
 * @author xxm
 * @since 2024/6/17
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单查询参数")
public class TransferOrderQuery extends MchAppQuery {

    /** 商户转账号 */
    @Schema(description = "商户转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String bizTransferNo;

    /** 转账号 */
    @Schema(description = "转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String transferNo;

    /** 通道转账号 */
    @Schema(description = "通道转账号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String outTransferNo;

    /**
     * 支付通道
     * @see org.dromara.daxpay.core.enums.ChannelEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /** 标题 */
    @Schema(description = "标题")
    private String title;



    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "收款人类型")
    private String payeeType;

    /** 收款人账号 */
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "状态")
    private String status;
}
