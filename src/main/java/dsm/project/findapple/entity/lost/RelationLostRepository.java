package dsm.project.findapple.entity.lost;

import dsm.project.findapple.payload.response.FindResponse;
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
@Slf4j
@RequiredArgsConstructor
public class RelationLostRepository {

    private final NamedParameterJdbcTemplate template;
    private final MyQueries myQueries;
    private final RowMapper<FindResponse> mapper;

    @Transactional
    public List<FindResponse> findAllByRelation(String addQuery, Page page) {
        String sql = myQueries.getFindAllByRelation() + addQuery
                + "ORDER BY l.write_at, l.find_at DESC LIMIT :limit OFFSET :offset";

        log.info(sql);

        Map<String, Object> params = new HashMap<>();
        params.put("limit", page.getLimit());
        params.put("offset", page.getOffset());

        return template.queryForStream(sql, params, mapper).collect(Collectors.toList());
    }
}
