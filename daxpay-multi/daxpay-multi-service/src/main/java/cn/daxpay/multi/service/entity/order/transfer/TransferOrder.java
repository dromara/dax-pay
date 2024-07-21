package cn.daxpay.multi.service.entity.order.transfer;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.TransferPayeeTypeEnum;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import cn.daxpay.multi.service.result.order.transfer.TransferOrderResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 转账订单
 * @author xxm
 * @since 2024/6/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_transfer_order")
public class TransferOrder extends MchEntity implements ToResult<TransferOrderResult> {
    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /** 通道转账号 */
    private String outTransferNo;

    /**
     * 支付通道
     * @see cn.daxpay.multi.core.enums.ChannelEnum
     */
    private String channel;

    /** 转账金额 */
    private BigDecimal amount;

    /** 标题 */
    private String title;

    /** 转账原因/备注 */
    private String reason;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    private String payeeType;

    /** 收款人账号 */
    private String payeeAccount;

    /** 收款人姓名 */
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    private String status;

    /** 成功时间 */
    private LocalDateTime successTime;


    /** 异步通知地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    private String attach;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

    /** 终端ip */
    private String clientIp;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    @Override
    public TransferOrderResult toResult() {
        return null;
    }
}
