package com.bookStore;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bookStore.controller.BookController;
import com.bookStore.entity.Book;
import com.bookStore.entity.MyBookList;
import com.bookStore.service.BookService;
import com.bookStore.service.MyBookListService;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService service;

    @Mock
    private MyBookListService myBookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testBookRegister() throws Exception {
        mockMvc.perform(get("/book_register"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookRegister"));
    }

    @Test
    public void testGetAllBook() throws Exception {
        List<Book> books = Arrays.asList(new Book(1, "Book1", "Author1", "10.0"),
                                          new Book(2, "Book2", "Author2", "20.0"));
        when(service.getAllBook()).thenReturn(books);

        mockMvc.perform(get("/available_books"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("book", books));
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book(1, "Book1", "Author1", "10.0");

        mockMvc.perform(post("/save")
                .flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/available_books"));

        verify(service, times(1)).save(book);
    }

    @Test
    public void testGetMyBooks() throws Exception {
        List<MyBookList> myBooks = Arrays.asList(new MyBookList(1, "Book1", "Author1", "10.0"),
                                                 new MyBookList(2, "Book2", "Author2", "20.0"));
        when(myBookService.getAllMyBooks()).thenReturn(myBooks);

        mockMvc.perform(get("/my_books"))
                .andExpect(status().isOk())
                .andExpect(view().name("myBooks"))
                .andExpect(model().attribute("book", myBooks));
    }
 
    @Test
    public void testGetMyList() throws Exception {
        Book book = new Book(1, "Book1", "Author1", "10.0");
        when(service.getBookById(1)).thenReturn(book);

        mockMvc.perform(get("/mylist/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my_books"));

        verify(myBookService, times(1)).saveMyBooks(any(MyBookList.class));
    }

    @Test
    public void testEditBook() throws Exception {
        Book book = new Book(1, "Book1", "Author1", "10.0");
        when(service.getBookById(1)).thenReturn(book);

        mockMvc.perform(get("/editBook/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(get("/deleteBook/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/available_books"));

        verify(service, times(1)).deleteById(1);
    }
}