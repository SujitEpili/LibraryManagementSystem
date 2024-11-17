package booksLibrary;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Librarian extends Library
{
	String librarianId;
	String name;
	String email;
	String contactNumber;

	boolean isTaken = false;
	boolean isFound = false;
	boolean isAvailable = false;
	boolean memberFound = false;
	
	void addBook(Book book)
	{
		System.out.print("Add Book name : " );
		book.title = sc.next();
		System.out.print("Who is the author of this book : ");
		book.author = sc.next();
		System.out.print("Enter ISB Number : ");
		book.ISBN = sc.next();
		System.out.print("Enter this book's publication year : ");
		book.publicationYear = sc.nextInt();
		super.addBook(book);
	}
	
	void removeBook(Book book)
	{
		super.removeBook(book);
	}
	
	void issueBook(Book book, Member member)
	{
		System.out.print("Enter your memberId(name+contact) : ");
		String memberId = sc.next();
		System.out.print("Which book you want : ");
		String bookName = sc.next();
		super.members.forEach((eachMemberId, item) -> { // here item is for every member
			if(item.memberId.equals(memberId))
			{
				member.memberId = item.memberId;
				member.name = item.name;
				member.address = item.address;
				member.contactNumber = item.contactNumber;
				member.borrowedBooks = item.borrowedBooks;
				memberFound = true;
				return;
			}
		});
		if(memberFound == false)
		{
			System.out.println("\nMember " + memberId + " does not exist !!!");
			return;
		}
		super.books.forEach((ISBN, item) -> {  // here item is every book
			if(item.title.equals(bookName)) // isAvailable checks for multiple books of same name
			{								// and we have to restrict member to the take same  
											// book twice without returning
				isFound = true;
				if(item.isAvailable)
				{
					isAvailable = true;
					book.title = item.title;
					book.author = item.author;
					book.ISBN = item.ISBN;
					book.publicationYear = item.publicationYear;
					book.isAvailable = item.isAvailable;
					return;
				}
			}
		});
		if(isAvailable == false)
		{
			System.out.println("\nBook " + bookName +
					" is not available right now !!!");
		}
		if(isFound == false)
		{
			System.out.println("\nBook " + bookName + " does not exist !!!");
		}		
		if(super.books.containsKey(book.ISBN) && super.members.containsKey(member.memberId) )
		{
			if(book.isAvailable)
			{				
				member.borrowedBooks.forEach((ISBNumber, eachBook) -> {
					if(book.title.equals(eachBook.title))
					{
						isTaken = true;
						System.out.println("\nAnd book " + book.title + " already taken by " + member.memberId 
								+ " of ISBN " + eachBook.ISBN);
						return;
					}
				});
				if(isTaken == false)
				{
					super.books.forEach((ISBN, item) ->{ // here item is for every book
						if(item.isAvailable && book.ISBN.equals(item.ISBN))
						{
							Transaction transaction = new Transaction();
							transaction.issueDate = LocalDateTime.now();
							int trLength = super.transactions.size(); // it makes each transaction unique
							String s = String.valueOf(++trLength);	      // as a serial number
							transaction.transactionId = s;
							transaction.book = book;
							transaction.member = member;
							super.transactions.put(transaction.transactionId, transaction);
//							super.books.remove(book.ISBN); //we can't remove because we have to mark as unavailable
							
							book.updateAvailability(false);
							member.borrowedBooks.put(book.ISBN, book);
							System.out.println("\nTotal books taken by member " + member.memberId + " is " +
									member.borrowedBooks.size());
							System.out.println("\nBook " + book.title + " of ISBN " + book.ISBN + 
									" is given to " + member.memberId);
							item.updateAvailability(false);
							return;
						}
					});
				}
			}
			else
			{
				System.out.println("\nBook " + book.title +
						" is not available right now!!!, it was taken by somebody else");
			}
		}
	}
	
	void receiveReturnedBook(Book book, Member member)
	{
		System.out.print("Enter your memberId(name+contact) : ");
		String memberId = sc.next();
		System.out.print("Which book you are returning : ");
		String bookName = sc.next();
		
		super.members.forEach((eachMemberId, item) -> { // here item is for every member
			if(item.memberId.equals(memberId))
			{
				member.memberId = item.memberId;
				member.name = item.name;
				member.address = item.address;
				member.contactNumber = item.contactNumber;
				member.borrowedBooks = item.borrowedBooks;
				return;
			}
		});	
		member.borrowedBooks.forEach((ISBN, item) -> {  // here item is every book
			if(item.title.equals(bookName))
			{
				book.title = item.title;
				book.author = item.author;
				book.ISBN = item.ISBN;
				book.publicationYear = item.publicationYear;
				book.isAvailable = item.isAvailable;
				return;
			}
		});				
		if(member.borrowedBooks.get(book.ISBN) != null)
		{
			book.updateAvailability(true);
			super.books.put(book.ISBN, book);//
			member.borrowedBooks.remove(book.ISBN);
			book.updateAvailability(true);
			
			Transaction transaction = new Transaction();
			transaction.returnDate = LocalDateTime.now();
			int trLength = super.transactions.size(); // it makes each transaction unique
			String s = String.valueOf(++trLength);	      // as a serial number
			transaction.transactionId = s;
			transaction.book = book;
			transaction.member = member;
			transaction.markAsReturned();
			transactions.put(transaction.transactionId, transaction);
			System.out.println("\nTotal remaining books of member " + member.memberId 
					 + " is " + member.borrowedBooks.size());
			System.out.println("\nThe book " + book.title + " is received of ISBN " + book.ISBN + " from "
					+ member.memberId);
			
			super.transactions.forEach((id, eachTransaction) -> {
				if(eachTransaction.member.memberId.equals(member.memberId) && 
				   eachTransaction.book.ISBN.equals(book.ISBN) && !eachTransaction.isReturned)
				{
					LocalDateTime dueDate = eachTransaction.issueDate.plusMonths(1);
					Fine fine = new Fine();
					fine.calculateFine(dueDate, transaction.returnDate);
					if(fine.amount > 0)
					{
						int finesLength = super.fines.size();
						String fineId = String.valueOf(++finesLength);
						fine.fineId = fineId;
						fine.issueDate = LocalDateTime.now();
						fine.member = member;
						System.out.println("\nA fine is generated for member " + member.memberId +
								" of " + fine.amount);
						fine.markAsPaid();
						System.out.println("Fine amount " + fine.amount + " collected from " + member.memberId);
						super.fines.put(fineId, fine);
					}else
					{
						System.out.println("\nMember " + member.memberId + " returned the book within period");
					}
				}
			});
		}else
		{
			System.out.println("\nBook " + bookName + " was not taken by this member ");
		}
	}
	
	void printBooks()
	{
		if(super.books.size() == 0)
		{
			System.out.println("\nThere are no books available in this library !!!");
		}else
		{
			System.out.println("\n------Books------");
			super.books.forEach((ISBN, book) -> {
				System.out.println("\nBook name : " + book.title);
				System.out.println("Book author : " + book.author);
				System.out.println("Book ISBN : " + ISBN);
				System.out.println("Book publication year : " + book.publicationYear);
				System.out.println("Book available : " + book.isAvailable);
			});
		}
	}
	
	void printMembers()
	{
		if(super.members.size() == 0)
		{
			System.out.println("\nThere are no members added in the Library !!!");
		}else
		{
			System.out.println("\n------Members------");
			super.members.forEach((memberId, member) -> {
				System.out.println("\nMember id : " + member.memberId);
				System.out.println("Member name : " + member.name);
				System.out.println("Member contact : " + member.contactNumber);
				System.out.println("Total books taken by this member : " + member.borrowedBooks.size());
				System.out.println("Member address : " + member.address);
			});
		}
	}
	
	void printTransactions()
	{
		if(super.transactions.size() == 0)
		{
			System.out.println("\nThere are no transactions made in Transaction Book !!!");
		}else
		{
			System.out.println("\n------Transactions------");
			super.transactions.forEach((transactionId, transaction) -> {
				System.out.println("\nTransaction Id : " + transaction.transactionId);
				System.out.println("Transaction by : " + transaction.member.memberId);
				System.out.println("Transaction book : " + transaction.book.title);
				System.out.println("Transaction issue date : " + transaction.issueDate);
				System.out.println("Transaction return date : " + transaction.returnDate);
				System.out.println("Book " +transaction.book.title + " is returned : " + transaction.isReturned);
			});
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Hello, Welcome to " + new Library().libraryName);
		System.out.println("I am the Liabrarian, how can I help you ");
		while(true)
		{
			try
			{
				new Librarian().whatToDo();
			}catch(Exception e)
			{
				e.printStackTrace(); // java.util.ConcurrentModificationException
				new Librarian().whatToDo();
			}
		}
	}
	
	void whatToDo()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\nPress "
						 + "\n1: see all books and members "
						 + "\n2: open transaction book "
						 + "\n3: register member "
						 + "\n4: remove member "
						 + "\n5: add book "
						 + "\n6: remove book "
						 + "\n7: issue book "
						 + "\n8: return book"
						 + "\n9: search books by name"
						 + "\n10: search books by author"
						 + "\n11: see only available books"
						 + "\n12: open Fine book");
		int choice = sc.nextInt();
		switch(choice)
		{
			case 1 :
				printBooks();
				printMembers();
				break;
			case 2 :
				printTransactions();
				break;
			case 3 :
				super.registerMember(new Member());
				break;
			case 4 :
				super.removeMember(new Member());
				break;
			case 5 :
				addBook(new Book());
				break;
			case 6 :
				removeBook(new Book());
				break;
			case 7 :
				issueBook(new Book(), new Member());
				break;
			case 8 :
				receiveReturnedBook(new Book(), new Member()); //check for issued book is given or not
				break;
			case 9 :
				System.out.print("Enter book name : ");
				super.searchBook(sc.next());
				break;
			case 10 :
				System.out.print("Enter author name : ");
				super.searchBookByAuthor(sc.next());
				break;
			case 11 :
				super.listAvailableBooks();
				break;
			case 12 :
				super.openFineBook();
				break;
			default :
				System.out.println("Invalid choice !!!, Choose a valid one ");
				
		}
	}
	
}
