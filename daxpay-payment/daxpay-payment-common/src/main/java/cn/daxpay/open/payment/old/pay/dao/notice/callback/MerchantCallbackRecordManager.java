package cn.daxpay.open.payment.old.pay.dao.notice.callback;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.old.pay.entity.notice.callback.MerchantCallbackRecord;
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
