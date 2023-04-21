package node_value.project.sketch_transfer_server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import node_value.project.sketch_transfer_server.util.ProjectDataMsgType;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProjectDataDTO {
    @NonNull ProjectDataMsgType type;
    @NonNull String receiver, data;
    
}
