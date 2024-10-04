package cn.bootx.platform.baseapi.service.dict;

import cn.bootx.platform.baseapi.convert.dict.DictionaryConvert;
import cn.bootx.platform.baseapi.dao.dict.DictionaryItemManager;
import cn.bootx.platform.baseapi.dao.dict.DictionaryManager;
import cn.bootx.platform.baseapi.entity.dict.Dictionary;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryResult;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = "cache:dict", allEntries = true)
    public DictionaryResult add(DictionaryParam param) {
        if (dictionaryManager.existsByCode(param.getCode())) {
            throw new BizException("字典编码已存在");
        }
        Dictionary dictionary = DictionaryConvert.CONVERT.convert(param);
        dictionaryManager.save(dictionary);
        return dictionary.toResult();
    }

    /**
     * 删除字典
     */
    @CacheEvict(value = "cache:dict", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        if (!dictionaryManager.existedById(id)) {
            throw new BizException("字典不存在");
        }
        if (dictionaryItemManager.existsByDictId(id)) {
            throw new BizException("字典下有字典项,不能删除");
        }
        dictionaryManager.deleteById(id);
    }

    /**
     * 更新
     */
    @CacheEvict(value = "cache:dict", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DictionaryResult update(DictionaryParam param) {

        Dictionary dictionary = dictionaryManager.findById(param.getId())
                .orElseThrow(()->new DataNotExistException("字典不存在"));

        // 判断字典是否重名
        if (dictionaryManager.existsByCode(param.getCode(), param.getId())) {
            throw new BizException("字典编码已存在");
        }
        // 更新字典项
        BeanUtil.copyProperties(param, dictionary, CopyOptions.create().ignoreNullValue());
        dictionaryItemManager.updateDictCode(dictionary.getId(), dictionary.getCode());
        dictionaryManager.updateById(dictionary);
        return dictionary.toResult();
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
    public DictionaryResult findById(Long id) {
        return dictionaryManager.findById(id).map(Dictionary:: toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 查询所有字典
     */
    public List<DictionaryResult> findAll() {
        List<Dictionary> dictionaries = dictionaryManager.findAll();
        return dictionaries.stream().map(Dictionary::toResult).collect(Collectors.toList());
    }

    /**
     * 查询所有字典
     */
    public PageResult<DictionaryResult> page(PageParam pageParam, DictionaryParam param) {
        return MpUtil.toPageResult(dictionaryManager.page(pageParam, param));
    }

}
