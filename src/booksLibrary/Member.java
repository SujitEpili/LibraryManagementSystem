package booksLibrary;

import java.util.HashMap;

public class Member extends Librarian
{
	String memberId;
	String name;
	String address;
	String contactNumber;
	HashMap<String, Book> borrowedBooks = new HashMap<>();
	
	
	void borrowBook(Book book)
	{
		super.issueBook(book, this);
	}
	
	void returnBook(Book book)
	{
		super.receiveReturnedBook(book, this);
	}
	
	HashMap<String, Book> listBorrowedBooks()
	{
		return borrowedBooks;
	}

}
