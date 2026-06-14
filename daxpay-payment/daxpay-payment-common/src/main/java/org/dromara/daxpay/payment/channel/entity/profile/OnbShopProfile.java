package org.dromara.daxpay.payment.channel.entity.profile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.type.StringListTypeHandler;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.convert.profile.OnbShopProfileConvert;
import org.dromara.daxpay.payment.channel.bo.profile.OnbShopProfileBo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 门店信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_onb_shop_profile", autoResultMap = true)
public class OnbShopProfile extends MchBaseEntity implements ToResult<OnbShopProfileBo> {

    /// 进件申请Id
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long applyId;

    /// 门店类型 普通店/总店/分店
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String type;

    /// 结算类型 独立结算/合并到总店
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String settleType;

    /// 经营场所名称
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String name;

    /// 门店地址-省市区编码
    @TableField(typeHandler = StringListTypeHandler.class, updateStrategy = FieldStrategy.ALWAYS)
    private List<String> regionCode;

    /// 门店详细地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String address;

    /// 门店照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String doorPic;

    /// 门店照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String doorPicUrl;

    /// 门店内景照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String insidePic;

    /// 门店内景照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String insidePicUrl;

    /// 收银台照片(媒体ID)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cashierPic;

    /// 收银台照片路径(系统存储)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cashierPicUrl;

    /// 转换
    @Override
    public OnbShopProfileBo toResult() {
        return OnbShopProfileConvert.CONVERT.toResult(this);
    }

}
