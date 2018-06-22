package com.saurabh.stats.api;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saurabh.stats.dto.StatsDto;
import com.saurabh.stats.dto.TransactionDto;
import com.saurabh.stats.service.TransactionHandler;

/**
 * StatsController for saving transactions, resolving statistics and fetching
 * resolved statistics.
 * 
 * @author Kumar.Saurabh
 *
 */
@RestController
public class StatsController {

	// ConcurrentHashMap to ensure thread safety
	// For more practical scenario, singleton class can hold this data structure and
	// define operations for it
	private Map<Long, StatsDto> transactionMap = new ConcurrentHashMap<>();

	private final Long statsTimeIntervalInSec = 60l;

	@Autowired
	private TransactionHandler transactionHandler;

	/**
	 * Saves transaction in memory and calls async handler to delete old enteries
	 * 
	 * @param transactionDto
	 * @return ResponseEntity with status 201 (success) or 204 (issue)
	 */
	@PostMapping("/transactions")
	public ResponseEntity<Object> addTransaction(@RequestBody TransactionDto transactionDto) {

		// Initialize
		long inputTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(transactionDto.getTimestamp());
		long currentTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());

		// Validation
		if (inputTimestampInSeconds < currentTimestampInSeconds - statsTimeIntervalInSec) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

		// Build data for transaction map
		{
			StatsDto statsDto = transactionMap.get(inputTimestampInSeconds);
			double currentAmount = transactionDto.getAmount();

			if (statsDto != null) {
				// This is case when for same second but different millisec we get input
				statsDto.setMin(statsDto.getMin() < currentAmount ? statsDto.getMin() : currentAmount);
				statsDto.setMax(statsDto.getMax() > currentAmount ? statsDto.getMax() : currentAmount);
				statsDto.setCount(statsDto.getCount() + 1);
				statsDto.setSum(statsDto.getSum() + currentAmount);
			} else {
				statsDto = new StatsDto(1l, currentAmount, currentAmount, currentAmount);
			}

			transactionMap.put(inputTimestampInSeconds, statsDto);
		}

		// Async Call to delete older transactions to keep transactionMap lean
		transactionHandler.deleteTransaction(transactionMap, currentTimestampInSeconds, statsTimeIntervalInSec);

		// Success
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	/**
	 * Gives metrics for request in last 60 sec
	 * 
	 * @return StatsDto: This is having statistics
	 */
	@GetMapping("/statistics")
	public StatsDto getStats() {

		// Initialize
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		long count = 0;
		Double sum = 0.0;
		long currentTimestampInSeconds = TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli());

		// For any transaction map size - loop is constant for statsTimeIntervalInSec
		// (60 in our case)
		for (long i = currentTimestampInSeconds - statsTimeIntervalInSec; i < currentTimestampInSeconds; i++) {
			StatsDto statsDto2 = transactionMap.get(i);

			// Create metrics if we have valid data
			if (statsDto2 != null) {
				min = statsDto2.getMin() < min ? statsDto2.getMin() : min;
				max = statsDto2.getMax() > max ? statsDto2.getMax() : max;
				count = count + statsDto2.getCount();
				sum = sum + statsDto2.getSum();
			}
		}

		StatsDto statsDto = null;
		// Build response object when we have data
		if (count > 0) {
			statsDto = new StatsDto(count, min, max, sum);
		}

		// This could be null if no transactions are found in last
		// statsTimeIntervalInSec
		return statsDto;
	}

}
