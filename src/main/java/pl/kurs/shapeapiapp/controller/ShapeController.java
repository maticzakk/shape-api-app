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
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.service.implementation.ShapeService;


import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {
    private final ShapeService shapeService;
    private final ModelMapper modelMapper;

    public ShapeController(ShapeService shapeService, ModelMapper modelMapper) {
        this.shapeService = shapeService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeDto> addShape(@RequestBody ShapeRequestDto shapeRequestDto, Principal p) {
//        ShapeDto shape = shapeService.saveShape(shapeRequestDto, p.getName());
//        return ResponseEntity.status(HttpStatus.CREATED).body(shape);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(shapeService.saveShape(shapeRequestDto, p.getName()), shapeService.chooseCorrectDto(shapeRequestDto.getType())));

    }

    @GetMapping("/parameters")
    public ResponseEntity<Page<ShapeDto>> getAllShapes(@PageableDefault(sort = "type", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam Map<String, String> parameters) {
        Page<Shape> shapes = shapeService.getAllShapes(pageable,  parameters);
        //Page<ShapeDto> shapeDtos = shapes.map(shapeService::getResponse);
        Page<ShapeDto> shapeDtos = shapes.map(x -> modelMapper.map(x, ShapeDto.class));
        return ResponseEntity.ok(shapeDtos);

    }
}
