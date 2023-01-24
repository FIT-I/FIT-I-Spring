package fit.fitspring.controller.dto.communal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SliceResDto<D> {
    private int numberOfElements;
    private boolean hasNext;
    private List<D> dto;
}
