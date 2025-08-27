package org.dromara.daxpay.service.device.service.terminal;

import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.device.dao.terminal.ChannelTerminalManager;
import org.dromara.daxpay.service.device.dao.terminal.TerminalDeviceManager;
import org.dromara.daxpay.service.device.entity.terminal.TerminalDevice;
import org.dromara.daxpay.service.device.param.commom.AssignMerchantParam;
import org.dromara.daxpay.service.device.param.terminal.TerminalDeviceParam;
import org.dromara.daxpay.service.device.param.terminal.TerminalDeviceQuery;
import org.dromara.daxpay.service.device.result.terminal.TerminalDeviceResult;
import org.dromara.daxpay.service.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.service.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalDeviceService {
    private final TerminalDeviceManager terminalDeviceManager;
    private final ChannelTerminalManager channelTerminalManager;
    private final MchAppManager mchAppManager;
    private final MerchantManager merchantManager;

    /**
     * 分页
     */
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query){
        return MpUtil.toPageResult(terminalDeviceManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public TerminalDeviceResult findById(Long id){
        return terminalDeviceManager.findById(id).map(TerminalDevice::toResult)
                .orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
    }

    /**
     * 创建
     */
    public void add(TerminalDeviceParam param){
        var entity = TerminalDevice.init(param);
        entity.setTerminalNo(genTerminalNo());
        terminalDeviceManager.save(entity);
    }

    /**
     * 修改
     */
    public void edit(TerminalDeviceParam param){
        var entity = terminalDeviceManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        BeanUtil.copyProperties(param, entity, CopyOptions.create().ignoreNullValue());
        terminalDeviceManager.updateById(entity);
    }


    /**
     * 绑定商户和应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(AssignMerchantParam param){
        if (StrUtil.isNotBlank(param.getMchNo())){
            var merchant = merchantManager.findByMchNo(param.getMchNo()).orElseThrow(() -> new ValidationFailedException("商户不存在"));
            if (StrUtil.isNotBlank(param.getAppId())){
                var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
                if (!Objects.equals(merchant.getMchNo(), app.getMchNo())){
                    throw new ValidationFailedException("商户和应用不匹配");
                }
            }
        } else {
            param.setAppId(null);
        }
        terminalDeviceManager.lambdaUpdate()
                .set(TerminalDevice::getMchNo, param.getMchNo())
                .set(TerminalDevice::getAppId, param.getAppId())
                .in(TerminalDevice::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 商户解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(AssignMerchantParam param){
        terminalDeviceManager.lambdaUpdate()
                .set(TerminalDevice::getMchNo, null)
                .set(TerminalDevice::getAppId, null)
                .in(TerminalDevice::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();

    }

    /**
     * 绑定应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(AssignMerchantParam param){
        if (StrUtil.isNotBlank(param.getAppId())){
            var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
            if (!Objects.equals(MchContextLocal.getMchNo(), app.getMchNo())){
                throw new ValidationFailedException("商户和应用不匹配");
            }
        }
        terminalDeviceManager.lambdaUpdate()
                .set(TerminalDevice::getAppId, param.getAppId())
                .in(TerminalDevice::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 应用解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(AssignMerchantParam param){
        terminalDeviceManager.lambdaUpdate()
                .set(TerminalDevice::getAppId, null)
                .in(TerminalDevice::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 删除
     */
    public void delete(Long id){
        // 判断是有已经进行了上报的
        if (channelTerminalManager.existsByTerminalId(id)){
            throw new DataErrorException("该终端设备下有通道报备记录，无法被删除");
        }
        terminalDeviceManager.deleteById(id);
    }

    /**
     * 终端下拉列表(应用)
     */
    public List<LabelValue> dropdownByAppId(String appId) {
        return terminalDeviceManager.findAllByField(TerminalDevice::getAppId, appId).stream()
                .map(o -> new LabelValue(o.getName(), o.getTerminalNo()))
                .toList();
    }

    /**
     * 生成终端号
     */
    private String genTerminalNo(){
        String terminalNo = "D"+ RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++){
            if (!terminalDeviceManager.existedByField(TerminalDevice::getTerminalNo, terminalNo)){
                return terminalNo;
            }
            terminalNo = "D"+ RandomUtil.randomNumbers(16);
        }
        throw new BizException("设备终端号生成失败");
    }
}
