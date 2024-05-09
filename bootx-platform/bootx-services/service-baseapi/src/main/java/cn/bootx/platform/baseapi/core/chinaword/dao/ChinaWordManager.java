package cn.bootx.platform.baseapi.core.chinaword.dao;

import cn.bootx.platform.baseapi.core.chinaword.entity.ChinaWord;
import cn.bootx.platform.baseapi.param.chinaword.ChinaWordParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 敏感词
 * @author xxm
 * @since 2023-08-09
 */
@Repository
@RequiredArgsConstructor
public class ChinaWordManager extends BaseManager<ChinaWordMapper, ChinaWord> {

    public List<ChinaWord> findAllByEnable(boolean enable){
        return findAllByField(ChinaWord::getEnable,enable);
    }

    /**
     * 分页
     */
    public Page<ChinaWord> page(PageParam pageParam, ChinaWordParam param) {
        Page<ChinaWord> mpPage = MpUtil.getMpPage(pageParam, ChinaWord.class);
        QueryWrapper<ChinaWord> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(ChinaWord::getId));
        return this.page(mpPage,wrapper);
    }
}
