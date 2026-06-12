package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchBaseProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchBaseProfileResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户基础资料
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_base_profile", autoResultMap = true)
public class MchBaseProfile extends MchBaseEntity implements ToResult<MchBaseProfileResult> {

    /// 联系人姓名
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = DataEncryptTypeHandler.class)
    private String contactName;

    /// 联系电话
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = DataEncryptTypeHandler.class)
    private String contactPhone;

    /// 联系邮箱
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = DataEncryptTypeHandler.class)
    private String contactEmail;

    /// 省份编码
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String provinceCode;

    /// 城市编码
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cityCode;

    /// 详细地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = DataEncryptTypeHandler.class)
    private String address;

    /// 备注
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String remark;

    @Override
    public MchBaseProfileResult toResult() {
        return MchBaseProfileConvert.CONVERT.toResult(this);
    }
}
