package cn.bootx.platform.daxpay.util;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayWayExtraCode;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * 支付通道相关工具类
 *
 * @author xxm
 * @since 2022/7/12
 */
@UtilityClass
public class PayChannelUtil {

    /**
     * 构建扩展参数构建
     * @param payChannel 支付渠道编码
     * @param map 支付方式扩展字段信息 key 为 PayModelExtraCode中定义的
     */
    public String buildExtraParamsJson(String payChannel, Map<String, Object> map) {
        PayChannelEnum payChannelEnum = PayChannelEnum.findByCode(payChannel);
        switch (payChannelEnum) {
            case ALI: {
                AliPayParam aliPayParam = new AliPayParam();
                aliPayParam.setAuthCode(MapUtil.getStr(map, PayWayExtraCode.AUTH_CODE));
                return JSONUtil.toJsonStr(aliPayParam);
            }
            case WECHAT: {
                return JSONUtil.toJsonStr(new WeChatPayParam().setOpenId(MapUtil.getStr(map,PayWayExtraCode.OPEN_ID))
                    .setAuthCode(MapUtil.getStr(map,PayWayExtraCode.AUTH_CODE)));
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


}
