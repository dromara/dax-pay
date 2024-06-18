package cn.daxpay.single.service.core.record.flow.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.code.TradeFlowRecordTypeEnum;
import cn.daxpay.single.service.core.record.flow.convert.TradeFlowRecordConvert;
import cn.daxpay.single.service.dto.record.flow.TradeFlowRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 资金流水记录
 * @author xxm
 * @since 2024/4/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_flow_record")
@DbTable(comment = "资金流水记录")
public class TradeFlowRecord extends MpCreateEntity implements EntityBaseFunction<TradeFlowRecordDto> {

    /** 订单标题 */
    @DbColumn(comment = "标题", length = 100, isNull = false)
    private String title;

    /** 金额 */
    @DbColumn(comment = "金额", length = 15, isNull = false)
    private Integer amount;

    /**
     * 业务类型
     * @see TradeFlowRecordTypeEnum
     */
    @DbColumn(comment = "业务类型", length = 20, isNull = false)
    private String type;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道", length = 20, isNull = false)
    private String channel;

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

    @Override
    public TradeFlowRecordDto toDto() {
        return TradeFlowRecordConvert.CONVERT.convert(this);
    }
}
