package org.feixu.geer

import com.alibaba.fastjson.JSON
import org.apache.pdfbox.contentstream.PDContentStream
import org.apache.pdfbox.cos.COSBase
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSString
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import java.nio.charset.StandardCharsets

@SpringBootTest
class GeerApplicationTests {

    @Test
    void contextLoads() {

        def source = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test.pdf"
        File file = new File(source)
        PDDocument doc = PDDocument.load(file)

        fillFormField1(doc)

        def target = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test5.pdf"
        File tf = new File(target)
        doc.save(tf)
        doc.close()
    }

    static def fillFormField(PDDocument doc) {
        int pageNum = 0;
        Iterator var3 = doc.getPages().iterator();

        while(var3.hasNext()) {
            PDPage page = (PDPage)var3.next();
            ++pageNum;
            page.annotations.eachWithIndex { PDAnnotation entry, int i ->
                println entry.class
                if (entry instanceof PDAnnotationWidget) {
                    PDAnnotationWidget annotationWidget = (PDAnnotationWidget)entry;
                    COSDictionary dictionary = annotationWidget.getCOSObject()
                    // dictionary.getString("T")
                    // dictionary.getString("V")
                    COSString text = (COSString)dictionary.getDictionaryObject("V")
                    text.setValue("123".getBytes(StandardCharsets.UTF_8.toString()))
                    dictionary.setItem("V", text)
                }
            }
        }
    }

    static def fillFormField1(PDDocument doc) {
        int pageNum = 0;
        Iterator var3 = doc.getPages().iterator();

        while(var3.hasNext()) {
            PDPage page = (PDPage)var3.next();
            ++pageNum;
            page.contentStreams.each {
                if (it instanceof PDContentStream) {
                    PDContentStream pdContentStream = (PDContentStream)it
                }
            }
        }
    }
}
