package cn.daxpay.single.service.core.order.transfer.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.TransferPayeeTypeEnum;
import cn.daxpay.single.code.TransferTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @DbColumn(comment = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @DbColumn(comment = "转账号")
    private String transferNo;

    /** 通道转账号 */
    @DbColumn(comment = "通道转账号")
    private String outTransferNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道")
    private String channel;

    /** 转账金额 */
    @DbColumn(comment = "转账金额")
    private Integer amount;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 转账原因/备注 */
    @DbColumn(comment = "转账原因/备注")
    private String reason;

    /**
     * 转账类型, 微信使用
     * @see TransferTypeEnum
     */
    @DbColumn(comment = "转账类型, 微信使用")
    private String transferType;

    /** 付款方 */
    @DbColumn(comment = "付款方")
    private String payer;

    /** 付款方显示名称 */
    @DbColumn(comment = "付款方显示名称")
    private String payerShowName;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    @DbColumn(comment = "收款人类型")
    private String payeeType;

    /** 收款人账号 */
    @DbColumn(comment = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @DbColumn(comment = "收款人姓名")
    private String payeeName;

    /**
     * 状态
     * @see cn.daxpay.single.code.TransferStatusEnum
     */
    @DbColumn(comment = "状态")
    private String status;

    /** 成功时间 */
    @DbColumn(comment = "成功时间")
    private LocalDateTime successTime;


    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @DbColumn(comment = "商户扩展参数")
    private String attach;

    /** 请求时间，时间戳转时间 */
    @DbColumn(comment = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

    /** 错误提示码 */
    @DbColumn(comment = "错误提示码")
    private String errorCode;

    /** 错误提示 */
    @DbColumn(comment = "错误提示")
    private String errorMsg;

}
