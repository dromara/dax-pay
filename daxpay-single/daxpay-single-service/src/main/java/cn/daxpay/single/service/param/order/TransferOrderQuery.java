package cn.daxpay.single.service.param.order;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.TransferPayeeTypeEnum;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.code.TransferTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账订单查询参数
 * @author xxm
 * @since 2024/6/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单查询参数")
public class TransferOrderQuery extends QueryOrder {

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
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 转账金额 */
    @Schema(description = "转账金额")
    private Integer amount;

    /** 标题 */
    @Schema(description = "标题")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String title;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    @Schema(description = "转账类型, 微信使用")
    private String transferType;

    /** 付款方 */
    @Schema(description = "付款方")
    private String payer;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    @Schema(description = "收款人类型")
    private String payeeType;

    /** 收款人账号 */
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /** 成功时间 */
    @Schema(description = "成功时间")
    private LocalDateTime successTime;

    /**
     * 错误码
     */
    @Schema(description = "错误码")
    private String errorCode;

}
