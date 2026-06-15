package org.dromara.daxpay.channel.wechat.convert.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectApp;
import org.dromara.daxpay.channel.wechat.param.direct.WechatDirectAppParam;
import org.dromara.daxpay.channel.wechat.result.direct.WechatDirectAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 微信直连商户应用转换
///
/// MapStruct转换器，负责微信直连商户应用在实体、参数和返回结果之间的相互转换。
///
@Mapper
public interface WechatDirectAppConvert {

    WechatDirectAppConvert CONVERT = Mappers.getMapper(WechatDirectAppConvert.class);

    /// 转换为返回对象
    WechatDirectAppResult toResult(WechatDirectApp entity);

    /// 转换为实体
    WechatDirectApp toEntity(WechatDirectAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(WechatDirectAppParam param, @MappingTarget WechatDirectApp entity);
}
