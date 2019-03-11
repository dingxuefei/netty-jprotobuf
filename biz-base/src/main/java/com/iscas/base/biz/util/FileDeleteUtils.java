package com.iscas.base.biz.util;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 文件删除工具类，开启一个单线程池，防止并发出错。
 *未用到，将在未来版本删除。
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/6 9:11
 * @since jdk1.8
 */
@Deprecated
public class FileDeleteUtils {
    private static ExecutorService es = Executors.newFixedThreadPool(1);
    private FileDeleteUtils(){}
    public static boolean delete(File file) throws ExecutionException, InterruptedException {
        Future<Boolean> submit = es.submit(() -> {
            return file.delete();
        });
        return submit.get();
    }
}
