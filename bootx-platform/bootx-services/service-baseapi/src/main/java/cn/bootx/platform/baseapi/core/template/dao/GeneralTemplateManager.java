package cn.bootx.platform.baseapi.core.template.dao;

import cn.bootx.platform.baseapi.core.template.entity.GeneralTemplate;
import cn.bootx.platform.baseapi.param.template.GeneralTemplateParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@Repository
@RequiredArgsConstructor
public class GeneralTemplateManager extends BaseManager<GeneralTemplateMapper, GeneralTemplate> {

    /**
    * 分页
    */
    public Page<GeneralTemplate> page(PageParam pageParam, GeneralTemplateParam param) {
        Page<GeneralTemplate> mpPage = MpUtil.getMpPage(pageParam, GeneralTemplate.class);
        QueryWrapper<GeneralTemplate> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(GeneralTemplate::getId));
        return this.page(mpPage,wrapper);
    }

    public Optional<GeneralTemplate> findByCode(String code){
        return findByField(GeneralTemplate::getCode,code);
    }

    public boolean existsByCode(String code) {
        return existedByField(GeneralTemplate::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(GeneralTemplate::getCode, code, id);
    }
}
