package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchCardHolderProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchCardHolderProfileResult;
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
@TableName("mch_card_holder_profile")
public class MchCardHolderProfile extends MchBaseEntity {

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

    /// 身份证人像面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String frontPic;

    /// 身份证国徽面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String backPic;

    /// 非法人结算授权函图片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String letterOfAuthPic;

    /// 转换为结果对象
    public MchCardHolderProfileResult toResult() {
        return MchCardHolderProfileConvert.CONVERT.toResult(this);
    }
}
