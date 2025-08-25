package org.dromara.daxpay.service.pay.dao.notice.notify;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.pay.entity.notice.notify.MerchantNotifyTask;
import org.dromara.daxpay.service.pay.param.notice.notify.MerchantNotifyTaskQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantNotifyTaskManager extends BaseManager<MerchantNotifyTaskMapper, MerchantNotifyTask> {

    /**
     * 分页
     */
    public Page<MerchantNotifyTask> page(PageParam param, MerchantNotifyTaskQuery query){
        var mpPage = MpUtil.getMpPage(param, MerchantNotifyTask.class);
        QueryWrapper<MerchantNotifyTask> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /**
     * 获取数据, 不经过租户
     */
    @IgnoreTenant
    public Optional<MerchantNotifyTask> findByIdNotTenant(Long taskId) {
        return this.findById(taskId);
    }
}
