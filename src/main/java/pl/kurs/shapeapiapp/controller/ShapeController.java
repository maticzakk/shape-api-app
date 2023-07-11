package pl.kurs.shapeapiapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.service.IShapeManagementService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shapes")
public class ShapeController {

    private final IShapeManagementService shapeManagementService;

    public ShapeController(IShapeManagementService shapeManagementService) {
        this.shapeManagementService = shapeManagementService;
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeDto> addShape(@RequestBody ShapeRequestDto shapeRequestDto, Principal p) {
        ShapeDto shapeDto = shapeManagementService.saveShape(shapeRequestDto, p.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(shapeDto);

    }

    @GetMapping
    public ResponseEntity<List<ShapeDto>> filteredShapes(@RequestParam Map<String, String> parameters) {
        List<ShapeDto> filter = shapeManagementService.getFiltered(parameters);
        return ResponseEntity.ok(filter);
    }
}
