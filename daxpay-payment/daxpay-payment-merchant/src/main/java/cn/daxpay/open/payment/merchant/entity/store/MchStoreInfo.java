package cn.daxpay.open.payment.merchant.entity.store;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.store.MchStoreInfoConvert;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

/// # 门店信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("mch_store_info")
public class MchStoreInfo extends MchBaseEntity implements ToResult<MchStoreInfoResult> {

    /// 门店号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String storeNo;

    /// 门店名称
    private String storeName;

    /// 联系人电话
    private String contactPhone;

    /// 门店LOGO
    private String logoUrl;

    /// 门头照
    private String facadeUrl;

    /// 门店内景照
    private String interiorUrl;

    /// 行政区划代码
    private String regionCode;

    /// 详细地址
    private String address;

    /// 经度
    private BigDecimal longitude;

    /// 纬度
    private BigDecimal latitude;

    /// 状态
    /// @see cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum
    private String status;

    /// 备注
    private String remark;

    @Override
    public MchStoreInfoResult toResult() {
        return MchStoreInfoConvert.CONVERT.toResult(this);
    }
}
