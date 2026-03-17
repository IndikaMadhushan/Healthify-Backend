package com.healthcare.personal_health_monitoring.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PatientMedicalInfoDto {

    private MedicalInfoSection medicalInfo = new MedicalInfoSection();
    private LifestyleAndAllergiesSection lifestyleAndAllergies = new LifestyleAndAllergiesSection();
    private ParentMedicalInfoSection parentMedicalInfo = new ParentMedicalInfoSection();

    @Data
    public static class MedicalInfoSection {
        private ChronicSection chronic = new ChronicSection();
        private VaccinesSection vaccines = new VaccinesSection();
        private List<SurgeryItem> surgeries = new ArrayList<>();
    }

    @Data
    public static class ChronicSection {
        private List<String> chronicIllnesses = new ArrayList<>();
        private String otherChronic = "";
        private String cancerChronic = "";
    }

    @Data
    public static class VaccinesSection {
        private List<String> takenVaccines = new ArrayList<>();
        private String otherVaccine = "";
    }

    @Data
    public static class SurgeryItem {
        @JsonAlias({"reason", "description"})
        private String surgeonName;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate surgeryDate;

        private String hospital;
        private String complications;
    }

    @Data
    public static class LifestyleAndAllergiesSection {
        private String smokingStatus = "";
        private String smokingFrequency = "";
        private String alcoholStatus = "";
        private String alcoholFrequency = "";
        private String drugUseStatus = "";
        private String drugUseFrequency = "";
        private String stressLevel = "";
        private String foodAllergies = "";
        private String drugAllergies = "";
    }

    @Data
    public static class ParentMedicalInfoSection {
        private List<String> chronicIllnesses = new ArrayList<>();
        private String otherChronic = "";
    }
}
