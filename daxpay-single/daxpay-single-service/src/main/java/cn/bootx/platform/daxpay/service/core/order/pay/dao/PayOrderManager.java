package cn.bootx.platform.daxpay.service.core.order.pay.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.param.order.PayOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 支付订单
 * 注意: 增删改需要使用 PayOrderQueryService 服务类, 不可以直接使用此dao, 因为订单超时任务需要处理
 *
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return findByField(PayOrder::getOrderNo,orderNo);
    }

    /**
     * 根据商户订单号查询
     */
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo) {
        return findByField(PayOrder::getBizOrderNo,bizOrderNo);
    }

    /**
     * 分页
     */
    public Page<PayOrder> page(PageParam pageParam, PayOrderQuery query){
        Page<PayOrder> mpPage = MpUtil.getMpPage(pageParam, PayOrder.class);
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

    /**
     * 查询对账用订单记录(指定时间和状态的订单)
     */
    public List<PayOrder> findReconcile(LocalDateTime startTime, LocalDateTime endTime, PayStatusEnum...statusEnum) {
        List<String> status = Arrays.stream(statusEnum)
                .map(PayStatusEnum::getCode)
                .collect(Collectors.toList());
        return this.lambdaQuery()
                .between(PayOrder::getPayTime, startTime, endTime)
                .in(PayOrder::getStatus, status)
                .list();
    }

}
