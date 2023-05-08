package cn.bootx.daxpay.core.refund.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.daxpay.core.refund.entity.RefundRecord;
import cn.bootx.daxpay.dto.refund.RefundRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author xxm
 * @date 2022/3/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundRecordManager extends BaseManager<RefundRecordMapper, RefundRecord> {

    public Page<RefundRecord> page(PageParam pageParam, RefundRecordDto param) {
        Page<RefundRecord> mpPage = MpUtil.getMpPage(pageParam, RefundRecord.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(param.getPaymentId()), RefundRecord::getPaymentId, param.getPaymentId())
            .like(Objects.nonNull(param.getBusinessId()), RefundRecord::getBusinessId, param.getBusinessId())
            .like(Objects.nonNull(param.getTitle()), RefundRecord::getTitle, param.getTitle())
            .page(mpPage);
    }

}
