package cn.daxpay.multi.service.dao.merchant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.daxpay.multi.service.entity.merchant.MchApp;
import cn.daxpay.multi.service.param.merchant.MchAppQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MchAppManager extends BaseManager<MchAppMapper, MchApp> {

    /**
     * 根据id进行更新
     */
    @Override
    @CacheEvict(value = "cache:mchApp", key = "#mchApp.appId")
    public int updateById(MchApp mchApp) {
        return super.updateById(mchApp);
    }

    /**
     * 批量更新
     */
    @Override
    @CacheEvict(value = "cache:mchApp", allEntries = true)
    public boolean updateAllById(Collection<MchApp> entityList) {
        return super.updateAllById(entityList);
    }

    /**
     * 根据应用AppId查询
     */
    public Optional<MchApp> findByAppId(String appId) {
        return this.findByField(MchApp::getAppId, appId);
    }

    /**
     * 商户是否存在APP
     */
    public boolean existByMchNo(String mchNo) {
        return existedByField(MchApp::getMchNo, mchNo);
    }

    /**
     * 分页
     */
    public Page<MchApp> page(PageParam pageParam, MchAppQuery query) {
        Page<MchApp> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MchApp> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
