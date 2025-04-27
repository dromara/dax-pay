package org.dromara.daxpay.channel.wechat.dao.allocation;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.channel.wechat.entity.allocation.WechatAllocReceiverBind;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2025/1/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WechatAllocReceiverBindManager extends BaseManager<WechatAllocReceiverBindMapper, WechatAllocReceiverBind> {

    /**
     * 分页
     */
    public Page<WechatAllocReceiverBind> page(PageParam pageParam, WechatAllocReceiverBindQuery param) {
        var mpPage = MpUtil.getMpPage(pageParam, WechatAllocReceiverBind.class);
        QueryWrapper<WechatAllocReceiverBind> generator = QueryGenerator.generator(param);
        return page(mpPage, generator);
    }
}
