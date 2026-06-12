package org.dromara.daxpay.platform.system.dao.dict;

import org.dromara.daxpay.platform.system.entity.dict.Dict;
import org.dromara.daxpay.platform.system.param.dict.DictParam;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpIdEntity;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 字典
///
@Repository
@AllArgsConstructor
public class DictManager extends BaseManager<DictMapper, Dict> {

    /// 根据code查询重复
    public boolean existsByCode(String code) {
        return existedByField(Dict::getCode, code);
    }

    /// 根据code查询重复 排除id
    public boolean existsByCode(String code, Long id) {
        return existedByField(Dict::getCode, code, id);
    }

    public Page<Dict> page(PageParam pageParam, DictParam param) {
        Page<Dict> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), Dict::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), Dict::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getDictType()), Dict::getDictType, param.getDictType())
            .page(mpPage);
    }

    public List<Dict> findAllByEnable(boolean enable) {
        return lambdaQuery().eq(Dict::getEnable, enable).list();
    }

}
