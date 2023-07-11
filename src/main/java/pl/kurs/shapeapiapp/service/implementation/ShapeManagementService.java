package pl.kurs.shapeapiapp.service.implementation;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.repository.RoleRepository;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.service.IShapeManagementService;
import pl.kurs.shapeapiapp.service.IShapeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShapeManagementService implements IShapeManagementService {

    private final ShapeRepository shapeRepository;
    private final RoleRepository roleRepository;
    private final List<IShapeService> shapeServices;

    public ShapeManagementService(ShapeRepository shapeRepository, RoleRepository roleRepository, List<IShapeService> shapeServices) {
        this.shapeRepository = shapeRepository;
        this.roleRepository = roleRepository;
        this.shapeServices = shapeServices;
    }

    @Override
    public ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username) {
        IShapeService shapeService = shapeServices.stream()
                .filter(h -> h.getShape().equalsIgnoreCase(shapeRequestDto.getType()))
                .findFirst()
                .orElseThrow();
        return shapeService.save(shapeRequestDto, username);
    }

    @Override
    public List<ShapeDto> getFiltered(Map<String, String> parameters) {
        List<ShapeDto> shapeDtoList= new ArrayList<>();
        shapeServices.forEach(x -> shapeDtoList.addAll(x.filter(parameters)));
        return shapeDtoList;
    }
}
