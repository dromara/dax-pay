package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.code.AllocOrderResultEnum;
import cn.daxpay.single.sdk.code.AllocOrderStatusEnum;
import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分账订单返回结果
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocOrderModel extends DaxPayResponseModel {

    /** 分账单号 */
    private String allocNo;

    /** 商户分账单号 */
    private String bizAllocNo;

    /** 通道分账号 */
    private String outAllocNo;

    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 通道支付订单号 */
    private String outOrderNo;

    /** 支付订单标题 */
    private String title;

    /**
     * 所属通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 总分账金额 */
    private Integer amount;

    /** 分账描述 */
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
    private String status;

    /**
     * 处理结果
     * @see AllocOrderResultEnum
     */
    private String result;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /** 分账订单完成时间 */
    private LocalDateTime finishTime;

    /** 分账明细 */
    private List<AllocOrderDetailModel> details;

}
