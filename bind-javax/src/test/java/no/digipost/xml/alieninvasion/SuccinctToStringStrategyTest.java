package no.digipost.xml.alieninvasion;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.toHexString;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.stream.Collectors.toList;
import static no.digipost.DiggExceptions.applyUnchecked;
import static no.digipost.xml.alieninvasion.DiplomaticAgreement.FOE;
import static no.digipost.xml.alieninvasion.DiplomaticAgreement.FRIEND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

class SuccinctToStringStrategyTest {

    @Test
    void emptyObject() {
        Alien alien = new Alien();
        assertThat("toString outputs simple class name instead of fully qualified",
                alien, where(Alien::toString, startsWith(Alien.class.getSimpleName())));
        assertThat("toString outputs no key-value pairs for its properties",
                alien, where(Alien::toString, not(containsString("="))));
    }

    @Test
    void doesNotIncludehashCode() {
        Alien alien = new Alien("Blarg", new Planet("Alpha Centauri b", "Centaurus"), FOE);
        assertThat("toString does not include hashCode",
                alien, where(Alien::toString, not(containsString(toHexString(alien.hashCode())))));
    }

    @Test
    void onlyIncludesNonNullFields() {
        Alien homelessAlien = new Alien("Blarg", null, FRIEND);
        List<Field> alienFields = Stream.of(Alien.class.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .collect(toList());

        assertAll("toString includes all non-null field values", alienFields.stream()
                .filter(field -> applyUnchecked(field::get, homelessAlien) != null)
                .map(Field::getName)
                .map(fieldName -> () -> assertThat(homelessAlien, where(Alien::toString, containsString(fieldName)))));

        assertAll("toString excludes all null field values", alienFields.stream()
                .filter(field -> applyUnchecked(field::get, homelessAlien) == null)
                .map(Field::getName)
                .map(fieldName -> () -> assertThat(homelessAlien, where(Alien::toString, not(containsString(fieldName))))));
    }

}
