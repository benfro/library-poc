package net.benfro.library.bookhub.repository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.benfro.library.bookhub.domain.Book;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "authorId", target = "author_id")
    DbBook toDbBook(Book book);
}
