package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchBankCardProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchBankCardProfileResult;
import org.dromara.daxpay.platform.core.enums.channel.OnbBankAccountTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户结算卡信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("mch_bank_card_profile")
public class MchBankCardProfile extends MchBaseEntity implements ToResult<MchBankCardProfileResult> {

    /// 银行账户类型
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

    /// 银行卡正面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardFrontPic;

    /// 银行卡反面照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cardBackPic;

    /// 转换为结果对象
    @Override
    public MchBankCardProfileResult toResult() {
        return MchBankCardProfileConvert.CONVERT.toResult(this);
    }
}

