package org.dromara.daxpay.payment.pay.service.assist;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.common.context.PaymentClientLocal;
import org.dromara.daxpay.payment.common.context.PaymentReqInfoLocal;
import org.dromara.daxpay.payment.common.exception.ConfigNotEnableException;
import org.dromara.daxpay.payment.common.exception.VerifySignFailedException;
import org.dromara.daxpay.payment.common.properties.DaxPayProperties;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.common.service.config.PlatformConfigService;
import org.dromara.daxpay.payment.common.util.PaySignUtil;
import org.dromara.daxpay.payment.isv.common.cache.IsvInfoCacheService;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import org.dromara.daxpay.payment.merchant.enums.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.local.MchContextLocal;
import org.dromara.daxpay.payment.merchant.result.config.MerchantCredentialResult;
import org.dromara.daxpay.payment.merchant.service.config.MerchantCredentialService;
import org.dromara.daxpay.payment.pay.local.PaymentContextLocal;
import org.dromara.daxpay.payment.unipay.param.PaymentCommonParam;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 交易支持服务接口
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAssistService {
    private final DaxPayProperties daxPayProperties;

    private final ClientCodeService clientCodeService;

    private final PlatformConfigService platformConfigService;

    private final IsvInfoCacheService isvInfoCacheService;

    private final MerchantCredentialService merchantCredentialService;

    private final MerchantManager merchantManager;

    private final MchAppManager mchAppManager;

    /**
     * 初始化请求相关信息上下文
     */
    public void initClient(PaymentCommonParam paymentCommonParam){
        PaymentClientLocal request = PaymentContextLocal.get().getClientInfo();
        request.setClientIp(paymentCommonParam.getClientIp());
    }

    /**
     * 入参签名校验
     */
    public void signVerify(PaymentCommonParam param) {
        var request = PaymentContextLocal.get();
        PaymentReqInfoLocal reqInfo = request.getReqInfo();
        // 使用商户公钥验签
        if (!PaySignUtil.verify(param, reqInfo.getPublicKey())){
            throw new VerifySignFailedException();
        }
    }

    /**
     * 请求有效时间校验
     */
    public void reqTimeoutVerify(PaymentCommonParam param) {
        var reqInfo = PaymentContextLocal.get().getReqInfo();
        // 开启请求超时校验并设置值
        if (reqInfo.isReqTimeout() && Objects.nonNull(reqInfo.getReqTimeoutSecond()) ){
            LocalDateTime now = LocalDateTime.now();
            // 时间差值(秒)
            long durationSeconds = Math.abs(LocalDateTimeUtil.between(now, param.getReqTime()).getSeconds());
            // 当前时间是否晚于请求时间
            if (DateTimeUtil.lt(now, param.getReqTime())){
                // 请求时间比服务器时间晚, 超过配置时间直接打回
                if (durationSeconds > reqInfo.getReqTimeoutSecond()){
                    throw new ValidationFailedException("请求时间晚于服务器接收到的时间，请检查");
                }
            } else {
                // 请求时间比服务器时间早, 超过配置时间直接打回
                if (durationSeconds > reqInfo.getReqTimeoutSecond()){
                    throw new ValidationFailedException("请求已过期，请重新发送！");
                }
            }

        }
    }

    /**
     * 使用平台私钥对响应对象进行签名
     */
    public void sign(DaxResult<?> result) {
        String privateKey = daxPayProperties.getKeyConfig().getPrivateKey();
        result.setSign(PaySignUtil.sign(result,privateKey));
    }

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
        // 获取应用信息, 如果应用号不传输, 查询默认应用
        MchApp mchApp;
        if (StrUtil.isBlank(appId)){
            mchApp = mchAppManager.findDefaultByMchNoNotTenant(mchNo).orElseThrow(() -> new ConfigNotEnableException("未找到商户默认应用配置"));
        } else {
            mchApp = mchAppManager.findByAppIdNotTenant(appId).orElseThrow(() -> new ConfigNotEnableException("未找到指定的应用配置"));
        }
        Merchant merchant = merchantManager.findByMchNo(mchNo).orElseThrow(() -> new ConfigNotEnableException("未找到指定的商户配置"));
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
        var mchApp = mchAppManager.findByAppIdNotTenant(appId).orElseThrow(() -> new ConfigNotEnableException("未找到指定的应用配置"));
        // 商户端商户号读取系统, 不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), DaxPayCode.Client.MERCHANT)){
            if (!Objects.equals(mchApp.getMchNo(), MchContextLocal.getMchNo())){
                throw new ValidationFailedException("该商户不拥有该应用");
            }
        }
        Merchant merchant = merchantManager.findByMchNo(mchApp.getMchNo()).orElseThrow(() -> new ConfigNotEnableException("未找到指定的商户配置"));
        this.initData(merchant, mchApp);
    }

    /**
     * 验证
     */
    public void checkStatus(){
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        if (Objects.isNull(reqInfo)){
            throw new DataNotExistException("未获取到请求相关信息，请检查");
        }
        // 应用
        if (!Objects.equals(reqInfo.getAppStatus(), MchAppStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("商户应用未启用");
        }
        // 商户
        if (!Objects.equals(reqInfo.getMchStatus(), MerchantStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("商户未启用");
        }
        // 服务商
        if (Objects.nonNull(reqInfo.getIsvStatus())&&!Objects.equals(reqInfo.getIsvStatus(), IsvStatusEnum.ENABLE.getCode())){
            throw new ConfigNotEnableException("服务商未启用");
        }
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
        // 提前设置商户号, 防止被数据权限插件影响到查询数据为空
        MchContextLocal.setMchNo(merchant.getMchNo());
        // 初始化支付上下文信息
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();

        // 商户信息和公钥配置
        var publicKey = Optional.ofNullable(merchantCredentialService.findByMchNo(merchant.getMchNo()))
                .map(MerchantCredentialResult::getPublicKey)
                .orElse(null);
        reqInfo.setMchNo(merchant.getMchNo())
                .setMchStatus(merchant.getStatus())
                .setPublicKey(publicKey);

        // 应用信息
        reqInfo.setAppId(mchApp.getAppId())
                .setAppStatus(mchApp.getStatus());

        // 查询服务商信息
        var isv = isvInfoCacheService.get(merchant.getIsvNo());
        reqInfo.setIsvNo(isv.getIsvNo())
                .setIsvStatus(isv.getStatus());
        // 平台信息
        var basicConfig = platformConfigService.getBasicConfig();
        var urlConfig = platformConfigService.getUrlConfig();
        reqInfo.setGatewayServiceUrl(urlConfig.getGatewayServiceUrl())
                .setGatewayH5Url(urlConfig.getGatewayH5Url());
        // 支付限额, 未配置设置为无限大99亿
        reqInfo.setLimitAmount(Optional.ofNullable(basicConfig.getSingleLimitAmount()).orElse(new BigDecimal("99999999999")));
        // 超时时间, 未配置设置为30分钟
        reqInfo.setOrderTimeout(Optional.ofNullable(basicConfig.getOrderTimeout()).orElse(30));
    }
}
