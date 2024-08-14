package cn.daxpay.multi.channel.wechat.bo.reconcile;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.lang.reflect.Field;

/**
 * 微信交易对账解析文件
 * @author xxm
 * @since 2024/8/11
 */
@Slf4j
@Data
@Accessors(chain = true)
public class WechatReconcileBillDetail {

    // 交易时间
    @Excel(name="交易时间")
    private String transactionTime;
    // 公众账号ID
    @Excel(name="公众账号ID")
    private String appid;

    // 商户号
    @Excel(name="商户号")
    private String mchid;

    // 特约商户号
    @Excel(name="特约商户号")
    private String subMchid;

    // 设备号
    @Excel(name="设备号")
    private String deviceInfo;

    // 微信订单号
    @Excel(name="微信订单号")
    private String transactionId;

    // 商户订单号
    @Excel(name="商户订单号")
    private String outTradeNo;

    // 用户标识
    @Excel(name="用户标识")
    private String openid;

    // 交易类型 JSAPI/NATIVE
    @Excel(name="交易类型")
    private String tradeType;

    // 交易状态
    @Excel(name="交易状态")
    private String tradeState;

    // 付款银行
    @Excel(name="付款银行")
    private String bankType;

    // 货币种类
    @Excel(name="货币种类")
    private String feeType;

    // 应结订单金额
    @Excel(name="应结订单金额")
    private String settlementTotalFee;

    // 代金券金额
    @Excel(name="代金券金额")
    private String couponFee;

    // 微信退款单号
    @Excel(name="微信退款单号")
    private String refundId;

    // 商户退款单号
    @Excel(name="商户退款单号")
    private String outRefundNo;

    // 退款金额
    @Excel(name="退款金额")
    private String refundFee;

    // 充值券退款金额
    @Excel(name="充值券退款金额")
    private String couponRefundFee;

    // 退款类型
    @Excel(name="退款类型")
    private String refundType;

    // 退款状态
    @Excel(name="退款状态")
    private String refundStatus;

    // 商品名称
    @Excel(name="商品名称")
    private String body;

    // 商户数据包
    @Excel(name="商户数据包")
    private String attach;

    // 手续费
    @Excel(name="手续费")
    private String fee;

    // 费率
    @Excel(name="费率")
    private String feeRate;

    // 订单金额
    @Excel(name="订单金额")
    private String totalFee;

    // 申请退款金额
    @Excel(name="申请退款金额")
    private String refundFeeType;

    // 费率备注
    @Excel(name="费率备注")
    private String feeRemark;

    /**
     * 去除前缀的 ` 符号
     */
    public void removeStartSymbol() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(this);
                    if (StrUtil.startWith(value, "`")) {
                        field.set(this, StrUtil.replaceFirst(value, "`", ""));
                    }
                } catch (IllegalAccessException e) {
                    log.warn("去除前缀错误错误", e);
                }
            }
        }
    }
}
