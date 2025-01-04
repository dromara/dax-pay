package org.dromara.daxpay.service.entity.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.result.allocation.receiver.AllocGroupReceiverVo;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocGroupReceiverConvert;

import java.math.BigDecimal;

/**
 * 分账组和接收者关系
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_group_receiver")
public class AllocGroupReceiver extends MchAppBaseEntity implements ToResult<AllocGroupReceiverVo> {

    /** 分账组ID */
    private Long groupId;

    /** 接收者ID */
    private Long receiverId;

    /** 分账比例(百分之多少) */
    private BigDecimal rate;

    @Override
    public AllocGroupReceiverVo toResult() {
        return AllocGroupReceiverConvert.CONVERT.toVo(this);
    }
}
