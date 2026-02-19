package com.example.springbootweb.springbootweb.controllers;

import com.example.springbootweb.springbootweb.dto.EmployeeDTO;
import com.example.springbootweb.springbootweb.entities.EmployeeEntity;
import com.example.springbootweb.springbootweb.exceptions.ResourseNotFoundException;
import com.example.springbootweb.springbootweb.repositories.EmployeeRepository;
import com.example.springbootweb.springbootweb.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

//@Controller + @ResponseBody(json/xml data to java object and vice versa)
@RestController //teling spring this is a Rest Controller
@RequestMapping(path = "/employees") // parent route path now
public class EmployeeController {

    // never use Repository and Entity in Controller instead use Service/DTO.
//    private final EmployeeRepository employeeRepository;
////depdency injection with the help of bean by the constructor
//    public  EmployeeController(EmployeeRepository employeeRepository){
//        this.employeeRepository=employeeRepository;
//
//    }

public final EmployeeService employeeService;

public EmployeeController(EmployeeService employeeService){
    this.employeeService=employeeService;

}

//@ExceptionHandler to deal with any NoSuchElementException.class only within/works for this controller(EmployeeController) only but we dont want that as we want GlobalExceptionHandler,So
//@ExceptionHandler(NoSuchElementException.class)
//public ResponseEntity<String> employeeNotFoundHandler(NoSuchElementException exception){
//    return  new ResponseEntity<>("No such employee found",HttpStatus.NOT_FOUND);
//
//}

    // "/getAllEmployees" is resourse locator
    @GetMapping(path = "/getSomeData") // for GET api
    public String getAllEmployees(){ // this method invoke on above url
        return "some data recivved";
    }

    // since employeeid was mandatory that's why we used @PathVariable /employees/employeeid not as @RequestParam /employees?employeeid=1
    // ResponseEntity class is used to Control HTTP status code,Control response body,Control headers
    // awlays return ResponseEntity embed kisi DTO ko krtey hue not just DTO
    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable long employeeId ){
        Optional<EmployeeDTO>  employeeDTO= employeeService.getEmployeeById(employeeId);
        // Optional<EmployeeDTO> -->That means: If employee exists → Optional contains DTO,If not → Optional.empty()
        return  employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1))
//                .orElse(ResponseEntity.notFound().build());
                .orElseThrow(()-> new ResourseNotFoundException("employee not found! with id :" +employeeId));
//        return new EmployeeDTO(employeeId,"Rajnish",true,24, LocalDate.of(2025,2,10));
        // it's jackson duty to convert automatice of this java object into json
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam String name, @RequestParam(required = false,name = "inputAge") Integer age){
//        return "hello" +" " +"name is "+ name  + " with age " +age;
        return ResponseEntity.ok(employeeService.getAllEmployees(name,age));
    }
    /*
    (name = "inputAge") can be used in @PathVariable also
    note :Primitive types cannot be null
     as int cannot hold null → ❌ error
👉 Integer can hold null → ✅ works  hence Integer(non-primitive ) used in @RequestParam variables
     */

    // @RequestBody is used to take input from  input json -> converted in java objects by jackson
    @PostMapping(path = "/add")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody @Valid EmployeeDTO employeeJson ){
        // @Valid Annotation make sures all validations present in EmployeeDTO in @RequestBody must ve validated first before reaching to service layer
//        employeeJson.setId(1000L); // no need now as auto-incremented
        EmployeeDTO employeeDTO =employeeService.addEmployee(employeeJson);
//         return employeeJson;
        return new ResponseEntity<>(employeeDTO, HttpStatus.CREATED); // using new ResponseEntity since employeeDTO is created and we have to send to Response Body hence passing the same using as new ResponseEntity<>(employeeDTO, HttpStatus.CREATED);

    }

    // NOTE : type argument cannot be primitve data type, it must be a wrapper class of corresponding primitve data type.
    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> updateEmployeeById(@RequestBody EmployeeDTO employeeJson,@PathVariable Long employeeId){

        boolean isUpdated = employeeService.updateEmployeeById(employeeJson,employeeId);
        if (!isUpdated) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(true);

    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long employeeId){
        boolean isDeleted= employeeService.deleteEmployeeById(employeeId);
        if(!isDeleted) return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(true);
    }

    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> updateEmployeePatchById(@PathVariable Long employeeId, @RequestBody Map<String,Object> updates){
       boolean isPatched = employeeService.updateEmployeePatchById(employeeId,updates);
       if(!isPatched) return ResponseEntity.notFound().build();
       return ResponseEntity.ok(true);
        // note : can be Map<String,String> also but we don't know which filed will come in Patch so that's why use Map<String,Object> in general
        // here key is the field while value is the object can be anything(String,Integar etc.)
    }
}
