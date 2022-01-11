package com.kkj.javadynamicproxy;

public class BookServiceProxy implements BookService {

    BookService bookService;

    public BookServiceProxy(BookService bookService) {
        this.bookService = bookService;
    }


    @Override
    public void rent(Book book) {
        System.out.println("aaaa");
        bookService.rent(book);
        System.out.println("bbbbbb");
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("aaaaaa");
        bookService.returnBook(book);
    }
}
