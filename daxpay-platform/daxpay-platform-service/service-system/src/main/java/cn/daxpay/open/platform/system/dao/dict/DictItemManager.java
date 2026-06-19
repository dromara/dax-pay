package cn.daxpay.open.platform.system.dao.dict;

import cn.daxpay.open.platform.system.entity.dict.DictItem;
import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 字典项
///
@Repository
@AllArgsConstructor
public class DictItemManager extends BaseManager<DictItemMapper, DictItem> {

    public boolean existsByDictId(Long dictId) {
        return existedByField(DictItem::getDictId, dictId);
    }

    public boolean existsByCode(String code, Long dictId) {
        return lambdaQuery().eq(DictItem::getCode, code).eq(DictItem::getDictId, dictId).exists();
    }

    public boolean existsByCode(String code, Long dictId, Long itemId) {
        return lambdaQuery().eq(DictItem::getCode, code)
            .eq(DictItem::getDictId, dictId)
            .ne(MpIdEntity::getId, itemId)
            .exists();
    }

    /// 查询指定字典下的所有内容
    public List<DictItem> findByDictId(Long dictId) {
        return findAllByField(DictItem::getDictId, dictId);
    }

    /// 查询指定字典下的所有内容
    public List<DictItem> findByDictCodeAndEnable(String dictCode, boolean enable) {
        return lambdaQuery().eq(DictItem::getDictCode, dictCode).eq(DictItem::getEnable, enable).list();
    }

    /// 分页查询,根据字典Id
    public Page<DictItem> findAllByDictionaryId(Long dictId, PageParam pageParam) {
        Page<DictItem> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().eq(DictItem::getDictId, dictId)
            .orderByAsc(DictItem::getSortNo)
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    public void updateDictCode(Long dictId, String dictCode) {
        lambdaUpdate()
                .set(DictItem::getDictCode, dictCode)
                .eq(DictItem::getDictId, dictId)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    public List<DictItem> findAllByEnable(boolean enable) {
        return lambdaQuery().eq(DictItem::getEnable, enable).list();
    }

    public Optional<DictItem> findByCodeAndEnable(String dictCode, String code, boolean enable) {
        return lambdaQuery().eq(DictItem::getDictCode, dictCode)
            .eq(DictItem::getCode, code)
            .eq(DictItem::getEnable, enable)
            .oneOpt();
    }

}
