package pl.kurs.shapeapiapp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestEditDto;
import pl.kurs.shapeapiapp.service.ShapeManager;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private final ShapeManager manager;
    private final ModelMapper modelMapper;

    public ShapeController(ShapeManager manager, ModelMapper modelMapper) {
        this.manager = manager;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeDto> addShape(@RequestBody @Valid ShapeRequestDto shapeRequestDto, Principal p) {
        ShapeDto shapeDto = manager.saveShape(shapeRequestDto, p.getName());
        return new ResponseEntity<>(shapeDto, HttpStatus.CREATED);

    }

    @GetMapping("/parameters")
    public ResponseEntity<Page<ShapeDto>> getAllShapes(@PageableDefault(sort = "type", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam Map<String, String> parameters) {
        return ResponseEntity.ok(manager.getFilteredShapes(parameters, pageable).map(s -> modelMapper.map(s, ShapeDto.class)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<ShapeDto> edit(@PathVariable Long id, @RequestBody ShapeRequestEditDto shapeRequestEditDto,
                                         Principal principal) {
        ShapeDto shapeDto = manager.editShape(id, shapeRequestEditDto, principal.getName());
        return ResponseEntity.ok(shapeDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#id)")
    @GetMapping("/{id}/changes")
    public ResponseEntity<List<ShapeChangeDto>> getChanges(@PathVariable Long id) {
        List<ShapeChangeDto> shapeChanges = manager.getChanges(id);
        return ResponseEntity.ok(shapeChanges);
    }
}
