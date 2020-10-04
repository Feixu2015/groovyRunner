package org.feixu.geer.biz.service

import org.apache.commons.lang.StringUtils
import org.apache.pdfbox.multipdf.LayerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import org.feixu.geer.enums.SexEnum
import org.feixu.geer.model.CancerRelatedGeneMutations
import org.feixu.geer.model.Organ
import org.feixu.geer.model.ReportInfo
import org.feixu.geer.model.ReportResult
import org.feixu.geer.model.TumorSuppressorGene
import org.feixu.geer.model.UserInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils

import java.awt.geom.AffineTransform

@Service
class ReportService {
    private static Logger log = LoggerFactory.getLogger(ReportService.class)

    private def excelColumns() {
        def setDisease = { ReportInfo report, String colName, def value ->
            Organ organ = report.organs.find { it.name == colName }
            organ.riskLevel = value || (value instanceof String && StringUtils.isNotBlank(value)) ?
                    Double.valueOf(value).intValue() : 0
        }

        def setTumorSuppressorGene = { ReportInfo report, String colName, def value ->
            def gene = new TumorSuppressorGene(
                    name: colName,
                    result: null != value || (value instanceof String && StringUtils.isNotBlank(value)) ?
                            Double.valueOf(value).intValue() : 0
            )
            report.getTumorSuppressorGeneList().add(gene)
        }

        def setGeneMutations = { ReportInfo report, String colName, def value ->
            def geneMutation = new CancerRelatedGeneMutations(
                    name: colName,
                    result: null != value || (value instanceof String && StringUtils.isNotBlank(value)) ?
                            Double.valueOf(value).intValue() : 0
            )
            report.getCancerRelatedGeneMutations().add(geneMutation)
        }

        [
                '受检者'             :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.name = value
                                }
                        ],
                '出生年月'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.birthday = value
                                }
                        ],
                '性别'              :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    SexEnum sex = SexEnum.getByDesc(value)
                                    report.userInfo.sex = sex
                                    // 初始化
                                    report.organs = ReportInfo.getAllOrganNames().collect {
                                        new Organ(name: it)
                                    }
                                }
                        ],
                '登记编号'            :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.number = value
                                }
                        ],
                '登记日期'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.registerDate = value
                                }
                        ],
                '报告日期'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.reportDate = value
                                }
                        ],
                '乳房'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '卵巢'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '宫颈'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '子宫内膜'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '前列腺'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胃'               :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '大肠'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肺'               :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肝'               :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '甲状腺'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '膀胱'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '肾脏'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胰腺'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '食道'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '胆囊'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '脑'               :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '淋巴'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '神经胶质'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setDisease(report, colName, value)
                                }
                        ],
                '身高'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.height = new BigDecimal(value.toString()).round(2).doubleValue()
                                }
                        ],
                '体重'              :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.weight = new BigDecimal(value.toString()).round(2).doubleValue()
                                }
                        ],
                '是否吸烟'            :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.smokeLevel = '无' == value ? 0 : 1
                                }
                        ],
                '饮酒频度'            :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.drinkWineLevel = '无' == value ? 0 : 1
                                }
                        ],
                '高血压'             :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = '无' == value
                                }
                        ],
                '糖尿病'             :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = '无' == value
                                }
                        ],
                'BMI'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    // dynamic calc
                                }
                        ],
                'APAF1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'APC'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'BRCA1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'CDH1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'CDH13'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'DAPK'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'DLEC1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'ER-a'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'ER-b'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'FHIT'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'GSTP1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'HIC1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'hMLH1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'LKB1'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MGMT'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MINT1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MINT31'          :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'MYOD1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p14ARF'          :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p15'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p16'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'PTEN'            :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'PYCARD'          :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'RASSF1A'         :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'RUNX3'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'SLC5A8'          :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'SOCS1'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'TIMP3'           :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'VHL'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'WT1'             :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setTumorSuppressorGene(report, colName, value)
                                }
                        ],
                'p53 c.524G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.743G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.747G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'p53 c.817C>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.1624G>A':
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.1633G>A':
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PIK3CA c.3140A>G':
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.34G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.35G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.35G>T'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'KRAS c.38G>A'    :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PTEN c.388C>G'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'PTEN c.389G>A'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    value
                                }
                        ],
                'PTEN c.697C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'APC c.4348C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'ATM c.1009C>T'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'BRAF c.1799T>A'  :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'IDH1 c.395G>A'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
                'RET c.2753T>C'   :
                        [
                                cellType: CellType.NUMERIC,
                                field   : { ReportInfo report, String colName, def value ->
                                    setGeneMutations(report, colName, value)
                                }
                        ],
        ]
    }

    private List<ReportInfo> readExcel(File excel) {
        List<ReportInfo> reportInfoList = new ArrayList<>()
        def colAndTypeMap = excelColumns()
        def dateColNames = ['出生年月', '登记日期', '报告日期']
        InputStream inp = null
        try {
            inp = new FileInputStream(excel)
            //InputStream inp = new FileInputStream("workbook.xlsx")
            Workbook wb = XSSFWorkbookFactory.create(inp)
            Sheet sheet = wb.getSheetAt(0)
            // read each row
            sheet.collect().eachWithIndex { row, index ->
                // read each cell
                def userName = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                println "First Cell Value:$userName"
                if (index > 0 && null != userName && StringUtils.isNotBlank(userName)) {
                    println "===Row ${index + 1}==="
                    ReportInfo report = new ReportInfo()
                    80.times {
                        Cell cell = row.getCell(it, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        println "cell is:$cell"
                        def colName = colAndTypeMap.keySet()[it]
                        println "col is:$colName"
                        def fieldMap = colAndTypeMap[colName].field
                        def value = cell?.toString()
                        if (StringUtils.isNotBlank(value)) {
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

    public void createReport(File excel, File targetFolder) {
        // 1. 读取excel
        List<ReportInfo> reports = readExcel(excel)
        // 2. 遍历生成报告
        reports.each {
            produceReportByReportInfo(it, targetFolder)
        }
    }

    public ReportResult produceReportByReportInfo(ReportInfo report, File targetFolder) {
        ReportResult result = new ReportResult()

        try {
            File blankPage = ResourceUtils.getFile("classpath:meterial/Template_空白页.pdf")
            UserInfo user = report.userInfo
            // 1. 用户信息
            File templateFirstPart = ResourceUtils.getFile("classpath:meterial/Template_用户信息_综合详解.pdf")
            PDDocument doc = PDDocument.load(templateFirstPart)
            reportAddUserInfo(doc, 0, user)

            // 2. 综合详述
            // 3. 器官详解
            // 4. 表格填充
            // 5. 保存
            String reportFile = "${targetFolder.getAbsolutePath()}/${user.name}_检测报告_${new Date().format('yyyyMMdd')}.pdf"
            doc.save(reportFile)
            doc.close()

            result.isSuccess = true
        } catch (e) {
            result.message = e.getMessage()
            log.error("Exception: ${e.class} Detail: ${result.message}")
        }

        result
    }

    /**
     *
     *
     * @param doc
     * @param pageNo
     * @param user
     */
    public void reportAddUserInfo(PDDocument doc, int pageNo, UserInfo user) {
        // 姓名
        addText(doc, pageNo, user.name, [260, 412] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
    }

    /**
     *
     * @param doc
     * @param pageNo
     * @param message
     * @param position [ x, y ] as float[]
     * @param fontSize
     * @param nonStrokingColor [ r, g, b ] as float[]
     * @return
     */
    def addText(PDDocument doc, int pageNo, String message, float[] position, float fontSize, float[] nonStrokingColor) {
        File file = ResourceUtils.getFile("classpath:meterial/Template_空白页.pdf")
        PDDocument targetDoc = PDDocument.load(file)
        PDPageContentStream cs = new PDPageContentStream(targetDoc, targetDoc.getPage(0))
        //def ttfPath = '/Users/idcos/Downloads/字体/msyh.ttf'
        File fontFile = ResourceUtils.getFile("classpath:meterial/msyh.ttf")
        PDFont font = PDType0Font.load(targetDoc, fontFile)
        cs.beginText()

        cs.newLineAtOffset(position[0], position[1])
        cs.setFont(font, fontSize)
        cs.setNonStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
        cs.showText(message)
        cs.endText()

        cs.close()

        LayerUtility layerUtility = new LayerUtility(doc)
        PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, pageNo)
        AffineTransform affineTransform = new AffineTransform()
        layerUtility.appendFormAsLayer(doc.getPage(0), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        targetDoc.close()
    }
}
