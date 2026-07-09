package cn.daxpay.open.platform.system.convert;

import cn.daxpay.open.platform.system.entity.config.platform.PlatformWechatNotifyConfig;
import cn.daxpay.open.platform.system.param.config.PlatformWechatNotifyConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformWechatNotifyConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 微信消息通知模板配置转换
@Mapper
public interface PlatformWechatNotifyConfigConvert {

    PlatformWechatNotifyConfigConvert CONVERT = Mappers.getMapper(PlatformWechatNotifyConfigConvert.class);

    PlatformWechatNotifyConfigResult toResult(PlatformWechatNotifyConfig data);

    void copy(PlatformWechatNotifyConfigParam param, @MappingTarget PlatformWechatNotifyConfig data);
}
