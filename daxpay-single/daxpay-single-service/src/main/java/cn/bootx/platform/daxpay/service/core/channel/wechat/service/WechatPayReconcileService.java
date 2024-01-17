package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.DownloadBillModel;
import com.ijpay.wxpay.model.DownloadFundFlowModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * 微信支付对账
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayReconcileService {
    private final WeChatPayConfigService weChatPayConfigService;

    /**
     * 下载对账单并保存
     * @param date 下载日期
     */
    public void downAndSave(String date, WeChatPayConfig config) {
        config = weChatPayConfigService.getConfig();
        this.downBill(date, config);
        this.downFundFlow(date, config);
    }

    /**
     * 下载交易账单
     */
    public void downBill(String date, WeChatPayConfig config){
        // 下载交易账单
        Map<String, String> bill = DownloadBillModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .bill_date(date)
                .bill_type("ALL")
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        String s = WxPayApi.downloadBill(config.isSandbox(), bill);
        System.out.println(s);
    }


    /**
     * 下载资金账单, 需要双向证书
     */
    public void downFundFlow(String date, WeChatPayConfig config){
        if (StrUtil.isBlank(config.getP12())){
            return;
        }
        Map<String, String> fundFlow = DownloadFundFlowModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .bill_date(date)
                .account_type("Basic")
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        byte[] fileBytes = Base64.decode(config.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String s = WxPayApi.downloadFundFlow(fundFlow, inputStream, config.getWxMchId());
        System.out.println(s);
    }

}
