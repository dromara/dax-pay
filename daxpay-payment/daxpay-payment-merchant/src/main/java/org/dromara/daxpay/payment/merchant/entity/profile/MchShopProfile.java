package org.dromara.daxpay.payment.merchant.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.type.StringListTypeHandler;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.profile.MchShopProfileConvert;
import org.dromara.daxpay.payment.merchant.result.profile.MchShopProfileResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户经营场所信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_shop_profile",autoResultMap = true)
public class MchShopProfile extends MchBaseEntity implements ToResult<MchShopProfileResult> {

    /// 门店类型 普通店/总店/分店
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String type;

    /// 结算类型 独立结算/合并到总店
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String settleType;

    /// 经营场所名称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String name;

    /// 省市区编码
    @TableField(typeHandler = StringListTypeHandler.class, updateStrategy = FieldStrategy.ALWAYS)
    private List<String> regionCode;

    /// 经营场所详细地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String address;

    /// 门头照
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String doorPic;

    /// 室内照
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String insidePic;

    /// 收银台照片
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cashierPic;

    /// 转换为结果对象
    @Override
    public MchShopProfileResult toResult() {
        return MchShopProfileConvert.CONVERT.toResult(this);
    }
}
