package com.saurabh.stats.service;

import java.util.Map;

import com.saurabh.stats.dto.StatsDto;

public interface TransactionHandler {

	Boolean deleteTransaction(Map<Long, StatsDto> transactionMap, long fromTimeInSec, long statsTimeIntervalinSec);
}
