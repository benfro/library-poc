package net.benfro.library.commons.data;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Fake {

    public static final Faker FAKER = new Faker();
}
