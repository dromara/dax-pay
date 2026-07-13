package cn.daxpay.open.payment.appadmin.service.merchant.appinfo;

import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoParam;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoQuery;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-商户应用服务
///
/// 转发至 [MchAppInfoService]
@Service
@RequiredArgsConstructor
public class AppAdminMchAppInfoService {

    private final MchAppInfoService mchAppInfoService;

    /// 新增
    public void add(MchAppInfoParam param) {
        mchAppInfoService.add(param);
    }

    /// 修改
    public void update(MchAppInfoParam param) {
        mchAppInfoService.update(param);
    }

    /// 分页
    public PageResult<MchAppInfoResult> page(PageParam pageParam, MchAppInfoQuery query) {
        return mchAppInfoService.page(pageParam, query);
    }

    /// 详情
    public MchAppInfoResult findById(Long id) {
        return mchAppInfoService.findById(id);
    }

    /// 删除
    public void delete(Long id) {
        mchAppInfoService.delete(id);
    }

    /// 设为默认
    public void setDefault(Long id) {
        mchAppInfoService.setDefault(id);
    }

    /// 取消默认
    public void clearDefault(Long id) {
        mchAppInfoService.clearDefault(id);
    }
}
