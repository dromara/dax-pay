package cn.bootx.platform.daxpay.core.channel.union.entity;

import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_union_payment")
public class UnionPayment extends BasePayment {

}
