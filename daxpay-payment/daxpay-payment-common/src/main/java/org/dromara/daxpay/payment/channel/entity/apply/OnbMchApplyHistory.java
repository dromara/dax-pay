package org.dromara.daxpay.payment.channel.entity.apply;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户入驻申请历史记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_onb_mch_apply_history")
public class OnbMchApplyHistory extends MpCreateEntity {

    /// 进件申请Id
    private Long applyId;

}
