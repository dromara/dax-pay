package org.dromara.daxpay.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.constant.MerchantNotifyConst;
import org.dromara.daxpay.service.param.constant.MerchantNotifyConstQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 商户通知类型
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantNotifyConstManager extends BaseManager<MerchantNotifyConstMapper,MerchantNotifyConst> {

    /**
     * 分页
     */
    public Page<MerchantNotifyConst> page(PageParam pageParam, MerchantNotifyConstQuery query) {
        Page<MerchantNotifyConst> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MerchantNotifyConst> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
