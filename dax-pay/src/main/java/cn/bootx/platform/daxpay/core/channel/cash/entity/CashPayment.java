package cn.bootx.platform.daxpay.core.channel.cash.entity;

import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 现金支付记录
 *
 * @author xxm
 * @since 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_cash_payment")
@Accessors(chain = true)
public class CashPayment extends BasePayment {

}
