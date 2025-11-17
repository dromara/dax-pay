package org.dromara.daxpay.payment.common.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.convert.PlatformConfigConvert;
import org.dromara.daxpay.payment.common.result.config.platform.PlatformWebsiteConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 站点显示内容配置
 * @author xxm
 * @since 2025/6/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_website_config")
public class PlatformWebsiteConfig extends MpBaseEntity implements ToResult<PlatformWebsiteConfigResult> {

    /** 系统名称 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String systemName;

    /** 公司全称 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String companyName;

    /** 公司电话 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String companyPhone;

    /** 公司邮箱 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String companyEmail;

    /** 系统完整logo */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wholeLogo;

    /** 系统简化Logo */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String simpleLogo;

    /** 工信部ICP备案信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String icpInfo;

    /** 工信部ICP链接地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String icpLink;

    /** 公网安备案信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String mpsInfo;

    /** 公网安备案链接地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String mpsLink;

    /** 中国支付清算协会备案信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String pcacInfo;

    /** 中国支付清算协会备案链接地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String pcacLink;

    /** 电信增值业务许可信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String icpPlusInfo;

    /** 电信增值业务许可链接地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String icpPlusLink;

    /** 版权信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String copyright;

    /** 版权信息链接 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String copyrightLink;

    /**
     * 转换
     */
    @Override
    public PlatformWebsiteConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
