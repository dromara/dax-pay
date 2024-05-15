package cn.bootx.platform.notice.core.template.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.core.template.entity.MessageTemplate;
import cn.bootx.platform.notice.param.template.MessageTemplateParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 消息模板
 *
 * @author xxm
 * @since 2021/8/9
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageTemplateManager extends BaseManager<MessageTemplateMapper, MessageTemplate> {

    public Optional<MessageTemplate> findByCode(String code) {
        return findByField(MessageTemplate::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(MessageTemplate::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(MessageTemplate::getCode, code, id);
    }

    public Page<MessageTemplate> page(PageParam pageParam, MessageTemplateParam query) {
        Page<MessageTemplate> mpPage = MpUtil.getMpPage(pageParam, MessageTemplate.class);
        return lambdaQuery().like(StrUtil.isNotBlank(query.getCode()), MessageTemplate::getCode, query.getCode())
            .like(StrUtil.isNotBlank(query.getName()), MessageTemplate::getName, query.getName())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

}
