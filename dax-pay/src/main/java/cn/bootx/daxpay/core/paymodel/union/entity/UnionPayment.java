package cn.bootx.daxpay.core.paymodel.union.entity;

import cn.bootx.daxpay.core.paymodel.base.entity.BasePayment;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_union_payment")
public class UnionPayment extends BasePayment {

}
