package com.example.cities;

import com.example.cities.DTO.CityUpdateRequestDto;
import com.example.cities.controller.CityController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CityControllerTests {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCitiesByName() throws Exception {
        mockMvc.perform(get(CityController.URL + "/{name}", "Tokyo").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Tokyo"))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].photo").value("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"));
    }

    @Test
    void testGetCitiesThatHaveSameNameReturnsBoth() throws Exception {
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto();
        cityUpdateRequestDto.setName("Tokyo");
        cityUpdateRequestDto.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg");

        String json = objectMapper.writeValueAsString(cityUpdateRequestDto);
        mockMvc.perform(put(CityController.URL + "/{id}", 2).contentType(MediaType.APPLICATION_JSON).content(json));

        mockMvc.perform(get(CityController.URL + "/{name}", "Tokyo").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Tokyo"))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].photo").value("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Tokyo"))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[1].photo").value("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg"));
    }

    @Test
    void testGetCitiesByNameThatDoesNotExist() throws Exception {
        String invalidName = "ddsas";
        MvcResult result = mockMvc.perform(get(CityController.URL + "/{name}", invalidName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("the city with name " + invalidName + " was not found", content);
    }

    @Test
    void testGetCitiesByNameUsingBlankName() throws Exception {
        String blankName = " ";
        MvcResult result = mockMvc.perform(get(CityController.URL + "/{name}", blankName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("getCity.name: must not be blank", content);
    }

    @Test
    void testGetCitiesMaxPageValues() throws Exception {
        mockMvc.perform(get(CityController.URL).param("pageSize", "50")
                        .param("pageNumber", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("totalItems").value(1000))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("cities", Matchers.hasSize(50)))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("totalPages").value(20))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("currentPage").value(1));
    }

    @Test
    void testGetCitiesInvalidPageSize() throws Exception {
        MvcResult result = mockMvc.perform(get(CityController.URL).param("pageSize", "51")
                        .param("pageNumber", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("getAllCities.pageSize: must be less than or equal to 50", content);
    }

    @Test
    void testGetCitiesInvalidPageNumber() throws Exception {
        MvcResult result = mockMvc.perform(get(CityController.URL).param("pageSize", "50").param("pageNumber", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("getAllCities.pageNumber: must be greater than or equal to 0", content);
    }

    @Test
    void testUpdateCity() throws Exception {
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto();
        cityUpdateRequestDto.setName("Tokyo new");
        cityUpdateRequestDto.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg");

        String json = objectMapper.writeValueAsString(cityUpdateRequestDto);
        mockMvc.perform(put(CityController.URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(json));

        mockMvc.perform(get(CityController.URL + "/{name}", "Tokyo new").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))

                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Tokyo new"))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].photo").value("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg"));
    }

    @Test
    void testUpdateCityWithNoNameAddedInUpdate() throws Exception {
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto();
        cityUpdateRequestDto.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg");

        String json = objectMapper.writeValueAsString(cityUpdateRequestDto);
        mockMvc.perform(put(CityController.URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(json));

        MvcResult response = mockMvc.perform(get(CityController.URL + "/{name}", "Tokyo new").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        String content = response.getResponse().getContentAsString();
        Assertions.assertEquals("the city with name Tokyo new was not found", content);
    }

    @Test
    void testUpdateCityNoPhotoAddedInUpdate() throws Exception {
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto();
        cityUpdateRequestDto.setName("Tokyo new");

        String json = objectMapper.writeValueAsString(cityUpdateRequestDto);
        mockMvc.perform(put(CityController.URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(json));

        mockMvc.perform(get(CityController.URL + "/{name}", "Tokyo new").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))

                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].photo").doesNotExist());
    }


}
