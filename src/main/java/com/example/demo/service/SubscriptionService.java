package com.example.demo.service;

import java.util.List;
import com.example.demo.model.AccessCode;

public interface SubscriptionService {
	List<AccessCode> getCodeCtime(Long c_time);
	List<AccessCode> getCodeCtimeById(Long c_time, String id);
	int saveCode(String code,Long c_time);
	int saveCodeById(String code,Long c_time, String id);
}
