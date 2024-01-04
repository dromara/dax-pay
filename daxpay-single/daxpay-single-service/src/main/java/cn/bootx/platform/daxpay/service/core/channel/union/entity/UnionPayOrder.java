package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.table.modify.annotation.DbTable;
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
@DbTable(comment = "云闪付支付订单")
@Accessors(chain = true)
@TableName("pay_union_pay_order")
public class UnionPayOrder extends BasePayOrder {

}
