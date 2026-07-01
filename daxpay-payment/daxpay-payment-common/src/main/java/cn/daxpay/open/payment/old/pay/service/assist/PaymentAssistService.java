package cn.daxpay.open.payment.old.pay.service.assist;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.exception.config.ConfigNotEnableException;
import cn.daxpay.open.platform.core.exception.business.VerifySignFailedException;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.payment.common.service.MerchantPaymentQueryService;
import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Optional.*;


/// # 交易支持服务接口
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAssistService {
    private final PlatformConfigProperties platformConfigProperties;

    private final ClientCodeService clientCodeService;



    private final MerchantPaymentQueryService merchantPaymentQueryService;

    private final PaymentContext apiContext;

    /// 入参签名校验
    public void signVerify(PaymentCommonParam param) {
        // 获取商户公钥
        String publicKey = merchantPaymentQueryService.findMerchantPublicKey(apiContext.getTradeInfo().getMchNo());
        // 签名和公钥校验
        if (StrUtil.isBlank(publicKey)){
            // 商户公钥为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchPublicKeyEmpty");
        }
        if (StrUtil.isBlank(param.getSign())){
            // 签名为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.signEmpty");
        }

        // 使用商户公钥验签
        if (!PaySignUtil.verify(param, publicKey)){
            throw new VerifySignFailedException();
        }
    }

    /// 使用平台私钥对响应对象进行签名
    public void sign(DaxResult<?> result) {
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        result.setSign(PaySignUtil.sign(result,privateKey));
    }

    /// 初始化商户和应用等相关信息
    /// 1. 统一支付相关接口调用时，要进行初始化
    /// 2. 接收到回调时，要进行初始化
    /// 3. 接收到消息通知时, 要进行初始化
    /// 4. 手动发起根据订单记录发起一些操作时, 读取信息进行初始化
    /// 5. 针对核心能力进行包装成功能时(收银台), 手动进行初始化
    public void initMchAndApp(String mchNo, String appId) {
        // 商户端商户号读取系统, 不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())){
            mchNo = apiContext.getTradeInfo().getMchNo();
        }
        // 获取应用信息, 如果应用号不传输, 查询默认应用
        cn.daxpay.open.payment.common.service.dto.MchAppInfoAccessInfo mchApp;
        if (StrUtil.isBlank(appId)) {
            mchApp = ofNullable(merchantPaymentQueryService.getDefaultAppByMchNo(mchNo))
                    // 未找到商户默认应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.defaultAppConfigNotFound"));
        } else {
            mchApp = ofNullable(merchantPaymentQueryService.getAppByAppId(appId))
                    // 未找到指定的应用配置
                    .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedAppConfigNotFound"));
        }
        var merchant = ofNullable(merchantPaymentQueryService.getMerchantByMchNo(mchNo))
                // 未找到指定的商户配置
                .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedMchConfigNotFound"));
        this.initData(merchant, mchApp);
    }

    /// 初始化商户和应用信息
    /// 1. 统一支付相关接口调用时，要进行初始化
    /// 2. 接收到回调时，要进行初始化
    /// 3. 接收到消息通知时, 要进行初始化
    /// 4. 手动发起根据订单记录发起一些操作时, 读取信息进行初始化
    /// 5. 针对核心能力进行包装成功能时(收银台), 手动进行初始化
    public void initMchAndApp(String appId) {
        // 获取应用信息
        var mchApp = ofNullable(merchantPaymentQueryService.getAppByAppId(appId))
                // 未找到指定的应用配置
                .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedAppConfigNotFound"));
        // 商户端商户号读取系统, 不允许自行设置
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())){
            if (!Objects.equals(mchApp.getMchNo(), apiContext.getTradeInfo().getMchNo())){
                // 该商户不拥有该应用
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchNotOwnApp");
            }
        }
        var merchant = ofNullable(merchantPaymentQueryService.getMerchantByMchNo(mchApp.getMchNo()))
                // 未找到指定的商户配置
                .orElseThrow(() -> new ConfigNotEnableException("error.payment.merchant.specifiedMchConfigNotFound"));
        this.initData(merchant, mchApp);
    }

    /// 初始化数据,
    /// 1.商户信息
    /// 2.应用信息
    /// 3.服务商信息
    
    private void initData(cn.daxpay.open.payment.common.service.dto.MerchantAccessInfo merchant,
                          cn.daxpay.open.payment.common.service.dto.MchAppInfoAccessInfo mchApp){
        // 判断是否匹配
        if (!Objects.equals(mchApp.getMchNo(), merchant.getMchNo())){
            // 商户号和应用号不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.mchNoAppNoMatch");
        }
        // 提前设置商户号, 防止被数据权限插件影响到查询数据为空
        apiContext.getTradeInfo().setMchNo(merchant.getMchNo());

        // 应用信息
        apiContext.getTradeInfo().setAppId(mchApp.getAppId());


        // ===== 状态校验 =====
        // 商户
        if (!Objects.equals(merchant.getStatus(), MerchantStatusEnum.ENABLE.getCode())){
            // 商户未启用
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchNotEnabled");
        }
        // 应用
        if (!Objects.equals(mchApp.getStatus(), MchAppStatusEnum.ENABLE.getCode())){
            // 商户应用未启用
            throw new ConfigNotEnableException(CommonCode.FAIL_CODE, "pay.error.assist.mchAppNotEnabled");
        }
    }
}

