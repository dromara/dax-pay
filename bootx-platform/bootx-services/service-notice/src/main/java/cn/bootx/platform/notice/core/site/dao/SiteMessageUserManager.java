package cn.bootx.platform.notice.core.site.dao;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.notice.core.site.entity.SiteMessageUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xxm
 * @since 2022/8/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SiteMessageUserManager extends BaseManager<SiteMessageUserMapper, SiteMessageUser> {

    /**
     * 根据消息ID查询
     */
    public Optional<SiteMessageUser> findByMessageId(Long messageId) {
        return findByField(SiteMessageUser::getMessageId, messageId);
    }

    /**
     * 阅读
     */
    public void readById(Long id) {
        lambdaUpdate().eq(MpIdEntity::getId, id)
            .set(SiteMessageUser::isHaveRead, Boolean.TRUE)
            .set(SiteMessageUser::getReadTime, LocalDateTime.now())
            .update();
    }

    /**
     * 根据消息ID删除
     */
    public void deleteByMessageId(Long messageId) {
        deleteByField(SiteMessageUser::getMessageId, messageId);

    }

}
