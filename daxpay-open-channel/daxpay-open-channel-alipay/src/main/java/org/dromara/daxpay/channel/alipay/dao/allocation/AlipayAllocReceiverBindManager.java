package org.dromara.daxpay.channel.alipay.dao.allocation;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.channel.alipay.entity.allocation.AlipayAllocReceiverBind;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2025/1/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocReceiverBindManager extends BaseManager<AlipayAllocReceiverBindMapper, AlipayAllocReceiverBind> {


    /**
     * 分页
     */
    public Page<AlipayAllocReceiverBind> page(PageParam pageParam, AlipayAllocReceiverBindQuery query) {
        var mpPage = MpUtil.getMpPage(pageParam,AlipayAllocReceiverBind.class);
        QueryWrapper<AlipayAllocReceiverBind> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

}
