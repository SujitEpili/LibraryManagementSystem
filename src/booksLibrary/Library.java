package booksLibrary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Library
{
	protected String libraryName = "Boost Mind Library";
	protected String libraryAddress = "Adventure Park";
	protected static HashMap<String, Book> books = new HashMap<>();
	protected static HashMap<String, Member> members = new HashMap<>();
	protected static HashMap<String, Librarian> librarians = new HashMap<>();
	protected static LinkedHashMap<String, Transaction> transactions = new LinkedHashMap<>();
	protected static LinkedHashMap<String, Fine> fines = new LinkedHashMap<>();
	
	boolean isBookPresent = false;
	boolean anyBookOfAuthor = false;
	boolean bookAvailable = false;
	boolean isFinePresent = false;
	
	static Scanner sc = new Scanner(System.in);
		
	
	void addBook(Book book)
	{
		if(books.containsKey(book.ISBN))
		{
			System.out.println("\nA Book is already present of ISBN " + book.ISBN + ", try to give unique ISBN");
		}else
		{
			books.put(book.ISBN, book);
			book.updateAvailability(true);
			System.out.println("\nThe book " + book.title + " is added to the library of ISBN : " + book.ISBN);
		}
	}
	
	void removeBook(Book book)
	{
		System.out.print("Enter book's ISBN number which you want to remove :");
		book.ISBN = sc.next();
		if(books.containsKey(book.ISBN))
		{
			books.remove(book.ISBN);
			System.out.println("\nA Book of ISBN " + book.ISBN + " is removed from the library");
		}else
		{
			System.out.println("\nBook with ISBN "+ book.ISBN + " is not there in library !!!");
		}
	}
	
	void registerMember(Member member)
	{
		System.out.print("Enter the new member name : ");
		member.name = sc.next();
		System.out.print("Enter member's contact number : ");
		member.contactNumber = sc.next();
		member.memberId = member.name + member.contactNumber;
		System.out.print("Enter member's address : ");
		member.address = sc.next();
		if(members.containsKey(member.memberId))
		{
			System.out.println("\nMember " + member.memberId + " is already present !!!");
		}else
		{
			members.put(member.memberId, member);
			System.out.println("\nMember created with member id : " + member.memberId);
		}
	}
	
	void removeMember(Member member)
	{
		System.out.print("Enter memberId(name+contact) :");
		member.memberId = sc.next();
		if(members.containsKey(member.memberId))
		{
			System.out.println("\nMeber " + member.memberId + " is removed from library");
			members.remove(member.memberId);
		}else
		{
			System.out.println("\nMember " + member.memberId + " not found !!!");
		}
	}
	
	HashSet<Book> searchBook(String title)
	{
		HashSet<Book> matchedBooks = new HashSet<>();
		System.out.println("--Available books of this name--");
		books.forEach((ISBN, book) -> {
			if(book.title.equals(title))
			{
				isBookPresent = true;
				matchedBooks.add(book);
				System.out.println(book.title);
			}
		});
		if(!isBookPresent)
		{
			System.out.println("\nNo book present with this name !!!");
		}
		return matchedBooks;
	}
	
	HashSet<Book> searchBookByAuthor(String author)
	{
		HashSet<Book> matchedBooks = new HashSet<>(); 
		System.out.println("--Available books of this author--");
		books.forEach((ISBN, book) -> {
			if(book.author.equals(author))
			{
				anyBookOfAuthor = true;
				matchedBooks.add(book);
				System.out.println(book.title);
			}
		});
		if(!anyBookOfAuthor)
		{
			System.out.println("\nNo book present with this author !!!");
		}
		return matchedBooks;
	}
	
	HashMap<String, Book> listAvailableBooks()
	{
		HashMap<String, Book> availableBook = new HashMap<>();
		System.out.println("--Available books--");
		books.forEach((ISBN, book) -> {
			if(book.isAvailable)
			{
				bookAvailable = true;
				availableBook.put(ISBN, book);
				System.out.println(book.title);
			}
		});
		if(!bookAvailable)
		{
			System.out.println("\nThere are no available books present inside library !!!");
		}
		return availableBook;
	}
	
	void openFineBook()
	{
		if(fines.size() == 0)
		{
			System.out.println("\nFine book is empty !!!");
		}
		else
		{
			System.out.println("--Fine Book--");
			fines.forEach((id, fine) -> {
				System.out.println("Fine id : " + fine.fineId);
				System.out.println("Member : " + fine.member.memberId);
				System.out.println("Fine amount : " + fine.amount);
				System.out.println("Fine issue date : " + fine.issueDate);
				System.out.println("Is paid : " + fine.isPaid);
			});
		}
	}

}
