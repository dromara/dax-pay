package cn.daxpay.single.admin.controller.order;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.service.core.order.reconcile.service.ReconcileDiffService;
import cn.daxpay.single.service.core.order.reconcile.service.ReconcileQueryService;
import cn.daxpay.single.service.core.payment.reconcile.service.ReconcileService;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileTradeDetailDto;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileDiffDto;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileOrderDto;
import cn.daxpay.single.service.param.reconcile.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 支付对账
 * @author xxm
 * @since 2024/1/18
 */
@Tag(name = "对账控制器")
@RestController
@RequestMapping("/order/reconcile")
@RequiredArgsConstructor
public class ReconcileOrderController {
    private final ReconcileService reconcileService;
    private final ReconcileQueryService reconcileQueryService;
    private final ReconcileDiffService reconcileDiffService;

    @Operation(summary = "手动创建对账订单")
    @PostMapping("/create")
    public ResResult<Void> create(@RequestBody ReconcileOrderCreate param){
        ValidationUtil.validateParam(param);
        reconcileService.create(param.getDate(), param.getChannel());
        return Res.ok();
    }

    @Operation(summary = "手动触发对账文件下载")
    @PostMapping("/downAndSave")
    public ResResult<Void> downAndSave(Long id){
        reconcileService.downAndSave(id);
        return Res.ok();
    }

    @Operation(summary = "手动上传交易对账单文件")
    @PostMapping("/upload")
    public ResResult<Void> upload(ReconcileUploadParam param, MultipartFile file){
        ValidationUtil.validateParam(param);
        reconcileService.upload(param,file);
        return Res.ok();
    }

    @Operation(summary = "手动触发交易对账单比对")
    @PostMapping("/compare")
    public ResResult<Void> compare(Long id){
        reconcileService.compare(id);
        return Res.ok();
    }

    @Operation(summary = "下载原始交易对账单文件")
    @GetMapping("/downOriginal")
    public ResponseEntity<byte[]> downOriginal(Long id){
        return reconcileService.downOriginal(id);
    }

    @Operation(summary = "下载原始交易对账单记录(CSV格式)")
    @GetMapping("/downOriginal2Csv")
    public ResponseEntity<byte[]> downOriginal2Csv(Long id){
        return reconcileService.downOriginal2Csv(id);
    }

    @Operation(summary = "下载系统对账单(CSV格式)")
    @GetMapping("/downLocalCsv")
    public ResponseEntity<byte[]> downLocalCsv(Long id){
        return reconcileService.downLocalCsv(id);
    }

    @Operation(summary = "下载对账差异单(CSV格式)")
    @GetMapping("/downDiffCsv")
    public ResponseEntity<byte[]> downDiffCsv(Long id){
        return reconcileService.downDiffCsv(id);
    }

    @Operation(summary = "对账单分页")
    @GetMapping("/page")
    public ResResult<PageResult<ReconcileOrderDto>> page(PageParam pageParam, ReconcileOrderQuery query){
        return Res.ok(reconcileQueryService.page(pageParam, query));
    }

    @Operation(summary = "对账单详情")
    @GetMapping("/findById")
    public ResResult<ReconcileOrderDto> findById(Long id){
        return Res.ok(reconcileQueryService.findById(id));
    }

    @Operation(summary = "对账明细分页")
    @GetMapping("/detail/page")
    public ResResult<PageResult<ReconcileTradeDetailDto>> pageDetail(PageParam pageParam, ReconcileTradeDetailQuery query){
        return Res.ok(reconcileQueryService.pageDetail(pageParam, query));
    }

    @Operation(summary = "对账明细详情")
    @GetMapping("/detail/findById")
    public ResResult<ReconcileTradeDetailDto> findDetailById(Long id){
        return Res.ok(reconcileQueryService.findDetailById(id));
    }

    @Operation(summary = "对账差异分页")
    @GetMapping("/diff/page")
    public ResResult<PageResult<ReconcileDiffDto>> pageDiff(PageParam pageParam, ReconcileDiffQuery query){
        return Res.ok(reconcileDiffService.page(pageParam, query));
    }

    @Operation(summary = "对账差异详情")
    @GetMapping("/diff/findById")
    public ResResult<ReconcileDiffDto> findDiffById(Long id){
        return Res.ok(reconcileDiffService.findById(id));
    }
}
