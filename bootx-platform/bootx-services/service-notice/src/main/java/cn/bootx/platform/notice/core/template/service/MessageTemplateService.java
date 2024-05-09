package cn.bootx.platform.notice.core.template.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.param.template.MessageTemplateParam;
import cn.bootx.platform.notice.core.template.dao.MessageTemplateManager;
import cn.bootx.platform.notice.core.template.entity.MessageTemplate;
import cn.bootx.platform.notice.dto.template.MessageTemplateDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 消息模板
 *
 * @author xxm
 * @since 2021/8/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageTemplateService {

    private final MessageTemplateManager messageTemplateManager;

    /**
     * 添加
     */
    public MessageTemplateDto add(MessageTemplateParam param) {
        MessageTemplate messageTemplate = MessageTemplate.init(param);
        // code 不重复
        if (messageTemplateManager.existsByCode(messageTemplate.getCode())) {
            throw new BizException("模板编码不可重复");
        }
        return messageTemplateManager.save(messageTemplate).toDto();

    }

    /**
     * 更新
     */
    public MessageTemplateDto update(MessageTemplateParam param) {
        // code 不重复
        if (messageTemplateManager.existsByCode(param.getCode(), param.getId())) {
            throw new BizException("模板编码不可重复");
        }
        MessageTemplate messageTemplate = messageTemplateManager.findById(param.getId())
            .orElseThrow(() -> new BizException("消息模板不存在"));
        BeanUtil.copyProperties(param, messageTemplate, CopyOptions.create().ignoreNullValue());
        return messageTemplateManager.updateById(messageTemplate).toDto();
    }

    /**
     * 分页
     */
    public PageResult<MessageTemplateDto> page(PageParam pageParam, MessageTemplateParam query) {
        return MpUtil.convert2DtoPageResult(messageTemplateManager.page(pageParam, query));
    }

    /**
     * 获取详情
     */
    public MessageTemplateDto findById(Long id) {
        return messageTemplateManager.findById(id).map(MessageTemplate::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return messageTemplateManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return messageTemplateManager.existsByCode(code, id);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        messageTemplateManager.deleteById(id);
    }

    /**
     * 渲染
     */
    public String rendering(String code, Map<String, Object> paramMap) {
        MessageTemplate messageTemplate = messageTemplateManager.findByCode(code)
            .orElseThrow(() -> new BizException("消息模板不存在"));
        String date = messageTemplate.getData();
        TemplateEngine engine = TemplateUtil.createEngine();
        Template template = engine.getTemplate(date);
        return template.render(paramMap);
    }

}
