package org.feixu.geer.biz.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.helger.font.api.IHasFontResource
import com.helger.pdflayout4.PageLayoutPDF
import com.helger.pdflayout4.base.PLPageSet
import com.helger.pdflayout4.element.text.PLText
import com.helger.pdflayout4.spec.FontSpec
import com.helger.pdflayout4.spec.PreloadFont
import javafx.application.Application
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.RandomUtils
import org.apache.pdfbox.multipdf.LayerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType3Font
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import org.feixu.geer.GeerApplication
import org.feixu.geer.biz.helper.MyFontResource
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
import java.nio.charset.StandardCharsets
import java.nio.file.Files

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
                            Double.valueOf(value).intValue() > 0 : false
            )
            report.getTumorSuppressorGeneList().add(gene)
        }

        def setGeneMutations = { ReportInfo report, String colName, def value ->
            def geneMutation = new CancerRelatedGeneMutations(
                    name: colName,
                    result: null != value || (value instanceof String && StringUtils.isNotBlank(value)) ?
                            Double.valueOf(value).intValue() > 0 : false
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
                                    report.userInfo.smokeLevel = value
                                }
                        ],
                '饮酒频度'            :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.drinkWineLevel = value
                                }
                        ],
                '高血压'             :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = value
                                }
                        ],
                '糖尿病'             :
                        [
                                cellType: CellType.STRING,
                                field   : { ReportInfo report, String colName, def value ->
                                    report.userInfo.hypertension = value
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
                    log.error(ex.getMessage())
                }
            }
        }
        reportInfoList
    }

    public void createReport(File excel, File targetFolder) {
        // 1. 读取excel
        List<ReportInfo> reports = readExcel(excel)
        log.info('reports is:' + JSON.toJSONString(reports, true))
        // 2. 遍历生成报告
        reports.each {
            produceReportByReportInfo(it, targetFolder)
        }
    }

    private ReportResult produceReportByReportInfo(ReportInfo report, File targetFolder) {
        ReportResult result = new ReportResult()

        try {
            UserInfo user = report.userInfo
            // 1. 用户信息
            File templateFirstPart = ResourceUtils.getFile("classpath:meterial/Template_用户信息_综合详解.pdf")
            PDDocument doc = PDDocument.load(templateFirstPart)
            reportAddUserInfo(doc, 1, report)

            // 2. 综合详述
            // 2.1 绘制图表
            drawHistogram(doc, 2, report)
            List<Organ> checkedOrgans = report.getCheckedOrgans()
            List<Organ> positiveOrgans = checkedOrgans.stream().filter({ it -> it.riskLevel > 15 && it.riskLevel <= 20 }).collect()
            List<Organ> warnOrgans = checkedOrgans.stream().filter({ it -> it.riskLevel > 10 && it.riskLevel <= 15 }).collect()
            List<Organ> attentionOrgans = checkedOrgans.stream().filter({ it -> it.riskLevel > 5 && it.riskLevel <= 10 }).collect()
            List<Organ> negativeOrgans = checkedOrgans.stream().filter({ it -> it.riskLevel <= 5 }).collect()
            def riskLevelCount = [
                    positiveOrgans.size(),
                    warnOrgans.size(),
                    attentionOrgans.size(),
                    negativeOrgans.size()
            ]
            // 2.2 各个风险级别的数量
            fillRiskLevelNum(doc, 2, riskLevelCount as int[])
            // 2.3 详细信息
            fillDetailInfo(doc, 3, report, riskLevelCount as int[])
            // 2.4 生活习惯、饮食习惯建议
            fillEatingHabitsAndLivingHabitsPropose(doc, report, [positiveOrgans, warnOrgans, attentionOrgans, negativeOrgans])

            // 3. 器官详解
            // 阳性
            List<PDDocument> tempDocs = []
            addOrganDetailToReport(doc, positiveOrgans, 'positive', tempDocs)
            // 警告
            addOrganDetailToReport(doc, warnOrgans, 'warn', tempDocs)
            // 注意
            addOrganDetailToReport(doc, attentionOrgans, 'attention', tempDocs)

            // 4. 表格填充
            File file = ResourceUtils.getFile("classpath:meterial/Template_检查结果.pdf")
            PDDocument templateDoc = PDDocument.load(file)

            fillCheckTable(templateDoc, report)
            doc.addPage(templateDoc.getPage(0))

            drawGeneMainFunctionBg(templateDoc, report)
            doc.addPage(templateDoc.getPage(1))

            // 5. 保存
            String reportFile = "${targetFolder.getAbsolutePath()}/${new Date().format('yyyyMMdd')}_${user.name}_检测报告.pdf"
            doc.save(reportFile)
            doc.close()
            // close other pdf
            templateDoc.close()
            tempDocs.each {
                it.close()
            }

            result.isSuccess = true
        } catch (e) {
            e.printStackTrace()
            result.message = e.getMessage()
            log.error("Exception: ${e.class} Detail: ${result.message}")
        }

        result
    }

    private void addOrganDetailToReport(PDDocument doc, List<Organ> organs, def riskLevel, def tempDocs) {
        organs.each {
            File organDetail = ResourceUtils.getFile("classpath:meterial/detail/${riskLevel}/${it.name}.pdf")
            PDDocument organDoc = PDDocument.load(organDetail)
            doc.addPage(organDoc.getPage(0))
            tempDocs << organDoc
        }
    }

    /**
     *
     *
     * @param doc
     * @param pageNo
     * @param user
     */
    private void reportAddUserInfo(PDDocument doc, int pageNo, ReportInfo report) {
        UserInfo user = report.userInfo
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

    /**
     * 填充详细信息
     * @param doc
     * @param pageNo
     * @param report
     * @param riskCount
     * @return
     */
    private def fillDetailInfo(PDDocument doc, int pageNo, ReportInfo report, int[] riskCount) {
        UserInfo user = report.userInfo
        def total = riskCount.sum()
        // 姓名
        addText(doc, pageNo, user.name, [78, 720] as float[], 21.5f, [0.408f, 0.255f, 0.384f] as float[])
        def countY = 683
        // 总数
        addText(doc, pageNo, total.toString(), [126, countY] as float[], 11.5f, [0.2f, 0.2f, 0.2f] as float[])
        // 阳性
        addText(doc, pageNo, "阳性 ${riskCount[0]} 个", [275, countY] as float[], 11.5f, [0.953f, 0.267f, 0.235f] as float[])
        // 警告
        addText(doc, pageNo, "警告 ${riskCount[1]} 个", [337, countY] as float[], 11.5f, [0.463f, 0.294f, 0.443f] as float[])
        // 注意
        addText(doc, pageNo, "注意 ${riskCount[2]} 个", [405, countY] as float[], 11.5f, [0.957f, 0.725f, 0.078f] as float[])

        def checkY1 = 580
        // 身高
        addText(doc, pageNo, "${user.height}cm", [146, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 吸烟
        addText(doc, pageNo, user.smokeLevel, [299, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 高血压
        addText(doc, pageNo, user.hypertension, [448, checkY1] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])

        def checkY2 = 555
        // 体重
        BigDecimal bg = new BigDecimal(user.weight / 2)
        double weightInKg = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
        addText(doc, pageNo, "${weightInKg}kg", [146, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 饮酒
        addText(doc, pageNo, user.drinkWineLevel, [299, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])
        // 糖尿病
        addText(doc, pageNo, user.diabetes, [448, checkY2] as float[], 14f, [0.2f, 0.2f, 0.2f] as float[])

        // BMI
        addText(doc, pageNo, user.name, [63, 513] as float[], 14f, [0.408f, 0.255f, 0.384f] as float[])
        addText(doc, pageNo, user.getBMI(), [110, 488] as float[], 15f, [0.2f, 0.2f, 0.2f] as float[])
        def bmiLevelX = user.getBMILevel().size() > 2 ? 90 : 108
        addText(doc, pageNo, user.getBMILevel(), [bmiLevelX, 463] as float[], 18f, [0.2f, 0.2f, 0.2f] as float[])
        // mark
        // 上 下 左 右
        def bmiLevelMap = [
                '体重不足': [
                        [268, 505, 65, 2],
                        [268, 453, 65, 2],
                        [268, 453, 2, 52 + 2],
                        [268 + 65, 453, 2, 52 + 2]
                ],
                '正常'  : [
                        [333, 505, 66, 2],
                        [333, 453, 66, 2],
                        [333, 453, 2, 52 + 2],
                        [333 + 66, 453, 2, 52 + 2]
                ],
                '过重'  : [
                        [399, 505, 66, 2],
                        [399, 453, 66, 2],
                        [399, 453, 2, 52 + 2],
                        [399 + 66, 453, 2, 52 + 2]
                ],
                '肥胖'  : [
                        [465, 505, 66, 2],
                        [465, 453, 66, 2],
                        [465, 453, 2, 52 + 2],
                        [465 + 66, 453, 2, 52 + 2]
                ]
        ]
        def rectangle = bmiLevelMap[user.getBMILevel()].value
        rectangle.each {
            addRectangle(doc, doc.getPage(pageNo), Color.RED, it as float[])
        }
    }

    /**
     * 基因检测表格填充
     * @return
     */
    private def fillCheckTable(PDDocument templateDoc, ReportInfo report) {
        //File file = ResourceUtils.getFile("classpath:meterial/Template_检查结果.pdf")
        //PDDocument templateDoc = PDDocument.load(file)
        //doc.save("/Users/idcos/Downloads/zhoulinxian/报告基础元素/基础组成部分/test3.pdf")
        PDPage page = templateDoc.getPage(0)
        // 842.5
        // 595.5
        // 抑癌基因甲基化检查结果
        // 填充行
        report.getTumorSuppressorGeneList().eachWithIndex { gene, y ->
            // 对号
            def checkX = 135f
            def checkY = 111f
            if (gene.result) {
                addImageFromResource("classpath:meterial/other_img/对号.png", templateDoc, page,
                        [-0.5 + checkX, 843.5 - 12 - checkY - (10 + 2.368) * y] as float[], 0.3f, 0.3f)

                // 对应疾病
                // 填充列
                def relation = getTumorSuppressorGeneRelatedOrgan().find { it -> it.key == gene.name }
                //println JSON.toJSONString(relation)
                def relatedDisease = relation.value.organs.split(',').collect { it.trim() }
                relatedDisease.each { organ ->
                    def x = ReportInfo.getAllOrganNames().findIndexOf { it -> it == organ }
                    def diseaseX = 160f
                    def diseaseY = 111f
                    addRectangle(templateDoc, page, Color.RED, [-0.5 + diseaseX + (18.1 + 2.368) * x, 843.5 - 10 - diseaseY - (10 + 2.368) * y, 18.2, 10] as float[])
                }
            }
        }

        // 癌症相关基因突变检查的结果
        report.getCancerRelatedGeneMutations().eachWithIndex { gene, y ->
            // 对号
            def checkX = 135f
            def checkY = 546.62f
            if (gene.result) {
                addImageFromResource("classpath:meterial/other_img/对号.png", templateDoc, page,
                        [-0.5 + checkX
                         , 843.5 - 12 - checkY - (10 + 2.368) * y] as float[], 0.3f, 0.3f)
                // 疾病
                def relation = getCancerRelatedGeneMutationsRelatedOrgan().find { it -> it.key == gene.name }
                def relatedDisease = relation.value.organs.split(',').collect { it.trim() }
                relatedDisease.each { organ ->
                    def x = ReportInfo.getAllOrganNames().findIndexOf { it -> it == organ }
                    def diseaseX = 160f
                    def diseaseY = 546.62f
                    addRectangle(templateDoc, page, Color.RED, [-0.5 + diseaseX + (18.1 + 2.368) * x, 843.5 - 10 - diseaseY - (10 + 2.368) * y, 18.2, 10] as float[])
                }
            }
        }

        /*File tmp = Files.createTempFile("report_${UUID.randomUUID().toString()}", ".pdf").toFile()
        templateDoc.save(tmp.getAbsolutePath())

        templateDoc.close()
        tmp*/
    }

    /**
     * 基因主要功能背景填充
     * @return
     */
    def drawGeneMainFunctionBg(PDDocument templateDoc, ReportInfo report) {
        //File file = ResourceUtils.getFile("classpath:meterial/Template_检查结果.pdf")
        //PDDocument templateDoc = PDDocument.load(file)

        // 842.5
        // 595.5

        PDPage page = templateDoc.getPage(1)
        // 抑癌基因的主要功能
        def twoLineCount = 0
        report.getTumorSuppressorGeneList().eachWithIndex { gene, y ->
            if (gene.result) {
                def diseaseX = 55.5f
                def diseaseY = 98f
                def height = 11f
                def twoLine = [20].contains(y)
                if (twoLine) {
                    height = 23.2f
                    twoLineCount++
                }
                addRectangle(templateDoc, page, new Color(248, 209, 212),
                        [
                                diseaseX,
                                843.5 - 13 - diseaseY - ((10 + 2.368) * (y - twoLineCount) + (10 + 2.368) * 2 * twoLineCount),
                                473,
                                height
                        ] as float[], PDPageContentStream.AppendMode.PREPEND)
            }
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
        report.getCancerRelatedGeneMutations().eachWithIndex { gene, y ->
            if (gene.result) {
                def it = geneMutation.find { it.key == gene.name }
                def diseaseX = 55.5f
                def diseaseY = 536f
                def height = it.value.row > 1 ? 12.2f : 11.2f
                def indexInGroup = it.value.index
                def groupHeight = it.value.row * height
                addRectangle(templateDoc, page, new Color(248, 209, 212),
                        [
                                diseaseX,
                                843.5 - 13 - diseaseY - (10 + 2.368) * y,
                                70,
                                height
                        ] as float[], PDPageContentStream.AppendMode.PREPEND)

                addRectangle(templateDoc, page, new Color(248, 209, 212),
                        [
                                diseaseX + 70,
                                843.5 - 13 - diseaseY - (10 + 2.368) * (y + (it.value.row - indexInGroup)),
                                403.5f,
                                groupHeight
                        ] as float[], PDPageContentStream.AppendMode.PREPEND)
            }
        }

        /*File tmp = Files.createTempFile("report_${UUID.randomUUID().toString()}", ".pdf").toFile()
        templateDoc.save(tmp.getAbsolutePath())

        templateDoc.close()
        tmp*/
        templateDoc
    }

    private def getTumorSuppressorGeneRelatedOrgan() {
        File file = ResourceUtils.getFile('classpath:data/tumor_suppressor_gene_related_organ.json')
        JSON.parseObject(file.getText(StandardCharsets.UTF_8.toString()))
    }

    private def getCancerRelatedGeneMutationsRelatedOrgan() {
        File file = ResourceUtils.getFile('classpath:data/cancer_related_gene_mutations_related_organ.json')
        JSON.parseObject(file.getText(StandardCharsets.UTF_8.toString()))
    }

    private def getTumorSuppressorGeneRelatedOrganOrigin() {
        [
                'APAF1'  : [
                        'mainFunction': '调节细胞的凋亡',
                        'organs'      : '胃, 肝, 甲状腺, 膀胱, 肾脏, 神经胶质'
                ],
                'APC'    : [
                        'mainFunction': '调节细胞增殖与粘连，迁移以及凋亡',
                        'organs'      : '乳房, 子宫内膜, 前列腺, 大肠, 肝, 甲状腺, 膀胱, 食道, 胆囊'
                ],
                'BRCA1'  : [
                        'mainFunction': '调节细胞周期，参与修复DNA损伤以及维持遗传物质的稳定性',
                        'organs'      : '乳房, 卵巢, 胃, 甲状腺, 胰腺'
                ],
                'CDH1'   : [
                        'mainFunction': '参与细胞间的粘连，抑制癌细胞的浸润',
                        'organs'      : '卵巢, 宫颈, 子宫内膜, 前列腺, 胃, 大肠, 肺, 甲状腺, 膀胱, 胆囊'
                ],
                'CDH13'  : [
                        'mainFunction': '抑制癌细胞浸润与增殖的肿瘤抑制基因',
                        'organs'      : '乳房, 卵巢, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 肾脏'
                ],
                'DAPK'   : [
                        'mainFunction': '诱发细胞凋亡，抑制癌症转移',
                        'organs'      : '乳房, 宫颈, 胃, 肺, 甲状腺, 膀胱, 淋巴'
                ],
                'DLEC1'  : [
                        'mainFunction': '调节细胞增殖，抑制肿瘤',
                        'organs'      : '乳房, 胃, 大肠, 肺, 肝, 甲状腺, 肾脏, 淋巴'
                ],
                'ER-a'   : [
                        'mainFunction': '调节基因表达，参与细胞增殖',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 前列腺, 胃, 大肠, 甲状腺'
                ],
                'ER-b'   : [
                        'mainFunction': '调节基因表达，参与细胞增殖，与大肠，肝，心肌老化相关',
                        'organs'      : '乳房, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 肾脏'
                ],
                'FHIT'   : [
                        'mainFunction': '参与细胞凋亡以及调节细胞周期',
                        'organs'      : '乳房, 卵巢, 宫颈, 肾脏, 食道, 胆囊'
                ],
                'GSTP1'  : [
                        'mainFunction': '参与解毒酶，修复DNA',
                        'organs'      : '卵巢, 前列腺, 大肠, 肺'
                ],
                'HIC1'   : [
                        'mainFunction': '参与修复DNA损伤以及细胞凋亡',
                        'organs'      : '卵巢, 宫颈, 前列腺, 胃, 大肠, 肝, 甲状腺, 脑'
                ],
                'hMLH1'  : [
                        'mainFunction': '参与修复DNA损伤',
                        'organs'      : '子宫内膜, 胃, 大肠, 肝'
                ],
                'LKB1'   : [
                        'mainFunction': '调节细胞极性和功能，维持细胞代谢能量平衡',
                        'organs'      : '胃, 肠, 胰腺, 肺, 宫颈, 乳腺'
                ],
                'MGMT'   : [
                        'mainFunction': '参与修复DNA损伤而抑制基因突变以及形成癌症',
                        'organs'      : '乳房, 胃, 大肠, 肾脏, 食道, 脑, 淋巴'
                ],
                'MINT1'  : [
                        'mainFunction': '参与细胞信号转导',
                        'organs'      : '脑, 神经胶质'
                ],
                'MINT31' : [
                        'mainFunction': '参与肿瘤相关基因的过甲基化',
                        'organs'      : '乳房, 卵巢, 胃, 大肠, 肝, 甲状腺, 膀胱'
                ],
                'MYOD1'  : [
                        'mainFunction': '参与细胞分化，再生',
                        'organs'      : '乳房, 胃, 大肠, 肝, 肾脏'
                ],
                'p14ARF' : [
                        'mainFunction': '调节细胞周期的核心角色，与MDM蛋白质结合促进p53的激活',
                        'organs'      : '乳房, 卵巢, 前列腺, 胃, 大肠, 甲状腺, 肾脏, 胰腺, 食道, 淋巴'
                ],
                'p15'    : [
                        'mainFunction': '调节细胞周期的核心角色',
                        'organs'      : '胃, 大肠, 肺, 肝, 脑, 淋巴'
                ],
                'p16'    : [
                        'mainFunction': '参与细胞周期，通过抑制过度增殖而诱导细胞凋亡',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 胰腺,胆囊, 脑, 淋巴'
                ],
                'PTEN'   : [
                        'mainFunction': '控制过度增殖，抑制生成肿瘤',
                        'organs'      : '卵巢, 宫颈, 甲状腺'
                ],
                'PYCARD' : [
                        'mainFunction': '参与炎症反应与细胞凋亡的信号转导通路',
                        'organs'      : '乳房, 卵巢, 前列腺, 胃, 大肠, 甲状腺'
                ],
                'RASSF1A': [
                        'mainFunction': '参与细胞凋亡及细胞有丝分裂',
                        'organs'      : '乳房, 子宫内膜, 前列腺, 胃, 大肠, 肺, 甲状腺, 胰腺, 脑'
                ],
                'RUNX3'  : [
                        'mainFunction': '调节细胞生长，抑制转移的肿瘤抑制基因',
                        'organs'      : '乳房, 卵巢, 胃, 大肠, 肺, 肝, 膀胱, 肾脏, 胆囊'
                ],
                'SLC5A8' : [
                        'mainFunction': '参与细胞内运输钠离子，调节细胞生长',
                        'organs'      : '大肠, 甲状腺'
                ],
                'SOCS1'  : [
                        'mainFunction': '通过细胞因子参与调节细胞内的信号转导,抑制细胞生长',
                        'organs'      : '胃, 肝, 膀胱'
                ],
                'TIMP3'  : [
                        'mainFunction': '切断癌细胞的浸润、转移、血管新生',
                        'organs'      : '前列腺, 胃, 大肠, 肝'
                ],
                'VHL'    : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '胃, 大肠, 膀胱, 肾脏, 脑'
                ],
                'WT1'    : [
                        'mainFunction': '诱导细胞凋亡',
                        'organs'      : '卵巢, 胃, 大肠, 肝, 脑'
                ]
        ]
    }

    private def getCancerRelatedGeneMutationsRelatedOrganOrigin() {
        [
                'p53 c.524G>A'    : [
                        'mainFunction': '参与细胞凋亡以及调节细胞周期 , 参与修复DNA损伤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 淋巴, 神经胶质'
                ],
                'p53 c.743G>A'    : [
                        'mainFunction': '参与细胞凋亡以及调节细胞周期 , 参与修复DNA损伤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 淋巴, 神经胶质'
                ],
                'p53 c.747G>T'    : [
                        'mainFunction': '参与细胞凋亡以及调节细胞周期 , 参与修复DNA损伤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 淋巴, 神经胶质'
                ],
                'p53 c.817C>T'    : [
                        'mainFunction': '参与细胞凋亡以及调节细胞周期 , 参与修复DNA损伤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 淋巴, 神经胶质'
                ],
                'PIK3CA c.1624G>A': [
                        'mainFunction': '参与细胞的生长、增殖、分化、迁移以及存活等多种细胞内的信号转导通路',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 神经胶质'
                ],
                'PIK3CA c.1633G>A': [
                        'mainFunction': '参与细胞的生长、增殖、分化、迁移以及存活等多种细胞内的信号转导通路',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 神经胶质'
                ],
                'PIK3CA c.3140A>G': [
                        'mainFunction': '参与细胞的生长、增殖、分化、迁移以及存活等多种细胞内的信号转导通路',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺, 食道,胆囊, 脑, 神经胶质'
                ],
                'KRAS c.34G>T'    : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏(仅限于c.35G>A),胰腺, 食道, 胆囊,淋巴'
                ],
                'KRAS c.35G>A'    : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏(仅限于c.36G>A),胰腺, 食道, 胆囊,淋巴'
                ],
                'KRAS c.35G>T'    : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏(仅限于c.37G>A),胰腺, 食道, 胆囊,淋巴'
                ],
                'KRAS c.38G>A'    : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏(仅限于c.38G>A),胰腺, 食道, 胆囊,淋巴'
                ],
                'PTEN c.388C>G'   : [
                        'mainFunction': '控制过度增殖，抑制生成肿瘤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺,胆囊,脑, 淋巴, 神经胶质'
                ],
                'PTEN c.389G>A'   : [
                        'mainFunction': '控制过度增殖，抑制生成肿瘤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺,胆囊,脑, 淋巴, 神经胶质'
                ],
                'PTEN c.697C>T'   : [
                        'mainFunction': '控制过度增殖，抑制生成肿瘤',
                        'organs'      : '乳房, 卵巢, 子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏, 胰腺,胆囊,脑, 淋巴, 神经胶质'
                ],
                'APC c.4348C>T'   : [
                        'mainFunction': '调节细胞增殖与粘连、迁移以及凋亡',
                        'organs'      : '子宫内膜, 宫颈, 前列腺, 胃, 大肠, 肺, 肝, 甲状腺, 胰腺,食道'
                ],
                'ATM c.1009C>T'   : [
                        'mainFunction': '参与修复DNA损伤以及调节细胞凋亡',
                        'organs'      : '乳房, 子宫内膜, 前列腺, 胃,大肠, 肺, 肝, 膀胱, 肾脏, 胰腺, 淋巴'
                ],
                'BRAF c.1799T>A'  : [
                        'mainFunction': '参与细胞内的信号转导而调节细胞增殖、分化以及存活',
                        'organs'      : '卵巢, 大肠, 肺, 肝, 甲状腺, 膀胱, 肾脏,胆囊, 脑, 淋巴, 神经胶质'
                ],
                'IDH1 c.395G>A'   : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '神经胶质,胆囊,淋巴组织,甲状腺,大肠,子宫内膜,前列腺,肝,膀胱,乳腺'
                ],
                'RET c.2753T>C'   : [
                        'mainFunction': '参与细胞生长与分化',
                        'organs'      : '乳房,子宫内膜, 大肠, 肺, 甲状腺, 膀胱, 肾脏'
                ]
        ]
    }

    private def prepareHabitsPropose() {
        File file = ResourceUtils.getFile('classpath:data/eating_habits_and_living_habits.json')
        JSON.parseObject(file.getText(StandardCharsets.UTF_8.toString()))
    }

    /**
     * 根据检测结果给出饮食习惯和生活习惯建议
     * @param doc
     * @param report
     * @param classifiedOrgans 划分了风险级别的器官
     */
    def fillEatingHabitsAndLivingHabitsPropose(PDDocument doc, ReportInfo report, List<List<Organ>> classifiedOrgans) {
        def habitsData = prepareHabitsPropose()

        // 1. merge propose
        def positiveOrgans = classifiedOrgans[0]
        def eatingHabitsPropose = [
                'head'   : '',
                'content': '',
                'items'  : []
        ]
        def livingHabitsPropose = [
                'head'   : '',
                'content': '',
                'items'  : []
        ]
        if (positiveOrgans.size() > 0) {
            UserInfo user = report.userInfo
            positiveOrgans.each {
                def habit = habitsData.find { habit -> habit.key == it.name }?.value
                if (habit) {
                    if (eatingHabitsPropose.items.size() == 0) {
                        eatingHabitsPropose = habit.eatingHabits.positive
                    } else {
                        eatingHabitsPropose.items.addAll(habit.eatingHabits.positive.items.findAll { item ->
                            null == eatingHabitsPropose.items.find {
                                it.title.equals(item.title)
                            }
                        })
                    }

                    def matchedPropose = habit.livingHabits.positive[user.sex.code][user.smokeLevel.equals('无') ? 'nonSmoke' : 'smoke']
                    if (livingHabitsPropose.size() == 0 && StringUtils.isNotBlank(matchedPropose.head)) {
                        livingHabitsPropose = matchedPropose
                    } else {
                        livingHabitsPropose.items.addAll(matchedPropose.items.findAll { item ->
                            null == livingHabitsPropose.items.find {
                                it.title.equals(item.title)
                            }
                        })
                    }
                }
            }
        } else {
            def otherProposeOrgans = []
            otherProposeOrgans.addAll(classifiedOrgans[1])
            otherProposeOrgans.addAll(classifiedOrgans[2])
            otherProposeOrgans.each {
                def habit = habitsData.find { habit -> habit.key == it.name }?.value
                if (habit) {
                    if (eatingHabitsPropose.items.size() == 0) {
                        eatingHabitsPropose = habit.eatingHabits.other
                    } else {
                        eatingHabitsPropose.items.addAll(habit.eatingHabits.other.items.findAll { item ->
                            null == eatingHabitsPropose.items.find {
                                it.title.equals(item.title)
                            }
                        })
                    }

                    def matchedPropose = habit.livingHabits.other
                    if (livingHabitsPropose.size() == 0 && StringUtils.isNotBlank(matchedPropose.head)) {
                        livingHabitsPropose = matchedPropose
                    } else {
                        livingHabitsPropose.items.addAll(matchedPropose.items.findAll { item ->
                            null == livingHabitsPropose.items.find {
                                it.title.equals(item.title)
                            }
                        })
                    }
                }
            }
        }

        log.info("饮食习惯" + JSON.toJSONString(eatingHabitsPropose, true))
        log.info("生活习惯" + JSON.toJSONString(livingHabitsPropose, true))

        // 2. draw propose
        // 饮食习惯
        //addHabitsPropose(doc, 4, eatingHabitsPropose, [100, 412] as float[], [0.584f, 0.596f, 0.618f] as float[], 200f)
        addMultiLineText(doc, 3, eatingHabitsPropose, [300, 240, 20, 90] as float[], [0.584f, 0.596f, 0.618f] as float[], 200f)

        // 生活习惯
        //addHabitsPropose(doc, 4, livingHabitsPropose, [260, 412] as float[], [0.584f, 0.596f, 0.618f] as float[], 200f)
        addMultiLineText(doc, 3, livingHabitsPropose, [300, 20, 20, 90] as float[], [0.584f, 0.596f, 0.618f] as float[], 200f)

    }

    /**
     * 使用pdfbox-layout实现
     * @param doc
     * @param pageNo
     * @param habits [ 'title': '', 'common': '', 'items': [] ]
     * @param position [ x, y ] as float[]
     * @param fontSize
     * @param nonStrokingColor [ r, g, b ] as float[]
     * @param lineWidth
     * @return
     */
    private def addHabitsPropose(PDDocument doc, int pageNo, def habits, float[] position, float[] nonStrokingColor, float lineWidth) {
        File file = ResourceUtils.getFile("classpath:meterial/Template_空白页.pdf")
        PDDocument targetDoc = PDDocument.load(file)
        PDPageContentStream cs = new PDPageContentStream(targetDoc, targetDoc.getPage(0))
        //def ttfPath = '/Users/idcos/Downloads/字体/msyh.ttf'
        File fontFile = ResourceUtils.getFile("classpath:meterial/msyh.ttf")
        PDFont font = PDType0Font.load(targetDoc, fontFile)
        cs.beginText()

        cs.newLineAtOffset(position[0], position[1])
        cs.setLineWidth(lineWidth)
        cs.setStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
        cs.setNonStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])

        habits.each { key, value ->
            if ((value instanceof String && StringUtils.isNotBlank(value))
                    || (value instanceof Collection && value.size() > 0)) {
                switch (key) {
                    case 'title':
                        cs.setFont(font, 14.52f)
                        cs.showText(value)
                        cs.newLine()
                        cs.newLine()
                        break
                    case 'common':
                        cs.setFont(font, 10.22f)
                        cs.showText(value)
                        cs.newLine()
                        cs.newLine()
                        break
                    case 'items':
                        value.each { k, v ->
                            switch (k) {
                                case 'title':
                                    cs.setFont(font, 14.52f)
                                    cs.showText(value.title)
                                    cs.newLine()
                                    break
                                case 'content':
                                    cs.setFont(font, 10.22f)
                                    cs.showText(value.content)
                                    cs.newLine()
                                    cs.newLine()
                                    break
                                default:
                                    break
                            }
                        }
                        break
                    default:
                        break
                }
            }
        }
        cs.endText()

        cs.close()

        LayerUtility layerUtility = new LayerUtility(doc)
        PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, 0)
        AffineTransform affineTransform = new AffineTransform()
        layerUtility.appendFormAsLayer(doc.getPage(pageNo), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        targetDoc.close()
    }

    private def addTextWithReturn(PDDocument doc, int pageNo, def habits, float[] position, float[] nonStrokingColor, float lineWidth) {
        final PreloadFont aFont = PreloadFont.createEmbedding(MyFontResource.MICROSOFT_YAHEI.getFontResource())
        FontSpec fontSpec = new FontSpec(aFont, 10.22f)
        final PLPageSet aPS1 = new PLPageSet(PDRectangle.A4)
        final float fLineSpacing = 1.5f

        //aPS1.addElement(new PLText(paragraph, fontSpec))
        String text = ""
        habits.each { key, value ->
            if ((value instanceof String && StringUtils.isNotBlank(value))
                    || (value instanceof Collection && value.size() > 0)) {
                switch (key) {
                    case 'head':
                        fontSpec = new FontSpec(PreloadFont.REGULAR, 14.52f)
                        /*aPS1.addElement(new PLText(value + '\n', fontSpec)
                                .setMargin(position[0], position[1], position[2], position[3])
                                .setMaxWidth(lineWidth))*/
                        text += value + '\n'
                        break
                    case 'common':
                        fontSpec = new FontSpec(PreloadFont.REGULAR, 10.22f)
                        /*aPS1.addElement(new PLText(value + '\n', fontSpec))*/
                        text += value + '\n'
                        break
                    case 'items':
                        value.each { item ->
                            fontSpec = new FontSpec(PreloadFont.REGULAR, 14.52f)
                            /*aPS1.addElement(new PLText(item.title + '\n', fontSpec))*/
                            text += item.title + '\n'

                            fontSpec = new FontSpec(PreloadFont.REGULAR, 10.22f)
                            /*aPS1.addElement(new PLText(item.content + '\n', fontSpec))*/
                            text += item.content + '\n'
                        }
                        break
                    default:
                        break
                }
            }
        }
        aPS1.addElement(new PLText(text, fontSpec))

        final PageLayoutPDF aPageLayout = new PageLayoutPDF().setCompressPDF(false)
        aPageLayout.addPageSet(aPS1)
        File textFile = new File("tmp/${UUID.randomUUID().toString()}.pdf")
        aPageLayout.renderTo(textFile)

        PDDocument targetDoc = PDDocument.load(textFile)
        LayerUtility layerUtility = new LayerUtility(doc)
        PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, 0)
        AffineTransform affineTransform = new AffineTransform()
        layerUtility.appendFormAsLayer(doc.getPage(pageNo), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        targetDoc.close()
        //Files.delete(textFile.toPath())
    }

    public static def addMultiLineText(PDDocument doc, int pageNo, def habits, float[] position, float[] nonStrokingColor, float lineWidth) {
        File file = ResourceUtils.getFile("classpath:meterial/Template_空白页.pdf")
        PDDocument targetDoc = null
        try {
            targetDoc = PDDocument.load(file)
            PDPage page = targetDoc.getPage(0)
            PDPageContentStream contentStream = new PDPageContentStream(doc, page)

            File fontFile = ResourceUtils.getFile("classpath:meterial/msyh.ttf")
            PDFont pdfFont = PDType0Font.load(doc, fontFile)

            //String text = "I am trying to create a PDF file with a lot of text contents in the document. I am using PDFBox"

            contentStream.beginText()
            contentStream.newLineAtOffset(position[0], position[1])
            contentStream.setStrokingColor(nonStrokingColor[0], nonStrokingColor[1], nonStrokingColor[2])
            float fontSize = 14.52f
            habits.each { key, v ->
                if (v instanceof String && StringUtils.isNotBlank(v)) {
                    def value = v.replace('\r', '').replace('\n', '')
                    switch (key) {
                        case 'head':
                            fontSize = 14.52f
                            contentStream.setFont(pdfFont, fontSize)
                            contentStream.showText(value)
                            contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)
                            contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)
                            break
                        case 'common':
                            fontSize = 10.22f
                            contentStream.setFont(pdfFont, fontSize)
                            contentStream.showText(value)
                            contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)
                            contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)
                            break
                        default:
                            break
                    }
                } else if (v instanceof JSONArray && v.size() > 0) {
                    switch (key) {
                        case 'items':
                            v.each { item ->
                                fontSize = 14.52f
                                contentStream.setFont(pdfFont, fontSize)
                                contentStream.showText(item.title)
                                contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)

                                fontSize = 10.22f
                                contentStream.setFont(pdfFont, fontSize)
                                contentStream.showText(item.content)
                                contentStream.newLineAtOffset(0, -fontSize * 1.5 as float)
                            }
                            break
                        default:
                            break
                    }
                }
            }
            contentStream.endText()
            contentStream.close()

            LayerUtility layerUtility = new LayerUtility(doc)
            PDFormXObject firstForm = layerUtility.importPageAsForm(targetDoc, 0)
            AffineTransform affineTransform = new AffineTransform()
            layerUtility.appendFormAsLayer(doc.getPage(pageNo), firstForm, affineTransform, "text${UUID.randomUUID().toString()}")

        } catch (e) {
            e.printStackTrace()
        } finally {
            if (targetDoc != null) {
                targetDoc.close()
            }
        }
    }
}
