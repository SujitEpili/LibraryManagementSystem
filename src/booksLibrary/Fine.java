package booksLibrary;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Fine
{
	String fineId;
	Member member;
	double amount;
	LocalDateTime issueDate;
	boolean isPaid;
	
	void calculateFine(LocalDateTime dueDate, LocalDateTime returnDate)
	{
		long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
		while(daysOverdue > 0)
		{
			amount += 10;  // per day 10unit fine
		}	
	}
	void markAsPaid()
	{
		isPaid = true;
	}


}
