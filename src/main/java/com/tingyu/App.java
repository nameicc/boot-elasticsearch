package com.tingyu;

import com.tingyu.example.Test1;
import com.tingyu.model.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        Test1 test1 = (Test1) applicationContext.getBean("test1");

        /*System.out.println(test1.insertPerson(new Person("张三", "3")));*/

        /*System.out.println(test1.getPersonById("6e2b95d5-4a15-4bc8-816c-1907cfd79f2a"));*/

        /*Person person = new Person("李四", "4");
        person.setPersonId("6e2b95d5-4a15-4bc8-816c-1907cfd79f2a");
        System.out.println(test1.updatePerson(person));*/

        test1.deletePerson("6e2b95d5-4a15-4bc8-816c-1907cfd79f2a");


        test1.closeConnection();
    }
}
