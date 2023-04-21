package node_value.project.sketch_transfer_server.dto;

import lombok.NonNull;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AuthResponse {
    @NonNull private String token;
}