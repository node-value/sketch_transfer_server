package node_value.project.sketch_transfer_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserDTO {
    @NonNull private String name, password;
}