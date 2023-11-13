package org.example.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.example.data.TravelCardRepository;
import org.example.data.TurnstileRepository;
import org.example.model.card.TravelAmount;
import org.example.model.card.TravelCardType;
import org.example.model.card.ValidityPeriod;
import org.example.testcontainer.TestContainerConfiguration;
import org.example.web.dto.card.DefaultTravelCardModifyDto;
import org.example.web.dto.card.LoyaltyTravelCardModifyDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@SpringBootTest
class TravelCardControllerTest {
  private final String url = "travel-cards";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TravelCardRepository travelCardRepository;

  @Autowired
  private TurnstileRepository turnstileRepository;

  @AfterEach
  public void deleteAll() {
    travelCardRepository.deleteAll();
    turnstileRepository.deleteAll();
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testFindingAll() throws Exception {
    mockMvc.perform(get("/{url}", url))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testFindingById() throws Exception {
    Long id = 3L;
    mockMvc.perform(get("/{url}/{id}", url, id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(id.intValue())));
  }

  @Test
  void testDefaultTravelCardCreation() throws Exception {
    DefaultTravelCardModifyDto modifyDto = new DefaultTravelCardModifyDto();
    modifyDto.setTravelCardType(TravelCardType.SCHOOL);
    modifyDto.setJsonType("default");
    modifyDto.setTravelAmount(TravelAmount.TEN);
    modifyDto.setValidityPeriod(ValidityPeriod.TEN_DAYS);
    String json = objectMapper.writeValueAsString(modifyDto);

    mockMvc.perform(post("/{url}", url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.id", notNullValue()),
            jsonPath("$.lastPassedId", nullValue()),
            jsonPath("$.travelCardType", is(TravelCardType.SCHOOL.name())),
            jsonPath("$.expiresAt", notNullValue()),
            jsonPath("$.travelsLeft", is(10))
        );
  }

  @Test
  void testLoyaltyTravelCardCreation() throws Exception {
    LoyaltyTravelCardModifyDto modifyDto = new LoyaltyTravelCardModifyDto();
    modifyDto.setJsonType("loyalty");
    modifyDto.setCardBalance(BigDecimal.TEN);
    String json = objectMapper.writeValueAsString(modifyDto);

    mockMvc.perform(post("/{url}", url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.id", notNullValue()),
            jsonPath("$.lastPassedId", nullValue()),
            jsonPath("$.travelCardType", is(TravelCardType.ORDINARY.name())),
            jsonPath("$.cardBalance", is(BigDecimal.TEN.toString()))
        );
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testPartialUpdate() throws Exception {
    var modifyDto = new DefaultTravelCardModifyDto();
    modifyDto.setTravelAmount(TravelAmount.TEN);
    modifyDto.setJsonType("default");
    modifyDto.setTravelCardType(TravelCardType.SCHOOL);

    Long id = 3L;
    String json = objectMapper.writeValueAsString(modifyDto);

    mockMvc.perform(patch("/{url}/{id}", url, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.travelsLeft", is(10)),
            jsonPath("$.expiresAt", notNullValue())
        );
  }

  @ParameterizedTest
  @MethodSource("deleteTestData")
  void testDeletion(Long id, ResultMatcher expectedStatus) throws Exception {
    mockMvc.perform(delete("/{url}/{id}", url, id)).andExpect(expectedStatus);
    assertFalse(travelCardRepository.existsById(id));
  }

  private static Stream<Arguments> deleteTestData() {
    return Stream.of(
        Arguments.of(3L, status().isNoContent()),
        Arguments.of(13142L, status().isNoContent())
    );
  }


}
