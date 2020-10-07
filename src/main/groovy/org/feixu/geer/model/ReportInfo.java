package org.feixu.geer.model;

import org.feixu.geer.enums.SexEnum;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private UserInfo userInfo = new UserInfo();

    private List<Organ> organs;

    private List<TumorSuppressorGene> tumorSuppressorGeneList = new ArrayList<>();

    private List<CancerRelatedGeneMutations> cancerRelatedGeneMutations = new ArrayList<>();

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

    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<TumorSuppressorGene> getTumorSuppressorGeneList() {
        return tumorSuppressorGeneList;
    }

    public void setTumorSuppressorGeneList(List<TumorSuppressorGene> tumorSuppressorGeneList) {
        this.tumorSuppressorGeneList = tumorSuppressorGeneList;
    }

    public List<CancerRelatedGeneMutations> getCancerRelatedGeneMutations() {
        return cancerRelatedGeneMutations;
    }

    public void setCancerRelatedGeneMutations(List<CancerRelatedGeneMutations> cancerRelatedGeneMutations) {
        this.cancerRelatedGeneMutations = cancerRelatedGeneMutations;
    }

    private static String[][] commonOrgans = new String[][] {
            new String[] {  },
            new String[] { "胃", "大肠", "肺", "肝", "甲状腺", "膀胱", "肾脏", "胰腺", "食道", "胆囊", "脑", "淋巴", "神经胶质" }
    };
    private static String[] maleOrgans = new String[] { "前列腺" };
    private static String[] femaleOrgans = new String[] { "乳房", "卵巢", "宫颈", "子宫内膜" };

    /**
     * 获取所有器官的列表
     *
     * @return
     */
    public static List<String> getAllOrganNames() {
        List<String> organs = new ArrayList<>();
        organs.addAll(Arrays.asList(commonOrgans[0]));
        organs.addAll(Arrays.asList(femaleOrgans));
        organs.addAll(Arrays.asList(maleOrgans));
        organs.addAll(Arrays.asList(commonOrgans[1]));
        return organs;
    }

    /**
     * 获取报告中对应的器官
     * @param sex
     * @return
     */
    public static List<Organ> getReportOrgansBySex(@NonNull SexEnum sex) {
        List<Organ> organs = new ArrayList<>();
        for (String s : commonOrgans[0]) {
            Organ organ = new Organ();
            organ.setName(s);
            organs.add(organ);
        }
        switch (sex) {
            case male:
                for (String s : maleOrgans) {
                    Organ organ = new Organ();
                    organ.setName(s);
                    organs.add(organ);
                }
                break;
            case female:
                for (String s : femaleOrgans) {
                    Organ organ = new Organ();
                    organ.setName(s);
                    organs.add(organ);
                }
                break;
            default:

                break;
        }
        for (String s : commonOrgans[1]) {
            Organ organ = new Organ();
            organ.setName(s);
            organs.add(organ);
        }

        return organs;
    }

    /**
     * 获取已检测的器官
     * @return
     */
    public List<Organ> getCheckedOrgans() {
        return this.organs.stream()
                .filter(it -> getOrganNamesBySex().stream().anyMatch(organ -> organ.equals(it.getName())))
                .collect(Collectors.toList());
    }

    private List<String> getOrganNamesBySex() {
        List<String> organs = new ArrayList<>();
        organs.addAll(Arrays.asList(commonOrgans[0]));
        switch (this.userInfo.getSex()) {
            case male:
                organs.addAll(Arrays.asList(maleOrgans));
                break;
            case female:
                organs.addAll(Arrays.asList(femaleOrgans));
                break;
            default:
                break;
        }
        organs.addAll(Arrays.asList(commonOrgans[1]));
        return organs;
    }
}
