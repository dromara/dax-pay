package cn.bootx.platform.daxpay.util;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayWayExtraCode;
import cn.bootx.platform.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 支付方式工具类
 *
 * @author xxm
 * @since 2022/7/12
 */
@UtilityClass
public class PayWayUtil {

    /**
     * 获取支付宝的过期时间
     */
    public String getAliExpiredTime(Integer minute) {
        return minute + "m";
    }

    /**
     * 获取微信的过期时间
     */
    public String getWxExpiredTime(Integer minute) {
        LocalDateTime time = LocalDateTimeUtil.offset(LocalDateTime.now(), minute, ChronoUnit.MINUTES);
        return LocalDateTimeUtil.format(time, DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取支付单的超时时间
     */
    public LocalDateTime getPaymentExpiredTime(Integer minute) {
        return LocalDateTimeUtil.offset(LocalDateTime.now(), minute, ChronoUnit.MINUTES);
    }

    /**
     * 判断是否有异步支付
     */
    public boolean isNotSync(List<PayWayParam> payWayParams) {
        return payWayParams.stream()
            .map(PayWayParam::getPayChannel)
            .noneMatch(PayChannelEnum.ASYNC_TYPE_CODE::contains);
    }

    /**
     * 获取异步支付参数
     */
    public PayWayParam getAsyncPayModeParam(PayParam payParam) {
        return payParam.getPayWayList()
            .stream()
            .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getPayChannel()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
    }

    /**
     * 构建扩展参数构建
     * @param payChannel 支付通道编码
     * @param map 支付方式扩展字段信息 key 为 PayModelExtraCode中定义的
     */
    public String buildExtraParamsJson(String payChannel, Map<String, Object> map) {
        PayChannelEnum payChannelEnum = PayChannelEnum.findByCode(payChannel);
        switch (payChannelEnum) {
            case ALI: {
                return JSONUtil.toJsonStr(new AliPayParam().setAuthCode(MapUtil.getStr(map,PayWayExtraCode.AUTH_CODE))
                    .setReturnUrl(MapUtil.getStr(map,PayWayExtraCode.RETURN_URL)));
            }
            case WECHAT: {
                return JSONUtil.toJsonStr(new WeChatPayParam().setOpenId(MapUtil.getStr(map,PayWayExtraCode.OPEN_ID))
                    .setAuthCode(MapUtil.getStr(map,PayWayExtraCode.AUTH_CODE)));
            }
            case VOUCHER: {
                String voucherNo = MapUtil.getStr(map,PayWayExtraCode.VOUCHER_NO);
                @SuppressWarnings("unchecked")
                List<String> voucherNos = MapUtil.get(map,PayWayExtraCode.VOUCHER_NO_LIST,List.class);
                List<String> list = new ArrayList<>();
                if (CollUtil.isNotEmpty(voucherNos)){
                    list.addAll(voucherNos);
                }
                else if (StrUtil.isNotBlank(voucherNo)) {
                    list.add(voucherNo);
                }
                return JSONUtil.toJsonStr(new VoucherPayParam().setCardNoList(list));
            }
            case WALLET: {
                String walletId = MapUtil.getStr(map,PayWayExtraCode.WALLET_ID);
                String userId = MapUtil.getStr(map,PayWayExtraCode.USER_ID);
                WalletPayParam walletPayParam = new WalletPayParam();

                if (StrUtil.isNotBlank(walletId)){
                    walletPayParam.setWalletId(Long.valueOf(walletId));
                }

                if (StrUtil.isNotBlank(userId)){
                    walletPayParam.setUserId(Long.valueOf(userId));
                }
                return JSONUtil.toJsonStr(walletPayParam);
            }
            default: {
                return null;
            }
        }
    }

    /**
     * 检查支付金额
     */
    public void validationAmount(List<PayWayParam> payModeList) {
        for (PayWayParam payWayParam : payModeList) {
            // 支付金额小于等于零
            if (BigDecimalUtil.compareTo(payWayParam.getAmount(), BigDecimal.ZERO) < 1) {
                throw new PayAmountAbnormalException();
            }
        }
    }

    /**
     * 检查异步支付方式
     */
    public void validationAsyncPayMode(PayParam payParam) {
        // 组合支付时只允许有一个异步支付方式
        List<PayWayParam> payModeList = payParam.getPayWayList();

        long asyncPayModeCount = payModeList.stream()
            .map(PayWayParam::getPayChannel)
            .map(PayChannelEnum::findByCode)
            .filter(PayChannelEnum.ASYNC_TYPE::contains)
            .count();
        if (asyncPayModeCount > 1) {
            throw new PayFailureException("组合支付时只允许有一个异步支付方式");
        }
    }

}
