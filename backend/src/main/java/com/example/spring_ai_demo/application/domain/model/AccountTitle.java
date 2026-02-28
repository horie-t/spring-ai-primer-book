package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;

@Getter
public enum AccountTitle {
    @JsonPropertyDescription("従業員に対する給与、諸手当、賃金の支払いを記録するための勘定科目。")
    SALARIES_AND_WAGES("701"),

    @JsonPropertyDescription("事務用品、消耗品、文房具などの購入費用。")
    SUPPLIES_EXPENSE("702"),

    @JsonPropertyDescription("外部への販売手数料や仲介手数料などの支払い。")
    COMMISSIONS_PAID("703"),

    @JsonPropertyDescription("業務のための出張旅費、交通費。")
    TRAVEL_EXPENSE("704"),

    @JsonPropertyDescription("電話代、郵便代、インターネット利用料などの通信費。")
    COMMUNICATION_EXPENSE("705"),

    @JsonPropertyDescription("事務所や店舗の賃借料、家賃。")
    RENT_EXPENSE("706"),

    @JsonPropertyDescription("電気代、ガス代、水道代などの水道光熱費。")
    UTILITIES_EXPENSE("707"),

    @JsonPropertyDescription("広告宣伝、PR活動、パンフレット作成などの費用。")
    ADVERTISING_EXPENSE("708");

    private final String categoryCode;

    AccountTitle(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}