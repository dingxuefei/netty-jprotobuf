package com.iscas.common.tools.math;

import java.io.*;

/**
 * <p>float一些增强工具方法</p>
 * @author zhuquanwen
 * @version 1.0
 * @since jdk1.8
 * @date 2018/7/13 18:12
 **/
public final class FloatUtils {
    private FloatUtils(){}
    /**
     *将float类型转为一个长度为4的字节数组
     *@date 2018/7/13 18:13
     *@param f float类型数字
     *@return byte[] 字节数组
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public final static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    /**
     * 将一个字节数组转为float类型数字
     *@date 2018/7/13 18:15
     *@param b 字节数组
     *@param index 偏移量
     *@return float
     */
    public final static float byte2float(byte[] b, int index) {
        assert b != null && b.length >= 4;
        assert index >= 0;
        assert b.length >= index + 4;
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 将文件转为一个float的二位数组
     * @date 2018/7/16
     * @param file {@link File}
     * @param row 二维数组行数
     * @param col 二维数组列数
     * @throws IOException IO异常
     * @return float[][] float类型二维数组
     * @see #convertIS2FArray(InputStream, int, int)
     */
    public final static float[][] convertFile2FArray(File file
            , int row, int col) throws IOException {
        InputStream is = new FileInputStream(file);
        return convertIS2FArray(is, row, col);
    }

    /**
     * 将输入流转为一个float的二位数组,使用DataInputStream
     * @date 2018/7/16
     * @param is {@link InputStream}
     * @param row 二维数组行数
     * @param col 二维数组列数
     * @throws IOException IO异常
     * @return float[][] float类型二维数组
     */
    public final static float[][] convertIS2FArray(InputStream is
            , int row, int col) throws IOException {
        assert is != null;
        assert row > 0;
        assert col > 0;
        float[][] datas = new float[row][col];
        DataInputStream dis = new DataInputStream(is);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                float floatData = dis.readFloat();
                datas[i][j] = floatData;
            }
        }
        dis.close();
        is.close();
        return datas;
    }

    /**
     * 将float的二维数组转为一个file,使用DataOutputStream
     * @date 2018/7/16
     * @param datas 二维数组
     * @param file {@link File} 输出的文件
     * @throws IOException IO异常
     */
    public static void convertFData2File(float[][] datas, File file) throws IOException {

        assert datas != null && datas.length > 0 && datas[0].length > 0;
        assert file != null;
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file, true);
        DataOutputStream dos = new DataOutputStream(os);
        for (int i = 0; i < datas.length ; i++) {
            for (int j = 0; j < datas[0].length ; j++) {
                float data = datas[i][j];
                dos.writeFloat(data);
            }
        }
        dos.close();
        os.close();
    }
}
