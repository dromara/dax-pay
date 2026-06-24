package cn.daxpay.open.platform.notify.service.notice;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.notify.convert.notice.NotifyNoticeConvert;
import cn.daxpay.open.platform.notify.dao.notice.NotifyNoticeManager;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import cn.daxpay.open.platform.notify.enums.NotifySeverityEnum;
import cn.daxpay.open.platform.notify.enums.NotifyStatusEnum;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeParam;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeQuery;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// 公告管理端服务(发布/编辑/下线/分页/详情)
@Service
@AllArgsConstructor
public class NotifyNoticeService {

    private final NotifyNoticeManager noticeManager;

    private final NotifySseService sseService;

    /// 新建公告(默认草稿状态, 通过发布接口上线)
    @Transactional(rollbackFor = Exception.class)
    public NotifyNoticeResult add(NotifyNoticeParam param) {
        NotifyNotice notice = NotifyNoticeConvert.CONVERT.convert(param);
        // 重要程度默认普通
        if (notice.getSeverity() == null) {
            notice.setSeverity(NotifySeverityEnum.normal.getCode());
        }
        // 置顶默认否
        if (notice.getIsTop() == null) {
            notice.setIsTop(false);
        }
        // 新建默认草稿
        notice.setStatus(NotifyStatusEnum.draft.getCode());
        noticeManager.save(notice);
        return notice.toResult();
    }

    /// 编辑公告(允许编辑已发布, 状态与已读记录不变)
    @Transactional(rollbackFor = Exception.class)
    public NotifyNoticeResult update(NotifyNoticeParam param) {
        NotifyNotice notice = noticeManager.findById(param.getId())
            .orElseThrow(() -> new DataNotExistException("error.notify.notice.notExist"));
        NotifyNoticeConvert.CONVERT.copy(param, notice);
        noticeManager.updateById(notice);
        return notice.toResult();
    }

    /// 删除公告(逻辑删除)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!noticeManager.existedById(id)) {
            throw new DataNotExistException("error.notify.notice.notExist");
        }
        noticeManager.deleteById(id);
    }

    /// 发布公告(草稿 -> 发布)
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        NotifyNotice notice = noticeManager.findById(id)
            .orElseThrow(() -> new DataNotExistException("error.notify.notice.notExist"));
        notice.setStatus(NotifyStatusEnum.published.getCode());
        noticeManager.updateById(notice);
        // 实时推送给所有在线用户新公告
        sseService.publishToAll(java.util.Map.of(
            "type", "notice",
            "event", "published",
            "id", notice.getId(),
            "title", notice.getTitle() == null ? "" : notice.getTitle()
        ));
    }

    /// 下线公告(发布 -> 下线, 已读记录保留)
    @Transactional(rollbackFor = Exception.class)
    public void offline(Long id) {
        NotifyNotice notice = noticeManager.findById(id)
            .orElseThrow(() -> new DataNotExistException("error.notify.notice.notExist"));
        notice.setStatus(NotifyStatusEnum.offline.getCode());
        noticeManager.updateById(notice);
    }

    /// 查询公告详情
    public NotifyNoticeResult findById(Long id) {
        return noticeManager.findById(id)
            .map(NotifyNotice::toResult)
            .orElseThrow(DataNotExistException::new);
    }

    /// 分页查询
    public PageResult<NotifyNoticeResult> page(PageParam pageParam, NotifyNoticeQuery query) {
        return MpUtil.toPageResult(noticeManager.page(pageParam, query));
    }
}
