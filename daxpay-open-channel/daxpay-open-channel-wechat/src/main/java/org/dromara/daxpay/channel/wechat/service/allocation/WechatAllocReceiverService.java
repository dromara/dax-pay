package org.dromara.daxpay.channel.wechat.service.allocation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.convert.WechatAllocReceiverConvert;
import org.dromara.daxpay.channel.wechat.entity.allocation.WechatAllocReceiver;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverParam;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 微信分账接收方服务类
 * @author xxm
 * @since 2025/1/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAllocReceiverService {

    private final AllocReceiverManager receiverManager;

    private final PaymentAssistService paymentAssistService;

    /**
     * 获取分账接收方
     */
    public WechatAllocReceiverResult findById(Long id){
        return receiverManager.findById(id)
                .map(WechatAllocReceiver::convertChannel)
                .map(WechatAllocReceiver::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("微信分账接收方不存在"));
    }

    /**
     * 新增或更新
     */
    public void saveOrUpdate(WechatAllocReceiverParam param){
        if (param.getId() == null) {
            add(param);
        } else {
            update(param);
        }
    }

    /**
     * 添加
     */
    public void add(WechatAllocReceiverParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        var entity = WechatAllocReceiverConvert.CONVERT.toEntity(param);
        AllocReceiver receiver = entity.toReceiver(param.isIsv());
        String uuid = UUID.fastUUID().toString(true);
        receiver.setReceiverNo(uuid);
        receiverManager.save(receiver);
    }

    /**
     * 更新
     */
    public void update(WechatAllocReceiverParam param){
        var allocReceiver = receiverManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("微信分账接收方不存在"));
        // 通道配置 --转换--> 通道配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        var wechatAllocReceiver = WechatAllocReceiver.convertChannel(allocReceiver);
        BeanUtil.copyProperties(param, wechatAllocReceiver, CopyOptions.create()
                .ignoreNullValue());
        var receiver = wechatAllocReceiver.toReceiver(Objects.equals(allocReceiver.getChannel(), ChannelEnum.WECHAT_ISV.getCode()));
        // 手动清空一下默认的数据版本号
        receiver.setVersion(null);
        BeanUtil.copyProperties(receiver, allocReceiver, CopyOptions.create()
                .ignoreNullValue());
        receiverManager.updateById(allocReceiver);
    }
}

