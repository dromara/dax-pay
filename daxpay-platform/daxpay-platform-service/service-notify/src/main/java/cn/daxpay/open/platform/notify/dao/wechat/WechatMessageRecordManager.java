package cn.daxpay.open.platform.notify.dao.wechat;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.notify.entity.wechat.WechatMessageRecord;
import cn.daxpay.open.platform.notify.param.wechat.WechatMessageQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/// # 微信消息记录 Manager
///
@Repository
@AllArgsConstructor
public class WechatMessageRecordManager extends BaseManager<WechatMessageRecordMapper, WechatMessageRecord> {

    /// 管理端分页查询(按 openId/消息类型/状态/发送时间范围过滤)
    public Page<WechatMessageRecord> page(PageParam pageParam, WechatMessageQuery query) {
        Page<WechatMessageRecord> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getOpenId()), WechatMessageRecord::getOpenId, query.getOpenId())
                .eq(StrUtil.isNotBlank(query.getMessageType()), WechatMessageRecord::getMessageType, query.getMessageType())
                .eq(StrUtil.isNotBlank(query.getStatus()), WechatMessageRecord::getStatus, query.getStatus())
                .ge(query.getStartTime() != null, WechatMessageRecord::getSendTime, query.getStartTime())
                .le(query.getEndTime() != null, WechatMessageRecord::getSendTime, query.getEndTime())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
