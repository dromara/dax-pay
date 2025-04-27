package org.dromara.daxpay.channel.wechat.service.allocation;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.dao.allocation.WechatAllocReceiverBindManager;
import org.dromara.daxpay.channel.wechat.entity.allocation.WechatAllocReceiverBind;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindParam;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindQuery;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverBindResult;
import org.dromara.daxpay.channel.wechat.service.payment.allocation.receiver.WechatPayAllocReceiverV2Service;
import org.dromara.daxpay.channel.wechat.service.payment.allocation.receiver.WechatPayAllocReceiverV3Service;
import org.dromara.daxpay.channel.wechat.service.payment.allocation.receiver.WechatPaySubAllocReceiverV2Service;
import org.dromara.daxpay.channel.wechat.service.payment.allocation.receiver.WechatPaySubAllocReceiverV3Service;
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 微信分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAllocReceiverBindService {

    private final WechatPayAllocReceiverV2Service receiverV2Service;

    private final WechatPaySubAllocReceiverV2Service receiverSubV2Service;

    private final WechatPayAllocReceiverV3Service receiverV3Service;

    private final WechatPaySubAllocReceiverV3Service receiverSubV3Service;

    private final WechatPayConfigService wechatPayConfigService;

    private final WechatAllocReceiverBindManager receiverBindManager;

    private final PaymentAssistService paymentAssistService;


    /**
     * 分页
     */
    public PageResult<WechatAllocReceiverBindResult> page(PageParam pageParam, WechatAllocReceiverBindQuery param){
        return MpUtil.toPageResult(receiverBindManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public WechatAllocReceiverBindResult findById(Long id) {
        return receiverBindManager.findById(id)
                .map(WechatAllocReceiverBind::toResult)
                .orElseThrow(() -> new RuntimeException("数据不存在"));
    }

    /**
     * 添加
     */
    public void add(WechatAllocReceiverBindParam param) {
        if (!validation(param)){
            throw new RuntimeException("接收方类型错误");
        }
        paymentAssistService.initMchAndApp(param.getAppId());
        var receiverBind = WechatAllocReceiverBind.init(param);
        receiverBindManager.save(receiverBind);
    }

    /**
     * 编辑
     */
    public void update(WechatAllocReceiverBindParam param){
        var receiverBind = receiverBindManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
        if (receiverBind.isBind()){
            throw new DataErrorException("分账接收方已绑定");
        }
        BeanUtil.copyProperties(param,receiverBind, CopyOptions.create().ignoreNullValue());
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 绑定
     */
    public void bind(Long id) {
        var receiverBind = receiverBindManager.findById(id).orElseThrow(() -> new DataNotExistException("微信分账接收方数据不存在"));
        paymentAssistService.initMchAndApp(receiverBind.getMchNo(),receiverBind.getAppId());
        if (receiverBind.isBind()){
            throw new DataErrorException("分账接收方已绑定");
        }
        try {
            if (receiverBind.isIsv()){
                var config = wechatPayConfigService.getAndCheckConfig(true);
                if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
                    receiverSubV2Service.bind(receiverBind, config);
                } else {
                    receiverSubV3Service.bind(receiverBind, config);
                }
            } else {
                var config = wechatPayConfigService.getAndCheckConfig(false);
                if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
                    receiverV2Service.bind(receiverBind, config);
                } else {
                    receiverV3Service.bind(receiverBind, config);
                }
            }
            receiverBind.setBind(true);
        } catch (OperationFailException e) {
            receiverBind.setErrorMsg(e.getMessage());
        }
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 解绑
     */
    public void unbind(Long id) {
        var receiverBind = receiverBindManager.findById(id).orElseThrow(() -> new DataNotExistException("微信分账接收方数据不存在"));
        paymentAssistService.initMchAndApp(receiverBind.getMchNo(),receiverBind.getAppId());
        if (!receiverBind.isBind()){
            throw new DataErrorException("分账接收方已解绑");
        }

        try {
            if (receiverBind.isIsv()){
                var config = wechatPayConfigService.getAndCheckConfig(true);
                if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
                    receiverSubV2Service.unbind(receiverBind, config);
                } else {
                    receiverSubV3Service.unbind(receiverBind, config);
                }
            } else {
                var config = wechatPayConfigService.getAndCheckConfig(false);
                if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
                    receiverV2Service.unbind(receiverBind, config);
                } else {
                    receiverV3Service.unbind(receiverBind, config);
                }
            }
            receiverBind.setBind(false);
        } catch (OperationFailException e) {
            receiverBind.setErrorMsg(e.getMessage());
        }
        receiverBindManager.updateById(receiverBind);
    }

    /**
     * 删除
     */
    public void remove(Long id){
        receiverBindManager.deleteById(id);
    }

    /**
     * 校验方法
     */
    public boolean validation(WechatAllocReceiverBindParam receiverBind){
        List<String> list = Arrays.asList(AllocReceiverTypeEnum.OPEN_ID.getCode(), AllocReceiverTypeEnum.MERCHANT_NO.getCode());
        String receiverType = receiverBind.getReceiverType();
        return list.contains(receiverType);
    }
}
