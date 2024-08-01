package cn.daxpay.multi.service.dao.merchant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.param.merchant.MerchantQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

}
