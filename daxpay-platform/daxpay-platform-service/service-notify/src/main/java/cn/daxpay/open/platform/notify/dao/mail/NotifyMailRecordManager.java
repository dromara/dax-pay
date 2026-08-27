package cn.daxpay.open.platform.notify.dao.mail;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.notify.entity.mail.NotifyMailRecord;
import cn.daxpay.open.platform.notify.param.mail.NotifyMailRecordQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/// 邮件发送记录
@Repository
@AllArgsConstructor
public class NotifyMailRecordManager extends BaseManager<NotifyMailRecordMapper, NotifyMailRecord> {

    /// 管理端分页查询
    public Page<NotifyMailRecord> page(PageParam pageParam, NotifyMailRecordQuery query) {
        Page<NotifyMailRecord> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery()
            .like(StrUtil.isNotBlank(query.getReceiverEmail()), NotifyMailRecord::getReceiverEmail, query.getReceiverEmail())
            .eq(StrUtil.isNotBlank(query.getStatus()), NotifyMailRecord::getStatus, query.getStatus())
            .eq(StrUtil.isNotBlank(query.getBusinessType()), NotifyMailRecord::getBusinessType, query.getBusinessType())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }
}
