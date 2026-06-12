package org.dromara.daxpay.payment.channel.bo;

import org.dromara.daxpay.platform.core.enums.channel.OnbApplyStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户进件同步
///
@Data
@Accessors(chain = true)
public class OnbMchApplyStatusBo {

    /// 申请单状态
    /// @see OnbApplyStatusEnum
    private OnbApplyStatusEnum status = OnbApplyStatusEnum.APPLY;

    /// 通道申请状态
    private String outStatus;

    /// 错误信息
    private String errorMsg;
}

