package cn.daxpay.open.platform.capability.sensitiveword.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordStatusEnum;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 敏感词 Manager
///
@Repository
public class SystemSensitiveWordManager extends BaseManager<SystemSensitiveWordMapper, SystemSensitiveWord> {

    /// 分页
    public Page<SystemSensitiveWord> page(PageParam pageParam, SystemSensitiveWordQuery query) {
        Page<SystemSensitiveWord> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<SystemSensitiveWord> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 词面是否已存在
    public boolean existsByWord(String word, Long excludeId) {
        if (StrUtil.isBlank(word)) {
            return false;
        }
        return lambdaQuery()
                .eq(SystemSensitiveWord::getWord, word.trim())
                .ne(excludeId != null, SystemSensitiveWord::getId, excludeId)
                .exists();
    }

    /// 全部启用词
    public List<SystemSensitiveWord> listEnabled() {
        return lambdaQuery()
                .eq(SystemSensitiveWord::getStatus, SensitiveWordStatusEnum.ENABLE.getCode())
                .list();
    }
}

