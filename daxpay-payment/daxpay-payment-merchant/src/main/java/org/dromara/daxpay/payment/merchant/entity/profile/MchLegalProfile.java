package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchLegalProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchLegalProfileResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 商户法人信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_legal_profile")
public class MchLegalProfile extends MchBaseEntity implements ToResult<MchLegalProfileResult> {

    /// 法人姓名
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String legalName;

    /// 身份证号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String certNo;

    /// 联系人手机号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String contactPhone;

    /// 身份证长期有效
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private boolean periodLong;

    /// 身份证开始时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate startDate;

    /// 身份证结束时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate endDate;

    /// 身份证地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String address;

    /// 身份证人像面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String frontPic;

    /// 身份证国徽面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String backPic;

    /// 转换为结果对象
    @Override
    public MchLegalProfileResult toResult() {
        return MchLegalProfileConvert.CONVERT.toResult(this);
    }
}
