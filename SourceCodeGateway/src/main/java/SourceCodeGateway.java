import java.util.List;

public interface SourceCodeGateway {
    void connect(String url, String user, String password);

    void disconnect();

    List<String> selectApplicationsWhereComponent(String component);
}
