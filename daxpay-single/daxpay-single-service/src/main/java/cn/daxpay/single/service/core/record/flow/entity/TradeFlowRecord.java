package cn.daxpay.single.service.core.record.flow.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.code.TradeFlowRecordTypeEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 资金流水记录
 * @author xxm
 * @since 2024/4/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "资金流水记录")
public class TradeFlowRecord extends MpCreateEntity {
    /** 支付订单标题 */
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

    /** 网关完成时间 */
    @DbColumn(comment = "网关完成时间")
    private LocalDateTime finishTime;
}
