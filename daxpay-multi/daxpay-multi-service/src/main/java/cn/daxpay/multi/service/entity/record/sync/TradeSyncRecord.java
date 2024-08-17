package cn.daxpay.multi.service.entity.record.sync;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchRecordEntity;
import cn.daxpay.multi.service.convert.record.TradeSyncRecordConvert;
import cn.daxpay.multi.service.result.record.sync.TradeSyncRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 交易同步记录
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_sync_record")
public class TradeSyncRecord extends MchRecordEntity implements ToResult<TradeSyncRecordResult> {

    /** 平台交易号 */
    private String tradeNo;

    /** 商户交易号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /**
     * 通道返回的状态
     */
    private String outTradeStatus;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /**
     * 同步通道
     * @see ChannelEnum#getCode()
     */
    private String channel;

    /** 网关返回的同步消息 */
    private String syncInfo;

    /**
     * 支付单如果状态不一致, 是否进行调整
     */
    private boolean adjust;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /** 终端ip */
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public TradeSyncRecordResult toResult() {
        return TradeSyncRecordConvert.CONVERT.convert(this);
    }
}
