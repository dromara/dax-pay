package cn.daxpay.open.payment.app.merchant.service.info;

import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoParam;
import cn.daxpay.open.payment.merchant.param.appinfo.MchAppInfoQuery;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-商户应用服务
///
/// 转发至 core [MchAppInfoService]
@Service
@RequiredArgsConstructor
public class AppMerchantAppInfoService {

    private final MchAppInfoService mchAppInfoService;

    /// 新增商户应用
    public void add(MchAppInfoParam param) {
        mchAppInfoService.add(param);
    }

    /// 修改商户应用
    public void update(MchAppInfoParam param) {
        mchAppInfoService.update(param);
    }

    /// 商户应用分页
    public PageResult<MchAppInfoResult> page(PageParam pageParam, MchAppInfoQuery query) {
        return mchAppInfoService.page(pageParam, query);
    }

    /// 商户应用列表
    public List<MchAppInfoResult> list() {
        return mchAppInfoService.list();
    }

    /// 根据id查询
    public MchAppInfoResult findById(Long id) {
        return mchAppInfoService.findById(id);
    }

    /// 根据应用AppId获取应用详情
    public MchAppInfoResult findByAppId(String appId) {
        return mchAppInfoService.findByAppId(appId);
    }

    /// 删除商户应用
    public void delete(Long id) {
        mchAppInfoService.delete(id);
    }

    /// 设置默认商户应用
    public void setDefault(Long id) {
        mchAppInfoService.setDefault(id);
    }

    /// 取消默认商户应用
    public void clearDefault(Long id) {
        mchAppInfoService.clearDefault(id);
    }
}
