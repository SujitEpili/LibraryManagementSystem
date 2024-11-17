package booksLibrary;

public class Book
{
	String title;
	String author;
	String ISBN;
	int publicationYear;
	boolean isAvailable;
	
	
	void updateAvailability(boolean status)
	{
		isAvailable = status;
	}
	
	String getDetails()
	{
		String details = "Title : " + title + ", Author" + author + " Publication year : " + publicationYear;
		return details;
	}
}
