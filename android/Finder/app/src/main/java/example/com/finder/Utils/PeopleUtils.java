package example.com.finder.Utils;

import java.util.ArrayList;
import java.util.List;

import example.com.finder.POJO.Person;

/**
 * Created by Alexandru on 28-Mar-15.
 */
public class PeopleUtils {
    private static List<Person> people;
    public static List<Person> getPeople()
    {
//        List<Person> people = new ArrayList<>();
//
//        Person p = new Person();
//        p.setId("952512441448630");
//        p.setName("Alex");
//        p.setMutualFriends(10);
//        p.setMutualLikes(11);
//        p.setPictureUrl("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/v/t1.0-1/s200x200/1623731_733876939978849_1621900119_n.jpg?oh=b55e7e38370f1eea54c2545fba9ee563&oe=55724E06&__gda__=1437755344_cbfa9b50586c9cd5d9cf530d926112d9");
//
//        people.add(p);
//        people.add(p);
//        people.add(p);

        if(people == null)
        {
            people = new ArrayList<>();
        }

        return people;
    }

    public static void setPeople(List<Person> peopleList)
    {
        people = peopleList;
    }
}
