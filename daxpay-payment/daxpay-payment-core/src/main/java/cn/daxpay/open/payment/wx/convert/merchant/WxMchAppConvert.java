package cn.daxpay.open.payment.wx.convert.merchant;

import cn.daxpay.open.payment.wx.entity.merchant.WxMchApp;
import cn.daxpay.open.payment.wx.param.merchant.WxMchAppParam;
import cn.daxpay.open.payment.wx.result.merchant.WxMchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 商户微信应用转换
///
@Mapper
public interface WxMchAppConvert {

    WxMchAppConvert CONVERT = Mappers.getMapper(WxMchAppConvert.class);

    /// 转换为返回对象
    WxMchAppResult toResult(WxMchApp entity);

    /// 转换为实体
    WxMchApp toEntity(WxMchAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(WxMchAppParam param, @MappingTarget WxMchApp entity);
}
