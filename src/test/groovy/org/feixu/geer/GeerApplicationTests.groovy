package org.feixu.geer

import com.alibaba.fastjson.JSON
import org.apache.commons.lang.StringUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

import java.nio.charset.StandardCharsets

@SpringBootTest(args = [ GeerApplication.SPECIAL ])
class GeerApplicationTests {
    private static Logger log = LoggerFactory.getLogger(GeerApplicationTests.class)

    @Test
    void contextLoads() {
    }

    @Test
    void readEatingHabitsAndLivingHabits() {
        def data = readEatingHabitsAndLivingHabitsFromFile(new File('/Users/idcos/Downloads/zhoulinxian/报告基础元素/饮食习惯生活习惯.xlsx'))
        new File('/Users/idcos/Documents/feixu/codes/geer/src/main/resources/data/eating_habits_and_living_habits.json').withWriter(StandardCharsets.UTF_8.toString()) {
            it.write(JSON.toJSONString(data, true))
        }
    }

    private def readEatingHabitsAndLivingHabitsFromFile(File excel) {
        def ret = [:]
        InputStream inp = null
        try {
            inp = new FileInputStream(excel)
            //InputStream inp = new FileInputStream("workbook.xlsx")
            Workbook wb = XSSFWorkbookFactory.create(inp)
            Sheet sheet = wb.getSheetAt(0)
            // read each row
            sheet.collect().eachWithIndex { row, index ->
                // read each cell
                def organName = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                println "First Cell Value:$organName"
                if (index > 0 && null != organName && StringUtils.isNotBlank(organName)) {
                    def data = [
                            eatingHabits: [
                                    'positive': '',
                                    'other': ''
                            ],
                            livingHabits: [
                                    'positive': [
                                            'male': [
                                                    'smoke': '',
                                                    'nonSmoke': '',
                                            ],
                                            'female': [
                                                    'smoke': '',
                                                    'nonSmoke': '',
                                            ]
                                    ],
                                    'other': ''
                            ]
                    ]
                    ret.put(organName, data)
                    8.times {
                        Cell cell = row.getCell(it, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        println "cell value is:$cell"
                        def value = cell?.toString()
                        println value

                        switch (it) {
                            case 1:
                                // 饮食习惯——阳性
                                data.eatingHabits.positive = analyzeData(value)
                                break
                            case 2:
                                // 饮食习惯——警告及以下
                                data.eatingHabits.other = analyzeData(value)
                                break
                            case 3:
                                // 生活习惯-不吸烟-男
                                data.livingHabits.positive.male.noneSmoke = analyzeData(value)
                                break
                            case 4:
                                // 生活习惯-不吸烟-女
                                data.livingHabits.positive.female.noneSmoke = analyzeData(value)
                                break
                            case 5:
                                // 生活习惯-吸烟-男
                                data.livingHabits.positive.male.smoke = analyzeData(value)
                                break
                            case 6:
                                // 生活习惯-吸烟-女
                                data.livingHabits.positive.female.smoke = analyzeData(value)
                                break
                            case 7:
                                // 生活习惯（没有警告以上等级）
                                data.livingHabits.other = analyzeData(value)
                                break
                            default:
                                break
                        }


                    }
                    println ""
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
        ret
    }

    private def analyzeData(String value) {
        List<String> array = []
        value.split('\n').each{ str ->
            def line = str.replace('\r', '').trim()
            if (StringUtils.isNotBlank(line)) {
                array << line
            }
        }
        def innerData = [
                'head': '',
                'common': '',
                'items': []
        ]
        array.eachWithIndex { line, i ->
            if (0 == i) {
                innerData.head = line
            } else if (1 == i) {
                if (!line.startsWith('●')) {
                    innerData.common = line
                } else {
                    innerData.items << ['title': line]
                }
            } else {
                if (line.startsWith('●')) {
                    innerData.items << ['title': line]
                } else {
                    innerData.items.last().content = line
                }
            }
        }
        innerData
    }
}
