package cn.bootx.platform.daxpay.core.channel.voucher.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlFieldType;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlFieldTypeEnum;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import cn.bootx.platform.daxpay.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherPaymentDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 储值卡支付记录
 *
 * @author xxm
 * @since 2022/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_voucher_payment",autoResultMap = true)
public class VoucherPayment extends BasePayment implements EntityBaseFunction<VoucherPaymentDto> {

    /** 储值卡扣款记录列表 */
    @DbColumn(comment = "储值卡扣款列表")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    private List<VoucherRecord> voucherRecords;


    @Override
    public VoucherPaymentDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
