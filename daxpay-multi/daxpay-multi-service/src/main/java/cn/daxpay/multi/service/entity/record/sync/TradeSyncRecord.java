package cn.daxpay.multi.service.entity.record.sync;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchBaseEntity;
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
public class TradeSyncRecord extends MchBaseEntity implements ToResult<TradeSyncRecordResult> {

    /** 本地交易号 */
    private String tradeNo;

    /** 商户交易号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /**
     * 三方支付返回状态, 分账无返回状态
     */
    private String outTradeStatus;

    /**
     * 同步类型 支付/退款/分账/转账
     * @see TradeTypeEnum
     */
    private String type;

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
