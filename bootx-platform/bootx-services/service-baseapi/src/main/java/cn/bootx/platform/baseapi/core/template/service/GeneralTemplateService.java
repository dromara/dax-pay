package cn.bootx.platform.baseapi.core.template.service;

import cn.bootx.platform.baseapi.core.template.dao.GeneralTemplateManager;
import cn.bootx.platform.baseapi.core.template.entity.GeneralTemplate;
import cn.bootx.platform.baseapi.dto.template.GeneralTemplateDto;
import cn.bootx.platform.baseapi.param.template.GeneralTemplateParam;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralTemplateService {
    private final GeneralTemplateManager generalTemplateManager;

    /**
     * 添加
     */
    public void add(GeneralTemplateParam param){
        if (generalTemplateManager.existsByCode(param.getCode())) {
            throw new BizException("code重复");
        }
        GeneralTemplate generalTemplate = GeneralTemplate.init(param);
        generalTemplateManager.save(generalTemplate);
    }

    /**
     * 修改
     */
    public void update(GeneralTemplateParam param){
        if (generalTemplateManager.existsByCode(param.getCode(),param.getId())) {
            throw new BizException("code重复");
        }
        GeneralTemplate generalTemplate = generalTemplateManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,generalTemplate, CopyOptions.create().ignoreNullValue());
        generalTemplateManager.updateById(generalTemplate);
    }

    /**
     * 分页
     */
    public PageResult<GeneralTemplateDto> page(PageParam pageParam,GeneralTemplateParam query){
        return MpUtil.convert2DtoPageResult(generalTemplateManager.page(pageParam,query));
    }

    /**
     * 获取单条
     */
    public GeneralTemplateDto findById(Long id){
        return generalTemplateManager.findById(id).map(GeneralTemplate::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据编码获取
     */
    public GeneralTemplateDto findByCode(String code){
        return generalTemplateManager.findByCode(code).map(GeneralTemplate::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<GeneralTemplateDto> findAll(){
        return ResultConvertUtil.dtoListConvert(generalTemplateManager.findAll());
    }


    /**
     * code是否存在
     */
    public boolean existsByCode(String code) {
        return generalTemplateManager.existsByCode(code);
    }

    /**
     * code是否存在
     */
    public boolean existsByCode(String code, Long id) {
        return generalTemplateManager.existsByCode(code, id);
    }

    /**
     * 删除
     */
    public void delete(Long id){
        generalTemplateManager.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Long> ids){
        generalTemplateManager.deleteByIds(ids);
    }

}
