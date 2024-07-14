package cn.daxpay.multi.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.convert.constant.ApiConstConvert;
import cn.daxpay.multi.service.result.constant.ApiConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付接口常量
 * @author xxm
 * @since 2024/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_api_const")
public class ApiConst extends MpIdEntity implements ToResult<ApiConstResult> {

    /** 编码 */
    private String code;

    /** 接口名称 */
    private String name;

    /** 接口地址 */
    private String api;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public ApiConstResult toResult() {
        return ApiConstConvert.CONVERT.toResult(this);
    }
}
