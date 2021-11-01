package dsm.project.findapple.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MyQueries {
    @Value("${find.all.by.relation.find}")
    private String findAllByRelation;
}
