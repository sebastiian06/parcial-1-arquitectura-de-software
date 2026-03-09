package com.iglesia;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iglesia.dto.request.PersonRequest;
import com.iglesia.dto.response.PersonResponse;
import com.iglesia.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/people")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse create(@Valid @RequestBody PersonRequest request) {
        return personService.createPerson(request);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping
    public List<PersonResponse> list() {
        return personService.getAllPeople();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping("/{id}")
    public PersonResponse getById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @PutMapping("/{id}")
    public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest request) {
        return personService.updatePerson(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personService.deletePerson(id);
    }
}