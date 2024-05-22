package cn.daxpay.single.service.core.payment.allocation.service;

import cn.daxpay.single.param.payment.allocation.AllocationParam;
import cn.daxpay.single.service.common.context.ApiInfoLocal;
import cn.daxpay.single.service.common.context.NoticeLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderExtraManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分账支撑方法
 * @author xxm
 * @since 2024/5/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationAssistService {


    private final AllocationOrderExtraManager allocationOrderExtraManager;

    /**
     * 初始化通知相关上下文
     * 1. 异步通知参数: 读取参数配置 -> 读取接口配置 -> 读取平台参数
     * 2. 同步跳转参数: 读取参数配置 -> 读取平台参数
     * 3. 中途退出地址: 读取参数配置
     */
    public void initNotice(AllocationParam allocationParam) {
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        PlatformLocal platform = PaymentContextLocal.get()
                .getPlatformInfo();
        // 异步回调为开启状态
        if (apiInfo.isNotice()) {
            // 首先读取请求参数
            noticeInfo.setNotifyUrl(allocationParam.getNotifyUrl());
            // 读取接口配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())) {
                noticeInfo.setNotifyUrl(apiInfo.getNoticeUrl());
            }
            // 读取平台配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())) {
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
    }
    /**
     * 根据新传入的分账订单更新订单和扩展信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(AllocationParam allocationParam, AllocationOrderExtra orderExtra) {
        // 扩展信息
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        orderExtra.setClientIp(allocationParam.getClientIp())
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setAttach(allocationParam.getAttach())
                .setClientIp(allocationParam.getClientIp())
                .setReqTime(allocationParam.getReqTime());

        allocationOrderExtraManager.updateById(orderExtra);


    }


    }
