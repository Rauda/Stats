package com.saurabh.stats;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.saurabh.stats.dto.StatsDto;
import com.saurabh.stats.service.TransactionHandler;
import com.saurabh.stats.service.TransactionHandlerImpl;

@RunWith(SpringRunner.class)
public class TransactionHandlerTests {

	@TestConfiguration
	static class TransactionHandlerTestsContextConfiguration {

		@Bean
		public TransactionHandler transactionHandler() {
			return new TransactionHandlerImpl();
		}
	}

	@Autowired
	private TransactionHandler transactionHandler;
	
	@Test
	public void transactionDeleted() throws Exception{
		
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		
		StatsDto statsDto = new StatsDto();
		for(int i=0; i<100; i++) {
			long tmpTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());
			tmpTimestampInSeconds = tmpTimestampInSeconds -i;
			transactionMap.put(tmpTimestampInSeconds, statsDto);
		}
		
		
		long currentTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());
		Boolean isDeleted = transactionHandler.deleteTransaction(transactionMap , currentTimestampInSeconds, 60l);
		
		assertThat(isDeleted).isTrue();
	}
	
	@Test
	public void transactionNotDeleted() throws Exception{
		
		Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();
		
		StatsDto statsDto = new StatsDto();
		for(int i=0; i<60; i++) {
			long tmpTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());
			tmpTimestampInSeconds = tmpTimestampInSeconds -i;
			transactionMap.put(tmpTimestampInSeconds, statsDto);
		}
		
		
		long currentTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());
		Boolean isDeleted = transactionHandler.deleteTransaction(transactionMap , currentTimestampInSeconds, 60l);
		
		assertThat(isDeleted).isFalse();
	}

}
