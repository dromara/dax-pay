package cn.daxpay.open.payment.trade.record.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.record.convert.PayCallbackRecordConvert;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道入站回调记录
///
/// 记录通道 → 平台的异步回调报文与处理结果, 只审计不重放
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_callback_record")
public class PayCallbackRecord extends MchBaseEntity implements ToResult<PayCallbackRecordResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 通道商户号(回调 path 入站身份)
    private String channelMchNo;

    /// 平台交易号(支付回调为 trade_no, 退款回调为 refund_no)
    private String tradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 回调类型: pay / refund
    /// @see TradeFlowTypeEnum
    private String callbackType;

    /// 通知消息内容(JSON)
    private String notifyInfo;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    private String status;

    /// 错误信息
    private String errorMsg;

    /// 错误信息截断(最长300), 防止超长写入
    public PayCallbackRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg, 0, 300);
        return this;
    }

    @Override
    public PayCallbackRecordResult toResult() {
        return PayCallbackRecordConvert.CONVERT.toResult(this);
    }
}
