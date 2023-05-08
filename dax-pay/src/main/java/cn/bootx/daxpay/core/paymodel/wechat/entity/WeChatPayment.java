package cn.bootx.daxpay.core.paymodel.wechat.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.daxpay.core.paymodel.base.entity.BasePayment;
import cn.bootx.daxpay.core.paymodel.wechat.convert.WeChatConvert;
import cn.bootx.daxpay.dto.paymodel.wechat.WeChatPaymentDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2021/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wechat_payment")
public class WeChatPayment extends BasePayment implements EntityBaseFunction<WeChatPaymentDto> {

    /**
     * 微信交易号
     */
    private String tradeNo;

    @Override
    public WeChatPaymentDto toDto() {
        return WeChatConvert.CONVERT.convert(this);
    }

}
