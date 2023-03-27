package com.example.cities.controller;

import com.example.cities.DTO.CityUpdateRequestDto;
import com.example.cities.model.City;
import com.example.cities.service.CityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(CityController.URL)
@Validated
public class CityController {

    public static final String URL = "/cities";

    private final CityService cityService;

    @GetMapping("/{name}")
    public List<City> getCity(@PathVariable @NotBlank String name) {
        return cityService.getCity(name);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCities(@RequestParam @Min(0) int pageNumber,
                                                            @RequestParam @Min(1) @Max(50)
                                                            int pageSize) {
        return cityService.getAllCities(pageNumber, pageSize);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable @Positive Long id, @Valid @RequestBody CityUpdateRequestDto cityUpdateRequestDto) {
        return cityService.updateCity(id, cityUpdateRequestDto);
    }


}
