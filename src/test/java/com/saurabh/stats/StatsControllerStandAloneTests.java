package com.saurabh.stats;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurabh.stats.api.StatsController;
import com.saurabh.stats.dto.StatsDto;
import com.saurabh.stats.dto.TransactionDto;
import com.saurabh.stats.service.TransactionHandler;
import static org.mockito.BDDMockito.given;

//@RunWith(MockitoJUnitRunner.class)
//@WebMvcTest(StatsController.class)
public class StatsControllerStandAloneTests {
/*
	private MockMvc mvc;

	@InjectMocks
	private StatsController statsController;

	@Mock
	private TransactionHandler transactionHandler;

	private JacksonTester<TransactionDto> jsonTransactionDto;

	@Before
	public void setup() {

		JacksonTester.initFields(this, new ObjectMapper());

		mvc = MockMvcBuilders.standaloneSetup(statsController).build();
	}

	@Test
	public void getStatistics() throws Exception {

		// when
		MockHttpServletResponse response = mvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	public void saveTransactionCreated() throws Exception {

		// Given
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		given(transactionHandler.deleteTransaction(transactionMap, 10l, 10l)).willReturn(true);

		TransactionDto transactionDto = new TransactionDto(23.12, Instant.now().toEpochMilli());

		// when
		MockHttpServletResponse response = mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(jsonTransactionDto.write(transactionDto).getJson())).andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

	}

	@Test
	public void saveTransactionNoContent() throws Exception {

		// Given
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		given(transactionHandler.deleteTransaction(transactionMap, 10l, 10l)).willReturn(true);

		TransactionDto transactionDto = new TransactionDto(23.12, Instant.now().toEpochMilli() - (100 * 1000));

		// when
		MockHttpServletResponse response = mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(jsonTransactionDto.write(transactionDto).getJson())).andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

	}
	*/
}
