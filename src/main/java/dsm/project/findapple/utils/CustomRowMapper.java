package dsm.project.findapple.utils;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.response.LostResponse;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class CustomRowMapper {

    @Component
    static class RelationLostMapper implements RowMapper<LostResponse> {

        @Override
        @SneakyThrows
        public LostResponse mapRow(ResultSet rs, int rowNum) {
            Long lostId = rs.getLong("lostId");
            String title = rs.getString("title");
            Category category = Category.values()[rs.getInt("category")];
            String detail = rs.getString("detail");
            Double longitude = rs.getDouble("longitude");
            Double latitude = rs.getDouble("latitude");
            LocalDate writeAt = rs.getDate("writeAt").toLocalDate();
            LocalDateTime lostAt = rs.getDate("lostAt")
                    .toInstant()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toLocalDateTime();
            Long kakaoId = rs.getLong("kakaoId");
            String lostUser = rs.getString("lostUser");

            return LostResponse.builder()
                    .category(category)
                    .detail(detail)
                    .kakaoId(kakaoId)
                    .title(title)
                    .latitude(latitude)
                    .longitude(longitude)
                    .lostAt(lostAt)
                    .lostUser(lostUser)
                    .writeAt(writeAt)
                    .lostId(lostId)
                    .build();
        }
    }
}
