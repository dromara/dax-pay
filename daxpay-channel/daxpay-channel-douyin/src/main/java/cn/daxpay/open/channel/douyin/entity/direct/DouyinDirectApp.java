package cn.daxpay.open.channel.douyin.entity.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppConvert;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用
///
/// 表示抖音直连模式下商户注册的应用实体，每个应用关联一个通道商户号，拥有独立的抖音应用ID。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "douyin_direct_app", autoResultMap = true)
public class DouyinDirectApp extends MchBaseEntity implements ToResult<DouyinDirectAppResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 抖音应用AppId(APPID)
    private String douyinAppId;

    /** 应用类型: mini_program-小程序 mobile_app-移动应用 web_app-网站应用 */
    private String appType;

    /// 转换
    @Override
    public DouyinDirectAppResult toResult() {
        return DouyinDirectAppConvert.CONVERT.toResult(this);
    }
}
