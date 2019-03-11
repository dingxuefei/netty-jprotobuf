package com.iscas.common.web.tools.file.limiter;


import com.iscas.common.web.tools.file.FileDownloadUtils;

/**
 * 下载限流工具类
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/14 21:28
 * @since jdk1.8
 */
public class BandWidthLimiter {
    public  static int maxBandWith = 2 * 1024; //kb
    /* kb */
    public static Long kb = new Long(1024);
    /* The smallest count chunk length in bytes */
    private static Long chunkLength = new Long(1024);
    /* How many bytes will be sent or receive */
    private int bytesWillBeSentOrReceive = 0;
    /* When the last piece was sent or receive */
    private long lastPieceSentOrReceiveTick = System.nanoTime();
    /* Default rate is 1024KB/s */
    private int maxRate = 1024;
    /* Time cost for sending chunkLength bytes in nanoseconds */
    private long timeCostPerChunk = (new Long(1000000000) * chunkLength)
            / (this.maxRate * kb);
    /**
     * Initialize a BandwidthLimiter object with a certain rate.
     *
     * @param maxRate the download or upload speed in KBytes
     */
    public BandWidthLimiter(int maxRate) {
        this.setMaxRate(maxRate);
    }
    /**
     * Set the max upload or download rate in kb/s. maxRate must be grater than
     * 0. If maxRate is zero, it means there is no bandwidth limit.
     *
     * @param maxRate If maxRate is zero, it means there is no bandwidth limit.
     * @throws IllegalArgumentException
     */
    public synchronized void setMaxRate(int maxRate)
            throws IllegalArgumentException {
        if (maxRate < 0) {
            throw new IllegalArgumentException("maxRate can not less than 0");
        }
        this.maxRate = maxRate < 0 ? 0 : maxRate;
        if (maxRate == 0) {
            this.timeCostPerChunk = 0;
        } else {
            this.timeCostPerChunk = (new Long(1000000000) * chunkLength)
                        / (this.maxRate * kb);
        }
    }
    /**
     * Next 1 byte should do bandwidth limit.
     */
    public synchronized void limitNextBytes() {
        this.limitNextBytes(1);
    }
    /**
     * Next len bytes should do bandwidth limit
     *
     * @param len
     */
    public synchronized void limitNextBytes(int len) {
        int online = FileDownloadUtils.onlineDownloadNumber.get();
        int max =  online > 0 ? BandWidthLimiter.maxBandWith / online : BandWidthLimiter.maxBandWith;
        setMaxRate(max);
        this.bytesWillBeSentOrReceive += len;
        /* We have sent chunkLength bytes */
        while (this.bytesWillBeSentOrReceive > chunkLength) {
            long nowTick = System.nanoTime();
            long missedTime = this.timeCostPerChunk
                    - (nowTick - this.lastPieceSentOrReceiveTick);
            if (missedTime > 0) {
                try {
                    Thread.sleep(missedTime / 1000000,
                            (int) (missedTime % 1000000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.bytesWillBeSentOrReceive -= chunkLength;
            this.lastPieceSentOrReceiveTick = nowTick
                    + (missedTime > 0 ? missedTime : 0);
        }
    }
}
