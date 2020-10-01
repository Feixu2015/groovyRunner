package org.feixu.geer

import com.alibaba.fastjson.JSON
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.RandomUtils
import org.apache.pdfbox.contentstream.PDContentStream
import org.apache.pdfbox.contentstream.operator.Operator
import org.apache.pdfbox.cos.COSBase
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSString
import org.apache.pdfbox.multipdf.LayerUtility
import org.apache.pdfbox.pdfwriter.ContentStreamWriter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDPageTree
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.util.Matrix
import org.apache.pdfbox.util.Vector
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import org.feixu.geer.enums.SexEnum
import org.feixu.geer.model.Organ
import org.feixu.geer.model.ReportInfo
import org.junit.jupiter.api.Test

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

class GeerTest {
    private pdfs = []

    def generateIt() {
        def basePath = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/"
        File file = new File(basePath, "用户信息.pdf")
        PDDocument doc = PDDocument.load(file)
        pdfs << doc

        PDPageTree pdPageTree = getPage(basePath, "综合详解.pdf")

        pdPageTree.each {
            doc.addPage(it)
        }

        doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test1.pdf")
        pdfs.each {
            it.close()
        }
    }

    PDPageTree getPage(path, name) {
        File file = new File(path, name)
        PDDocument doc = PDDocument.load(file)
        pdfs << doc
        PDPageTree pDPageTree = doc.getPages()
        return pDPageTree
    }

    void deletePage() {
        def basePath = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/"
        File file = new File(basePath, "test.pdf")
        PDDocument doc = PDDocument.load(file)

        doc.removePage(2)
        doc.save(basePath + "test3.pdf")
        doc.close()
    }

/**
 * 添加颜色矩形
 *
 * @param doc
 * @param page
 * @param color
 * @param rectangle [200, 650, 100, 100] as float[]
 * @return
 */
    def addRectangle(PDDocument doc, PDPage page, Color color, float[] rectangle, PDPageContentStream.AppendMode appendMode = PDPageContentStream.AppendMode.APPEND) {
        //Instantiating the PDPageContentStream class
        PDPageContentStream contentStream = new PDPageContentStream(doc,
                page, appendMode, false)

        //Setting the non stroking color
        contentStream.setNonStrokingColor(color)

        //Drawing a rectangle
        contentStream.addRect(rectangle[0], rectangle[1], rectangle[2], rectangle[3])

        //Drawing a rectangle
        contentStream.fill()

        //Closing the ContentStream object
        contentStream.close()
    }

    def addText(PDDocument doc, PDPage page, Color color, float[] rectangle) {
        //Instantiating the PDPageContentStream class
        PDPageContentStream contentStream = new PDPageContentStream(doc,
                page, PDPageContentStream.AppendMode.APPEND, false)

        //Setting the non stroking color
        contentStream.setNonStrokingColor(color)

        //Drawing a rectangle
        contentStream.addRect(rectangle[0], rectangle[1], rectangle[2], rectangle[3])

        //Drawing a rectangle
        contentStream.fill()

        //Closing the ContentStream object
        contentStream.close()
    }

