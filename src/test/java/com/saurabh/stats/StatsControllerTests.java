package com.saurabh.stats;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.saurabh.stats.dto.StatsDto;
import com.saurabh.stats.dto.TransactionDto;
import com.saurabh.stats.service.TransactionHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StatsControllerTests {

	@MockBean
	private TransactionHandler transactionHandler;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void isTransactionCreated() throws Exception {

		// Given
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		given(transactionHandler.deleteTransaction(transactionMap, 10l, 10l)).willReturn(true);

		// when
		TransactionDto transactionDto = new TransactionDto(22.12, Instant.now().toEpochMilli());
		ResponseEntity<Object> response = restTemplate.postForEntity("/transactions", transactionDto, Object.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

	}

	@Test
	public void isTransactionNoContent() throws Exception {

		// Given
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		given(transactionHandler.deleteTransaction(transactionMap, 10l, 10l)).willReturn(true);

		// when
		TransactionDto transactionDto = new TransactionDto(23.12, Instant.now().toEpochMilli() - (100 * 1000));
		ResponseEntity<Object> response = restTemplate.postForEntity("/transactions", transactionDto, Object.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

	}

	@Test
	public void isStatisticsWorking() throws Exception {

		// when
		ResponseEntity<StatsDto> response = restTemplate.getForEntity("/statistics", StatsDto.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNull();
	}

	@Test
	public void validateStatistics() throws Exception {

		// Given
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		given(transactionHandler.deleteTransaction(transactionMap, 10l, 10l)).willReturn(true);

		for (int i = 0; i < 10; i++) {
			// when
			TimeUnit.SECONDS.sleep(1);
			TransactionDto transactionDto = new TransactionDto(22.12 + i, Instant.now().toEpochMilli() - i * 1000);
			ResponseEntity<Object> response = restTemplate.postForEntity("/transactions", transactionDto, Object.class);

			// then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		}

		// when
		ResponseEntity<StatsDto> response = restTemplate.getForEntity("/statistics", StatsDto.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		
		// Note: WHEN THIS TEST IS RUN INDIVIDUALLY all values will differ as count will be 10
		// count is 11 as one of test above has already created a transaction data
		StatsDto statsDto = new StatsDto(11, 22.12, 31.12, 288.32);
		assertThat(response.getBody().equals(statsDto)).isTrue();

	}
}
