package cn.daxpay.open.payment.merchant.entity.wxverify;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.wxverify.WxDomainVerifyConvert;
import cn.daxpay.open.payment.merchant.result.wxverify.WxDomainVerifyResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 微信域名验证文件
///
/// 商户将公众号/小程序的 MP_verify_xxx.txt 上传至平台, 由平台网关统一响应微信域名校验请求
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
