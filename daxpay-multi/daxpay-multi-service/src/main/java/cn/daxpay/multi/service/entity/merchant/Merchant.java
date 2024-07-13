package cn.daxpay.multi.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.convert.merchant.MerchantConvert;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant")
public class Merchant extends MpBaseEntity implements ToResult<MerchantResult> {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchNo;

    /** 商户名称 */
    private String mchName;

    /** 公司名称 */
    private String companyName;

    /** 证件类型 */
    private String idType;

    /** 证件号 */
    private String idNo;

    /** 联系方式 */
    private String contact;

    /** 法人名称 */
    private String legalPerson;

    /** 法人证件号码 */
    private String legalPersonIdNo;

    /** 状态 */
    private String status;

    @Override
    public MerchantResult toResult() {
        return MerchantConvert.CONVERT.toResult(this);
    }
}
