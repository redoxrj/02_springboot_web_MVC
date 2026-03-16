package com.example.springbootweb.springbootweb.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// DTO --> data transfer object //data carriers ,only job is to carry data between client → controller → service,NOT contain business logic,so they  have private fields,expose them via getters/setters
//private fields + public getters/setters(JavaBean standard)
// works to transfer data from  client to controller and thehn to service and vice-versa.
// exists between presentation and business layer not in persistant layer,
// better to use lombok library intead of writing mnually getters,setters,AllArgsConstrctuor,noArgsCOnsturor  coz more lines of code problem, and also due to serilizatiom string read by jackson of some fields like isActive mappings ki aisi taisi kar deta
public class EmployeeDTO { // this is all POJO class(plain old java object) genrally used to defined some entities

//    private long id;
    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 2,max = 30,message = "name field character's limit : [2,30]")
    private String name;

//    @AssertTrue(message = "Employees should be active") // in case we want to enforce boolean value
    private Boolean isActive;

    @NotNull (message = "age is mandatory")  // for mandatory fileds of other than Strings coz they have @NotBlank
    @Max(value = 100,message = "age can be max 100")
    @Min(value = 18,message = "age can be min 18")
    @Positive // for numbers gerater than 0
//    @PositiveOrZero
    private int age;

    @FutureOrPresent (message = "DOJ cannot be of PAST") // for dates and have to be of future or present
    private LocalDate doj;

//    @Past (message = "DOB should be of PAST") // for dates and have to be of past
//    private LocalDate DOB;

//    @Positive(message = "salary should be >0")
//    @Digits(integer = 6,fraction = 2,message = "Invalid salary[XXXXXX.FF]")
//    @DecimalMin(value = "1000.50") // .50 not possible with just @Min for int
//    @DecimalMax(value = "100000.99") // .99 not possible with just @Max for int
//    private double salary;


//    @Pattern(regexp = "^(USER|ADMIN)$\n")
//    private String role;

    // note : @NotNull → only checks null
    // @NotBlank → checks null + empty string + spaces (always use @NotBlank for Strings.)
    // @NotEmpty --> check null + field size/length is greater than 0.(useful for collections like arrays,Strings)
    // @Email check is there
    // If numeric string("22) can be safely converted(22) using Integer.parseInt("22") by Jackson for  JSON conversion and vice-versa → Jackson allows it. this is called Type Coercion.
    // use @Size for Strings and arrays while @Max and @Min when dealing with numbers

    /* private means no direct access(outside the class),so generating getters and setters to Controlled access and modify them accordingly
   // Spring / Jackson / Hibernate uses getters & setters to convert JSON into objects.(JSON → DTO (RequestBody))
    // for  json :
    {
        "name": "Rajnish",
        "age": 23
    }
    ➡️ Jackson(depdency from  starter web  depdency) internally does:
    employeeDTO.setName("Rajnish");
    employeeDTO.setAge(23);

*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getisActive() {
        return isActive;
    }

    public void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public EmployeeDTO(){ // default consturctor

    }


    public EmployeeDTO(long id, String name, Boolean isActive, int age, LocalDate doj) {  // all args constructor
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.age = age;
        this.doj = doj;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", age=" + age +
                ", doj=" + doj +
                '}';
    }
}

// NOTE :Controlled access (VERY important in real apps)
//Getters & setters let you add rules later without breaking code.
/*
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
    this.age = age; // set after oonly after a condition setisfy not directly thta's why fied has to be private and getter and setter smethods should be there
}


 */