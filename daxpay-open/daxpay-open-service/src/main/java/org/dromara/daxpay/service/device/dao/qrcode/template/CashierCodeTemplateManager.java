package org.dromara.daxpay.service.device.dao.qrcode.template;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.device.entity.qrcode.template.CashierCodeTemplate;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 收银码模板
 * @author xxm
 * @since 2025/7/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeTemplateManager extends BaseManager<CashierCodeTemplateMapper, CashierCodeTemplate> {

    /**
     * 分页
     */
    public Page<CashierCodeTemplate> page(PageParam pageParam, CashierCodeTemplateQuery query) {
        Page<CashierCodeTemplate> mpPage = MpUtil.getMpPage(pageParam, CashierCodeTemplate.class);
        QueryWrapper<CashierCodeTemplate> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
