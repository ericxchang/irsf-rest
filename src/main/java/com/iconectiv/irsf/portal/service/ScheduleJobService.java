package com.iconectiv.irsf.portal.service;

public interface ScheduleJobService {
	void checkNewMobileIdUpdate();
	void partitionStaleNotify();
}
