package cn.bootx.platform.daxpay.core.channel.union.entity;

import cn.bootx.platform.daxpay.common.entity.BasePayOrder;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 云闪付支付订单
 * @author xxm
 * @since 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_union_payment")
public class UnionPayOrder extends BasePayOrder {

}
