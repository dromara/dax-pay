package cn.bootx.platform.daxpay.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.dto.order.refund.PayRefundOrderDto;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundOrderService {

    private final PayRefundOrderManager refundRecordManager;

    /**
     * 分页查询
     */
    public PageResult<PayRefundOrderDto> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> page = refundRecordManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PayRefundOrderDto findById(Long id) {
        return refundRecordManager.findById(id).map(PayRefundOrder::toDto).orElseThrow(DataNotExistException::new);
    }

}
