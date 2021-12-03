package dsm.project.findapple.entity.find;

import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.utils.MyQueries;
import dsm.project.findapple.utils.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RelationFindRepository {

    private final NamedParameterJdbcTemplate template;
    private final MyQueries myQueries;
    private final RowMapper<LostResponse> mapper;

    @Transactional
    public List<LostResponse> findAllByRelation(String addQuery, Page page) {
        String sql = myQueries.getFindLostAllByRelation() + addQuery
                + "ORDER BY l.write_at, l.lost_at DESC LIMIT :limit OFFSET :offset";

        log.info(sql);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", page.getOffset());
        paramMap.put("limit", page.getLimit());

        return template.queryForStream(sql, paramMap, mapper).collect(Collectors.toList());
    }
}
