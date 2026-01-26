package se.bastagruppen.todo_appen.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.exception.BadRequestException;
import se.bastagruppen.todo_appen.exception.NotFoundException;
import se.bastagruppen.todo_appen.exception.ToDoListNotFoundException;
import se.bastagruppen.todo_appen.mapper.ToDoListEntryMapper;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListEntry;
import se.bastagruppen.todo_appen.repository.ToDoListEntryRepository;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoListEntryServiceTest {

    @Mock
    private ToDoListRepository listRepository;

    @Mock
    private ToDoListEntryRepository repository;

    @Mock
    private ToDoListEntryMapper mapper;

    @InjectMocks
    private ToDoListEntryService service;


    @Test
    void createEntry_withDetailsAndDeadline_setsFields() {
        Long listId = 1L;

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setDetails("Some details");
        dto.setDeadline(LocalDate.of(2026, 1, 22));

        ToDoListEntry entry = new ToDoListEntry();
        entry.setDetails("Some details");
        entry.setDeadline(LocalDate.of(2026, 1, 22));

        ToDoListEntry saved = new ToDoListEntry();
        saved.setId(10L);
        saved.setDetails("Some details");
        saved.setDeadline(LocalDate.of(2026, 1, 22));
        saved.setList(list);

        ToDoListEntryResponseDto responseDto = new ToDoListEntryResponseDto();
        responseDto.setId(10L);
        responseDto.setDetails("Some details");
        responseDto.setDeadline(LocalDate.of(2026, 1, 22));
        responseDto.setListId(listId);

        when(listRepository.findById(listId)).thenReturn(Optional.of(list));
        when(mapper.toEntity(dto)).thenReturn(entry);
        when(repository.save(entry)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        ToDoListEntryResponseDto result =
                service.createToDoListEntry(listId, dto);

        assertEquals("Some details", result.getDetails());
        assertEquals(LocalDate.of(2026, 1, 22), result.getDeadline());
        assertEquals(listId, result.getListId());
    }

    @Test
    void createEntry_withoutOptionalFields_leavesThemNull() {
        Long listId = 1L;

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setSummary("Task");

        ToDoListEntry entry = new ToDoListEntry();
        entry.setSummary("Task");

        ToDoListEntry saved = new ToDoListEntry();
        saved.setId(11L);
        saved.setSummary("Task");
        saved.setDetails(null);
        saved.setDeadline(null);
        saved.setList(list);

        ToDoListEntryResponseDto responseDto = new ToDoListEntryResponseDto();
        responseDto.setId(11L);
        responseDto.setSummary("Task");
        responseDto.setDetails(null);
        responseDto.setDeadline(null);
        responseDto.setListId(listId);

        when(listRepository.findById(listId))
                .thenReturn(Optional.of(list));

        when(mapper.toEntity(dto))
                .thenReturn(entry);

        when(repository.save(entry))
                .thenReturn(saved);

        when(mapper.toDto(saved))
                .thenReturn(responseDto);

        ToDoListEntryResponseDto result =
                service.createToDoListEntry(listId, dto);

        assertNotNull(result);
        assertNull(result.getDetails());
        assertNull(result.getDeadline());
    }

    @Test
    void createList_listNotFound_throwsException() {
        Long listId = 13L;

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setSummary("Task");

        when(listRepository.findById(listId))
                .thenReturn(Optional.empty());

        assertThrows(ToDoListNotFoundException.class, () ->
                service.createToDoListEntry(listId, dto)
        );
    }

    @Test
    void createSubtask_parentExists_setsParent() {
        Long listId = 1L;
        Long parentId = 5L;

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntry parent = new ToDoListEntry();
        parent.setId(parentId);
        parent.setList(list);

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setSummary("Subtask");
        dto.setParentId(parentId);

        ToDoListEntry entry = new ToDoListEntry();

        ToDoListEntry saved = new ToDoListEntry();
        saved.setId(10L);
        saved.setParent(parent);
        saved.setList(list);

        ToDoListEntryResponseDto responseDto = new ToDoListEntryResponseDto();
        responseDto.setId(10L);
        responseDto.setParentId(parentId);
        responseDto.setListId(listId);

        when(listRepository.findById(listId)).thenReturn(Optional.of(list));
        when(repository.findById(parentId)).thenReturn(Optional.of(parent));
        when(mapper.toEntity(dto)).thenReturn(entry);
        when(repository.save(entry)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        ToDoListEntryResponseDto result =
                service.createToDoListEntry(listId, dto);

        assertEquals(parentId, result.getParentId());
    }

    @Test
    void createSubtask_parentNotFound_throwsNotFound() {
        Long listId = 1L;
        Long missingParentId = 99L;

        ToDoList list = new ToDoList();
        list.setId(listId);

        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setSummary("Subtask");
        dto.setParentId(missingParentId);

        ToDoListEntry entry = new ToDoListEntry();

        when(listRepository.findById(listId)).thenReturn(Optional.of(list));
        when(repository.findById(missingParentId)).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(entry);

        assertThrows(NotFoundException.class, () ->
                service.createToDoListEntry(listId, dto)
        );
        verify(repository, never()).save(any());
    }

    @Test
    void createSubtask_parentInDifferentList_throwsBadRequest() {
        Long listId = 1L;
        Long parentId = 10L;
        Long otherListId = 2L;

        // Target list (where we try to add subtask)
        ToDoList targetList = new ToDoList();
        targetList.setId(listId);

        // Parent entry (which belongs to another list)
        ToDoList parentList = new ToDoList();
        parentList.setId(otherListId);

        ToDoListEntry parent = new ToDoListEntry();
        parent.setId(parentId);
        parent.setList(parentList);  // parent doesn't belong target list! Should throw Bad Request.

        // DTO with parentId = parentId, but listId = targetList
        ToDoListEntryRequestDto dto = new ToDoListEntryRequestDto();
        dto.setSummary("Subtask");
        dto.setParentId(parentId);

        ToDoListEntry entry = new ToDoListEntry();

        when(listRepository.findById(listId)).thenReturn(Optional.of(targetList));
        when(repository.findById(parentId)).thenReturn(Optional.of(parent));
        when(mapper.toEntity(dto)).thenReturn(entry);

        assertThrows(BadRequestException.class, () ->
                service.createToDoListEntry(listId, dto)
        );
        verify(repository, never()).save(any());
    }

}
