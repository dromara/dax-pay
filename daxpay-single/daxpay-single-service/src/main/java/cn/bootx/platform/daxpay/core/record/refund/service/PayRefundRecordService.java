package cn.bootx.platform.daxpay.core.record.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.record.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.core.record.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.dto.order.refund.PayRefundOrderDto;
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
public class PayRefundRecordService {

    private final PayRefundOrderManager refundRecordManager;

    /**
     * 分页查询
     */
    public PageResult<PayRefundOrderDto> page(PageParam pageParam, PayRefundOrderDto param) {
        Page<PayRefundOrder> page = refundRecordManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PayRefundOrderDto findById(Long id) {
        return refundRecordManager.findById(id).map(PayRefundOrder::toDto).orElseThrow(DataNotExistException::new);
    }

}
