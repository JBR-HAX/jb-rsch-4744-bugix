package org.jetbrains.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final ApiCallLogRepository apiCallLogRepository;

    public ApiController(ApiCallLogRepository apiCallLogRepository) {
        this.apiCallLogRepository = apiCallLogRepository;
    }

    @PostMapping("/moves")
    public List<CommandDTO> movesList(@RequestBody List<CoordinateDTO> coordinateDTOS) {

        var moveDTOS = new ArrayList<CommandDTO>();

        var first = true;

        var x = 0;
        var y = 0;

        for (var coordinate : coordinateDTOS) {

            if (!first) {
                var dx = coordinate.x() - x;
                var dy = coordinate.y() - y;

                if (dx > 0) {
                    moveDTOS.add(new CommandDTO(Direction.EAST, dx));
                }

                if (dx < 0) {
                    moveDTOS.add(new CommandDTO(Direction.WEST, dx * -1));
                }

                if (dy > 0) {
                    moveDTOS.add(new CommandDTO(Direction.NORTH, dy));
                }

                if (dy < 0) {
                    moveDTOS.add(new CommandDTO(Direction.SOUTH, dy * -1));
                }
            }

            x = coordinate.x();
            y = coordinate.y();
            first = false;
        }

        var apiCallLog = new ApiCallLog();
        apiCallLog.setCommand("moves");

        var input = coordinateDTOS.stream().map(Record::toString).collect(Collectors.joining(" - ", "", ""));
        var output = moveDTOS.stream().map(Record::toString).collect(Collectors.joining(" - ", "", ""));

        apiCallLog.setInput(input);
        apiCallLog.setOutput(output);

        apiCallLogRepository.save(apiCallLog);

        return moveDTOS;
    }

    @PostMapping("/locations")
    public List<CoordinateDTO> coordinateList(@RequestBody List<CommandDTO> commandDTOS) {

        var coordinateDTOS = new ArrayList<CoordinateDTO>();

        var x = 0;
        var y = 0;

        coordinateDTOS.add(new CoordinateDTO(x, y));

        for (var command : commandDTOS) {
            switch (command.direction()) {
                case EAST -> x = x + command.steps();
                case NORTH -> y = y + command.steps();
                case SOUTH -> y = y - command.steps();
                case WEST -> x = x - command.steps();
            }

            coordinateDTOS.add(new CoordinateDTO(x, y));
        }

        var apiCallLog = new ApiCallLog();
        apiCallLog.setCommand("locations");

        var input = commandDTOS.stream().map(Record::toString).collect(Collectors.joining(" - ", "", ""));
        var output = coordinateDTOS.stream().map(Record::toString).collect(Collectors.joining(" - ", "", ""));

        apiCallLog.setInput(input);
        apiCallLog.setOutput(output);

        apiCallLogRepository.save(apiCallLog);

        return coordinateDTOS;
    }

}
