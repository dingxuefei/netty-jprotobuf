package com.iscas.common.tools.security;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * URL编码工具类
 * @author zhuquanwen
 * @date: 2018/7/13 16:29
 **/
public final class URLCoderUtils {
    private URLCoderUtils(){}
    /**
     * 转换编码 ISO-8859-1到GB2312
     * @param text
     * @return
     */
    public static final String iso2Gb(String text) throws UnsupportedEncodingException {
        assert StringUtils.isNotBlank(text);
        String result = null;
        result = new String(text.getBytes("ISO-8859-1"), "GB2312");
        return result;
    }

    /**
     * 转换编码 GB2312到ISO-8859-1
     * @param text
     * @return
     */
    public static final String gb2Iso(String text) throws UnsupportedEncodingException {
        assert StringUtils.isNotBlank(text);
        String result = "";
        result = new String(text.getBytes("GB2312"), "ISO-8859-1");
        return result;
    }
    /**
     * Utf8URL编码
     * @param text
     * @return
     */
    public static final String utf8URLencode(String text) throws UnsupportedEncodingException {
        assert StringUtils.isNotBlank(text);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 0 && c <= 255) {
                result.append(c);
            }else {
                byte[] b = new byte[0];
                b = Character.toString(c).getBytes("UTF-8");
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    result.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return result.toString();
    }

    /**
     * Utf8URL解码
     * @param text
     * @return
     */
    public static final String utf8URLdecode(String text) {
        assert StringUtils.isNotBlank(text);
        String result = "";
        int p = 0;
        if (text!=null && text.length()>0){
            text = text.toLowerCase();
            p = text.indexOf("%e");
            if (p == -1) {
                return text;
            }
            while (p != -1) {
                result += text.substring(0, p);
                text = text.substring(p, text.length());
                if (text == "" || text.length() < 9) {
                    return result;
                }
                result += codeToWord(text.substring(0, 9));
                text = text.substring(9, text.length());
                p = text.indexOf("%e");
            }

        }

        return result + text;
    }

    /**
     * utf8URL编码转字符
     * @param text
     * @return
     */
    private static final String codeToWord(String text) {
        String result;

        if (utf8CodeCheck(text)) {
            byte[] code = new byte[3];
            code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
            code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
            code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
            try {
                result = new String(code, "UTF-8");
            }catch (UnsupportedEncodingException ex) {
                result = null;
            }
        }
        else {
            result = text;
        }

        return result;
    }

    /**
     * 编码是否有效
     * @param text
     * @return
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private static final boolean utf8CodeCheck(String text){
        String sign = "";
        if (text.startsWith("%e")) {
            for (int i = 0, p = 0; p != -1; i++) {
                p = text.indexOf("%", p);
                if (p != -1) {
                    p++;
                }
                sign += p;
            }
        }
        return "147-1".equals(sign);
    }

    /**
     * 判断是否Utf8Url编码
     * @param text
     * @return
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static final boolean isUtf8Url(String text) {
        assert StringUtils.isNotBlank(text);
        text = text.toLowerCase();
        int p = text.indexOf("%");
        if (p != -1 && text.length() - p > 9) {
            text = text.substring(p, p + 9);
        }
        return utf8CodeCheck(text);
    }


}
