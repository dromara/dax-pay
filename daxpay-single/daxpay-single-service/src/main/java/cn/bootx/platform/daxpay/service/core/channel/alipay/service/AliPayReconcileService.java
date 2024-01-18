package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliReconcileBillTotal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝对账服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayReconcileService {
    private final AliPayConfigService configService;

    /**
     * 下载对账单, 并进行解析进行保存
     * @param date 对账日期 yyyy-MM-dd 格式
     */
    @SneakyThrows
    public void downAndSave(String date){
        AliPayConfig config = configService.getConfig();
        configService.initConfig(config);

        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillDate(date);
            model.setBillType("trade");
            val response = AliPayApi.billDownloadUrlQueryToResponse(model);
            // 判断返回结果
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                log.error("获取支付宝对账单失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }

            // 获取对账单下载地址并下载
            String url = response.getBillDownloadUrl();
            byte[] bytes = HttpUtil.downloadBytes(url);
            // 使用 Apache commons-compress 包装流,
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(bytes),"GBK");
            ZipArchiveEntry entry;
            List<AliReconcileBillDetail> billDetails;
            List<AliReconcileBillTotal> billTotals;
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipArchiveInputStream,"GBK"));
                List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                String name = entry.getName();
                if (StrUtil.endWith(name,"_业务明细(汇总).csv")){
                    billTotals = this.parseTotal(strings);
                } else {
                    billDetails = this.parseDetail(strings);
                }
            }
            // 保存
            System.out.println();
        } catch (AlipayApiException e) {
            log.error("下载对账单失败",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析明细
     */
    public List<AliReconcileBillDetail> parseDetail(List<String> list){
        // 去除前 4 行和后 2 行 然后合并是个一个字符串
        String billDetail = list.subList(4, list.size() - 2)
                .stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billDetail = billDetail.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        List<AliReconcileBillDetail> billDetails = reader.read(billDetail, AliReconcileBillDetail.class);
        return billDetails;
    }

    /**
     * 解析汇总
     */
    public List<AliReconcileBillTotal> parseTotal(List<String> list){
        // 去除前 4 行和后 2 行 然后合并是个一个字符串
        String billTotal = list.subList(4, list.size() - 2)
                .stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billTotal = billTotal.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        List<AliReconcileBillTotal> billTotals = reader.read(billTotal, AliReconcileBillTotal.class);
        return billTotals;
    }

}
