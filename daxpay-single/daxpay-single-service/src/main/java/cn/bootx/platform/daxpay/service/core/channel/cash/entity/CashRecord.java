package cn.bootx.platform.daxpay.service.core.channel.cash.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.code.VoucherRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.cash.convert.CashPayConfigConvert;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 现金记录
 * @author xxm
 * @since 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "现金记录")
public class CashRecord extends MpCreateEntity implements EntityBaseFunction<CashRecordDto> {

    /**
     * 业务类型
     * @see VoucherRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * 交易订单号
     * 支付订单/退款订单
     */
    @DbColumn(comment = "交易订单号")
    private String orderId;

    /** 终端ip */
    @DbColumn(comment = "终端ip")
    private String ip;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public CashRecordDto toDto() {
        return CashPayConfigConvert.CONVERT.convert(this);
    }
}
