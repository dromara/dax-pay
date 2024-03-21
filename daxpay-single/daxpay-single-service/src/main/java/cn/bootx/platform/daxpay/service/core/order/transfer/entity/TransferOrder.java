package cn.bootx.platform.daxpay.service.core.order.transfer.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账订单
 * @author xxm
 * @since 2024/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "转账订单")
@TableName("pay_transfer_order")
public class TransferOrder extends MpBaseEntity {

    /** 业务号 */
    private String outTradeNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 金额 */
    private Integer amount;

    /** 状态 */
    private String status;

    /** 付款方 */
    private String payer;

    /** 收款方 */
    private String payee;

    /** 成功时间 */
    private LocalDateTime successTime;

}
