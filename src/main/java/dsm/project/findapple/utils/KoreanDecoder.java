package dsm.project.findapple.utils;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KoreanDecoder {

    public List<String> decodeKorean(String title) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult komoranResult = komoran.analyze(title);

        return komoranResult.getMorphesByTags(new ArrayList<>(Arrays.asList("NNP", "NNG")));
    }
}
