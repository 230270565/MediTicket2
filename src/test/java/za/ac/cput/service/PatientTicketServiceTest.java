/*
 PatientTicketServiceTest.java

 Test class for PatientTicketService

 Author: Joshua Reid Adams (230317693)

 Date: 6th July 2026
*/

package za.ac.cput.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.repository.PatientTicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientTicketServiceTest {

    @Mock
    private PatientTicketRepository repository;

    @InjectMocks
    private PatientTicketService service;

    private PatientTicket ticket;

    @BeforeEach
    void setUp() {
        ticket = new PatientTicket.Builder()
                .setTicketId(1)
                .setTicketDescription("Headache")
                .setTicketCreatedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void create() {
        // create() calls save() twice: once to persist, once after addStatus().
        when(repository.save(any(PatientTicket.class))).thenReturn(ticket);

        PatientTicket created = service.create(ticket);

        assertNotNull(created);
        assertSame(ticket, created);
        assertEquals(1, created.getTicketId());
        assertEquals("Headache", created.getTicketDescription());

        verify(repository, times(2)).save(any(PatientTicket.class));
    }

    @Test
    void createNull() {
        PatientTicket created = service.create(null);

        assertNull(created);
        verify(repository, never()).save(any(PatientTicket.class));
    }

    @Test
    void read() {
        when(repository.findById(1)).thenReturn(Optional.of(ticket));

        PatientTicket found = service.read(1);

        assertNotNull(found);
        assertEquals(1, found.getTicketId());
        assertEquals("Headache", found.getTicketDescription());

        verify(repository).findById(1);
    }

    @Test
    void readNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        PatientTicket found = service.read(99);

        assertNull(found);

        verify(repository).findById(99);
    }

    @Test
    void update() {

        PatientTicket updatedTicket = new PatientTicket.Builder()
                .copy(ticket)
                .setTicketDescription("Migraine")
                .build();

        when(repository.save(updatedTicket)).thenReturn(updatedTicket);

        PatientTicket result = service.update(updatedTicket);

        assertNotNull(result);
        assertEquals(1, result.getTicketId());
        assertEquals("Migraine", result.getTicketDescription());

        verify(repository).save(updatedTicket);
    }

    @Test
    void updateNull() {

        PatientTicket result = service.update(null);

        assertNull(result);

        verify(repository, never()).save(any(PatientTicket.class));
    }

    @Test
    void delete() {

        doNothing().when(repository).deleteById(1);

        service.delete(1);

        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void getAll() {

        List<PatientTicket> tickets = List.of(ticket);

        when(repository.findAll()).thenReturn(tickets);

        List<PatientTicket> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(ticket, result.get(0));

        verify(repository).findAll();
    }
}
