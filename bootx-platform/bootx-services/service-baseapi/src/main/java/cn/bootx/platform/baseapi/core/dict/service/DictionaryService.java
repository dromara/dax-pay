package cn.bootx.platform.baseapi.core.dict.service;

import cn.bootx.platform.baseapi.dto.dict.DictionaryDto;
import cn.bootx.platform.baseapi.exception.dict.DictAlreadyExistedException;
import cn.bootx.platform.baseapi.exception.dict.DictItemAlreadyUsedException;
import cn.bootx.platform.baseapi.exception.dict.DictNotExistedException;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.baseapi.core.dict.dao.DictionaryItemManager;
import cn.bootx.platform.baseapi.core.dict.dao.DictionaryManager;
import cn.bootx.platform.baseapi.core.dict.entity.Dictionary;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xxm
 * @since 2020/4/10 15:52
 */
@Service
@AllArgsConstructor
public class DictionaryService {

    private final DictionaryManager dictionaryManager;

    private final DictionaryItemManager dictionaryItemManager;

    /**
     * 添加字典
     */
    @Transactional(rollbackFor = Exception.class)
    public DictionaryDto add(DictionaryParam param) {
        if (dictionaryManager.existsByCode(param.getCode())) {
            throw new DictAlreadyExistedException();
        }
        Dictionary dictionary = Dictionary.init(param);
        return dictionaryManager.save(dictionary).toDto();
    }

    /**
     * 删除字典
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        if (!dictionaryManager.existedById(id)) {
            throw new DictNotExistedException();
        }
        if (dictionaryItemManager.existsByDictId(id)) {
            throw new DictItemAlreadyUsedException();
        }
        dictionaryManager.deleteById(id);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public DictionaryDto update(DictionaryParam param) {

        Dictionary dictionary = dictionaryManager.findById(param.getId()).orElseThrow(DictNotExistedException::new);

        // 判断字典是否重名
        if (dictionaryManager.existsByCode(param.getCode(), param.getId())) {
            throw new DictAlreadyExistedException();
        }
        // 更新字典项
        BeanUtil.copyProperties(param, dictionary, CopyOptions.create().ignoreNullValue());
        dictionaryItemManager.updateDictCode(dictionary.getId(), dictionary.getCode());
        return dictionaryManager.updateById(dictionary).toDto();
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByCode(String code) {
        return dictionaryManager.existsByCode(code);
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByCode(String code, Long id) {
        return dictionaryManager.existsByCode(code, id);
    }

    /**
     * 查询指定字典
     */
    public DictionaryDto findById(Long id) {
        return dictionaryManager.findById(id).map(Dictionary::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 查询所有字典
     */
    public List<DictionaryDto> findAll() {
        List<Dictionary> dictionaries = dictionaryManager.findAll();
        return dictionaries.stream().map(Dictionary::toDto).collect(Collectors.toList());
    }

    /**
     * 查询所有字典
     */
    public PageResult<DictionaryDto> page(PageParam pageParam, DictionaryParam param) {
        return MpUtil.convert2DtoPageResult(dictionaryManager.page(pageParam, param));
    }

}
