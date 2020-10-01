package org.feixu.geer.enums;

public enum SexEnum {
    male("male", "男"),
    female("female", "女");
    private String code;
    private String description;

    SexEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public SexEnum setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SexEnum setDescription(String description) {
        this.description = description;
        return this;
    }

    public static SexEnum getByDesc(String description) {
        for (SexEnum value : SexEnum.values()) {
            if (value.description.equals(description)) {
                return value;
            }
        }
        return null;
    }
}
