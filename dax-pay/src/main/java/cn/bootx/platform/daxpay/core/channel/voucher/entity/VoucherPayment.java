package cn.bootx.platform.daxpay.core.channel.voucher.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import cn.bootx.platform.daxpay.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherPaymentDto;
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
@Accessors(chain = true)
@TableName("pay_voucher_payment")
public class VoucherPayment extends BasePayment implements EntityBaseFunction<VoucherPaymentDto> {

    /** 储值卡id列表 */
    private String voucherIds;

    @Override
    public VoucherPaymentDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
