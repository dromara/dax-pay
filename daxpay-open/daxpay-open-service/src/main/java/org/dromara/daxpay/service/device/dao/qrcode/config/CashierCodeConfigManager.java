package org.dromara.daxpay.service.device.dao.qrcode.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.device.entity.qrcode.config.CashierCodeConfig;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeConfigManager extends BaseManager<CashierCodeConfigMapper, CashierCodeConfig> {

    /**
     * 分页
     */
    public Page<CashierCodeConfig> page(PageParam pageParam, CashierCodeConfigQuery query) {
        Page<CashierCodeConfig> mpPage = MpUtil.getMpPage(pageParam, CashierCodeConfig.class);
        QueryWrapper<CashierCodeConfig> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
