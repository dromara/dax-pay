package cn.bootx.platform.daxpay.service.core.record.sync.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.param.record.PaySyncRecordQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 *
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PaySyncRecordManager extends BaseManager<PaySyncRecordMapper, PaySyncRecord> {

    /**
     * 分页
     */
    public Page<PaySyncRecord> page(PageParam pageParam, PaySyncRecordQuery param) {
        Page<PaySyncRecord> mpPage = MpUtil.getMpPage(pageParam, PaySyncRecord.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
                .like(Objects.nonNull(param.getPaymentId()), PaySyncRecord::getPaymentId, param.getPaymentId())
                .eq(Objects.nonNull(param.getChannel()), PaySyncRecord::getChannel, param.getChannel())
                .eq(Objects.nonNull(param.getStatus()), PaySyncRecord::getGatewayStatus, param.getStatus())
                .page(mpPage);
    }

}
