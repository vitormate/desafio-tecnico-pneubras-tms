package com.pneubras.tms.service;

import com.pneubras.tms.dto.request.AssignTicketRequest;
import com.pneubras.tms.dto.request.UpdateTicketRequest;
import com.pneubras.tms.dto.response.TicketsResponse;
import com.pneubras.tms.entity.Tickets;
import com.pneubras.tms.entity.User;
import com.pneubras.tms.exception.BusinessException;
import com.pneubras.tms.repository.TicketsRepository;
import com.pneubras.tms.repository.UserRepository;
import com.pneubras.tms.utils.enums.PriorityEnum;
import com.pneubras.tms.utils.enums.RoleEnum;
import com.pneubras.tms.utils.enums.StatusEnum;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketsServiceTest {

    @Mock
    TicketsRepository ticketsRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TicketsService ticketsService;

    Tickets ticket;
    User user;
    User agent;

    @BeforeEach
    void setUp() {
        this.user = new User(1L, "admin", "123456", RoleEnum.ADMIN);
        this.agent = new User(1L, "agent", "123456", RoleEnum.AGENT);
        this.ticket = new Tickets("Test", "Test", PriorityEnum.BAIXA, user);
        this.ticket.setId(1L);
    }

    @Nested
    class assignTicket {

        @Test
        void statusShouldBeEmProgressoWhenAssignPass() {
            AssignTicketRequest data = new AssignTicketRequest("agent");

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));
            when(userRepository.findByLogin(data.login())).thenReturn(Optional.of(agent));

            TicketsResponse ticketsResponse = ticketsService.assignTicket(1L, data);

            assertEquals(StatusEnum.EM_PROGRESSO, ticketsResponse.status());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verify(userRepository, times(1)).findByLogin(any(String.class));
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenTicketNotFound() {
            AssignTicketRequest data = new AssignTicketRequest("agent");

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> ticketsService.assignTicket(1L, data)
            );

            assertEquals("Ticket not found with id: 1", exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void shouldReturnEntityNotFoundExceptionWhenUserNotFound() {
            AssignTicketRequest data = new AssignTicketRequest("agent");

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));
            when(userRepository.findByLogin(data.login())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> ticketsService.assignTicket(1L, data)
            );

            assertEquals("User not found with login: " + data.login(), exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verify(userRepository, times(1)).findByLogin(any(String.class));
        }

        @Test
        void shouldReturnBusinessExceptionWhenStatusNotAberto() {
            ticket.setStatus(StatusEnum.EM_PROGRESSO);
            AssignTicketRequest data = new AssignTicketRequest("agent");

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));
            when(userRepository.findByLogin(data.login())).thenReturn(Optional.of(agent));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> ticketsService.assignTicket(1L, data)
            );

            assertEquals("The status should be ABERTO. Status: " + ticket.getStatus(), exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verify(userRepository, times(1)).findByLogin(any(String.class));
        }
    }

    @Nested
    class resolveTicket {

        @Test
        void statusShouldBeResolvidoWhenResolvePass() {
            ticket.setStatus(StatusEnum.EM_PROGRESSO);

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

            TicketsResponse ticketsResponse = ticketsService.resolveTicket(1L);

            assertEquals(StatusEnum.RESOLVIDO, ticketsResponse.status());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void shouldReturnEntityNotFoundWhenTicketNotFound() {
            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> ticketsService.resolveTicket(1L)
            );

            assertEquals("Ticket not found with id: 1", exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenStatusNotEm_Progresso() {
            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> ticketsService.resolveTicket(1L)
            );

            assertEquals("The status should be EM_PROGRESSO. Status: " + ticket.getStatus(), exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }
    }

    @Nested
    class closeTicketTests {

        @Test
        void statusShouldBeFechadoWhenClosePass() {
            ticket.setStatus(StatusEnum.RESOLVIDO);

            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

            TicketsResponse ticketsResponse = ticketsService.closeTicket(1L);

            assertEquals(StatusEnum.FECHADO, ticketsResponse.status());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void shouldReturnEntityNotFoundWhenTicketNotFound() {
            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> ticketsService.closeTicket(1L)
            );

            assertEquals("Ticket not found with id: 1", exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void shouldReturnBusinessExceptionWhenStatusNotResolvido() {
            when(ticketsRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> ticketsService.closeTicket(1L)
            );

            assertEquals("The status should be RESOLVIDO. Status: " + ticket.getStatus(), exception.getMessage());
            verify(ticketsRepository, times(1)).findById(any(Long.class));
            verifyNoInteractions(userRepository);
        }
    }
}