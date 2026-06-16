package org.dromara.daxpay.payment.old.pay.dao.notice.callback;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.old.pay.entity.notice.callback.MerchantCallbackRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantCallbackRecordManager extends BaseManager<MerchantCallbackRecordMapper, MerchantCallbackRecord> {

    /// 分页
    public Page<MerchantCallbackRecord> page(PageParam param, Long taskId){
        var mpPage = MpUtil.getMpPage(param, MerchantCallbackRecord.class);
        return lambdaQuery().eq(MerchantCallbackRecord::getTaskId,taskId)
                .page(mpPage);
    }
}
