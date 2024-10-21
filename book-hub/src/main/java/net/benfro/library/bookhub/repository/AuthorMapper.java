package net.benfro.library.bookhub.repository;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.domain.Book;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(target="books", source = "books")
    Author authorWithBooksToAuthor(Author author, Collection<Book> books);
}
