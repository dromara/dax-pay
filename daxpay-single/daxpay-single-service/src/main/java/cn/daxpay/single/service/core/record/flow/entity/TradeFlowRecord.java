package cn.daxpay.single.service.core.record.flow.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.daxpay.single.code.PayChannelEnum;
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
    @DbColumn(comment = "标题")
    private String title;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see TradeFlowRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道")
    private String channel;

    /** 本地交易号 */
    @DbColumn(comment = "本地订单号")
    private String tradeNo;

    /** 商户交易号 */
    @DbColumn(comment = "商户交易号")
    private String bizTradeNo;

    /** 三方系统交易号 */
    @DbColumn(comment = "三方系统交易号")
    private String outTradeNo;

    @Override
    public TradeFlowRecordDto toDto() {
        return TradeFlowRecordConvert.CONVERT.convert(this);
    }
}
