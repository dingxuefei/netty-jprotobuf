package com.iscas.common.web.tools.file.limiter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 自己定义一个输出流继承OutputStream
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/14 21:34
 * @since jdk1.8
 */
public class LimiterOutputStream extends OutputStream {
    private OutputStream os = null;
    private BandWidthLimiter bandwidthLimiter = null;
    public LimiterOutputStream(OutputStream os, BandWidthLimiter bandwidthLimiter) {
        this.os = os;
        this.bandwidthLimiter = bandwidthLimiter;
    }
    @Override
    public void write(int b) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes();
        }
        this.os.write(b);
    }
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(len);
        }
        this.os.write(b, off, len);
    }

}
