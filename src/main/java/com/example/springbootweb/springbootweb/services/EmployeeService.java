package com.example.springbootweb.springbootweb.services;

import com.example.springbootweb.springbootweb.dto.EmployeeDTO;
import com.example.springbootweb.springbootweb.entities.EmployeeEntity;
import com.example.springbootweb.springbootweb.entities.UserEntity;
import com.example.springbootweb.springbootweb.exceptions.ResourseNotFoundException;
import com.example.springbootweb.springbootweb.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    public final EmployeeRepository employeeRepository;
//    public final ModelMapperConfig modelMapperConfig; // wrong coz ModelMapperConfig is just a configuration class.Its purpose is to create a bean, not to perform mapping.
    public final ModelMapper modelMapper;//correct use Actual class

    // is args walein constructker ki requienment porri krni hi pdegi tab bhi iss EmployeeService ka koi object banega uske baad is EmployeeService class mein kuch kaam hoga
    EmployeeService(EmployeeRepository employeeRepository,ModelMapper modelMapper){
        this.employeeRepository=employeeRepository;
        this.modelMapper=modelMapper;

    }

// Optional<T> entity can embeded any type of object in it
    // is a container object introduced in Java 8 that may or may not contain a non-null value.
    // Instead of returning null, methods can return: Optional<EmployeeEntity> which means: “There may be an EmployeeEntity inside… or maybe not.”
    // Optional is Better Than .orElse(null)
    public Optional<EmployeeDTO> getEmployeeById(long employeeId) {
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
//                .orElse(null);
//        return new EmployeeDTO(employeeEntity.getId(),employeeEntity.getName()) // not a good preactice just to convert in EmployeeDTO type object that's why use ModelMapper library
        // comverting EmployeeEntity type object into EmployeeDTO object using ModelMapper Library/Depepdency Maven.
//        ModelMapper modelMapper = new ModelMapper(); // spring use krney ka faayda kya jab new keyword se object bannaan pdein
        // ModelMapper (given source object se, given target class) se common fields nikaal k target class ke object mein badal deta h
//        return modelMapper.map(employeeEntity,EmployeeDTO.class);
        // map() works like this: “If value is present, apply this function. If not, return empty.”
        // If employee exists → convert Entity → DTO,If not → return empty Optional
//        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1,EmployeeDTO.class) );
        // OR
        // accessing/geeting current logged-in user details/userId
//        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info(String.valueOf(user));

        return  employeeRepository.findById(employeeId).map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDTO.class) );

    }

    public List<EmployeeDTO> getAllEmployees(String name, Integer age) {
        List<EmployeeEntity> employeeEntityList =  employeeRepository.findAll();
        // to convert one EntityList to DTOList we have to use stream() library with map() and finally collect alongwith ModelMapper
        return employeeEntityList
                .stream()
                .map((employeeEntity)->modelMapper.map(employeeEntity,EmployeeDTO.class))
                .collect(Collectors.toList());

    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeJson) {
        // custom business logic like authorization can be performed here
        // converting DTO to Entity using same ModelMapper for Repository Object input that accepts only Entity  Object
        EmployeeEntity employeeJsonEntity = modelMapper.map(employeeJson,EmployeeEntity.class);
        EmployeeEntity employeeEntity =  employeeRepository.save(employeeJsonEntity);
        return modelMapper.map(employeeEntity,EmployeeDTO.class);
    }

    public void isEmployeeExists(Long employeeId){
        boolean isExists =  employeeRepository.existsById(employeeId);
        if (!isExists) throw new ResourseNotFoundException("employee not found with id : "+employeeId);
    }

    public boolean updateEmployeeById(EmployeeDTO employeeJson, Long employeeId) {
//        boolean isExists = isEmployeeExists(employeeId);
         isEmployeeExists(employeeId);
//        if(!isExists) return false;
//        if (!isExists) throw new ResourseNotFoundException("employee not found with id : "+employeeId);
//

        EmployeeEntity employeeEntity = modelMapper.map(employeeJson,EmployeeEntity.class);
        employeeEntity.setId(employeeId); // in case the user didnot passed employeeId in request body JSON but only in @RequestPath
        employeeRepository.save(employeeEntity);
        // this .save() method is so powerful and works like in HashMap that if for that key(employeeId) if value exists it updates the value(data) agginst that key else add a new key(employeeId)-value(data) pair.
        // but save method is used only for @PutMapping as all data key-value should be present if any thing is null,the same null(or by default value of Diff. data types) will be updated on that field against that employeeId. unlike @PatchMapping.

        return true;

    }

    public boolean deleteEmployeeById(Long employeeId) {
//        boolean isExists = isEmployeeExists(employeeId);
        isEmployeeExists(employeeId);
//        if(!isExists) return false;
//        if (!isExists) throw new ResourseNotFoundException("employee not found with id : "+employeeId);
        employeeRepository.deleteById(employeeId);
        return true;
    }

    public boolean updateEmployeePatchById(Long employeeId, Map<String ,Object> updates) {
//        boolean isExists = isEmployeeExists(employeeId);
        isEmployeeExists(employeeId);
//        if(!isExists) return false;
//        if (!isExists) throw new ResourseNotFoundException("employee not found with id : "+employeeId);
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
        System.out.println(employeeEntity); // make sure employeeEntity has already generated toString() method else print wiered Object representation.
        // to achieve Patch we have to use concept of Reflection, i,.ie original old EmployeeEntity object ko nye updates ke object ko compare krke same fields(common key) ko updates krke(value) old EmployeeEntity object ko updated new EmployeeEntity object  mein convert kar dengey.

        updates.forEach((field,value)->{ // key<->field
           Field fieldToBeUpdated= ReflectionUtils.findRequiredField(EmployeeEntity.class,field);
            fieldToBeUpdated.setAccessible(true);// taaki fields update ho jaaye as all fields in EmployeeEntity is private.
            ReflectionUtils.setField(fieldToBeUpdated,employeeEntity,value);

        });
        System.out.println(employeeEntity);
        //employeeEntity is now updated Object containing updates object conatianing field-values + employeeEntity existing key-value pairs
        employeeRepository.save(employeeEntity); //.save() method at last
        return true;
    }
}

// note :  each Service method should always accept and return DTO from and to for a Controller.
// we can throw exceptions(even our own custom exceptions) from  services as well , all exceptions thown in any conroller/serivce will be caught by our GlobalExceptionHandler