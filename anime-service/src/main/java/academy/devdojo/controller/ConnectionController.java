package academy.devdojo.controller;

import academy.devdojo.config.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/connections")
public class ConnectionController {
    private final Connection connection;

    @GetMapping
    public ResponseEntity<Connection> getConnections() {
        return ResponseEntity.ok(connection);
    }
}
