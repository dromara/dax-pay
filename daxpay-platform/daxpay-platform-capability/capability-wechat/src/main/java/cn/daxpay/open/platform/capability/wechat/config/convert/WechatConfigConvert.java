package cn.daxpay.open.platform.capability.wechat.config.convert;

import cn.daxpay.open.platform.capability.wechat.config.entity.WechatConfig;
import cn.daxpay.open.platform.capability.wechat.config.param.WechatConfigParam;
import cn.daxpay.open.platform.capability.wechat.config.result.WechatConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 微信配置转换
///
@Mapper
public interface WechatConfigConvert {
    WechatConfigConvert CONVERT = Mappers.getMapper(WechatConfigConvert.class);

    WechatConfigResult toResult(WechatConfig entity);

    void copy(WechatConfigParam param, @MappingTarget WechatConfig entity);
}
