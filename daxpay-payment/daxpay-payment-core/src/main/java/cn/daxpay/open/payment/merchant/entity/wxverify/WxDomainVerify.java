package cn.daxpay.open.payment.merchant.entity.wxverify;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.wxverify.WxDomainVerifyConvert;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 域名校验文件
///
/// 平台/商户将微信、支付宝下发的域名校验文件上传至平台, 由平台网关按文件名统一响应抓取请求:
/// - 商户级仅 MP_verify_*.txt（商户自己公众号/小程序的域名验证）;
/// - 平台级另支持微信「扫普通链接二维码」随机名 .txt 与支付宝「关联普通二维码」32位hex .html
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("mch_wx_domain_verify")
public class WxDomainVerify extends MchBaseEntity implements ToResult<WxDomainVerifyResult> {

    /// 是否平台级：false-商户级 true-平台级
    private boolean platform;

    /// 完整文件名（如 MP_verify_PjhdRxpB8FhG06Fr.txt）
    private String fileName;

    /// 验证码（文件名提取，全局唯一）
    private String verifyCode;

    /// 文件内容（微信生成的随机字符串）
    private String fileContent;

    /// 备注
    private String remark;

    /// 转换
    @Override
    public WxDomainVerifyResult toResult() {
        return WxDomainVerifyConvert.CONVERT.toResult(this);
    }
}
