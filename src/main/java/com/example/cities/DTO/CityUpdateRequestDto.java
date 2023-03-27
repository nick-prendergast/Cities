package com.example.cities.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityUpdateRequestDto {

    @Size(min = 1, max = 50)
    private String name;

    @URL(message = "must be a url")
    @Pattern(regexp = ".*?(gif|jpeg|png|jpg|bmp)", message = "must be a valid image extension")
    private String photo;


}
