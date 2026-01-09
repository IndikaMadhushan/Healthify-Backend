package com.healthcare.personal_health_monitoring.config;

import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MasterDataInitializer implements CommandLineRunner {

    private final DiseaseRepository diseaseRepository;
    private final AllergyRepository allergyRepository;

    @Override
    public void run(String... args) {

        seedDiseases();
        seedAllergies();
    }


    //diseasis
    private void seedDiseases() {

        List<Disease> diseases = List.of(
                new Disease(null, "Diabetes Mellitus", "E11"),
                new Disease(null, "Hypertension", "I10"),
                new Disease(null, "Asthma", "J45"),
                new Disease(null, "Coronary Artery Disease", "I25"),
                new Disease(null, "Chronic Kidney Disease", "N18"),
                new Disease(null, "Tuberculosis", "A15"),
                new Disease(null, "Dengue Fever", "A90"),
                new Disease(null, "COVID-19", "U07.1"),
                new Disease(null, "Migraine", "G43"),
                new Disease(null, "Epilepsy", "G40")
        );

        for (Disease disease : diseases) {
            diseaseRepository.findByName(disease.getName())
                    .orElseGet(() -> diseaseRepository.save(disease));
        }
    }

    //allergies

    private void seedAllergies() {

        List<Allergy> allergies = List.of(
                new Allergy(null, "Penicillin", AllergyType.DRUG),
                new Allergy(null, "Aspirin", AllergyType.DRUG),
                new Allergy(null, "Sulfa Drugs", AllergyType.DRUG),
                new Allergy(null, "Peanuts", AllergyType.NON_DRUG),
                new Allergy(null, "Seafood", AllergyType.NON_DRUG),
                new Allergy(null, "Dust Mites", AllergyType.NON_DRUG),
                new Allergy(null, "Pollen", AllergyType.NON_DRUG),
                new Allergy(null, "Latex", AllergyType.NON_DRUG),
                new Allergy(null, "Milk", AllergyType.NON_DRUG),
                new Allergy(null, "Eggs", AllergyType.NON_DRUG)
        );

        for (Allergy allergy : allergies) {
            allergyRepository.findByName(allergy.getName())
                    .orElseGet(() -> allergyRepository.save(allergy));
        }
    }
}
