package cn.daxpay.multi.service.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款订单
 * @author xxm
 * @since 2024/5/27
 */
@Data
@Accessors(chain = true)
@TableName("pay_refund_order")
public class RefundOrder {
}
