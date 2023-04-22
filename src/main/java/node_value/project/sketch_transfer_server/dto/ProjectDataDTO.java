package node_value.project.sketch_transfer_server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProjectDataDTO {
    @NonNull String sender, receiver, data;
    
}
