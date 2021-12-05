package dsm.project.findapple.utils;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class CustomRowMapper {

    @Component
    static class RelationLostMapper implements RowMapper<FindResponse> {

        @Override
        @SneakyThrows
        public FindResponse mapRow(ResultSet rs, int rowNum) {
            Long lostId = rs.getLong("findId");
            String title = rs.getString("title");
            Category category = Category.valueOf(rs.getString("category"));
            String detail = rs.getString("detail");
            String profileUrl = rs.getString("profileUrl");
            Double longitude = rs.getDouble("longitude");
            Double latitude = rs.getDouble("latitude");
            LocalDate writeAt = rs.getDate("writeAt").toLocalDate();
            LocalDateTime lostAt = rs.getDate("findAt")
                    .toInstant()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toLocalDateTime();
            Long kakaoId = rs.getLong("kakaoId");
            String lostUser = rs.getString("lostUser");

            return FindResponse.builder()
                    .category(category)
                    .detail(detail)
                    .kakaoId(kakaoId)
                    .title(title)
                    .latitude(latitude)
                    .longitude(longitude)
                    .findAt(lostAt)
                    .findUser(lostUser)
                    .writeAt(writeAt)
                    .findId(lostId)
                    .profileUrl(profileUrl)
                    .build();
        }
    }

    @Component
    static class RelationFindMapper implements RowMapper<LostResponse> {

        @Override
        @SneakyThrows
        public LostResponse mapRow(ResultSet rs, int rowNum) {
            Long lostId = rs.getLong("lostId");
            String title = rs.getString("title");
            Category category = Category.valueOf(rs.getString("category"));
            String detail = rs.getString("detail");
            Double longitude = rs.getDouble("longitude");
            Double latitude = rs.getDouble("latitude");
            LocalDate writeAt = rs.getDate("writeAt").toLocalDate();
            String profileUrl = rs.getString("profileUrl");
            LocalDateTime lostAt = rs.getDate("lostAt")
                    .toInstant()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toLocalDateTime();
            Long kakaoId = rs.getLong("kakaoId");
            String lostUser = rs.getString("lostUser");

            return LostResponse.builder()
                    .lostId(lostId)
                    .writeAt(writeAt)
                    .lostUser(lostUser)
                    .lostAt(lostAt)
                    .longitude(longitude)
                    .latitude(latitude)
                    .title(title)
                    .detail(detail)
                    .category(category)
                    .kakaoId(kakaoId)
                    .profileUrl(profileUrl)
                    .build();
        }
    }
}
