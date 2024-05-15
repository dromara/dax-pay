package cn.bootx.platform.notice.core.sms.dao;

import cn.bootx.platform.notice.param.sms.SmsTemplateParam;
import cn.bootx.platform.notice.core.sms.entity.SmsTemplate;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@Repository
@RequiredArgsConstructor
public class SmsTemplateManager extends BaseManager<SmsTemplateMapper, SmsTemplate> {

    /**
    * 分页
    */
    public Page<SmsTemplate> page(PageParam pageParam, SmsTemplateParam param) {
        Page<SmsTemplate> mpPage = MpUtil.getMpPage(pageParam, SmsTemplate.class);
        QueryWrapper<SmsTemplate> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(SmsTemplate::getId));
        return this.page(mpPage,wrapper);
    }
}
