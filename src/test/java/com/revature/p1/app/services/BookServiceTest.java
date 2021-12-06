package com.revature.p1.app.services;

import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class BookServiceTest {
    BookService sut;
    BookDAO mockBookDAO;

    @Before
    public void setup(){
        mockBookDAO = mock(BookDAO.class);
        sut = new BookService(mockBookDAO);
    }

    @After
    public void cleanup(){
        sut = null;
    }

    // when(mockUserDAO.update(any(),anyInt())).thenReturn(true);
    // userDAO.update(user, user.getId())

    @Test
    public void test_getBookById_returnsTrue_givenValidUuid(){
        String validUuid = "7dfca934-4f83-42bd-b0fa-d6a676ff5a26";
        when(mockBookDAO.getById(any())).thenReturn(new ArrayList<>(Arrays.asList("Positive Result")));

        List returnedValue = sut.getBookById(validUuid);
        boolean result = returnedValue != null;

        Assert.assertTrue("Expected valid UUID to return an array", result);
    }

    @Test
    public void test_getBookById_returnsFalse_givenInvalidString(){
        String invalidUuid= "invalid";
        when(mockBookDAO.getById(any())).thenReturn(new ArrayList<>(Arrays.asList("Positive Result")));

        List returnedValue = sut.getBookById(invalidUuid);
        boolean result = returnedValue != null;

        Assert.assertFalse("Expected the method to fail", result);
    }

    @Test
    public void test_insertBook_returnsTrue_givenValidBook(){
        Book validBook = new Book("valid", "valid", 999, "valid");
        when(mockBookDAO.insert(any())).thenReturn(true);

        boolean result = sut.insertBook(validBook);

        Assert.assertTrue("Expected book to be considered valid", result);
    }

    @Test
    public void test_insertBook_returnsFalse_givenBookWithNoAuthor(){
        Book nullAuthor = new Book("valid", null, 999, "valid");
        when(mockBookDAO.insert(any())).thenReturn(true);

        boolean result = sut.insertBook(nullAuthor);

        Assert.assertFalse("Expected the Insert method to fail", result);
    }

    @Test
    public void test_updateBook_returnsTrue_givenValidBook(){
        Book validBook = new Book("valid", "valid", 999, "valid");
        when(mockBookDAO.update(any())).thenReturn(true);

        boolean result = sut.updateBook(validBook);

        Assert.assertTrue("Expected the book record to update", result);
    }

    @Test
    public void test_updateBook_returnsFalse_givenBookWithNoPageCount(){
        Book nullPageCount = new Book("valid", "valid", -42, "valid");
        when(mockBookDAO.update(any())).thenReturn(true);

        boolean result = sut.updateBook(nullPageCount);

        Assert.assertFalse("Expected book update to fail", result);
    }

    @Test
    public void test_deleteBook_returnsTrue_givenValidId(){
        String validUuid = "7dfca934-4f83-42bd-b0fa-d6a676ff5a26";
        when(mockBookDAO.delete(any())).thenReturn(true);

        boolean result = sut.deleteBook(validUuid);

        Assert.assertTrue("Expected the book record to be deleted", result);
    }

    @Test
    public void test_deleteBook_returnsFalse_givenInvalidId(){
        String invalidUuid = "invalid";
        when(mockBookDAO.delete(any())).thenReturn(true);

        boolean result = sut.deleteBook(invalidUuid);

        Assert.assertFalse("Expected the book record to be deleted", result);
    }

    @Test
    public void test_isBookValid_returnsTrue_givenValidBook(){
        Book validBook = new Book("valid", "valid", 999, "valid");
        boolean result = sut.isBookValid(validBook);

        Assert.assertTrue("Expected book to be considered valid", result);
    }

    @Test
    public void test_isBookValid_returnsFalse_givenBookWithNoTitle(){
        Book nullTitle = new Book(null, "valid", 999, "valid");
        Book emptyTitle = new Book("", "valid", 999, "valid");
        boolean nullResult = sut.isBookValid(nullTitle);
        boolean emptyResult = sut.isBookValid(emptyTitle);

        Assert.assertFalse("Null title should provide false.", nullResult);
        Assert.assertFalse("Empty title should provide false.", emptyResult);
    }
}
