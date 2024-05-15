package cn.bootx.platform.notice.core.site.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.core.site.domain.SiteMessageInfo;
import cn.bootx.platform.notice.core.site.entity.SiteMessage;
import cn.bootx.platform.notice.code.SiteMessageCode;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.notice.code.SiteMessageCode.STATE_SENT;

/**
 * 站内信
 *
 * @author xxm
 * @since 2021/8/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SiteMessageManager extends BaseManager<SiteMessageMapper, SiteMessage> {

    /**
     * 接收用户消息分页
     */
    public Page<SiteMessageInfo> pageByReceive(PageParam pageParam, SiteMessageInfo query, Long userId) {
        val mpPage = MpUtil.getMpPage(pageParam, SiteMessageInfo.class);

        val wrapper = new LambdaQueryWrapper<SiteMessageInfo>()
                .and(o -> o
                        .and(p -> p.eq(SiteMessageInfo::getReceiveType, SiteMessageCode.RECEIVE_ALL)
                                .gt(SiteMessageInfo::getEfficientTime, LocalDate.now()))
                        .or()
                        .eq(SiteMessageInfo::getReceiveId, userId))
                .eq(SiteMessageInfo::getSendState, STATE_SENT)
                .eq(StrUtil.isNotBlank(query.getTitle()), SiteMessageInfo::getTitle, query.getTitle())
                .orderByAsc(SiteMessageInfo::getHaveRead)
                .orderByDesc(SiteMessageInfo::getReadTime);
        if (Objects.equals(query.getHaveRead(), true)) {
            wrapper.eq(SiteMessageInfo::getHaveRead, query.getHaveRead());
        }
        // 已读为空也视为未读
        if (Objects.equals(query.getHaveRead(), false)) {
            wrapper.and(o -> o.eq(SiteMessageInfo::getHaveRead, false).or().isNull(SiteMessageInfo::getHaveRead));

        }

        return baseMapper.pageMassage(mpPage, wrapper);
    }

    /**
     * 查询未读的消息数量
     */
    public Integer countByReceiveNotRead(Long userId) {
        val wrapper = new LambdaQueryWrapper<SiteMessageInfo>()
                .and(o -> o
                        .and(p -> p.eq(SiteMessageInfo::getReceiveType, SiteMessageCode.RECEIVE_ALL)
                                .gt(SiteMessageInfo::getEfficientTime, LocalDate.now()))
                        .or()
                        .eq(SiteMessageInfo::getReceiveId, userId))
                .and(o -> o.eq(SiteMessageInfo::getHaveRead, false).or().isNull(SiteMessageInfo::getHaveRead))
                .eq(SiteMessageInfo::getSendState, STATE_SENT)
                .orderByAsc(SiteMessageInfo::getHaveRead)
                .orderByDesc(SiteMessageInfo::getReadTime);
        return baseMapper.countMassage(wrapper);
    }

    public List<String> listByReceiveNotRead(Long userId) {
        Page<SiteMessageInfo> page = new Page<>();
        page.setSize(5);
        page.setSize(1);
        val wrapper = new LambdaQueryWrapper<SiteMessageInfo>()
                .and(o -> o
                        .and(p -> p.eq(SiteMessageInfo::getReceiveType, SiteMessageCode.RECEIVE_ALL)
                                .gt(SiteMessageInfo::getEfficientTime, LocalDate.now()))
                        .or()
                        .eq(SiteMessageInfo::getReceiveId, userId))
                .and(o -> o.eq(SiteMessageInfo::getHaveRead, false).or().isNull(SiteMessageInfo::getHaveRead))
                .eq(SiteMessageInfo::getSendState, STATE_SENT)
                .orderByAsc(SiteMessageInfo::getHaveRead)
                .orderByDesc(SiteMessageInfo::getReadTime);
        return baseMapper.pageMassage(page, wrapper).getRecords().stream()
                .map(SiteMessageInfo::getTitle).collect(Collectors.toList());
    }

    /**
     * 发送人消息分页
     */
    public Page<SiteMessage> pageBySender(PageParam pageParam, SiteMessageInfo query, Long userId) {
        Page<SiteMessage> mpPage = MpUtil.getMpPage(pageParam, SiteMessage.class);
        return lambdaQuery().select(SiteMessage.class, MpUtil::excludeBigField)
                .eq(SiteMessage::getSenderId, userId)
                .like(StrUtil.isNotBlank(query.getTitle()), SiteMessage::getSenderId, query.getTitle())
                .eq(StrUtil.isNotBlank(query.getSendState()), SiteMessage::getSendState, query.getSendState())
                .eq(StrUtil.isNotBlank(query.getReceiveType()), SiteMessage::getReceiveType, query.getReceiveType())
                .orderByDesc(SiteMessage::getId)
                .page(mpPage);
    }

}
