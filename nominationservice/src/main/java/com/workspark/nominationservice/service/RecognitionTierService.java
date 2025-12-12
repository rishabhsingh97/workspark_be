package com.workspark.nominationservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.workspark.nominationservice.model.RecognitionCategory;
import com.workspark.nominationservice.model.RecognitionTier;
import com.workspark.nominationservice.repository.RecognitionCategoryRepository;
import com.workspark.nominationservice.repository.RecognitionTierRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecognitionTierService {

    private final RecognitionCategoryRepository recognitionCategoryRepository;
    private final RecognitionTierRepository recognitionTierRepository;

    // Constructor injection for the required repositories
    public RecognitionTierService(RecognitionCategoryRepository recognitionCategoryRepository,
                                  RecognitionTierRepository recognitionTierRepository) {
        this.recognitionCategoryRepository = recognitionCategoryRepository;
        this.recognitionTierRepository = recognitionTierRepository;
    }

    /**
     * Scheduled method to process nomination cycles. 
     * Runs daily at midnight based on the configured cron expression.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void processNominationCycles() {
        LocalDate today = LocalDate.now();
        System.out.println("Scheduler triggered. Today's date: " + today);

        // Fetch all recognition categories
        List<RecognitionCategory> categories = recognitionCategoryRepository.findAll();
//
//        for (RecognitionCategory category : categories) {
//            // Check if today matches the nomination open date
//            if (category.getNominationOpenDate().toLocalDate().equals(today)) {
//                createRecognitionTiersForCategory(category);
//            }
//
//            // Check if today is one day after the nomination end date
//            if (category.getNominationEndDate().toLocalDate().plusDays(1).equals(today)) {
//                updateNominationCycleDates(category);
//            }
//        }
    }

    /**
     * Dynamically creates Recognition Tiers for a given category.
     * 
     * @param category the recognition category for which tiers are created
     */
    private void createRecognitionTiersForCategory(RecognitionCategory category) {
        System.out.println("Creating tiers for category: " + category.getCategoryName());

        RecognitionTier tier = new RecognitionTier();
        tier.setCategoryId(category.getCategoryId());
        tier.setName(category.getCategoryName() + " - Tier 1"); // Example tier naming logic
        tier.setDescription("Auto-created tier for nominations");
        tier.setStartDate(LocalDateTime.now());
       // tier.setEndDate(category.getNominationEndDate());
        tier.setIsActive(true);
        tier.setCreatedDateTime(LocalDateTime.now());
        tier.setUpdatedDateTime(LocalDateTime.now());

        recognitionTierRepository.save(tier);

        System.out.println("Created tier: " + tier.getName());
    }

    /**
     * Updates the nomination start and end dates for the next cycle.
     * 
     * @param category the recognition category to update
     */
    private void updateNominationCycleDates(RecognitionCategory category) {
        System.out.println("Updating nomination cycle for category: " + category.getCategoryName());

        // Adjust the nomination open and end dates for the next cycle
//        category.setNominationOpenDate(category.getNominationOpenDate().plusMonths(1));
//        category.setNominationEndDate(category.getNominationEndDate().plusMonths(1));

        recognitionCategoryRepository.save(category);

        System.out.println("Updated nomination dates for category: " + category.getCategoryName());
    }
}
