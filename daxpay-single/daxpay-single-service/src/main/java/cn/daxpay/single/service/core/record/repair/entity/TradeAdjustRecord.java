package cn.daxpay.single.service.core.record.repair.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.core.record.repair.convert.TradeAdjustRecordConvert;
import cn.daxpay.single.service.dto.record.repair.TradeAdjustRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单调整记录
 * @author xxm
 * @since 2024/7/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_adjust_record")
public class TradeAdjustRecord extends MpCreateEntity implements EntityBaseFunction<TradeAdjustRecordDto> {

    /**
     * 调整号
     */
    @DbColumn(comment = "调整号", length = 32, isNull = false)
    private String adjustNo;

    /** 交易ID */
    @DbColumn(comment = "本地订单ID", isNull = false)
    private Long tradeId;

    /**
     * 本地交易号, 支付号/退款号
     */
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /** 通道 */
    @DbColumn(comment = "通道", length = 20, isNull = false)
    private String channel;

    /**
     * 来源
     * @see TradeAdjustSourceEnum
     */
    @DbColumn(comment = "修复来源", length = 20, isNull = false)
    private String source;

    /**
     * 调整类型
     * @see cn.daxpay.single.service.code.TradeNotifyTypeEnum
     */
    @DbColumn(comment = "调整类型", length = 20, isNull = false)
    private String type;

    /**
     * 调整方式
     */
    @DbColumn(comment = "调整方式", length = 20, isNull = false)
    private String way;
    /**
     * 调整前状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "修复前状态", length = 20, isNull = false)
    private String beforeStatus;

    /**
     * 调整后状态
     */
    @DbColumn(comment = "修复后状态", length = 20, isNull = false)
    private String afterStatus;

    /**
     * 备注
     */
    @DbColumn(comment = "备注", length = 600)
    private String remark;


    @Override
    public TradeAdjustRecordDto toDto() {
        return TradeAdjustRecordConvert.CONVERT.convert(this);
    }
}
