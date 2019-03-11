package com.iscas.common.web.tools.file.limiter;

import java.io.IOException;
import java.io.InputStream;

/**
 * 限流输入流,自己定义一个输入流继承Inputstream
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/14 21:30
 * @since jdk1.8
 */
public class LimiterInputStream extends InputStream {
    private InputStream is = null;
    private BandWidthLimiter bandwidthLimiter = null;
    public LimiterInputStream(InputStream is, BandWidthLimiter bandwidthLimiter) {
        this.is = is;
        this.bandwidthLimiter = bandwidthLimiter;
    }
    @Override
    public int read() throws IOException {
        if (this.bandwidthLimiter != null) {
            this.bandwidthLimiter.limitNextBytes();
        }
        return this.is.read();
    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(len);
        }
        return this.is.read(b, off, len);
    }
}
