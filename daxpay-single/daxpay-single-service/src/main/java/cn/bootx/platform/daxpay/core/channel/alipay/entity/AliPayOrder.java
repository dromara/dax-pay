package cn.bootx.platform.daxpay.core.channel.alipay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.dto.channel.alipay.AliPaymentDto;
import cn.hutool.core.bean.BeanUtil;
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
@TableName("pay_ali_payment")
public class AliPayOrder extends BasePayOrder implements EntityBaseFunction<AliPaymentDto> {

    /** 支付宝交易号 */
    private String tradeNo;

    /** 支付方式 */
    private String payWay;

    @Override
    public AliPaymentDto toDto() {
        AliPaymentDto dto = new AliPaymentDto();
        BeanUtil.copyProperties(this, dto);
        return dto;
    }

}
