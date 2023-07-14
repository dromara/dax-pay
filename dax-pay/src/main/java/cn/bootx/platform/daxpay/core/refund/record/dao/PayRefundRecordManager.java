package cn.bootx.platform.daxpay.core.refund.record.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.refund.record.entity.PayRefundRecord;
import cn.bootx.platform.daxpay.dto.refund.PayRefundRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRefundRecordManager extends BaseManager<PayRefundRecordMapper, PayRefundRecord> {

    public Page<PayRefundRecord> page(PageParam pageParam, PayRefundRecordDto param) {
        Page<PayRefundRecord> mpPage = MpUtil.getMpPage(pageParam, PayRefundRecord.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(param.getPaymentId()), PayRefundRecord::getPaymentId, param.getPaymentId())
            .like(Objects.nonNull(param.getBusinessId()), PayRefundRecord::getBusinessId, param.getBusinessId())
            .like(Objects.nonNull(param.getTitle()), PayRefundRecord::getTitle, param.getTitle())
            .page(mpPage);
    }

}
