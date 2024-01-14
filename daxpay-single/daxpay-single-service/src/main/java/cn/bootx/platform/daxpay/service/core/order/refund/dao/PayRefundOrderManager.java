package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付退款订单管理
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRefundOrderManager extends BaseManager<PayRefundOrderMapper, PayRefundOrder> {

    /**
     * 分页
     */
    public Page<PayRefundOrder> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> mpPage = MpUtil.getMpPage(pageParam, PayRefundOrder.class);
        QueryWrapper<PayRefundOrder> generator = QueryGenerator.generator(query);
//        if (Objects.nonNull(query.getSortField())){
//            generator.orderBy(true, query.getAsc(), NamingCase.toUnderlineCase(query.getSortField()));
//        }
        return page(mpPage,generator);
    }

}
