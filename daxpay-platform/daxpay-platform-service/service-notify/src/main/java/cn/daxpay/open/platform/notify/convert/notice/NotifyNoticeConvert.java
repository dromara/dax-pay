package cn.daxpay.open.platform.notify.convert.notice;

import cn.daxpay.open.platform.notify.entity.message.NotifyMessage;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeParam;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeBriefResult;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// 通知转换
@Mapper
public interface NotifyNoticeConvert {

    NotifyNoticeConvert CONVERT = Mappers.getMapper(NotifyNoticeConvert.class);

    /// 参数转实体
    NotifyNotice convert(NotifyNoticeParam in);

    /// 实体转详情结果
    NotifyNoticeResult convert(NotifyNotice in);

    /// 复制属性(更新时, 忽略空值)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(NotifyNoticeParam param, @MappingTarget NotifyNotice entity);

    /// 公告转铃铛摘要(类型固定为公告, 内容摘要来自正文, 已读状态由服务层填充)
    @Mapping(target = "type", constant = "notice")
    @Mapping(target = "message", source = "content")
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "link", ignore = true)
    NotifyNoticeBriefResult toBrief(NotifyNotice in);

    /// 个人消息转铃铛摘要(类型固定为个人消息, 内容摘要来自正文)
    @Mapping(target = "type", constant = "message")
    @Mapping(target = "message", source = "content")
    @Mapping(target = "severity", ignore = true)
    @Mapping(target = "isTop", ignore = true)
    NotifyNoticeBriefResult convert(NotifyMessage in);
}
