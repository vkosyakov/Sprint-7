import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListOrders {

    private List<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

}
