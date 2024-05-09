package cn.bootx.platform.baseapi.core.dynamicform.service;

import cn.bootx.platform.baseapi.dto.dynamicform.DynamicFormDto;
import cn.bootx.platform.baseapi.param.dynamicform.DynamicFormParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.baseapi.core.dynamicform.dao.DynamicFormManager;
import cn.bootx.platform.baseapi.core.dynamicform.entity.DynamicForm;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicFormService {

    private final DynamicFormManager dynamicFormManager;

    /**
     * 添加
     */
    public void add(DynamicFormParam param) {
        DynamicForm dynamicForm = DynamicForm.init(param);
        dynamicFormManager.save(dynamicForm);
    }

    /**
     * 修改
     */
    public void update(DynamicFormParam param) {
        DynamicForm dynamicForm = dynamicFormManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param, dynamicForm, CopyOptions.create().ignoreNullValue());
        dynamicFormManager.updateById(dynamicForm);
    }

    /**
     * 分页
     */
    public PageResult<DynamicFormDto> page(PageParam pageParam, DynamicFormParam dynamicFormParam) {
        return MpUtil.convert2DtoPageResult(dynamicFormManager.page(pageParam, dynamicFormParam));
    }

    /**
     * 获取单条
     */
    public DynamicFormDto findById(Long id) {
        return dynamicFormManager.findById(id).map(DynamicForm::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return dynamicFormManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return dynamicFormManager.existsByCode(code, id);
    }

    /**
     * 获取全部
     */
    public List<DynamicFormDto> findAll() {
        return ResultConvertUtil.dtoListConvert(dynamicFormManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        dynamicFormManager.deleteById(id);
    }

}
