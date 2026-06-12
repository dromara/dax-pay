package org.dromara.daxpay.payment.channel.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.bo.profile.OnbBaseProfileBo;
import org.dromara.daxpay.payment.channel.convert.profile.OnbMerchantProfileConvert;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 进件商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_onb_base_profile", autoResultMap = true)
public class OnbBaseProfile extends MchBaseEntity implements ToResult<OnbBaseProfileBo> {

    /// 进件申请Id
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long applyId;

    /// 主体类型
    /// @see SubjectTypeEnum
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String subjectType;

    /// 商户名称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String merchantName;

    /// 商户简称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String merchantShortName;

    /// 经营内容编号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String businessContent;

    @Override
    public OnbBaseProfileBo toResult() {
        return OnbMerchantProfileConvert.CONVERT.toResult(this);
    }

}

