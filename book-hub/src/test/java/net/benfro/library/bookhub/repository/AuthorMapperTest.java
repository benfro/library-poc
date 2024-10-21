package net.benfro.library.bookhub.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.domain.Book;

class AuthorMapperTest {

    @Test
    void testAuthorAndBooks() {
        Author inputAuthor = Author.builder().id(4077L).build();

        List<Book> inputBooks = List.of(Book.builder().build(), Book.builder().build());

        Author result = AuthorMapper.INSTANCE.authorWithBooksToAuthor(inputAuthor, inputBooks);
        assertEquals(4077, result.getId());
        assertEquals(2, result.getBooks().size());
    }
}
