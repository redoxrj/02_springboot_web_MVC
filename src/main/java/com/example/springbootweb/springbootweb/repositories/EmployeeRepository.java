package com.example.springbootweb.springbootweb.repositories;

import com.example.springbootweb.springbootweb.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// in spring JPA,koi bhi repository hmesha ek interface banegi not class jo always extends krgei JpaRepository  in-built inteface(provided by Spring Data JPA) ko ,i.e  JpaRepository<EntityType, PrimaryKeyType>
//You don’t write it's implementation — Spring generates it automatically at runtime.
//@Repository extends @Component hence its bean will be made ,so later we can inject it whererve we want.
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
    //write queries in Repository to perform operation on data.
    // back then we had to write our own implemenation and queries for the same, but nowdays spring data JPA do it itself. spring data JPA defines the CRUD operation for us and also more complex queris with the help of JPQL(Jakarta Persistence Query Language).so we dont have to define our own implemnetaion.
    // now all CRUD operation sql queries will by mainated by JPA.we can also cutomize our own implementation/emethod.
}

/*
By extending JpaRepository, you automatically get: Basic CRUD Methods like :
save(entity)
findById(id)
findAll()
deleteById(id)
existsById(id)
count()
You don’t write SQL.
You don’t write implementation.
Spring does everything.
Spring uses Hibernate (JPA implementation) internally.

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long>  --> means "Create a repository for EmployeeEntity where the primary key is Long, and give me all database CRUD operations automatically."


 */
