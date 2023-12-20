package cn.bootx.platform.daxpay.core.channel.voucher.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherPayOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 储值卡支付记录
 *
 * @author xxm
 * @since 2022/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "储值卡支付记录")
@Accessors(chain = true)
@TableName(value = "pay_voucher_payment",autoResultMap = true)
public class VoucherPayment extends BasePayOrder implements EntityBaseFunction<VoucherPayOrderDto> {

    /** 扣款储值卡 */
    @DbColumn(comment = "扣款储值卡")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private VoucherRecord voucherRecord;


    @Override
    public VoucherPayOrderDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
