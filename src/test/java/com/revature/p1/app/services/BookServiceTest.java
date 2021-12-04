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
    public void test_insertBook_returnsTrue_givenValidBook(){
        Book validBook = new Book("valid", "valid", 999, "valid");
        when(mockBookDAO.insert(any())).thenReturn(true);

        boolean result = sut.insertBook(validBook);

        Assert.assertTrue("Expected book to be considered valid", result);
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
