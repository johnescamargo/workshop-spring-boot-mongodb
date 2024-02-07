package com.imav.whatsapp.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.entity.WeekTime;
import com.imav.whatsapp.repository.WeekTimeRepository;

@Service
public class WeekUtil {

	@Autowired
	private WeekTimeRepository weekTimeRepo;

	public boolean checkIfDayIsOff() {
		
		//if true, talk

		boolean response = false;

		int[] timeNow = getTimeNow();
		int day = timeNow[0];

		response = getDbDayOff(day);

		if (response) {

			int[] week = getDbTime(day);
			if (timeNow[1] >= week[0] && timeNow[1] <= week[1]) {
				response = true;
			} else {
				response = false;
			}

		} else {
			response = false;
		}

		return response;

	}

	public int[] getTimeNow() {

		int[] week = new int[2];

		LocalDate currentDate = LocalDate.now();
		Date date = Date.valueOf(currentDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateTimeFormatter timeDt = DateTimeFormatter.ofPattern("HHmm");
		LocalDateTime now = LocalDateTime.now();
		String timeStr = String.valueOf(timeDt.format(now));

		week[0] = cal.get(Calendar.DAY_OF_WEEK);
		week[1] = Integer.parseInt(timeStr);

		return week;

	}

	public int[] getDbTime(int day) {
		int[] week = new int[2];

		try {

			WeekTime date = weekTimeRepo.getByDay(day);

			week[0] = Integer.parseInt(date.getDayInit().replace(":", ""));
			week[1] = Integer.parseInt(date.getDayEnd().replace(":", ""));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return week;
	}

	public boolean getDbDayOff(int day) {
		boolean resp = false;

		try {

			WeekTime date = weekTimeRepo.getByDay(day);
			resp = date.isDayOff();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

}
