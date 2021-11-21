package dsm.project.findapple.service.promise;

import dsm.project.findapple.payload.request.PromiseRequest;
import dsm.project.findapple.payload.request.UpdatePromiseRequest;
import dsm.project.findapple.payload.response.PromiseResponse;
import dsm.project.findapple.payload.response.SendPromiseResponse;

import java.util.List;

public interface PromiseService {
    SendPromiseResponse sendPromise(String token, String chatId, PromiseRequest promiseRequest);
    List<PromiseResponse> getMyPromises(String token);
    PromiseResponse getPromise(String token, Long promiseId);
    void acceptPromise(String token, Long promiseId, Boolean isAccept);
    void updatePromise(String token, Long promiseId, String chatId, UpdatePromiseRequest updatePromiseRequest);
}
