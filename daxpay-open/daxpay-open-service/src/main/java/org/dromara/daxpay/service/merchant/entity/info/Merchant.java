package org.dromara.daxpay.service.merchant.entity.info;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import org.dromara.daxpay.service.merchant.convert.info.MerchantConvert;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_merchant")
public class Merchant extends MpBaseEntity implements ToResult<MerchantResult> {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchNo;

    /** 商户名称 */
    private String mchName;

    /** 商户简称 */
    private String mchShortName;

    /** 是否有关联管理员 */
    private boolean administrator;

    /** 关联管理员用户 */
    private Long adminUserId;

    /**
     * 状态
     * @see MerchantStatusEnum
     */
    private String status;

    @Override
    public MerchantResult toResult() {
        return MerchantConvert.CONVERT.toResult(this);
    }
}
