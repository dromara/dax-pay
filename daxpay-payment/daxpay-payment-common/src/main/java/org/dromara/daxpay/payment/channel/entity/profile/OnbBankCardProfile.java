package org.dromara.daxpay.payment.channel.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.convert.profile.OnbBankCardProfileConvert;
import org.dromara.daxpay.platform.core.enums.channel.OnbBankAccountTypeEnum;
import org.dromara.daxpay.payment.channel.bo.profile.OnbBankCardProfileBo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 进件结算卡信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_onb_bank_card_profile", autoResultMap = true)
public class OnbBankCardProfile extends MchBaseEntity implements ToResult<OnbBankCardProfileBo> {

    /// 进件申请Id
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long applyId;

    /// 账户类型
    /// @see OnbBankAccountTypeEnum
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String accountType;

    /// 银行卡账户名
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String accountName;

    /// 银行卡号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardNo;

    /// 银行卡开户行名称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String bankName;

    /// 银行卡开户行联行号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String branchNo;

    /// 银行预留手机号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String bankPhone;

    /// 银行卡正面照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardFrontPic;

    /// 银行卡正面照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardFrontPicUrl;

    /// 银行卡反面照片(媒体ID)
    @Schema(description = "银行卡反面照片")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardBackPic;

    /// 银行卡反面照片路径(系统存储)
    @Schema(description = "银行卡反面照片地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardBackPicUrl;

    @Override
    public OnbBankCardProfileBo toResult() {
        return OnbBankCardProfileConvert.CONVERT.toResult(this);
    }

}