    def addImage(String image, PDDocument doc, PDPage page, float[] position, float scaleWidth = 1.0F, float scaleHeight = 1.0F) {
        PDImageXObject pdImage = PDImageXObject.createFromFile(image, doc)
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.drawImage(pdImage, position[0], position[1], (float) (pdImage.getWidth() * scaleWidth), (float) (pdImage.getHeight() * scaleHeight))
        contentStream.close()
    }

// 基因检测表格填充
    def openAndSaveToNew() {
        File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test2.pdf")
        PDDocument doc = PDDocument.load(file)
        doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test3.pdf")
        PDPage page = doc.getPage(0)
        // 842.5
        // 595.5
        // 抑癌基因甲基化检查结果
        // 填充行
        30.times { y ->
            // 对号
            def checkX = 135f
            def checkY = 111f
            addImage("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/对号.png", doc, page,
                    [-0.5 + checkX, 843.5 - 12 - checkY - (10 + 2.368) * y] as float[], 0.3f)
            // 对应疾病
            // 填充列
            18.times { x ->
                def diseaseX = 160f
                def diseaseY = 111f
                addRectangle(doc, page, Color.RED, [-0.5 + diseaseX + (18.1 + 2.368) * x, 843.5 - 10 - diseaseY - (10 + 2.368) * y, 18.2, 10] as float[])
            }
        }

        // 癌症相关基因突变检查的结果
        19.times { y ->
            // 对号
            def checkX = 135f
            def checkY = 546.62f
            addImage("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/对号.png", doc, page,
                    [-0.5 + checkX, 843.5 - 12 - checkY - (10 + 2.368) * y] as float[], 0.3f)
            // 疾病
            18.times { x ->
                def diseaseX = 160f
                def diseaseY = 546.62f
                addRectangle(doc, page, Color.RED, [-0.5 + diseaseX + (18.1 + 2.368) * x, 843.5 - 10 - diseaseY - (10 + 2.368) * y, 18.2, 10] as float[])
            }
        }

        doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test3.pdf")
        doc.close()
    }

// 基因主要功能背景填充
    def drawBg() {
        //File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/空白页.pdf")

        File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test2.pdf")
        PDDocument doc = PDDocument.load(file)

        // 842.5
        // 595.5

        PDPage page = doc.getPage(1)
        // 抑癌基因的主要功能
        def twoLineCount = 0
        30.times { y ->
            def diseaseX = 55.5f
            def diseaseY = 98f
            def height = 11f
            def twoLine = [20].contains(y)
            if (twoLine) {
                height = 23.2f
                twoLineCount++
            }
            addRectangle(doc, page, new Color(248, 209, 212),
                    [
                            diseaseX,
                            843.5 - 13 - diseaseY - ((10 + 2.368) * (y - twoLineCount) + (10 + 2.368) * 2 * twoLineCount),
                            473,
                            height
                    ] as float[], PDPageContentStream.AppendMode.PREPEND)
        }

        // 癌症相关基因的主要功能
        def geneMutation = [
                'p53 c.524G>A'    : [index: 1, row: 4],
                'p53 c.743G>A'    : [index: 2, row: 4],
                'p53 c.747G>T'    : [index: 3, row: 4],
                'p53 c.817C>T'    : [index: 4, row: 4],
                'PIK3CA c.1624G>A': [index: 1, row: 3],
                'PIK3CA c.1633G>A': [index: 2, row: 3],
                'PIK3CA c.3140A>G': [index: 3, row: 3],
                'KRAS c.34G>T'    : [index: 1, row: 4],
                'KRAS c.35G>A'    : [index: 2, row: 4],
                'KRAS c.35G>T'    : [index: 3, row: 4],
                'KRAS c.38G>A'    : [index: 4, row: 4],
                'PTEN c.388C>G'   : [index: 1, row: 3],
                'PTEN c.389G>A'   : [index: 2, row: 3],
                'PTEN c.697C>T'   : [index: 3, row: 3],
                'APC c.4348C>T'   : [index: 1, row: 1],
                'ATM c.1009C>T'   : [index: 1, row: 1],
                'BRAF c.1799T>A'  : [index: 1, row: 1],
                'IDH1 c.395G>A'   : [index: 1, row: 1],
                'RET c.2753T>C'   : [index: 1, row: 1],
        ]
        geneMutation.eachWithIndex { it, y ->
            def diseaseX = 55.5f
            def diseaseY = 536f
            def height = it.value.row > 1 ? 12.2f : 11.2f
            def indexInGroup = it.value.index
            def groupHeight = it.value.row * height
            addRectangle(doc, page, new Color(248, 209, 212),
                    [
                            diseaseX,
                            843.5 - 13 - diseaseY - (10 + 2.368) * y,
                            70,
                            height
                    ] as float[], PDPageContentStream.AppendMode.PREPEND)

            if (it.value.row != 1) {
                addRectangle(doc, page, new Color(248, 209, 212),
                        [
                                diseaseX + 70,
                                843.5 - 13 - diseaseY - (10 + 2.368) * (y + (it.value.row - indexInGroup)),
                                403.5f,
                                groupHeight
                        ] as float[], PDPageContentStream.AppendMode.PREPEND)
            }
        }

        doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test4.pdf")
        doc.close()
    }

// add page content as a layout
    def mergePageObjects() {
        def docs = []
        File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test2.pdf")
        PDDocument doc = PDDocument.load(file)
        docs << doc
        doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test3.pdf")

        PDDocument doc2 = PDDocument.load(new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test4.pdf"))
        docs << doc2

        LayerUtility layerUtility = new LayerUtility(doc2)
        PDFormXObject firstForm = layerUtility.importPageAsForm(doc, 1)
        AffineTransform affineTransform = new AffineTransform()

        layerUtility.appendFormAsLayer(doc2.getPage(0), firstForm, affineTransform, "external page")

        doc2.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test4.pdf")

        docs.each {
            doc.close()
        }
    }

    def readText() {
        def docs = []
        File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test3.pdf")
        PDDocument doc = PDDocument.load(file)
        docs << doc

        def page = doc.getPage(0)
        println JSON.toJSONString(page, true)

        PDFTextStripper pdfTextStripper = new PDFTextStripper()
        pdfTextStripper.setSortByPosition(true)
        pdfTextStripper.setStartPage(1)
        pdfTextStripper.setEndPage(1)
        String test = pdfTextStripper.getText(doc)
        println test
        //println JSON.toJSONString(page, true)

        docs.each {
            it.close()
        }
    }

// final run
//generateIt()
//deletePage()
//drawBg()
//mergePageObjects()

    def excelColumns() {
        def setDisease = { ReportInfo report, String colName, def value ->
            Organ organ = report.organs.find { it.name == colName }
            organ.riskLevel = value ? Integer.valueOf(value) : 0
        }

        def setTumorSuppressorGene = { ReportInfo report, String colName, def value ->
            def gene = report.getTumorSuppressorGeneList().find { it.name == colName }
            gene.result = null != value && Math.round(value) > 0
        }

        def setGeneMutations = { ReportInfo report, String colName, def value ->
            def geneMutations = report.getCancerRelatedGeneMutations().find { it.name == colName }
            geneMutations.result = null != value && Math.round(value) > 0
        }

        [
                '受检者'             :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.name = value
                                }
                        ],
                '出生年月'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.birthday = value
                                }
                        ],
                '性别'              :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    SexEnum sex = SexEnum.getByDesc(value)
                                    report.userInfo.sex = sex
                                    // 初始化
                                    report.organs = ReportInfo.getReportOrgansBySex(sex)
                                }
                        ],
                '登记编号'            :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.number = value
                                }
                        ],
                '登记日期'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    report.registerDate = value
                                }
                        ],
                '报告日期'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    report.reportDate = value
                                }
                        ],
                '乳房'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '卵巢'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '宫颈'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '子宫内膜'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '前列腺'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胃'               :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '大肠'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肺'               :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肝'               :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '甲状腺'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '膀胱'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肾脏'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胰腺'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '食道'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胆囊'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '脑'               :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '淋巴'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '神经胶质'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '身高'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.height = (Math.round(Double.valueOf(value) / 100) * 100) / 100
                                }
                        ],
                '体重'              :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.weight = (Math.round(Double.valueOf(value) / 100) * 100) / 100
                                }
                        ],
                '是否吸烟'            :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.smokeLevel = '无' == value ? 0 : 1
                                }
                        ],
                '饮酒频度'            :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.drinkWineLevel = '无' == value ? 0 : 1
                                }
                        ],
                '高血压'             :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = '无' == value
                                }
                        ],
                '糖尿病'             :
                        [
                                cellType: CellType.STRING,
                                field: { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = '无' == value
                                }
                        ],
                'BMI'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    // dynamic calc
                                }
                        ],
                'APAF1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'APC'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'BRCA1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'CDH1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'CDH13'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'DAPK'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'DLEC1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'ER-a'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'ER-b'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'FHIT'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'GSTP1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'HIC1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'hMLH1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'LKB1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MGMT'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MINT1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MINT31'          :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MYOD1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p14ARF'          :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p15'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p16'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'PTEN'            :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'PYCARD'          :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'RASSF1A'         :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'RUNX3'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'SLC5A8'          :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'SOCS1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'TIMP3'           :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'VHL'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'WT1'             :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p53 c.524G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.743G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.747G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.817C>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.1624G>A':
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.1633G>A':
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.3140A>G':
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.34G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.35G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.35G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.38G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PTEN c.388C>G'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PTEN c.389G>A'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    value
                                }
                        ],
                'PTEN c.697C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'APC c.4348C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'ATM c.1009C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'BRAF c.1799T>A'  :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'IDH1 c.395G>A'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'RET c.2753T>C'   :
                        [
                                cellType: CellType.NUMERIC,
                                field: { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
        ]
    }

    def readExcel() {
        List<ReportInfo> reportInfoList = new ArrayList<>()
        def colAndTypeMap = excelColumns()
        def dateColNames = ['出生年月', '登记日期', '报告日期']
        InputStream inp = null
        try {
            inp = new FileInputStream("/Users/idcos/Downloads/zhoulinxian/20200930/检测数据.xlsx")
            //InputStream inp = new FileInputStream("workbook.xlsx")
            Workbook wb = XSSFWorkbookFactory.create(inp)
            Sheet sheet = wb.getSheetAt(0)
            // read each row
            sheet.collect().eachWithIndex { row, index ->
                // read each cell
                def userName = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                println "First Cell Value:$userName"
                if (null != userName && StringUtils.isNotBlank(userName)) {
                    println "===Row ${index + 1}==="
                    ReportInfo report = new ReportInfo()
                    80.times {
                        Cell cell = row.getCell(it, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        println "cell is:$cell"
                        def colName = colAndTypeMap.keySet()[it]
                        def fieldMap = colAndTypeMap[colName].field
                        def value = cell?.toString()
                        println "value is:$value"
                        if (StringUtils.isNotBlank(value)) {
                            if (0 == index) {
                                value = cell.getStringCellValue()
                            } else {
                                try {
                                    if (CellType.NUMERIC == colAndTypeMap[colName].cellType) {
                                        if (dateColNames.contains(colName)) {
                                            value = cell?.getDateCellValue().format('yyyy-MM-dd HH:mm:ss')
                                        } else {
                                            value = cell?.getNumericCellValue()
                                        }
                                    } else {
                                        value = cell?.getStringCellValue()
                                    }
                                } catch (e) {
                                    println e
                                    value = null
                                }
                            }
                        }
                        println value
                        fieldMap(report, colName, value)
                    }
                    println ""
                    reportInfoList.add(report)
                }
            }
        } catch (e) {
            println e
        } finally {
            if (null != inp) {
                try {
                    inp.close()
                } catch (ex) {

                }
            }
        }
        reportInfoList
    }

// 基因检测表格填充
    def drawChart(PDDocument doc, int pageNo) {
        /*File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/Template_综合详解.pdf")
        PDDocument doc = PDDocument.load(file)*/
        PDPage page = doc.getPage(pageNo)
        // 疾病的风险等级
        List<Organ> organs = ReportInfo.getReportOrgansBySex(SexEnum.female)
        organs.each {
            it.riskLevel = RandomUtils.nextInt(20) + 1
        }
        println "total: ${organs.size()}"
        def riskLevelColorMap = [
                '阴性': new Color(173, 170, 153),
                '注意': new Color(245, 188, 30),
                '警告': new Color(103, 63, 98),
                '阳性': new Color(228, 58, 60)
        ]
        ReportInfo.getAllOrganNames().eachWithIndex { organName, index ->
            def organ = organs.find { organName == it.name }
            // 对号
            def checkX = 100f
            def checkY = 350f
            def riskLevel = organ ? organ.riskLevel : 0
            def level = Organ.getRiskDesc(riskLevel)
            def fileName = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/疾病图片/${level}-${organName}.png".toString()
            addImage(fileName, doc, page, [checkX + (24 * index), 843.5 - checkY] as float[], 0.3f, 0.3f)
            if (organ) {
                println "$organName $level"
                // 绘制柱状图
                Color columnColor = riskLevelColorMap[level]
                def colWidth = 16
                addRectangle(doc, page, columnColor,
                        [checkX + 3.7 + ((colWidth + 7.93) * index), 843.5 + 24.75 - checkY, colWidth, riskLevel * 10.72] as float[],
                        PDPageContentStream.AppendMode.APPEND)
            }
        }

        /*File f = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/result_综合详解.pdf")
        doc.save(f.getAbsolutePath())
        doc*/
    }

// 裁剪图片
    def clipImage() {
        new File('/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/疾病图片/').eachDir {
            it.eachFile {
                if (it.name.endsWith('.png')) {
                    // 读取图片
                    BufferedImage bufImage = ImageIO.read(it)
                    BufferedImage subImage = bufImage.getSubimage(0, 0, bufImage.getWidth(), (int) (bufImage.getHeight() / 2) + 8)
                    ImageIO.write(subImage, "png", new File("/tmp/abc/${it.name}"))
                }
            }
        }
    }

// 字符串替换
    def replaceText(File f) {
        PDDocument doc = PDDocument.load(f)
        for (PDPage page : doc.getDocumentCatalog().getPages()) {
            PdfContentStreamEditor editor = new PdfContentStreamEditor(doc, page) {
                final StringBuilder recentChars = new StringBuilder()
                PDFont lastFont = null

                @Override
                protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, Vector displacement)
                        throws IOException {
                    String str = font.toUnicode(code)
                    println "string is: $str"
                    if (str != null) {
                        recentChars.append(str)
                        lastFont = font
                    }

                    super.showGlyph(textRenderingMatrix, font, code, displacement)
                }

                @Override
                protected void write(ContentStreamWriter contentStreamWriter, Operator operator, List<COSBase> operands) throws IOException {
                    String recentText = recentChars.toString()
                    recentChars.setLength(0)
                    String operatorString = operator.getName()

                    println "recentText: $recentText"
                    if (TEXT_SHOWING_OPERATORS.contains(operatorString) && "[P]".equals(recentText)) {
                        COSString cosString = operands[0] as COSString
                        cosString.setValue("计算机".getBytes())
                    }

                    super.write(contentStreamWriter, operator, operands)
                }

                final List<String> TEXT_SHOWING_OPERATORS = Arrays.asList("Tj", "'", "\"", "TJ")
            }
            editor.processPage(page)
        }
        doc.save(f.getAbsolutePath())
        doc.close()
    }

    def walkDocument(PDDocument doc) {
        doc.getPages().each { page ->
            page.contentStreams.each {
                if (it instanceof PDPageContentStream) {
                    PDPageContentStream pageContentStream = (PDPageContentStream) it
                    pageContentStream.toString()
                }
            }
        }
    }

    def writeText(PDDocument doc, PDPage page, String message, PDFont font, float fontSize, float centerX, float centerY, double rotateTheta = 0D) {
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.beginText()
        contentStream.setFont(font, fontSize)
        contentStream.setNonStrokingColor(Color.red)
        if (rotateTheta) {
            contentStream.setTextMatrix(Matrix.getRotateInstance(rotateTheta, centerX, centerY))
        } else {
            contentStream.setTextMatrix(Matrix.getTranslateInstance(centerX, centerY))
        }

        contentStream.showText(message)
        contentStream.endText()
        contentStream.close()
    }

    def creatForm() {
        PDDocument document = new PDDocument()
        PDPage page = new PDPage(PDRectangle.A4)
        document.addPage(page)
        PDFont font = PDType1Font.HELVETICA
        PDResources resources = new PDResources()
        resources.put(COSName.getPDFName("Helv"), font)
        PDAcroForm acroForm = new PDAcroForm(document)
        document.getDocumentCatalog().setAcroForm(acroForm)
        acroForm.setDefaultResources(resources)
        String defaultAppearanceString = "/Helv 0 Tf 0 g"
        acroForm.setDefaultAppearance(defaultAppearanceString)
        PDTextField textBox = new PDTextField(acroForm)
        textBox.setPartialName("SampleField")
        defaultAppearanceString = "/Helv 12 Tf 0 0 1 rg"
        textBox.setDefaultAppearance(defaultAppearanceString)
        acroForm.getFields().add(textBox)
        PDAnnotationWidget widget = (PDAnnotationWidget) textBox.getWidgets().get(0)
        PDRectangle rect = new PDRectangle(50.0F, 750.0F, 200.0F, 50.0F)
        widget.setRectangle(rect)
        widget.setPage(page)
        PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary())
        fieldAppearance.setBorderColour(new PDColor([0.0F, 1.0F, 0.0F] as float[], PDDeviceRGB.INSTANCE))
        fieldAppearance.setBackground(new PDColor([1.0F, 1.0F, 0.0F] as float[], PDDeviceRGB.INSTANCE))
        widget.setAppearanceCharacteristics(fieldAppearance)
        widget.setPrinted(true)
        page.getAnnotations().add(widget)
        textBox.setValue("Sample field content")
        PDPageContentStream cs = new PDPageContentStream(document, page)
        cs.beginText()
        cs.setFont(PDType1Font.HELVETICA, 15.0F)
        cs.newLineAtOffset(50.0F, 810.0F)
        cs.showText("Field:")
        cs.endText()
        cs.close()
        document.save("target/SimpleForm.pdf")
        document.close()
    }

    def fillFormField(PDDocument doc) {
        int pageNum = 0
        Iterator var3 = doc.getPages().iterator()

        while (var3.hasNext()) {
            PDPage page = (PDPage) var3.next()
            ++pageNum
            page.contentStreams.each {
                if (it instanceof PDContentStream) {
                    PDContentStream pdContentStream = (PDContentStream) it
                }
            }
        }
    }

    def addText(PDDocument doc, int pageNo, String message, float[] position, float fontSize, float[] nonStrokingColor) {
        def source = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/空白页.pdf"
        File file = new File(source)
        PDDocument targetDoc = PDDocument.load(file)
        PDPageContentStream cs = new PDPageContentStream(targetDoc, targetDoc.getPage(0))
        def ttfPath = '/Users/idcos/Downloads/字体/msyh.ttf'
        PDFont font = PDType0Font.load(targetDoc, new File(ttfPath))
        cs.beginText()
        cs.newLineAtOffset(position[0], position[1])
        cs.setFont(font, fontSize)
        cs.setNonStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
        cs.showText(message)
        cs.endText()
        cs.close()
        File target = new File("/tmp/${UUID.randomUUID().toString()}.pdf")
        targetDoc.save(target.getAbsolutePath())

        LayerUtility layerUtility = new LayerUtility(doc)
        PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, pageNo)
        AffineTransform affineTransform = new AffineTransform()
        layerUtility.appendFormAsLayer(doc.getPage(0), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        targetDoc.close()
    }

// 填充风险级别
    def fillRiskLevelNum(PDDocument doc, int pageNo = 0, int[] riskLevels) {
        addText(doc, pageNo, riskLevels[0].toString(), [135 - (riskLevels[0] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.953f, 0.267f, 0.235f] as float[])
        addText(doc, pageNo, riskLevels[1].toString(), [135 + 100 - (riskLevels[1] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.463f, 0.294f, 0.443f] as float[])
        addText(doc, pageNo, riskLevels[2].toString(), [135 + 200 - (riskLevels[2] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.957f, 0.725f, 0.078] as float[])
        addText(doc, pageNo, riskLevels[3].toString(), [135 + 300 - (riskLevels[3] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.686f, 0.667f, 0.600f] as float[])
    }

    def createReport() {
        def source = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/Template_综合详解.pdf"
        File file = new File(source)
        PDDocument doc = PDDocument.load(file)

// 综合详情 1
// 绘制图表
        drawChart(doc, 0)

// 数量
        fillRiskLevelNum(doc, 0, [13, 22, 14, 16] as int[])

// 综合详情 2


        def target = "/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test5.pdf"
        File tf = new File(target)
        doc.save(tf)
        doc.close()
    }

    @Test
    public void generateReport() {
        readExcel()
    }
}
