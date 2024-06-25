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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/5/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantManager extends BaseManager<MerchantMapper, Merchant> {

    /**
     * 分页
     */
    public Page<Merchant> page(PageParam pageParam, MerchantQuery query) {
        Page<Merchant> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<Merchant> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

}
