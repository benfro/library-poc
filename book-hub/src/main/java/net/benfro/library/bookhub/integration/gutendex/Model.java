package net.benfro.library.bookhub.integration.gutendex;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Model {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookList implements Serializable {

        private int count;
        private String next;
        private String previous;
        private List<Book> results;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Book implements Serializable {
        private int id;
        private String title;
        private List<String> subjects;
        private List<Person> authors;
        private List<Person> translators;
        private List<String> bookshelves;
        private List<String> languages;
        private boolean copyright;
        @JsonProperty("media_type")
        private String mediaType;
        private Format formats;
        @JsonProperty("download_count")
        private int downloadCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Person implements Serializable {
        private @JsonProperty("birth_year")
        String birthYear;
        private @JsonProperty("death_year")
        String deathYear;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Format implements Serializable {
        private List<MimeUrl> content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MimeUrl implements Serializable {
        private String mimeType;
        private String url;
    }

}
