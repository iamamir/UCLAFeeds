
package models.dto;


public class Response
{

    public static enum Status
    {
        ALL_OK(1, ""),
        USER_NOT_FOUND(2, "Invalid User Id or Password"),

        UNAUTHORIZED_ACCESS(803, "Unauthorized access! (It may be due to the reason that you are not authorized to perform this action or all required details were not provided)"),

        //Global
        INTERNAL_SYSTEM_ERROR(9998, "Internal system error"),
        INVALID_SERVICE_REQUESTED(9999, "Invalid service requested"),
        ADJUSTMENT_PARAM_NOT_FOUND(9995, "Adjustment param not found");

        int code;
        String errorMsg;

        private Status(int code, String errorMsg)
        {
            this.code = code;
            this.errorMsg = errorMsg;
        }

        public int getErrorCode()
        {
            return this.code;
        }

        public String getErrorMsg()
        {
            return this.errorMsg;
        }
    }

    public static enum Params
    {



        NONCE("nonce"),
        STATUS_CODE("statusCode"),
        ERROR_MSG("errorMsg"),
        STATUS("status"),

        USER_ID("userId"),
        USER_CARD_ID("userCardId"),
        ID("id"),

        USER_ITEMS("userItems"),

        QUIZ_ID("quizId"),
        QUIZ_LEVEL("quizLevel"),
        QUIZ_QUESTION("quizQuestion"),
        QUIZ_OPTION_A("quizOptionA"),
        QUIZ_OPTION_B("quizOptionB"),
        QUIZ_OPTION_C("quizOptionC"),
        QUIZ_OPTION_D("quizOptionD"),
        QUIZ_ANSWER("quizAnswer"),
        QUIZ("quiz"),
        RESULT("result"),
        RESULT_YEAR("resultYear"),
        RESULT_MONTH("resultMonth"),
        REWARD_VALUE("rewardValue"),
        REWARD_CODE("rewardCode"),
        RESULT_ID("resultId"),


        USER_POINT("userPoint"),
        USER_RANK("userRank");


        String param;

        private Params(String param)
        {
            this.param = param;
        }

        public String getValue()
        {
            return this.param;
        }
    }

    public static enum MainParams
    {

