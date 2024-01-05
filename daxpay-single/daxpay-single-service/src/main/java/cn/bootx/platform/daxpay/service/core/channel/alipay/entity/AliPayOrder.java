package cn.bootx.platform.daxpay.service.core.channel.alipay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.service.core.channel.alipay.convert.AlipayConvert;
import cn.bootx.platform.daxpay.service.dto.channel.alipay.AliPaymentDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝支付记录
 *
 * @author xxm
 * @since 2021/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付宝支付记录")
@TableName("pay_ali_pay_order")
public class AliPayOrder extends BasePayOrder implements EntityBaseFunction<AliPaymentDto> {

    /** 支付宝交易号 */
    @DbColumn(comment = "交易号")
    private String tradeNo;

    /** 所使用的支付方式 */
    @DbColumn(comment = "支付方式")
    private String payWay;

    @Override
    public AliPaymentDto toDto() {
        return AlipayConvert.CONVERT.convert(this);
    }

}
