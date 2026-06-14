package org.dromara.daxpay.payment.pay.entity.order.transfer;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferPayeeTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.order.transfer.TransferOrderConvert;
import org.dromara.daxpay.payment.pay.result.order.transfer.TransferOrderVo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 转账订单
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_transfer_order")
public class TransferOrder extends MchAppBaseEntity implements ToResult<TransferOrderVo> {
    /// 商户转账号
    private String bizTransferNo;

    /// 转账号
    private String transferNo;

    /// 通道转账号
    private String outTransferNo;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 转账金额
    private BigDecimal amount;

    /// 标题
    private String title;

    /// 转账原因/备注
    private String reason;

    /// 收款人类型
    /// @see TransferPayeeTypeEnum
    private String payeeType;

    /// 收款人账号
    private String payeeAccount;

    /// 收款人姓名
    private String payeeName;

    /// 状态
    /// @see TransferStatusEnum
    private String status;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 异步通知地址
    private String notifyUrl;

    /// 转账参数, 用于拉起转账确认(微信)
    private String transferBody;

    /// 附加参数, 通常是各通道的参数
    private String extraParam;

    /// 商户扩展参数,回调时会原样返回
    private String attach;

    /// 请求时间
    private OffsetDateTime reqTime;

    /// 终端ip
    private String clientIp;

    /// 订单来源
    private String source;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /// 转换
    @Override
    public TransferOrderVo toResult() {
        return TransferOrderConvert.CONVERT.toVo(this);
    }
}

