package cn.daxpay.single.service.core.order.transfer.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.code.PayChannelEnum;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.code.TransferPayeeTypeEnum;
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

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /** 通道转账号 */
    private String outTransferNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 转账金额 */
    private Integer amount;

    /** 标题 */
    private String title;

    /** 转账原因/备注 */
    private String reason;

    /**
     * 转账类型, 微信使用
     */
    private String transferType;

    /** 付款方 */
    private String payer;

    /** 付款方显示名称 */
    private String payerShowName;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    private String payeeType;

    /** 收款人账号 */
    private String payeeAccount;

    /** 收款人姓名 */
    private String payeeName;

    /** 状态 */
    private String status;

    /** 成功时间 */
    private LocalDateTime successTime;

}
