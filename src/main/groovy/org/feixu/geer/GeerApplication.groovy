package org.feixu.geer

import org.apache.commons.io.FileUtils
import org.feixu.geer.biz.service.ReportService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import java.nio.file.Files

@SpringBootApplication
class GeerApplication implements CommandLineRunner, ExitCodeGenerator {
    private static Logger log = LoggerFactory.getLogger(GeerApplication.class)

    private static String reportPath = "${System.properties['user.dir']}/reports/"

    @Autowired
    private ReportService reportService

    static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(GeerApplication, args)))
    }

    @Override
    void run(String... args) throws Exception {
        String defaultUserDir = reportPath
        if (null != args && args.size() < 1) {
            println """使用方法: java -jar reportSystem.jar [excel名，例如: D:/report/检测数据.xlsx] [报告存放文件路径, 例如：D:/report/]
生成的报告为 xxx_检测报告_20201003.pdf
默认的报告生成目录: ${System.properties['user.dir']}/reports/
"""
            throw new RuntimeException("启动参数不正确")
        }
        String excelFileName = args[0]
        File excelFile = new File(excelFileName)
        if (!excelFile.exists()) {
            println "检测数据文件（${excelFileName}）不存在！"
        }
        String targetPath = args.size() > 1 ? args[1] : defaultUserDir
        File targetFolder = new File(targetPath)
        if (!targetFolder.exists()) {
            try {
                Files.createDirectories(targetFolder.toPath())
            } catch(e) {
                throw e
            }
        }

        reportService.createReport(excelFile, targetFolder)
    }

    @Override
    int getExitCode() {
        return 0
    }
}
