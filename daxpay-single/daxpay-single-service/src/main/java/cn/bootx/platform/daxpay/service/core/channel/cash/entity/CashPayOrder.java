package cn.bootx.platform.daxpay.service.core.channel.cash.entity;

import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.table.modify.annotation.DbTable;
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
@DbTable(comment = "现金支付记录")
@TableName("pay_cash_payment")
@Accessors(chain = true)
public class CashPayOrder extends BasePayOrder {

}
