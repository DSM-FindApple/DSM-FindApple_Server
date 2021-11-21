package dsm.project.findapple.controller;

import dsm.project.findapple.payload.request.PromiseRequest;
import dsm.project.findapple.payload.request.UpdatePromiseRequest;
import dsm.project.findapple.payload.response.PromiseResponse;
import dsm.project.findapple.payload.response.SendPromiseResponse;
import dsm.project.findapple.service.promise.PromiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promise")
@RequiredArgsConstructor
public class PromiseController {

    private final PromiseService promiseService;

    @GetMapping
    public List<PromiseResponse> getMyPromises(@RequestHeader("Authorization") String token) {
        return promiseService.getMyPromises(token);
    }

    @GetMapping("/{promiseId}")
    public PromiseResponse getPromise(@PathVariable Long promiseId,
                                      @RequestHeader("Authorization") String token) {
        return promiseService.getPromise(token, promiseId);
    }

    @PostMapping("/{chatId}")
    public SendPromiseResponse sendPromise(@PathVariable String chatId,
                                           @RequestHeader("Authorization") String token,
                                           @RequestBody PromiseRequest promiseRequest) {
        return promiseService.sendPromise(token, chatId, promiseRequest);
    }

    @PutMapping("/{chatId}/{promiseId}")
    public void updatePromise(@RequestHeader("Authorization") String token,
                              @PathVariable String chatId,
                              @PathVariable Long promiseId,
                              @RequestBody UpdatePromiseRequest updatePromiseRequest) {
        promiseService.updatePromise(token, promiseId, chatId, updatePromiseRequest);
    }

    @PutMapping("/{promiseId}")
    public void acceptPromise(@PathVariable Long promiseId,
                              @RequestHeader("Authorization") String token,
                              @RequestParam Boolean isAccept) {
        promiseService.acceptPromise(token, promiseId, isAccept);
    }
}
