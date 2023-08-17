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
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.service.ShapeManager;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private final ShapeManager manager;
    private final ModelMapper modelMapper;

    public ShapeController(ShapeManager manage, ModelMapper modelMapper) {
        this.manager = manage;
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
}
