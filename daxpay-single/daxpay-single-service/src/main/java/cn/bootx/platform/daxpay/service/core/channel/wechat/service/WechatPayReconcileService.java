package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.service.core.channel.wechat.domain.WechatPayReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.channel.wechat.domain.WechatPayReconcileBillTotal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
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
import java.util.List;
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
        Map<String, String> params = DownloadBillModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .bill_date(date)
                .bill_type("ALL")
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        String res = WxPayApi.downloadBill(config.isSandbox(), params);
        // 获取交易记录

        // 获取汇总数据
        System.out.println(res);
        CsvReader reader = CsvUtil.getReader();
        List<WechatPayReconcileBillDetail> read = reader.read(res, WechatPayReconcileBillDetail.class);
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
//        String url = WxPayApi.getReqUrl(PayApiEnum.DOWNLOAD_BILL, null, config.isSandbox());
//        String post = HttpUtil.post(url, WxPayKit.toXml(fundFlow));
        System.out.println(s);
    }


    public static void main(String[] args){
        String s = "\uFEFF交易时间,公众账号ID,商户号,特约商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,应结订单金额,代金券金额,微信退款单号,商户退款单号,退款金额,充值券退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率,订单金额,申请退款金额,费率备注\n" +
                "`2024-01-16 22:43:59,`wx9dfe38480f030e85,`1627859777,`0,`,`4200002086202401160745964084,`1747268229775183872,`o2RW45oGxo1Z8Fhcf6Oy9rMM7aT8,`NATIVE,`SUCCESS,`OTHERS,`CNY,`0.02,`0.00,`0,`0,`0.00,`0.00,`,`,`测试支付4,`,`0.00000,`0.90%,`0.02,`0.00,`\n" +
                "`2024-01-16 22:45:16,`wx9dfe38480f030e85,`1627859777,`0,`,`4200002086202401160745964084,`1747268229775183872,`o2RW45oGxo1Z8Fhcf6Oy9rMM7aT8,`NATIVE,`REFUND,`OTHERS,`CNY,`0.00,`0.00,`50303108562024011601487282563,`R1747268742516264960,`0.01,`0.00,`ORIGINAL,`SUCCESS,`测试支付4,`,`0.00000,`0.90%,`0.00,`0.01,`\n" +
                "`2024-01-16 23:10:18,`wx9dfe38480f030e85,`1627859777,`0,`,`4200002086202401160745964084,`1747268229775183872,`o2RW45oGxo1Z8Fhcf6Oy9rMM7aT8,`NATIVE,`REFUND,`OTHERS,`CNY,`0.00,`0.00,`50303108562024011683774350275,`R1747275041228390400,`0.01,`0.00,`ORIGINAL,`SUCCESS,`测试支付4,`,`0.00000,`0.90%,`0.00,`0.01,`\n" +
                "总交易单数,应结订单总金额,退款总金额,充值券退款总金额,手续费总金额,订单总金额,申请退款总金额\n" +
                "`3,`0.02,`0.02,`0.00,`0.00000,`0.02,`0.02";
        s = s.replaceAll("`", "").replaceAll("\uFEFF", "");
        String s1 = StrUtil.subBefore(s, "总交易单数", false);
        String s2 = s.substring(s.indexOf("总交易单数"));
        CsvReader reader = CsvUtil.getReader();
        List<WechatPayReconcileBillDetail> read = reader.read(s1, WechatPayReconcileBillDetail.class);
        List<WechatPayReconcileBillTotal> read1 = reader.read(s2, WechatPayReconcileBillTotal.class);

    }
}
