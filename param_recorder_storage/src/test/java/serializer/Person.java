package serializer;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Data
public class Person {
    String name;
    int age;
    Date birth;
    Pet pet;
    LocalDate now;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.now = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name) && Objects.equals(birth, person.birth) && Objects.equals(pet, person.pet) && Objects.equals(now, person.now);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, birth, pet, now);
    }
}
