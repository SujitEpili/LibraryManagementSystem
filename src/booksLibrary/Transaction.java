package booksLibrary;

import java.time.*;

public class Transaction 
{
	String transactionId;
	Member member;
	Book book;
	LocalDateTime issueDate;
	LocalDateTime returnDate;
	boolean isReturned;
	
	void markAsReturned()
	{
		isReturned = true;
	}

}
