package cn.bootx.platform.daxpay.service.core.channel.alipay.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝流水记录
 * @author xxm
 * @since 2024/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付宝流水记录")
@TableName("pay_alipay_record")
public class AliPayRecord extends MpCreateEntity {

    /** 标题 */

    /** 金额 */

    /** 业务类型 */

    /** 本地订单号 */

    /** 网关订单号 */
}
