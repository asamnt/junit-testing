package com.virtualpairprogrammers.isbntools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class StockManagementTest {

    ExternalISBNDataService webService;
    ExternalISBNDataService databaseService;
    StockManager stockManager;

    @Before
    public void setup(){
        System.out.println("set up running");
        webService = mock(ExternalISBNDataService.class);
        databaseService = mock(ExternalISBNDataService.class);
        stockManager = new StockManager();
    }

    @Test
    public void testCanGetACorrectLocatorCode(){

        //this is a mock up of the external service - test stub
        //we are not going to use the implementation of the actual service
//        ExternalISBNDataService testWebService = new ExternalISBNDataService() {
//            @Override
//            public Book lookup(String isbn) {
//                return new Book(isbn, "Of Mice and Men", "J. Steibeck");
//            }
//        };

//        ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {
//            @Override
//            public Book lookup(String isbn) {
//                return null;
//            }
//        };


        when(webService.lookup(anyString())).thenReturn(new Book("0140177396", "Of Mice and Men", "J. Steibeck"));
        when(databaseService.lookup(anyString())).thenReturn(null);

        //when we instantiate the stock manager, we set the service to be our mock service

        stockManager.setWebService(webService);
        stockManager.setDatabaseService(databaseService);

        String isbn = "0140177396";
//        StockManager stockManager = new StockManager();
        String locatorCode = stockManager.getLocatorCode(isbn);
        assertEquals("7396J4",locatorCode);
    }

    @Test
    public void databaseIsUSedIfDataIsPresent(){

        when(databaseService.lookup("0140177396")).thenReturn(new Book("0140177396","abc", "abc"));
        stockManager.setWebService(webService);
        stockManager.setDatabaseService(databaseService);
        String isbn = "0140177396";
        String locatorCode = stockManager.getLocatorCode(isbn);
        verify(databaseService).lookup("0140177396");
        verify(webService,never()).lookup(anyString());
    }

    @Test
    public void webserviceIsUSedIfDataIsNotPresentInDatabase(){
        when(webService.lookup("0140177396")).thenReturn(new Book("0140177396","abc", "abc"));
        when(databaseService.lookup("0140177396")).thenReturn(null);

        stockManager.setWebService(webService);
        stockManager.setDatabaseService(databaseService);
        String isbn = "0140177396";
        String locatorCode = stockManager.getLocatorCode(isbn);
        verify(databaseService).lookup("0140177396");
        verify(webService).lookup(anyString());

    }
}
