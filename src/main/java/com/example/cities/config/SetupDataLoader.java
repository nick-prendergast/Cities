package com.example.cities.config;

import com.example.cities.model.City;
import com.example.cities.repo.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.Reader;

@Slf4j
@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${csvImportFileName}")
    private String csvFile;

    @Autowired
    CityRepository cityRepository;
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup || cityRepository.findAll().iterator().hasNext()) {
            return;
        }
        insertCitiesFromCSV();
        alreadySetup = true;
    }

    void insertCitiesFromCSV() {
        try (Reader in = new FileReader(new ClassPathResource(csvFile).getFile())) {
            log.info("importing from csv {}", csvFile);
            CSVFormat.RFC4180.builder()
                    .setAllowMissingColumnNames(true).setHeader("id", "name", "photo")
                    .setSkipHeaderRecord(true).build().parse(in).forEach(record -> {
                        City city = new City();
                        city.setName(record.get("name"));
                        city.setPhoto(record.get("photo"));
                        cityRepository.save(city);
                    });

        } catch (Exception e) {
            log.error("Unable to read CSV file", e);
        }
    }

}