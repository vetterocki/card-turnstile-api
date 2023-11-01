package org.example.web;


import static org.example.model.Interaction.InteractionType.SUCCESSFUL_ACCESS;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import org.example.testcontainer.TestContainerConfiguration;
import org.example.web.dto.TurnstileModifyDto;
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
class TurnstileControllerTest {
  private final String url = "turnstiles";

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
  @Sql("/sql/turnstiles.sql")
  void testFindingById() throws Exception {
    Long id = 2L;
    mockMvc.perform(get("/{url}/{id}", url, id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(id.intValue())));
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql", "/sql/reports.sql"})
  void testFindingAllReportsById() throws Exception {
    Long id = 2L;
    mockMvc.perform(get("/{url}/{id}/reports", url, id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }


  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql", "/sql/reports.sql"})
  void testFindingAllReportsGroupedByTravelCardTypeById() throws Exception {
    Long id = 2L;
    mockMvc.perform(get("/{url}/{id}/reports/grouped-by-type", url, id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", aMapWithSize(2)));
  }

  @Test
  void testTurnstileCreation() throws Exception {
    var modifyDto = new TurnstileModifyDto();
    modifyDto.setFarePrice(BigDecimal.TEN);
    String json = objectMapper.writeValueAsString(modifyDto);

    mockMvc.perform(post("/{url}", url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.id", notNullValue()),
            jsonPath("$.farePrice", is(BigDecimal.TEN.intValue()))
        );
  }

  @Test
  @Sql("/sql/turnstiles.sql")
  void testPartialUpdate() throws Exception {
    var modifyDto = new TurnstileModifyDto();
    var farePrice = BigDecimal.TEN;
    modifyDto.setFarePrice(farePrice);
    Long id = 2L;
    String json = objectMapper.writeValueAsString(modifyDto);

    mockMvc.perform(patch("/{url}/{id}", url, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.farePrice", is(farePrice.intValue())));
  }

  @Test
  @Sql({"/sql/turnstiles.sql", "/sql/travel-cards.sql"})
  void testTurnstilePassing() throws Exception {
    Long turnstileId = 2L;
    Long travelCardId = 3L;

    mockMvc.perform(patch("/{url}/{id}/pass/{card-id}", url, turnstileId, travelCardId))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.interactionType", is(SUCCESSFUL_ACCESS.name())),
            jsonPath("$.description", equalTo("Successful access")),
            jsonPath("$.time", notNullValue())
        );
  }

  @ParameterizedTest
  @MethodSource("deleteTestData")
  void testDeletion(Long id, ResultMatcher expectedStatus) throws Exception {
    mockMvc.perform(delete("/{url}/{id}", url, id)).andExpect(expectedStatus);
    assertFalse(turnstileRepository.existsById(id));
  }

  private static Stream<Arguments> deleteTestData() {
    return Stream.of(
        Arguments.of(2L, status().isNoContent()),
        Arguments.of(13142L, status().isNoContent())
    );
  }



}
