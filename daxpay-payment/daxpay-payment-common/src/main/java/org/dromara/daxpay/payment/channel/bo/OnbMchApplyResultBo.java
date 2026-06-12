package org.dromara.daxpay.payment.channel.bo;

import org.dromara.daxpay.platform.core.enums.channel.OnbApplyStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户进件申请结果
///
@Data
@Accessors(chain = true)
public class OnbMchApplyResultBo {

    /// 申请单状态
    /// @see OnbApplyStatusEnum
    private OnbApplyStatusEnum status = OnbApplyStatusEnum.APPLY;

    /// 通道申请状态
    private String outStatus;

    /// 错误信息
    private String errorMsg;
}

