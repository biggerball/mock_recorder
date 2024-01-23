package serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class SerializerTest {

    @Test
    public void test() {
        Date birth = new Date();
        Person peter = new Person("Peter", 12);
        peter.setBirth(birth);

        Pet pet = new Pet("Peppa");
        pet.setType("pig");

        peter.setPet(pet);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        String jsonString = gson.toJson(peter);
        System.out.println(jsonString);

        Person newPeter = gson.fromJson(jsonString, Person.class);

        Assert.assertTrue(peter.equals(newPeter));
    }
}
