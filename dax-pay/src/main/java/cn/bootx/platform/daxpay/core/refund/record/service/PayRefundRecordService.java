package cn.bootx.platform.daxpay.core.refund.record.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.refund.record.dao.PayRefundRecordManager;
import cn.bootx.platform.daxpay.core.refund.record.entity.PayRefundRecord;
import cn.bootx.platform.daxpay.dto.refund.PayRefundRecordDto;
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

    private final PayRefundRecordManager refundRecordManager;

    /**
     * 分页查询
     */
    public PageResult<PayRefundRecordDto> page(PageParam pageParam, PayRefundRecordDto param) {
        Page<PayRefundRecord> page = refundRecordManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PayRefundRecordDto findById(Long id) {
        return refundRecordManager.findById(id).map(PayRefundRecord::toDto).orElseThrow(DataNotExistException::new);
    }

}
