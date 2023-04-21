package node_value.project.sketch_transfer_server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data @Builder 
public class UserDTO {
    @NonNull private String name, password;
}