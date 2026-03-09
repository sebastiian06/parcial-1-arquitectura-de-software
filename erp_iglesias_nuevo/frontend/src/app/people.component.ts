import { Component, OnInit } from '@angular/core';
import { PersonService } from '../services/person/person.service';
import { Person, PersonPayload } from '../models/person.model';

@Component({
    selector: 'app-people',
    templateUrl: './people.component.html',
    styleUrls: ['./people.component.scss']
})
export class PeopleComponent implements OnInit {
    people: Person[] = [];
    loading = false;
    error: string | null = null;

    constructor(private personService: PersonService) {}

    ngOnInit(): void {
        this.loadPeople();
    }

    loadPeople(): void {
        this.loading = true;
        this.personService.list().subscribe({
            next: (data) => {
                this.people = data;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Error al cargar personas';
                this.loading = false;
                console.error(err);
            }
        });
    }

    createPerson(payload: PersonPayload): void {
        this.personService.create(payload).subscribe({
            next: (newPerson) => {
                this.people.push(newPerson);
            },
            error: (err) => {
                this.error = err.error?.message || 'Error al crear persona';
            }
        });
    }

    deletePerson(id: number): void {
        if (confirm('¿Está seguro de eliminar esta persona?')) {
            this.personService.delete(id).subscribe({
                next: () => {
                    this.people = this.people.filter(p => p.id !== id);
                },
                error: (err) => {
                    this.error = 'Error al eliminar persona';
                }
            });
        }
    }
}