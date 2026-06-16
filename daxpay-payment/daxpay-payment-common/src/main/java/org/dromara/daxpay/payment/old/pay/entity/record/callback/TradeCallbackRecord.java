package org.dromara.daxpay.payment.old.pay.entity.record.callback;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppRecordEntity;
import org.dromara.daxpay.payment.old.pay.convert.record.TradeCallbackRecordConvert;
import org.dromara.daxpay.payment.old.pay.result.record.callback.TradeCallbackRecordResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 回调通知记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_callback_record")
public class TradeCallbackRecord extends MchAppRecordEntity implements ToResult<TradeCallbackRecordResult> {

    /// 平台交易号
    private String tradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 回调类型
    /// @see TradeTypeEnum
    private String callbackType;

    /// 通知消息内容
    private String notifyInfo;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    private String status;

    /// 错误码
    private String errorCode;

    /// 提示信息
    private String errorMsg;

    public TradeCallbackRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg,0,300);
        return this;
    }

    /// 转换
    @Override
    public TradeCallbackRecordResult toResult() {
        return TradeCallbackRecordConvert.CONVERT.convert(this);
    }
}

