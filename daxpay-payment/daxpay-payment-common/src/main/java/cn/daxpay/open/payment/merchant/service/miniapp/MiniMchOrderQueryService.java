package cn.daxpay.open.payment.merchant.service.miniapp;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.core.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.merchant.param.miniapp.order.MiniPayOrderQuery;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 小程序订单查询服务
///
/// 基于 core 普通支付业务单(NormalPayOrder)实现小程序端订单查询
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchOrderQueryService {
    private final NormalPayOrderManager normalPayOrderManager;

    /// 支付订单列表
    public PageResult<NormalPayOrderResult> pageByPay(MiniPayOrderQuery query){
        var queryChainWrapper = normalPayOrderManager.lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getAppId()), NormalPayOrder::getAppId, query.getAppId())
                .in(CollUtil.isNotEmpty(query.getPayStatus()), NormalPayOrder::getStatus, query.getPayStatus())
                .in(CollUtil.isNotEmpty(query.getProduct()), NormalPayOrder::getProduct, query.getProduct())
                .in(CollUtil.isNotEmpty(query.getChannel()), NormalPayOrder::getChannel, query.getChannel());
        if (ObjectUtil.isAllNotEmpty(query.getStartTime(), query.getEndTime())){
            var beginOfDay = LocalDateTimeUtil.beginOfDay(query.getStartTime());
            var endOfDay = LocalDateTimeUtil.endOfDay(query.getEndTime());
            queryChainWrapper.between(NormalPayOrder::getPayTime, beginOfDay, endOfDay);
        }
        // 业务单号模糊查询
        if (StrUtil.isNotBlank(query.getOrderNo())){
            queryChainWrapper.like(NormalPayOrder::getBizOrderNo, query.getOrderNo());
        }

        Page<NormalPayOrder> page = queryChainWrapper.page(MpUtil.getMpPage(query));
        var records = page.getRecords().stream().map(NormalPayOrderConvert.CONVERT::toResult).toList();
        return new PageResult<NormalPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 支付订单详情
    public NormalPayOrderResult findPayOrderById(Long id){
        return normalPayOrderManager.findById(id)
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.order.payOrderNotExist"));
    }

    /// 根据业务单号查询支付订单信息
    public NormalPayOrderResult findPayOrderByNo(String orderNo, String appId) {
        return normalPayOrderManager.findByBizOrderNo(orderNo, appId)
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.order.payOrderNotExist"));
    }

}
