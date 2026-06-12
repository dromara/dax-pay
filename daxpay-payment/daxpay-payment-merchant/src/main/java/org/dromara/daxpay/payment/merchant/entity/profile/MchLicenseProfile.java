package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.type.StringListTypeHandler;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchLicenseProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchLicenseProfileResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/// # 商户营业执照信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_license_profile",autoResultMap = true)
public class MchLicenseProfile extends MchBaseEntity implements ToResult<MchLicenseProfileResult> {

    /// 营业执照号
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String licenseNo;

    /// 营业执照名称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String licenseName;

    /// 执照地址-省市区编码
    @TableField(typeHandler = StringListTypeHandler.class, updateStrategy = FieldStrategy.ALWAYS)
    private List<String> regionCode;

    /// 营业执照详细地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String address;

    /// 营业执照长期有效
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private boolean periodLong;

    /// 营业执照开始日期
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate startDate;

    /// 营业执照结束日期
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDate endDate;

    /// 营业执照照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String licensePic;

    /// 转换为结果对象
    @Override
    public MchLicenseProfileResult toResult() {
        return MchLicenseProfileConvert.CONVERT.toResult(this);
    }
}
