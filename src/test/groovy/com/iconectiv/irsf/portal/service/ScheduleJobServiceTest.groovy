package com.iconectiv.irsf.portal.service

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory

/**
 * Created by echang on 5/31/2017.
 */
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ["classpath:spring-cfg.xml", "classpath:spring-jpa.xml"])
@WebAppConfiguration
class ScheduleJobServiceTest {
    def log = LoggerFactory.getLogger(ScheduleJobServiceTest.class)
    @Autowired
    ScheduleJobService scheduleService

    @Test
    void testMobileIDUpdate() {
        scheduleService.checkNewMobileIdUpdate()
    }
}
