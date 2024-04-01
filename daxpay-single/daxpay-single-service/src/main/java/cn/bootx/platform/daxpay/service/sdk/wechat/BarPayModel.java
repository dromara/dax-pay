package cn.bootx.platform.daxpay.service.sdk.wechat;

import com.ijpay.core.model.BaseModel;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 付款码支付, 增加分账字段
 * @author xxm
 * @since 2024/3/27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BarPayModel extends BaseModel {
    /**
     * 是否押金支付
     */
    private String deposit;
    private String appid;
    private String sub_appid;
    private String mch_id;
    private String sub_mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String sign_type;
    private String body;
    private String detail;
    private String attach;
    private String out_trade_no;
    private String total_fee;
    private String fee_type;
    private String spbill_create_ip;
    private String goods_tag;
    private String limit_pay;
    private String time_start;
    private String time_expire;
    private String auth_code;
    private String receipt;
    private String scene_info;
    private String openid;
    /**
     * 人脸凭证，用于人脸支付
     */
    private String face_code;
    private String profit_sharing;
}
