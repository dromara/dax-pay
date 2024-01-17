package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 支付宝对账服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayReconcileService {

    /**
     * 下载对账单, 并进行解析进行保存
     */
    @SneakyThrows
    public String downAndSave(String date){
        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillDate(date);
            model.setBillType("trade");
            // 获取对账单下载地址并下载
            String url = AliPayApi.billDownloadUrlQuery(model);
            byte[] bytes = HttpUtil.downloadBytes(url);
            // 使用 Apache commons-compress 包装流,
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(bytes),"GBK");

            ZipArchiveEntry entry;
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                String name = entry.getName();
                System.out.println(name);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipArchiveInputStream,"GBK"));
                List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                strings.forEach(System.out::println);

            }

            return url;
        } catch (AlipayApiException e) {
            log.error("下载对账单失败",e);
            throw new RuntimeException(e);
        }
    }
}
