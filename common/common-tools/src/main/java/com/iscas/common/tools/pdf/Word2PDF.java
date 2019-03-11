package com.iscas.common.tools.pdf;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/8/28 14:54
 * @since jdk1.8
 */
public class Word2PDF {
    private static final int WD_FORMAT_PDF = 17;// PDF 格式
    public static void wordToPDF(String startFile,String overFile){

        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            //转换前的文件路径
//            String startFile = "E:/math2017-2.docx";
            //转换后的文件路劲
//            String overFile =  "E:/math2017-2.pdf";

            doc = Dispatch.call(docs,  "Open" , startFile).toDispatch();
            File tofile = new File(overFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc,"SaveAs", overFile, WD_FORMAT_PDF);
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
        } finally {
            Dispatch.call(doc,"Close",false);
            if (app != null) {
                app.invoke("Quit", new Variant[] {});
            }
        }
        //结束后关闭进程
        ComThread.Release();
    }


//    public static void main(String[] args) {
//        wordToPDF();
//    }

}
