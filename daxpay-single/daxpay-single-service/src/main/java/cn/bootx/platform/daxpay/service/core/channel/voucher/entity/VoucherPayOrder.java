package cn.bootx.platform.daxpay.service.core.channel.voucher.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.service.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherPayOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "pay_voucher_pay_order",autoResultMap = true)
public class VoucherPayOrder extends BasePayOrder implements EntityBaseFunction<VoucherPayOrderDto> {

    /** 扣款储值卡 */
    @DbColumn(comment = "扣款储值卡")
    @DbMySqlFieldType(MySqlFieldTypeEnum.JSON)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private VoucherRecord voucherRecord;


    @Override
    public VoucherPayOrderDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
