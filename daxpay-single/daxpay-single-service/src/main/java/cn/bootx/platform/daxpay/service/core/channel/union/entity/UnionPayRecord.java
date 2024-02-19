package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 云闪付流水记录
 * @author xxm
 * @since 2024/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "云闪付流水记录")
@Accessors(chain = true)
@TableName("pay_union_pay_record")
public class UnionPayRecord extends BasePayOrder {

}
