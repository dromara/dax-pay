package cn.bootx.platform.baseapi.dao.dict;

import cn.bootx.platform.baseapi.entity.dict.Dictionary;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典
 *
 * @author xxm
 * @since 2020/11/13
 */
@Repository
@AllArgsConstructor
public class DictionaryManager extends BaseManager<DictionaryMapper, Dictionary> {

    /**
     * 根据code查询重复
     */
    public boolean existsByCode(String code) {
        return existedByField(Dictionary::getCode, code);
    }

    /**
     * 根据code查询重复 排除id
     */
    public boolean existsByCode(String code, Long id) {
        return existedByField(Dictionary::getCode, code, id);
    }

    public Page<Dictionary> page(PageParam pageParam, DictionaryParam param) {
        Page<Dictionary> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), Dictionary::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), Dictionary::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getGroupTag()), Dictionary::getGroupTag, param.getGroupTag())
            .page(mpPage);
    }

    public List<Dictionary> findAllByEnable(boolean enable) {
        return lambdaQuery().eq(Dictionary::getEnable, enable).list();
    }

}
