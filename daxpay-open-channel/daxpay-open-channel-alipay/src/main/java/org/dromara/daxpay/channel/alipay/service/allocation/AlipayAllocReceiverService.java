package org.dromara.daxpay.channel.alipay.service.allocation;

import org.dromara.daxpay.channel.alipay.convert.AlipayAllocReceiverConvert;
import org.dromara.daxpay.channel.alipay.entity.allocation.AlipayAllocReceiver;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverParam;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝分账接收者管理
 * @author xxm
 * @since 2024/3/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocReceiverService {

    private final AllocReceiverManager receiverManager;

    private final PaymentAssistService paymentAssistService;

    /**
     * 获取支付宝分账接收方
     */
    public AlipayAllocReceiverResult findById(Long id){
        return receiverManager.findById(id)
                .map(AlipayAllocReceiver::convertChannel)
                .map(AlipayAllocReceiver::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("乐刷分账接收方不存在"));

    }

    /**
     * 新增或修改
     */
    public void saveOrUpdate(AlipayAllocReceiverParam param){
        if(param.getId() == null){
            add(param);
        }else{
            update(param);
        }
    }

    /**
     * 添加
     */
    public void add(AlipayAllocReceiverParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        AlipayAllocReceiver entity = AlipayAllocReceiverConvert.CONVERT.toEntity(param);
        entity.setMchNo(mchApp.getMchNo());
        AllocReceiver receiver = entity.toReceiver(param.isIsv());
        String uuid = UUID.fastUUID().toString(true);
        receiver.setReceiverNo(uuid);
        receiverManager.save(receiver);
    }

    /**
     * 更新
     */
    public void update(AlipayAllocReceiverParam param){
        var allocReceiver = receiverManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("乐刷分账接收方不存在"));
        // 通道配置 --转换--> 乐刷配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        var leshuaConfig = AlipayAllocReceiver.convertChannel(allocReceiver);
        BeanUtil.copyProperties(param, leshuaConfig, CopyOptions.create()
                .ignoreNullValue());

        var receiver = leshuaConfig.toReceiver(Objects.equals(allocReceiver.getChannel(), ChannelEnum.ALIPAY_ISV.getCode()));
        // 手动清空一下默认的数据版本号
        receiver.setVersion(null);
        BeanUtil.copyProperties(receiver, allocReceiver, CopyOptions.create()
                .ignoreNullValue());
        receiverManager.updateById(allocReceiver);
    }

}
