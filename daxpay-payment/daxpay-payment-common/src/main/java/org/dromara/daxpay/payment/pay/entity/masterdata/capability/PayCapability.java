package org.dromara.daxpay.payment.pay.entity.masterdata.capability;

import org.dromara.daxpay.payment.pay.convert.capability.PayCapabilityConvert;
import org.dromara.daxpay.payment.pay.result.masterdata.capability.PayCapabilityResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付能力主数据
///
/// `code` 对齐 `PayCapabilityEnum`（独立发版字典，与支付方式无表字段关联）；展示名走 enum i18n。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_capability")
public class PayCapability extends MpBaseEntity implements ToResult<PayCapabilityResult> {

    /// 支付能力编码
    /// @see PayCapabilityEnum
    private String code;

    /// 全局排序
    private Integer sortNo;

    /// 说明
    private String description;

    /// 转换
    @Override
    public PayCapabilityResult toResult() {
        return PayCapabilityConvert.CONVERT.toResult(this);
    }
}