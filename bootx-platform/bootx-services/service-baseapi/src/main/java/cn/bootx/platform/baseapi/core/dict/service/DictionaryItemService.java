package cn.bootx.platform.baseapi.core.dict.service;

import cn.bootx.platform.baseapi.dto.dict.DictionaryItemDto;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemSimpleDto;
import cn.bootx.platform.baseapi.exception.dict.DictItemAlreadyExistedException;
import cn.bootx.platform.baseapi.exception.dict.DictItemNotExistedException;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.baseapi.core.dict.dao.DictionaryItemManager;
import cn.bootx.platform.baseapi.core.dict.dao.DictionaryManager;
import cn.bootx.platform.baseapi.core.dict.entity.Dictionary;
import cn.bootx.platform.baseapi.core.dict.entity.DictionaryItem;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
    public DictionaryItemDto add(DictionaryItemParam param) {

        // 在同一个Dictionary不允许存在相同code的DictionaryItem
        if (dictionaryItemManager.existsByCode(param.getCode(), param.getDictId())) {
            throw new DictItemAlreadyExistedException();
        }

        Dictionary dictionary = dictionaryManager.findById(param.getDictId())
            .orElseThrow(() -> new BizException("字典不存在"));
        param.setDictCode(dictionary.getCode());
        DictionaryItem dictionaryItem = DictionaryItem.init(param);
        dictionaryItem = dictionaryItemManager.save(dictionaryItem);
        return dictionaryItem.toDto();
    }

    /**
     * 修改内容
     */
    @Transactional(rollbackFor = Exception.class)
    public DictionaryItemDto update(DictionaryItemParam param) {

        // 判断字典item是否存在
        DictionaryItem dictionaryItem = dictionaryItemManager.findById(param.getId())
            .orElseThrow(DictItemNotExistedException::new);

        // 判断是否有重复code的Item
        if (dictionaryItemManager.existsByCode(dictionaryItem.getDictCode(), param.getDictId(), param.getId())) {
            throw new DictItemAlreadyExistedException();
        }
        BeanUtil.copyProperties(param, dictionaryItem, CopyOptions.create().ignoreNullValue());
        return dictionaryItemManager.updateById(dictionaryItem).toDto();
    }

    /**
     * 删除内容
     */
    public void delete(Long id) {
        dictionaryItemManager.deleteById(id);
    }

    /**
     * 根据ID查询指定内容
     */
    public DictionaryItemDto findById(Long id) {
        return dictionaryItemManager.findById(id).map(DictionaryItem::toDto).orElseThrow(DataNotExistException::new);
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
    public List<DictionaryItemDto> findByDictionaryId(Long dictionaryId) {
        return dictionaryItemManager.findByDictId(dictionaryId)
            .stream()
            .sorted(Comparator.comparingDouble(DictionaryItem::getSortNo))
            .map(DictionaryItem::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 查询指定目录下的所有内容
     */
    public List<DictionaryItemDto> findEnableByDictCode(String dictCode) {
        return dictionaryItemManager.findByDictCodeAndEnable(dictCode, true)
            .stream()
            .map(DictionaryItem::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 分页查询指定目录下的内容
     */
    public PageResult<DictionaryItemDto> pageByDictionaryId(Long dictionaryId, PageParam pageParam) {
        Page<DictionaryItem> dictionaryItems = dictionaryItemManager.findAllByDictionaryId(dictionaryId, pageParam);
        return MpUtil.convert2DtoPageResult(dictionaryItems);
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
    public List<DictionaryItemSimpleDto> findAll() {
        return dictionaryItemManager.findAll()
            .stream()
            .sorted(Comparator.comparing(DictionaryItem::getDictId)
                .thenComparing(DictionaryItem::getSortNo)
                .thenComparing(MpIdEntity::getId))
            .map(DictionaryItem::toSimpleDto)
            .collect(Collectors.toList());
    }

    /**
     * 获取启用的字典项列表
     */
    public List<DictionaryItemSimpleDto> findAllByEnable() {

        // 获取被停用的字典
        List<Long> unEnableDictIds = dictionaryManager.findAllByEnable(false)
            .stream()
            .map(MpIdEntity::getId)
            .collect(Collectors.toList());

        // 过滤掉被停用的字典项
        return dictionaryItemManager.findAllByEnable(true)
            .stream()
            .filter(o -> !unEnableDictIds.contains(o.getDictId()))
            .sorted(Comparator.comparing(DictionaryItem::getDictId)
                .thenComparing(DictionaryItem::getSortNo)
                .thenComparing(MpIdEntity::getId))
            .map(DictionaryItem::toSimpleDto)
            .collect(Collectors.toList());
    }

}
