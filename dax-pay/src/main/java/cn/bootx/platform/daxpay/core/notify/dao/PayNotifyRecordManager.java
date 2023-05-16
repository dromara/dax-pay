package cn.bootx.platform.daxpay.core.notify.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.notify.entity.PayNotifyRecord;
import cn.bootx.platform.daxpay.dto.notify.PayNotifyRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 支付消息通知回调
 *
 * @author xxm
 * @date 2021/6/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayNotifyRecordManager extends BaseManager<PayNotifyRecordMapper, PayNotifyRecord> {

    public Page<PayNotifyRecord> page(PageParam pageParam, PayNotifyRecordDto param) {
        Page<PayNotifyRecord> mpPage = MpUtil.getMpPage(pageParam, PayNotifyRecord.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(param.getPaymentId()), PayNotifyRecord::getPaymentId, param.getPaymentId())
            .eq(Objects.nonNull(param.getPayChannel()), PayNotifyRecord::getPayChannel, param.getPayChannel())
            .eq(Objects.nonNull(param.getStatus()), PayNotifyRecord::getStatus, param.getStatus())
            .page(mpPage);
    }

}
