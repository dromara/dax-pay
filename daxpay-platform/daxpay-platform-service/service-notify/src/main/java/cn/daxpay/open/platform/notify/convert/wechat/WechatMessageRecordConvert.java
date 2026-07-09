package cn.daxpay.open.platform.notify.convert.wechat;

import cn.daxpay.open.platform.notify.entity.wechat.WechatMessageRecord;
import cn.daxpay.open.platform.notify.result.wechat.WechatMessageRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信消息记录转换
///
@Mapper
public interface WechatMessageRecordConvert {

    WechatMessageRecordConvert CONVERT = Mappers.getMapper(WechatMessageRecordConvert.class);

    /// 实体转结果
    WechatMessageRecordResult toResult(WechatMessageRecord entity);
}
