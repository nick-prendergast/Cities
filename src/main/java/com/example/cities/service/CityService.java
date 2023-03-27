package com.example.cities.service;

import com.example.cities.DTO.CityUpdateRequestDto;
import com.example.cities.model.City;
import com.example.cities.repo.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> getCity(String name) {
        List<City> cities = cityRepository.findByName(name);
        if (cities.isEmpty()) {
            throw new EntityNotFoundException("the city with name " + name + " was not found");
        }
        return cities;
    }

    public ResponseEntity<Map<String, Object>> getAllCities(Integer page, Integer pageSize) {
        try {
            Pageable paging = PageRequest.of(page, pageSize);
            Page<City> pageCities = cityRepository.findAll(paging);
            List<City> cities = pageCities.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("cities", cities);
            response.put("currentPage", pageCities.getNumber());
            response.put("totalItems", pageCities.getTotalElements());
            response.put("totalPages", pageCities.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<City> updateCity(long id, CityUpdateRequestDto cityUpdateRequestDto) {
        City updateCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("the city with id:" + id + " does not exist"));

        updateCity.setName(cityUpdateRequestDto.getName());
        updateCity.setPhoto(cityUpdateRequestDto.getPhoto());
        cityRepository.save(updateCity);
        return ResponseEntity.ok(updateCity);
    }

}
