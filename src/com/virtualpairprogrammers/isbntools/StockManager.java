package com.virtualpairprogrammers.isbntools;

public class StockManager {

    private ExternalISBNDataService webService;

    public void setDatabaseService(ExternalISBNDataService databaseService) {
        this.databaseService = databaseService;
    }

    private ExternalISBNDataService databaseService;

    public void setWebService(ExternalISBNDataService webService) {
        this.webService = webService;
    }
    public String getLocatorCode(String isbn) {
        Book book = databaseService.lookup(isbn);
        if(book == null)  book = webService.lookup(isbn);//if we dont get book from db service, we call webservice
        StringBuilder locator = new StringBuilder();
        locator.append(isbn.substring(isbn.length()-4));
        locator.append(book.getAuthor().substring(0,1));
        locator.append(book.getTitle().split(" ").length);
        return locator.toString();
    }
}
