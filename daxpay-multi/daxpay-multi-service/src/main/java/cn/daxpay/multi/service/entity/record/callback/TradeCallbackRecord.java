package cn.daxpay.multi.service.entity.record.callback;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchRecordEntity;
import cn.daxpay.multi.service.convert.record.TradeCallbackRecordConvert;
import cn.daxpay.multi.service.result.record.callback.TradeCallbackRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 回调通知记录
 * @author xxm
 * @since 2024/6/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_callback_record")
public class TradeCallbackRecord extends MchRecordEntity implements ToResult<TradeCallbackRecordResult> {

    /** 平台交易号 */
    private String tradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 回调类型
     * @see TradeTypeEnum
     */
    private String callbackType;

    /** 通知消息内容 */
    private String notifyInfo;

    /**
     * 回调处理状态
     * @see CallbackStatusEnum
     */
    private String status;

    /** 错误码 */
    private String errorCode;

    /** 提示信息 */
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public TradeCallbackRecordResult toResult() {
        return TradeCallbackRecordConvert.CONVERT.convert(this);
    }
}
