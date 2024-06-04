package cn.daxpay.multi.service.entity.order.transfer;

import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账订单
 * @author xxm
 * @since 2024/6/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_transfer_order")
public class TransferOrder extends MchEntity {
}