        QUOTATION_ID("quotationId"),
        QUOTATION_DETAIL_ID("quotationDetailId"),
        RENTAL_SEQUENCE("rentalSequence"),
        START_TERM("startTerm"),
        END_TERM("endTerm"),
        RENTAL_AMOUNT("rentalAmount"),
        NFA_PERCENTAGE("nfaPercentage"),
        INTREST_RATE("interestRate"),
        VAT_AMOUNT("vatAmount"),
        REGISTRATION_AMOUNT("registrationAmount"),
        INSURANCE_AMOUNT("insuranceAmount"),
        MAINTENANCE_AMOUNT("maintenanceAmount"),
        PARENT_ID("parentId"),
        CHILD_ID("childId"),
        RELATIONSHIP_ID("relationshipId"),
        ACTIVE_IND("activeInd"),
        CREATION_DATE("creationDate"),
        INSERTED_BY("insertedBy"),
        COMPANY_ID("companyId"),
        RULE_ID("ruleId"),
        RULE_NAME("ruleName"),
        RULE_TYPE_ID("ruleTypeId"),
        USER_ID("userId"),
        USER_NAME("userName"),
        USER_PASSWORD("userPassword"),
        CHANGE_PASSWORD_IND("changePasswordInd"),
        DEPARTMENT_ID("departmentId"),
        DESIGNATION_ID("designationId"),
        USER_LEVEL("userLevel"),
        NEW_USER_IND("newUserInd"),
        PASSWORD_EXPIRY_DAYS("passwordExpiryDays"),
        MIN_PASSWORD_LENGTH("minPasswordLength"),
        LOGIN_ATTEMPTS("loginAttempts"),
        LANGUAGE_ID("languageId"),
        POS_USER_EMAIL("poUserEmail"),
        POS_USER_PIN("poUserPin"),
        LANGUAGE_ADMIN_IND("languageAdminInd"),
        SERIAL_NUMBER("serialNumber"),
        EMAIL_ID("emailId"),
        EMAIL_PASSWORD("emailPassword"),
        REALAY_HOST("realayHost"),
        GROUP_ID("groupID"),
        LAST_PASSWORD("lastPassword"),
        CHANGE_DATE("changeDate"),
        ITEM_ID("itemId"),
        AUXILIARY_VALUE("auxiliaryValue"),
        VALIDATE_ON_SAVE("validateOnSave"),
        VALIDATE_ON_SUBMIT("validateOnSubmit"),
        SERVICE_TYPE("serviceType"),
        UPDATE_DATE("updateDate"),
        ASSET_MODEL_ID("assetModelId"),
        ASSET_MAKE_ID("assetMakeId"),
        ASSET_TYPE_ID("assetType"),
        ASSET_SUB_TYPE_ID("assetSubTypeId"),
        SYSTEM_IND("systemInd"),
        LIST_PRICE("listPrice"),
        IMAGE_PATH("imagePath"),
        LAST_UPDATED("lastUpdated"),
        TRANSMISSION_TYPE_CODE("transmission_type_code"),
        MAKEMODEL_GROUP_ID("makeModelGroupId"),
        FINANCIAL_PRODUCT_ID("financialProductId"),
        BUSINESS_PARTNER_ID("businessPartnerId"),
        BUSINESS_PARTNER_NAME("businessPartnerName"),
        BP_NUMBER("bpNumber"),
        BP_TYPE_ID("bpTypeId"),
        LEGAL_STATUS_CODE("legalStatusCode"),
        PMS_DEALER_NBR("pmsDealerNbr"),
        ROLE_ID("roleId"),
        CHART_PRIORITY_RANK("chartPriorityRank"),
        BP_FC_IND("bpFcInd"),
        ACTIVATE_IND("activateInd"),
        CHART_DETAIL_ID("chartDetailId"),
        AUTO_INCREMENT("autoIncrement"),
        TRANSECTION_TYPE_ID("transectionTypeId"),
        CHART_DETAIL_SEQ("chartDetailSeq"),
        CHART_MAIN_ID("chartMainId"),
        FINANCIAL_GROUP_ID("financialGroupId"),
        ASSET_MAKE_CODE("assetMakeCode"),
        ASSET_MODEL_CODE("assetModelCode"),
        TERM_RANGE_ID("termRangeId"),
        AGE_RANGE_ID("ageRangeId"),
        VEHICLE_TYPE_CODE("vehicleTypeCode"),
        AMOUNT_RANGE_ID("amountRangeId"),
        ASSET_CONDITION_CODE("assetConditionCode"),
        DOWNPAYMENT_RANGE_ID("downpaymentRangeId"),
        RENTAL_MODE_TYPE("rentalModeType"),
        COMMISSION_FIXED_AMOUNT("commissionFixedAmount"),
        LEVEL1_FLEX_PERCENTAGE("level1FlexPercentage"),
        LEVEL2_FLEX_PERCENTAGE("level2FlexPercentage"),
        LEVEL3_FLEX_PErCENTAGE("level3FlexPercentage"),
        FC_COMMISSION_AMOUNT("fcCommissionAmount"),
        FC_COMMISSION_PERCENTAGE("fcCommissionPercentage"),
        DEALER_PARENT_COMM_PCT("dealerParentCommPct"),
        DEALER_PARENT_COMM_AMOUNT("dealerParentCommAmount"),
        DEALER_PARENT_COMM_PCT_AMT("dealerParentCommPctAmt"),
        RESERVED_POOL_COMM_PCT("reservedPoolCommPct"),
        BASIC_CALCULATION_PCT("basicCalculationPct"),
        MINIMUM_COMMISSION_AMOUNT("minimumCommissionAmount"),
        CP_MAXBAL_EFFECTIVE_DATE("cpMaxbalEfectiveDate"),
        CHART_VALUE("chartValue"),
        MAXIMUM_RATE("maximumRate"),
        MINIMUM_RATE("minimumRate"),
        MILEAGE_PER_YEAR_CDE("milagePerYearCde"),
        PRODUCTION_YEAR_CDE("productionYearCde"),
        MODEL_YEAR_CDE("modelYearCde"),
        EFF_END_DTE("effEndDte"),
        EFF_START_DTE("effStartDte"),
        CHART_ID("chartId"),
        CHART_NAME("chartName"),
        CHART_TYPE("chartType"),
        COMPANY_NAME("companyName"),
        COMPANY_URL("companyUrl"),
        GROUP_NAME("groupName"),
        GROUP_FULL_NAME("groupFullName"),
        REGISTRATION_VAT_APPLY("registrationVatApply"),
        INSURANCE_VAT_APPLY("InsuranceVatApply"),
        FINANCIAL_PARAMETER_ID("financialParameterId"),
        PARAMETER_NAME("parameterName"),
        MINIMUM_LEASE_TERM("minimumLeaseTerm"),
        MAXIMUM_LEASE_TERM("maximumLeaseTerm"),
        MINIMUM_FINANCING_PCT("minimumFinancingPct"),
        MINIMUM_FINANCING_AMOUNT("minimumFinancingAmount"),
        PRODUCT_NAME("productName"),
        PRODUCT_FULL_NAME("productFullName"),
        AMORTIZATION_METHOD("amortizationMethod"),
        COMMISSION_CALCBASE_TYPE("commissionCalcbaseType"),
        COMMISSION_SUBSIDY_IND("commissionSubsidyInd"),
        FIX_VARIABLE_TYPE("fixVariableType"),
        FUTURE_VALUE_TYPE("futureValueType"),
        FUTURE_VALUE_PCT("futureValuePct"),
        FUTURE_VALUE_AMOUNT("futureValueAmount"),
        INTEREST_MARGIN_IND("interestMarginInd"),
        INTEREST_RATE("interestRate"),
        LASTRENTAL_ROUNDING_IND("lastrentalRoundingInd"),
        LEASE_SUB_TYPE("leaseSubType"),
        OD_CALCBASE_STATUS("odCalcbaseStatus"),
        OD_INTEREST_PCT("odInterestPct"),
        OVERDUE_ACTUAL_RATE("overdueActualRate"),
        PRODUCT_TYPE("productType"),
        PROMOTIONAL_IND("promotionInd"),
        RENTAL_CALCULATION_METHOD("rentalCalculationMethod"),
        RV_PCT("rvPct"),
        STRUCTURED_RENTAL_IND("structuredRentalInd"),
        VALID_FROM_DATE("validFromDate"),
        VALID_TO_DATE("validToDate"),
        ZERO_INTEREST_IND("zeroInterestInd"),
        MANUFACTURER_SUBSIDY_PCT("manufacturer_subsidyPct"),
        DEALER_SUBSIDY_PCT("dealerSubsidyPct"),
        DEALER_COMMISION_PCT("dealer"),
        DCLT_COMMISSION_PCT("dcltCommisionPct"),
        COMPILIANCE_WTH_DTE("compilianceWthDte"),
        SINGLE_PAYMENT_IND("singlePaymentInd"),
        WAIVED_RENTAL("waivedRental"),
        WAIVED_RENTAL_IND("waivedRentalInd"),
        SD_ROUND("sdRound"),
        SD_ROUND_IND("sdRoundInd"),
        SD_CTR_AMT_PCT("sdCtrAmtPct"),
        SD_RENTAL("sdRental"),
        SD_PCT_RENTAL_IND("sdPctRentalInd"),
        LOOKUP_RANGE_ID("lookupRangeId"),
        LOOKUP_MAIN_ID("lookupMainId"),
        EXTERNAL_CODE("externalCode"),
        MINIMUM_RANGE("minimumRange"),
        MAXIMUM_RANGE("maximumRange"),
        STATEMENT_ID("statementId"),
        BATCH_CODE("batchCode"),
        LOOKUP_NARRATIVE("lookupNarrative"),
        STATEMENT_TEXT("statementText"),
        EXECUTION_DATE("executionDate"),
        RUNNING_STATUS("runningStatus"),
        SECURITY_DEPOSIT("securityDeposit"),
        ERROR_TEXT("errorText"),
        MANDATORY_IND("mandatoryInd"),
        TRACKING_IND("trackingInd"),
        DEFAULT_VALUE("defaultValue"),
        QUOTATION_NUMBER("quotationNumber"),
        PROPOSAL_NUMBER("proposalNumber"),
        QUOTATION_DATE("quotationDate"),
        FINANCIAL_COMPANY_ID("financialCompanyId"),
        BRANCH_BP_ID("branchBPId"),
        ZONE_CODE("zoneCode"),
        DEALER_BP_ID("dealerBpId"),
        SHOW_ROOM_BP_ID("showRoomBpId"),
        SALES_PERSON_BP_ID("salesPersonBpId"),
        FINANCIAL_CONSULTANT_BP_ID("financialConsultantBpId"),
        MANUFACTURER_BP_ID("manufacturerBpId"),
        APPLICANT_TYPE_ID("applicantTypeId"),
        TITLE_ID("titleId"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        FIRST_NAME_LOCAL("firstNameLocal"),
        LAST_NAME_LOCAL("lastNameLocal"),
        PHONE_TYPE_ID("phoneTypeId"),
        PHONE_NUMBER("phoneNumber"),
        REVISED_QUOTATION_NUMBER("revisedQuotationNumber"),
        ID_NUMBER("id_number"),
        ACTION_TYPE("actionType"),
        ACCESSORY_TYPE("accessoryType"),
        AMOUNT("amount"),
        ACCESSORY_DETAIL_ID("accessoryDetailId"),
        IDENTIFICATION_CODE("identificationCode"),
        ASSET_TYPE_CODE("assetTypeCode"),
        ASSET_SUBTYPE_CODE("asseSubtypeCode"),
        ASSET_USAGE_CODE("assetUsageCode"),
        ASSET_COST("assetCost"),
        RENTAL_MODE_CODE("rentalModeCode"),
        TERMS("terms"),
        PAYMENT_FREQUENCY_ID("paymentFrequencyId"),
        INTEREREST_RATE_TYPE("interestRateType"),
        INTEREREST_RATE("interestRate"),
        FLATE_RATE("flateRate"),
        BASE_RATE("baseRate"),
        SUBSIDY_RATE("subsidyRate"),
        DOWN_PAYMENT_PERCENTAGE("downPaymentPercentage"),
        DOWN_PAYMENT_AMOUNT("downPaymentAmount"),
        MAINTAINCE_FEE("maintainceFee"),
        FINANCED_CHARGES("financedCharges"),
        TOTAL_RENEWAL_TAX("totalRenewalTax"),
        RENEWAL_TAX_PER_MONTH("renewalTaxPerMonth"),
        RV_BALOON_PERCENTAGE("rvBaloonPercentage"),
        RV_BALOON_AMOUNT("rvBaloonAmount"),
        FV_PERCENTAGE("fvPercentage"),
        FV_AMOUNT("fvAmount"),
        SUBSIDY_PERCENTAGE("subsidyPercentage"),
        SUBSIDY_AMOUNT("subsidyAmount"),
        TOTAL_INTEREST_CHARGES("totalInterestCharges"),
        FINANCED_AMOUNT("financedAmount"),
        COMMISSION_AMOUNT("commissionAmount"),
        COMMISSION_PERCENTAGE("commissionPercentage"),
        EXTRA_COMMISSION_AMOUNT("extraCommissionAmount"),
        COMMISSION_TYPE("commissionType"),
        USED_CAR_COMMISSION_AMOUNT("usedCarCommissionAmount"),
        FIN_COMMISSION_AMOUNT("finCommissionamount"),
        FIN_COMMISSION_PCT("finCommissionPct"),
        FIN_COMMISSION_PCT_AMT("finCommissionPctAmt"),
        DEALER_COMMISSION_AMOUNT("dealerCommissionAmount"),
        DEALER_COMMISSION_PCT("dealerCommissionPct"),
        DEALER_COMMISSION_PCT_AMT("dealerCommissionPctAmt"),
        RESERVED_POOL_COMM_AMOUNT("reservedPoolCommAmount"),
        RESERVED_POOL_COMM_PCT_AMT("reservedPoolCommPctAmt"),
        DEALER_PARENT_COMM__AMT("dealerParentCommAmt"),
        BP_INDIVIDUAL_ID("bpIndividualid"),
        CHARGE_PERCENTAGE("chargesPercentage"),
        FEE_TYPE("feetype"),
        IS_FINANCED("isFinanced"),
        IS_AMORTIZED("isAmortized"),
        IS_DEDUCTED("isDeducted"),
        INSURANCE_TYPE("issuranceType"),
        PARENT_NAME("parentName"),
        APPLICATION_SEQUENCE("applicationSequence"),
        ITEM_STATUS("itemStatus"),
        ITEM_DESCRIPTION("itemDescription"),
        DESCRIPTION_LOCAL("descriptionLocal"),
        WINDOW_NAME("windowName"),
        ITEM_TYPE("itemType"),
        MARK_DELETION("markDeletion"),
        LOOKUP_DETAIL_ID("lookupDetailId"),
        NARRATION("narration"),
        LOOKUP_TYPE("lookupType"),
        SD_APP_IND("sdAppInd"),
        PREPAY_ROUND("prepayRound"),
        PREPAY_RND_IND("prepayRndInd"),
        PREPAY_PCT("prepayPct"),
        PREPAY_IND("prepayInd"),
        LEASE_TYP("leaseTyp"),
        ZERO_SUN_FEE_IND("zeroSunFeeInd"),
        APP_SUN_FEE_IND("appSunFeeInd"),
        ZERO_PT_IND("zeroPtInd"),
        APP_PT_IND("appPtInd"),
        ZERO_SER_FEE_IND("zeroSerFeeInd"),
        APP_SER_FEE_IND("appSerFeeInd"),
        ZERO_REG_FEE_IND("zeroRegFeeIng"),
        APP_REG_FEE_IND("appRegFeeInd"),
        ZERO_VT_IND("zeroVtInd"),
        APP_VT_IND("appVtInd"),
        ZERO_COMP_INS_IND("zeroCompInsInd"),
        APP_COMP_INS_IND("appCompInsInd"),
        OPERATING_BUSINESS("operatingBusiness"),
        COMMISSION_CHART_ID("commissionChartId"),
        SUBSIDY_CHART_ID("subsidyChartId"),
        SUBSIDY_TYPE("subsidyType"),
        USER_TYPE("userType"),
        MAXIMUM_FINANCING_AMOUNT("maximumFinancialAmount"),
        MAXIMUM_FINANCING_PCT("minimumFinancialPct"),
        CONTROL_NAME("controlName"),
        WINDOW_PATH("windowPath"),
        PRIORTY("priority");

        String MainParam;

        private MainParams(String param)
        {
            this.MainParam = param;
        }

        public String getValue()
        {
            return this.MainParam;
        }
    }

}
