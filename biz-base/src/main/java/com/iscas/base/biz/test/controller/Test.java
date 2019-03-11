package com.iscas.base.biz.test.controller;



import com.iscas.common.web.tools.file.FileDownloadUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/21 10:04
 * @since jdk1.8
 */
@RestController
public class Test {
    @PostMapping("/a/{name:.+}")
    public ResponseEntity<InputStreamResource> testa(@PathVariable String name){
        String filePath = "E:/aaa.xlsx";
//        File file = new File(filePath);
        FileSystemResource file = new FileSystemResource(filePath);
        try (InputStream is = new FileInputStream(filePath)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"","aaa.xlsx" ));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/b/{name:.+}")
    public void testb(HttpServletRequest request, HttpServletResponse response, @PathVariable String name) throws Exception {
        String filePath = "E:/aaa.xlsx";
        FileDownloadUtils.downExcelStream(request, response,new FileInputStream(filePath), "a.xlsx");
    }

}
