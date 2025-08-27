package org.dromara.daxpay.server.service.common;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.common.service.config.PlatformConfigService;
import org.dromara.daxpay.service.merchant.cache.MchAppCacheService;
import org.dromara.daxpay.service.merchant.cache.MerchantCacheService;
import org.dromara.daxpay.service.merchant.entity.app.MchApp;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付网关端交易(支付、退款等各类操作)支持服务接口
 * @author xxm
 * @since 2024/12/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAssistServiceImpl implements PaymentAssistService {

    private final ClientCodeService clientCodeService;

    private final PlatformConfigService platformConfigService;

    private final MerchantCacheService merchantCacheService;

    private final MchAppCacheService mchAppCacheService;

    /**
     * 初始化商户和应用等相关信息
     * 1. 统一支付相关接口调用时，要进行初始化
     * 2. 接收到回调时，要进行初始化
     * 3. 接收到消息通知时, 要进行初始化
     * 4. 手动发起根据订单记录发起一些操作时, 读取信息进行初始化
     * 5. 针对核心能力进行包装成功能时(收银台), 手动进行初始化
     */
    public void initMchAndApp(String mchNo, String appId) {
        // 商户端商户号读取系统, 不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), DaxPayCode.Client.MERCHANT)){
            mchNo = MchContextLocal.getMchNo();
        }
        // 获取应用信息
        Merchant merchant = merchantCacheService.get(mchNo);
        MchApp mchApp = mchAppCacheService.get(appId);
        this.initData(merchant, mchApp);
    }

    /**
     * 初始化商户和应用信息
     * 1. 统一支付相关接口调用时，要进行初始化
     * 2. 接收到回调时，要进行初始化
     * 3. 接收到消息通知时, 要进行初始化
     * 4. 手动发起根据订单记录发起一些操作时, 读取信息进行初始化
     * 5. 针对核心能力进行包装成功能时(收银台), 手动进行初始化
     */
    public void initMchAndApp(String appId) {
        // 获取应用信息
        var mchApp = mchAppCacheService.get(appId);
        // 商户端商户号读取系统, 不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), DaxPayCode.Client.MERCHANT)){
            if (!Objects.equals(mchApp.getMchNo(), MchContextLocal.getMchNo())){
                throw new ValidationFailedException("该商户不拥有该应用");
            }
        }
        Merchant merchant = merchantCacheService.get(mchApp.getMchNo());
        this.initData(merchant, mchApp);
    }

    /**
     * 初始化数据,
     * 1.商户信息
     * 2.应用信息
     * 3.服务商信息
     * 4.代理商信息
     * 5.平台配置信息
     */
    private void initData(Merchant merchant, MchApp mchApp){
        // 判断是否匹配
        if (!Objects.equals(mchApp.getMchNo(), merchant.getMchNo())){
            throw new ValidationFailedException("商户号和应用号不匹配");
        }
        // 初始化支付上下文信息
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        BeanUtil.copyProperties(mchApp, reqInfo);

        // 商户信息
        reqInfo.setMchName(merchant.getMchName())
                .setMchNo(merchant.getMchNo())
                .setMchStatus(merchant.getStatus());

        // 平台信息
        var basicConfig = platformConfigService.getBasicConfig();
        var urlConfig = platformConfigService.getUrlConfig();
        reqInfo.setGatewayServiceUrl(urlConfig.getGatewayServiceUrl())
                .setGatewayH5Url(urlConfig.getGatewayH5Url());
        // 支付限额
        if (basicConfig.getSingleLimitAmount() != null){
            // 如果应用为空读取平台配置
            if (reqInfo.getLimitAmount() == null){
                reqInfo.setLimitAmount(basicConfig.getSingleLimitAmount());
            } else {
                // 都不为空读取小的
                reqInfo.setLimitAmount(reqInfo.getLimitAmount().min(basicConfig.getSingleLimitAmount()));
            }
        }

        // 初始化商户租户上下文信息
        MchContextLocal.setMchNo(merchant.getMchNo());
    }

}
