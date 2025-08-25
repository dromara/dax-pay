package org.dromara.daxpay.service.merchant.dao.app;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.merchant.entity.app.MchApp;
import org.dromara.daxpay.core.enums.MchAppStatusEnum;
import org.dromara.daxpay.service.merchant.param.app.MchAppQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
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
     * 根据应用AppId查询, 商户端使用会过滤租户
     */
    public Optional<MchApp> findByAppId(String appId) {
        return this.findByField(MchApp::getAppId, appId);
    }
    /**
     * 根据应用AppId查询， 忽略租户拦截
     */
    @IgnoreTenant
    public Optional<MchApp> findByAppIdNotTenant(String appId) {
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

    /**
     * 根据状态查询所有
     */
    public List<MchApp> findAllByStatus(MchAppStatusEnum status) {
        return findAllByField(MchApp::getStatus, status.getCode());

    }

    /**
     * 根据商户号查询启用的商户
     */
    public List<MchApp> findAllByMchNoAndEnable(String mchNo) {
        return lambdaQuery()
                .eq(MchApp::getMchNo, mchNo)
                .eq(MchApp::getStatus, MchAppStatusEnum.ENABLE.getCode())
                .list();
    }

    /**
     * 根据商户号查询应用
     */
    public List<MchApp> findAllByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MchApp::getMchNo, mchNo)
                .list();
    }

    /**
     * 根据商户号判断是否存在
     */
    public boolean existsByAppId(String appId) {
        return existedByField(MchApp::getAppId, appId);
    }

}
