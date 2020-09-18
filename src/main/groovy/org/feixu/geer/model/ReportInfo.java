package org.feixu.geer.model;

import com.sun.istack.internal.NotNull;
import org.feixu.geer.enums.SexEnum;

import java.util.List;

/**
 * 报告信息
 */
public class ReportInfo {

    private String registerDate;
    private String reportDate;
    private int positiveCount;
    private int warningCount;
    private int cautionCount;
    private int negativeCount;

    public String getRegisterDate() {
        return registerDate;
    }

    public ReportInfo setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public String getReportDate() {
        return reportDate;
    }

    public ReportInfo setReportDate(String reportDate) {
        this.reportDate = reportDate;
        return this;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public ReportInfo setPositiveCount(int positiveCount) {
        this.positiveCount = positiveCount;
        return this;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public ReportInfo setWarningCount(int warningCount) {
        this.warningCount = warningCount;
        return this;
    }

    public int getCautionCount() {
        return cautionCount;
    }

    public ReportInfo setCautionCount(int cautionCount) {
        this.cautionCount = cautionCount;
        return this;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public ReportInfo setNegativeCount(int negativeCount) {
        this.negativeCount = negativeCount;
        return this;
    }

    public List<Organ> getOrgansBySex(@NotNull SexEnum sex) {
        String[] commonOrgans = new String[]{ "乳房", "胃", "大肠", "肺", "肝", "甲状腺", "膀胱", "肾脏", "胰腺", "食道", "胆囊", "脑", "淋巴", "神经胶质" };
        String[] maleOrgans = new String[] { "前列腺" };
        String[] femaleOrgans = new String[] { "卵巢", "宫颈", "子宫内膜" };
        switch (sex) {
            case male:
                break;
            case female:
                break;
            default:
                break;
        }

        return null;
    }
}
