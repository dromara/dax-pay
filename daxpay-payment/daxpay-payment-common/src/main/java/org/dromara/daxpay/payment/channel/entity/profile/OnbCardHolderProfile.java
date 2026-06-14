package org.dromara.daxpay.payment.channel.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.convert.profile.OnbCardHolderProfileConvert;
import org.dromara.daxpay.payment.channel.bo.profile.OnbCardHolderProfileBo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 持卡人信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_onb_card_holder_profile", autoResultMap = true)
public class OnbCardHolderProfile extends MchBaseEntity implements ToResult<OnbCardHolderProfileBo> {

    /// 进件申请Id
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long applyId;

    /// 持卡人姓名
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String holderName;

    /// 身份证号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String certNo;

    /// 身份证长期有效
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private boolean periodLong;

    /// 身份证开始时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate startDate;

    /// 身份证结束时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate endDate;

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

    /// 非法人结算授权函图片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String letterOfAuthPic;

    /// 非法人结算授权函图片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String letterOfAuthPicUrl;

    /// 转换
    @Override
    public OnbCardHolderProfileBo toResult() {
        return OnbCardHolderProfileConvert.CONVERT.toResult(this);
    }

}
