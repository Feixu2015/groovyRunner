package org.feixu.geer.biz.service

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.RandomUtils
import org.apache.pdfbox.multipdf.LayerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
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

import java.awt.Color
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
                                        value = cell?.getDateCellValue().format('yyyy-MM-dd')
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
            reportAddUserInfo(doc, 0, report)

            // 2. 综合详述
            // 2.1 绘制图表
            drawHistogram(doc, 1, report)
            def riskLevelCount = [
                    report.organs.stream().filter({ it -> it.riskLevel > 15 && it.riskLevel <= 20 }).count(),
                    report.organs.stream().filter({ it -> it.riskLevel > 10 && it.riskLevel <= 15 }).count(),
                    report.organs.stream().filter({ it -> it.riskLevel > 5 && it.riskLevel <= 10 }).count(),
                    report.organs.stream().filter({ it -> it.riskLevel <= 5 }).count(),
            ]
            // 2.2 各个风险级别的数量
            fillRiskLevelNum(doc, 1, riskLevelCount as int[])
            // 2.3 详细信息
            fillDetailInfo(doc, 2, report, riskLevelCount as int[])

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
    private void reportAddUserInfo(PDDocument doc, int pageNo, ReportInfo report) {
        UserInfo user = report.userInfo;
        def lineSpaceHeight = 19
        // 姓名
        addText(doc, pageNo, user.name, [260, 412 - lineSpaceHeight * 0] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 出生年月
        addText(doc, pageNo, user.birthday, [260, 412 - lineSpaceHeight * 1] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 性别/年龄
        addText(doc, pageNo, "${user.sex.description}/${user.getAge()}", [260, 412 - lineSpaceHeight * 2] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 委托机构
        addText(doc, pageNo, user.agent, [260, 412 - lineSpaceHeight * 3] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 登记编号
        addText(doc, pageNo, user.number, [260, 412 - lineSpaceHeight * 4] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 登记日期
        addText(doc, pageNo, report.registerDate, [260, 412 - lineSpaceHeight * 5] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
        // 报告日期
        addText(doc, pageNo, report.reportDate, [260, 412 - lineSpaceHeight * 6] as float[], 12f, [0.584f, 0.596f, 0.618f] as float[])
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
    private def addText(PDDocument doc, int pageNo, String message, float[] position, float fontSize, float[] nonStrokingColor) {
        if (StringUtils.isBlank(message)) {
            log.warn("nothing to write")
            return
        }
        File file = ResourceUtils.getFile("classpath:meterial/Template_空白页.pdf")
        PDDocument targetDoc = PDDocument.load(file)
        PDPageContentStream cs = new PDPageContentStream(targetDoc, targetDoc.getPage(0))
        //def ttfPath = '/Users/idcos/Downloads/字体/msyh.ttf'
        File fontFile = ResourceUtils.getFile("classpath:meterial/msyh.ttf")
        PDFont font = PDType0Font.load(targetDoc, fontFile)
        cs.beginText()

        cs.newLineAtOffset(position[0], position[1])
        cs.setFont(font, fontSize)
        cs.setStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
        cs.setNonStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
        cs.showText(message)
        cs.endText()

        cs.close()

        LayerUtility layerUtility = new LayerUtility(doc)
        PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, 0)
        AffineTransform affineTransform = new AffineTransform()
        layerUtility.appendFormAsLayer(doc.getPage(pageNo), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        targetDoc.close()
    }

    /**
     *
     * @param doc
     * @param pageNo
     * @return
     */
    private def drawHistogram(PDDocument doc, int pageNo, ReportInfo report) {
        /*File file = new File("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/Template_综合详解.pdf")
        PDDocument doc = PDDocument.load(file)*/
        PDPage page = doc.getPage(pageNo)
        // 疾病的风险等级
        List<Organ> organs = report.getOrgans()
        println "total: ${organs.size()}"
        def riskLevelColorMap = [
                '阴性': new Color(173, 170, 153),
                '注意': new Color(245, 188, 30),
                '警告': new Color(103, 63, 98),
                '阳性': new Color(228, 58, 60)
        ]
        organs.eachWithIndex { organ, index ->
            println "$organ.name $organ.riskLevel"
            // 对号
            def checkX = 100f
            def checkY = 350f
            def riskLevel = organ ? organ.riskLevel : 0
            def level = Organ.getRiskDesc(riskLevel)
            def fileName = "classpath:meterial/disease_img/${level}-${organ.name}.png"
            addImageFromResource(fileName, doc, page, [checkX + (24 * index), 843.5 - checkY] as float[], 0.3f, 0.3f)
            if (riskLevel > 0) {
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

    /**
     *
     * @param imagePath
     * @param doc
     * @param page
     * @param position [ x, y ] as float[]
     * @param scaleWidth 0.3
     * @param scaleHeight 0.3
     * @return
     */
    private def addImageFromResource(String imagePath, PDDocument doc, PDPage page, float[] position, float scaleWidth = 1.0F, float scaleHeight = 1.0F) {
        File image = ResourceUtils.getFile(imagePath)
        PDImageXObject pdImage = PDImageXObject.createFromFileByContent(image, doc)
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.drawImage(pdImage, position[0], position[1], (float) (pdImage.getWidth() * scaleWidth), (float) (pdImage.getHeight() * scaleHeight))
        contentStream.close()
    }

    private def addImageFromPath(String image, PDDocument doc, PDPage page, float[] position, float scaleWidth = 1.0F, float scaleHeight = 1.0F) {
        PDImageXObject pdImage = PDImageXObject.createFromFile(image, doc)
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)
        contentStream.drawImage(pdImage, position[0], position[1], (float) (pdImage.getWidth() * scaleWidth), (float) (pdImage.getHeight() * scaleHeight))
        contentStream.close()
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
    private def addRectangle(PDDocument doc, PDPage page, Color color, float[] rectangle, PDPageContentStream.AppendMode appendMode = PDPageContentStream.AppendMode.APPEND) {
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

    /**
     * 填充风险级别数量统计
     *
     * @param doc
     * @param pageNo
     * @param riskLevels [ 阳性, 警告, 注意, 阴性 ] as int[]
     * @return
     */
    private def fillRiskLevelNum(PDDocument doc, int pageNo = 0, int[] riskLevels) {
        addText(doc, pageNo, riskLevels[0].toString(), [135 - (riskLevels[0] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.953f, 0.267f, 0.235f] as float[])
        addText(doc, pageNo, riskLevels[1].toString(), [135 + 100 - (riskLevels[1] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.463f, 0.294f, 0.443f] as float[])
        addText(doc, pageNo, riskLevels[2].toString(), [135 + 200 - (riskLevels[2] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.957f, 0.725f, 0.078] as float[])
        addText(doc, pageNo, riskLevels[3].toString(), [135 + 300 - (riskLevels[3] > 9 ? 15 : 0), 366] as float[], 50.02f, [0.686f, 0.667f, 0.600f] as float[])
    }

    private def fillDetailInfo(PDDocument doc, int pageNo, ReportInfo report, int[] riskCount) {
        UserInfo user = report.userInfo
        // 姓名
        addText(doc, pageNo, user.name, [78, 720] as float[], 21.5f, [0.408f, 0.255f, 0.384f] as float[])
        def countY = 683
        // 阳性
        addText(doc, pageNo, "阳性 ${riskCount[0]} 个", [278, countY] as float[], 11.5f, [0.953f, 0.267f, 0.235f] as float[])
        // 警告
        addText(doc, pageNo, "警告 ${riskCount[1]} 个", [337, countY] as float[], 11.5f, [0.463f, 0.294f, 0.443f] as float[])
        // 注意
        addText(doc, pageNo, "注意 ${riskCount[2]} 个", [405, countY] as float[], 11.5f, [0.957f, 0.725f, 0.078f] as float[])

        def checkY1 = 580
        // 身高
        addText(doc, pageNo, "${user.height.toString()}cm", [146, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 吸烟
        addText(doc, pageNo, user.smokeLevel , [299, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 高血压
        addText(doc, pageNo, user.hypertension, [448, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])

        def checkY2 = 555
        // 体重
        addText(doc, pageNo, "${user.weight.toString()}kg", [146, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 饮酒
        addText(doc, pageNo, user.drinkWineLevel, [299, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 糖尿病
        addText(doc, pageNo, user.diabetes, [448, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
    }
}
