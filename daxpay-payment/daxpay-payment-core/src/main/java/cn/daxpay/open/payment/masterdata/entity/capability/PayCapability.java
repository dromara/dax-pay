package cn.daxpay.open.payment.masterdata.entity.capability;

import cn.daxpay.open.payment.masterdata.convert.capability.PayCapabilityConvert;
import cn.daxpay.open.payment.masterdata.result.capability.PayCapabilityResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
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
@TableName("pay_md_capability")
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