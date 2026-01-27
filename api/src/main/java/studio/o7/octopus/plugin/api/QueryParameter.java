package studio.o7.octopus.plugin.api;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueryParameter {

    private String keyPattern;
    private String dataFilter;

    private boolean includeExpired;

    private int page;
    private int pageSize;

    private com.google.protobuf.Timestamp createdAtStart;
    private com.google.protobuf.Timestamp createdAtEnd;

}
