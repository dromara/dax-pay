package org.dromara.daxpay.service.merchant.dao.info;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.daxpay.service.merchant.param.info.MerchantQuery;
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
 * 商户信息
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantManager extends BaseManager<MerchantMapper, Merchant> {

    /**
     * 根据id进行更新
     */
    @Override
    @CacheEvict(value = "cache:merchantCache", key = "#merchant.mchNo")
    public int updateById(Merchant merchant) {
        return super.updateById(merchant);
    }


    /**
     * 批量更新
     */
    @Override
    @CacheEvict(value = "cache:merchantCache", allEntries = true)
    public boolean updateAllById(Collection<Merchant> entityList) {
        return super.updateAllById(entityList);
    }

    /**
     * 根据商户信息查询
     */
    public Optional<Merchant> findByMchNo(String mchNo) {
        return this.findByField(Merchant::getMchNo, mchNo);
    }

    /**
     * 分页
     */
    public Page<Merchant> page(PageParam pageParam, MerchantQuery query) {
        Page<Merchant> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<Merchant> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /**
     * 查询启用的商户,
     */
    public List<Merchant> findAllByEnable() {
        return this.lambdaQuery()
                .eq(Merchant::getStatus, MerchantStatusEnum.ENABLE.getCode())
                .list();
    }

    /**
     * 查询商户号是否存在
     */
    public boolean existedByMchNo(String mchNo) {
        return existedByField(Merchant::getMchNo, mchNo);
    }

}
