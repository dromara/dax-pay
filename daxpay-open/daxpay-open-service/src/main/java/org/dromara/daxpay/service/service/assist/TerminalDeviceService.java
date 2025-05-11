package org.dromara.daxpay.service.service.assist;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.assist.TerminalDeviceManager;
import org.dromara.daxpay.service.entity.assist.TerminalDevice;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.param.termina.TerminalDeviceQuery;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalDeviceService {
    private final TerminalDeviceManager terminalManager;
    private final PaymentAssistService paymentAssistService;

    /**
     * 分页
     */
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query){
        return MpUtil.toPageResult(terminalManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public TerminalDeviceResult findById(Long id){
        return terminalManager.findById(id).map(TerminalDevice::toResult)
                .orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
    }

    /**
     * 添加
     */
    public void add(TerminalDeviceParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        String uuid = UUID.fastUUID().toString(true);
        TerminalDevice entity = TerminalDevice.init(param);
        entity.setTerminalNo(uuid)
                .setAppId(mchApp.getAppId())
                .setMchNo(mchApp.getMchNo());
        terminalManager.save(entity);
    }

    /**
     * 修改
     */
    public void edit(TerminalDeviceParam param){
        var entity = terminalManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        BeanUtil.copyProperties(param, entity, CopyOptions.create().ignoreNullValue());
        terminalManager.updateById(entity);
    }

    /**
     * 删除
     */
    public void delete(Long id){
        terminalManager.deleteById(id);
    }

    /**
     * 终端下拉列表
     */
    public List<LabelValue> dropdown(String appId) {
        return terminalManager.findAllByField(MchAppBaseEntity::getAppId, appId).stream()
                .map(o -> new LabelValue(o.getName(), o.getTerminalNo()))
                .toList();
    }
}
