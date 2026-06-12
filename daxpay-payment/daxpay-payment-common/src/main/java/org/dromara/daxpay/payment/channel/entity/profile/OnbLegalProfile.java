package org.dromara.daxpay.payment.channel.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.convert.profile.OnbLegalProfileConvert;
import org.dromara.daxpay.payment.channel.bo.profile.OnbLegalProfileBo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 法人信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_onb_legal_profile", autoResultMap = true)
public class OnbLegalProfile extends MchBaseEntity implements ToResult<OnbLegalProfileBo> {

    /// 进件申请Id
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long applyId;

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

    /// 身份证人像面照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String frontPic;

    /// 身份证人像面照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String frontPicUrl;

    /// 身份证国徽面照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String backPic;

    /// 身份证国徽面照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String backPicUrl;

    /// 转换
    @Override
    public OnbLegalProfileBo toResult() {
        return OnbLegalProfileConvert.CONVERT.toResult(this);
    }
}
