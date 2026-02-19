package com.example.springbootweb.springbootweb.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// @Entity will tell the spring jpa or hibernate,hey EmployeeEntity is java class that you need to convert into a table in db
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Employees") // is naam ki table banegi
public class EmployeeEntity {

    @Id   // primary key (jis field ke uper ye annotations use kiya ho)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private Boolean isActive;
    private int age;
    private LocalDate doj;

    @Override
    public String toString() {
        return "EmployeeEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", age=" + age +
                ", doj=" + doj +
                '}';
    }

//    public EmployeeEntity(){
//
//    }
//    public EmployeeEntity(long id, String name, Boolean isActive, int age, LocalDate doj) {
//        this.id = id;
//        this.name = name;
//        this.isActive = isActive;
//        this.age = age;
//        this.doj = doj;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Boolean getActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public LocalDate getDoj() {
//        return doj;
//    }
//
//    public void setDoj(LocalDate doj) {
//        this.doj = doj;
//    }

    // again getterrs,setters,defualtConstrudtur,ArgsConstruxctor is also requiired for Entities just like DTO's, for that boiplterplatr code lombok depencey can be used for the same.so need need manual work or xtra work.
    // not giving suggestion for now,we will look lter on(now working as we insatlled lombok plugin).

    // fields inside EmployeeEntity(or any Entity) will convert into columns of the table.
    // Enitties are used for sensitive-data also.
    // DTO's are used for validation and round around client<->Controller(Presenetaion layer)
    //Enitties are used for columns constrint and how the data will be stored in db/table service,<-->Repository(persistant layer)
    // Entity and repository shoudld not be found/used/imported in Controolers instead they must be in service.
    // @Entity in JPA (java or jakrata persitance api) ise used to mark a class as a persistent entity meaing it repsents a table in relational database.
    //this is a fundmental part oF ORM(object-realtional mapping) where java objects are mapped to database tables.
    // all happending behind the scenes with the help of  hibernate.
    // Persistence layer --> for data access(db), Presentation layer --> for client access ,Service layer --> acts as bridge b/w Presentation layer and Persistence layer where business/custom logic is written
    // service layer acts as bridge b/w controller and repository.
    // Controller should only deal with DTO but not Entity whereas Repository should only deal with Entity not DTO.
    // Service take DTO from controller pass this DTO as input to Repository and get Entity in response and then convert in DTO and send back the DTO to Controller.
    // FOR Proper MVC structure.

}
