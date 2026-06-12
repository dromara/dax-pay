package org.dromara.daxpay.payment.merchant.entity.info;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.info.MerchantInfoConvert;
import org.dromara.daxpay.payment.merchant.result.info.MerchantInfoResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("mch_info")
public class MerchantInfo extends MchBaseEntity implements ToResult<MerchantInfoResult> {

    /// 商户名称
    private String mchName;

    /// 商户简称
    private String mchShortName;

    /// 关联管理员用户
    private Long adminUserId;

    /// 状态
    /// @see MerchantStatusEnum
    private String status;

    /// 主体类型
    /// @see SubjectTypeEnum
    private String subjectType;

    @Override
    public MerchantInfoResult toResult() {
        return MerchantInfoConvert.CONVERT.toResult(this);
    }
}

