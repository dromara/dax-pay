package cn.bootx.platform.iam.core.client.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.entity.QueryParams;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.iam.core.client.entity.LonginType;
import cn.bootx.platform.iam.param.client.LoginTypeParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@Repository
@RequiredArgsConstructor
public class LoginTypeManager extends BaseManager<LoginTypeMapper, LonginType> {

    public Optional<LonginType> findByCode(String code) {
        return findByField(LonginType::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(LonginType::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(LonginType::getCode, code, id);
    }

    public Page<LonginType> page(PageParam pageParam, LoginTypeParam loginTypeParam) {
        Page<LonginType> mpPage = MpUtil.getMpPage(pageParam, LonginType.class);
        return lambdaQuery()
            .like(StrUtil.isNotBlank(loginTypeParam.getName()), LonginType::getName, loginTypeParam.getName())
            .like(StrUtil.isNotBlank(loginTypeParam.getCode()), LonginType::getCode, loginTypeParam.getCode())
            .page(mpPage);
    }

    /**
     * 超级查询
     */
    public Page<LonginType> supperPage(PageParam pageParam, QueryParams queryParams) {
        QueryWrapper<LonginType> generator = QueryGenerator.generator(queryParams);
        Page<LonginType> mpPage = MpUtil.getMpPage(pageParam, LonginType.class);
        return this.page(mpPage, generator);
    }

}
