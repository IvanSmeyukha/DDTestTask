package com.digdes.school;

public class Main {

    // Важно, что одинарные кавычки такие ' , а не такие `.
    public static void main(String... args){
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Петров', 'id'=1, 'age'=30, 'cost' = 5.4, 'active'=true"));
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Иванов' , 'id'=2, 'age'=25, 'cost' = 4.3,'active'=false"));
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true"));
            System.out.println(starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3"));
            System.out.println(starter.execute("UPDATE VALUES 'active'=true  where 'active'=false"));
            System.out.println(starter.execute("SELECT WHERE 'age'>=30 and 'lastName' ilike '%п%'"));
            System.out.println(starter.execute("DELETE WHERE 'id'=3"));

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
