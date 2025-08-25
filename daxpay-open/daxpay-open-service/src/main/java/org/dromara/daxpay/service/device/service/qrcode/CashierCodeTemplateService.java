package org.dromara.daxpay.service.device.service.qrcode;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeTemplateConvert;
import org.dromara.daxpay.service.device.dao.qrcode.template.CashierCodeTemplateManager;
import org.dromara.daxpay.service.device.entity.qrcode.template.CashierCodeTemplate;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateParam;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateQuery;
import org.dromara.daxpay.service.device.result.qrcode.template.CashierCodeTemplateResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 收款码牌模板服务
 * @author xxm
 * @since 2025/7/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeTemplateService {
    private final CashierCodeTemplateManager codeTemplateManager;

    /**
     * 分页
     */
    public PageResult<CashierCodeTemplateResult> page(PageParam pageParam, CashierCodeTemplateQuery query){
        return MpUtil.toPageResult(codeTemplateManager.page(pageParam, query));
    }

    /**
     * 详情
     */
    public CashierCodeTemplateResult findById(Long id){
        return codeTemplateManager.findById(id)
                .map(CashierCodeTemplate::toResult)
                .orElseThrow(() -> new DataNotExistException("收银码牌模板不存在"));
    }

    /**
     * 生成预览图
     */
    public String generatePreviewImg(CashierCodeTemplateParam param){
        return "";
    }

    /**
     * 新增
     */
    public void create(CashierCodeTemplateParam param){
        var entity = CashierCodeTemplateConvert.CONVERT.toEntity(param);
        // 保存
        codeTemplateManager.save(entity);

    }

    /**
     * 修改
     */
    public void update(CashierCodeTemplateParam param){
        var template = codeTemplateManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收款码牌模板不存在"));
        BeanUtil.copyProperties(param, template);
        codeTemplateManager.updateById(template);
    }
}
