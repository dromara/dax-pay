package org.dromara.daxpay.channel.wechat.convert.isv;

import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvApp;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppParam;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 微信服务商应用转换
///
@Mapper
public interface WechatIsvAppConvert {

    WechatIsvAppConvert CONVERT = Mappers.getMapper(WechatIsvAppConvert.class);

    /// 转换为返回对象
    WechatIsvAppResult toResult(WechatIsvApp entity);

    /// 转换为实体
    WechatIsvApp toEntity(WechatIsvAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(WechatIsvAppParam param, @MappingTarget WechatIsvApp entity);
}
