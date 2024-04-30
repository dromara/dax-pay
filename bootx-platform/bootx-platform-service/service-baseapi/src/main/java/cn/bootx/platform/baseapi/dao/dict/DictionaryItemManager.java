package cn.bootx.platform.baseapi.dao.dict;

import cn.bootx.platform.baseapi.entity.dict.DictionaryItem;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 字典项
 *
 * @author xxm
 * @since 2020/4/21 14:08
 */
@Repository
@AllArgsConstructor
public class DictionaryItemManager extends BaseManager<DictionaryItemMapper, DictionaryItem> {

    public boolean existsByDictId(Long dictId) {
        return existedByField(DictionaryItem::getDictId, dictId);
    }

    public boolean existsByCode(String code, Long dictId) {
        return lambdaQuery().eq(DictionaryItem::getCode, code).eq(DictionaryItem::getDictId, dictId).exists();
    }

    public boolean existsByCode(String code, Long dictId, Long itemId) {
        return lambdaQuery().eq(DictionaryItem::getCode, code)
            .eq(DictionaryItem::getDictId, dictId)
            .ne(MpIdEntity::getId, itemId)
            .exists();
    }

    /**
     * 查询指定字典下的所有内容
     */
    public List<DictionaryItem> findByDictId(Long dictId) {
        return findAllByField(DictionaryItem::getDictId, dictId);
    }

    /**
     * 查询指定字典下的所有内容
     */
    public List<DictionaryItem> findByDictCodeAndEnable(String dictCode, boolean enable) {
        return lambdaQuery().eq(DictionaryItem::getDictCode, dictCode).eq(DictionaryItem::getEnable, enable).list();
    }

    /**
     * 分页查询,根据字典Id
     */
    public Page<DictionaryItem> findAllByDictionaryId(Long dictId, PageParam pageParam) {
        Page<DictionaryItem> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().eq(DictionaryItem::getDictId, dictId)
            .orderByAsc(DictionaryItem::getSortNo)
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    public void updateDictCode(Long dictId, String dictCode) {
        lambdaUpdate().set(DictionaryItem::getDictCode, dictCode).eq(DictionaryItem::getDictId, dictId).update();
    }

    public List<DictionaryItem> findAllByEnable(boolean enable) {
        return lambdaQuery().eq(DictionaryItem::getEnable, enable).list();
    }

    public Optional<DictionaryItem> findByCodeAndEnable(String dictCode, String code, boolean enable) {
        return lambdaQuery().eq(DictionaryItem::getDictCode, dictCode)
            .eq(DictionaryItem::getCode, code)
            .eq(DictionaryItem::getEnable, enable)
            .oneOpt();
    }

}
