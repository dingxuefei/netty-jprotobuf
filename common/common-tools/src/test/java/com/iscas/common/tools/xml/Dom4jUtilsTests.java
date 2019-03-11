package com.iscas.common.tools.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * XMLUtils工具类测试
 * @author zhuquanwen
 * @date: 2018/7/13 14:25
 **/
public class Dom4jUtilsTests {
    /**
     * 测试通过流获取XML读取其中一些属性的操作，通过文件，或者XML字符串的方式类似，<br/>
     * 只是获取document的传参不一样而已，这里不做测试了
     * */
    @Test
    @Ignore
    public  void readXMLTest() throws IOException {
        //测试的XML
//        String xmlStr = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
//                "<datasource>\n" +
//                "    <url>aaaa</url>\n" +
//                "    <username>root</username>\n" +
//                "    <test prefix=\"xxx\">bbbb</test>\n" +
//                "</datasource>";

        InputStream is = Dom4jUtilsTests.class.getClassLoader().getResourceAsStream("dom4jtest/test.xml");
        //1.获取document对象
        Document document = Dom4jUtils.getXMLByInputStream(is);
        //2.获取根节点
        Element root = document.getRootElement();
        //3. 获取根节点的名称
        String rootName = root.getName();
        Assert.assertEquals("datasource", rootName);
        //4. 获取根节点的子节点
        List<Element> elements = Dom4jUtils.getChildElements(root);
        //5. 获取根节点的某个子节点
        Element element1 = Dom4jUtils.getChildElement(root,"url");
        String element1Name = element1.getName();
        Assert.assertEquals("url", element1Name);
        //6. 或者url节点的内容
        String text1 = Dom4jUtils.getText(root,"url");
        Assert.assertEquals("aaaa",text1);
        //7. 获取test节点的prefix内容
        String prefix = Dom4jUtils.getAttribute(Dom4jUtils.getChildElement(root, "test"),"prefix" );
        Assert.assertEquals("xxx", prefix);
        is.close();
    }

    /**
     * 测试构建一个XML，并写入文件
     * */
    @Test
    @Ignore
    public void writeXML() throws IOException {
        //1 构建一个默认的document
        Document document = new DefaultDocument();
        //2. 构建根节点
        Element root = document.addElement("root");
        //3. 添加attribute
        root.addAttribute("prefix", "aaa");
        //4. 添加子节点
        Element child = root.addElement("child");
        //5. 添加子节点的text
        child.setText("i am child");
        //6. 将document转为XML 字符串
        String xmlStr = Dom4jUtils.documentToString(document, "utf-8");
        System.out.println(xmlStr);
        //7. 将document存入file
        Dom4jUtils.writeXMLToFile(document, "d:/test.xml");
    }

}
