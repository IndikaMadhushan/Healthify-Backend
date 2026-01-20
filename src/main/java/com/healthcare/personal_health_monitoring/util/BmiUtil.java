package com.healthcare.personal_health_monitoring.util;

public class BmiUtil {

    public static Double calculateBmi(Double weightKg, Double heightCm) {
        if (weightKg == null || heightCm == null || heightCm == 0) {
            return null;
        }

        double heightM = heightCm / 100.0;
        return Math.round((weightKg / (heightM * heightM)) * 100.0) / 100.0;
    }

    public static String getBmiCategory(double bmi) {

        if (bmi < 18.5) return "UNDERWEIGHT";
        else if (bmi < 25) return "NORMAL";
        else if (bmi < 30) return "OVERWEIGHT";
        else return "OBESE";
    }

    public static String getHealthTip(String category) {
        return switch (category) {
            case "UNDERWEIGHT" -> "Increase protein and calorie intake. Consult your doctor.";
            case "NORMAL" -> "Great job! Maintain a balanced diet and exercise regularly.";
            case "OVERWEIGHT" -> "Consider reducing sugar and fat intake. Add daily exercise.";
            case "OBESE" -> "You should consult a doctor. Regular exercise and diet control are essential.";
            default -> "No tip available.";
        };
    }


}
