package cn.daxpay.open.platform.system.service.dict;

import cn.daxpay.open.platform.system.convert.dict.DictConvert;
import cn.daxpay.open.platform.system.dao.dict.DictItemManager;
import cn.daxpay.open.platform.system.dao.dict.DictManager;
import cn.daxpay.open.platform.system.entity.dict.Dict;
import cn.daxpay.open.platform.system.entity.dict.DictItem;
import cn.daxpay.open.platform.system.param.dict.DictItemParam;
import cn.daxpay.open.platform.system.result.dict.DictItemResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 字典项服务类
///
@Service
@AllArgsConstructor
public class DictItemService {

    private final DictItemManager dictItemManager;

    private final DictManager dictManager;

    /// 添加内容
    @Transactional(rollbackFor = Exception.class)
    public void add(DictItemParam param) {

        // 在同一个Dict不允许存在相同code的DictItem
        if (dictItemManager.existsByCode(param.getCode(), param.getDictId())) {
            // 系统: 字典项编码重复
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.itemCodeDuplicate");
        }

        Dict dict = dictManager.findById(param.getDictId())
                // 系统: 字典不存在
                .orElseThrow(() -> new DataNotExistException("error.system.dict.notExist"));
        param.setDictCode(dict.getCode());
        DictItem dictItem = DictConvert.CONVERT.convert(param);
        dictItemManager.save(dictItem);
    }

    /// 修改内容
    @Transactional(rollbackFor = Exception.class)
    public void update(DictItemParam param) {
        // 判断字典item是否存在
        DictItem dictItem = dictItemManager.findById(param.getId())
                // 系统: 字典项不存在
                .orElseThrow(() -> new DataNotExistException("error.system.dict.itemNotExist"));

        // 判断是否有重复code的Item
        if (dictItemManager.existsByCode(dictItem.getDictCode(), param.getDictId(), param.getId())) {
            // 系统: 字典项编码重复
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.itemCodeDuplicate");
        }
        DictConvert.CONVERT.copy(param, dictItem);
        dictItemManager.updateById(dictItem);
    }

    /// 删除内容
    public void delete(Long id) {
        dictItemManager.deleteById(id);
    }

    /// 根据ID查询指定内容
    public DictItemResult findById(Long id) {
        return dictItemManager.findById(id).map(DictItem::toResult)
                // 系统: 字典项不存在
                .orElseThrow(() -> new DataNotExistException("error.system.dict.itemNotExist"));
    }

    /// 根据字典编码和字典项编码查询启用的菜单项
    public Optional<DictItem> findEnableByCode(String dictCode, String code) {
        return dictItemManager.findByCodeAndEnable(dictCode, code, true);
    }

    /// 查询指定目录下的所有内容
    public List<DictItemResult> findByDictionaryId(Long dictionaryId) {
        return dictItemManager.findByDictId(dictionaryId)
                .stream()
                .sorted(Comparator.comparing(DictItem::getSortNo, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(DictItem::toResult)
                .toList();
    }

    /// 查询指定字典编码下的所有启用的字典项
    /// @return code -> nameCn 的映射
    @Cacheable(value = "system:dict", key = "#dictCode")
    public Map<String, String> findEnableByDictCode(String dictCode) {
        return dictItemManager.findByDictCodeAndEnable(dictCode, true)
                .stream()
                .collect(HashMap::new,
                        (map, item) -> map.put(item.getCode(), item.getNameCn()),
                        HashMap::putAll);

    }

    /// 查询指定字典编码下所有启用的字典项完整列表（含多语言字段）
    /// 供 DictTranslatorImpl 使用，缓存键与 findEnableByDictCode 区分以避免类型冲突
    /// @return 字典项完整实体列表
    @Cacheable(value = "system:dict", key = "#dictCode + ':list'")
    public List<DictItem> findAllEnableByDictCode(String dictCode) {
        return dictItemManager.findByDictCodeAndEnable(dictCode, true);
    }

    /// 分页查询指定目录下的内容
    public PageResult<DictItemResult> pageByDictionaryId(Long dictionaryId, PageParam pageParam) {
        Page<DictItem> dictItems = dictItemManager.findAllByDictionaryId(dictionaryId, pageParam);
        return MpUtil.toPageResult(dictItems);
    }

    /// 判断编码是否存在
    public boolean existsByCode(String code, Long dictId) {
        return dictItemManager.existsByCode(code, dictId);
    }

    /// 判断编码是否存在
    public boolean existsByCode(String code, Long dictId, Long id) {
        return dictItemManager.existsByCode(code, dictId, id);
    }

    /// 获取全部字典项
    public List<DictItemResult> findAll() {
        return dictItemManager.findAll()
                .stream()
                .sorted(Comparator.comparing(DictItem::getDictId)
                        .thenComparing(DictItem::getSortNo, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MpIdEntity::getId))
                .map(DictItem::toResult)
                .toList();
    }

    /// 获取启用的字典项列表
    public List<DictItemResult> findAllByEnable() {

        // 获取被停用的字典
        List<Long> disableDictIds = dictManager.findAllByEnable(false)
                .stream()
                .map(MpIdEntity::getId)
                .toList();

        // 过滤掉被停用的字典项
        return dictItemManager.findAllByEnable(true)
                .stream()
                .filter(o -> !disableDictIds.contains(o.getDictId()))
                .sorted(Comparator.comparing(DictItem::getDictId)
                        .thenComparing(DictItem::getSortNo, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MpIdEntity::getId))
                .map(DictItem::toResult)
                .peek(item -> item.setEnable(null).setId(null).setCreateTime(null).setDictId(null).setRemark(null))
                .toList();
    }

}

