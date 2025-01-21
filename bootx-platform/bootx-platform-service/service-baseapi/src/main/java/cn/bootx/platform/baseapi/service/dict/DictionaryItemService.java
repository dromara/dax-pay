package cn.bootx.platform.baseapi.service.dict;

import cn.bootx.platform.baseapi.convert.dict.DictionaryConvert;
import cn.bootx.platform.baseapi.dao.dict.DictionaryItemManager;
import cn.bootx.platform.baseapi.dao.dict.DictionaryManager;
import cn.bootx.platform.baseapi.entity.dict.Dictionary;
import cn.bootx.platform.baseapi.entity.dict.DictionaryItem;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryItemResult;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author xxm
 * @since 2020/4/16 21:16
 */
@Service
@AllArgsConstructor
public class DictionaryItemService {

    private final DictionaryItemManager dictionaryItemManager;

    private final DictionaryManager dictionaryManager;

    /**
     * 添加内容
     */
    @CacheEvict(value = "cache:dict", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void add(DictionaryItemParam param) {

        // 在同一个Dictionary不允许存在相同code的DictionaryItem
        if (dictionaryItemManager.existsByCode(param.getCode(), param.getDictId())) {
            throw new BizException("字典项编码重复");
        }

        Dictionary dictionary = dictionaryManager.findById(param.getDictId())
                .orElseThrow(() -> new BizException("字典不存在"));
        param.setDictCode(dictionary.getCode());
        DictionaryItem dictionaryItem = DictionaryConvert.CONVERT.convert(param);
        dictionaryItemManager.save(dictionaryItem);
    }

    /**
     * 修改内容
     */
    @CacheEvict(value = "cache:dict", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DictionaryItemParam param) {
        // 判断字典item是否存在
        DictionaryItem dictionaryItem = dictionaryItemManager.findById(param.getId())
                .orElseThrow(() -> new BizException("字典项不存在"));

        // 判断是否有重复code的Item
        if (dictionaryItemManager.existsByCode(dictionaryItem.getDictCode(), param.getDictId(), param.getId())) {
            throw new BizException("字典项编码重复");
        }
        BeanUtil.copyProperties(param, dictionaryItem, CopyOptions.create().ignoreNullValue());
        dictionaryItemManager.updateById(dictionaryItem);
    }

    /**
     * 删除内容
     */
    @CacheEvict(value = "cache:dict", allEntries = true)
    public void delete(Long id) {
        dictionaryItemManager.deleteById(id);
    }

    /**
     * 根据ID查询指定内容
     */
    public DictionaryItemResult findById(Long id) {
        return dictionaryItemManager.findById(id).map(DictionaryItem::toResult).orElseThrow(() -> new DataNotExistException("字典项不存在"));
    }

    /**
     * 根据字典编码和字典项编码查询启用的菜单项
     */
    public Optional<DictionaryItem> findEnableByCode(String dictCode, String code) {
        return dictionaryItemManager.findByCodeAndEnable(dictCode, code, true);
    }

    /**
     * 查询指定目录下的所有内容
     */
    public List<DictionaryItemResult> findByDictionaryId(Long dictionaryId) {
        return dictionaryItemManager.findByDictId(dictionaryId)
                .stream()
                .sorted(Comparator.comparingDouble(DictionaryItem::getSortNo))
                .map(DictionaryItem::toResult)
                .toList();
    }

    /**
     * 查询指定目录下的所有内容
     */
    public List<DictionaryItemResult> findEnableByDictCode(String dictCode) {
        return dictionaryItemManager.findByDictCodeAndEnable(dictCode, true)
                .stream()
                .map(DictionaryItem::toResult)
                .toList();
    }

    /**
     * 分页查询指定目录下的内容
     */
    public PageResult<DictionaryItemResult> pageByDictionaryId(Long dictionaryId, PageParam pageParam) {
        Page<DictionaryItem> dictionaryItems = dictionaryItemManager.findAllByDictionaryId(dictionaryId, pageParam);
        return MpUtil.toPageResult(dictionaryItems);
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByCode(String code, Long dictId) {
        return dictionaryItemManager.existsByCode(code, dictId);
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByCode(String code, Long dictId, Long id) {
        return dictionaryItemManager.existsByCode(code, dictId, id);
    }

    /**
     * 获取全部字典项
     */
    public List<DictionaryItemResult> findAll() {
        return dictionaryItemManager.findAll()
                .stream()
                .sorted(Comparator.comparing(DictionaryItem::getDictId)
                        .thenComparing(DictionaryItem::getSortNo)
                        .thenComparing(MpIdEntity::getId))
                .map(DictionaryItem::toResult)
                .toList();
    }

    /**
     * 获取启用的字典项列表
     */
    @Cacheable(value = "cache:dict", key = "'all'")
    public List<DictionaryItemResult> findAllByEnable() {

        // 获取被停用的字典
        List<Long> disableDictIds = dictionaryManager.findAllByEnable(false)
                .stream()
                .map(MpIdEntity::getId)
                .toList();

        // 过滤掉被停用的字典项
        return dictionaryItemManager.findAllByEnable(true)
                .stream()
                .filter(o -> !disableDictIds.contains(o.getDictId()))
                .sorted(Comparator.comparing(DictionaryItem::getDictId)
                        .thenComparing(DictionaryItem::getSortNo)
                        .thenComparing(MpIdEntity::getId))
                .map(DictionaryItem::toResult)
                .peek(item -> item.setEnable(null).setId(null).setCreateTime(null).setDictId(null).setRemark(null))
                .toList();
    }

}
