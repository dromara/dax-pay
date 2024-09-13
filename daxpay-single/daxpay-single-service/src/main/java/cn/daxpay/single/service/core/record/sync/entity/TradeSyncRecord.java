package cn.daxpay.single.service.core.record.sync.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.core.record.sync.convert.TradeSyncRecordConvert;
import cn.daxpay.single.service.dto.record.sync.SyncRecordDto;
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
@DbTable(comment = "交易同步记录")
@Accessors(chain = true)
@TableName("pay_trade_sync_record")
public class TradeSyncRecord extends MpCreateEntity implements EntityBaseFunction<SyncRecordDto> {

    /** 本地交易号 */
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /** 商户交易号 */
    @DbMySqlIndex(comment = "商户交易号索引")
    @DbColumn(comment = "商户交易号", length = 100, isNull = false)
    private String bizTradeNo;

    /** 通道交易号 */
    @DbMySqlIndex(comment = "通道交易号索引")
    @DbColumn(comment = "通道交易号", length = 150)
    private String outTradeNo;

    /**
     * 三方支付返回状态, 分账无返回状态
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     */
    @DbColumn(comment = "网关返回状态", length = 30)
    private String outTradeStatus;

    /**
     * 同步类型 支付/退款/分账/转账
     * @see TradeTypeEnum
     */
    @DbColumn(comment = "同步类型", length = 20, isNull = false)
    private String type;

    /**
     * 同步通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "同步通道", length = 20, isNull = false)
    private String channel;

    /** 网关返回的同步消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "同步消息", isNull = false)
    private String syncInfo;

    /**
     * 支付单如果状态不一致, 是否进行调整
     */
    @DbColumn(comment = "是否进行调整", isNull = false)
    private boolean adjust;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 2048)
    private String errorMsg;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip", length = 64)
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public SyncRecordDto toDto() {
        return TradeSyncRecordConvert.CONVERT.convert(this);
    }
}
