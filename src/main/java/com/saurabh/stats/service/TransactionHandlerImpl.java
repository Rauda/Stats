package com.saurabh.stats.service;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.saurabh.stats.dto.StatsDto;

@Service
public class TransactionHandlerImpl implements TransactionHandler {

	/**
	 * Async call to ensure actual thread is not blocked for this activity This
	 * method deletes data from transactionMap where key value is older than
	 * statsTimeIntervalinSec from fromTimeInSec i.e, all data except in between (
	 * fromTimeInSec - statsTimeIntervalinSec) will be deleted
	 * 
	 * @transactionMap : Map on which operation will be done
	 * @param transactionMap:
	 *            map on which delete operation will be done
	 * @param fromTimeInSec:
	 *            time (in seconds) from which data will be deleted
	 * @param statsTimeIntervalinSec:
	 *            time internal (in seconds) for which data will be deleted
	 * @return whether any data is deleted or not
	 */
	@Async
	@Override
	public Boolean deleteTransaction(Map<Long, StatsDto> transactionMap, long fromTimeInSec,
			long statsTimeIntervalinSec) {
		boolean isDeleted = transactionMap.entrySet()
				.removeIf(e -> e.getKey() < fromTimeInSec - statsTimeIntervalinSec);
		return isDeleted;
	}

}
