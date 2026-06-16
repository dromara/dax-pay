package org.dromara.daxpay.payment.old.pay.entity.record.sync;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppRecordEntity;
import org.dromara.daxpay.payment.old.pay.convert.record.TradeSyncRecordConvert;
import org.dromara.daxpay.payment.old.pay.result.record.sync.TradeSyncRecordResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 交易同步记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_sync_record")
public class TradeSyncRecord extends MchAppRecordEntity implements ToResult<TradeSyncRecordResult> {

    /// 平台交易号
    private String tradeNo;

    /// 商户交易号
    private String bizTradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 通道返回的状态
    private String outTradeStatus;

    /// 交易类型
    /// @see TradeTypeEnum
    private String tradeType;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 同步通道
    /// @see ChannelEnum#getCode()
    private String channel;

    /// 网关返回的同步消息
    private String syncInfo;

    /// 支付单如果状态不一致, 是否进行调整
    private boolean adjust;

    /// 错误码
    private String errorCode;

    /// 错误信息
    private String errorMsg;

    /// 终端ip
    private String clientIp;

    public TradeSyncRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg,0,300);
        return this;
    }

    /// 转换
    @Override
    public TradeSyncRecordResult toResult() {
        return TradeSyncRecordConvert.CONVERT.convert(this);
    }
}

